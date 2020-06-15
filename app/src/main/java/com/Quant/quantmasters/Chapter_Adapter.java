package com.Quant.quantmasters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by Shreyas on 21-06-2017.
 * <p>
 */


public class Chapter_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Chapter_List> chapter_lists;
    private Context context;

    public Chapter_Adapter(Context context, List<Chapter_List> chapter_lists) {
        this.chapter_lists = chapter_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_round_chapter, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Chapter_List feeds = chapter_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");

        ((News_Card_5) holder).chapter_name.setText(feeds.Get_Name());

       // ((News_Card_5) holder).topic_number.setText("Number of Topics : "+ feeds.Get_Number());
        //((News_Card_5) holder).topic_number.setTypeface(typeface2);
        ((News_Card_5) holder).chapter_name.setTypeface(typeface2);

        ((News_Card_5) holder).relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Video_Activity.class);
                intent.putExtra("video_id", feeds.Get_Id());
                intent.putExtra("video_title", feeds.Get_Name());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapter_lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    private static class News_Card_5 extends RecyclerView.ViewHolder {
        TextView chapter_name, topic_number;
        RelativeLayout relative;


        News_Card_5(View itemView) {
            super(itemView);
            chapter_name = (TextView) itemView.findViewById(R.id.chapter_name);
           // topic_number = (TextView) itemView.findViewById(R.id.topic_number);
            relative = itemView.findViewById(R.id.relative);
        }
    }


}
