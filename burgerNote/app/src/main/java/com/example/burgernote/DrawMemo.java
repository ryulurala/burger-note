package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class DrawMemo extends Memo{
    public DrawMemo(Context context){
        initMemoButton(context);
        setButtonClick();
        initMemoDialog(context);
        setAnimation(context);
    }

    @SuppressLint("InflateParams")
    @Override
    void initMemoDialog(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoDialog = (LinearLayout) layoutInflater.inflate(R.layout.dialog_drawing, null);     // 리소스 바꾸기
    }

    @Override
    void initMemoButton(Context context) {
        super.initMemoButton(context);
        mMemoButton.setTag("DrawButton");
        mMemoButton.setImageResource(R.mipmap.ic_launcher);      // 리소스 바꾸기
    }

}
