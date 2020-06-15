package com.Quant.quantmasters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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


public class Top_Course_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Course_List> course_lists;
    private Context context;
    String vid;
    String link = "none";
    private SessionManager session;
    private ProgressDialog pDialog;
    private DatabaseHandler db;
    public Top_Course_Adapter(Context context, List<Course_List> course_lists) {
        this.course_lists = course_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_course_new, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Course_List feeds = course_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");
        ((News_Card_5) holder).title.setText(feeds.Get_Title());


        db = new DatabaseHandler(context);



        Glide.with(context)
                .load("https://quantmasters.in/"+feeds.Get_Image())
                .fitCenter()
                .skipMemoryCache(true)
                .into(((News_Card_5) holder).image);

        ((News_Card_5) holder).title.setTypeface(typeface2);


            ((News_Card_5) holder).image.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    final Intent intent = new Intent(context, Preview_Activity.class);
                                                                    intent.putExtra("video_title", feeds.Get_Title());
                                                                    intent.putExtra("v_id", feeds.getID());
                                                                    intent.putExtra("v_gr", feeds.Get_Group());
                                                                    intent.putExtra("thumb", feeds.Get_Image());
                                                                    intent.putExtra("flag", feeds.getFlag());
                                                                    context.startActivity(intent);
                                                                }
                                                            }
            );





    }

    @Override
    public int getItemCount() {
        return course_lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    private static class News_Card_5 extends RecyclerView.ViewHolder {
        TextView title,rating,price,discount;
        ImageView image;


        News_Card_5(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
        }
    }



    private void Load(final String token){
        pDialog.setMessage("Loading..");
        showDialog();
        Log.d("Response", "good");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/videos/"+vid+"/link",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        link = response;
                        Log.d("Response", response);
                            final Intent intent = new Intent(context,VideoPlayerActivity.class);
                            intent.putExtra("v_link", link);
                            intent.putExtra("v_id", vid);
                            final Handler h = new Handler();
                            h.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                     context.startActivity(intent);
                                    hideDialog();
                                }
                            }, 1000);

                        }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                VolleyLog.d("Error", "Error: " + error.getMessage());
                new LovelyStandardDialog(context)
                        .setButtonsColorRes(R.color.red)
                        .setTitle("Error")
                        .setMessage("Something went wrong")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideDialog();
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
