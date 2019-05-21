package com.dataflow.deliverytalk.Activities;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dataflow.deliverytalk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ETCPrivateTermsActivity extends AppCompatActivity {

    private TextView privateTermText1;
    private TextView privateTermText2;
    private TextView privateTermText3;

    private ImageButton prevButton;

    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_private_terms);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        privateTermText1 = findViewById(R.id.privateTerm_privateTermText1);
        privateTermText2 = findViewById(R.id.privateTerm_privateTermText2);
        privateTermText3 = findViewById(R.id.privateTerm_privateTermText3);

        prevButton = findViewById(R.id.privateTerm_prevButton);



        ref = FirebaseDatabase.getInstance("https://deliverytalk-31595.firebaseio.com").getReference("Terms");
        initDatabase();
        setListeners();

        // 서비스 약관 불러오기 & 삽입
        ref.child("log").setValue("check");

    }
    private void setListeners(){
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 데이터베이스 초기화
    private void initDatabase(){


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                privateTermText1.setText(dataSnapshot.child("private1").getValue().toString());
                privateTermText2.setText(dataSnapshot.child("private2").getValue().toString());
                privateTermText3.setText(dataSnapshot.child("private3").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
