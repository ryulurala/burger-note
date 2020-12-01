package com.example.backgroundfab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class FloatingView extends LinearLayout implements View.OnTouchListener {

    private GestureDetector mGestureDetector;
    private Callbacks mCallbacks;       // = FloatingViewManager

    // layoutParams 에 접근하므로 int 형
    private int downRawX, downRawY;					// 움직이기 위해 터치한 시작 점
    private int lastX, lastY;       // 움직이기 이전에 뷰가 마지막으로 위치한 점
    private boolean oneTime = false;        // for. setMaxPosition()

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
    }

    void setCallbacks(Callbacks callbacks){
        this.mCallbacks = callbacks;     // register
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int rowX = (int) motionEvent.getRawX();                    //터치 시작 점
        int rowY = (int) motionEvent.getRawY();                    //터치 시작 점

        mGestureDetector.onTouchEvent(motionEvent);

        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:                // 사용자 터치 다운이면(0)
                if(!oneTime) {mCallbacks.setMaxPosition(); oneTime=true;}    // limit move View position

                downRawX = rowX;                           //뷰의 시작 점
                downRawY = rowY;                            //뷰의 시작 점

                lastX = mCallbacks.getParamsX();           // View 가 있던 이전의 마지막 X
                lastY = mCallbacks.getParamsY();           // View 가 있던 이전의 마지막 Y

                return true;
            case MotionEvent.ACTION_MOVE:                // 사용자 터치 무브면(2)
                int distX = rowX - downRawX;    //이동한 거리
                int distY = rowY - downRawY;    //이동한 거리

                int destX = lastX + distX;
                int destY = lastY + distY;

                mCallbacks.onDrag(destX, destY);

                return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mCallbacks.onClick();
            return true;
        }
    }

    interface Callbacks{
        int getParamsX();
        int getParamsY();
        void onDrag(int dx, int dy);        // ACTION_MOVE
        void onClick();     // click
        void setMaxPosition();      // limit Position
    }
}