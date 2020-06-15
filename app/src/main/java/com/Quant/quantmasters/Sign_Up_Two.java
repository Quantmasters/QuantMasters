package com.Quant.quantmasters;

import android.app.DatePickerDialog;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shreyas on 30-07-2017.
 */

public class Sign_Up_Two extends AppCompatActivity {
    TextView title, terms, sub_title2, sub_title;
    EditText Usn, DOB;
    CheckBox sub;
    Button Get_Started;
    private DatabaseHandler db;
    private ProgressDialog pDialog;
    AutoCompleteTextView Branch, College, YOP;
    String name, email, last_name, password, sign_link, phone;
    SharedPreferences sharedpreferences;
    public static final String linkpreference = "Links";
    public static final String Sign_Up = "sign_up";
    private SessionManager session;
    boolean flag = true;
    String dats = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_two);
        Intent intent = getIntent();
        name = intent.getExtras().getString("U_First_Name");
        last_name = intent.getExtras().getString("L_Name");
        email = intent.getExtras().getString("U_Email");
        password = intent.getExtras().getString("U_Password");
        phone = intent.getExtras().getString("U_Phone");

        title = findViewById(R.id.title);
        db = new DatabaseHandler(getApplicationContext());
        sharedpreferences = getSharedPreferences(linkpreference,
                Context.MODE_PRIVATE);
        sign_link = sharedpreferences.getString(Sign_Up, "");
        sub_title2 = findViewById(R.id.sub_title2);
        sub_title = findViewById(R.id.sub_title);
        TextView sign = findViewById(R.id.sign);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        title.setTypeface(typeface);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        RelativeLayout already_registered = findViewById(R.id.already_registered);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        sub = (CheckBox) findViewById(R.id.sub);
        sub_title2.setTypeface(typeface2);
        sub_title.setTypeface(typeface2);
        Usn = (EditText) findViewById(R.id.USN);
        DOB = (EditText) findViewById(R.id.Date);
        Branch = (AutoCompleteTextView) findViewById(R.id.Branch);
        College = (AutoCompleteTextView) findViewById(R.id.College);
        session = new SessionManager(getApplicationContext());
        YOP = (AutoCompleteTextView) findViewById(R.id.YOP);
        TextView namex = findViewById(R.id.name);

        Get_Started = findViewById(R.id.Get_Started);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        TextView sub = findViewById(R.id.sub);
        sub.setTypeface(typeface2);
        namex.setTypeface(typeface);
        TextPaint paint = title.getPaint();
        float width = paint.measureText("Profile");
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#a86cff"),
                        Color.parseColor("#6191ff"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);
        namex.getPaint().setShader(textShader);
        sign.getPaint().setShader(textShader);

        String[] branch_list = {"BE/BTech", "BSC", "BCA", "ME/MTech", "MCA", "MBA", "Other"};


        String[] yop_list = {"2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036"};

        String[] college = {
                "Srinivas Institute of Technology",
                "St Joseph Engineering College",
                "Canara Engineering College",
                "Mangalore Institute of Technology & Engineering",
                "Sahyadri College of Engineering & Management",
                "Nitte Engineering College",
                "P A Engineering College",
                "Manipal Institute of Technology",
                "Srinivas School of Engineering",
                "National Institute of Technology Karnataka",
                "Karavali Institute Of Technology",
                "Bearys Institute of Technology",
                "Moodlakatte Institute of Technology",
                "Shree Devi Institute of Technology",
                "Shri Madhwa Vadiraja Institute of Technology and Management",
                "Alva's Institute of Engineering and Technology",
                "Mangalore Marine College and Technology",
                "A. J. Institute of Management",
                "K.V.G. College of Engineering",
                "Vivekananda College of Engineering and Technology",
                "Yenepoya Institute Of Technology",
                "Bearys Institute of Technology", "Other"};

        ArrayAdapter<String> branch_adapter = new ArrayAdapter<String>(this, R.layout.gender_list, branch_list);
        Branch.setThreshold(1);
        Branch.setAdapter(branch_adapter);

        ArrayAdapter<String> yop_adapter = new ArrayAdapter<String>(this, R.layout.gender_list, yop_list);
        YOP.setThreshold(1);
        YOP.setAdapter(yop_adapter);

        ArrayAdapter<String> college_adapter = new ArrayAdapter<String>(this, R.layout.gender_list, college);
        College.setThreshold(1);
        College.setAdapter(college_adapter);


        sub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (((CheckBox) v).isChecked()) {
                    Log.d("flag", "true");
                    flag = true;
                } else {
                    flag = false;
                    Log.d("flag", "false");
                }

            }
        });

        already_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_Up_Two.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        College.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(College.getWindowToken(), 0);
                College.showDropDown();
            }
        });

        YOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(YOP.getWindowToken(), 0);
                YOP.showDropDown();
            }
        });

        DOB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(Sign_Up_Two.this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        int month = selectedmonth + 1;


                        if (month <= 9) {
                            if (selectedday <= 9) {
                                dats = selectedyear + "-0" + month + "-0" + selectedday;
                                DOB.setText("0" + selectedday + "-0" + month + "-" + selectedyear);
                            } else {
                                dats = selectedyear + "-0" + month + "-" + selectedday;
                                DOB.setText(selectedday + "-0" + month + "-" + selectedyear);
                            }


                        } else {
                            if (selectedday <= 9) {
                                dats = selectedyear + "-" + month + "-0" + selectedday;
                                DOB.setText("0" + selectedday + "-" + month + "-" + selectedyear);
                            } else {
                                dats = selectedyear + "-" + month + "-" + selectedday;
                                DOB.setText(selectedday + "-" + month + "-" + selectedyear);
                            }
                        }
                        Log.d("date", dats);
                    }

                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        Branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Branch.getWindowToken(), 0);
                Branch.showDropDown();
            }
        });

        Get_Started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usn = Usn.getText().toString().trim();
                String branch = Branch.getText().toString().trim();
                String college = College.getText().toString().trim();
                String dob = dats;
                String yop = YOP.getText().toString().trim();

                if (!branch.isEmpty() && !college.isEmpty() && !dob.isEmpty() && !yop.isEmpty()) {
                    Register_User(email, password, name, last_name, usn, college, branch, dob, yop, flag, phone);
                } else {


                    if (college.isEmpty()) {
                        College.setError("Please enter your College");
                    }
                    if (branch.isEmpty()) {
                        Branch.setError("Please enter your Branch");
                    }
                    if (dob.isEmpty()) {
                        DOB.setError("Please enter your Date of Birth");
                    }
                    if (yop.isEmpty()) {
                        YOP.setError("Please enter your Year of Passing");
                    }

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_Up_Two.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Sign_Up_Two.this, Login_Activity.class);
        startActivity(intent);
        finish();
    }

    private void Register_User(final String email, final String password, final String name, final String last_name, final String usn, final String college, final String branch, final String dob, final String yop, final boolean flag, final String phone) {
        pDialog.setMessage("Hang on...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/user/new/add",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("res", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.has("user")) {
                                String user = jObj.getString("user");
                                String email = jObj.getString("email");
                                String token = jObj.getString("token");
                                db.insertData(1, user, email, token, "0");
                                session.setLogin(true);
                                hideDialog();
                                Toast.makeText(Sign_Up_Two.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Sign_Up_Two.this, New_Main_Activity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                hideDialog();
                                new LovelyStandardDialog(Sign_Up_Two.this)
                                        .setButtonsColorRes(R.color.text_color)
                                        .setTitle("Error")
                                        .setMessage("User Already Exists.")
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


                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            hideDialog();
                            Toast.makeText(Sign_Up_Two.this, "Network Error", Toast.LENGTH_SHORT).show();
                        } //else if (error instanceof AuthFailureError) {
                        // Log.d("Errorz","2");
                        // //TODO
                        // }
                        else if (error instanceof ServerError) {
                            hideDialog();
                            new LovelyStandardDialog(Sign_Up_Two.this)
                                    .setButtonsColorRes(R.color.text_color)
                                    .setTitle("Error")
                                    .setMessage("User Already Exists.")
                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    })
                                    .show();
                        } //else if (error instanceof NetworkError) {
                        // Log.d("Errorz","4");
                        //  //TODO
                        //} else if (error instanceof ParseError) {
                        //  Log.d("Errorz","5");
                        //  //TODO
                        // }



                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("passwd", password);
                params.put("f_name", name);
                params.put("l_name", last_name);
                params.put("usn", usn);
                params.put("inst_name", college);
                params.put("qual", branch);
                params.put("dob", dob);
                params.put("yop", yop);
                params.put("phone_no", phone);
                params.put("subscribed", String.valueOf(flag));
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