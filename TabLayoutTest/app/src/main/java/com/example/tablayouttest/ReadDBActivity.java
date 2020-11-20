package com.example.tablayouttest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReadDBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_db);

        TextView titleView = findViewById(R.id.read_title);
        TextView contentView = findViewById(R.id.read_content);

        TextMemoDBHelper helper = new TextMemoDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select title, content from tb_text_memo order by _id desc limit 1", null);
        while(cursor.moveToNext()) {
            titleView.setText(cursor.getString(0));
            contentView.setText(cursor.getString(1));
        }
        db.close();
    }
}