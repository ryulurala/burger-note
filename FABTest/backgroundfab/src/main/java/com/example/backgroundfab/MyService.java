package com.example.backgroundfab;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyService extends Service {

    WindowManager wm;           // 윈도우 매니저(가장 바깥 쪽 View?)
    View fstView;             // 오버레이할 View
    View scdView;
    Animation anim;
    WindowManager.LayoutParams mParams;             // Layout 미리 속성들 지정

    private float START_X, START_Y;					// 움직이기 위해 터치한 시작 점
    private int PREV_X, PREV_Y;						// 움직이기 이전에 뷰가 위치한 점
    private int MAX_X = -1, MAX_Y = -1;				// 뷰의 위치 최대 값

    private final int duration = 100;          // 터치 지속 시간
    private final int buttonScale = 80;         // dp
    private final int dialogScale = 200;        // dp
    long initTime;                          // 터치시간

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();

        // 레이아웃 인플레이터를 가져옴
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);       // 윈도우 서비스의 Manager 가져옴

        initWindowParams();     // 레이아웃 속성들 초기화

        // 레이아웃을 객체로 만듬(Inflate)
        fstView = layoutInflater.inflate(R.layout.first, null);
        scdView = layoutInflater.inflate(R.layout.second, null);     // 나중에는 두 번째 파라미터로 view group 에 붙이기

        // 애니메이션 불가 - 시스템 권한때문에, windowManager 를 move 하는 것처럼 update 시켜줘야 함.(timeout, tick)
//        anim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
//        anim.setFillAfter(true);

        final ImageButton bt = (ImageButton) fstView.findViewById(R.id.fstbt);
        bt.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:                // 사용자 터치 다운이면(0)
                        initTime = System.currentTimeMillis();          // 처음 터치 시간
                        if(MAX_X == -1)
                            setMaxPosition();
                        START_X = motionEvent.getRawX();                    //터치 시작 점
                        START_Y = motionEvent.getRawY();                    //터치 시작 점
                        PREV_X = mParams.x;                            //뷰의 시작 점
                        PREV_Y = mParams.y;                            //뷰의 시작 점
                        break;
                    case MotionEvent.ACTION_MOVE:                // 사용자 터치 무브면(2)
                        int x = (int)(motionEvent.getRawX() - START_X);    //이동한 거리
                        int y = (int)(motionEvent.getRawY() - START_Y);    //이동한 거리

                        //터치해서 이동한 만큼 이동 시킨다
                        mParams.x = PREV_X + x;
                        mParams.y = PREV_Y + y;

                        optimizePosition();        //뷰의 위치 최적화
                        wm.updateViewLayout(fstView, mParams);    //뷰 업데이트
                        break;
                    // ----여기다가 시간에 따라서 onClick 처럼 보이게 작용--- 코드 작성
                    case MotionEvent.ACTION_UP:
                        Log.d("myLog", "initTime = " + initTime);
                        if(System.currentTimeMillis() - initTime < duration){
                            // onClick
                            initDialogParams();          // layoutParams 속성 바꾸기(gravity 중점적으로)
                            wm.addView(scdView, mParams);

//                       scdView.startAnimation(anim);        // 시스템 권한은 애니메이션 불가

                            wm.removeView(fstView);
                            fstView = null;
                        }
                        break;
                }
                return true;            // Motion 이벤트 소모
            }
        });            // for. 움직임
        wm.addView(fstView, mParams);
    }

    private void initWindowParams() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,        // 변경해야함 - 부모의 가중치로 크기를 지정
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    PixelFormat.TRANSLUCENT);
        } else {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    PixelFormat.TRANSLUCENT);
        }
        mParams.width = buttonScale * (int)getResources().getDisplayMetrics().density;       // 80dp(나중에 수정 가능하도록, static??)
        mParams.height = buttonScale * (int)getResources().getDisplayMetrics().density;      // 80dp
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
    }

    private void initDialogParams() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    PixelFormat.TRANSLUCENT);
        } else {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    PixelFormat.TRANSLUCENT);
        }
        mParams.width = dialogScale * (int)getResources().getDisplayMetrics().density;       // 80dp(나중에 수정 가능하도록, static??)
        mParams.height = dialogScale * (int)getResources().getDisplayMetrics().density;      // 80dp
    }

    private void setMaxPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(matrix);		//화면 정보를 가져와서

        MAX_X = matrix.widthPixels - fstView.getWidth();			//x 최대값 설정
        MAX_Y = matrix.heightPixels - fstView.getHeight();			//y 최대값 설정
    }

    private void optimizePosition() {
        //최대값 넘어가지 않게 설정
        if(mParams.x > MAX_X) mParams.x = MAX_X;
        if(mParams.y > MAX_Y) mParams.y = MAX_Y;
        if(mParams.x < 0) mParams.x = 0;
        if(mParams.y < 0) mParams.y = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(wm != null) {
            if(fstView != null) {
                wm.removeView(fstView);
                fstView = null;
            }
            else if(scdView != null){
                wm.removeView(scdView);
                scdView = null;
            }
            Log.d("myLog", "myService onDestroy()");
            wm = null;
        }
    }
}