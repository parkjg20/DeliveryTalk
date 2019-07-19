package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.adapters.CarrierSelectListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectCarrierActivity extends AppCompatActivity {

    private Intent intent = new Intent();
    private ImageButton prevButton;

    private FirebaseFirestore db;
    private FirebaseFirestoreSettings settings;

    private List<Carrier> datas = new ArrayList<>();
    private ListView listview;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        intent.putExtra("code", "");
//        intent.putExtra("carrierName", "");
        setResult(2, intent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_carrier);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#0DCCB5"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        db = FirebaseFirestore.getInstance(FirebaseApp.getInstance("[DEFAULT]"));
        settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        prevButton = findViewById(R.id.select_carrier_prevButton);
        listview = findViewById(R.id.select_carrier_listview);

        setListeners();
        initDatabase();
    }

    private void initDatabase() {
        db.collection("Carriers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot t : task.getResult()) {
                                Carrier carrier = new Carrier(
                                        t.getId(),
                                        t.getData().get("name").toString(),
                                        t.getData().get("tel").toString(),
                                        t.getData().get("logo").toString(),
                                        t.getData().get("homepage").toString()
                                );
                                datas.add(carrier);
                            }
                            listViewBind();
                        }
                    }
                });
    }

    private void listViewBind() {
        CarrierSelectListAdapter adapter = new CarrierSelectListAdapter(datas, SelectCarrierActivity.this);
        listview.setAdapter(adapter);
    }


    private void setListeners() {
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}

