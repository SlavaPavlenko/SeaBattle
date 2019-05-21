package com.example.sunray.mergedproject;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    boolean changingActivity = false;
    Intent bgMusic;
    Intent sound;
    boolean binded;
    Switch musicSwitch;
    Switch soundSwitch;
    ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bgMusic = new Intent(this, bgMusicService.class);
        sound = new Intent(this, soundService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binded = true;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                binded = false;
            }
        };
        startService(sound);
        bindService(sound, serviceConnection, BIND_AUTO_CREATE);   //0, BIND_AUTO_CREATE
        //TODO: data transfer though binded service. Lesson #98
        musicSwitch = findViewById(R.id.musicSwitch);
        musicSwitch.setOnCheckedChangeListener(musicSwitchListener);
        soundSwitch = findViewById(R.id.soundSwitch);
        soundSwitch.setOnCheckedChangeListener(soundSwitchListener);
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

    CompoundButton.OnCheckedChangeListener musicSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                startService(bgMusic);
            else stopService(bgMusic);
        }
    };

    CompoundButton.OnCheckedChangeListener soundSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                buttonView.setSoundEffectsEnabled(true);
            else buttonView.setSoundEffectsEnabled(false);
        }
    };
}
