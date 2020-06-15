package com.Quant.quantmasters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
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

public class Model_Question extends Activity {
    ProgressBar progressBar;
    private DatabaseHandler db;
    private SessionManager session;
    RecyclerView recycler_views;
    private List<Video_List> video_lists =  new ArrayList<Video_List>();
    Video_List_Adapter video_list_adapter;
    String group_id;
    private List<Chapter_List> chapter_list =  new ArrayList<Chapter_List>();
    Model_Question_Adapter test_chapter_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_question_chapter);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        Intent intent = getIntent();
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        db = new DatabaseHandler(this);
        TextView chapter_name = findViewById(R.id.chapter_name);
        Typeface bold = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        chapter_name.setTypeface(bold);
        chapter_name.setText("Model Question Paper");

        TextPaint paint = chapter_name.getPaint();
        float width = paint.measureText("Model Question Paper");
        Shader textShader = new LinearGradient(0, 0, width, chapter_name.getTextSize(),
                new int[]{
                        Color.parseColor("#177ED5"),
                        Color.parseColor("#16B1E9"),
                }, null, Shader.TileMode.CLAMP);
        chapter_name.getPaint().setShader(textShader);

        recycler_views = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_views.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recycler_views.setLayoutManager(mLayoutManager);
        recycler_views.setHasFixedSize(true);

        test_chapter_adapter = new Model_Question_Adapter(this, chapter_list);
        recycler_views.setItemAnimator(new DefaultItemAnimator());
        recycler_views.setAdapter(test_chapter_adapter);
        progressBar.setVisibility(View.VISIBLE);

        HashMap<String, String> user = db.getUserDetails();
        String tokens = user.get("token");
        Load(tokens);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }



    private void Load(final String token){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/test/question/ret/papers",
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONArray feedArray = new JSONArray(response);
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Chapter_List chapter_lists = new Chapter_List();
                                chapter_lists.Set_Id(feedObj.getString("paper_id"));
                                chapter_lists.Set_Name(feedObj.getString("paper_name"));
                                chapter_lists.Set_Flag(feedObj.getInt("public"));
                                chapter_lists.Set_Time(feedObj.getInt("time_lim"));
                                chapter_lists.Set_Number("");
                                chapter_list.add(chapter_lists);
                                progressBar.setVisibility(View.GONE);
                            }
                            test_chapter_adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                new LovelyStandardDialog(Model_Question.this)
                        .setButtonsColorRes(R.color.text_color)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("Something went wrong..Retry")
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
        })
        {
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
