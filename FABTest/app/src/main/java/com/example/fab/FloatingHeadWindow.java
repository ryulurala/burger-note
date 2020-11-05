package com.example.fab;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

public class FloatingHeadWindow implements FloatingView.Callbacks{

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private View mView;
    private boolean mViewAdded = false;
    private Rect rect;
    private ObjectAnimator mAnimator;
    private Context context;

    public FloatingHeadWindow(Context context){
        this.context = context;
        Log.d("myLog", "Current Context = " + this.context.toString());
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    void show(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(Settings.canDrawOverlays(context)){
                Log.d("myLog", "current Version >= M " + "before addView");
                Log.d("myLog", "mWindowManager = " + mWindowManager.toString());
                mWindowManager.addView(mView, mLayoutParams);
                Log.d("myLog", "current Version >= M " + "after addView");
            }
        }else{
            Log.d("myLog", "current Version < M " + "before addView");
            mWindowManager.addView(mView, mLayoutParams);
            Log.d("myLog", "current Version < M " + "after addView");
        }
        mViewAdded = true;
    }

    void hide(){
        mWindowManager.removeView(mView);
        mViewAdded = false;
    }

    void create(){
        if(!mViewAdded){
            mView = LayoutInflater.from(context).inflate(R.layout.item_floating, null, false);
            Log.d("myLog", "mView = " + mView.toString());
            Log.d("myLog", "before setCallbacks()");
            ((FloatingView)mView).setCallbacks(this);           // mView 가 FloatingView 로 바뀌지 않음
            Log.d("myLog", "after setCallbacks()");
            rect = new Rect();
            updateScreenLimit(rect);
            mAnimator = ObjectAnimator.ofPropertyValuesHolder(this);
            mAnimator.setInterpolator(new DecelerateInterpolator(1.0f));
        }
    }

    private void updateScreenLimit(Rect rect) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        rect.left = -30;
        rect.right = dm.widthPixels - getWidth();
        rect.top = 30;
        rect.bottom = dm.heightPixels - getHeight();
    }

    int magHorizontal(int _x){
        int x = _x;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if(x + 78 < 0){
            x = rect.left;
        } else if(x + getWidth() - 78 > dm.widthPixels){
            x = rect.right;
        }
        return x;
    }

    int magVertical(int _y){
        int y = _y;
        if(y < rect.top){
            y = rect.top;
        } else if(y>rect.bottom){
            y = rect.bottom;
        }
        return y;
    }

    int getX(){
        return mLayoutParams.x;
    }

    int getY(){
        return mLayoutParams.y;
    }

    void settle(){
        Log.e("settle", "before X"+getX());
        Log.e("settle", "before X"+getY());
        int x = magHorizontal(getX());
        int y = magVertical(getY());
        Log.e("settle", "" + x);
        Log.e("settle", "" + y);
        animateTo(x, y);
    }

    private void animateTo(int x, int y) {
        PropertyValuesHolder xHolder = PropertyValuesHolder.ofInt("x", x);
        PropertyValuesHolder yHolder = PropertyValuesHolder.ofInt("y", y);
        Log.e("animateTo", "x holder" + xHolder);
        Log.e("animateTo", "y holder" + yHolder);
        mAnimator.setValues(xHolder, yHolder);
        mAnimator.setDuration(200);
        mAnimator.start();
        mAnimator.addListener(new Animator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("AnimatorListener", "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("AnimatorListener", "onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("AnimatorListener", "onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("AnimatorListener", "onAnimationRepeat");
            }
        });
    }

    void createLayoutParams(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mLayoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    PixelFormat.TRANSLUCENT
            );
        } else{
            mLayoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    PixelFormat.TRANSLUCENT
            );
        }
        mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mLayoutParams.x = -mView.getWidth();
        mLayoutParams.y = -mView.getHeight();
    }

    void moveTo(Point pos) {
        moveTo(pos.x, pos.y);
    }
    void moveTo(int x, int y){
        if(mView != null && mViewAdded){
            Log.e("test int", "x: "+x+" y: "+y);
            mLayoutParams.x = x;
            mLayoutParams.y = y;
            mWindowManager.updateViewLayout(mView, mLayoutParams);
        }
    }

    void moveBy(int dx, int dy){
        if(mView != null && mViewAdded){
            Log.e("test int", "dx: "+dx+" dy: "+dy);
            mLayoutParams.x += dx;
            mLayoutParams.y += dy;
            mWindowManager.updateViewLayout(mView, mLayoutParams);
        }
    }

    int getWidth(){
        return mView.getMeasuredWidth();
    }

    int getHeight() {
        return mView.getMeasuredHeight();
    }


    @Override
    public void onDrag(int dx, int dy) {
        Log.e("test Float", "dx: "+dx+" dy: "+dy);
        moveBy(dx, dy);
    }

    @Override
    public void onDragEnd(int dx, int dy) {
        settle();
    }

    @Override
    public void onDragStart(int dx, int dy) {

    }

    @Override
    public void onClick() {
        startActivity();
    }

    private void startActivity() {
        try{
            // 알림 팝업용 펜딩 인덴트
            Log.d("myLog", "Before pendingIndent send()");
            PendingIntent contentIndent = PendingIntent.getActivity(
              context,
              9000,
              new Intent(context, FloatingActivity.class)
                      .addFlags(
                              Intent.FLAG_ACTIVITY_NEW_TASK
                              | Intent.FLAG_ACTIVITY_SINGLE_TOP
                              | Intent.FLAG_ACTIVITY_NO_USER_ACTION
                              | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                      ),
                    PendingIntent.FLAG_ONE_SHOT
            );
            contentIndent.send();
            Log.d("myLog", "After pendingIndent send()");
        } catch(Exception e){
            Log.d("myLog", "startActivity error");
            e.printStackTrace();
        }
    }
}
