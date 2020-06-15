package com.Quant.quantmasters;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class New_ViewPager_Adapter extends PagerAdapter {
    private List<View_Pager_List> question_lists;
    private Context context;
    private ProgressDialog pDialog;
    private DatabaseHandler db;
    private LayoutInflater layoutInflater;


    public New_ViewPager_Adapter(Context context, List<View_Pager_List> question_lists) {
        this.question_lists = question_lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return question_lists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View_Pager_List feeds = question_lists.get(position);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.main_image);
        //   imageView.setImageResource(images[position]);


        switch (feeds.Get_Type()) {
            case 0:
                Log.d("imagez", feeds.Get_Image_Link());
                Glide.with(context)
                        .load(feeds.Get_Image_Link())
                        .placeholder(R.drawable.placeholder)
                        .centerCrop()
                        .into(imageView);

                break;
            case 1:
                Log.d("imagez", feeds.Get_Image_Link());
                Glide.with(context)
                        .load(feeds.Get_Image_Link())
                        .placeholder(R.drawable.placeholder)
                        .centerCrop()
                        .into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, Payment_Activity.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case 2:
                Log.d("imagez", feeds.Get_Image_Link());
                Glide.with(context)
                        .load(feeds.Get_Image_Link())
                        .placeholder(R.drawable.placeholder)
                        .centerCrop()
                        .into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, Preview_Activity.class);
                        intent.putExtra("Video_Title", feeds.Get_Group_Name());
                        intent.putExtra("Video_Group_Id", feeds.Get_Group_Id());
                        context.startActivity(intent);
                    }
                });
                break;
            case 3:
                Log.d("imagez", feeds.Get_Image_Link());
                Glide.with(context)
                        .load(feeds.Get_Image_Link())
                        .placeholder(R.drawable.placeholder)
                        .centerCrop()
                        .into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db = new DatabaseHandler(context);
                        pDialog = new ProgressDialog(context);
                        pDialog.setCancelable(false);
                        HashMap<String, String> user = db.getUserDetails();
                        final String token = user.get("token");
                        pDialog.setMessage("Loading..");
                        showDialog();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, feeds.Get_Video_Link(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("vlink",response);
                                        final Intent intent = new Intent(context, VideoPlayerActivity.class);
                                        intent.putExtra("v_link", response);
                                        intent.putExtra("v_id", "video_id");
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
                                new LovelyStandardDialog(context)
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
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);


                    }

                });

                break;
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

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