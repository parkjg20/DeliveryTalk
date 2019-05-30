package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dataflow.deliverytalk.Activities.popup.EventDialogPopup;
import com.dataflow.deliverytalk.R;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class IntroActivity extends AppCompatActivity {

    public static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";
    private SharedPreferences appData;
    private Boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        isLogin = appData.getBoolean("isLogin",false);

        CountDownTimer cnt = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
//        인터넷 연결 체크하는 부분 ( 추후에 작성 )
//        if(!result) {
//            intent = new Intent(IntroActivity.this, EventDialogPopup.class);
//            intent.putExtra("title", "[error 0]");
//            intent.putExtra("content", "인터넷 연결을 확인할 수 없습니다.");
//            startActivity(intent);
//            finish();
//        }else{
//            Log.d("internet", "인터넷 연결 확인");

                Intent intent;
                if(isLogin){
                    intent = new Intent(IntroActivity.this, MainActivity.class);
                }else{
                    intent = new Intent(IntroActivity.this, IntroMessageActivity.class);
                }
                startActivity(intent);
                finish();
//        }
            }
        }.start();

    }
}
