package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class TextMemo extends Memo{

    public TextMemo(Context context){
        initMemoButton(context);
        setButtonClick();
        initMemoDialog(context);
        setAnimation(context, R.anim.scale_up);
    }

    @SuppressLint("InflateParams")
    @Override
    void initMemoDialog(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoDialog = (LinearLayout) layoutInflater.inflate(R.layout.dialog_text, null);
    }

    @Override
    void setButtonClick() {
        super.setButtonClick();
    }

    @Override
    void initMemoButton(Context context) {
        super.initMemoButton(context);
        mMemoButton.setTag("TextButton");
        mMemoButton.setImageResource(R.drawable.button_text);      // 리소스 바꾸기
    }
}
