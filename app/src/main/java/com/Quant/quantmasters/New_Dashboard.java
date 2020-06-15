package com.Quant.quantmasters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class New_Dashboard extends AppCompatActivity {
    RecyclerView recycler_views;
    ProgressBar progressBar;
    Dash_Board_Adapter dash_board_adapter;
    private List<Chapter_List> chapter_lists = new ArrayList<Chapter_List>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_dashboard);

        TextView test_result = findViewById(R.id.test_result);
        recycler_views = (RecyclerView) findViewById(R.id.recycler_views);
        Typeface bold = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
        Typeface fontz = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
        Typeface fontx = Typeface.createFromAsset(getAssets(), "SourceSansProLight.otf");

        TextView title = findViewById(R.id.title);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        title.setText("Dashboard");
        title.setTypeface(bold);
        ImageButton back = (ImageButton) findViewById(R.id.back);
        TextPaint paint = title.getPaint();
        float width = paint.measureText("Dashboard");
        Shader textShader = new LinearGradient(0, 0, width, title.getTextSize(),
                new int[]{
                        Color.parseColor("#177ED5"),
                        Color.parseColor("#16B1E9"),
                }, null, Shader.TileMode.CLAMP);
        title.getPaint().setShader(textShader);
        test_result.setTypeface(bold);

       // TextPaint paintz = test_result.getPaint();
       // float widthz = paintz.measureText("View Test Result");
       // Shader textShaderz = new LinearGradient(0, 0, widthz, test_result.getTextSize(),
        //        new int[]{
        //                Color.parseColor("#E46478"),
         //               Color.parseColor("#FB933A"),
          //      }, null, Shader.TileMode.CLAMP);
       // test_result.getPaint().setShader(textShaderz);
      //  test_result.setTypeface(bold);

        dash_board_adapter = new Dash_Board_Adapter(New_Dashboard.this, chapter_lists);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dash_board_adapter);
        // [{"id":"1","name":"Chapters"},{"id":"2","name":"Model Question Paper"},{"id":"3","name":"Competitive"},{"id":"4","name":"Company Specific"}]
        String response = "[{\"id\":\"1\",\"name\":\"Chapters\"},{\"id\":\"2\",\"name\":\"Model Question Paper\"},{\"id\":\"3\",\"name\":\"Competitive\"},{\"id\":\"4\",\"name\":\"Company Specific\"}]";
        try {
            JSONArray feedArray = new JSONArray(response);
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                Chapter_List chapter_list = new Chapter_List();
                chapter_list.Set_Flag(feedObj.getInt("id"));
                chapter_list.Set_Name(feedObj.getString("name"));
                chapter_lists.add(chapter_list);

            }
            dash_board_adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        test_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(New_Dashboard.this, Marks_Activity.class);
               startActivity(intent1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}