package com.Quant.quantmasters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class New_Profile extends AppCompatActivity {
    private DatabaseHandler db;
    private ProgressDialog pDialog;
    ProgressBar progressBar2;
    TextView title, name, emails, phone, college, dob, qualification, logout;
    ImageButton edit, back;
    private SessionManager session;
    RelativeLayout main_rel;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_profile);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        title = findViewById(R.id.chapter_name);
        db = new DatabaseHandler(this);
        title.setTypeface(typeface);
        title.setText("Profile");
        progressBar2 = (ProgressBar) findViewById(R.id.spin_kit);
        progressBar2.setVisibility(View.VISIBLE);
        pDialog = new ProgressDialog(New_Profile.this);
        pDialog.setCancelable(false);
        edit = findViewById(R.id.edit);
        back = findViewById(R.id.back);
        session = new SessionManager(this);
        name = findViewById(R.id.name);
        logout = findViewById(R.id.logout);
        emails = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        college = findViewById(R.id.college);
        dob = findViewById(R.id.dob);
        qualification = findViewById(R.id.qualification);
        main_rel = findViewById(R.id.main_rel);

        emails.setTypeface(typeface2);
        phone.setTypeface(typeface2);
        name.setTypeface(typeface2);
        college.setTypeface(typeface2);
        dob.setTypeface(typeface2);
        qualification.setTypeface(typeface2);

        name.setTypeface(typeface);
        TextPaint paint = title.getPaint();
        float width = paint.measureText("Profile");
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#a86cff"),
                        Color.parseColor("#6191ff"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);
        logout.getPaint().setShader(textShader);
        HashMap<String, String> user = db.getUserDetails();
        String tokens = user.get("token");
        email = user.get("email");
        Load(tokens, email);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(New_Profile.this, Edit_Profile.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout_User(email);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(New_Profile.this, New_Main_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(New_Profile.this, New_Main_Activity.class);
        startActivity(intent);
        finish();
    }

    private void Load(final String token, final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/user/manage/" + email + "/profile",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {

                            JSONObject jObj = new JSONObject(response);
                            String f_name = jObj.getString("f_name");
                            String l_name = jObj.getString("l_name");
                            String phone_no = jObj.getString("phone_no");
                            String emailz = jObj.getString("email");
                            String qual = jObj.getString("qual");
                            String inst_name = jObj.getString("inst_name");
                            String usn = jObj.getString("usn");
                            String yop = jObj.getString("yop");
                            String dobs = jObj.getString("dob");

                            name.setText(f_name + " " + l_name);
                            emails.setText(emailz);

                            TextPaint paint = name.getPaint();
                            float width = paint.measureText(f_name + " " + l_name);
                            Shader textShader = new LinearGradient(0, 0, width, name.getTextSize(),
                                    new int[]{
                                            Color.parseColor("#a86cff"),
                                            Color.parseColor("#6191ff"),
                                    }, null, Shader.TileMode.CLAMP);
                            name.getPaint().setShader(textShader);


                            TextPaint paints = emails.getPaint();
                            float widths = paints.measureText(emailz);
                            Shader textShaders = new LinearGradient(0, 0, widths, title.getTextSize(),
                                    new int[]{
                                            Color.parseColor("#177ED5"),
                                            Color.parseColor("#16B1E9"),
                                    }, null, Shader.TileMode.CLAMP);
                            emails.getPaint().setShader(textShaders);
                            dob.getPaint().setShader(textShaders);
                            qualification.getPaint().setShader(textShaders);
                            college.getPaint().setShader(textShaders);
                            phone.getPaint().setShader(textShaders);

                            if (!dobs.equals("null") && !dobs.isEmpty()) {
                                dob.setText(dobs);
                            } else {
                                dob.setText("Add your Date of Birth");
                            }

                            if (!qual.equals("null") && !qual.isEmpty()) {
                                qualification.setText(qual);
                            } else {
                                qualification.setText("Add Your Qualification");
                            }

                            if (!inst_name.equals("null") && !inst_name.isEmpty()) {
                                college.setText(inst_name);
                            } else {
                                college.setText("Add Your College");
                            }


                            if (!phone_no.equals("null") && !phone_no.isEmpty()) {
                                phone.setText(phone_no);
                            } else {
                                phone.setText("Add your Phone number");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        main_rel.setVisibility(View.VISIBLE);
                        progressBar2.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                new LovelyStandardDialog(New_Profile.this)
                        .setButtonsColorRes(R.color.text_color)
                        .setTitle("Error")
                        .setCancelable(false)
                        .setMessage("Something went wrong.")
                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HashMap<String, String> user = db.getUserDetails();
                                String tokens = user.get("token");
                                String email = user.get("email");
                                Load(tokens, email);
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

    private void Logout_User(final String email) {
        pDialog.setMessage("Logging Out ...");
        showDialog();
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
                                hideDialog();
                                Intent intent = new Intent(New_Profile.this, First_Activity.class);
                                startActivity(intent);
                                finish();


                            } else {
                                hideDialog();
                                new LovelyStandardDialog(New_Profile.this)
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
                        hideDialog();
                        Toast.makeText(New_Profile.this, "Network Error", Toast.LENGTH_SHORT).show();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}