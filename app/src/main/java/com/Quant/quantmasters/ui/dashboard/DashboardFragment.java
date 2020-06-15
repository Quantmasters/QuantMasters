package com.Quant.quantmasters.ui.dashboard;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.Quant.quantmasters.Chapter_Level;
import com.Quant.quantmasters.Company_Specific;
import com.Quant.quantmasters.Competitive;
import com.Quant.quantmasters.Model_Question;
import com.Quant.quantmasters.R;
import com.bumptech.glide.Glide;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        RelativeLayout chapter = root.findViewById(R.id.chapter);
        RelativeLayout mcq = root.findViewById(R.id.mcq);
        RelativeLayout Company_specific = root.findViewById(R.id.Company_specific);
        RelativeLayout competitive = root.findViewById(R.id.competitive);
        Typeface typeface2 = Typeface.createFromAsset(getContext().getAssets(), "SourceSansProRegular.otf");
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "SourceSansProBold.otf");
        TextView chapter_text = root.findViewById(R.id.chapter_text);
        TextView mq_text = root.findViewById(R.id.mq_text);
        TextView c_text = root.findViewById(R.id.c_text);
        ImageView mcq_image = root.findViewById(R.id.mcq_image);
        ImageView company_specific_image = root.findViewById(R.id.company_specific_image);
        TextView cs_text = root.findViewById(R.id.cs_text);
        ImageView chapter_image = root.findViewById(R.id.chapter_image);
        ImageView competitive_image = root.findViewById(R.id.competitive_image);
        chapter_text.setTypeface(typeface);
        mq_text.setTypeface(typeface);
        cs_text.setTypeface(typeface);
        c_text.setTypeface(typeface);



        chapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Chapter_Level.class);
                startActivity(intent);
            }
        });


        mcq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Model_Question.class);
                startActivity(intent);
            }
        });

        Company_specific.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Company_Specific.class);
                startActivity(intent);
            }
        });

        competitive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Competitive.class);
                startActivity(intent);
            }
        });

        return root;
    }
}