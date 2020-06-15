package com.Quant.quantmasters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Competitive_Test extends Activity {
    ProgressBar progressBar;
    private DatabaseHandler db;
    private SessionManager session;
    RecyclerView recycler_views;
    private List<Question_List> question_lists = new ArrayList<Question_List>();
    Test_Question_Adapter test_question_adapter;
    String group_id, name;
    private Marks_Database md;
    private ProgressDialog pDialog;
    TextView _tv;
    int time;
    TextView submit;
    private Answer_Database ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        Intent intent = getIntent();
        pDialog = new ProgressDialog(this);
        name = intent.getExtras().getString("paper_title");
        group_id = intent.getExtras().getString("paper_id");
        time = intent.getExtras().getInt("time_lim");
        Log.d("ids", group_id);
        submit = findViewById(R.id.submit);
        db = new DatabaseHandler(this);
        TextView chapter_name = findViewById(R.id.chapter_name);
        md = new Marks_Database(this);
        Typeface bold = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface fontz = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        _tv = (TextView) findViewById(R.id.timer);
        _tv.setTypeface(bold);
        ad = new Answer_Database(this);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> user = md.getUserDetails();
                String marks = user.get("email");
                HashMap<String, String> userz = db.getUserDetails();
                String email = userz.get("email");
                String token = userz.get("token");
                Submit(token, email, marks);
            }
        });


        TextPaint paint = chapter_name.getPaint();
        float width = paint.measureText(name);
        Shader textShader = new LinearGradient(0, 0, width, chapter_name.getTextSize(),
                new int[]{
                        Color.parseColor("#177ED5"),
                        Color.parseColor("#16B1E9"),
                }, null, Shader.TileMode.CLAMP);
        chapter_name.getPaint().setShader(textShader);

        chapter_name.setTypeface(bold);
        TextPaint paints = _tv.getPaint();
        float widths = paints.measureText("Timer");
        Shader textShaders = new LinearGradient(0, 0, widths, _tv.getTextSize(),
                new int[]{
                        Color.parseColor("#E46478"),
                        Color.parseColor("#FB933A"),
                }, null, Shader.TileMode.CLAMP);
        _tv.getPaint().setShader(textShaders);
        submit.setTypeface(bold);

        TextPaint paintz = submit.getPaint();
        float widthz = paintz.measureText("Submit");
        Shader textShaderz = new LinearGradient(0, 0, widthz, submit.getTextSize(),
                new int[]{
                        Color.parseColor("#E46478"),
                        Color.parseColor("#FB933A"),
                }, null, Shader.TileMode.CLAMP);
        submit.getPaint().setShader(textShaderz);

        chapter_name.setText(name);


        recycler_views = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_views.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Competitive_Test.this, LinearLayoutManager.VERTICAL, false);
        recycler_views.setLayoutManager(mLayoutManager);
        recycler_views.setHasFixedSize(true);


        HashMap<String, String> user = db.getUserDetails();
        String tokens = user.get("token");
        Load(tokens);

        test_question_adapter = new Test_Question_Adapter(this, question_lists);
        recycler_views.setItemAnimator(new DefaultItemAnimator());
        recycler_views.setAdapter(test_question_adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                md.deleteall();
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        md.deleteall();
        finish();
    }


    private void Load(final String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/test/competitive/paper/" + group_id,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONArray feedArray = new JSONArray(response);
                            ad.deleteall();
                            for (int i = 0; i < feedArray.length(); i++) {

                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Question_List question_list = new Question_List();
                                int num = i;
                                num = num + 1;
                                if (i == 0) {
                                    md.insertData(1, feedObj.getString("paper_id"), "0");
                                }
                                ad.insertData(String.valueOf(i),String.valueOf(0));
                                question_list.Set_Id(feedObj.getString("paper_id"));
                                question_list.Set_Question(feedObj.getString("question"));
                                question_list.Set_Option_One(feedObj.getString("opt_1"));
                                question_list.Set_Option_Two(feedObj.getString("opt_2"));
                                question_list.Set_Option_Three(feedObj.getString("opt_3"));
                                question_list.Set_Option_Four(feedObj.getString("opt_4"));
                                question_list.Set_Option_Five(feedObj.getString("opt_5"));
                                question_list.Set_Solution(feedObj.getInt("correct_opt"));
                                question_list.Set_year(feedObj.getString("question_origin"));
                                question_list.Set_One(0);
                                question_list.Set_Two(0);
                                question_list.Set_Three(0);
                                question_list.Set_Four(0);
                                question_list.Set_Five(0);
                                question_list.setFlag(0);
                                question_list.setRight_flag(0);


                                question_lists.add(question_list);
                                progressBar.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
                            }
                            test_question_adapter.notifyDataSetChanged();
                            if (time == 0) {
                                _tv.setVisibility(View.GONE);

                            } else {
                                new CountDownTimer(time, 1000) { // adjust the milli seconds here

                                    public void onTick(long millisUntilFinished) {
                                        _tv.setText("" + String.format("%d:%d",
                                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                                    }

                                    public void onFinish() {
                                        HashMap<String, String> user = md.getUserDetails();
                                        String marks = user.get("email");
                                        HashMap<String, String> userz = db.getUserDetails();
                                        String email = userz.get("email");
                                        String token = userz.get("token");
                                        Submit(token, email, marks);
                                    }
                                }.start();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                new LovelyStandardDialog(Competitive_Test.this)
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

    private void Submit(final String token, final String email, final String marks) {
        pDialog.setMessage("Submitting Result..");
        pDialog.setCancelable(false);
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/test/competitive/submit/marks",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);

                            Log.d("res", response);
                            if (jObj.getString("text").equals("Answer Submitted")) {
                                hideDialog();
                                Intent intent = new Intent(Competitive_Test.this, Test_Done.class);
                                intent.putExtra("total_score", marks);
                                intent.putExtra("total_score", marks);
                                intent.putExtra("paper_id", group_id);
                                intent.putExtra("paper_title", name);
                                intent.putExtra("paper_type", 4);
                                md.deleteall();
                                startActivity(intent);
                                finish();

                            } else {
                                hideDialog();
                                new LovelyStandardDialog(Competitive_Test.this)
                                        .setButtonsColorRes(R.color.red)
                                        .setTitle("Error")
                                        .setMessage("Something Went Wrong..Retry")
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new LovelyStandardDialog(Competitive_Test.this)
                                .setButtonsColorRes(R.color.red)
                                .setTitle("Error")
                                .setMessage("Something Went Wrong...Retry")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .show();
                        hideDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("paper_id", group_id);
                params.put("email", email);
                params.put("marks", marks);
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
