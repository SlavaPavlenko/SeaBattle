package com.example.sunray.mergedproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class bgMusicService extends Service {
    MediaPlayer player;
    public bgMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return Service.START_STICKY;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this,
                getResources().getIdentifier("main_theme", "raw", getPackageName()));
        player.setLooping(true);
        player.start();
    }

    public void onStop() {
        player.stop();
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }
}
