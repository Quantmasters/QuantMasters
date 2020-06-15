package com.Quant.quantmasters;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.webkit.WebView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import android.webkit.WebSettings;


public class Solution extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solution);


        Intent intent = getIntent();
        Typeface bold = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface fontz = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        String name = Objects.requireNonNull(intent.getExtras()).getString("paper_title");
        String sol = intent.getExtras().getString("sol");


        WebView solution = findViewById(R.id.solution);

        final String front = "<html>" +
                "    <head>" +
                "        <script src=\'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.5/MathJax.js?config=TeX-MML-AM_CHTML\' async></script>" +
                "    </head>" +
                "    <body>";

        final String back = " </body>" +
                "</html>" ;




       String content = front + sol ;
        solution.loadData(content, "text/html; charset=utf-8", "UTF-8");

    }
}