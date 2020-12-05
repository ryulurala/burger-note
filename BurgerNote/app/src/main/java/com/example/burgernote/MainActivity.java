package com.example.burgernote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    MainService mService;        // 서비스랑 통신 가능함
    boolean mBound = false;      // Service 중인지 확인

    PagerAdapter adapter;
    ViewPager viewPager;

    final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Service 와 연결 됐을 시에 호출되는 메소드
            MainService.myBinder binder = (MainService.myBinder) service;
            mService = binder.getService();      // Service 객체 받음
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 예기치 않은 종료 or Service 와 연결이 끊겼을 경우 호출 메소드
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // tab 추가
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tab_drawing_24px));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tab_memo_24px));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tab_recording_24px));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tab_calendar_24px));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_tab_setting_24px));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Tab 이벤트에 대한 Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();      // 권한 검사 및 시작
        if(mBound){
            mService.hideView();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 홈 버튼 눌렀을 시 호출되는 메소드
        if(mBound){
            mService.showView();
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
        if (requestCode == 306) {       // ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 306
            if (!Settings.canDrawOverlays(this)) {
                // 권한 동의를 못얻음
                finish();       // 종료
            } else {
                // 권한 동의를 얻음
                openService();
            }
        }
    }

    void checkPermission() {
        // 명시적 권한 동의를 받아야 함, 참고: Q버전 이상은 Bubbles API(preview) 가능
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {          // 권한을 아직 안받았다.
                // 권한 setting 창을 키는 Intent
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName())
                );
                // 권한 Setting 창을 켬, 결과를 받는 메소드(+ Result)
                startActivityForResult(intent, 306);// ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 306
            } else {         // 이미 권한을 받았다.
                openService();
            }
        } else {        // 마시멜로우 미만 버전은 이미 설치했을 경우 권한 체크를 했다.
            openService();
        }
    }

    void openService(){
        Log.d("myLog", "openService()");
        if(!mBound) {
            // bind service
            bindService(
                    new Intent(this, MainService.class),
                    mConnection,
                    Context.BIND_AUTO_CREATE
            );
        }
    }

    void closeService(){
        Log.d("myLog", "closeService()");
        if(mBound){
            unbindService(mConnection);
        }
    }

}