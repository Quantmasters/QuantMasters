package com.Quant.quantmasters;

import android.content.Context;
import android.graphics.Typeface;
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


public class Test_Question_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Question_List> question_lists;
    private Context context;
    private Marks_Database md;
    private Answer_Database ad;
    int marks = 0;

    public Test_Question_Adapter(Context context, List<Question_List> question_lists) {
        this.question_lists = question_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_question_list, parent, false);
        return new News_Card_5(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        md = new Marks_Database(context);
        ad = new Answer_Database(context);

        final Question_List feeds = question_lists.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");

        ((News_Card_5) holder).Question.setText(feeds.Get_Question());
        int num = position;
        num = num+1;
        ((News_Card_5) holder).Question_Number.setText("Q."+num);
        ((News_Card_5) holder).Question.setTypeface(typeface2);
        ((News_Card_5) holder).option_one.setTypeface(typeface2);
        ((News_Card_5) holder).option_two.setTypeface(typeface2);
        ((News_Card_5) holder).option_three.setTypeface(typeface2);
        ((News_Card_5) holder).option_four.setTypeface(typeface2);
        ((News_Card_5) holder).option_five.setTypeface(typeface2);
        ((News_Card_5) holder).Origin.setTypeface(typeface);
        ((News_Card_5) holder).Question_Number.setTypeface(typeface2);

        if (feeds.Get_year()!=null&&!feeds.Get_year().equals("null") && !feeds.Get_year().isEmpty()) {
            ((News_Card_5) holder).Origin.setText("("+feeds.Get_year()+")");
        }else{
            ((News_Card_5) holder).Origin.setVisibility(View.GONE);
        }

        ((News_Card_5) holder).radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.option_one:
                        if (feeds.Get_Solution() == 1) {
                            Log.d("write answer ", "you are rite");
                            if (feeds.getFlag() == 0 && feeds.getRight_flag() == 0) {
                                marks = marks + 1;
                                md.updateData(feeds.Get_Id(), String.valueOf(marks));
                                Log.d("marks", String.valueOf(marks));
                                feeds.setRight_flag(1);
                            }
                            feeds.setFlag(1);

                        } else if (feeds.getRight_flag() == 1) {
                            marks = marks - 1;
                            md.updateData(feeds.Get_Id(), String.valueOf(marks));
                            feeds.setRight_flag(0);
                            feeds.setFlag(0);
                            Log.d("marks", String.valueOf(marks));
                        }
                        ((News_Card_5) holder).option_one.setChecked(true);
                        ad.updateName(String.valueOf(position), String.valueOf(1));
                        feeds.Set_One(1);
                        feeds.Set_Two(0);
                        feeds.Set_Three(0);
                        feeds.Set_Four(0);
                        feeds.Set_Five(0);
                        break;

                    case R.id.option_two:
                        if (feeds.Get_Solution() == 2) {
                            Log.d("write answer ", "you are rite");
                            if (feeds.getFlag() == 0 && feeds.getRight_flag() == 0) {
                                marks = marks + 1;
                                md.updateData(feeds.Get_Id(), String.valueOf(marks));
                                Log.d("marks", String.valueOf(marks));

                                feeds.setRight_flag(1);
                            }
                            feeds.setFlag(1);

                        } else if (feeds.getRight_flag() == 1) {
                            marks = marks - 1;
                            md.updateData(feeds.Get_Id(), String.valueOf(marks));
                            feeds.setRight_flag(0);
                            feeds.setFlag(0);
                            Log.d("marks", String.valueOf(marks));
                        }
                        feeds.Set_Two(1);
                        ((News_Card_5) holder).option_two.setChecked(true);
                        ad.updateName(String.valueOf(position), String.valueOf(2));
                        feeds.Set_One(0);
                        feeds.Set_Three(0);
                        feeds.Set_Four(0);
                        feeds.Set_Five(0);


                        break;

                    case R.id.option_three:
                        if (feeds.Get_Solution() == 3) {
                            Log.d("write answer ", "you are rite");
                            if (feeds.getFlag() == 0 && feeds.getRight_flag() == 0) {
                                marks = marks + 1;
                                md.updateData(feeds.Get_Id(), String.valueOf(marks));
                                Log.d("marks", String.valueOf(marks));
                                feeds.setRight_flag(1);
                            }
                            feeds.setFlag(1);

                        } else if (feeds.getRight_flag() == 1) {
                            marks = marks - 1;
                            md.updateData(feeds.Get_Id(), String.valueOf(marks));
                            feeds.setRight_flag(0);
                            feeds.setFlag(0);
                            Log.d("marks", String.valueOf(marks));
                        }
                        feeds.Set_Three(1);
                        ((News_Card_5) holder).option_three.setChecked(true);
                        ad.updateName(String.valueOf(position), String.valueOf(3));
                        feeds.Set_One(0);
                        feeds.Set_Two(0);
                        feeds.Set_Four(0);
                        feeds.Set_Five(0);
                        Log.d("marks", String.valueOf(marks));
                        break;
                    case R.id.option_four:
                        if (feeds.Get_Solution() == 4) {
                            Log.d("write answer ", "you are rite");
                            if (feeds.getFlag() == 0 && feeds.getRight_flag() == 0) {
                                marks = marks + 1;
                                md.updateData(feeds.Get_Id(), String.valueOf(marks));
                                Log.d("marks", String.valueOf(marks));

                                feeds.setRight_flag(1);
                            }
                            feeds.setFlag(1);

                        } else if (feeds.getRight_flag() == 1) {
                            marks = marks - 1;
                            md.updateData(feeds.Get_Id(), String.valueOf(marks));
                            feeds.setRight_flag(0);
                            feeds.setFlag(0);
                            Log.d("marks", String.valueOf(marks));
                        }
                        feeds.Set_Four(1);
                        feeds.Set_One(0);
                        ((News_Card_5) holder).option_four.setChecked(true);
                        ad.updateName(String.valueOf(position), String.valueOf(4));
                        feeds.Set_Two(0);
                        feeds.Set_Five(0);
                        feeds.Set_Three(0);
                        break;
                    case R.id.option_five:
                        if (feeds.Get_Solution() == 5) {
                            Log.d("write answer ", "you are rite");
                            if (feeds.getFlag() == 0 && feeds.getRight_flag() == 0) {
                                marks = marks + 1;
                                md.updateData(feeds.Get_Id(), String.valueOf(marks));
                                Log.d("marks", String.valueOf(marks));

                                feeds.setRight_flag(1);
                            }
                            feeds.setFlag(1);

                        } else if (feeds.getRight_flag() == 1) {
                            marks = marks - 1;
                            md.updateData(feeds.Get_Id(), String.valueOf(marks));
                            feeds.setRight_flag(0);
                            feeds.setFlag(0);
                            Log.d("marks", String.valueOf(marks));
                        }
                        ((News_Card_5) holder).option_five.setChecked(true);
                        ad.updateName(String.valueOf(position), String.valueOf(5));
                        feeds.Set_Five(1);
                        feeds.Set_Four(0);
                        feeds.Set_One(0);
                        feeds.Set_Two(0);
                        feeds.Set_Five(0);
                        break;

                }
            }
        });


        if (feeds.Get_One() == 1) {
            ((News_Card_5) holder).option_one.setChecked(true);
        } else {
            ((News_Card_5) holder).option_one.setChecked(false);
            ((News_Card_5) holder).option_two.setChecked(false);
            ((News_Card_5) holder).option_three.setChecked(false);
            ((News_Card_5) holder).option_four.setChecked(false);
            ((News_Card_5) holder).option_five.setChecked(false);
        }


        if (feeds.Get_Two() == 1) {
            ((News_Card_5) holder).option_two.setChecked(true);
        } else {

            ((News_Card_5) holder).option_one.setChecked(false);
            ((News_Card_5) holder).option_two.setChecked(false);
            ((News_Card_5) holder).option_three.setChecked(false);
            ((News_Card_5) holder).option_four.setChecked(false);
            ((News_Card_5) holder).option_five.setChecked(false);
        }


        if (feeds.Get_Three() == 1) {
            ((News_Card_5) holder).option_three.setChecked(true);

        } else {
            ((News_Card_5) holder).option_one.setChecked(false);
            ((News_Card_5) holder).option_two.setChecked(false);
            ((News_Card_5) holder).option_three.setChecked(false);
            ((News_Card_5) holder).option_four.setChecked(false);
            ((News_Card_5) holder).option_five.setChecked(false);
        }


        if (feeds.Get_Four() == 1) {
            ((News_Card_5) holder).option_four.setChecked(true);
        } else {
            ((News_Card_5) holder).option_one.setChecked(false);
            ((News_Card_5) holder).option_two.setChecked(false);
            ((News_Card_5) holder).option_three.setChecked(false);
            ((News_Card_5) holder).option_four.setChecked(false);
            ((News_Card_5) holder).option_five.setChecked(false);
        }


        if (feeds.Get_Five() == 1) {
            ((News_Card_5) holder).option_five.setChecked(true);
        } else {
            ((News_Card_5) holder).option_one.setChecked(false);
            ((News_Card_5) holder).option_two.setChecked(false);
            ((News_Card_5) holder).option_three.setChecked(false);
            ((News_Card_5) holder).option_four.setChecked(false);
            ((News_Card_5) holder).option_five.setChecked(false);
        }


        ((News_Card_5) holder).option_one.setText(feeds.Get_Option_One());
        ((News_Card_5) holder).option_two.setText(feeds.Get_Option_Two());
        ((News_Card_5) holder).option_three.setText(feeds.Get_Option_Three());

        if (feeds.Get_Option_Four()!=null&&!feeds.Get_Option_Four().equals("") && !feeds.Get_Option_Four().isEmpty()) {
            ((News_Card_5) holder).option_four.setText(feeds.Get_Option_Four());
        } else {
            ((News_Card_5) holder).option_four.setVisibility(View.GONE);
        }



        if (feeds.Get_Option_Five()!=null&&!feeds.Get_Option_Five().equals("") && !feeds.Get_Option_Five().isEmpty()) {
            ((News_Card_5) holder).option_five.setText(feeds.Get_Option_Five());
        } else {
            ((News_Card_5) holder).option_five.setVisibility(View.GONE);
        }





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
        TextView Question,Origin,Question_Number;
        RadioButton option_four, option_three, option_two, option_one, option_five;
        RadioGroup radio_group;


        News_Card_5(View itemView) {
            super(itemView);
            Question = (TextView) itemView.findViewById(R.id.Question);
            option_four = itemView.findViewById(R.id.option_four);
            option_three = itemView.findViewById(R.id.option_three);
            option_two = itemView.findViewById(R.id.option_two);
            option_one = itemView.findViewById(R.id.option_one);
            option_five = itemView.findViewById(R.id.option_five);
            radio_group = itemView.findViewById(R.id.radio_group);
            Origin = itemView.findViewById(R.id.Origin);
            Question_Number = itemView.findViewById(R.id.Question_Number);
        }
    }


}
