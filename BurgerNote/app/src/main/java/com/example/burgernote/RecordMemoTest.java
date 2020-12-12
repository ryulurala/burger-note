package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordMemoTest extends AppCompatActivity implements View.OnClickListener {

    // Record memo의 DB가 잘 작동하는지 확인하기 위한
    // 임시 액티비티입니다.

    // 이 액티비티에서는 DB와 저장소에 파일을 저장하는 작업을 하는데
    // 테스트 용이므로 이후에 삭제해도 됩니다.

    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_memo_test);

        addBtn = findViewById(R.id.record_memo_test_add_btn);

        addBtn.setOnClickListener(this);
    }

    public void onClick(View view) {

        // 지금 시간을 yyyy년 MN월 dd일 HH시 mm분 포맷의 문자열로 저장합니다.
        String date = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분").format(new Date());
        String title = "테스트";
        String length = "4분 33초";

        // saveImage() 함수를 이용해 녹음을 저장하고 그 파일명을 return 받습니다.
        Log.i("테스트", "0번");
        String id = saveRecord();
        Log.i("테스트", id);
        if(id != "Exception"){
            Log.i("테스트", "10번");
            RecordMemoDBHelper helper = new RecordMemoDBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into tb_record_memo (record_id, title, length, date) values (?, ?, ?, ?)", new String[]{id, title, length, date});
            db.close();
        }
    }

    private String saveRecord() {

        FileOutputStream outStream;
        int testRecordID = getResources().getIdentifier("test.mp3", "raw", getPackageName());
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".mp3";
        File file = new File(getFilesDir(), fileName);

        InputStream in = null;
        OutputStream out = null;
        try
        {
            if (!file.exists()) {
                file.createNewFile();
            }

            out = new FileOutputStream(file);
            Log.i("테스트", "1번");
            in = getApplicationContext().getAssets().open("test.mp3");
            Log.i("테스트", "1번");
            Log.i("테스트", "2번");
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }finally{
            if(in!=null){
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Exception";
                }
            }
            if(out!=null){
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Exception";
                }
            }
        }
        return fileName;
    }
}