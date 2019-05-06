package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.popup.AddWaybillPopupActivity;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.ParcelListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout gotoEtc;
    private ConstraintLayout whole;
    private ConstraintLayout onDelivery;
    private ConstraintLayout arrived;

    private TextView wholeText;
    private TextView onDeliveryText;
    private TextView arrivedText;



    private ImageButton addButton;
    private boolean addButtonFlag;
    private ListView myParcelList;
    private ParcelListAdapter adapter;
    private int trackingCode = 1;


    private List<Map<String, String>> datas = new ArrayList<>();
    private DatabaseReference ref;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getBooleanExtra("success", false)){
            ref.child("log").setValue("check");
        }else{

        }
    }

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
        myParcelList = findViewById(R.id.main_listView);
        addButton = findViewById(R.id.main_addButton);

        wholeText = findViewById(R.id.main_nav_whole_text);
        onDeliveryText = findViewById(R.id.main_nav_onDelivery_text);
        arrivedText = findViewById(R.id.main_nav_arrived_text);

        //초기화
        setListeners();
        initDatabase();
        whole.setBackground(getDrawable(R.drawable.borderbottom_active));
        wholeText.setTextColor(Color.parseColor("#0DCCB5"));
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Log.d("current", auth.getCurrentUser().getPhoneNumber());

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
                navbarSelected(whole, wholeText,1);
            }
        });
        onDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navbarSelected(onDelivery, onDeliveryText, 2);
            }
        });
        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navbarSelected(arrived, arrivedText,3);
            }
        });

        // 운송장 등록버튼
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addButtonFlag){
                    addButtonFlag = true;
                    addButton.setRotation(45);
                    Intent intent = new Intent(MainActivity.this, AddWaybillPopupActivity.class);
                    startActivityForResult(intent,1);
                }else{
                    addButtonFlag = false;
                    addButton.setRotation(0);
                }
            }
        });
    }


    private void navbarSelected(ConstraintLayout ele, TextView textView, int trackingCode){
        clearActive();
        Log.d("test"," 클릭");
        ele.setBackground(getDrawable(R.drawable.borderbottom_active));
        textView.setTextColor(Color.parseColor("#0DCCB5"));
        this.trackingCode = trackingCode;
        onTrackingCodeChange();
    }

    private void clearActive(){
        whole.setBackground(getDrawable(R.drawable.can_press_layout));
        onDelivery.setBackground(getDrawable(R.drawable.can_press_layout));
        arrived.setBackground(getDrawable(R.drawable.can_press_layout));
        wholeText.setTextColor(Color.parseColor("#333333"));
        onDeliveryText.setTextColor(Color.parseColor("#333333"));
        arrivedText.setTextColor(Color.parseColor("#333333"));
    }

    // 어댑터 생성
    private void onTrackingCodeChange(){
        adapter = new ParcelListAdapter(datas);
        myParcelList.setAdapter(adapter);
    }

    // 데이터베이스 스캔
    private void initDatabase(){
        ref = FirebaseDatabase.getInstance("https://deliverytalk-31595.firebaseio.com").getReference("Parcels").child("+1055431787");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch(trackingCode){
                    case 1:
                        Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                        while(it.hasNext()){
                            Map<String, String> map = new HashMap<>();
                            DataSnapshot temp = it.next();
                            if(temp.getKey().equals("log")){
                                break;
                            }
                            map.put("title", temp.child("title").getValue().toString());
                            map.put("sender", temp.child("sender").getValue().toString());
                            map.put("waybill", temp.child("waybill").getValue().toString());
                            map.put("carrierName", temp.child("carrierName").getValue().toString());
                            datas.add(map);
                        }
                        break;
                    case 2:
                        break;
                    case 3:
                        break;

                }
                ref.child("log").setValue("check");
                onTrackingCodeChange();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
