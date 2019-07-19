package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.popup.EventDialogPopup;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.adapters.ParcelDeleteListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackModifyActivity extends AppCompatActivity {

    private Intent intent = new Intent();
    private Button doneButton;
    private ConstraintLayout delete;
    private ImageView deleteImage;
    private TextView deleteText;

    private ConstraintLayout whole;
    private ImageView wholeImage;
    private TextView wholeText;

    protected List<ParcelModel> datas;
    static protected Map<Integer, String> deleteList = new HashMap<>();
    private ListView parcelList;

    ParcelDeleteListAdapter adapter;
    boolean doubleClick = false;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Parcels").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_modify);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        doneButton = findViewById(R.id.modify_header_doneButton);
        parcelList = findViewById(R.id.modify_listView);

        delete = findViewById(R.id.modify_del);
        deleteImage = findViewById(R.id.modify_delImage);
        deleteText = findViewById(R.id.modify_delText);

        whole = findViewById(R.id.modify_all);
        wholeImage = findViewById(R.id.modify_allImage);
        wholeText = findViewById(R.id.mmodify_allText);

        datas = getIntent().getParcelableArrayListExtra("myData");

        addAdapter(false);
        addListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(2, intent);
        finish();
    }



    private void addListeners(){
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(3, intent);
                deleteList.clear();
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doubleClick) {
                    doubleClick = true;
                    final List<Integer> list = new ArrayList<>();
                    for (final Integer key : deleteList.keySet()) {
                        list.add(key);
                    }
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = i + 1; j < list.size(); j++) {
                            if (list.get(i) < list.get(j)) {
                                int temp = list.get(j);
                                list.set(j, list.get(i));
                                list.set(i, temp);
                            }
                        }
                    }
                    for (final Integer key : list) {
                        ref.child(deleteList.get(key)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    adapter.deleteItem(key);
                                    deleteList.remove(key);

                                } else {
                                    Intent intent = new Intent(TrackModifyActivity.this, EventDialogPopup.class);
                                    intent.putExtra("title", "[error 2]");
                                    intent.putExtra("content", "택배 삭제 도중 문제가 발생했습니다.");
                                    startActivity(intent);
                                }
                                parcelList.invalidateViews();
                                addAdapter(false);
                                doubleClick = false;
                            }
                        });
                    }
                }
            }
        });

        whole.setTag(false);
        whole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!doubleClick){
                    doubleClick = true;
                    if(!(boolean)v.getTag()){
                        v.setTag(true);
                        for(int i = 0; i < datas.size(); i++){
                            deleteList.put(i, datas.get(i).getParcelKey());
                        }
                        addAdapter(true);
                    }else{
                        v.setTag(false);
                        deleteList.clear();
                        addAdapter(false);
                    }

                    doubleClick = false;

                }
            }
        });
    }

    private void addAdapter(boolean flag){
        adapter = new ParcelDeleteListAdapter(datas, deleteList, flag, TrackModifyActivity.this);
        adapter.notifyDataSetChanged();
        parcelList.setAdapter(adapter);
    }

}
