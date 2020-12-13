package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarPlay extends AppCompatActivity implements View.OnClickListener {

    int id;
    String calendar_content;
    String calendar_date;
    String calendar_start_time;
    String calendar_end_time;

    TextView calendarContent;
    TextView calendarDate;
    TextView calendarStartTime;
    TextView calendarEndTime;

    Button deleteBtn;
    ImageButton closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_play);

        Intent intent = new Intent(this.getIntent());
        id = intent.getIntExtra("id", 0);
        calendar_content = intent.getStringExtra("calendar_content");
        calendar_date = intent.getStringExtra("calendar_date");
        calendar_start_time = intent.getStringExtra("calendar_start_time");
        calendar_end_time = intent.getStringExtra("calendar_end_time");

        closeBtn = findViewById(R.id.calendar_close_button);
        deleteBtn = findViewById(R.id.calendar_delete_button);

        calendarContent = findViewById(R.id.calendar_play_content);
        calendarDate = findViewById(R.id.calendar_play_date);
        calendarStartTime = findViewById(R.id.calendar_play_start_time);
        calendarEndTime = findViewById(R.id.calendar_play_end_time);

        closeBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        calendarContent.setText(calendar_content);
        calendarDate.setText(calendar_date);
        calendarStartTime.setText(calendar_start_time);
        calendarEndTime.setText(calendar_end_time);
        calendarContent.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calendar_close_button: {
                finish();
                break;
            }

            case R.id.calendar_delete_button: {

                CalendarDBHelper helper = new CalendarDBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                // db.beginTransaction();
                String sql = "DELETE FROM tb_calendar where _id='"+ id +"'";
                db.execSQL(sql);

                //db.endTransaction();
                db.close();

                Toast.makeText(this, "일정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }
    }
}