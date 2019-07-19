package com.dataflow.deliverytalk.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.AppDataControlService;
import com.dataflow.deliverytalk.util.adapters.SwitchButton;

public class ETCPushActivity extends AppCompatActivity {
    private ImageButton prevButton;
    private SwitchButton pushButton;
    private SwitchButton smsButton;
    AppDataControlService appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_push);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        prevButton = findViewById(R.id.push_prevButton);
        pushButton = findViewById(R.id.push_pushAllowButton);
        smsButton = findViewById(R.id.push_smsAllowButton);
        appData = new AppDataControlService(getSharedPreferences("appData", MODE_PRIVATE));
        initView();
        setListeners();
    }
    private void setListeners(){
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        pushButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                appData.changeFlag("pushFlag", isChecked);
            }
        });

        smsButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                appData.changeFlag("smsFlag", isChecked);
            }
        });

    }

    private void initView(){
        smsButton.setChecked(appData.getSmsFlag());
        pushButton.setChecked(appData.getPushFlag());
    }

}
