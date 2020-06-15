package com.Quant.quantmasters;

import android.app.ProgressDialog;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Marks_Activity extends AppCompatActivity {
    ProgressBar progressBar;
    RecyclerView recycler_views;
    private List<Marks_List> marks_lists = new ArrayList<Marks_List>();
    Marks_Adapter marks_adapter;
    private DatabaseHandler db;
    TextView Test_Error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marks);

         Test_Error = findViewById(R.id.Test_Error);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        Intent intent = getIntent();
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        db = new DatabaseHandler(this);
        TextView chapter_name = findViewById(R.id.chapter_name);
        chapter_name.setText("Marks");
        Typeface bold = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface fontz = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        chapter_name.setTypeface(bold);
        Test_Error.setTypeface(fontz);
        TextPaint paint = chapter_name.getPaint();
        float width = paint.measureText("Marks");
        Shader textShader = new LinearGradient(0, 0, width, chapter_name.getTextSize(),
                new int[]{
                        Color.parseColor("#FF03B94D"),
                        Color.parseColor("#FF039742"),
                }, null, Shader.TileMode.CLAMP);
        chapter_name.getPaint().setShader(textShader);
        recycler_views = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_views.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_views.setLayoutManager(mLayoutManager);
        recycler_views.setHasFixedSize(true);


        marks_adapter = new Marks_Adapter(this, marks_lists);
        recycler_views.setItemAnimator(new DefaultItemAnimator());
        recycler_views.setAdapter(marks_adapter);
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> user = db.getUserDetails();
        String tokens = user.get("token");
        String emailz = user.get("email");
        Load(tokens, emailz);


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


    private void Load(final String token, final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/view/results/ind/all",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONArray feedArray = new JSONArray(response);

                            if (feedArray.length() == 0) {
                                Test_Error.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < feedArray.length(); i++) {


                                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                                    Marks_List marks_list = new Marks_List();
                                    marks_list.Set_Id(feedObj.getString("paper_id"));
                                    marks_list.Set_Name(feedObj.getString("paper_name"));
                                    marks_list.Set_Type(feedObj.getString("paper_type"));
                                    marks_list.Set_Time(feedObj.getString("created_at"));
                                    marks_list.Set_Marks(feedObj.getString("marks"));
                                    marks_list.Set_Total(feedObj.getString("total_marks"));
                                    marks_lists.add(marks_list);
                                    progressBar.setVisibility(View.GONE);


                                }
                            }

                            Collections.sort(marks_lists, new Comparator<Marks_List>() {
                                @Override
                                public int compare(Marks_List o1, Marks_List o2) {
                                    return o2.Get_Time().compareTo(o1.Get_Time());
                                }
                            });
                            // Log.d("sorted", "id: " + marks_lists.get(0).Get_Time() + " \nname: " + marks_lists.get(0).Get_Name());
                            marks_adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                new LovelyStandardDialog(Marks_Activity.this)
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
                                String emailz = user.get("email");
                                Load(tokens, emailz);
                            }
                        })
                        .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
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


    public static JSONArray sortJsonArray(JSONArray array) {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                String rid = null;
                try {
                    lid = lhs.getString("created_at");
                    rid = rhs.getString("created_at");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                assert rid != null;
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }

}
