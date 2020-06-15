package com.Quant.quantmasters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Answer extends AppCompatActivity {
    ProgressBar progressBar;

    private DatabaseHandler db;
    RecyclerView recycler_views;
    private List<Question_List> question_lists = new ArrayList<Question_List>();
    New_Answer_Adapter test_question_adapter;
    String group_id;
    private ProgressDialog pDialog;
    int flag;
    Answer_Database ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        pDialog = new ProgressDialog(Answer.this);
        Intent intent = getIntent();
        Typeface bold = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface fontz = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        String name = Objects.requireNonNull(intent.getExtras()).getString("paper_title");
        group_id = intent.getExtras().getString("paper_id");
        flag = intent.getExtras().getInt("paper_type");
        Log.d("Paper id", group_id);
        db = new DatabaseHandler(this);
        ab = new Answer_Database(this);
        TextView chapter_name = findViewById(R.id.chapter_name);

     //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        HashMap<String, String> user = db.getUserDetails();
        String tokens = user.get("token");
        switch (flag) {
            case 1:
                Load(tokens);
                break;
            case 2:
                Load_Mq(tokens);
                break;
            case 3:
                Load_Cp(tokens);
                break;
            case 4:
                Load_CT(tokens);
                break;
            case 5:
                Load_FT(tokens);
                break;
        }

        TextPaint paint = chapter_name.getPaint();
        float width = paint.measureText(name);
        Shader textShader = new LinearGradient(0, 0, width, chapter_name.getTextSize(),
                new int[]{
                        Color.parseColor("#FF03B94D"),
                        Color.parseColor("#FF039742"),
                }, null, Shader.TileMode.CLAMP);
        chapter_name.getPaint().setShader(textShader);

        chapter_name.setTypeface(bold);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        chapter_name.setText(name);
        recycler_views = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_views.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Answer.this, LinearLayoutManager.VERTICAL, false);
        recycler_views.setLayoutManager(mLayoutManager);
        recycler_views.setHasFixedSize(true);
        test_question_adapter = new New_Answer_Adapter(this, question_lists);
        recycler_views.setItemAnimator(new DefaultItemAnimator());
        recycler_views.setAdapter(test_question_adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ab.deleteall();
                finish();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        ab.deleteall();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ab.deleteall();
    }


    @Override
    public void onBackPressed() {
        ab.deleteall();
        finish();
    }


    private void Load(final String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/test/progression/paper/new/" + group_id,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject feedObjs = new JSONObject(response);
                            JSONArray feedArray =feedObjs.getJSONArray("questions");
                            for (int i = 0; i < feedArray.length(); i++) {

                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Question_List question_list = new Question_List();
                                HashMap<String, String> user = ab.getAnswer(i);
                                String user_answer = user.get("name");
                                question_list.Set_Id(feedObj.getString("paper_id"));
                                question_list.Set_Question(feedObj.getString("question"));
                                question_list.Set_Option_One(feedObj.getString("opt_1"));
                                question_list.Set_Option_Two(feedObj.getString("opt_2"));
                                question_list.Set_Option_Three(feedObj.getString("opt_3"));
                                question_list.Set_Option_Four(feedObj.getString("opt_4"));
                                question_list.Set_Option_Five(feedObj.getString("opt_5"));
                                question_list.Set_Solution(feedObj.getInt("correct_opt"));
                                question_list.Set_Method(feedObj.getString("explanation"));
                                question_list.setClick(0);
                                if (user_answer!=null) {
                                    question_list.Set_User_Answer(Integer.parseInt(user_answer));
                                    Log.d("right answer",user_answer);
                                }else{
                                    question_list.Set_User_Answer(0);
                                }

                                switch (feedObj.getInt("correct_opt")) {
                                    case 1:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_1"));
                                        break;
                                    case 2:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_2"));
                                        break;
                                    case 3:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_3"));
                                        break;
                                    case 4:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_4"));
                                        break;
                                    case 5:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_5"));
                                        break;
                                    default:
                                        question_list.Set_Right_Answer("Not Available");
                                        break;
                                }


                                question_lists.add(question_list);
                                progressBar.setVisibility(View.GONE);
                            }


                            test_question_adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new LovelyStandardDialog(Answer.this)
                        .setButtonsColorRes(R.color.text_color)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("Something Went Wrong...Retry")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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


    private void Load_Mq(final String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/test/question/ret/" + group_id,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("Response", response);
                        try {
                            JSONObject feedObject = new JSONObject(response);
                            JSONArray feedArray = feedObject.getJSONArray("questions");
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Question_List question_list = new Question_List();
                                HashMap<String, String> user = ab.getAnswer(i);
                                String user_answer = user.get("name");
                                if (user_answer!=null) {
                                    question_list.Set_User_Answer(Integer.parseInt(user_answer));
                                    Log.d("right answer",user_answer);
                                }else{
                                    question_list.Set_User_Answer(0);
                                }
                                question_list.Set_Id(feedObj.getString("paper_id"));
                                question_list.Set_Question(feedObj.getString("question"));
                                question_list.Set_Option_One(feedObj.getString("option_1"));
                                question_list.Set_Option_Two(feedObj.getString("option_2"));
                                question_list.Set_Option_Three(feedObj.getString("option_3"));
                                question_list.Set_Option_Four(feedObj.getString("option_4"));
                                question_list.Set_Solution(feedObj.getInt("correct_opt"));
                                question_list.Set_Method(feedObj.getString("explanation"));
                                question_list.setClick(0);

                                switch (feedObj.getInt("correct_opt")) {
                                    case 1:
                                        question_list.Set_Right_Answer(feedObj.getString("option_1"));
                                        break;
                                    case 2:
                                        question_list.Set_Right_Answer(feedObj.getString("option_2"));
                                        break;
                                    case 3:
                                        question_list.Set_Right_Answer(feedObj.getString("option_3"));
                                        break;
                                    case 4:
                                        question_list.Set_Right_Answer(feedObj.getString("option_4"));
                                        break;
                                    case 5:
                                        question_list.Set_Right_Answer(feedObj.getString("option_5"));
                                        break;
                                    default:
                                        question_list.Set_Right_Answer("Not Available");
                                        break;
                                }


                                question_lists.add(question_list);
                                progressBar.setVisibility(View.GONE);

                            }
                            test_question_adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new LovelyStandardDialog(Answer.this)
                        .setButtonsColorRes(R.color.text_color)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("Something Went Wrong...Retry")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, String> user = db.getUserDetails();
                                String tokens = user.get("token");
                                Load_Mq(tokens);
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


    private void Load_Cp(final String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/test/company/paper/new/" + group_id,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject feedObjs = new JSONObject(response);
                            JSONArray feedArray =feedObjs.getJSONArray("questions");
                            for (int i = 0; i < feedArray.length(); i++) {

                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Question_List question_list = new Question_List();
                                HashMap<String, String> user = ab.getAnswer(i);
                                String user_answer = user.get("name");
                                if (user_answer!=null) {
                                    question_list.Set_User_Answer(Integer.parseInt(user_answer));
                                    Log.d("right answer",user_answer);
                                }else{
                                    question_list.Set_User_Answer(0);
                                }
                                question_list.Set_Id(feedObj.getString("paper_id"));
                                question_list.Set_Question(feedObj.getString("question"));
                                question_list.Set_Option_One(feedObj.getString("opt_1"));
                                question_list.Set_Option_Two(feedObj.getString("opt_2"));
                                question_list.Set_Option_Three(feedObj.getString("opt_3"));
                                question_list.Set_Option_Four(feedObj.getString("opt_4"));
                                question_list.Set_Option_Five(feedObj.getString("opt_5"));
                                question_list.Set_Solution(feedObj.getInt("correct_opt"));
                                question_list.Set_Method(feedObj.getString("explanation"));
                                question_list.setClick(0);
                                switch (feedObj.getInt("correct_opt")) {
                                    case 1:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_1"));
                                        break;
                                    case 2:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_2"));
                                        break;
                                    case 3:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_3"));
                                        break;
                                    case 4:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_4"));
                                        break;
                                    case 5:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_5"));
                                        break;
                                    default:
                                        question_list.Set_Right_Answer("Not Available");
                                        break;
                                }

                                question_lists.add(question_list);
                                progressBar.setVisibility(View.GONE);

                            }
                            test_question_adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new LovelyStandardDialog(Answer.this)
                        .setButtonsColorRes(R.color.text_color)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("Something Went Wrong...Retry")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, String> user = db.getUserDetails();
                                String tokens = user.get("token");
                                Load_Mq(tokens);
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


    private void Load_CT(final String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/test/competitive/paper/new/" + group_id,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject feedObjs = new JSONObject(response);
                            JSONArray feedArray =feedObjs.getJSONArray("questions");
                            for (int i = 0; i < feedArray.length(); i++) {

                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Question_List question_list = new Question_List();
                                HashMap<String, String> user = ab.getAnswer(i);
                                String user_answer = user.get("name");
                                if (user_answer!=null) {
                                    question_list.Set_User_Answer(Integer.parseInt(user_answer));
                                    Log.d("right answer",user_answer);
                                }else{
                                    question_list.Set_User_Answer(0);
                                }
                                question_list.Set_Id(feedObj.getString("paper_id"));
                                question_list.Set_Question(feedObj.getString("question"));
                                question_list.Set_Option_One(feedObj.getString("opt_1"));
                                question_list.Set_Option_Two(feedObj.getString("opt_2"));
                                question_list.Set_Option_Three(feedObj.getString("opt_3"));
                                question_list.Set_Option_Four(feedObj.getString("opt_4"));
                                question_list.Set_Option_Five(feedObj.getString("opt_5"));
                                question_list.Set_Solution(feedObj.getInt("correct_opt"));
                                question_list.Set_year(feedObj.getString("question_origin"));
                                question_list.Set_Method(feedObj.getString("explanation"));
                                question_list.setClick(0);
                                switch (feedObj.getInt("correct_opt")) {
                                    case 1:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_1"));
                                        break;
                                    case 2:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_2"));
                                        break;
                                    case 3:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_3"));
                                        break;
                                    case 4:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_4"));
                                        break;
                                    case 5:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_5"));
                                        break;
                                    default:
                                        question_list.Set_Right_Answer("Not Available");
                                        break;
                                }


                                question_lists.add(question_list);
                                progressBar.setVisibility(View.GONE);

                            }
                            test_question_adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new LovelyStandardDialog(Answer.this)
                        .setButtonsColorRes(R.color.text_color)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("Something Went Wrong...Retry")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, String> user = db.getUserDetails();
                                String tokens = user.get("token");
                                Load_CT(tokens);
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


    private void Load_FT(final String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/test/sample/paper/new/" + group_id,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject feedObjs = new JSONObject(response);
                            JSONArray feedArray =feedObjs.getJSONArray("questions");
                            for (int i = 0; i < feedArray.length(); i++) {

                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Question_List question_list = new Question_List();
                                HashMap<String, String> user = ab.getAnswer(i);
                                String user_answer = user.get("name");
                                if (user_answer!=null) {
                                    question_list.Set_User_Answer(Integer.parseInt(user_answer));
                                    Log.d("right answer",user_answer);
                                }else{
                                    question_list.Set_User_Answer(0);
                                }
                                question_list.Set_Id(feedObj.getString("paper_id"));
                                question_list.Set_Question(feedObj.getString("question"));
                                question_list.Set_Option_One(feedObj.getString("opt_1"));
                                question_list.Set_Option_Two(feedObj.getString("opt_2"));
                                question_list.Set_Option_Three(feedObj.getString("opt_3"));
                                question_list.Set_Option_Four(feedObj.getString("opt_4"));
                                question_list.Set_Option_Five(feedObj.getString("opt_5"));
                                question_list.Set_Solution(feedObj.getInt("correct_opt"));
                                question_list.Set_Method(feedObj.getString("explanation"));
                                question_list.setClick(0);

                                switch (feedObj.getInt("correct_opt")) {
                                    case 1:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_1"));
                                        break;
                                    case 2:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_2"));
                                        break;
                                    case 3:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_3"));
                                        break;
                                    case 4:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_4"));
                                        break;
                                    case 5:
                                        question_list.Set_Right_Answer(feedObj.getString("opt_5"));
                                        break;
                                    default:
                                        question_list.Set_Right_Answer("Not Available");
                                        break;
                                }


                                question_lists.add(question_list);
                                progressBar.setVisibility(View.GONE);

                            }
                            test_question_adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new LovelyStandardDialog(Answer.this)
                        .setButtonsColorRes(R.color.text_color)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("Something Went Wrong...Retry")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, String> user = db.getUserDetails();
                                String tokens = user.get("token");
                                Load_FT(tokens);
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

}
