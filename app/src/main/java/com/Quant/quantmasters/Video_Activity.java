package com.Quant.quantmasters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Video_Activity extends Activity {
    ProgressBar progressBar;
    private DatabaseHandler db;
    private SessionManager session;
    RecyclerView recycler_views;
    private List<Video_List> video_lists =  new ArrayList<Video_List>();
    Video_List_Adapter video_list_adapter;
    String group_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        Intent intent = getIntent();
       String name = intent.getExtras().getString("video_title");
        group_id = intent.getExtras().getString("video_id");
        db = new DatabaseHandler(this);
        TextView chapter_name = findViewById(R.id.chapter_name);
        progressBar.setVisibility(View.VISIBLE);
        chapter_name.setText(name);


        recycler_views = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_views.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Video_Activity.this,LinearLayoutManager.VERTICAL, false);
        recycler_views.setLayoutManager(mLayoutManager);
        recycler_views.setHasFixedSize(true);



        video_list_adapter = new Video_List_Adapter(this, video_lists);
        recycler_views.setItemAnimator(new DefaultItemAnimator());
        recycler_views.setAdapter(video_list_adapter);


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

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/videos/all",
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            JSONArray feedArray = new JSONArray(response);
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Video_List video_list = new Video_List();
                                if(group_id.equals(feedObj.getString("video_group"))) {
                                    video_list.Set_Id(feedObj.getString("video_id"));
                                    video_list.Set_Name(feedObj.getString("video_name"));
                                    video_list.Set_Description(feedObj.getString("description"));
                                    video_list.Set_Image(feedObj.getString("thumbnail"));
                                    video_lists.add(video_list);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                            video_list_adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                // Toast.makeText(ScrollingActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
