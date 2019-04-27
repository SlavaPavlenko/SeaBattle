package com.example.sunray.mergedproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void PlayClick(View view) {
        Intent myIntent = new Intent(MenuActivity.this, PlayActivity.class);
        startActivity(myIntent);
    }

    public void SettingsClick(View view) {
        Intent myIntent = new Intent(MenuActivity.this, SettingsActivity.class);
        startActivity(myIntent);
    }

    public void AboutClick(View view) {
        Intent myIntent = new Intent(MenuActivity.this, AboutActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
