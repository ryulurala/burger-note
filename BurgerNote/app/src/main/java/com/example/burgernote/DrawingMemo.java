package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class DrawingMemo extends Memo{

    public DrawingMemo(Context context){
        initMemoButton(context);
        setButtonClick();
        initMemoDialog(context);
        setAnimation(context);
    }

    @SuppressLint("InflateParams")
    @Override
    void initMemoDialog(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoDialog = (LinearLayout) layoutInflater.inflate(R.layout.dialog_drawing, null);
        super.initMemoDialog(context);
    }

    @Override
    void initMemoButton(Context context) {
        super.initMemoButton(context);
        mMemoButton.setImageResource(R.mipmap.ic_launcher);      // 리소스 바꾸기
    }

    public class DrawingView extends View {
        private Canvas mCanvas;
        private Paint mPaint;

        private int mLastX, mLastY;

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int downX = (int)event.getX();
            int downY = (int)event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mLastX = downX;
                    mLastY = downY;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    invalidate();           // 화면 갱신 --- onDraw() 호출
                    mLastX = downX;
                    mLastY = downY;
                    return true;
            }

            return super.onTouchEvent(event);
        }

        public DrawingView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(2.0f);
        }


    }
}
