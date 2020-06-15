package com.Quant.quantmasters;

/**
 * Created by Shreyas on 08-12-2017.
 */

public class Chapter_List {

    private String id,name,number;
    private int num,flag,time,type;

    public Chapter_List() {
    }

    public Chapter_List(String id, String name, String number,int num,int flag,int time,int type) {
        super();
        this.name = name;
        this.id = id;
        this.number = number;
        this.flag = flag;
        this.num = num;
        this.time = time;
        this.type = type;
     }

    public String Get_Id() {
        return id;
    }

    public void Set_Id(String id) {
        this.id = id;
    }


    public int Get_Type() {
        return type;
    }

    public void Set_Type(int type) {
        this.type = type;
    }

    public String Get_Name() {
        return name;
    }

    public void Set_Name(String name) {
        this.name = name;
    }

    public String Get_Number() {
        return number;
    }

    public void Set_Number(String number) {
        this.number = number;
    }


    public int Get_Num() {
        return num;
    }

    public void Set_Num(int num) {
        this.num = num;
    }

    public int Get_Flag() {
        return flag;
    }

    public void Set_Flag(int flag) {
        this.flag = flag;
    }

    public int Get_Time() {
        return time;
    }

    public void Set_Time(int time) {
        this.time = time;
    }
}
