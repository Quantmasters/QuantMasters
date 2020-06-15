package com.Quant.quantmasters;


/**
 * Created by Shreyas on 08-12-2017.
 */

public class Course_List {

    private String title, image, rating, price, discount, id,link,group;
    private int flag;

    public Course_List() {
    }

    public Course_List(String title, String id, String rating, String image, String price, String discount,String link,String group,int flag) {
        super();
        this.title = title;
        this.id = id;
        this.rating = rating;
        this.image = image;
        this.price = price;
        this.discount = discount;
        this.link = link;
        this.group = group;
        this.flag = flag;


    }

    public String Get_Title() {
        return title;
    }

    public void Set_Title(String title) {
        this.title = title;
    }

    public String Get_Rating() {
        return rating;
    }

    public void Set_Rating(String rating) {
        this.rating = rating;
    }

    public String Get_Price() {
        return price;
    }

    public void Set_Price(String price) {
        this.price = price;
    }

    public String Get_Group() {
        return group;
    }

    public void Set_Group(String group) {
        this.group = group;
    }


    public String Get_Image() {
        return image;
    }

    public void Set_Image(String image) {
        this.image = image;
    }


    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


}
