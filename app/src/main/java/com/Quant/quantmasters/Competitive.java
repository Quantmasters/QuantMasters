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

public class Competitive extends Activity {
    ProgressBar progressBar;
    private DatabaseHandler db;
    RecyclerView recycler_views;
    private List<Chapter_List> chapter_list = new ArrayList<Chapter_List>();
    Competitive_Chapter_Adapter test_chapter_adapter;
    private Marks_Database md;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_chapter);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        Intent intent = getIntent();
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        db = new DatabaseHandler(this);
        TextView chapter_name = findViewById(R.id.chapter_name);
        chapter_name.setText("Competitive");
        Typeface bold = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        chapter_name.setTypeface(bold);

        TextPaint paint = chapter_name.getPaint();
        float width = paint.measureText("Competitive");
        Shader textShader = new LinearGradient(0, 0, width, chapter_name.getTextSize(),
                new int[]{
                        Color.parseColor("#177ED5"),
                        Color.parseColor("#16B1E9"),
                }, null, Shader.TileMode.CLAMP);
        chapter_name.getPaint().setShader(textShader);
        recycler_views = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_views.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_views.setLayoutManager(mLayoutManager);
        recycler_views.setHasFixedSize(true);
        md = new Marks_Database(this);
        test_chapter_adapter = new Competitive_Chapter_Adapter(this, chapter_list);
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


    private void Load(final String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/test/competitive/groups",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONArray feedArray = new JSONArray(response);
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Chapter_List chapter_lists = new Chapter_List();
                                chapter_lists.Set_Id(feedObj.getString("group_id"));
                                chapter_lists.Set_Name(feedObj.getString("group_name"));
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
                new LovelyStandardDialog(Competitive.this)
                        .setButtonsColorRes(R.color.text_color)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("Something Went Wrong..Retry")
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
}
