package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.dataflow.deliverytalk.R;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout gotoEtc;
    private ConstraintLayout whole;
    private ConstraintLayout onDelivery;
    private ConstraintLayout arrived;

    private int trackingCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        gotoEtc = findViewById(R.id.main_footer_etc);
        whole = findViewById(R.id.main_nav_whole);
        onDelivery=  findViewById(R.id.main_nav_onDelivery);
        arrived = findViewById(R.id.main_nav_arrived);

        setListeners();


    }

    private void setListeners(){
        gotoEtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ETCActivity.class);
                startActivity(intent);
            }
        });

        whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearActive();
                imSelected(whole, 1);
            }
        });
        onDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearActive();
                imSelected(onDelivery, 2);
            }
        });
        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearActive();
                imSelected(arrived, 3);
            }
        });

    }

    private void imSelected(ConstraintLayout ele, int trackingCode){
        ele.setBackground(getDrawable(R.drawable.borderbottom_active));
        this.trackingCode = trackingCode;
        onTrackingCodeChange();
    }

    private void clearActive(){
        whole.setBackground(getDrawable(R.drawable.can_press_layout));
        onDelivery.setBackground(getDrawable(R.drawable.can_press_layout));
        arrived.setBackground(getDrawable(R.drawable.can_press_layout));
    }

    private void onTrackingCodeChange(){

    }
}
