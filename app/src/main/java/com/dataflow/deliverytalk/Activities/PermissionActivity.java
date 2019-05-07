package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dataflow.deliverytalk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Permission;

public class PermissionActivity extends AppCompatActivity {

    private TextView serviceTerm;
    private TextView privateTerm1;
    private TextView privateTerm2;
    private TextView privateTerm3;

    private DatabaseReference ref;

    private CheckBox allButton;
    private CheckBox serviceButton;
    private CheckBox privateButton;
    private CheckBox smsButton;

    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        ref = FirebaseDatabase.getInstance("https://deliverytalk-31595.firebaseio.com").getReference("Terms");
        initDatabase();

        // 서비스 약관 & 이용약관 TextView
        serviceTerm = findViewById(R.id.permission_service_term);
        privateTerm1 = findViewById(R.id.permission_private_term1);
        privateTerm2 = findViewById(R.id.permission_private_term2);
        privateTerm3 = findViewById(R.id.permission_private_term3);

        // 약관 동의 버튼
        allButton = findViewById(R.id.permission_all_button);
        serviceButton = findViewById(R.id.permission_service_button);
        privateButton = findViewById(R.id.permission_private_button);
        smsButton = findViewById(R.id.permission_sms_button);

        // 다음 버튼
        nextButton = findViewById(R.id.permission_next_button);

        // 서비스 약관 불러오기 & 삽입
        ref.child("log").setValue("check");

        // 버튼 로직
        // 모두 동의
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allButton.isChecked()){
                    serviceButton.setChecked(true);
                    privateButton.setChecked(true);
                    smsButton.setChecked(true);
                    setNextButton(true);
                }else{
                    serviceButton.setChecked(false);
                    privateButton.setChecked(false);
                    smsButton.setChecked(false);
                    setNextButton(false);
                }
            }
        });

        //서비스 약관 동의
        serviceButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(privateButton.isChecked() ){
                        setNextButton(true);
                        if(smsButton.isChecked()){
                            allButton.setChecked(true);
                        }
                    }
                }else{
                    allButton.setChecked(false);
                    setNextButton(false);
                }
            }
        });

        // 개인정보 이용 약관 동의
        privateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(serviceButton.isChecked() ){
                        setNextButton(true);
                        if(smsButton.isChecked()){
                            allButton.setChecked(true);
                        }
                    }
                }else{
                    allButton.setChecked(false);
                    setNextButton(false);
                }
            }
        });

        smsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(serviceButton.isChecked() && privateButton.isChecked()){
                        allButton.setChecked(true);
                    }
                }else{
                    allButton.setChecked(false);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nextButton.isEnabled()){
                    SharedPreferences appData = getSharedPreferences("appData", MODE_PRIVATE);
                    SharedPreferences.Editor edt = appData.edit();
                    edt.putBoolean("smsFlag", smsButton.isChecked());
                    edt.putBoolean("pushFlag", getIntent().getBooleanExtra("push", false));
                    edt.commit();

                    Intent intent = new Intent(PermissionActivity.this, LoginActivity.class);
                    intent.putExtra("phonenumber", "");
                    startActivity(intent);
                }
            }
        });


    }

    // 다음 버튼 상태 변경
    private void setNextButton(boolean flag){
        if(flag){
            nextButton.setEnabled(true);
            nextButton.setBackgroundColor(Color.parseColor("#0DCCB5"));
        }else{
            nextButton.setEnabled(false);
            nextButton.setBackgroundColor(Color.parseColor("#DEDEDE"));
        }
    }
    // 데이터베이스 초기화
    private void initDatabase(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                privateTerm1.setText(dataSnapshot.child("private1").getValue().toString());
                privateTerm2.setText(dataSnapshot.child("private2").getValue().toString());
                privateTerm3.setText(dataSnapshot.child("private3").getValue().toString());

                serviceTerm.setText(dataSnapshot.child("service").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
