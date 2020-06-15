package com.Quant.quantmasters;

/**
 * Created by Shreyas on 08-12-2017.
 */

public class Video_List {

    private String id,name,description,image,notes,group,length,rate;
    private int flag;


    public Video_List() {
    }

    public Video_List(String id, String name, String description,String Image,String notes,String group,int flag,String length,String rate) {
        super();
        this.name = name;
        this.id = id;
        this.description = description;
        this.image = image;
        this.notes = notes;
        this.group = group;
        this.flag = flag;
        this.length = length;
        this.rate = rate;
    }

    public String Get_Id() {
        return id;
    }

    public void Set_Id(String id) {
        this.id = id;
    }

    public String Get_Name() {
        return name;
    }

    public void Set_Name(String name) {
        this.name = name;
    }

    public String Get_Description() {
        return description;
    }

    public void Set_Description(String description) {
        this.description = description;
    }

    public String Get_Image() {
        return image;
    }

    public void Set_Image(String image) {
        this.image = image;
    }


    public String Get_Notes() {
        return notes;
    }

    public void Set_Notes(String notes) {
        this.notes = notes;
    }

    public String Get_Group() {
        return group;
    }

    public void Set_Group(String group) {
        this.group = group;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getRating() {
        return rate;
    }

    public void setRating(String rate) {
        this.rate = rate;
    }

    public String Get_Length() {
        return length;
    }

    public void Set_Length(String length) {
        this.length = length;
    }

}
