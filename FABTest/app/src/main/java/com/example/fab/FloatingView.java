package com.example.fab;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class FloatingView extends LinearLayout implements View.OnTouchListener {

    private final float CLICK_DRAG_TOLERANCE = 10f;

    private int downRawX = 0;
    private int downRawY = 0;
    private int lastX = 0;
    private int lastY = 0;
    private GestureDetector mGestureDetector;
    private int mTouchSlop;
    private Callbacks callbacks;

    public FloatingView(Context context){
        this(context, null);
        Log.d("myLog", "FloatingView constructor");
    }

    public FloatingView(Context context, @Nullable AttributeSet attrs){
        this(context, attrs, 0);
    }

    public FloatingView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        Log.d("myLog", "FloatingView constructor");
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        setOnTouchListener(this);
        mGestureDetector = new GestureDetector(getContext(), new GestureListener());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    void setCallbacks(Callbacks callbacks){
        this.callbacks = callbacks;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();

        mGestureDetector.onTouchEvent(event);

        int action = event.getAction();
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();

        if(action == event.ACTION_DOWN){
            downRawX = x;
            downRawY = y;
            lastX = x;
            lastY = y;

            return true;        // Consumed
        } else if(action == event.ACTION_MOVE){
            int nx = (x - lastX);
            int ny = (y - lastY);
            lastX = x;
            lastY = y;

            callbacks.onDrag(nx, ny);
            return true;    // Consumed
        } else if(action == event.ACTION_UP){
            Log.d("myLog", "FloatingView ACTION_UP");
            callbacks.onDragEnd(x, y);
            return true;
        } else {
            return super.onTouchEvent(event);
        }
//        return super.onTouchEvent(event);
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d("myLog", "FloatingView onclick");
            callbacks.onClick();
            return true;
        }
    }

    interface Callbacks{
        void onDrag(int dx, int dy);
        void onDragEnd(int dx, int dy);
        void onDragStart(int dx, int dy);
        void onClick();
    }
}
