package com.dataflow.deliverytalk.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dataflow.deliverytalk.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.List;

public class IntroMessageActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ref;

    // 뒤로가기 버튼
    private ImageButton prevButton;

    // 프로필
    private ImageView profileic;
    private TextView profileName;

    // chat bubbles
    private TextView[] textviews = new TextView[6];
    private TextView myMessage;
    private LinearLayout pushLayout;

    // push & start buttons
    private Button startButton;
    private Button pushYes;
    private Button pushNo;
    private boolean push;

    private List<String> messages = new ArrayList<String>();
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_message);
        System.out.println("before");
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        //View 등록
        startButton = findViewById(R.id.introMessage_startButton);
        pushYes = findViewById(R.id.introMessage_yesButton);
        pushNo = findViewById(R.id.introMessage_noButton);
        pushLayout = findViewById(R.id.introMessage_pushLayout);
        profileic = findViewById(R.id.introMessage_profile);
        profileName = findViewById(R.id.introMessage_name);
        textviews[0] = findViewById(R.id.introMessage_message1);
        textviews[1] = findViewById(R.id.introMessage_message2);
        textviews[2] = findViewById(R.id.introMessage_message3);
        textviews[3] = findViewById(R.id.introMessage_message4);
        textviews[4] = findViewById(R.id.introMessage_message5);
        textviews[5] = findViewById(R.id.introMessage_message6);
        myMessage = findViewById(R.id.introMessage_myMessage);
        prevButton = findViewById(R.id.introMessage_prevButton);

        initDatabase();

        // 버튼 이벤트
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pushYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPushPermission(true);
            }
        });
        pushNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPushPermission(false);
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroMessageActivity.this, PermissionActivity.class);
                intent.putExtra("push", push);
                startActivity(intent);
            }
        });
    }

    private void getPushPermission(boolean allow){
        myMessage.setText((allow)?"알림 허용":"다음에 할게");
        myMessage.setVisibility(View.VISIBLE);
        push = allow;
        pushLayout.setVisibility(View.GONE);
        countDownTimer = new CountDownTimer(1100, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                afterAnimation();
            }
        }.start();
    }

    private void initDatabase(){
        database = FirebaseDatabase.getInstance("https://deliverytalk-31595.firebaseio.com");
        ref = database.getReference("IntroMessages/");
        ref.child("log").setValue("check");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=1; i<dataSnapshot.getChildrenCount(); i++){
                    messages.add(dataSnapshot.child(""+i).getValue().toString());
                }
                System.out.print("length : "+ messages.size());

                initView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initView(){
        for(int i=0; i< textviews.length; i++){
            textviews[i].setText(messages.get(i));
        }

        beforeAnimation();
    }

    private void beforeAnimation(){
        profileic.setVisibility(View.VISIBLE);
        profileName.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(3100, 1000) {
            int i = 0;
            public void onTick(long millisUntilFinished) {
                textviews[i].setVisibility(View.VISIBLE);
                i++;
            }
            public void onFinish() {
                textviews[i].setVisibility(View.VISIBLE);
                pushLayout.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void afterAnimation(){
        countDownTimer = new CountDownTimer(1100, 1000) {
            public void onTick(long millisUntilFinished) {
                textviews[4].setVisibility(View.VISIBLE);
            }
            public void onFinish() {
                textviews[5].setVisibility(View.VISIBLE);
                startButton.setVisibility(View.VISIBLE);
            }
        }.start();
    }

}
