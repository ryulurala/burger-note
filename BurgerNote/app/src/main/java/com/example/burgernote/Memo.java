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

    void initMemoDialog(Context context){
        float dp = context.getResources().getDisplayMetrics().density;      // dp
        int width =(int) (300 * dp);
        int height =(int) (280 * dp);

        // layoutParams 초기화
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);

        mMemoDialog.setLayoutParams(layoutParams);
    }

    void setAnimation(Context context){
        mAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_up);     // 애니메이션 리소스 지정
        mAnimation.setFillAfter(true);          // 끝난 상태로 유지
    }

    void setButtonClick(){
        mMemoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FloatingView floatingView = (FloatingView) mMemoButton.getRootView();
                LinearLayout mMemos = (LinearLayout) floatingView.getChildAt(1);

                floatingView.addView(mMemoDialog, 1);       // dialog 추가
                mMemos.setVisibility(View.GONE);        // list 감추기

                floatingView.mAdded = true;         // dialog 추가 state
                mMemoDialog.startAnimation(mAnimation);         // start animation
            }
        });
    }
}
