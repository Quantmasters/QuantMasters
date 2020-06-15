package com.Quant.quantmasters;

/**
 * Created by Shreyas on 08-12-2017.
 */

public class Question_List {

    private String id,question_number,question,option_one,option_two,option_three,option_four,option_five,year,right_answer,method;
    private int flag,check_one,check_two,check_three,check_four,check_five,solution,right_flag,user_answer,click;


    public Question_List() {
    }

    public Question_List(String id, String question_number,String question, String option_one,String option_two,String option_three,String option_four,String right_answer,String method,
                         int flag,String option_five,int check_one,int check_two,int check_three,int check_four,int check_five,int solution,int right_flag,String year,int user_answer,int click) {
        super();
        this.question_number = question_number;
        this.id = id;
        this.option_one = option_one;
        this.option_two = option_two;
        this.option_three = option_three;
        this.question = question;
        this.right_answer = right_answer;
        this.option_four = option_four;
        this.flag = flag;
        this.option_five = option_five;
        this.check_one = check_one;
        this.check_two = check_two;
        this.method = method;
        this.check_three = check_three;
        this.check_four = check_four;
        this.check_five = check_five;
        this.solution = solution;
        this.right_flag = right_flag;
        this.year = year;
        this.user_answer = user_answer;
        this.click = click;
    }

    public String Get_Id() {
        return id;
    }

    public void Set_Id(String id) {
        this.id = id;
    }

    public int Get_One() {
        return check_one;
    }

    public void Set_One(int check_one) {
        this.check_one = check_one;
    }


    public int Get_Two() {
        return check_two;
    }

    public void Set_Two(int check_two) {
        this.check_two = check_two;
    }


    public int Get_Three() {
        return check_three;
    }

    public void Set_Three(int check_three) {
        this.check_three = check_three;
    }



    public int Get_Four() {
        return check_four;
    }

    public void Set_Four(int check_four) {
        this.check_four = check_four;
    }


    public int Get_Five() {
        return check_five;
    }

    public void Set_Five(int check_five) {
        this.check_five = check_five;
    }


    public int Get_User_Answer() {
        return user_answer;
    }

    public void Set_User_Answer(int user_answer) {
        this.user_answer = user_answer;
    }


    public int Get_Solution() {
        return solution;
    }

    public void Set_Solution(int solution) {
        this.solution = solution;
    }


    public String Get_Question() {
        return question;
    }

    public void Set_Question(String question) {
        this.question = question;
    }

    public String Get_Option_One() {
        return option_one;
    }

    public void Set_Option_One(String option_one) {
        this.option_one = option_one;
    }

    public String Get_Right_Answer() {
        return right_answer;
    }

    public void Set_Right_Answer(String right_answer) {
        this.right_answer = right_answer;
    }

    public String Get_Option_Two() {
        return option_two;
    }

    public void Set_Option_Two(String option_two) {
        this.option_two = option_two;
    }

    public String Get_Option_Three() {
        return option_three;
    }

    public void Set_Option_Three(String option_three) {
        this.option_three = option_three;
    }

    public String Get_Option_Four() {
        return option_four;
    }

    public void Set_Option_Four(String option_four) {
        this.option_four = option_four;
    }

    public String Get_Option_Five() {
        return option_five;
    }

    public void Set_Option_Five(String option_five) {
        this.option_five = option_five;
    }



    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }



    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }


    public int getRight_flag() {
        return right_flag;
    }

    public void setRight_flag(int right_flag) {
        this.right_flag = right_flag;
    }

    public String Get_year() {
        return year;
    }

    public void Set_year(String year) {
        this.year = year;
    }


    public String Get_Method() {
        return method;
    }

    public void Set_Method(String method) {
        this.method = method;
    }


}
