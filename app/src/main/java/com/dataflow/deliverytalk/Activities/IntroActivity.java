package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dataflow.deliverytalk.R;

public class IntroActivity extends AppCompatActivity {

    private SharedPreferences appData;
    private Boolean isLogin;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        isLogin = appData.getBoolean("isLogin",false);

        countDownTimer = new CountDownTimer(1500, 1300) {
            int i = 0;
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                Intent intent;
                if(isLogin){
                     intent = new Intent(IntroActivity.this, MainActivity.class);
                }else{
                    intent = new Intent(IntroActivity.this, IntroMessageActivity.class);
                }
                startActivity(intent);
                System.out.println("didi");
                finish();
            }
        }.start();


    }
}
