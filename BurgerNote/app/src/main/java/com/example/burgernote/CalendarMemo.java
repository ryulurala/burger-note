package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class CalendarMemo extends Memo{

    public CalendarMemo(Context context){
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
}
