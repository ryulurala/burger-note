package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DrawingMemo extends Memo implements View.OnClickListener{

    private DrawingView mDrawingView;
    private Context mContext;

    public DrawingMemo(Context context){
        initMemoButton(context);
        initMemoDialog(context);
        setButtonClick();
        setAnimation(context, R.anim.scale_up);
        mContext = context;
    }

    @Override
    void initMemoButton(Context context) {
        super.initMemoButton(context);
        mMemoButton.setImageResource(R.mipmap.ic_launcher);      // 리소스 바꾸기
    }

    @SuppressLint("InflateParams")
    @Override
    void initMemoDialog(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoDialog = (LinearLayout) layoutInflater.inflate(R.layout.dialog_drawing, null);

        mDrawingView = new DrawingView(context);
        ((LinearLayout)mMemoDialog.getChildAt(0)).addView(mDrawingView);

        super.initMemoDialog(context);
    }

    @Override
    void setButtonClick(){
        super.setButtonClick();         // init click mMemoButton

        LinearLayout buttonList = (LinearLayout) mMemoDialog.getChildAt(1);
        for(int i=0; i<buttonList.getChildCount(); i++){
            buttonList.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.draw_copy:
                mDrawingView.copy();
                break;
            case R.id.draw_erase:
                mDrawingView.erase();
                break;
            case R.id.draw_save:
                mDrawingView.save();
                break;
        }
    }

    public class DrawingView extends View {
        private Canvas mCanvas;
        private Bitmap mBitmap;
        private Paint mPaint;

        private int mLastX, mLastY;

        public DrawingView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(2.0f);
            mCanvas = new Canvas();
            mCanvas.drawColor(Color.WHITE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(mBitmap != null) canvas.drawBitmap(mBitmap, 0, 0, null);
            else {
                mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                mCanvas.setBitmap(mBitmap);     // 연결
                Log.d("myLog", "onDraw() = " + getWidth() + ", "+getHeight());
            }
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
                    mCanvas.drawLine(mLastX, mLastY, downX, downY, mPaint);     // 선 그리기

                    invalidate();           // 화면 갱신 --- onDraw() 호출

                    mLastX = downX;
                    mLastY = downY;
                    return true;
            }
            return super.onTouchEvent(event);
        }

        void erase(){
            mBitmap.eraseColor(Color.WHITE);

            invalidate();
        }

        void copy(){

        }

        void save(){
            // 지금 시간을 yyyy년 MN월 dd일 HH시 mm분 포맷의 문자열로 저장합니다.
            String date = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분").format(new Date());

            // saveImage() 함수를 이용해 이미지를 저장하고 그 파일명을 return 받습니다.
            String image = saveImage();

            if(!image.equals("Exception")){
                DrawingMemoDBHelper helper = new DrawingMemoDBHelper(mContext);

                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("insert into tb_drawing_memo (image, date) values (?, ?)", new String[]{image, date});
                db.close();

                // 서비스가 20초 동안 잡으면 ANR 에러, 메인 스레드임
                Toast toast = Toast.makeText(mContext, "입력이 완료되었습니다,", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        private String saveImage(){
            FileOutputStream outStream;

            try {
                String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".png";
                File file = new File(mContext.getFilesDir(), fileName);

                Log.d("myLog", "file dir = "+mContext.getFilesDir());

                Log.d("myLog", "file (A) path = "+file.getAbsolutePath());
                Log.d("myLog", "file (P) path = "+file.getPath());
                Log.d("myLog", "file (C) path = "+file.getCanonicalPath());

                if (!file.exists()) {
                    file.createNewFile();
                }

                outStream = new FileOutputStream(file);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();

                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception";
            }
        }
    }
}
