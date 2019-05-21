package com.example.sunray.mergedproject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class soundService extends Service {

    public soundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
