package com.example.tablayouttest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalendarDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public CalendarDBHelper(Context context) {
        super(context, "calendar_db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String text_memo_SQL = "create table tb_calendar (" +
                "_id integer primary key autoincrement, " +
                "content, " +
                "date, " +
                "start_time, " +
                "end_time)";

        db.execSQL(text_memo_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION) {
            db.execSQL("drop table tb_calendar");
            onCreate(db);
        }
    }
}
