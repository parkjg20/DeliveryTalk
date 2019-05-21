package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

public class ETCInformationActivity extends AppCompatActivity {

    private final String databaseUrl = "https://deliverytalk-31595.firebaseio.com";
    private ConstraintLayout serviceTerm;
    private ConstraintLayout privateTerm;

    private TextView presentVersion;
    private TextView latestVersion;
    private ImageButton prevButton;
    private TextView note;

    private String lv;
    private String pv;

    DatabaseReference ref = FirebaseDatabase.getInstance(databaseUrl).getReference("Version");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_information);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        prevButton = findViewById(R.id.information_prevButton);
        serviceTerm = findViewById(R.id.information_serviceTerm);
        privateTerm = findViewById(R.id.information_privateTerm);
        presentVersion = findViewById(R.id.information_presentVersionText);
        latestVersion = findViewById(R.id.information_latestVersionText);
        note = findViewById(R.id.information_note);

        try{
            pv = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            presentVersion.setText(pv);
        }catch (Exception e){
            e.printStackTrace();
        }
        setListeners();
        initDatabase();
    }

    private void initDatabase(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lv = dataSnapshot.child("LatestVersion").getValue().toString();
                latestVersion.setText(lv);

                compareVersion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ref.child("log").setValue("check");

    }

    private void compareVersion(){
        if(!lv.equals(pv)){
            note.setText("업데이트가 있습니다.");
        }

    }

    private void setListeners(){
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
