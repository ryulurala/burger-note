package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class TextMemo extends Memo implements View.OnClickListener{

    public TextMemo(Context context){
        initMemoButton(context);
        setButtonClick();
        initMemoDialog(context, 200, 150);
        setAnimation(context, R.anim.scale_up);
    }

    @SuppressLint("InflateParams")
    @Override
    void initMemoDialog(Context context, int width, int height) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoDialog = (LinearLayout) layoutInflater.inflate(R.layout.dialog_text, null);

        mMemoDialog.findViewById(R.id.text_copy).setOnClickListener(this);
        mMemoDialog.findViewById(R.id.text_save).setOnClickListener(this);

        super.initMemoDialog(context, width, height);        // 너무 커짐
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_copy:
                copy();
                break;
            case R.id.text_save:
                save();
                break;
        }
    }

    void save() {

    }

    void copy() {

    }

}
