package com.Quant.quantmasters;

/**
 * Created by Shreyas on 08-12-2017.
 */

public class View_Pager_List {

    private String id, name, group_id, group_name, video_link,image_link;
    private int num, type;

    public View_Pager_List() {
    }

    public View_Pager_List(String id, String name, String number, int num, int flag, int time, int type, String group_id, String group_name, String video_link,String image_link) {
        super();
        this.name = name;
        this.id = id;
        this.num = num;
        this.type = type;
        this.group_id = group_id;
        this.group_name = group_name;
        this.image_link = image_link;
        this.video_link = video_link;

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


    public int Get_Num() {
        return num;
    }

    public void Set_Num(int num) {
        this.num = num;
    }


    public String Get_Group_Id() {
        return group_id;
    }

    public void Set_Group_Id(String group_id) {
        this.group_id = group_id;
    }


    public String Get_Group_Name() {
        return group_name;
    }

    public void Set_Group_Name(String group_name) {
        this.group_name = group_name;
    }

    public String Get_Video_Link() {
        return video_link;
    }

    public void Set_Video_Link(String video_link) {
        this.video_link = video_link;
    }

    public String Get_Image_Link() {
        return image_link;
    }

    public void Set_Image_Link(String image_link) {
        this.image_link = image_link;
    }


}
