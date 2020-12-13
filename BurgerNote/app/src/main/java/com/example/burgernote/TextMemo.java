package com.example.burgernote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.Serializable;

public class TextMemo extends Memo implements View.OnClickListener{

    private Context mContext;

    private ClipboardManager mClipboard;

    public static EditText mEditText;

    public TextMemo(Context context){
        mContext = context;
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

        mClipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        mMemoDialog.findViewById(R.id.text_write).setOnClickListener(this);
        mMemoDialog.findViewById(R.id.text_load).setOnClickListener(this);
//        mMemoDialog.findViewById(R.id.text_save).setOnClickListener(this);

        mEditText = mMemoDialog.findViewById(R.id.text_edit);

        super.initMemoDialog(context, width, height);
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
            case R.id.text_write:
                write();
                break;
            case R.id.text_load:
                load();
                break;
//            case R.id.text_save:
//                save();
//                break;
        }
    }

    void write() {
        Log.d("myLog", "TextMemo write()");
        Intent intent = new Intent(mContext, TextMemoWriteActivity.class);
        intent.putExtra("TextMemo", true);
        mContext.startActivity(intent);
    }

    void load(){
        Log.d("myLog", "TextMemo load()");
        if (!(mClipboard.hasPrimaryClip()) || !(mClipboard.getPrimaryClipDescription().hasMimeType("text/plain"))) {
            mEditText.setText("");
        } else {
            ClipData.Item item = mClipboard.getPrimaryClip().getItemAt(0);
            Log.d("myLog", "data = " + item.getText().toString());
            mEditText.setText(item.getText().toString());
        }
    }
}
