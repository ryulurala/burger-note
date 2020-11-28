package com.example.burgernote;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class FloatingView extends LinearLayout implements View.OnTouchListener {

    private GestureDetector mGestureDetector;
    private Callbacks mCallbacks;

    // layoutParams 에 접근하므로 int 형
    private int mDownRawX, mDownRawY;					// 움직이기 위해 터치한 시작 점
    private int mLastX, mLastY;       // 움직이기 이전에 뷰가 마지막으로 위치한 점
    private boolean mOneTime;        // for. setMaxPosition()

    public FloatingView(Context context) {
        this(context, null);
    }

    public FloatingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context){
        setOnTouchListener(this);
        mGestureDetector = new GestureDetector(context, new GestureListener());     // for. onclick()
        mOneTime = false;
    }

    void setCallbacks(Callbacks callbacks){
        this.mCallbacks = callbacks;     // register
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int rowX = (int) event.getRawX();                    //터치 시작 점
        int rowY = (int) event.getRawY();                    //터치 시작 점

        mGestureDetector.onTouchEvent(event);     // for. onClick()

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:                // 사용자 터치 다운이면(0)
                if(!mOneTime) {mCallbacks.setMaxPosition(); mOneTime=true; }    // 스크린 밖 이동 제한

                mDownRawX = rowX;                           //뷰의 시작 점
                mDownRawY = rowY;                            //뷰의 시작 점

                mLastX = mCallbacks.getParamsX();           // View 가 있던 이전의 마지막 X
                mLastY = mCallbacks.getParamsY();           // View 가 있던 이전의 마지막 Y

                return true;        // Motion 이벤트 소모, 다른 터치 이벤트 소모 X (focus)
            case MotionEvent.ACTION_MOVE:                // 사용자 터치 무브면(2)
                int distX = rowX - mDownRawX;    //이동한 거리
                int distY = rowY - mDownRawY;    //이동한 거리

                int destX = mLastX + distX;     // 목적지
                int destY = mLastY + distY;     // 목적지

                mCallbacks.onDrag(destX, destY);

                return true;        // Motion 이벤트 소모, 다른 터치 이벤트 소모 X (focus)
        }
        return super.onTouchEvent(event);
    }

    interface Callbacks{
        int getParamsX();
        int getParamsY();
        void onDrag(int dx, int dy);        // ACTION_MOVE
        void onClick();     // click
        void setMaxPosition();      // limit Position
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mCallbacks.onClick();
            return true;
        }
    }

}
