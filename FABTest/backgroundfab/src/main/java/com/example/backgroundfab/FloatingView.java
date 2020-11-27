package com.example.backgroundfab;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class FloatingView extends LinearLayout implements View.OnTouchListener {

    private Context mContext;
    private GestureDetector mGestureDetector;
    private Callbacks callbacks;
    private int mTouchSlop;

    // layoutParams 에 접근하므로 int 형
    private int downRawX, downRawY;					// 움직이기 위해 터치한 시작 점
    private int lastX;

    private int lastY;					// 움직이기 이전에 뷰가 마지막으로 위치한 점

    private boolean oneTime = false;

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

    private void init(Context context){
        setOnTouchListener(this);
        mGestureDetector = new GestureDetector(context, new GestureListener());
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();       // ?
        mContext = context;
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            callbacks.onClick();
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

    void setCallbacks(Callbacks callbacks){
        this.callbacks = callbacks;     // register
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int rowX = (int) motionEvent.getRawX();                    //터치 시작 점
        int rowY = (int) motionEvent.getRawY();                    //터치 시작 점

        if(!oneTime) {callbacks.setMaxPosition(); oneTime=true;}

        mGestureDetector.onTouchEvent(motionEvent);

        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:                // 사용자 터치 다운이면(0)
                downRawX = rowX;                           //뷰의 시작 점
                downRawY = rowY;                            //뷰의 시작 점

                lastX = callbacks.getParamsX();           // View 가 있던 이전의 마지막 X
                lastY = callbacks.getParamsY();           // View 가 있던 이전의 마지막 Y
                Log.d("myLog", "ACTION_DOWN, getRawX(): " + rowX);
                Log.d("myLog", "ACTION_DOWN, getRawY(): " + rowY);

                return true;
            case MotionEvent.ACTION_MOVE:                // 사용자 터치 무브면(2)
                int distX = rowX - downRawX;    //이동한 거리
                int distY = rowY - downRawY;    //이동한 거리

                int destX = lastX + distX;
                int destY = lastY + distY;
                Log.d("myLog", "ACTION_MOVE, getRawX(): " + rowX);
                Log.d("myLog", "ACTION_MOVE, getRawY(): " + rowY);
                callbacks.onDrag(destX, destY);

                return true;
        }
        return super.onTouchEvent(motionEvent);
    }

}
