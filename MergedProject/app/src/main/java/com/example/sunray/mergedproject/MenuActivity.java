package com.example.sunray.mergedproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {
    boolean changingActivity = false;
    Intent bgMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

    public void PlayClick(View view) {
        changingActivity = true;
        Intent myIntent = new Intent(MenuActivity.this, PlayActivity.class);
        startActivity(myIntent);
    }

    public void SettingsClick(View view) {
        changingActivity = true;
        Intent myIntent = new Intent(MenuActivity.this, SettingsActivity.class);
        startActivity(myIntent);
    }

    public void AboutClick(View view) {
        changingActivity = true;
        Intent myIntent = new Intent(MenuActivity.this, AboutActivity.class);
        startActivity(myIntent);
    }
}
