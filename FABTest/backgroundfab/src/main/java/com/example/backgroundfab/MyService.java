package com.example.backgroundfab;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyService extends Service {

    WindowManager wm;
    View mView;
    WindowManager.LayoutParams mParams;

    private float START_X, START_Y;							//움직이기 위해 터치한 시작 점
    private int PREV_X, PREV_Y;								//움직이기 이전에 뷰가 위치한 점
    private int MAX_X = -1, MAX_Y = -1;					//뷰의 위치 최대 값

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        initWindowParams();

        mView = inflate.inflate(R.layout.view_in_service, null);

        mView.setOnTouchListener(new mViewTouchListener());

        final ImageButton bt =  (ImageButton) mView.findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.setImageResource(R.mipmap.ic_launcher_round);
            }
        });

        wm.addView(mView, mParams);
    }

    private void initWindowParams() {
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
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
    }

   class mViewTouchListener implements View.OnTouchListener {
       @Override
       public boolean onTouch(View view, MotionEvent motionEvent) {
           switch(motionEvent.getAction()) {
               case MotionEvent.ACTION_DOWN:                //사용자 터치 다운이면
                   if(MAX_X == -1)
                       setMaxPosition();
                   START_X = motionEvent.getRawX();                    //터치 시작 점
                   START_Y = motionEvent.getRawY();                    //터치 시작 점
                   PREV_X = mParams.x;                            //뷰의 시작 점
                   PREV_Y = mParams.y;                            //뷰의 시작 점
                   break;
               case MotionEvent.ACTION_MOVE:
                   int x = (int)(motionEvent.getRawX() - START_X);    //이동한 거리
                   int y = (int)(motionEvent.getRawY() - START_Y);    //이동한 거리

                   //터치해서 이동한 만큼 이동 시킨다
                   mParams.x = PREV_X + x;
                   mParams.y = PREV_Y + y;

                   optimizePosition();        //뷰의 위치 최적화
                   wm.updateViewLayout(mView, mParams);    //뷰 업데이트
                   break;
           }
           return true;
       }
   };

    private void setMaxPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(matrix);		//화면 정보를 가져와서

        MAX_X = matrix.widthPixels - mView.getWidth();			//x 최대값 설정
        MAX_Y = matrix.heightPixels - mView.getHeight();			//y 최대값 설정
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
            if(mView != null) {
                wm.removeView(mView);
                mView = null;
            }
            wm = null;
        }
    }
}