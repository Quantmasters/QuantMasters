package com.Quant.quantmasters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Shreyas on 09-01-2018.
 */

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Buyer.db";
    public static final String TABLE_NAME = "buyer_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "EMAIL";
    public static final String COL_4 = "TOKEN";
    public static final String COL_5 = "SUBSCRIBE";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 14);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER ,NAME TEXT,EMAIL TEXT,TOKEN TEXT,SUBSCRIBE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int id, String name, String email, String token, String subscribe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, token);
        contentValues.put(COL_5, subscribe);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("id", cursor.getString(0));
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("token", cursor.getString(3));
            user.put("subscribe", cursor.getString(4));
        }
        cursor.close();
        db.close();
        return user;
    }


    public boolean updateToken(String token, String email, String subscribe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_4, token);
        contentValues.put(COL_5, subscribe);
        db.update(TABLE_NAME, contentValues, "EMAIL = ?", new String[]{email});
        Log.d("Message", "sub and token updated");
        return true;
    }

    public boolean updateName(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        db.update(TABLE_NAME, contentValues, "EMAIL = ?", new String[]{email});
        Log.d("Token name", name);
        return true;
    }




    public void deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "NAME = ?", new String[]{name});
        db.close();
    }

    public void deleteDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteall() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        db.close();
    }
}