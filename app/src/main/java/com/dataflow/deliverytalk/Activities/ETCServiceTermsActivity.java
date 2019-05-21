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

public class ETCServiceTermsActivity extends AppCompatActivity {

    private TextView serviceTermText;
    private ImageButton prevButton;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_service_terms);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        serviceTermText = findViewById(R.id.serviceTerm_serviceTermText);
        prevButton = findViewById(R.id.serviceTerm_prevButton);

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

                serviceTermText.setText(dataSnapshot.child("service").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
