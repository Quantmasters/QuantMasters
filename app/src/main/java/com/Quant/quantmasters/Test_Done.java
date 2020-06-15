package com.Quant.quantmasters;

import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;


public class Test_Done extends AppCompatActivity {
    ProgressBar progressBar;
    private DatabaseHandler db;
    private ProgressDialog pDialog;
    String token, paper_id, name;
    private SessionManager session;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_done);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        Typeface typeface3 = Typeface.createFromAsset(getAssets(), "SourceSansProLight.otf");
        Intent intent = getIntent();
        String total = Objects.requireNonNull(intent.getExtras()).getString("total_score");
        name = intent.getExtras().getString("paper_title");
        paper_id = intent.getExtras().getString("paper_id");
        flag = intent.getExtras().getInt("paper_type");
        //ArrayList<data> arrayParents = intent.getParcelableArrayListExtra("data");
        Log.d("Paper id",paper_id);
        pDialog = new ProgressDialog(this);
        TextView total_score = findViewById(R.id.total_score);
        TextView done = findViewById(R.id.done);
        done.setText("Test Done");
        total_score.setTypeface(typeface3);
        done.setTypeface(typeface);
        Button View_Answers = findViewById(R.id.View_Answers);

        View_Answers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Test_Done.this, Answer.class);
                intent.putExtra("paper_id", paper_id);
                intent.putExtra("paper_title", name);
                intent.putExtra("paper_type", flag);
                startActivity(intent);
                finish();
            }
        });

        TextPaint paint = done.getPaint();
        float width = paint.measureText("Test Done");
        Shader textShader = new LinearGradient(0, 0, width, done.getTextSize(),
                new int[]{
                        Color.parseColor("#a86cff"),
                        Color.parseColor("#6191ff"),
                }, null, Shader.TileMode.CLAMP);
        done.getPaint().setShader(textShader);

        if (total != null) {
            total_score.setText("Your Score is " + total);

        } else {
            total_score.setText("Your Score is 0");
        }


    }


    @Override
    public void onBackPressed() {
        finish();
    }


}