package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dataflow.deliverytalk.R;

public class SelectCarrierActivity extends AppCompatActivity {

    private Intent intent = new Intent();
    private ConstraintLayout cj;
    private ConstraintLayout post;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        intent.putExtra("code", "");
//        intent.putExtra("carrierName", "");
        setResult(2, intent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_carrier);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#0DCCB5"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        cj = findViewById(R.id.select_carrier_cj);
        post = findViewById(R.id.select_carrier_post);

        setListeners();
    }

    private void setListeners(){

        cj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("code", "kr.cjlogistics");
                intent.putExtra("carrierName", "CJ대한통운");
                setResult(1, intent);
                finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("code", "kr.epost");
                intent.putExtra("carrierName", "우체국택배");
                setResult(1,  intent);
                finish();
            }
        });
    }


}
