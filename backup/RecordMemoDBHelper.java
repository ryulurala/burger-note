package com.example.burgernote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class RecordMemoDBHelper extends SQLiteOpenHelper {





    public static final int DATABASE_VERSION = 1;

    public RecordMemoDBHelper(Context context) {
        super(context, "record_memo_db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String record_memo_SQL = "create table tb_record_memo (" +
                "_id integer primary key autoincrement," +
                "title," +
                "length," +
                "date)";
        db.execSQL(record_memo_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == DATABASE_VERSION) {
            db.execSQL("drop table tb_record_memo");
            onCreate(db);
        }
    }
}

