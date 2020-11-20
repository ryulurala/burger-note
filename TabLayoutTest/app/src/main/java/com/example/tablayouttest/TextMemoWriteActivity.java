package com.example.tablayouttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TextMemoWriteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText titleView;
    EditText contentView;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_memo_write);

        titleView = findViewById(R.id.text_memo_add_title);
        contentView = findViewById(R.id.text_memo_add_content);
        addBtn = findViewById(R.id.text_memo_add_btn);

        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        String title = titleView.getText().toString();
        String content = contentView.getText().toString();

        TextMemoDBHelper helper = new TextMemoDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into tb_text_memo (title, content) values (?, ?)", new String[]{title, content});
        db.close();

        Intent intent = new Intent(this, ReadDBActivity.class);
        startActivity(intent);
    }
}