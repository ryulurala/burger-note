package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RecordMemoTest extends AppCompatActivity {

    // Record memo의 DB가 잘 작동하는지 확인하기 위한
    // 임시 액티비티입니다.

    // 이 액티비티에서는 DB와 저장소에 파일을 저장하는 작업을 하는데
    // 테스트 용이므로 이후에 삭제해도 됩니다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_memo_test);
    }
}