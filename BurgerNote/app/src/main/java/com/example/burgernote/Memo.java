package com.example.burgernote;

import android.content.Context;
import android.view.Gravity;
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
        int dp = (int)context.getResources().getDisplayMetrics().density;
        mMemoButton.setLayoutParams(new ViewGroup.LayoutParams(80*dp, 80*dp));
        mMemoButton.setScaleType(ImageView.ScaleType.FIT_XY);
        mMemoButton.setAdjustViewBounds(true);
        mMemoButton.setBackground(null);
        mMemoButton.setPadding(5*dp, 5*dp, 5*dp, 5*dp);
    }

    void show(){
        mMemoButton.setVisibility(ImageView.VISIBLE);
    }

    void hide(){
        mMemoButton.setVisibility(ImageView.GONE);
    }

    void initMemoDialog(Context context, int width, int height){
        int dp = (int) context.getResources().getDisplayMetrics().density;      // dp

        // layoutParams 초기화
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width*dp, height*dp);

        mMemoDialog.setLayoutParams(layoutParams);
    }

    void setAnimation(Context context, int id){
        mAnimation = AnimationUtils.loadAnimation(context, id);     // 애니메이션 리소스 지정
        mAnimation.setFillAfter(true);          // 끝난 상태로 유지
    }

    void setButtonClick(){
        mMemoButton.setOnClickListener(v -> {
            FloatingView floatingView = (FloatingView) mMemoButton.getRootView();
            LinearLayout mMemos = (LinearLayout) floatingView.getChildAt(1);

            floatingView.addView(mMemoDialog, 1);       // dialog 추가
            mMemos.setVisibility(View.GONE);        // list 감추기

            floatingView.mAdded = true;         // dialog 추가 state
            mMemoDialog.startAnimation(mAnimation);         // start animation
        });
    }
}
