package com.Quant.quantmasters;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.material.navigation.NavigationView;
import com.onesignal.OneSignal;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class New_Main_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Navigation_Adapter navigation_adapter;
    private List<Navigation_List> navigation_lists = new ArrayList<>();
    int flag = 0;
    DatabaseHandler db;
    private List<Chapter_List> chapter_lists = new ArrayList<Chapter_List>();
    private List<View_Pager_List> view_pager_lists = new ArrayList<View_Pager_List>();
    public static final String linkpreference = "Links";
    SharedPreferences sharedpreferences;
    RecyclerView recycler_views;
    RelativeLayout Trend, ff,dashboard_layout;
    ProgressBar progressBar;
    Button Retry;
    New_List_Adapter new_list_adapter;
    private SessionManager session;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    String sub_flag = "0";
    New_ViewPager_Adapter viewPagerAdapter;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_activity);
        Intent intent = getIntent();
        //  sub_flag = Objects.requireNonNull(intent.getExtras()).getInt("subscribed");

        Typeface bold = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface fontz = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        Typeface fontx = Typeface.createFromAsset(getAssets(), "SourceSansProLight.otf");

        sharedpreferences = getSharedPreferences(linkpreference, Context.MODE_PRIVATE);
        session = new SessionManager(New_Main_Activity.this);

        db = new DatabaseHandler(New_Main_Activity.this);
        pDialog = new ProgressDialog(New_Main_Activity.this);
        pDialog.setCancelable(false);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ImageView nav = (ImageView) findViewById(R.id.nav);
        final ImageView fullscreen = (ImageView) findViewById(R.id.fullscreen);
        Trend = (RelativeLayout) findViewById(R.id.Trend);
        // ImageView preview_video = findViewById(R.id.preview_video);
        final RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        Retry = (Button) findViewById(R.id.Retry);
        ff = findViewById(R.id.ff);
        TextView dashboard_ = findViewById(R.id.dashboard_);
        ImageView dashboard = (ImageView) findViewById(R.id.dashboard);
        ImageView admin = (ImageView) findViewById(R.id.admin);
        ImageView profile = (ImageView) findViewById(R.id.profile);
        TextView title = findViewById(R.id.title);
        TextView nav_title = findViewById(R.id.nav_title);
        dashboard_layout = findViewById(R.id.dashboard_layout);
        dashboard_.setTypeface(bold);

        // Glide.with(this)
        //   .load("https://www.quantmasters.in/assets/main_image.jpg")
        //   .placeholder(R.drawable.placeholder)
        //   .centerCrop()
        //   .into(preview_video);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(New_Main_Activity.this, New_Profile.class);
                startActivity(intent);
                finish();

            }
        });
        TextPaint paints = nav_title.getPaint();
        float widths = paints.measureText("Quant Masters");
        Shader textShaders = new LinearGradient(0, 0, widths, nav_title.getTextSize(),
                new int[]{
                        Color.parseColor("#a86cff"),
                        Color.parseColor("#6191ff"),
                }, null, Shader.TileMode.CLAMP);
        nav_title.getPaint().setShader(textShaders);
        nav_title.setTypeface(bold);


        dashboard_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(New_Main_Activity.this, New_Dashboard.class);
                startActivity(intent);
            }
        });

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(New_Main_Activity.this, New_Dashboard.class);
                startActivity(intent);

            }
        });

        sharedpreferences = getSharedPreferences(linkpreference,
                Context.MODE_PRIVATE);

        recycler_views = (RecyclerView) findViewById(R.id.recycler_views);
        TextView disz = (TextView) findViewById(R.id.disz);
        TextView dis = findViewById(R.id.dis);


        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        ThreeBounce doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);


        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        recycler_views.setLayoutManager(mLayoutManager1);
        recycler_views.setHasFixedSize(true);

        disz.setTypeface(fontx);
        title.setTypeface(bold);
        dis.setTypeface(fontx);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        TextPaint paint = title.getPaint();
        float width = paint.measureText("Quant Masters");
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#177ED5"),
                        Color.parseColor("#16B1E9"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);


        new_list_adapter = new New_List_Adapter(New_Main_Activity.this, chapter_lists);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new_list_adapter);


        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(navigationView);
            }
        });


        Retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retry.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                HashMap<String, String> user = db.getUserDetails();
                String tokens = user.get("token");
                Load(tokens);
                Load_Images(tokens);
            }
        });


        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new New_ViewPager_Adapter(this, view_pager_lists);
        viewPager.setAdapter(viewPagerAdapter);

        //  String jsn = "[{\"image\":\"https://cdn.slidesharecdn.com/ss_thumbnails/timeworkstandardlevel-170127012501-thumbnail-4.jpg\",\"group_type\":\"1\"},{\"image\":\"https://cdn.slidesharecdn.com/ss_thumbnails/timeworkstandardlevel-170127012501-thumbnail-4.jpg\",\"group_type\":\"1\"}]";


        HashMap<String, String> user = db.getUserDetails();
        String tokens = user.get("token");
        sub_flag = user.get("subscribe");
        String email = user.get("email");

        assert email != null;
        OneSignal.setEmail(email);




        if (sub_flag != null && !sub_flag.equals("null")) {

            if (sub_flag.equals("1")) {
                sub_flag = "1";
            } else {
                sub_flag = "0";
            }

            Load(tokens);
            Load_Images(tokens);
            Navigation();

        } else {
            sub_flag = "0";

            Logout_New();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Navigation() {
        navigation_adapter = new Navigation_Adapter(New_Main_Activity.this, navigation_lists);
        recycler_views.setItemAnimator(new DefaultItemAnimator());
        recycler_views.setAdapter(navigation_adapter);
        for (int i = 0; i <= 8; i++) {
            Navigation_List feeds = new Navigation_List();
            switch (i) {

                case 1:
                    feeds.Set_Menu("Profile");
                    feeds.Set_Image(R.drawable.ic_userc);
                    feeds.Set_Id(1);
                    navigation_lists.add(feeds);
                    break;
                case 3:
                    feeds.Set_Menu("Dashboard");
                    feeds.Set_Image(R.drawable.ic_dashboard_);
                    feeds.Set_Id(2);
                    navigation_lists.add(feeds);
                    break;

                case 4:
                    feeds.Set_Menu("Doubts");
                    feeds.Set_Image(R.drawable.ic_whatsapp);
                    feeds.Set_Id(8);
                    navigation_lists.add(feeds);
                    break;

                case 7:
                    feeds.Set_Menu("About");
                    feeds.Set_Image(R.drawable.ic_info_);
                    feeds.Set_Id(3);
                    navigation_lists.add(feeds);
                    break;
                case 5:
                    feeds.Set_Menu("Share");
                    feeds.Set_Image(R.drawable.ic_share);
                    feeds.Set_Id(4);
                    navigation_lists.add(feeds);
                    break;
                case 6:
                    feeds.Set_Menu("Rate App");
                    feeds.Set_Image(R.drawable.ic_star_);
                    feeds.Set_Id(5);
                    navigation_lists.add(feeds);
                    break;

                case 2:
                    if (sub_flag != null && !sub_flag.equals("null")) {
                        if (sub_flag.equals("0")) {
                            feeds.Set_Menu("Subscribe");
                            feeds.Set_Image(R.drawable.ic_shopping_basket);
                            feeds.Set_Id(6);
                            navigation_lists.add(feeds);
                        }
                    }
                    break;

                case 8:
                    feeds.Set_Menu("Logout");
                    feeds.Set_Image(R.drawable.ic_logout);
                    feeds.Set_Id(7);
                    navigation_lists.add(feeds);
                    break;


            }
            navigation_adapter.notifyDataSetChanged();
        }
    }


    private void Load_Images(final String token) {
        view_pager_lists.clear();
        //https://api.quantmasters.in/home/images
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/home/images",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray feedArray = new JSONArray(response);
                            for (int j = 0; j < feedArray.length(); j++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(j);
                                View_Pager_List view_pager_list = new View_Pager_List();

                                switch (feedObj.getInt("image_type")) {
                                    case 0:
                                        view_pager_list.Set_Image_Link(feedObj.getString("image_link"));
                                        view_pager_list.Set_Type(feedObj.getInt("image_type"));
                                        view_pager_list.Set_Type(feedObj.getInt("image_type"));
                                        view_pager_lists.add(view_pager_list);
                                        break;
                                    case 1:
                                        if (sub_flag != null && !sub_flag.equals("null")) {
                                            if (sub_flag.equals("0")) {
                                                view_pager_list.Set_Image_Link(feedObj.getString("image_link"));
                                                view_pager_list.Set_Type(feedObj.getInt("image_type"));
                                                view_pager_lists.add(view_pager_list);
                                            }
                                        }
                                        break;
                                    case 2:
                                        view_pager_list.Set_Image_Link(feedObj.getString("image_link"));
                                        view_pager_list.Set_Type(feedObj.getInt("image_type"));
                                        view_pager_list.Set_Group_Id(feedObj.getString("group_id"));
                                        view_pager_list.Set_Group_Name(feedObj.getString("group_name"));
                                        view_pager_lists.add(view_pager_list);
                                        break;
                                    case 3:
                                        view_pager_list.Set_Image_Link(feedObj.getString("image_link"));
                                        view_pager_list.Set_Type(feedObj.getInt("image_type"));
                                        view_pager_list.Set_Video_Link(feedObj.getString("video_link"));
                                        view_pager_lists.add(view_pager_list);
                                        break;

                                }

                            }

                            viewPagerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dotscount = viewPagerAdapter.getCount();
                        dots = new ImageView[dotscount];
                        for (int i = 0; i < dotscount; i++) {

                            dots[i] = new ImageView(New_Main_Activity.this);
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins(8, 0, 8, 0);

                            sliderDotspanel.addView(dots[i], params);

                        }

                        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {

                                for (int i = 0; i < dotscount; i++) {
                                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                                }

                                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        RequestQueue requestQueue = Volley.newRequestQueue(New_Main_Activity.this);
        requestQueue.add(stringRequest);

    }


    private void Load(final String token) {
        chapter_lists.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/videos/groups",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        String jsn = "[{\"group_name\":\"Student Achievers\",\"group_type\":\"6\"},{\"group_name\":\"Achievers Experience\",\"group_type\":\"2\"},{\"group_name\":\"Testimonials\",\"group_type\":\"3\"},{\"group_name\":\"Free Videos\",\"group_type\":\"4\"},{\"group_name\":\"Free Test \",\"group_type\":\"5\"}]";
                        try {
                            JSONArray feedArray = new JSONArray(jsn);
                            for (int j = 0; j < feedArray.length(); j++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(j);
                                Chapter_List chapter_list = new Chapter_List();
                                chapter_list.Set_Name(feedObj.getString("group_name"));
                                chapter_list.Set_Type(feedObj.getInt("group_type"));
                                chapter_lists.add(chapter_list);

                            }
                            new_list_adapter.notifyDataSetChanged();

                            JSONArray feedArrays = new JSONArray(response);
                            for (int i = 0; i <= feedArray.length(); i++) {

                                JSONObject feedObj = (JSONObject) feedArrays.get(i);
                                Chapter_List chapter_list = new Chapter_List();
                                chapter_list.Set_Id(feedObj.getString("group_id"));
                                chapter_list.Set_Name(feedObj.getString("group_name"));
                                chapter_list.Set_Number(feedObj.getString("no_of_videos"));

                                chapter_list.Set_Type(1);
                                chapter_lists.add(chapter_list);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                        new_list_adapter.notifyDataSetChanged();
                        dashboard_layout.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                new LovelyStandardDialog(New_Main_Activity.this)
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
                                Load_Images(tokens);
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
        RequestQueue requestQueue = Volley.newRequestQueue(New_Main_Activity.this);
        requestQueue.add(stringRequest);

    }


    private void Logout_User(final String email) {
        //   pDialog.setMessage("Logging Out ...");
        //   pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/user/logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.has("text")) {
                                String text = jObj.getString("text");
                                session.setLogin(false);
                                db.deleteall();

                                Intent intent = new Intent(New_Main_Activity.this, First_Activity.class);
                                startActivity(intent);
                                finish();


                            } else {

                                new LovelyStandardDialog(New_Main_Activity.this)
                                        .setButtonsColorRes(R.color.text_color)
                                        .setTitle("Error")
                                        .setMessage("Something Went Wrong...Retry")
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Logout_User(email);
                                            }
                                        })
                                        .show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(New_Main_Activity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }

        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Logout_New() {
        session.setLogin(false);
        db.deleteDatabase();
        Intent intent = new Intent(New_Main_Activity.this, First_Activity.class);
        startActivity(intent);
        finish();
    }


}

