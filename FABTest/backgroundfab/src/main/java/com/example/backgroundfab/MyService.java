package com.example.backgroundfab;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
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

import androidx.annotation.NonNull;

public class MyService extends Service {

    WindowManager wm;           // 윈도우 매니저(화면을 구성하는 최대 단위)
    View fstView;             // 오버레이할 View
    WindowManager.LayoutParams mParams;             // Layout 미리 속성들 지정

    private float START_X, START_Y;					// 움직이기 위해 터치한 시작 점
    private int PREV_X, PREV_Y;						// 움직이기 이전에 뷰가 위치한 점
    private int MAX_X = -1, MAX_Y = -1;				// 뷰의 위치 최대 값, 처음 -1로 초기화

//    private final int duration = 100;          // 터치 지속 시간
    long initTime;                          // 터치시간

    IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {     // Binder 상속(Binder 는 IBinder interface 를 구현)
        MyService getService(){
            return MyService.this;      // 현재 Service 객체를 리턴
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서) 데이터를 주고받을 때 사용하는 메소드
        // Activity 에서 bindService()를 실행하면 호출됨.
        // data 를 전달할 필요 없으면 null 값 리턴.
        Log.d("myLog", "Service: onBind()");
        return mBinder;     // 리턴할 mBinder 객체는 서비스와 클라이언트 사이의 인터페이스 정의
    }


    class mTouchListener implements View.OnTouchListener{
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.d("myLog", "mParams.width: "+ mParams.width);
            Log.d("myLog", "mParams.height: "+ mParams.height);

            Log.d("myLog", "mParams.x: " + mParams.x);
            Log.d("myLog", "mParams.y: " + mParams.y);

            Log.d("myLog", "fstView.width: "+fstView.getWidth());
            Log.d("myLog", "fstView.height: "+fstView.getHeight());
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:                // 사용자 터치 다운이면(0)
                    initTime = System.currentTimeMillis();          // 처음 터치 시간

                    if(MAX_X == -1) setMaxPosition();

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
                    wm.updateViewLayout(fstView, mParams);    //뷰 업데이트, 이 때 View 위치 변함
                    break;
                case MotionEvent.ACTION_UP:
                    // ----여기다가 시간에 따라서 onClick 처럼 보이게 작용--- 코드 작성
                    Log.d("myLog", "initTime = " + initTime);
                    if(System.currentTimeMillis() - initTime < 100){
//                        // onClick
//                        initDialogParams();          // layoutParams 속성 바꾸기(gravity 중점적으로)
//                        View bt2 = scdView.findViewById(R.id.scdbt);            // 자식 뷰 애니메이션
//
//                        bt2.startAnimation(anim);

                        fstView = null;
                    }
                    break;
            }
            return true;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("myLog", "Service onCreate()");
        // 레이아웃 인플레이터를 가져옴
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);       // 윈도우 서비스의 Manager 가져옴

        initWindowParams();

        // 레이아웃을 객체로 만듬(Inflate)
        fstView = layoutInflater.inflate(R.layout.first, null, false);
        ImageButton bt = fstView.findViewById(R.id.fstbt);

        bt.setOnTouchListener(new mTouchListener());
//        wm.addView(fstView, mParams);
    }

    public void initWindowParams() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,   // 경고 창 같이 항상 다른 프로그램 위에 존재하도록
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE   // 콘텐츠에 대한 전체 화면 사용 가능, 윈도우가 포커스 X(다른 앱도 터치이벤트 먹히도록)
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS // 창을 화면 밖으로 확장 가능
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN    // 상태바 영역도 윈도우에 포함
                            | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR, // LAYOUT_IN_SCREEN 과 같이 쓰임, 다른 윈도우의 네비바와 상태바가 겹치지 않게 함
                    PixelFormat.TRANSLUCENT // 배경을 투명하게
            );
        } else {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,    // 모든 활동 창 위에 표시, permission.system_alert_window 필요
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    PixelFormat.TRANSLUCENT
            );
        }
        // dp

//        mParams.width = -fstView.getWidth();
//        mParams.height = -fstView.getHeight();
        mParams.gravity = Gravity.TOP | Gravity.START;      // 필수! 기준을 만듬
    }

    private void setMaxPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(matrix);		//화면 정보를 가져와서

//        MAX_X = matrix.widthPixels - fstView.getWidth();			//x 최대값 설정
//        MAX_Y = matrix.heightPixels - fstView.getHeight();			//y 최대값 설정

        MAX_X = matrix.widthPixels - fstView.getWidth();			//x 최대값 설정
        MAX_Y = matrix.heightPixels - fstView.getHeight();			//y 최대값 설정

        Log.d("myLog", "max X: " + MAX_X);
        Log.d("myLog", "max Y: " + MAX_Y);

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
            Log.d("myLog", "myService onDestroy()");
            wm = null;
        }
    }

    public void showView(){
        if(fstView != null){
            wm.addView(fstView, mParams);
        }
    }

    public void hideView(){
        if(fstView != null){
            wm.removeView(fstView);
        }
    }
}