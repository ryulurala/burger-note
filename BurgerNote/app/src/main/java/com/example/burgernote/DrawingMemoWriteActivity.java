package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DrawingMemoWriteActivity extends AppCompatActivity implements View.OnClickListener {

    Button addBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_memo_write);

        addBtn = findViewById(R.id.drawing_memo_add_btn);

        addBtn.setOnClickListener(this);
    }

    public void onClick(View view){

        // 지금 시간을 yyyy년 MN월 dd일 HH시 mm분 포맷의 문자열로 저장합니다.
        String date = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분").format(new Date());

        // saveImage() 함수를 이용해 이미지를 저장하고 그 파일명을 return 받습니다.
        String image = saveImage();
        if(image != "Exception"){
            DrawingMemoDBHelper helper = new DrawingMemoDBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into tb_drawing_memo (image, date) values (?, ?)", new String[]{image, date});
            db.close();

            Toast toast = Toast.makeText(getApplicationContext(), "입력이 완료되었습니다,", Toast.LENGTH_SHORT );
            toast.show();
        }
    }

    private String saveImage() {
        // Drawable 폴더에서 test_image_1, 2, 3, 4 중 하나를 내부 저장 공간에 저장하고
        // 그 파일명을 return 합니다.
        // 저장에 실패했다면 "Exception" 이라는 문자열을 return 합니다.
        // 파일 명은 저장 버튼을 누르는 시간을 yyyyMMddHHmmssSSS으로 합니다. ( SSS는 밀리세컨드임 )
        // 실습 07 파일 다루기 pdf 파일을 참고했습니다.

        // test_image_1에서 test_image_4 사이에서 하나를 선택
        // 그 이미지를 Bitmap 파일로 만들어서 저장.
        String testImageFileName = "test_image_" + (int)(Math.random()*4 + 1);
        int testImageID = getResources().getIdentifier(testImageFileName, "drawable", getPackageName());
        Bitmap image = BitmapFactory.decodeResource( getResources(), testImageID );

        FileOutputStream outStream;
        try {
            String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".png";
            File file = new File(getFilesDir(), fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            outStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception";
        }
    }


}