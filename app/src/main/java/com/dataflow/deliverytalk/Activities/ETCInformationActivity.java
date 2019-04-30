package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.dataflow.deliverytalk.R;

public class ETCInformationActivity extends AppCompatActivity {

    private ConstraintLayout serviceTerm;
    private ConstraintLayout privateTerm;

    private ImageButton prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_information);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        serviceTerm = findViewById(R.id.information_serviceTerm);
        privateTerm = findViewById(R.id.information_privateTerm);

        serviceTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivity("service");
            }
        });
        privateTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createActivity("private");
            }
        });

    }
    private void createActivity(String where){
        Intent intent;
        if(where.equals("service")){
            intent = new Intent(ETCInformationActivity.this, ETCServiceTermsActivity.class);
        }else{
            intent = new Intent(ETCInformationActivity.this, ETCPrivateTermsActivity.class);
        }

        startActivity(intent);


    }
}
