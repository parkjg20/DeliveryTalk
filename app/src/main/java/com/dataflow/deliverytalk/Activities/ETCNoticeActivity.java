package com.dataflow.deliverytalk.Activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ExpandableListView;

import com.dataflow.deliverytalk.Models.NoticeModel;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.NoticeListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ETCNoticeActivity extends Activity {

    DatabaseReference ref;
    private ArrayList<NoticeModel> mGroupList;
    private ArrayList<String> mChildList;
    private ExpandableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etc_notice);

        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        mGroupList = new ArrayList<NoticeModel>();
        mChildList = new ArrayList<String>();

        ref = FirebaseDatabase.getInstance("https://deliverytalk-31595.firebaseio.com").getReference("Notice");
        initDatabase();

    }

    private void initViews(){
        ref.child("log").setValue("check");

        mListView = findViewById(R.id.notice_notices);
        mListView.setAdapter(new NoticeListAdapter(ETCNoticeActivity.this, mGroupList));
    }

    // 데이터베이스 초기화
    private void initDatabase(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(int i = 1; i < dataSnapshot.getChildrenCount(); i++){
                    NoticeModel nModel = new NoticeModel();
                    nModel.setTitle(dataSnapshot.child("" + i).child("title").getValue().toString());
                    nModel.setWdate(dataSnapshot.child(""+i).child("wdate").getValue().toString());
                    nModel.setContent(dataSnapshot.child(""+i).child("content").getValue().toString());
                    mGroupList.add(nModel);
                }
                System.out.println(mGroupList.toString());

                initViews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }

}