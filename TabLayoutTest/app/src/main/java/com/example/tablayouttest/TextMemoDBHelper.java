package com.example.tablayouttest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TextMemoDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public TextMemoDBHelper(Context context) {
        super(context, "text_memo_db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String text_memo_SQL = "create table tb_text_memo (" +
                "_id integer primary key autoincrement, " +
                "title, " +
                "content)";

        db.execSQL(text_memo_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION) {
            db.execSQL("drop table tb_text_memo");
            onCreate(db);
        }
    }
}
