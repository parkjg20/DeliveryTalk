package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.popup.AddWaybillPopupActivity;
import com.dataflow.deliverytalk.Activities.popup.EventDialogPopup;
import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.Models.Person;
import com.dataflow.deliverytalk.Models.Progress;
import com.dataflow.deliverytalk.Models.State;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.adapters.ParcelListAdapter;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity {

    private final String databaseUrl = "https://deliverytalk-31595.firebaseio.com";
    private ConstraintLayout gotoEtc;
    private ConstraintLayout gotoST;
    private ConstraintLayout whole;
    private ConstraintLayout onDelivery;
    private ConstraintLayout arrived;
    private SwipeRefreshLayout swipeLayout;

    private TextView wholeText;
    private TextView onDeliveryText;
    private TextView arrivedText;

    private DrawerLayout drawerLayout;
    private View drawerView;

    private ImageButton alarmOpenButton;
    private ImageButton alarmCloseButton;
    private ImageButton addButton;
    private boolean addButtonFlag;
    private ListView myParcelList;

    private int trackingCode = 1;

    List<ParcelModel> myData;
    private List<ParcelModel> datas = new ArrayList<>();
    private DatabaseReference parcels;

    private boolean init = false;
    private boolean doubleClick = false;


    private ValueEventListener valueEventListener;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        addButtonFlag = false;
        addButton.setRotation(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        parcels = FirebaseDatabase.getInstance(databaseUrl).getReference("Parcels").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());


        drawerLayout = findViewById(R.id.main_layout);
        drawerView = findViewById(R.id.main_drawer);
        swipeLayout = findViewById(R.id.swipeLayout);

        gotoST = findViewById(R.id.main_footer_shoptalk);
        gotoEtc = findViewById(R.id.main_footer_etc);
        whole = findViewById(R.id.main_nav_whole);
        onDelivery = findViewById(R.id.main_nav_onDelivery);
        arrived = findViewById(R.id.main_nav_arrived);
        myParcelList = findViewById(R.id.main_listView);

        addButton = findViewById(R.id.main_addButton);
        alarmOpenButton = findViewById(R.id.main_alarmButton);
        alarmCloseButton = findViewById(R.id.main_drawer_closeButton);

        wholeText = findViewById(R.id.main_nav_whole_text);
        onDeliveryText = findViewById(R.id.main_nav_onDelivery_text);
        arrivedText = findViewById(R.id.main_nav_arrived_text);

        //초기화
        setListeners();
        initDatabase();

        whole.setBackground(getDrawable(R.drawable.borderbottom_active));
        wholeText.setTextColor(Color.parseColor("#0DCCB5"));
    }

    private void setListeners() {

        gotoST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventDialogPopup.class);
                intent.putExtra("title", "서비스 준비중입니다.");
                intent.putExtra("content", "빠른 시일 내에 찾아뵙겠습니다");
                startActivity(intent);
            }
        });
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
                navbarSelected(whole, wholeText, 1);
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
                navbarSelected(arrived, arrivedText, 3);
            }
        });

        // 운송장 등록버튼
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addButtonFlag) {
                    addButtonFlag = true;
                    addButton.setRotation(45);
                    Intent intent = new Intent(MainActivity.this, AddWaybillPopupActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    addButtonFlag = false;
                    addButton.setRotation(0);
                }
            }
        });

        alarmOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!doubleClick) {
                    doubleClick = true;
                    drawerLayout.openDrawer(drawerView);

                    doubleClick = false;
                }
            }
        });
        alarmCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
            }

            @Override
            public void onDrawerStateChanged(int i) {
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                parcels.addListenerForSingleValueEvent(valueEventListener);
                Log.d("새로고침","실행");
                swipeLayout.setRefreshing(false);
            }
        });
    }


    private void navbarSelected(ConstraintLayout ele, TextView textView, int trackingCode) {
        clearActive();
        ele.setBackground(getDrawable(R.drawable.borderbottom_active));
        textView.setTextColor(Color.parseColor("#0DCCB5"));
        this.trackingCode = trackingCode;
        onTrackingCodeChange();
    }

    private void clearActive() {
        whole.setBackground(getDrawable(R.drawable.can_press_layout));
        onDelivery.setBackground(getDrawable(R.drawable.can_press_layout));
        arrived.setBackground(getDrawable(R.drawable.can_press_layout));
        wholeText.setTextColor(Color.parseColor("#333333"));
        onDeliveryText.setTextColor(Color.parseColor("#333333"));
        arrivedText.setTextColor(Color.parseColor("#333333"));
    }

    // 어댑터 생성
    private void onTrackingCodeChange() {
        myData = new ArrayList<>();
        switch (trackingCode) {
            case 1:
                for(ParcelModel p : datas){
                    myData.add(0, p);
                }
                break;
            case 2:
                // 배송중 목록
                for (ParcelModel p : datas) {
                    if (p.getState().getId().equals("delivered")) continue;
                    myData.add(0,p);
                }
                break;
            case 3:
                // 배송완료 목록
                for (ParcelModel p : datas) {
                    if (!p.getState().getId().equals("delivered")) continue;
                    myData.add(0, p);
                }
                break;
        }

        if (myData.size() > 0) {
            // listView 바인딩
            ParcelListAdapter adapter = new ParcelListAdapter(myData);
            findViewById(R.id.main_empty).setVisibility(View.GONE);
            myParcelList.setVisibility(View.VISIBLE);
            myParcelList.setAdapter(adapter);

            init = true;
        } else {
            findViewById(R.id.main_empty).setVisibility(View.VISIBLE);
            myParcelList.setVisibility(View.GONE);
        }
    }

    // 데이터베이스 스캔
    private void initDatabase() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datas.clear();
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()) {
                    DataSnapshot temp = it.next();
                    if (temp.getKey().equals("log")) break;
                    ParcelModel parcel = new ParcelModel();
                    initParcel(temp, parcel);
                }
                onTrackingCodeChange();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        parcels.addListenerForSingleValueEvent(valueEventListener);

        parcels.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String s) {
                if(!ds.getKey().equals("log")&&init) {
                    ParcelModel parcel = new ParcelModel();
                    initParcel(ds, parcel);
                    myData.add(0, parcel);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // 배송정보 내용 등록
    private void initParcel(DataSnapshot temp, ParcelModel parcel) {
        parcel.setParcelKey(temp.getKey());
        parcel.setTitle(temp.child("title").getValue().toString());
        parcel.setWaybill(temp.child("waybill").getValue().toString());
        parcel.setFrom(new Person(temp.child("from").child("name").getValue().toString(), temp.child("from").child("time").getValue().toString()));
        parcel.setTo(new Person(temp.child("to").child("name").getValue().toString(), temp.child("to").child("time").getValue().toString()));
        parcel.setCarrier(
                new Carrier(
                        temp.child("carrier").child("id").getValue().toString(),
                        temp.child("carrier").child("name").getValue().toString(),
                        temp.child("carrier").child("tel").getValue().toString(),
                        temp.child("carrier").child("logo").getValue().toString(),
                        temp.child("carrier").child("homepage").getValue().toString()
                )
        );
        Iterator<DataSnapshot> it2 = temp.child("progresses").getChildren().iterator();
        List<Progress> progresses = new ArrayList<>();
        while (it2.hasNext()) {
            DataSnapshot t = it2.next();
            Progress progress = new Progress();
            progress.setTime(t.child("time").getValue().toString());
            progress.setLocation(t.child("location").getValue().toString());
            progress.setStatus(new State(t.child("status").child("id").getValue().toString(), t.child("status").child("text").getValue().toString()));
            progress.setDescription(t.child("description").getValue().toString());
            progresses.add(progress);
        }
        parcel.setProgresses(progresses);
        parcel.setAlarm(temp.child("alarm").getValue().toString().trim().equals("true"));
        parcel.setState(
                new State(
                        temp.child("state").child("id").getValue().toString(),
                        temp.child("state").child("text").getValue().toString()
                ));
        datas.add(parcel);
    }

}

