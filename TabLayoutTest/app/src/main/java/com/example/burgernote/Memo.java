package com.example.burgernote;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public abstract class Memo {

    protected String mTag;
    protected ImageButton mMemoButton;
    protected LinearLayout mMemoDialog;
    protected Animation mAnimation;

    void initMemoButton(Context context){
        mMemoButton = new ImageButton(context);
        mMemoButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        mMemoButton.setScaleType(ImageView.ScaleType.FIT_XY);
        mMemoButton.setAdjustViewBounds(true);
        mMemoButton.setBackground(null);
        mMemoButton.setPadding(0, 0, 0, 0);
    }

    void show(){
        mMemoButton.setVisibility(ImageView.VISIBLE);
    }

    void hide(){
        mMemoButton.setVisibility(ImageView.GONE);
    }

    abstract void initMemoDialog(Context context);

    void setAnimation(Context context){
//        mAnimation = AnimationUtils.loadAnimation(context, R.anim.)      // 애니메이션 리소스 지정
//        mAnimation.setFillAfter(true);          // 끝난 상태로 유지
    }

    void setButtonClick(){
        mMemoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("myLog", "onClick() = " + v.getTag());
                // 각 tag 에 맞는 Dialog 띄워주기

//                mMemoDialog.startAnimation(mAnimation);
            }
        });
    }
}
