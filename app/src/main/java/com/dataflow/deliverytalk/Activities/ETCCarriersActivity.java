package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.dataflow.deliverytalk.Activities.popup.CarrierTelPopupActivity;
import com.dataflow.deliverytalk.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class ETCCarriersActivity extends AppCompatActivity {


    private ImageButton cjCallButton;
    private ImageButton cjHomeButton;

    private ImageButton postCallButton;
    private ImageButton postHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_carriers);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        cjCallButton = findViewById(R.id.carriers_tel_cjlogistics);
        cjHomeButton = findViewById(R.id.carriers_home_cjlogistics);
        postCallButton = findViewById(R.id.carriers_tel_postoffice);
        postHomeButton = findViewById(R.id.carriers_home_postoffice);


        cjCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopup("cj");

            }
        });
        cjHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWebView("cj");
                Log.d("gogo","gogo");
            }
        });
        postCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopup("post");
            }
        });
        postHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWebView("post");
            }
        });
    }
    private void createPopup(String carrierId){
        Intent intent = new Intent(ETCCarriersActivity.this, CarrierTelPopupActivity.class);
        String carrierCode="";
        if(carrierId.equals("cj")){
            carrierCode = "kr.cjlogistics";

        }else if(carrierId.equals("post")){
            carrierCode = "kr.epost";
        }
        intent.putExtra("carrierCode", carrierCode);
        startActivity(intent);
    }
    private void createWebView(String carrierId){

        String url = "";
        if(carrierId.equals("cj")){
            url = "https://www.cjlogistics.com/ko/main";

        }else if(carrierId.equals("post")){
            url = "https://www.epost.go.kr/main.retrieveMainPage.comm";
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);


    }
}
