package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordMemo extends Memo implements View.OnClickListener{

    private Context mContext;

    public RecordMemo(Context context){
        initMemoButton(context);
        setButtonClick();
        initMemoDialog(context);
        setAnimation(context, R.anim.scale_up);
        mContext = context;
    }

    @SuppressLint("InflateParams")
    @Override
    void initMemoDialog(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoDialog = (LinearLayout) layoutInflater.inflate(R.layout.dialog_record, null);

        super.initMemoDialog(context);
    }

    @Override
    void setButtonClick() {
        super.setButtonClick();
    }

    @Override
    void initMemoButton(Context context) {
        super.initMemoButton(context);
        mMemoButton.setTag("RecordButton");
        mMemoButton.setImageResource(R.drawable.button_record);      // 리소스 바꾸기
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_start:

                break;
            case R.id.record_stop:

                break;
            case R.id.record_share:
                share();
                break;
            case R.id.record_save:
                save();
                break;
        }
    }

    void share(){
        Log.d("myLog", "RecordMemo: share()");
        // Audio -> URI
        ContentValues values = new ContentValues(5);
        values.put(MediaStore.MediaColumns.TITLE, "temp");
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "Recorded Audio");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/x-m4a");
        values.put(MediaStore.Audio.Media.DATA, "temp");
        Uri uri = mContext.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);

        Log.d("myLog", "uri = "+ uri);

        // 오디오 공유
        Intent intent = new Intent(Intent.ACTION_SEND);     // 전송 메소드를 호출
        intent.setType("audio/x-m4a");        // m4a 오디오를 공유 하기 위해 Type 을 정의
        intent.putExtra(Intent.EXTRA_STREAM, uri);      // 오디오의 Uri 로드
        Intent chooser = Intent.createChooser(intent, "Share");
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(chooser);        // Activity 를 이용하여 호출
        }
    }

    void save(){
        Log.d("myLog", "RecordMemo: save()");
//        try{
//            // 지금 시간을 yyyy년 MN월 dd일 HH시 mm분 포맷의 문자열로 저장합니다.
//            String date = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분").format(new Date());
//
//            String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".m4a";
//            File file = new File(mContext.getFilesDir(), fileName);
//
//            if (!file.exists()) file.createNewFile();
//
//            FileOutputStream outStream = new FileOutputStream(file);
//            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//
//            DrawingMemoDBHelper helper = new DrawingMemoDBHelper(mContext);
//
//            SQLiteDatabase db = helper.getWritableDatabase();
//            db.execSQL("insert into tb_drawing_memo (image, date) values (?, ?)", new String[]{fileName, date});
//            db.close();
//
//            // 서비스가 20초 동안 잡으면 ANR 에러, 메인 스레드임
//            Toast toast = Toast.makeText(mContext, "Image Saved", Toast.LENGTH_SHORT);
//            toast.show();
//
//        } catch(Exception e) {
//            // 서비스가 20초 동안 잡으면 ANR 에러, 메인 스레드임
//            Toast toast = Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT);
//            toast.show();
//            return;
//        }
    }
}
