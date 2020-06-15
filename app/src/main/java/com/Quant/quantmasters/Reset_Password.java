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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shreyas on 18-04-2017.
 */

public class Reset_Password extends AppCompatActivity {
    private ProgressDialog pDialog;
    private SessionManager session;
    private EditText inputPassword, Confirm_Password;
    TextView title, sub_title;
    private DatabaseHandler db;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_reset);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        Button Login_Button = (Button) findViewById(R.id.Login_Button);
        RelativeLayout sign_up = (RelativeLayout) findViewById(R.id.Sign_up);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sub_title = findViewById(R.id.sub_title);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        db = new DatabaseHandler(getApplicationContext());

        inputPassword = (EditText) findViewById(R.id.Password);
        Confirm_Password = (EditText) findViewById(R.id.Confirm_Password);
        TextView Forgot_Password = (TextView) findViewById(R.id.Forgot_Password);
        session = new SessionManager(getApplicationContext());
        title = (TextView) findViewById(R.id.title);
        sub_title.setTypeface(typeface2);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        title.setTypeface(typeface);
        TextView name = findViewById(R.id.name);
        TextView sign = findViewById(R.id.sign);
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
        name.setTypeface(typeface);

        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = inputPassword.getText().toString().trim();
                String confirm_password = Confirm_Password.getText().toString().trim();

                if (!password.isEmpty() && !confirm_password.isEmpty()) {

                    if (password.equals(confirm_password)) {

                        if (isValidPassword(confirm_password)) {
                            Login_User(email, password);
                        } else {
                            new LovelyStandardDialog(Reset_Password.this)
                                    .setButtonsColorRes(R.color.text_color)
                                    .setMessage("Password not Strong!!")
                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    })
                                    .show();
                        }


                    } else {
                        new LovelyStandardDialog(Reset_Password.this)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Oops!!!")
                                .setMessage("Passwords Don't Match")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .show();
                    }


                } else {
                    inputPassword.setError("Please enter the new password");
                }


            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reset_Password.this, Login_Activity.class);
                startActivity(intent);
                finish();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reset_Password.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Reset_Password.this, Login_Activity.class);
        startActivity(intent);
        finish();
    }


    private void Login_User(final String email, final String password) {
        pDialog.setMessage("Please Wait ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/password/reset/save",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);

                            if (jObj.has("text")) {
                                Toast.makeText(Reset_Password.this, "Password Reset Successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Reset_Password.this, Login_Activity.class);
                                startActivity(intent);
                                finish();

                            } else if (jObj.has("error")) {
                                new LovelyStandardDialog(Reset_Password.this)
                                        .setButtonsColorRes(R.color.text_color)
                                        .setTitle("Error")
                                        .setMessage(jObj.getString("error"))
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        })
                                        .show();
                            } else {
                                new LovelyStandardDialog(Reset_Password.this)
                                        .setButtonsColorRes(R.color.text_color)
                                        .setTitle("Error")
                                        .setMessage("Something went wrong")
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
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
                        Toast.makeText(Reset_Password.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
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