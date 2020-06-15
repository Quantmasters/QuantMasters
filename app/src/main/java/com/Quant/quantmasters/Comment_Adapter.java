package com.Quant.quantmasters;

/**
 * Created by Shreyas on 09-04-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


class Comment_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Comment_Feed> comment_feeds;
    private Context context;


    Comment_Adapter(Context context, List<Comment_Feed> comment_feeds) {
        this.comment_feeds = comment_feeds;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent, false);

        return new Comment_Card(view);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final Comment_Feed feeds = comment_feeds.get(position);

        ((Comment_Card) holder).user_name.setText(feeds.Get_User_Name());
        String decode = StringEscapeUtils.unescapeJava(feeds.Get_Comment());

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");
        Typeface typeface1 = Typeface.createFromAsset(context.getAssets(), "sourcesans.otf");
        ((Comment_Card) holder).content.setText(decode);
        String test = feeds.Get_User_Name();
        char first = test.charAt(0);

        ((Comment_Card) holder).user_name.setTypeface(typeface2);
        ((Comment_Card) holder).content.setTypeface(typeface1);


        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(typeface1)
                .fontSize(35) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(String.valueOf(first), Color.rgb(33, 150, 234));

        ((Comment_Card) holder).comment_pic.setImageDrawable(drawable);
        ((Comment_Card) holder).comment_pic.setVisibility(View.VISIBLE);
        ((Comment_Card) holder).reply.setTypeface(typeface1);

        if (feeds.Get_Count() != 0) {
            ((Comment_Card) holder).reply.setText("View "+feeds.Get_Count() + " Replies");
        } else {
            ((Comment_Card) holder).reply.setText("Reply");
        }


        ((Comment_Card) holder).reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Reply_Activity.class);
                intent.putExtra("comment_id", feeds.Get_Comment_Id());
                intent.putExtra("video_id", feeds.Get_Video_Id());
                intent.putExtra("replies", feeds.Get_Replies());
                intent.putExtra("time",feeds.Get_Time());
                intent.putExtra("name",feeds.Get_User_Name());
                intent.putExtra("comment",feeds.Get_Comment());

                context.startActivity(intent);
            }
        });



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy ");
        SimpleDateFormat output2 = new SimpleDateFormat("HH:mm a");

        Date d = null;
        try {
            d = sdf.parse(feeds.Get_Time());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String formattedTime = output.format(d);
        String formattedTime2 = output2.format(d);





        ((Comment_Card) holder).time.setText(formattedTime + "at "+formattedTime2);


        if (feeds.Get_Profile_Pic() != null) {
            Glide.with(context)
                    .load(feeds.Get_Profile_Pic())
                    .fitCenter()
                    .override(50, 50)
                    .dontAnimate()
                    .skipMemoryCache(false)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            ((Comment_Card) holder).comment_pic_two.setVisibility(View.VISIBLE);
                            ((Comment_Card) holder).comment_pic.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(((Comment_Card) holder).comment_pic_two);
        }
    }

    @Override
    public int getItemCount() {
        return comment_feeds.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    private static class Comment_Card extends RecyclerView.ViewHolder {
        ImageView comment_pic;
        TextView user_name, content, time, reply;
        CircleImageView comment_pic_two;
        RelativeLayout Relative;

        Comment_Card(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            content = (TextView) itemView.findViewById(R.id.Full_Name);
            comment_pic = (ImageView) itemView.findViewById(R.id.comment_pic);
            Relative = (RelativeLayout) itemView.findViewById(R.id.Relative);
            reply = (TextView) itemView.findViewById(R.id.reply);

        }
    }


}
