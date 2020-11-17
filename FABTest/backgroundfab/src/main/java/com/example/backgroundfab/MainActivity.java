package com.example.backgroundfab;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;             // for. 요청 받고 나온 값 검사

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_start = (Button) findViewById(R.id.bt_start);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();          // 권한 검사 메소드
                //startService(new Intent(MainActivity.this, AlwaysOnTopService.class));
            }
        });

        Button bt_stop = (Button) findViewById(R.id.bt_stop);
        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(MainActivity.this, MyService.class));
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)       // M버전 이상인 경우에만 적용 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // TODO 동의를 얻지 못했을 경우의 처리

            } else {        // 권한 동의를 얻음
                startService(new Intent(MainActivity.this, MyService.class));
            }
        }
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {          // 권한을 아직 안받았다.
                // 권한 setting 창을 키는 Intent
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                // 권한 Setting 창을 켬, 결과를 받는 메소드(+ Result)
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {         // 이미 권한을 받았다.
                startService(new Intent(MainActivity.this, MyService.class));
            }
        } else {        // 마시멜로우 미만 버전은 이미 설치했을 경우 권한 체크를 했다.
            startService(new Intent(MainActivity.this, MyService.class));
        }
    }
}