package com.Quant.quantmasters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Shreyas on 21-06-2017.
 * <p>
 */


public class Popular_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Course_List> course_lists;
    private Context context;
    private ProgressDialog pDialog;
    private DatabaseHandler db;

    public Popular_Adapter(Context context, List<Course_List> course_lists) {
        this.course_lists = course_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_course_list, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Course_List feeds = course_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");
        ((News_Card_5) holder).title.setText(feeds.Get_Title());

        db = new DatabaseHandler(context);

        Glide.with(context)
                .load("https://quantmasters.in" + feeds.Get_Image())
                .fitCenter()
                .skipMemoryCache(true)
                .into(((News_Card_5) holder).image);

        ((News_Card_5) holder).title.setTypeface(typeface2);


        ((News_Card_5) holder).image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(context, Preview_Activity.class);
                intent.putExtra("video_title", feeds.Get_Title());
                intent.putExtra("v_id", feeds.getID());
                intent.putExtra("v_gr", feeds.Get_Group());
                intent.putExtra("thumb", feeds.Get_Image());
                intent.putExtra("flag", feeds.getFlag());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return course_lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    private static class News_Card_5 extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;


        News_Card_5(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);


        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
