package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.popup.LogoutPopupActivity;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.AppDataControlService;

public class ETCProfileActivity extends AppCompatActivity {

    private ConstraintLayout logout;
    private ImageButton prevButton;

    private TextView phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_profile);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        logout = findViewById(R.id.profile_logout);
        phoneNumber = findViewById(R.id.profile_phonenumberText);
        AppDataControlService appData = new AppDataControlService(getSharedPreferences("appData",  MODE_PRIVATE));
        phoneNumber.setText(appData.getPhoneNumber());
        prevButton = findViewById(R.id.profile_prevButton);

        setListeners();
    }

    private void setListeners(){
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 담아서 팝업(액티비티) 호출
                Intent intent = new Intent(ETCProfileActivity.this, LogoutPopupActivity.class);
                startActivity(intent);
            }
        });
    }
}
