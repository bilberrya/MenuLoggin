package com.example.menu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "McDonaldsDb";
    public static final String TABLE_MENU = "Menu";
    public static final String TABLE_USERS = "Users";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PRICE = "price";

    public static final String KEY_ID2 = "_id2";
    public static final String KEY_USER = "user";
    public static final String KEY_PASSWORD = "password";

    public DBHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MENU + " (" +
                    KEY_ID + " integer primary key," +
                    KEY_NAME + " text," +
                    KEY_PRICE + " float" + ")");

        db.execSQL("create table " + TABLE_USERS + " (" +
                    KEY_ID2 + " integer primary key," +
                    KEY_USER  + " text," +
                    KEY_PASSWORD + " text" + ")");


        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_MENU);
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);
        }
    }
