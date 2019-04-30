package com.dataflow.deliverytalk.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dataflow.deliverytalk.R;

public class ETCActivity extends AppCompatActivity {

    // View의 요소 등록
    private ConstraintLayout myProfile;
    private ConstraintLayout notice;
    private ConstraintLayout question;
    private ConstraintLayout carriers;
    private ConstraintLayout push;
    private ConstraintLayout info;

    private ImageButton prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // 요소 초기화
        myProfile = findViewById(R.id.etc_myProfile);
        notice = findViewById(R.id.etc_notice);
        question = findViewById(R.id.etc_question);
        carriers = findViewById(R.id.etc_carriers);
        push = findViewById(R.id.etc_setpush);
        info = findViewById(R.id.etc_info);

        prevButton = findViewById(R.id.etc_prevButton);

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivity(new ETCProfileActivity());
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivity(new ETCNoticeActivity());
            }
        });
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivity(new ETCQuestionActivity());
            }
        });
        carriers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivity(new ETCCarriersActivity());
            }
        });
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivity(new ETCPushActivity());
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivity(new ETCInformationActivity());
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // layout 클릭 시 화면 생성( 오버랩 )
    private void createActivity(Object object){
        Intent intent = new Intent(ETCActivity.this, object.getClass());
        startActivity(intent);

    }
}
