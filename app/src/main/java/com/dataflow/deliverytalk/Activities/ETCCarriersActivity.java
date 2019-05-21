package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.dataflow.deliverytalk.Activities.popup.CarrierTelPopupActivity;
import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.adapters.CarrierListAdapter;
import com.dataflow.deliverytalk.util.adapters.ParcelListAdapter;
import com.dataflow.deliverytalk.util.retrofit.ParcelTrackingRetrofit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ETCCarriersActivity extends AppCompatActivity {

    private final String databaseUrl = "https://deliverytalk-31595.firebaseio.com";
    private ListView listview;

    private List<Carrier> carriers;
    private FirebaseFirestore db;
    private FirebaseFirestoreSettings settings;

    private ImageButton prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_carriers);
        db = FirebaseFirestore.getInstance(FirebaseApp.getInstance("[DEFAULT]"));
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        listview = findViewById(R.id.carriers_listView);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        prevButton = findViewById(R.id.carriers_prevButton);

        setListeners();
        initDatabase();
    }

    private void setListeners(){
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initDatabase(){
        carriers = new ArrayList<>();
        db.collection("Carriers")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot t : task.getResult()){
                        Carrier carrier = new Carrier(
                                        t.getId(),
                                        t.getData().get("name").toString(),
                                        t.getData().get("tel").toString(),
                                        t.getData().get("logo").toString(),
                                        t.getData().get("homepage").toString()
                        );
                        carriers.add(carrier);
                    }
                    listViewBind();
                }
            }
        });
    }

    private void listViewBind(){
        CarrierListAdapter adapter = new CarrierListAdapter(carriers);
        listview.setAdapter(adapter);
    }

}
