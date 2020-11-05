package com.example.fab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class FloatingActivity extends AppCompatActivity {

    private final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;
    private String TAG = "Floating";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myLog", "FloatingActivity onCreate()");
        setContentView(R.layout.activity_floating);
        checkPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("myLog", "FloatingActivity onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("myLog", "FloatingActivity onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("myLog", "FloatingActivity onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("myLog", "FloatingActivity onStart");
    }

    void checkPermission(){
        Log.d("myLog", "FloatingActivity checkPermission()");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!Settings.canDrawOverlays(this)){
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:"+getPackageName())
                );
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }else{
                Log.d("myLog", "from FloatingActivity2 startService");
                startService();
            }
        }else{
            startService();
        }
    }

    private void startService() {
        Intent floatingService = new Intent(getApplicationContext(), FloatingService.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            startService(floatingService);
        }
    }

    private void myStopService(){
        stopService(new Intent(getApplicationContext(), FloatingService.class));
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE){
            if(!Settings.canDrawOverlays(this)){
                // 동의를 얻지 못했을 경우
                Log.d("myLog", "FloatActivity Finish()");
                finish();
            } else{
                startService();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("myLog", "FloatActivity onDestroy()");
//        myStopService();
    }
}