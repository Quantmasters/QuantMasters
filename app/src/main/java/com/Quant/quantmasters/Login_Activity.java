package com.Quant.quantmasters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextPaint;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shreyas on 18-04-2017.
 */

public class Login_Activity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private SessionManager session;
    private EditText inputEmail, inputPassword;
    TextView title, sub_title;
    private DatabaseHandler db;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ImageButton visibility = findViewById(R.id.visibility);
        Button Login_Button = (Button) findViewById(R.id.Login_Button);
        RelativeLayout sign_up = (RelativeLayout) findViewById(R.id.Sign_up);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sub_title = findViewById(R.id.sub_title);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        db = new DatabaseHandler(getApplicationContext());
        TextView name = findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.Email);
        inputPassword = (EditText) findViewById(R.id.Password);
        TextView Forgot_Password = (TextView) findViewById(R.id.Forgot_Password);
        session = new SessionManager(getApplicationContext());


        title = (TextView) findViewById(R.id.title);
        TextView sign = findViewById(R.id.sign);
        sub_title.setTypeface(typeface2);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        Forgot_Password.setTypeface(typeface2);
        TextPaint paint = title.getPaint();
        float width = paint.measureText("Profile");
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#a86cff"),
                        Color.parseColor("#6191ff"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);
        name.getPaint().setShader(textShader);
        sign.getPaint().setShader(textShader);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        title.setTypeface(typeface);
        sign.setTypeface(typeface);
        name.setTypeface(typeface);
        if (session.isLoggedIn()) {
            Intent intent = new Intent(Login_Activity.this, New_Main_Activity.class);
            startActivity(intent);
            finish();
        }


        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    inputPassword.setTransformationMethod(null);
                    flag = 1;
                } else {
                    inputPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //session.setLogin(true);
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()) {

                    Login_User(email, password);


                } else {
                    if (email.isEmpty()) {
                        inputEmail.setError("Please enter your email");
                    }
                    if (password.isEmpty()) {
                        inputPassword.setError("Please enter your password");
                    }

                }

            }
        });


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, Sign_Up.class);
                startActivity(intent);
                finish();

            }
        });


        Forgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, Verify_Account.class);
                startActivity(intent);
                finish();

            }
        });

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


    private void Login_User(final String email, final String password) {
        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/user/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            Log.d("New Data ", response);
                            if (jObj.has("user")) {
                                String user = jObj.getString("user");
                                String email = jObj.getString("email");
                                String token = jObj.getString("token");
                                String subscribed = jObj.getString("subscribed");
                                db.insertData(1, user, email, token, subscribed);
                                session.setLogin(true);





                                Intent intent = new Intent(Login_Activity.this, New_Main_Activity.class);
                                startActivity(intent);
                                finish();
                                hideDialog();
                            } else {
                                Toast.makeText(Login_Activity.this, "Email and Password are not Matching", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("errors", String.valueOf(error));

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            hideDialog();
                            Toast.makeText(Login_Activity.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } //else if (error instanceof AuthFailureError) {
                        // Log.d("Errorz","2");
                        // //TODO
                        // }
                        else if (error instanceof ServerError) {
                            hideDialog();
                            new LovelyStandardDialog(Login_Activity.this)
                                    .setButtonsColorRes(R.color.text_color)
                                    .setTitle("Error")
                                    .setMessage("Invalid Credentials...Forgot Password?")
                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .show();
                        }


                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("passwd", password);
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