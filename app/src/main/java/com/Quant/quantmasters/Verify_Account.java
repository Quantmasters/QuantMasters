package com.Quant.quantmasters;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shreyas on 18-04-2017.
 */

public class Verify_Account extends AppCompatActivity {
    private ProgressDialog pDialog;
    private SessionManager session;
    private EditText inputEmail, Date;
    TextView title, sub_title;
    String dats = "";
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        Button Login_Button = (Button) findViewById(R.id.Login_Button);
        RelativeLayout sign_up = (RelativeLayout) findViewById(R.id.Sign_up);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sub_title = findViewById(R.id.sub_title);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        db = new DatabaseHandler(getApplicationContext());
        inputEmail = (EditText) findViewById(R.id.Email);
        Date = (EditText) findViewById(R.id.Date);
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
        Date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Verify_Account.this,android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        int month =  selectedmonth+1;


                        if(month<=9){
                            if(selectedday<=9){
                                dats = selectedyear + "-0" + month + "-0" + selectedday;
                                Date.setText("0"+selectedday + "-0" + month + "-" + selectedyear);
                            }else{
                                dats = selectedyear + "-0" + month + "-" + selectedday;
                                Date.setText(selectedday + "-0" + month + "-" + selectedyear);
                            }



                        }else {
                            if(selectedday<=9){
                                dats = selectedyear + "-" + month + "-0" + selectedday;
                                Date.setText("0"+selectedday + "-" + month + "-" + selectedyear);
                            }else {
                                dats = selectedyear + "-" + month + "-" + selectedday;
                                Date.setText(selectedday + "-" + month + "-" + selectedyear);
                            }
                        }

                        Log.d("date",dats);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //session.setLogin(true);
                String email = inputEmail.getText().toString().trim();
                String date = dats;
                if (!email.isEmpty() && !date.isEmpty()) {

                    Login_User(email,date);


                } else {
                    if (email.isEmpty()) {
                        inputEmail.setError("Please enter your email");
                    }
                    if (date.isEmpty()) {
                        Date.setError("Please enter your Date of Birth");
                    }

                }

            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Verify_Account.this, Login_Activity.class);
                startActivity(intent);
                finish();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Verify_Account.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Verify_Account.this, Login_Activity.class);
        startActivity(intent);
        finish();
    }


    private void Login_User(final String email, final String date) {
        pDialog.setMessage("Please Wait..");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/password/reset/validate/dob",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Intent intent = new Intent(Verify_Account.this, Reset_Password.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        new LovelyStandardDialog(Verify_Account.this)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Error")
                                .setMessage("Email and DOB are not matching.")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                })
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("dob", date);
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