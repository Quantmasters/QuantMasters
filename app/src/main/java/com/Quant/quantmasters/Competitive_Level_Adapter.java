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
        import android.widget.ImageButton;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import java.util.List;

/**
 * Created by Shreyas on 21-06-2017.
 * <p>
 */


public class Competitive_Level_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Chapter_List> chapter_lists;
    private Context context;

    public Competitive_Level_Adapter(Context context, List<Chapter_List> chapter_lists) {
        this.chapter_lists = chapter_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_chapter_list, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Chapter_List feeds = chapter_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");
        ((News_Card_5) holder).chapter_name.setText(feeds.Get_Name());
        ((News_Card_5) holder).topic_number.setText(feeds.Get_Number());
        ((News_Card_5) holder).topic_number.setTypeface(typeface2);
        ((News_Card_5) holder).chapter_name.setTypeface(typeface2);
        if(feeds.Get_Flag()==1){
            TextPaint paint =    ((News_Card_5) holder).topic_number.getPaint();
            float width = paint.measureText("Open");
            Shader textShader = new LinearGradient(0, 0, width,    ((News_Card_5) holder).topic_number.getTextSize(),
                    new int[]{
                            Color.parseColor("#177ED5"),
                            Color.parseColor("#16B1E9"),
                    }, null, Shader.TileMode.CLAMP);
            ((News_Card_5) holder).topic_number.getPaint().setShader(textShader);
            ((News_Card_5) holder).topic_number.setText("Open");
        }else{
            TextPaint paint =    ((News_Card_5) holder).topic_number.getPaint();
            float width = paint.measureText("Locked");
            Shader textShader = new LinearGradient(0, 0, width,    ((News_Card_5) holder).topic_number.getTextSize(),
                    new int[]{
                            Color.parseColor("#E46478"),
                            Color.parseColor("#FB933A"),
                    }, null, Shader.TileMode.CLAMP);
            ((News_Card_5) holder).topic_number.getPaint().setShader(textShader);
            ((News_Card_5) holder).topic_number.setText("Locked");
        }

        ((News_Card_5) holder).relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feeds.Get_Flag()==1) {
                Intent intent = new Intent(context, Competitive_Test.class);
                intent.putExtra("paper_id", feeds.Get_Id());
                intent.putExtra("paper_title", feeds.Get_Name());
                intent.putExtra("time_lim", feeds.Get_Time());
                context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, Payment_Activity.class);
                    context.startActivity(intent);
                }
            }
        });

        ((News_Card_5) holder).next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feeds.Get_Flag()==1) {
                    Intent intent = new Intent(context, Competitive_Test.class);
                    intent.putExtra("paper_id", feeds.Get_Id());
                    intent.putExtra("paper_title", feeds.Get_Name());
                    intent.putExtra("time_lim", feeds.Get_Time());
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(context, Payment_Activity.class);
                    context.startActivity(intent);
                }
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
        ImageButton next;


        News_Card_5(View itemView) {
            super(itemView);
            chapter_name = (TextView) itemView.findViewById(R.id.chapter_name);
            topic_number = (TextView) itemView.findViewById(R.id.topic_number);
            relative = itemView.findViewById(R.id.relative);
            next = itemView.findViewById(R.id.next);
        }
    }


}
