package com.Quant.quantmasters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;


import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Shreyas on 09-04-2017.
 */

public class Comment extends AppCompatActivity {
    Comment_Adapter comment_adapter;
    private List<Comment_Feed> comment_feeds = new ArrayList<Comment_Feed>();
    private ProgressDialog pDialog;
    ProgressBar progressBar;
    String postid;
    public EditText comment;
    TextView title, error_text;
    private DatabaseHandler db;
    String videoid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);

        Intent intent = getIntent();
        videoid = Objects.requireNonNull(intent.getExtras()).getString("v_id");

        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "sourcesans.otf");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        comment = (EditText) findViewById(R.id.Full_Name);
        error_text = findViewById(R.id.error_text);
        comment.setTypeface(typeface1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);


        progressBar = (ProgressBar) findViewById(R.id.spin_kit2);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        comment_adapter = new Comment_Adapter(Comment.this, comment_feeds);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(comment_adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);
                finish();
            }
        });
        title = findViewById(R.id.title);
        TextPaint paint = title.getPaint();
        title.setTypeface(typeface);
        error_text.setTypeface(typeface2);

        float width = paint.measureText("Comment");
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#177ED5"),
                        Color.parseColor("#16B1E9"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);


        ImageView imageView = (ImageView) findViewById(R.id.send);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usercomment = comment.getText().toString().trim();
                String Encoded = StringEscapeUtils.escapeJava(usercomment);


                if (!Encoded.isEmpty()) {
                    HashMap<String, String> users = db.getUserDetails();
                    String tokens = users.get("token");
                    String email = users.get("email");
                    Post_Comment(videoid, tokens, email, Encoded);

                } else {
                    Toast.makeText(getApplicationContext(),
                            "please fill the comment box", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        db = new DatabaseHandler(getApplicationContext());


        HashMap<String, String> users = db.getUserDetails();
        String tokens = users.get("token");
        Load_Comment(videoid, tokens);


    }


    private void Load_Comment(final String video_id, final String token) {
        comment_feeds.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/video/" + video_id + "/comments",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mess", response);

                        try {
                            JSONArray feedArray = new JSONArray(response);
                            Log.d("mess", String.valueOf(feedArray.length()));
                            if (feedArray.length() > 0) {
                                for (int i = 0; i < feedArray.length(); i++) {


                                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                                    Comment_Feed feeds = new Comment_Feed();
                                    feeds.Set_Comment_Id(feedObj.getString("comment_id"));
                                    feeds.Set_Comment(feedObj.getString("text"));
                                    feeds.Set_User_Name(feedObj.getString("created_by"));
                                    feeds.Set_Time(feedObj.getString("created_at"));
                                    feeds.Set_Video_Id(videoid);

                                    Log.d("rep", feedObj.getString("replies"));
                                    JSONArray genreArry = feedObj.getJSONArray("replies");
                                    if (genreArry.length() > 0) {
                                        feeds.Set_Count(genreArry.length());
                                    } else {
                                        feeds.Set_Count(0);
                                    }

                                    feeds.Set_Replies(feedObj.getString("replies"));

                                    comment_feeds.add(feeds);
                                    progressBar.setVisibility(View.GONE);
                                }
                                comment_adapter.notifyDataSetChanged();
                            } else {
                                error_text.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        new LovelyStandardDialog(Comment.this)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Error")
                                .setMessage("Something Went Wrong...Retry")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        Load_Comment(videoid, token);
                                    }
                                })
                                .show();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Content-Type", "application/json");
                headerMap.put("Authorization", "Bearer " + token);
                return headerMap;
            }

        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void Post_Comment(final String video_id, final String token, final String email, final String commentz) {
        pDialog.setMessage("Posting...");
        pDialog.setCancelable(false);
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/learn/video/" + video_id + "/comment",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mess", response);
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.has("msg")) {
                                hideDialog();
                                if (jObj.getString("msg").equals("commented added")) {
                                    comment.setText("");
                                    hideDialog();
                                    HashMap<String, String> users = db.getUserDetails();
                                    String tokens = users.get("token");
                                    Load_Comment(videoid, tokens);


                                } else {
                                    hideDialog();
                                    new LovelyStandardDialog(Comment.this)
                                            .setButtonsColorRes(R.color.text_color)
                                            .setTitle("Error")
                                            .setMessage("Something Went Wrong..Please Retry")
                                            .setCancelable(false)
                                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            })
                                            .show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new LovelyStandardDialog(Comment.this)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Error")
                                .setCancelable(false)
                                .setMessage("Something Went Wrong...Retry")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        hideDialog();
                                        Post_Comment(videoid, token, email, commentz);
                                    }
                                })
                                .show();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Authorization", "Bearer " + token);
                return headerMap;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("text", commentz);
                params.put("email", email);
                return params;
            }

        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}

