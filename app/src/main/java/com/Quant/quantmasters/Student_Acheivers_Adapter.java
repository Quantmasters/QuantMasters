package com.Quant.quantmasters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Student_Acheivers_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Video_List> chapter_lists;
    private Context context;

    public Student_Acheivers_Adapter(Context context, List<Video_List> chapter_lists) {
        this.chapter_lists = chapter_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acheiver_card, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Video_List feeds = chapter_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");
        Log.d("namez", feeds.Get_Name());
        ((News_Card_5) holder).Name.setTypeface(typeface2);
        ((News_Card_5) holder).Name.setText(feeds.Get_Name());

        ((News_Card_5) holder).Company.setTypeface(typeface2);
        ((News_Card_5) holder).Company.setText(feeds.Get_Description());


        ((News_Card_5) holder).Batch.setTypeface(typeface2);
        ((News_Card_5) holder).Batch.setText("Batch : "+feeds.Get_Length());

        Glide.with(context)
                .load(feeds.Get_Image())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(((News_Card_5) holder).Image);

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
        CircleImageView Image;
        TextView Name, Company, Batch;


        News_Card_5(View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.Image);
            Name = itemView.findViewById(R.id.Name);
            Company = itemView.findViewById(R.id.Company);
            Batch = itemView.findViewById(R.id.Batch);
        }
    }


}