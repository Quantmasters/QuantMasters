package com.Quant.quantmasters;

import java.util.ArrayList;

/**
 * Created by Shreyas on 09-04-2017.
 */

public class Comment_Feed {
    private String id,comment,timeStamp,username,profilepic,replies,video_id;
    private ArrayList<String> genre;
    private  int count;
    private int type;



    public Comment_Feed(){
    }
    public Comment_Feed(String id,String comment, String username, String timeStamp,String replies,int count,String video_id,int type,
                       String profilepic,ArrayList<String> genre){
        super();
        this.id = id;
        this.comment = comment;
        this.username =username;
        this.profilepic = profilepic;
        this.timeStamp = timeStamp;
        this.genre = genre;
        this.count =count;
        this.replies = replies;
        this.video_id = video_id;
        this.type = type;

    }
    public String Get_Comment_Id() {
        return id;
    }
    public void Set_Comment_Id(String id) {
        this.id = id;
    }
    public String Get_Profile_Pic() {
        return profilepic;
    }

    public void Set_Profile_Pic(String profilepic) {
        this.profilepic = profilepic;
    }
    public String Get_Comment() {
        return comment;
    }

    public void Set_Comment(String comment) {
        this.comment = comment;
    }

    public String Get_Time() {
        return timeStamp;
    }

    public void Set_Time(String timeStamp) {
        this.timeStamp =timeStamp;
    }
    public String Get_User_Name() {
        return username;
    }

    public void Set_User_Name(String username) {
        this.username =  username;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

    public String Get_Replies() {
        return replies;
    }

    public void Set_Replies(String replies) {
        this.replies =  replies;
    }

    public String Get_Video_Id() {
        return video_id;
    }

    public void Set_Video_Id(String video_id) {
        this.video_id =  video_id;
    }

    public int Get_Count() {
        return count;
    }

    public void Set_Count(int count) {
        this.count =  count;
    }

    public int Get_Type() {
        return type;
    }

    public void Set_Type(int type) {
        this.type =  type;
    }

}
