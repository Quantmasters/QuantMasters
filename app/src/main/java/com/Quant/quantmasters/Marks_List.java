package com.Quant.quantmasters;

/**
 * Created by Shreyas on 08-12-2017.
 */

public class Marks_List {

    private String paper_id,paper_name,paper_type,marks,time,total_marks;


    public Marks_List() {
    }

    public Marks_List(String paper_id, String paper_name, String paper_type,String marks,String time,String total_marks) {
        super();
        this.paper_id = paper_id;
        this.paper_name = paper_name;
        this.paper_type = paper_type;
        this.marks = marks;
        this.time = time;
        this.total_marks = total_marks;

    }

    public String Get_Id() {
        return paper_id;
    }

    public void Set_Id(String paper_id) {
        this.paper_id = paper_id;
    }


    public String Get_Type() {
        return paper_type;
    }

    public void Set_Type(String paper_type) {
        this.paper_type = paper_type;
    }

    public String Get_Name() {
        return paper_name;
    }

    public void Set_Name(String paper_name) {
        this.paper_name = paper_name;
    }



    public String Get_Marks() {
        return marks;
    }

    public void Set_Marks(String marks) {
        this.marks = marks;
    }


    public String Get_Time() {
        return time;
    }

    public void Set_Time(String time) {
        this.time = time;
    }


    public String Get_Total() {
        return total_marks;
    }

    public void Set_Total(String total_marks) {
        this.total_marks = total_marks;
    }
}
