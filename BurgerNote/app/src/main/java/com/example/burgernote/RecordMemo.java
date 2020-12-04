package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class RecordMemo extends Memo{

    public RecordMemo(Context context){
        initMemoButton(context);
        setButtonClick();
        initMemoDialog(context);
        setAnimation(context, R.anim.scale_up);
    }

    @SuppressLint("InflateParams")
    @Override
    void initMemoDialog(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoDialog = (LinearLayout) layoutInflater.inflate(R.layout.dialog_record, null);     // 리소스 바꾸기
    }

    @Override
    void setButtonClick() {
        super.setButtonClick();
    }

    @Override
    void initMemoButton(Context context) {
        super.initMemoButton(context);
        mMemoButton.setTag("RecordButton");
        mMemoButton.setImageResource(R.mipmap.ic_launcher);      // 리소스 바꾸기
    }
}
