package com.Quant.quantmasters.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Quant.quantmasters.First_Activity;
import com.Quant.quantmasters.Login_Activity;
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
import com.Quant.quantmasters.Chapter_Adapter;
import com.Quant.quantmasters.Chapter_List;
import com.Quant.quantmasters.Course_List;
import com.Quant.quantmasters.DatabaseHandler;
import com.Quant.quantmasters.Payment_Activity;
import com.Quant.quantmasters.Popular_Adapter;
import com.Quant.quantmasters.R;
import com.Quant.quantmasters.SessionManager;
import com.Quant.quantmasters.Top_Course_Adapter;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private SessionManager session;
    private List<Chapter_List> chapter_list = new ArrayList<Chapter_List>();
    Chapter_Adapter chapter_adapter;
    RecyclerView recycler_views, recycler_view_two, recycler_view_four, recycler_view_three, recycler_view_five, recycler_view_six;
    private List<Course_List> course_list = new ArrayList<Course_List>();
    private List<Course_List> course_list1 = new ArrayList<Course_List>();
    private List<Course_List> course_list2 = new ArrayList<Course_List>();
    private List<Course_List> course_list3 = new ArrayList<Course_List>();
    private List<Course_List> course_list4 = new ArrayList<Course_List>();

    private DatabaseHandler db;
    Popular_Adapter popular_adapter;
    Top_Course_Adapter top_course_adapter, popular_adapter3, popular_adapter2, popular_adapter1;
    String qunt_id, a_name, qunt_idv, q_name2, q_name3, qunt_ide, q_name4, qunt_ide5, qunt_ide6, q_name5;
    ProgressBar progressBar;
    TextView popular_text, best_seller_text, e_text, categories_text, gd_text, mi_text, title_note;
    int flag = 0;
    CollapsingToolbarLayout toolbar_layout;

    int tip = 0;
    View root;
    int pub_tag = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            homeViewModel =
                    ViewModelProviders.of(this).get(HomeViewModel.class);
            root = inflater.inflate(R.layout.fragment_home, container, false);

            //final TextView textView = root.findViewById(R.id.text_home);
            Typeface typeface2 = Typeface.createFromAsset(getContext().getAssets(), "SourceSansProBold.otf");
            ImageView back_image = root.findViewById(R.id.main_image);
            e_text = root.findViewById(R.id.e_text);
            popular_text = root.findViewById(R.id.popular_text);
            best_seller_text = root.findViewById(R.id.best_seller_text);
            categories_text = root.findViewById(R.id.categories_text);
            gd_text = root.findViewById(R.id.gd_text);
            mi_text = root.findViewById(R.id.mi_text);


            e_text.setTypeface(typeface2);
            popular_text.setTypeface(typeface2);
            best_seller_text.setTypeface(typeface2);
            categories_text.setTypeface(typeface2);
            mi_text.setTypeface(typeface2);
            gd_text.setTypeface(typeface2);
            //toolbar_layout.setBackgroundResource(R.drawable.main_image);
            Glide.with(getContext())
                    .load(R.drawable.placeholder)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(back_image);


            session = new SessionManager(getContext());
            db = new DatabaseHandler(getContext());
            progressBar = (ProgressBar) root.findViewById(R.id.spin_kit);
            recycler_views = (RecyclerView) root.findViewById(R.id.recycler_view);


            recycler_views.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recycler_views.setLayoutManager(mLayoutManager);
            recycler_views.setHasFixedSize(true);
            progressBar.setVisibility(View.VISIBLE);
            chapter_adapter = new Chapter_Adapter(getContext(), chapter_list);
            recycler_views.setItemAnimator(new DefaultItemAnimator());
            recycler_views.setAdapter(chapter_adapter);

            recycler_view_three = (RecyclerView) root.findViewById(R.id.recycler_view_three);
            recycler_view_three.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager mLayoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recycler_view_three.setLayoutManager(mLayoutManager4);
            recycler_view_three.setHasFixedSize(true);

            top_course_adapter = new Top_Course_Adapter(getContext(), course_list1);
            recycler_view_three.setItemAnimator(new DefaultItemAnimator());
            recycler_view_three.setAdapter(top_course_adapter);


            recycler_view_two = (RecyclerView) root.findViewById(R.id.recycler_view_two);
            recycler_view_two.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recycler_view_two.setLayoutManager(mLayoutManager3);
            recycler_view_two.setHasFixedSize(true);

            popular_adapter = new Popular_Adapter(getContext(), course_list);
            recycler_view_two.setItemAnimator(new DefaultItemAnimator());
            recycler_view_two.setAdapter(popular_adapter);


            recycler_view_four = (RecyclerView) root.findViewById(R.id.recycler_view_four);
            recycler_view_four.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager mLayoutManager5 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recycler_view_four.setLayoutManager(mLayoutManager5);
            recycler_view_four.setHasFixedSize(true);

            popular_adapter1 = new Top_Course_Adapter(getContext(), course_list2);
            recycler_view_four.setItemAnimator(new DefaultItemAnimator());
            recycler_view_four.setAdapter(popular_adapter1);


            recycler_view_five = (RecyclerView) root.findViewById(R.id.recycler_view_five);
            recycler_view_five.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager mLayoutManager6 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recycler_view_five.setLayoutManager(mLayoutManager6);
            recycler_view_five.setHasFixedSize(true);

            popular_adapter2 = new Top_Course_Adapter(getContext(), course_list3);
            recycler_view_five.setItemAnimator(new DefaultItemAnimator());
            recycler_view_five.setAdapter(popular_adapter2);


            recycler_view_six = (RecyclerView) root.findViewById(R.id.recycler_view_six);
            recycler_view_six.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager mLayoutManager7 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recycler_view_six.setLayoutManager(mLayoutManager7);
            recycler_view_six.setHasFixedSize(true);

            popular_adapter3 = new Top_Course_Adapter(getContext(), course_list4);
            recycler_view_six.setItemAnimator(new DefaultItemAnimator());
            recycler_view_six.setAdapter(popular_adapter3);


            HashMap<String, String> user = db.getUserDetails();
            String tokens = user.get("token");

            if (tip == 0) {
                tip = 1;

                if (tokens != null) {
                    Log.d("token", tokens);
                    Load(tokens);
                } else {
                    session.setLogin(false);
                    db.deleteall();
                    Intent intent = new Intent(getContext(), First_Activity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

            }


            return root;
        } else {
            return null;
        }

    }


    private void Load(final String token) {

        chapter_list.clear();
        chapter_adapter.notifyDataSetChanged();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/videos/groups",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONArray feedArray = new JSONArray(response);
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                Chapter_List chapter_lists = new Chapter_List();
                                if (i == 0) {
                                    a_name = feedObj.getString("group_name");

                                    qunt_id = feedObj.getString("group_id");
                                } else if (i == 1) {
                                    q_name2 = feedObj.getString("group_name");
                                    qunt_idv = feedObj.getString("group_id");
                                } else if (i == 2) {
                                    q_name3 = feedObj.getString("group_name");
                                    qunt_ide = feedObj.getString("group_id");
                                } else if (i == 4) {

                                    q_name4 = feedObj.getString("group_name");
                                    qunt_ide5 = feedObj.getString("group_id");

                                } else if (i == 3) {

                                    q_name5 = feedObj.getString("group_name");
                                    qunt_ide6 = feedObj.getString("group_id");

                                }

                                chapter_lists.Set_Id(feedObj.getString("group_id"));
                                chapter_lists.Set_Name(feedObj.getString("group_name"));
                                chapter_lists.Set_Number(feedObj.getString("no_of_videos"));
                                chapter_list.add(chapter_lists);

                            }

                            chapter_adapter.notifyDataSetChanged();


                            Loads(token);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("res", String.valueOf(error));

                progressBar.setVisibility(View.INVISIBLE);
                new LovelyStandardDialog(getContext())
                        .setButtonsColorRes(R.color.red)
                        .setTitle("Error")
                        .setMessage("Something went wrong..Try Again ")
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
        if (getContext() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }


    private void Loads(final String token) {
        course_list.clear();
        course_list1.clear();
        course_list2.clear();
        course_list3.clear();
        course_list4.clear();
        popular_adapter.notifyDataSetChanged();
        popular_adapter1.notifyDataSetChanged();
        popular_adapter2.notifyDataSetChanged();
        popular_adapter3.notifyDataSetChanged();
        top_course_adapter.notifyDataSetChanged();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/videos/all",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            JSONArray feedArray = new JSONArray(response);

                            for (int i = 0; i < feedArray.length(); i++) {

                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                if (qunt_id.equals(feedObj.getString("video_group"))) {

                                    if (feedObj.getInt("public") == 1) {

                                        Course_List course_lists = new Course_List();
                                        course_lists.setID(feedObj.getString("video_id"));
                                        course_lists.Set_Title(feedObj.getString("video_name"));
                                        course_lists.Set_Image(feedObj.getString("thumbnail"));
                                        course_lists.Set_Group(feedObj.getString("video_group"));
                                        course_lists.setFlag(feedObj.getInt("public"));
                                        course_list.add(course_lists);
                                        popular_adapter.notifyDataSetChanged();

                                    }


                                }
                            }


                            for (int i = 0; i < feedArray.length(); i++) {

                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                if (qunt_id.equals(feedObj.getString("video_group"))) {

                                    if (feedObj.getInt("public") == 0) {
                                        Course_List course_lists = new Course_List();
                                        course_lists.setID(feedObj.getString("video_id"));
                                        course_lists.Set_Title(feedObj.getString("video_name"));
                                        course_lists.Set_Image(feedObj.getString("thumbnail"));
                                        course_lists.Set_Group(feedObj.getString("video_group"));
                                        course_lists.setFlag(feedObj.getInt("public"));
                                        course_list.add(course_lists);
                                        popular_adapter.notifyDataSetChanged();
                                        flag = 1;


                                    }


                                }
                            }
                            popular_text.setText(a_name);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {

                            JSONArray feedArray = new JSONArray(response);
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                if (qunt_idv.equals(feedObj.getString("video_group"))) {
                                    Course_List course_lists = new Course_List();
                                    course_lists.setID(feedObj.getString("video_id"));
                                    course_lists.Set_Title(feedObj.getString("video_name"));
                                    course_lists.Set_Image(feedObj.getString("thumbnail"));
                                    course_lists.Set_Group(feedObj.getString("video_group"));
                                    course_lists.setFlag(feedObj.getInt("public"));
                                    course_list1.add(course_lists);
                                }
                            }
                            top_course_adapter.notifyDataSetChanged();
                            best_seller_text.setText(q_name2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {

                            JSONArray feedArray = new JSONArray(response);
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                if (qunt_ide.equals(feedObj.getString("video_group"))) {
                                    Course_List course_lists = new Course_List();
                                    course_lists.setID(feedObj.getString("video_id"));
                                    course_lists.Set_Title(feedObj.getString("video_name"));
                                    course_lists.Set_Image(feedObj.getString("thumbnail"));
                                    course_lists.Set_Group(feedObj.getString("video_group"));
                                    course_lists.setFlag(feedObj.getInt("public"));
                                    course_list2.add(course_lists);
                                }
                            }
                            popular_adapter1.notifyDataSetChanged();
                            e_text.setText(q_name3);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        try {

                            JSONArray feedArray = new JSONArray(response);
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                if (qunt_ide5.equals(feedObj.getString("video_group"))) {
                                    Course_List course_lists = new Course_List();
                                    course_lists.setID(feedObj.getString("video_id"));
                                    course_lists.Set_Title(feedObj.getString("video_name"));
                                    course_lists.Set_Image(feedObj.getString("thumbnail"));
                                    course_lists.Set_Group(feedObj.getString("video_group"));
                                    course_lists.setFlag(feedObj.getInt("public"));
                                    course_list3.add(course_lists);
                                }
                            }
                            popular_adapter2.notifyDataSetChanged();
                            gd_text.setText(q_name4);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        try {

                            JSONArray feedArray = new JSONArray(response);
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                if (qunt_ide6.equals(feedObj.getString("video_group"))) {
                                    Course_List course_lists = new Course_List();
                                    course_lists.setID(feedObj.getString("video_id"));
                                    course_lists.Set_Title(feedObj.getString("video_name"));
                                    course_lists.Set_Image(feedObj.getString("thumbnail"));
                                    course_lists.Set_Group(feedObj.getString("video_group"));
                                    course_lists.setFlag(feedObj.getInt("public"));
                                    course_list4.add(course_lists);
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            popular_adapter3.notifyDataSetChanged();
                            mi_text.setText(q_name5);
                            recycler_views.setVisibility(View.VISIBLE);
                            recycler_view_two.setVisibility(View.VISIBLE);
                            recycler_view_three.setVisibility(View.VISIBLE);
                            recycler_view_four.setVisibility(View.VISIBLE);
                            recycler_view_five.setVisibility(View.VISIBLE);
                            recycler_view_six.setVisibility(View.VISIBLE);
                            tip = 0;
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
        if (getContext() != null) {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        }
    }


}