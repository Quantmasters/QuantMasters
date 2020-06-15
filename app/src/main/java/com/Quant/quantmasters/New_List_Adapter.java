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


public class New_List_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Chapter_List> chapter_lists;
    private Context context;

    public New_List_Adapter(Context context, List<Chapter_List> chapter_lists) {
        this.chapter_lists = chapter_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_cards, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Chapter_List feeds = chapter_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");
        Log.d("namez", feeds.Get_Name());
        ((News_Card_5) holder).title.setTypeface(typeface2);
        ((News_Card_5) holder).title.setText(feeds.Get_Name());
        switch (position) {
            case 0:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad));
                break;
            case 1:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_blue));
                break;
            case 2:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_green));
                break;
            case 3:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_pink));
                break;
            case 4:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_light_blue));
                break;
            case 5:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_light_pink));
                break;
            case 6:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_pink));
                break;
            case 7:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_blue));
                break;
            case 8:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_light_pink));
                break;
            case 9:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_green));
                break;
            case 10:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_light_blue));
                break;
            case 11:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_blue));
                break;
            case 12:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_light_pink));
                break;
            case 13:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_pink));
                break;
            case 14:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad_blue));
                break;
            default:
                ((News_Card_5) holder).layoutz.setBackground(ContextCompat.getDrawable(context, R.drawable.new_grad));
                break;

        }


        switch (feeds.Get_Type()) {
            case 1:
                ((News_Card_5) holder).layoutz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, Preview_Activity.class);
                        intent.putExtra("Video_Title", feeds.Get_Name());
                        intent.putExtra("Video_Group_Id", feeds.Get_Id());
                        context.startActivity(intent);
                    }
                });
                break;
            case 2:
                ((News_Card_5) holder).layoutz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, Review_Activity.class);
                        intent.putExtra("Video_Title", feeds.Get_Name());
                        context.startActivity(intent);
                    }
                });
                break;
            case 3:
                ((News_Card_5) holder).layoutz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, Testimony.class);
                        intent.putExtra("Video_Title", feeds.Get_Name());
                        context.startActivity(intent);
                    }
                });
                break;
            case 4:
                ((News_Card_5) holder).layoutz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, Free_Video.class);
                        intent.putExtra("Video_Title", feeds.Get_Name());
                        context.startActivity(intent);
                    }
                });
                break;

            case 5:
                ((News_Card_5) holder).layoutz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, Free_Test_Papers.class);
                        intent.putExtra("Video_Title", feeds.Get_Name());
                        context.startActivity(intent);
                    }
                });
                break;

            case 6:
                ((News_Card_5) holder).layoutz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, Student_Achievers.class);
                        intent.putExtra("Video_Title", feeds.Get_Name());
                        context.startActivity(intent);
                    }
                });
                break;

            case 7:
                ((News_Card_5) holder).layoutz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(context, Live_Workshop.class);
                        intent.putExtra("Video_Title", feeds.Get_Name());
                        context.startActivity(intent);
                    }
                });
                break;


        }


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
        ImageView image;
        TextView title;
        RelativeLayout layoutz;


        News_Card_5(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            layoutz = itemView.findViewById(R.id.layoutz);

        }
    }


}