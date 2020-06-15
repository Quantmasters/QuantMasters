package com.Quant.quantmasters;


import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;




/**
 * Created by Shreyas on 21-06-2017.
 * <p>
 */


public class New_Answer_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Question_List> question_lists;
    private Context context;


    public New_Answer_Adapter(Context context, List<Question_List> question_lists) {
        this.question_lists = question_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_answer_new, parent, false);
        return new News_Card_5(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


        final Question_List feeds = question_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");

        ((News_Card_5) holder).Question.setText(feeds.Get_Question());
        int num = position;
        num = num + 1;
        ((News_Card_5) holder).Question_Number.setText("Q." + num);
        ((News_Card_5) holder).Question.setTypeface(typeface2);
        ((News_Card_5) holder).option_one.setTypeface(typeface2);
        ((News_Card_5) holder).option_two.setTypeface(typeface2);
        ((News_Card_5) holder).option_three.setTypeface(typeface2);
        ((News_Card_5) holder).option_four.setTypeface(typeface2);
        ((News_Card_5) holder).option_five.setTypeface(typeface2);
        ((News_Card_5) holder).answer.setTypeface(typeface2);
        ((News_Card_5) holder).Question_Number.setTypeface(typeface2);
        ((News_Card_5) holder).solution_button.setTypeface(typeface);

        TextPaint paintx = ((News_Card_5) holder).solution_button.getPaint();
        float widthx = paintx.measureText("Solution");
        Shader textShaderx = new LinearGradient(0, 0, widthx, ((News_Card_5) holder).solution_button.getTextSize(),
                new int[]{
                        Color.parseColor("#FF03B94D"),
                        Color.parseColor("#FF039742"),
                }, null, Shader.TileMode.CLAMP);
        ((News_Card_5) holder).solution_button.getPaint().setShader(textShaderx);


        if (feeds.Get_Method() != null && !feeds.Get_Method().equals("null") && !feeds.Get_Method().isEmpty() && feeds.getClick() == 0) {
            ((News_Card_5) holder).solution_button.setVisibility(View.VISIBLE);
        } else {
            ((News_Card_5) holder).solution_button.setVisibility(View.GONE);
        }


        ((News_Card_5) holder).solution_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String front = "<html>" +
                        "    <head>" +
                        "        <script src=\'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.5/MathJax.js?config=TeX-MML-AM_CHTML\' async></script>" +
                        "    </head>" +
                        "    <body>";

               

                WebSettings webSettings = ((News_Card_5) holder).solution.getSettings();
                webSettings.setJavaScriptEnabled(true);


                String content = front + feeds.Get_Method();
                ((News_Card_5) holder).solution.loadData(content, "text/html; charset=utf-8", "UTF-8");


                ((News_Card_5) holder).solution.setNestedScrollingEnabled(true);
                ((News_Card_5) holder).solution_button.setVisibility(View.GONE);

                final Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((News_Card_5) holder).solution.setVisibility(View.VISIBLE);
                    }

                }, 500);


                // feeds.setClick(1);

            }
        });


        ((News_Card_5) holder).option_one.setText("1 ) " + feeds.Get_Option_One());
        ((News_Card_5) holder).option_two.setText("2 ) " + feeds.Get_Option_Two());
        ((News_Card_5) holder).option_three.setText("3 ) " + feeds.Get_Option_Three());

        if (feeds.Get_Option_Four() != null && !feeds.Get_Option_Four().equals("") && !feeds.Get_Option_Four().isEmpty()) {
            ((News_Card_5) holder).option_four.setText("4 ) " + feeds.Get_Option_Four());
        } else {
            ((News_Card_5) holder).option_four.setVisibility(View.GONE);
        }


        if (feeds.Get_Option_Five() != null && !feeds.Get_Option_Five().equals("") && !feeds.Get_Option_Five().isEmpty()) {
            ((News_Card_5) holder).option_five.setText("5 ) " + feeds.Get_Option_Five());
        } else {
            ((News_Card_5) holder).option_five.setVisibility(View.GONE);
        }


        TextPaint paints = ((News_Card_5) holder).option_one.getPaint();
        float widths = paints.measureText(feeds.Get_Right_Answer());
        Shader textShaders = new LinearGradient(0, 0, widths, ((News_Card_5) holder).option_one.getTextSize(),
                new int[]{
                        Color.parseColor("#FFFC1100"),
                        Color.parseColor("#FFFC1100"),
                }, null, Shader.TileMode.CLAMP);

        ((News_Card_5) holder).answer.getPaint().setShader(textShaders);

        TextPaint paint = ((News_Card_5) holder).option_one.getPaint();
        float width = paint.measureText(feeds.Get_Right_Answer());
        Shader textShader = new LinearGradient(0, 0, width, ((News_Card_5) holder).option_one.getTextSize(),
                new int[]{
                        Color.parseColor("#FF03B94D"),
                        Color.parseColor("#FF039742"),
                }, null, Shader.TileMode.CLAMP);

        ((News_Card_5) holder).answer.getPaint().setShader(textShader);


        switch (feeds.Get_User_Answer()) {
            case 0:
                break;
            case 1:
                if (feeds.Get_Solution() == 1) {
                    ((News_Card_5) holder).option_one.getPaint().setShader(textShader);
                } else {
                    ((News_Card_5) holder).option_one.getPaint().setShader(textShaders);
                }
                break;
            case 2:
                if (feeds.Get_Solution() == 2) {
                    ((News_Card_5) holder).option_two.getPaint().setShader(textShader);
                } else {
                    ((News_Card_5) holder).option_two.getPaint().setShader(textShaders);
                }
                break;
            case 3:
                if (feeds.Get_Solution() == 3) {
                    ((News_Card_5) holder).option_three.getPaint().setShader(textShader);
                } else {
                    ((News_Card_5) holder).option_three.getPaint().setShader(textShaders);
                }
                break;
            case 4:
                if (feeds.Get_Solution() == 4) {
                    ((News_Card_5) holder).option_four.getPaint().setShader(textShader);
                } else {
                    ((News_Card_5) holder).option_four.getPaint().setShader(textShaders);
                }
                break;
            case 5:
                if (feeds.Get_Solution() == 5) {
                    ((News_Card_5) holder).option_five.getPaint().setShader(textShader);
                } else {
                    ((News_Card_5) holder).option_five.getPaint().setShader(textShaders);
                }
                break;
        }

        switch (feeds.Get_Solution()) {
            case 1:
                ((News_Card_5) holder).answer.setText("Answer : " + feeds.Get_Option_One());
                break;
            case 2:
                ((News_Card_5) holder).answer.setText("Answer : " + feeds.Get_Option_Two());
                break;
            case 3:
                ((News_Card_5) holder).answer.setText("Answer : " + feeds.Get_Option_Three());
                break;
            case 4:
                ((News_Card_5) holder).answer.setText("Answer : " + feeds.Get_Option_Four());
                break;
            case 5:
                ((News_Card_5) holder).answer.setText("Answer : " + feeds.Get_Option_Five());
                break;
        }


    }

    @Override
    public int getItemCount() {
        return question_lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private static class News_Card_5 extends RecyclerView.ViewHolder {
        TextView Question, Question_Number, answer;
        TextView option_four, option_three, option_two, option_one, option_five;
        WebView solution;
        TextView solution_button;

        News_Card_5(View itemView) {
            super(itemView);
            Question = (TextView) itemView.findViewById(R.id.Question);
            option_four = itemView.findViewById(R.id.option_four);
            option_three = itemView.findViewById(R.id.option_three);
            option_two = itemView.findViewById(R.id.option_two);
            option_one = itemView.findViewById(R.id.option_one);
            option_five = itemView.findViewById(R.id.option_five);
            answer = itemView.findViewById(R.id.answer);
            Question_Number = itemView.findViewById(R.id.Question_Number);
            solution = itemView.findViewById(R.id.solution);
            solution_button = itemView.findViewById(R.id.solution_text);
        }
    }


}
