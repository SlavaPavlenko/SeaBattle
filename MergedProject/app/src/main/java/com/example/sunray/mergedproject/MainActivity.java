package com.example.sunray.mergedproject;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnTouchListener {

    private TextView textView, tv;
    private Animation dimming, brighting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        dimming = AnimationUtils.loadAnimation(this, R.anim.dimming);
        dimming.setAnimationListener(dimmingListener);
        brighting = AnimationUtils.loadAnimation(this, R.anim.brighting);
        brighting.setAnimationListener(brightingListener);
        textView.startAnimation(dimming);
        tv = findViewById(R.id.screen);
        tv.setOnTouchListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        textView.clearAnimation();
    }

    Animation.AnimationListener dimmingListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            textView.startAnimation(brighting);
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    Animation.AnimationListener brightingListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            textView.startAnimation(dimming);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    @Override
    public boolean onTouch (View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(myIntent);
        }
        return true;
    }
}
