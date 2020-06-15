package com.Quant.quantmasters;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.onesignal.OneSignal;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class First_Activity extends AppCompatActivity {
    ProgressBar progressBar;
    private DatabaseHandler db;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        session = new SessionManager(getApplicationContext());
        db = new DatabaseHandler(getApplicationContext());
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        ThreeBounce doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.VISIBLE);
        TextView title = findViewById(R.id.title);
        Typeface bold = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        TextPaint paints = title.getPaint();
        float widths = paints.measureText("Quant Masters");
        Shader textShaders = new LinearGradient(0, 0, widths, title.getTextSize(),
                new int[]{
                        Color.parseColor("#a86cff"),
                        Color.parseColor("#6191ff"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShaders);
        title.setTypeface(bold);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        if (session.isLoggedIn()) {
            HashMap<String, String> user = db.getUserDetails();
            String email = user.get("email");
            Update(email);
        } else {
            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(First_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                }

            }, 3000);

        }


    }


    private void Update(final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/login/" + email + "/refresh",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            Log.d("Login", response);
                            if (jObj.has("token")) {
                                String token = jObj.getString("token");
                                String subscribed = jObj.getString("subscribed");
                                Log.d("new token", token);
                                db.updateToken(token, email,subscribed);
                                session.setLogin(true);
                                Intent intent = new Intent(First_Activity.this, New_Main_Activity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                new LovelyStandardDialog(First_Activity.this)
                                        .setButtonsColorRes(R.color.red)
                                        .setTitle("Oops!!!")
                                        .setCancelable(false)
                                        .setMessage("Something Went Wrong.. Try Again")
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (session.isLoggedIn()) {
                                                    HashMap<String, String> user = db.getUserDetails();
                                                    String email = user.get("email");
                                                    Update(email);
                                                } else {
                                                    final Handler h = new Handler();
                                                    h.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Intent intent = new Intent(First_Activity.this, Login_Activity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }

                                                    }, 3000);

                                                }
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

                        Log.d("res", String.valueOf(error));
                        new LovelyStandardDialog(First_Activity.this)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Oops!!!")
                                .setCancelable(false)
                                .setMessage("Something Went Wrong..Retry")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        HashMap<String, String> user = db.getUserDetails();
                                        String email = user.get("email");
                                        Update(email);
                                    }
                                })
                                .show();
                    }
                });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

