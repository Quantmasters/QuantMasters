
package com.Quant.quantmasters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Shreyas on 09-01-2018.
 */

public class Answer_Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "answer.db";
    public static final String TABLE_NAME = "order_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";


    public Answer_Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY ,NAME TEXT DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_1, id);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }


    public boolean updateName(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        Log.d("Token name", name);
        return true;
    }


    public void deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "NAME = ?", new String[]{name});
        db.close();
    }

    void deleteall() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        db.close();
    }


    public HashMap<String, String> getAnswer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, String> userz = new HashMap<String, String>();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_1,
                        COL_2}, COL_1 + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            userz.put("id", cursor.getString(0));
            userz.put("name", cursor.getString(1));
        }
        cursor.close();
        db.close();
        return userz;
    }
}