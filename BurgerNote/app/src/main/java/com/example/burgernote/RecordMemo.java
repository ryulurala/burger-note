package com.example.burgernote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaRecorder;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordMemo extends Memo implements View.OnClickListener{

    private Context mContext;

    private MediaRecorder mRecorder = null;

    private Chronometer mChronometer;
    private ImageButton mRecordStart;
    private ImageButton mRecordStop;

    private String mTitle;      // 녹음 파일 제목
    private String mDate;       // 녹음 파일 시작 시간
    private String mFileName;   // 파일 이름
    private long mLength;     // 녹음 길이

    private boolean mPlayed = false;
    

    public RecordMemo(Context context){
        initMemoButton(context);
        setButtonClick();
        initMemoDialog(context, 250, 300);
        setAnimation(context, R.anim.scale_up);
        mContext = context;
    }

    @SuppressLint("InflateParams")
    @Override
    void initMemoDialog(Context context, int width, int height) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemoDialog = (LinearLayout) layoutInflater.inflate(R.layout.dialog_record, null);

        mChronometer = mMemoDialog.findViewById(R.id.record_chronometer);       // 타이머
        mRecordStart = mMemoDialog.findViewById(R.id.record_start);             // 시작 버튼
        mRecordStop = mMemoDialog.findViewById(R.id.record_stop);              // 중지 버튼

        // 콜백 함수 등록
        mRecordStart.setOnClickListener(this);
        mRecordStop.setOnClickListener(this);
        mMemoDialog.findViewById(R.id.record_clear).setOnClickListener(this);   // 공유 버튼
        mMemoDialog.findViewById(R.id.record_save).setOnClickListener(this);    // 저장 버튼

        super.initMemoDialog(context, width, height);
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
                start();
                break;
            case R.id.record_stop:
                stop();
                break;
            case R.id.record_clear:
                clear();
                break;
            case R.id.record_save:
                save();
                break;
        }
    }

    private void initRecorder(){
        // DB init
        mDate = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분").format(new Date());
        mTitle = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        mFileName = mTitle + ".mp4";

        File file = new File(mContext.getFilesDir(), mFileName);

//        Log.d("myLog", "RecordFileName = "+ mFileName);
//        Log.d("myLog", "RecordFilePath = "+ file.toString());

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(file.toString());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
    }

    void start(){
        if(mPlayed || mRecorder != null) return;
        mPlayed = true;
//        Log.d("myLog", "RecordMemo: start()");

        initRecorder();     // recorder 초기화

        mChronometer.setBase(SystemClock.elapsedRealtime());        // 0 으로 초기화
        mChronometer.start();

        mLength = SystemClock.elapsedRealtime();

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("myLog", "recorder prepare() failed");
        }

        mRecorder.start();

        Toast.makeText(mContext, "Record Start", Toast.LENGTH_SHORT).show();

        // 버튼 전환
        mRecordStart.setVisibility(View.GONE);
        mRecordStop.setVisibility(View.VISIBLE);
    }

    void stop(){
        if(!mPlayed) return;
        mPlayed = false;
//        Log.d("myLog", "RecordMemo: stop()");

        mChronometer.stop();        // timer 중지

        mRecorder.stop();           // recorder 중지

        mLength = SystemClock.elapsedRealtime() - mLength;
//        Log.d("myLog", "mLength = "+mLength);

        Toast.makeText(mContext, "Record Stop", Toast.LENGTH_SHORT).show();

        // 버튼 전환
        mRecordStop.setVisibility(View.GONE);
        mRecordStart.setVisibility(View.VISIBLE);
    }

    void clear(){
        if(mPlayed || mRecorder == null) return;

        mChronometer.setBase(SystemClock.elapsedRealtime());        // timer 초기화
        mRecorder.reset();          // recorder 삭제
        mRecorder = null;
    }

    void save(){
        if(mPlayed || mRecorder == null) return;
        Log.d("myLog", "RecordMemo: save()");
        mChronometer.setBase(SystemClock.elapsedRealtime());        // 0 으로 초기화
        mRecorder.release();
        mRecorder = null;

        String min = Long.toString(mLength/60000);
        String sec = Long.toString(mLength/1000);
        String mil = Long.toString(mLength%1000);

        try{
            RecordMemoDBHelper helper = new RecordMemoDBHelper(mContext);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into tb_record_memo (record_id, title, length, date) values (?, ?, ?, ?)",
                    new String[]{mFileName, mTitle,min+"분 "+sec+"."+mil+"초", mDate});
            db.close();

            Toast.makeText(mContext, "Record Saved", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "Saved Error", Toast.LENGTH_SHORT).show();
        }
    }

    //    void share(){
//        if(mPlayed) return;
////        Log.d("myLog", "RecordMemo: share()");
//
//        mRecorder = null;
//        File file = new File(mContext.getFilesDir(), mFileName);
//
////        Log.d("myLog", "RecordMemo: share() filename = "+mFileName);
//
//        // Audio -> URI
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.MediaColumns.TITLE, "temp");
//        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "temp.mp4");
//        values.put(MediaStore.Audio.Media.IS_RINGTONE, 1);
//        values.put(MediaStore.Audio.Media.IS_MUSIC, 1);
//        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis()/1000);
//        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4");
//        values.put(MediaStore.Audio.Media.DATA, file.toString());
//
//        Uri uri = mContext.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
//
//        Log.d("myLog", "uri = "+ uri);
//
//        if(uri!=null){
//            // 오디오 공유
//            Intent intent = new Intent(Intent.ACTION_SEND);     // 전송 메소드를 호출
//            intent.setType("audio/mp4");        // m4a 오디오를 공유 하기 위해 Type 을 정의
//            intent.putExtra(Intent.EXTRA_STREAM, uri);      // 오디오의 Uri 로드
//            Intent chooser = Intent.createChooser(intent, "Share");
//            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
//                mContext.startActivity(chooser);        // Activity 를 이용하여 호출
//            }
//        }
//    }
}
