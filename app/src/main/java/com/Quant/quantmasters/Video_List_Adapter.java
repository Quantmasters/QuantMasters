package com.Quant.quantmasters;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
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

import androidx.core.content.ContextCompat;
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


public class Video_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Video_List> video_lists;
    private Context context;
    private ProgressDialog pDialog;
    private DatabaseHandler db;

    public Video_List_Adapter(Context context, List<Video_List> video_lists) {
        this.video_lists = video_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_video_list, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Video_List feeds = video_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");
        Typeface typeface1 = Typeface.createFromAsset(context.getAssets(), "sourcesans.otf");

        ((News_Card_5) holder).title.setText(feeds.Get_Name());
        ((News_Card_5) holder).sub_title.setText(feeds.Get_Description());
        ((News_Card_5) holder).title.setTypeface(typeface);
        ((News_Card_5) holder).time.setTypeface(typeface2);
        ((News_Card_5) holder).sub_title.setTypeface(typeface2);
        TextPaint paint = ((News_Card_5) holder).title.getPaint();
        float width = paint.measureText(feeds.Get_Name());
        Shader textShader = new LinearGradient(0, 0, width, ((News_Card_5) holder).title.getTextSize(),
                new int[]{
                        Color.parseColor("#177ED5"),
                        Color.parseColor("#16B1E9"),
                }, null, Shader.TileMode.CLAMP);
        ((News_Card_5) holder).title.getPaint().setShader(textShader);

        db = new DatabaseHandler(context);
        ((News_Card_5) holder).time.setText(feeds.Get_Length() + "  Min");
        Glide.with(context)
                .load("https://quantmasters.in" + feeds.Get_Image())
                .centerCrop()
                .skipMemoryCache(true)
                .into(((News_Card_5) holder).preview_video);

        int firstDigit = Integer.parseInt(feeds.getRating().substring(0, 1));

        switch (firstDigit){
            case 0:
                ((News_Card_5) holder).star1.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star2.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star3.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star4.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star5.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);

                break;
            case 1:
                ((News_Card_5) holder).star1.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star2.setColorFilter(ContextCompat.getColor(context,R.color.unlike), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star3.setColorFilter(ContextCompat.getColor(context,R.color.unlike), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star4.setColorFilter(ContextCompat.getColor(context,R.color.unlike), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star5.setColorFilter(ContextCompat.getColor(context,R.color.unlike), PorterDuff.Mode.SRC_IN);
                break;
            case 2:
                ((News_Card_5) holder).star1.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star2.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star3.setColorFilter(ContextCompat.getColor(context,R.color.unlike), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star4.setColorFilter(ContextCompat.getColor(context,R.color.unlike), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star5.setColorFilter(ContextCompat.getColor(context,R.color.unlike), PorterDuff.Mode.SRC_IN);
                break;
            case 3:
                ((News_Card_5) holder).star1.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star2.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star3.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star4.setColorFilter(ContextCompat.getColor(context,R.color.unlike), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star5.setColorFilter(ContextCompat.getColor(context,R.color.unlike), PorterDuff.Mode.SRC_IN);
                break;
            case 4:
                ((News_Card_5) holder).star1.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star2.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star3.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star4.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star5.setColorFilter(ContextCompat.getColor(context,R.color.unlike), PorterDuff.Mode.SRC_IN);
                break;
            case 5:
                 ((News_Card_5) holder).star1.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star2.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star3.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star4.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);
                ((News_Card_5) holder).star5.setColorFilter(ContextCompat.getColor(context,R.color.like), PorterDuff.Mode.SRC_IN);

                break;
        }

        ((News_Card_5) holder).layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feeds.getFlag() == 1) {
                    pDialog = new ProgressDialog(context);
                    pDialog.setCancelable(false);
                    HashMap<String, String> user = db.getUserDetails();
                    final String token = user.get("token");
                    final String vid = feeds.Get_Id();
                    pDialog.setMessage("Loading..");
                    showDialog();
                    Log.d("Response", "good");
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/videos/" + vid + "/link",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Response", response);
                                    hideDialog();
                                    final Intent intent = new Intent(context, New_Preview.class);
                                    intent.putExtra("video_title", feeds.Get_Name());
                                    intent.putExtra("v_link", response);
                                    intent.putExtra("v_id", vid);
                                    intent.putExtra("v_gr", feeds.Get_Group());
                                    intent.putExtra("thumb", feeds.Get_Image());
                                    intent.putExtra("flag", feeds.getFlag());
                                    intent.putExtra("rating", feeds.getRating());
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
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

                } else {
                    final Intent intent = new Intent(context, New_Preview.class);
                    intent.putExtra("video_title", feeds.Get_Name());
                    intent.putExtra("v_id", feeds.Get_Id());
                    intent.putExtra("v_gr", feeds.Get_Group());
                    intent.putExtra("thumb", feeds.Get_Image());
                    intent.putExtra("flag", feeds.getFlag());
                    intent.putExtra("rating", feeds.getRating());
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });


        ((News_Card_5) holder).download_notes.setOnClickListener(new View.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View view) {

                                                                         pDialog = new ProgressDialog(context);
                                                                         pDialog.setCancelable(false);
                                                                         HashMap<String, String> user = db.getUserDetails();
                                                                         final String token = user.get("token");
                                                                         final String vid = feeds.Get_Id();
                                                                         pDialog.setMessage("Loading..");
                                                                         showDialog();
                                                                         StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/videos/" + vid + "/dl-notes",
                                                                                 new Response.Listener<String>() {
                                                                                     @Override
                                                                                     public void onResponse(String response) {
                                                                                         hideDialog();
                                                                                         Intent intent = new Intent();
                                                                                         intent.setDataAndType(Uri.parse("https://quantmasters.in/" + response), "application/pdf");
                                                                                         context.startActivity(intent);
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


                                                                 }
        );


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
        TextView title, sub_title, download_notes, play_video, time;
        ImageView preview_video;
        RelativeLayout layout;
        ImageView star1,star2,star3,star4,star5;


        News_Card_5(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            sub_title = (TextView) itemView.findViewById(R.id.sub_title);
            preview_video = itemView.findViewById(R.id.preview_video);
            download_notes = itemView.findViewById(R.id.download_notes);
            time = itemView.findViewById(R.id.time);
            play_video = itemView.findViewById(R.id.play_video);
            layout = itemView.findViewById(R.id.layout);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);


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
