package com.example.backgroundfab;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 33;    // for. 요청 받고 나온 값 검사
    FloatingService myService;        // 서비스랑 통신 가능함
    boolean isService = false;      // Service 중인지 확인

    final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Service 와 연결 됐을 시에 호출되는 메소드
            FloatingService.FloatingBinder binder = (FloatingService.FloatingBinder) service;
            myService = binder.getService();      // Service 객체 받음
            isService = true;
            Log.d("myLog", "Activity: onServiceConnected()");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 예기치 않은 종료 or Service 와 연결이 끊겼을 경우 호출 메소드
            isService = false;
            Log.d("myLog", "Activity: onServiceDisconnected()");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // App 시작
        setContentView(R.layout.activity_main);         // activity_main layout 보여줌
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("myLog", "Activity: onStart()");
        checkPermission();      // 권한 검사
        if(isService){
            myService.hideView();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("myLog", "Activity: onStop()");
        // 홈 버튼 눌렀을 시 호출되는 메소드
        if(isService){
            myService.showView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 종료 했을 시 서비스도 같이 종료
        closeService();
    }

    @TargetApi(Build.VERSION_CODES.M)       // M버전 이상인 경우에만 적용 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // 권한 동의를 못얻음
                finish();       // 종료
            } else {
                // 권한 동의를 얻음
                openService();
            }
        }
    }

    public void checkPermission() {
        // 명시적 권한 동의를 받아야 함, 참고: Q버전 이상은 Bubbles API(preview) 가능
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {          // 권한을 아직 안받았다.
                // 권한 setting 창을 키는 Intent
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())
                );
                // 권한 Setting 창을 켬, 결과를 받는 메소드(+ Result)
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {         // 이미 권한을 받았다.
                openService();
            }
        } else {        // 마시멜로우 미만 버전은 이미 설치했을 경우 권한 체크를 했다.
            openService();
        }
    }

    public void openService(){
        Log.d("myLog", "openService()");
        if(!isService) {
            // bind service
//                    new Intent(this, MyService.class),
            bindService(
                    new Intent(this, FloatingService.class),
                    mConnection,
                    Context.BIND_AUTO_CREATE
            );
        }
    }

    public void closeService(){
        Log.d("myLog", "closeService()");
        if(isService){
            unbindService(mConnection);
        }
    }
}