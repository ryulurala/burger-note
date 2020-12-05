package com.example.burgernote;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
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

    @Override
    void initMemoDialog(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoDialog = (LinearLayout) layoutInflater.inflate(R.layout.dialog_drawing, null);

        mDrawingView = new DrawingView(context);
        mDrawingView.setPaintButton(mMemoDialog.findViewById(R.id.draw_paint));
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
            case R.id.draw_erase:
                mDrawingView.erase();
                break;
            case R.id.draw_share:
                mDrawingView.share();
                break;
            case R.id.draw_save:
                mDrawingView.save();
                break;
            case R.id.draw_paint:
                mDrawingView.changePaintColor();
                break;
        }
    }

    public class DrawingView extends View {
        private Canvas mCanvas;
        private Bitmap mBitmap;
        private Paint mPaint;
        private ImageButton mPaintButton;

        private int paintIndex;
        private int mLastX, mLastY;

        public DrawingView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(2.0f);
            mCanvas = new Canvas();
            mCanvas.drawColor(Color.WHITE);
            paintIndex = 0;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(mBitmap != null) canvas.drawBitmap(mBitmap, 0, 0, null);
            else {
                mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                mBitmap.eraseColor(Color.WHITE);
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

        void changePaintColor(){
            paintIndex = (paintIndex + 1) % 4;

            switch (paintIndex){
                case 0:
                    mPaint.setColor(Color.BLACK);
                    mPaintButton.setImageResource(R.color.black);
                    break;
                case 1:
                    mPaint.setColor(Color.RED);
                    mPaintButton.setImageResource(R.color.red);
                    break;
                case 2:
                    mPaint.setColor(Color.GREEN);
                    mPaintButton.setImageResource(R.color.green);
                    break;
                case 3:
                    mPaint.setColor(Color.BLUE);
                    mPaintButton.setImageResource(R.color.blue);
                    break;
            }
        }

        void erase(){
            mBitmap.eraseColor(Color.WHITE);

            invalidate();
        }

        void share(){
            // Bitmap -> URI
            String path = MediaStore.Images.Media.insertImage(
                    mContext.getContentResolver(),
                    mBitmap,
                    "Temp",
                    null
            );
            Uri uri = Uri.parse(path);

            Log.d("myLog", "uri = "+ uri);

            // 이미지 공유
            Intent intent = new Intent(Intent.ACTION_SEND);     // 전송 메소드를 호출
            intent.setType("image/jpg");        // jpg 이미지를 공유 하기 위해 Type 을 정의
            intent.putExtra(Intent.EXTRA_STREAM, uri);      // 사진의 Uri 로드
            Intent chooser = Intent.createChooser(intent, "Share");
            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(chooser);        // Activity 를 이용하여 호출
            }
        }

        void save(){
            try{
                // 지금 시간을 yyyy년 MN월 dd일 HH시 mm분 포맷의 문자열로 저장합니다.
                String date = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분").format(new Date());

                String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".png";
                File file = new File(mContext.getFilesDir(), fileName);

                if (!file.exists()) file.createNewFile();

                FileOutputStream outStream = new FileOutputStream(file);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();

                DrawingMemoDBHelper helper = new DrawingMemoDBHelper(mContext);

                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("insert into tb_drawing_memo (image, date) values (?, ?)", new String[]{fileName, date});
                db.close();

                // 서비스가 20초 동안 잡으면 ANR 에러, 메인 스레드임
                Toast toast = Toast.makeText(mContext, "Image Saved", Toast.LENGTH_SHORT);
                toast.show();

            } catch(Exception e) {
                // 서비스가 20초 동안 잡으면 ANR 에러, 메인 스레드임
                Toast toast = Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }

        void setPaintButton(ImageButton imageButton){
            this.mPaintButton = imageButton;
        }
    }
}
