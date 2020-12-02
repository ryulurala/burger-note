package com.example.burgernote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DrawingMemoDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public DrawingMemoDBHelper(Context context) {
        super(context, "drawing_memo_db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String drawing_memo_SQL = "create table tb_drawing_memo (" +
                "_id integer primary key autoincrement, " +
                "image, " +
                "date)";

        db.execSQL(drawing_memo_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION) {
            db.execSQL("drop table tb_drawing_memo");
            onCreate(db);
        }
    }
}
