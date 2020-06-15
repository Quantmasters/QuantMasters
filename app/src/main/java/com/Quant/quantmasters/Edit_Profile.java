package com.Quant.quantmasters;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 10-01-2017.
 */

public class Edit_Profile extends AppCompatActivity {

    AutoCompleteTextView Branch, College, YOP;
    EditText Full_Name, Last_Name, Phone;
    EditText Usn, DOB;
    String dats;
    private DatabaseHandler db;
    private ProgressDialog pDialog;
    Button Get_Started;
    ProgressBar progressBar;
    ScrollView scrollView2;
    String token, email, emailz;
    TextView title;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        Full_Name = (EditText) findViewById(R.id.Full_Name);
        db = new DatabaseHandler(this);
        Last_Name = findViewById(R.id.Last_Name);
        Phone = (EditText) findViewById(R.id.Phone);
        Usn = (EditText) findViewById(R.id.USN);
        DOB = (EditText) findViewById(R.id.Date);
        Branch = (AutoCompleteTextView) findViewById(R.id.Branch);
        College = (AutoCompleteTextView) findViewById(R.id.College);
        YOP = (AutoCompleteTextView) findViewById(R.id.YOP);
        scrollView2 = findViewById(R.id.scrollView2);
        title = findViewById(R.id.chapter_name);
        TextView pr_det = findViewById(R.id.pr_det);
        Get_Started = findViewById(R.id.Get_Started);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        pDialog = new ProgressDialog(this);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        pDialog.setCancelable(false);
        title.setTypeface(typeface);

        pr_det.setTypeface(typeface);


        Full_Name.setTypeface(typeface2);
        Last_Name.setTypeface(typeface2);
        Phone.setTypeface(typeface2);
        Usn.setTypeface(typeface2);
        DOB.setTypeface(typeface2);
        Branch.setTypeface(typeface2);
        College.setTypeface(typeface2);
        YOP.setTypeface(typeface2);

        Get_Started.setTypeface(typeface);


        TextPaint paints = pr_det.getPaint();
        float widths = paints.measureText("Update Info");
        Shader textShaders = new LinearGradient(0, 0, widths, pr_det.getTextSize(),
                new int[]{
                        Color.parseColor("#FA8432"),
                        Color.parseColor("#FDBE40"),
                }, null, Shader.TileMode.CLAMP);
        pr_det.getPaint().setShader(textShaders);


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

        TextPaint paint = title.getPaint();
        float width = paint.measureText("Edit Profile");
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#a86cff"),
                        Color.parseColor("#6191ff"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);

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

                DatePickerDialog mDatePicker = new DatePickerDialog(Edit_Profile.this, android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Edit_Profile.this, New_Profile.class);
                startActivity(intent);
                finish();
            }
        });

        Get_Started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String full_name = Full_Name.getText().toString().trim();
                String last_name = Last_Name.getText().toString().trim();
                String phone = Phone.getText().toString().trim();
                String usn = Usn.getText().toString().trim();
                String branch = Branch.getText().toString().trim();
                String college = College.getText().toString().trim();
                String dob = dats;
                String yop = YOP.getText().toString().trim();

                if (!full_name.isEmpty() && !last_name.isEmpty() && !phone.isEmpty() && !branch.isEmpty() && !college.isEmpty() && !dob.isEmpty() && !yop.isEmpty()) {

                    HashMap<String, String> user = db.getUserDetails();
                    String tokens = user.get("token");
                    emailz = user.get("email");

                    Register_User(tokens, full_name, last_name, usn, college, branch, dob, yop, phone);
                } else {

                    if (full_name.isEmpty()) {
                        Full_Name.setError("Please enter your Full Name");
                    }

                    if (last_name.isEmpty()) {
                        Last_Name.setError("Please enter your Last Name");
                    }


                    if (phone.isEmpty()) {
                        Phone.setError("Please enter your Phone Number");
                    }
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

        HashMap<String, String> user = db.getUserDetails();
        String tokens = user.get("token");
        String email = user.get("email");
        Load(tokens, email);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Edit_Profile.this, New_Profile.class);
        startActivity(intent);
        finish();
    }

    private void Register_User(final String tokenz, final String name, final String last_name, final String usn, final String college, final String branch, final String dob, final String yop, final String phone) {
        pDialog.setMessage("Updating ...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://api.quantmasters.in/user/manage/" + emailz + "/profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("res", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.getString("msg").equals("Updated")) {
                                db.updateName(name, emailz);
                                Toast.makeText(Edit_Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                hideDialog();
                                Intent intent = new Intent(Edit_Profile.this, New_Main_Activity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                hideDialog();
                                new LovelyStandardDialog(Edit_Profile.this)
                                        .setButtonsColorRes(R.color.red)
                                        .setTitle("Error")
                                        .setMessage("Something went wrong.")
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
                        Toast.makeText(Edit_Profile.this, "Network Error", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("f_name", name);
                params.put("email", emailz);
                params.put("l_name", last_name);
                params.put("usn", usn);
                params.put("inst_name", college);
                params.put("qual", branch);
                params.put("dob", dob);
                params.put("yop", yop);
                params.put("phone_no", phone);
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Authorization", "Bearer " + tokenz);
                return headerMap;
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
                            String qual = jObj.getString("qual");
                            String inst_name = jObj.getString("inst_name");
                            String usn = jObj.getString("usn");
                            String yop = jObj.getString("yop");
                            String dob = jObj.getString("dob");

                            dats = dob;

                            if (!usn.equals("null") && !usn.isEmpty()) {
                                Usn.setText(usn);
                            } else {
                                Usn.setText("");
                            }

                            if (!yop.equals("null") && !yop.isEmpty()) {
                                YOP.setText(yop);
                            } else {
                                YOP.setText("");
                            }


                            if (!dob.equals("null") && !dob.isEmpty()) {
                                DOB.setText(dob);
                            } else {
                                DOB.setText("");
                            }


                            if (!inst_name.equals("null") && !inst_name.isEmpty()) {
                                College.setText(inst_name);
                            } else {
                                College.setText("");
                            }


                            if (!l_name.equals("null") && !l_name.isEmpty()) {
                                Last_Name.setText(l_name);
                            } else {
                                Last_Name.setText("");
                            }

                            if (!phone_no.equals("null") && !phone_no.isEmpty()) {
                                Phone.setText(phone_no);
                            } else {
                                Phone.setText("");
                            }

                            if (!qual.equals("null") && !qual.isEmpty()) {
                                Branch.setText(qual);
                            } else {
                                Branch.setText("");
                            }


                            if (!f_name.equals("null") && !f_name.isEmpty()) {
                                Full_Name.setText(f_name);
                            } else {
                                Full_Name.setText("");
                            }


                            scrollView2.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new LovelyStandardDialog(Edit_Profile.this)
                        .setButtonsColorRes(R.color.red)
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


}