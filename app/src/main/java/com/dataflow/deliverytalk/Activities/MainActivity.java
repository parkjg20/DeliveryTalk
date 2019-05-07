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
import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.Models.Person;
import com.dataflow.deliverytalk.Models.State;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.adapters.ParcelListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String databaseUrl = "https://deliverytalk-31595.firebaseio.com";
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

    private int trackingCode = 1;

    private List<ParcelModel> datas = new ArrayList<>();
    private DatabaseReference ref;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addButtonFlag = false;
        addButton.setRotation(0);
        ref.child("log").setValue("check");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        ref = FirebaseDatabase.getInstance().getReference("Parcels").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

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
        List<ParcelModel> myData = new ArrayList<>();
        switch(trackingCode){
            case 1:
                myData = datas;
                break;
            case 2:
                // 배송중 목록
                for(ParcelModel p : datas){
                    if(p.getState().getId().equals("delivered"))continue;
                    myData.add(p);
                }
                break;
            case 3:
                // 배송완료 목록
                for(ParcelModel p : datas){
                    if(!p.getState().getId().equals("delivered"))continue;
                    myData.add(p);
                }
                break;
        }
        // listView 바인딩
        ParcelListAdapter adapter;adapter = new ParcelListAdapter(myData);
        myParcelList.setAdapter(adapter);
    }

    // 데이터베이스 스캔
    private void initDatabase(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datas.clear();
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while(it.hasNext()){
                    DataSnapshot temp = it.next();
                    if(temp.getKey().equals("log")) break;
                    ParcelModel parcel = new ParcelModel();
                    initParcel(temp, parcel);
                    datas.add(parcel);
                }

                onTrackingCodeChange();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // 배송정보 내용 등록
    private void initParcel(DataSnapshot temp, ParcelModel parcel){
        parcel.setTitle( temp.child("title").getValue().toString());
        parcel.setFrom(new Person(temp.child("from").child("name").getValue().toString(), temp.child("from").child("time").getValue().toString()));
        parcel.setWaybill(temp.child("waybill").getValue().toString());
        parcel.setCarrier(
                new Carrier(
                        temp.child("carrier").child("id").getValue().toString(),
                        temp.child("carrier").child("name").getValue().toString(),
                        temp.child("carrier").child("tel").getValue().toString()
                )
        );
//        Log.d("alarm", temp.)
        parcel.setAlarm(temp.child("alarm").getValue().toString().trim().equals(true));
        parcel.setState(
                new State(
                        temp.child("state").child("id").getValue().toString(),
                        temp.child("state").child("text").getValue().toString()
                ));
    }

}
