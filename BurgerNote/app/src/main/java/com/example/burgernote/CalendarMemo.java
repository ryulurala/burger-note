package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CalendarMemo extends Memo implements View.OnClickListener{

    private Context mContext;

    private ClipboardManager mClipboard;

    private EditText mTitle;

    private TextView mDate;
    private TextView mStartTime;
    private TextView mEndTime;

    public CalendarMemo(Context context){
        mContext = context;
        initMemoButton(context);
        setButtonClick();
        initMemoDialog(context, 270, 280);
        setAnimation(context, R.anim.scale_up);
    }

    @SuppressLint("InflateParams")
    @Override
    void initMemoDialog(Context context, int width, int height) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoDialog = (LinearLayout) layoutInflater.inflate(R.layout.dialog_calendar, null);     // 리소스 바꾸기

        mClipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        mMemoDialog.findViewById(R.id.calendar_reset).setOnClickListener(this);
        mMemoDialog.findViewById(R.id.calendar_load).setOnClickListener(this);

        mTitle = mMemoDialog.findViewById(R.id.calendar_title);
        mDate = mMemoDialog.findViewById(R.id.calendar_init_date);
        mStartTime = mMemoDialog.findViewById(R.id.calendar_init_time);
        mEndTime = mMemoDialog.findViewById(R.id.calendar_finish_time);

        mMemoDialog.findViewById(R.id.calendar_write_1).setOnClickListener(this);
        mMemoDialog.findViewById(R.id.calendar_write_2).setOnClickListener(this);
        mMemoDialog.findViewById(R.id.calendar_write_3).setOnClickListener(this);


        super.initMemoDialog(context, width, height);
    }

    @Override
    void setButtonClick() {
        super.setButtonClick();
    }

    @Override
    void initMemoButton(Context context) {
        super.initMemoButton(context);
        mMemoButton.setTag("CalendarButton");
        mMemoButton.setImageResource(R.drawable.button_calendar);      // 리소스 바꾸기
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.calendar_reset:
                clear();
                break;
            case R.id.calendar_load:
                load();
                break;
            default:
                write();
                break;
        }
    }

    void write(){
        Log.d("myLog", "CalendarMemo write()");
        Intent intent = new Intent(mContext, CalendarWriteActivity.class);
        intent.putExtra("CalendarMemo", true);  // 인탠트 보낸 여부

        intent.putExtra("Title", true);         // 제목
        intent.putExtra("Date", true);          // 날짜
        intent.putExtra("StartTime", true);     // 시작 시간
        intent.putExtra("EndTime", true);       // 마감 시간

        mContext.startActivity(intent);
    }

    void load(){
        if (!(mClipboard.hasPrimaryClip()) || !(mClipboard.getPrimaryClipDescription().hasMimeType("text/plain"))) {
//            clear();
        } else {
            String string = loadClipData();
            setTextData(string);
        }
    }

    private void setTextData(String string){
        mTitle.setText(new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분").format(new Date()));

        int from = 0;
        int end = string.indexOf('/');
        if(end < 0) return;

        mDate.setText(string.substring(from, end));
        from = end+1;

        end = string.indexOf('/', from);
        if(end < 0) return;

        mStartTime.setText(string.substring(from, end));
        from = end+1;
        if(from >= string.length()) return;

        mEndTime.setText(string.substring(from));
    }

    private String loadClipData(){
        ClipData.Item item = mClipboard.getPrimaryClip().getItemAt(0);
        Log.d("myLog", "data = " + item.getText().toString());
        return item.getText().toString();
    }

    void clear(){
        mTitle.setText("");
        mDate.setText("");
        mStartTime.setText("");
        mEndTime.setText("");

        mTitle.setHint("제목을 입력하세요.");
        mDate.setHint("날짜를 입력하세요.");
        mStartTime.setHint("시작하는 시간을 입력하세요.");
        mEndTime.setHint("끝나는 시간을 입력하세요.");
    }
}
