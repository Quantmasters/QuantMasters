package com.Quant.quantmasters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by Shreyas on 21-06-2017.
 * <p>
 */


public class Answer_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Question_List> question_lists;
    private Context context;
    private Marks_Database md;
    int marks = 0;

    public Answer_Adapter(Context context, List<Question_List> question_lists) {
        this.question_lists = question_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_answer_list, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        md = new Marks_Database(context);

        final Question_List feeds = question_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");
        Typeface typeface3 = Typeface.createFromAsset(context.getAssets(), "SourceSansProLight.otf");
        ((News_Card_5) holder).Question.setText(feeds.Get_Question());
        int num = position;
        num = num + 1;
        ((News_Card_5) holder).Question_Number.setText("Q." + num);
        ((News_Card_5) holder).Question.setTypeface(typeface2);
        ((News_Card_5) holder).Answer.setTypeface(typeface2);
        ((News_Card_5) holder).Question_Number.setTypeface(typeface2);
        ((News_Card_5) holder).Answer.setText("Answer : " + feeds.Get_Right_Answer());


        TextPaint paint = ((News_Card_5) holder).Answer.getPaint();
        float width = paint.measureText(feeds.Get_Right_Answer());
        Shader textShader = new LinearGradient(0, 0, width, ((News_Card_5) holder).Answer.getTextSize(),
                new int[]{
                        Color.parseColor("#FF03B94D"),
                        Color.parseColor("#FF039742"),
                }, null, Shader.TileMode.CLAMP);
        ((News_Card_5) holder).Answer.getPaint().setShader(textShader);


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
        TextView Question, Question_Number, Answer;


        News_Card_5(View itemView) {
            super(itemView);
            Question = (TextView) itemView.findViewById(R.id.Question);
            Answer = itemView.findViewById(R.id.answer);
            Question_Number = itemView.findViewById(R.id.Question_Number);
        }
    }


}
