package com.example.fab;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class FloatingService extends Service {
    private FloatingHeadWindow floatingHeadWindow;
    private IBinder mBinder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("myLog", "FloatingService onStartCommand");
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    private void init() {
        Log.d("myLog", "FloatingService init");
        if(floatingHeadWindow == null){
            Log.d("myLog", "FloatingHeadWindow is not initialized from FloatingService");
            Log.d("myLog", "appContext = " + getApplicationContext().toString());
            floatingHeadWindow = new FloatingHeadWindow(getApplicationContext());
            Log.d("myLog", "after new FloatingHeadWindow()");
            floatingHeadWindow.create();
            Log.d("myLog", "After create() FloatingHeadWindow from FloatingService");
            floatingHeadWindow.createLayoutParams();
            Log.d("myLog", "After createLayoutParams() FloatingHeadWindow from FloatingService");
            floatingHeadWindow.show();
            Log.d("myLog", "After show() FloatingHeadWindow from FloatingService");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("myLog", "FloatingService onBind()");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("myLog", "FloatingService onCreate");
    }

    class LocalBinder extends Binder{
        public FloatingService getService(){
            return FloatingService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        floatingHeadWindow.hide();
        Log.d("myLog", "FloatingService onDestroy");
    }
}
