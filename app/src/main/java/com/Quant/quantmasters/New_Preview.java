package com.Quant.quantmasters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class New_Preview extends AppCompatActivity {
    ProgressBar progressBar2;
    private DatabaseHandler db;
    RecyclerView recycler_views;
    private List<Video_List> video_lists = new ArrayList<Video_List>();
    Video_List_Adapter video_list_adapter;
    ProgressBar progressBar;
    String videoid;
    String token, grp, thumb, rating;
    private ProgressDialog pDialog;
    private static final String TAG = Preview_Activity.class.getSimpleName();
    String email;
    int flag;
    TextView resource;
    ImageView Play_arrow;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_preview);
        progressBar2 = (ProgressBar) findViewById(R.id.spin_kit2);
        Intent intent = getIntent();
        videoid = Objects.requireNonNull(intent.getExtras()).getString("v_id");
        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "sourcesans.otf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        grp = intent.getExtras().getString("v_gr");
        thumb = intent.getExtras().getString("thumb");
        String titl = intent.getExtras().getString("video_title");
        flag = intent.getExtras().getInt("flag");
        rating = intent.getExtras().getString("rating");
        TextView comment = findViewById(R.id.comment);
        TextView rate_count = findViewById(R.id.rate_count);
        Play_arrow = findViewById(R.id.Play_arrow);

         comment.setTypeface(typeface1);

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(New_Preview.this, Comment.class);
                intent.putExtra("v_id", videoid);
                startActivity(intent);
            }
        });



        if (!rating.equals("0")) {
            if (rating.length() > 4) {
                String rt = rating.substring(0, 3);
                rate_count.setText(rt);
            } else {
                rate_count.setText(rating);
            }

        } else {
            rate_count.setText("");
        }

        rate_count.setTypeface(typeface1);

        ImageView star1, star2, star3, star4, star5;

        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);


        db = new DatabaseHandler(this);
        title = findViewById(R.id.title);
        progressBar2.setVisibility(View.VISIBLE);
        TextPaint paint = title.getPaint();

        int firstDigit = Integer.parseInt(rating.substring(0, 1));

        switch (firstDigit) {
            case 0:

                break;
            case 1:
                star2.setColorFilter(ContextCompat.getColor(this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                star3.setColorFilter(ContextCompat.getColor(this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                star4.setColorFilter(ContextCompat.getColor(this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                star5.setColorFilter(ContextCompat.getColor(this, R.color.unlike), PorterDuff.Mode.SRC_IN);

                break;
            case 2:
                star3.setColorFilter(ContextCompat.getColor(this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                star4.setColorFilter(ContextCompat.getColor(this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                star5.setColorFilter(ContextCompat.getColor(this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                break;
            case 3:


                star4.setColorFilter(ContextCompat.getColor(this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                star5.setColorFilter(ContextCompat.getColor(this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                break;
            case 4:
                star5.setColorFilter(ContextCompat.getColor(this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                break;
            case 5:
                break;
        }

        float width = paint.measureText(titl);
        title.setTypeface(typeface);
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#FA8432"),
                        Color.parseColor("#FDBE40"),
                }, null, Shader.TileMode.CLAMP);
        // title.getPaint().setShader(textShader);

        TextView download = findViewById(R.id.download);
        ImageView image = findViewById(R.id.image);
        title.setText(titl);
        resource = findViewById(R.id.resource);
        resource.setTypeface(typeface);
        TextView wish_list = findViewById(R.id.wish_list);
        download.setTypeface(typeface);
        wish_list.setTypeface(typeface);
        if (flag == 0) {
            Play_arrow.setImageResource(R.drawable.ic_password);
            download.setText("Download Notes (Locked)");
            resource.setText("Resource Locked");
            wish_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(New_Preview.this, Payment_Activity.class);
                    startActivity(intent);

                }
            });
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(New_Preview.this, Payment_Activity.class);
                    startActivity(intent);

                }
            });
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(New_Preview.this, Payment_Activity.class);
                    startActivity(intent);

                }
            });

        } else {
            Play_arrow.setImageResource(R.drawable.ic_play_arrow);
            resource.setText("Play Video");
            download.setText("Download Notes");
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pDialog = new ProgressDialog(New_Preview.this);
                    pDialog.setCancelable(false);
                    HashMap<String, String> user = db.getUserDetails();
                    final String token = user.get("token");
                    final String vid = videoid;
                    pDialog.setMessage("Loading..");
                    showDialog();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/videos/" + vid + "/link",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    final Intent intent = new Intent(New_Preview.this, VideoPlayerActivity.class);
                                    intent.putExtra("v_link", response);
                                    intent.putExtra("v_id", videoid);
                                    intent.putExtra("type", "group");
                                    final Handler h = new Handler();
                                    h.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideDialog();
                                            startActivity(intent);

                                        }
                                    }, 1000);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
                            new LovelyStandardDialog(New_Preview.this)
                                    .setButtonsColorRes(R.color.text_color)
                                    .setTitle("Error")
                                    .setMessage("Something Went Wrong...Retry")
                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

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
                    RequestQueue requestQueue = Volley.newRequestQueue(New_Preview.this);
                    requestQueue.add(stringRequest);


                }
            });

            wish_list.setVisibility(View.GONE);

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    pDialog = new ProgressDialog(New_Preview.this);
                    pDialog.setCancelable(false);
                    HashMap<String, String> user = db.getUserDetails();
                    final String token = user.get("token");
                    final String vid = videoid;
                    pDialog.setMessage("Loading..");
                    Log.d("Xesponse", "good");
                    showDialog();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/videos/" + vid + "/dl-notes",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    hideDialog();
                                    Intent intent = new Intent();
                                    intent.setDataAndType(Uri.parse("https://quantmasters.in/" + response), "application/pdf");
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideDialog();
                            VolleyLog.d("Error", "Error: " + error.getMessage());
                            new LovelyStandardDialog(New_Preview.this)
                                    .setButtonsColorRes(R.color.text_color)
                                    .setTitle("Error")
                                    .setMessage("Something Went Wrong...Retry")
                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
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
                    RequestQueue requestQueue = Volley.newRequestQueue(New_Preview.this);
                    requestQueue.add(stringRequest);
                }


            });
        }


        db = new DatabaseHandler(getApplicationContext());


        Glide.with(this)
                .load("https://quantmasters.in" + thumb)
                .fitCenter()
                .skipMemoryCache(true)
                .into(image);

        recycler_views = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_views.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(New_Preview.this, LinearLayoutManager.VERTICAL, false);
        recycler_views.setLayoutManager(mLayoutManager);
        recycler_views.setHasFixedSize(true);


        video_list_adapter = new Video_List_Adapter(this, video_lists);
        recycler_views.setItemAnimator(new DefaultItemAnimator());
        recycler_views.setAdapter(video_list_adapter);


        HashMap<String, String> users = db.getUserDetails();
        String tokens = users.get("token");
        email = users.get("email");
        Load(tokens);


    }


    private void Load(final String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/videos/all",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Responsesss", response);
                        try {

                            JSONArray feedArray = new JSONArray(response);
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Video_List video_list = new Video_List();
                                if (grp.equals(feedObj.getString("video_group")) && !videoid.equals(feedObj.getString("video_id")) && !videoid.equals("0")) {
                                    video_list.Set_Id(feedObj.getString("video_id"));
                                    video_list.Set_Name(feedObj.getString("video_name"));
                                    video_list.Set_Description(feedObj.getString("description"));
                                    video_list.Set_Image(feedObj.getString("thumbnail"));
                                    video_list.Set_Group(feedObj.getString("video_group"));
                                    video_list.setFlag(feedObj.getInt("public"));
                                    video_list.Set_Length(feedObj.getString("length"));
                                    video_list.setRating(feedObj.getString("rating"));
                                    video_lists.add(video_list);

                                }
                            }
                            progressBar2.setVisibility(View.GONE);
                            video_list_adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                new LovelyStandardDialog(New_Preview.this)
                        .setButtonsColorRes(R.color.text_color)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("Something Went Wrong...Retry")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressBar.setVisibility(View.VISIBLE);
                                HashMap<String, String> user = db.getUserDetails();
                                String tokens = user.get("token");
                                Load(tokens);

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


    @Override
    public void onBackPressed() {
        finish();
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