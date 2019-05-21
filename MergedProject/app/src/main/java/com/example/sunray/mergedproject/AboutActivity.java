package com.example.sunray.mergedproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    boolean changingActivity = false;
    Intent bgMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        bgMusic = new Intent(this, bgMusicService.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!changingActivity) stopService(bgMusic);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (changingActivity)
            changingActivity= false;
        else startService(bgMusic);
    }

    @Override
    public void onBackPressed() {
        changingActivity = true;
        finish();
    }

}
