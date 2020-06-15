package com.Quant.quantmasters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Payment_Done extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_done);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        TextView total_score = findViewById(R.id.total_score);
        TextView done = findViewById(R.id.done);
        done.setText("Payment Successful");
        total_score.setTypeface(typeface2);
        done.setTypeface(typeface2);
        total_score.setText("");
        final Handler h = new Handler();
        TextPaint paint = done.getPaint();
        float width = paint.measureText("Payment Successful");
        Shader textShader = new LinearGradient(0, 0, width, done.getTextSize(),
                new int[]{
                        Color.parseColor("#a86cff"),
                        Color.parseColor("#6191ff"),
                }, null, Shader.TileMode.CLAMP);
        done.getPaint().setShader(textShader);
        done.setTypeface(typeface);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Payment_Done.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        }, 3000);


    }


    @Override
    public void onBackPressed() {
        finish();
    }
}