package com.Quant.quantmasters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shreyas Sanil on 13-02-2019.
 */

public class
Sign_Up extends AppCompatActivity {
    TextView title, terms, sub_title,sub_title2;
    EditText Full_Name, Email, Password, Confirm_Password,Last_Name,Phone;
    Button Get_Started;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        ImageButton back = (ImageButton) findViewById(R.id.back);

        Full_Name = (EditText) findViewById(R.id.Full_Name);
        RelativeLayout already_registered = findViewById(R.id.already_registered);
        Email = (EditText) findViewById(R.id.Email);
        Password = (EditText) findViewById(R.id.Password);
        terms = (TextView) findViewById(R.id.terms);
        Last_Name = findViewById(R.id.Last_Name);
        Confirm_Password = findViewById(R.id.Confirm_Password);
        title = (TextView) findViewById(R.id.title);
        sub_title = findViewById(R.id.sub_title);
        TextView sign = findViewById(R.id.sign);
        sub_title2 = findViewById(R.id.sub_title2);
        TextView name = findViewById(R.id.name);
        Phone = (EditText) findViewById(R.id.Phone);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        title.setTypeface(typeface);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        name.setTypeface(typeface);
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
        Get_Started = findViewById(R.id.Get_Started);
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sub_title.setTypeface(typeface2);
        sub_title2.setTypeface(typeface2);
        terms.setText("Terms & Condition");


        Get_Started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String full_name = Full_Name.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String confirm = Confirm_Password.getText().toString().trim();
                String last_name = Last_Name.getText().toString().trim();

                String phone = Phone.getText().toString().trim();


                if (!email.isEmpty() && !password.isEmpty() && !confirm.isEmpty() && !full_name.isEmpty() && !last_name.isEmpty()&& !phone.isEmpty()) {

                    if (password.equals(confirm)) {


                        if (isValidPassword(password)) {

                            Intent intent = new Intent(Sign_Up.this, Sign_Up_Two.class);
                            intent.putExtra("U_Email", email);
                            intent.putExtra("U_First_Name", full_name);
                            intent.putExtra("U_Password", password);
                            intent.putExtra("L_Name", last_name);
                            intent.putExtra("U_Phone", phone);
                            startActivity(intent);
                            finish();
                        } else {
                            new LovelyStandardDialog(Sign_Up.this)
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
                        Confirm_Password.setError("Passwords Don't Match");
                    }

                } else {
                    if (full_name.isEmpty()) {
                        Full_Name.setError("Please enter your Full Name");
                    }

                    if (last_name.isEmpty()) {
                        Last_Name.setError("Please enter your Last Name");
                    }

                    if (email.isEmpty()) {
                        Email.setError("Please enter your Email");
                    }
                    if (password.isEmpty()) {
                        Password.setError("Please enter your Password");
                    }
                    if (confirm.isEmpty()) {
                        Confirm_Password.setError("Please confirm your Password");
                    }
                    if (phone.isEmpty()) {
                        Phone.setError("Please enter your Phone Number");
                    }


                }


            }
        });

        already_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_Up.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_Up.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Sign_Up.this, Login_Activity.class);
        startActivity(intent);
        finish();
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}