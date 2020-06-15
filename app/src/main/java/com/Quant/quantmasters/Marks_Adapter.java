package com.Quant.quantmasters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Marks_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Marks_List> marks_lists;
    private Context context;

    public Marks_Adapter(Context context, List<Marks_List> marks_lists) {
        this.marks_lists = marks_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marks_list, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Marks_List feeds = marks_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");


        ((News_Card_5) holder).Type.setText(feeds.Get_Type());
        ((News_Card_5) holder).Time.setText(feeds.Get_Time());
        ((News_Card_5) holder).Marks.setText(feeds.Get_Marks()+"/" +feeds.Get_Total() );

        TextPaint paint =   ((News_Card_5) holder).name.getPaint();
        float width = paint.measureText(feeds.Get_Name());
        Shader textShader = new LinearGradient(0, 0, width,  ((News_Card_5) holder).name.getTextSize(),
                new int[]{
                        Color.parseColor("#FF03B94D"),
                        Color.parseColor("#FF039742"),
                }, null, Shader.TileMode.CLAMP);
        ((News_Card_5) holder).name.getPaint().setShader(textShader);
        ((News_Card_5) holder).name.setText(feeds.Get_Name());



        ((News_Card_5) holder).name.setTypeface(typeface);
        ((News_Card_5) holder).Type.setTypeface(typeface2);
        ((News_Card_5) holder).Time.setTypeface(typeface2);
        ((News_Card_5) holder).Marks.setTypeface(typeface2);
        ((News_Card_5) holder).Marks_text.setTypeface(typeface2);



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd MMM ");
        SimpleDateFormat output2 = new SimpleDateFormat("HH:mm a");

        Date d = null;
        try {
            d = sdf.parse(feeds.Get_Time());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String formattedTime = output.format(d);
        String formattedTime2 = output2.format(d);





        ((News_Card_5) holder).Time.setText("Test Taken on "+formattedTime + "at "+formattedTime2);



    }

    @Override
    public int getItemCount() {
        return marks_lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    private static class News_Card_5 extends RecyclerView.ViewHolder {
        TextView name, Type,Time,Marks,Marks_text;
        News_Card_5(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            Type = (TextView) itemView.findViewById(R.id.Type);
            Time = (TextView) itemView.findViewById(R.id.Time);
            Marks = (TextView) itemView.findViewById(R.id.Marks);
            Marks_text = (TextView) itemView.findViewById(R.id.Marks_text);
        }
    }


}
