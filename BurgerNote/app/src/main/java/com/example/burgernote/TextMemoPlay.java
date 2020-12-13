package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TextMemoPlay extends AppCompatActivity implements View.OnClickListener {

    int id;
    String text_memo_content;
    String text_memo_date;

    TextView textPlayContent;
    TextView textPlayDate;

    Button deleteBtn;
    ImageButton closeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_memo_play);

        Intent intent = new Intent(this.getIntent());
        id = intent.getIntExtra("id", 0);
        text_memo_content = intent.getStringExtra("text_memo_content");
        text_memo_date = intent.getStringExtra("text_memo_date");

        closeBtn = findViewById(R.id.text_memo_close_button);
        deleteBtn = findViewById(R.id.text_memo_delete_button);
        textPlayContent = findViewById(R.id.text_play_content);
        textPlayDate = findViewById(R.id.text_play_date);

        closeBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        textPlayContent.setText(text_memo_content);
        textPlayContent.setMovementMethod(new ScrollingMovementMethod());
        textPlayDate.setText(text_memo_date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_memo_close_button: {
                finish();
                break;
            }

            case R.id.text_memo_delete_button: {

                TextMemoDBHelper helper = new TextMemoDBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                // db.beginTransaction();
                String sql = "DELETE FROM tb_text_memo where _id='"+ id +"'";
                db.execSQL(sql);

                //db.endTransaction();
                db.close();

                Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }
    }
}