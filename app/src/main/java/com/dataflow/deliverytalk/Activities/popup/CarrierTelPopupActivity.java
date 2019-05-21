package com.dataflow.deliverytalk.Activities.popup;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dataflow.deliverytalk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class CarrierTelPopupActivity extends AppCompatActivity {

    private Button noButton;
    private Button yesButton;

    private TextView tel;
    private String telValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_carrier_tel_popup);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        telValue = getIntent().getStringExtra("tel");
        if(telValue.isEmpty() || telValue.length()  < 1){
            Toast.makeText(this, "올바르지 않은 요청입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        noButton = findViewById(R.id.telPopup_no);
        yesButton = findViewById(R.id.telPopup_yes);
        tel = findViewById(R.id.telPopup_title);

        tel.setText(telValue);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:"+telValue));
                    startActivity(intent);
                    finish();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
