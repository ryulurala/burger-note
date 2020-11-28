package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

public class FloatingViewManager implements FloatingView.Callbacks{
    private final Context mContext;
    private final WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private View mView;
    private boolean mViewAdded;

    private final int DISPLAY_WIDTH, DISPLAY_HEIGHT;
    private int mMaxX, mMaxY;

    FloatingViewManager(Context context){
        // constructor
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mContext = context;     // FloatingService 의 context
        DisplayMetrics matrix = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(matrix);
        DISPLAY_WIDTH = matrix.widthPixels;
        DISPLAY_HEIGHT = matrix.heightPixels;
    }

    void addView(){
        // window 에 view 추가, permission 필요
        mWindowManager.addView(mView, mLayoutParams);
        mViewAdded = true;
    }

    void removeView(){
        mWindowManager.removeView(mView);
        mViewAdded = false;
    }

    @SuppressLint("InflateParams")
    void create(){
        if(!mViewAdded){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 레이아웃을 객체로 만듬(Inflate)
            mView = layoutInflater.inflate(R.layout.rootview, null);
            ((FloatingView) mView).setCallbacks(this);
        }
    }

    void initLayoutParams() {
        // 레이아웃 속성들 초기화
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,    // 모든 활동 창 위에 표시, permission.system_alert_window 필요
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE   // 콘텐츠에 대한 전체 화면 사용 가능, 윈도우가 포커스 X(다른 앱도 터치이벤트 먹히도록)
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS  // 창을 화면 밖으로 확장 가능
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN  // 상태바 영역도 윈도우에 포함
                            | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,   // LAYOUT_IN_SCREEN 과 같이 쓰임, 다른 윈도우의 네비바와 상태바가 겹치지 않게 함
                    PixelFormat.TRANSLUCENT     // 배경을 투명하게
            );
        } else {
            mLayoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,   // 경고 창 같이 항상 다른 프로그램 위에 존재하도록
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    PixelFormat.TRANSLUCENT
            );
        }
        // dp
        mLayoutParams.gravity = Gravity.TOP | Gravity.START;        // 필수! 기준을 만듬 TOP | LEFT

        // size 변경
        mLayoutParams.width = 200;
        mLayoutParams.height = 200;
    }

    void optimizePosition(){
        //최대값 넘어가지 않게 설정
//        Log.d("myLog", "mLayoutParams(x, y): (" + mLayoutParams.x + ", " + mLayoutParams.y +")");
        if(mLayoutParams.x > mMaxX) mLayoutParams.x = mMaxX;
        if(mLayoutParams.y > mMaxY) mLayoutParams.y = mMaxY;
        if(mLayoutParams.x < 0) mLayoutParams.x = 0;
        if(mLayoutParams.y < 0) mLayoutParams.y = 0;
    }

    @Override
    public void setMaxPosition(){
        mMaxX = DISPLAY_WIDTH - mView.getWidth();
        mMaxY = DISPLAY_HEIGHT - mView.getHeight();
    }

    @Override
    public void onDrag(int dx, int dy) {
        mLayoutParams.x = dx;
        mLayoutParams.y = dy;

        optimizePosition();         // View 가 화면 밖으로 넘어가면 최적화

        mWindowManager.updateViewLayout(mView, mLayoutParams);
    }

    @Override
    public void onClick() {
        // click 하면 일어날 일
        Log.d("myLog", "onClick()");
    }

    @Override
    public int getParamsX(){
        return mLayoutParams.x;
    }

    @Override
    public int getParamsY(){
        return mLayoutParams.y;
    }
}
