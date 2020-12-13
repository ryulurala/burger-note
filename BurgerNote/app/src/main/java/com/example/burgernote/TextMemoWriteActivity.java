package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextMemoWriteActivity extends AppCompatActivity implements View.OnClickListener {

    EditText contentView;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_memo_write);

        contentView = findViewById(R.id.text_memo_add_content);
        addBtn = findViewById(R.id.text_memo_add_btn);

        addBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        String content = contentView.getText().toString();

        // 지금 시간을 yyyy년 MN월 dd일 HH시 mm분 포맷의 문자열로 저장함.
        String date = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분").format(new Date());


        TextMemoDBHelper helper = new TextMemoDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into tb_text_memo (content, date) values (?, ?)", new String[]{content, date});
        db.close();

        Toast toast = Toast.makeText(getApplicationContext(), "입력이 완료되었습니다,", Toast.LENGTH_SHORT );
        toast.show();
    }
}