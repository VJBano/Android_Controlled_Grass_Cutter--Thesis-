package com.example.robocuttercontroller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashSC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sc);

        //e hide ang action bar or title bar
        getSupportActionBar().hide();

        Handler hand = new Handler();

        //e display ang splash screen ng naat delay nga 3secs.
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                //mo next sa lain nga page or layout
                Intent next = new Intent(SplashSC.this,MainActivity.class);
                startActivity(next);
                finish();
            }
        }, 3000);
    }
}