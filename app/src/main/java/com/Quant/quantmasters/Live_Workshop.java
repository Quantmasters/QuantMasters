package com.Quant.quantmasters;

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
import android.widget.ImageView;
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

public class Live_Workshop extends AppCompatActivity {
    ProgressBar progressBar2;
    private DatabaseHandler db;
    RecyclerView recycler_views;
    private List<Video_List> video_lists = new ArrayList<Video_List>();
    Live_Workshop_Adapter video_list_adapter;
    ProgressBar progressBar;
    String token;
    ImageView image;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");


        Intent intent = getIntent();
        String tittl = intent.getExtras().getString("Video_Title");

        ImageButton back = (ImageButton) findViewById(R.id.back);
        progressBar2 = (ProgressBar) findViewById(R.id.spin_kit2);
        db = new DatabaseHandler(this);
        title = findViewById(R.id.title);
        progressBar2.setVisibility(View.VISIBLE);
        title.setTypeface(typeface);
        title.setText(tittl);
        TextPaint paint = title.getPaint();

        float width = paint.measureText(tittl);
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#FA8432"),
                        Color.parseColor("#FDBE40"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);

        db = new DatabaseHandler(getApplicationContext());


        recycler_views = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_views.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Live_Workshop.this, LinearLayoutManager.VERTICAL, false);
        recycler_views.setLayoutManager(mLayoutManager);
        recycler_views.setHasFixedSize(true);


        video_list_adapter = new Live_Workshop_Adapter(this, video_lists);
        recycler_views.setItemAnimator(new DefaultItemAnimator());
        recycler_views.setAdapter(video_list_adapter);


        HashMap<String, String> users = db.getUserDetails();
        String tokens = users.get("token");
        Load(tokens);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void Load(final String token) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/workshop/videos/all",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            JSONArray feedArray = new JSONArray(response);
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Video_List video_list = new Video_List();
                                video_list.Set_Id(feedObj.getString("video_id"));
                                video_list.Set_Name(feedObj.getString("video_name"));
                                video_list.Set_Image(feedObj.getString("thumbnail"));
                                video_lists.add(video_list);

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
                progressBar2.setVisibility(View.GONE);
                new LovelyStandardDialog(Live_Workshop.this)
                        .setButtonsColorRes(R.color.text_color)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("Something Went Wrong...Retry")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressBar2.setVisibility(View.VISIBLE);
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


}