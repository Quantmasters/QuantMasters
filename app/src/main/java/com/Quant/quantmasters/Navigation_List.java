package com.Quant.quantmasters;

/**
 * Created by Shreyas on 09-04-2017.
 */


public class Navigation_List {
    private String Menu;
    private int image,Id;

    public Navigation_List() {
    }

    public Navigation_List(String Menu, int Id, int image) {
        super();
        this.Menu = Menu;
        this.Id = Id;
        this.image = image;


    }

    int Get_Id() {
        return Id;
    }

    public void Set_Id(int Id) {
        this.Id = Id;
    }


    String Get_Menu() {
        return Menu;
    }

    void Set_Menu(String Menu) {
        this.Menu = Menu;
    }

    int Get_Image() {
        return image;
    }

    void Set_Image(int image) {
        this.image = image;
    }


}