package com.example.tablayouttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class CalendarWriteActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    EditText contentText;

    TextView dateText;
    TextView startTimeText;
    TextView endTimeText;

    Button dateButton;
    Button startTimeButton;
    Button endTimeButton;

    Button addBtn;

    // 시작하는 시각을 고르는 중인지, 끝나는 시각을 고르는 중인지 확인하기 위한 변수.
    // 시작 시각을 선택하는 다이얼로그를 터치할 때 1이 되고
    // 끝나는 시각을 선택하는 다이얼로그를 터치할 때 2가 됨.
    int state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_write);

        contentText = findViewById(R.id.calendar_write_content_text);

        dateText = findViewById(R.id.calendar_write_date_text);
        startTimeText = findViewById(R.id.calendar_write_start_time_text);
        endTimeText = findViewById(R.id.calendar_write_end_time_text);

        dateButton = findViewById(R.id.calendar_write_date_button);
        startTimeButton = findViewById(R.id.calendar_write_start_time_button);
        endTimeButton = findViewById(R.id.calendar_write_end_time_button);

        addBtn = findViewById(R.id.calendar_add_btn);
        addBtn.setOnClickListener(this);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new CalendarDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showStartTimePickerDialog(View v) {
        state = 1;
        DialogFragment newFragment = new CalendarTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "startTimePicker");
    }

    public void showEndTimePickerDialog(View v) {
        state = 2;
        DialogFragment newFragment = new CalendarTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "endTimePicker");
    }

    @Override
    public void onClick(View view) {

        String content = contentText.getText().toString();
        String date = dateText.getText().toString();
        String startTime = startTimeText.getText().toString();
        String endTime = endTimeText.getText().toString();

        CalendarDBHelper helper = new CalendarDBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into tb_calendar (content, date, start_time, end_time) values (?, ?, ?, ?)", new String[]{content, date, startTime, endTime});
        db.close();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        month = month + 1;
        String strMonth = null;
        String strDay = null;

        // 1월을 01월로 바꿔주는 작업
        if(month < 10){
            strMonth = 0 + Integer.toString(month);
        } else {
            strMonth = Integer.toString(month);
        }

        // 1일을 01일로 바꿔주는 작업
        if(day < 10){
            strDay = 0 + Integer.toString(day);
        } else {
            strDay = Integer.toString(day);
        }

        String output = year + "년 " + strMonth + "월 " + strDay + "일";
        dateText.setText(output);
        dateText.setTextColor(getResources().getColor(R.color.gray9));
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        String strHourOfDay = null;
        String strMinute = null;

        // 1시를 01시로 바꿔주는 작업
        if(hourOfDay < 10){
            strHourOfDay = 0 + Integer.toString(hourOfDay);
        } else {
            strHourOfDay = Integer.toString(hourOfDay);
        }

        // 1분을 01분으로 바꿔주는 작업
        if(minute < 10){
            strMinute = 0 + Integer.toString(minute);
        } else {
            strMinute = Integer.toString(minute);
        }

        if(state == 1){

            String output = strHourOfDay + "시 " + strMinute + "분";
            startTimeText.setText(output);
            startTimeText.setTextColor(getResources().getColor(R.color.gray9));
        }else if (state == 2){

            String output = strHourOfDay + "시 " + strMinute + "분";
            endTimeText.setText(output);
            endTimeText.setTextColor(getResources().getColor(R.color.gray9));
        }
    }
}