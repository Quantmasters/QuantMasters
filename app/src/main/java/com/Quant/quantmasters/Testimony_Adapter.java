package com.Quant.quantmasters;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Shreyas on 21-06-2017.
 * <p>
 */


public class Testimony_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Video_List> video_lists;
    private Context context;
    private ProgressDialog pDialog;
    private DatabaseHandler db;

    public Testimony_Adapter(Context context, List<Video_List> video_lists) {
        this.video_lists = video_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Video_List feeds = video_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");

        ((News_Card_5) holder).title.setText(feeds.Get_Name());

        ((News_Card_5) holder).title.setTypeface(typeface);

        TextPaint paint = ((News_Card_5) holder).title.getPaint();
        float width = paint.measureText(feeds.Get_Name());
        Shader textShader = new LinearGradient(0, 0, width, ((News_Card_5) holder).title.getTextSize(),
                new int[]{
                        Color.parseColor("#177ED5"),
                        Color.parseColor("#16B1E9"),
                }, null, Shader.TileMode.CLAMP);
        ((News_Card_5) holder).title.getPaint().setShader(textShader);

        db = new DatabaseHandler(context);
        Glide.with(context)
                .load("https://quantmasters.in" + feeds.Get_Image())
                .centerCrop()
                .skipMemoryCache(true)
                .into(((News_Card_5) holder).preview_video);

        ((News_Card_5) holder).layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pDialog = new ProgressDialog(context);
                pDialog.setCancelable(false);
                HashMap<String, String> user = db.getUserDetails();
                final String token = user.get("token");
                final String vid = feeds.Get_Id();
                pDialog.setMessage("Loading..");
                showDialog();
                Log.d("Response", "good");
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/testimony/videos/" + vid + "/link",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                final Intent intent = new Intent(context, VideoPlayerActivity.class);
                                intent.putExtra("v_link", response);
                                intent.putExtra("v_id", vid);
                                intent.putExtra("type", "single");
                                final Handler h = new Handler();
                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideDialog();
                                        context.startActivity(intent);

                                    }
                                }, 1000);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            }

        });







    }

    @Override
    public int getItemCount() {
        return video_lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    private static class News_Card_5 extends RecyclerView.ViewHolder {
        TextView title;
        ImageView preview_video;
        RelativeLayout layout;


        News_Card_5(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);

            preview_video = itemView.findViewById(R.id.preview_video);

            layout = itemView.findViewById(R.id.layout);

        }
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
