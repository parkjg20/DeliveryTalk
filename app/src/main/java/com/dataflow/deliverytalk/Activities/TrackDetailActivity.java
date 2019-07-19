package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.popup.CarrierTelPopupActivity;
import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.Models.Location;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.Models.Person;
import com.dataflow.deliverytalk.Models.Progress;
import com.dataflow.deliverytalk.Models.State;
import com.dataflow.deliverytalk.Models.TrackDetail;
import com.dataflow.deliverytalk.R;
import com.dataflow.deliverytalk.util.adapters.TrackDetailProgressAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TrackDetailActivity extends AppCompatActivity {
    // in headerlayout....
    private TextView title;
    private ImageButton prevButton;

    // in info...
    private TextView to;
    private TextView from;

    // in state
    private ImageView[] images = new ImageView[5];
    private TextView[] titles = new TextView[5];
    private TextView[] dates = new TextView[5];
    private View[] views = new View[4];

    private ListView progressList;

    // in waybill
    private TextView carrier;
    private TextView waybill;
    private ImageButton callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_detail);
        // 배경색 & status bar 아이콘 색
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        String key = getIntent().getStringExtra("key");
        title = findViewById(R.id.detail_header_text);
        prevButton = findViewById(R.id.detail_prevButton);

        from = findViewById(R.id.detail_info_fromto_from);
        to = findViewById(R.id.detail_info_fromto_to);

        carrier = findViewById(R.id.detail_info_waybill_carrier);
        waybill = findViewById(R.id.detail_info_waybill_waybill);
        callButton = findViewById(R.id.detail_info_waybill_callButton);

        progressList = findViewById(R.id.tracklist_progressList);

        getStateViews();
        setListeners();
        getData(key);

    }
    private void getStateViews(){
        for(int i =0; i< 5;i++){
            images[i] = findViewById(getResources().getIdentifier("detail_info_state_"+(i+1)+"_image", "id", getPackageName()));
            titles[i] = findViewById(getResources().getIdentifier("detail_info_state_"+(i+1)+"_title", "id", getPackageName()));
            dates[i] = findViewById(getResources().getIdentifier("detail_info_state_"+(i+1)+"_date", "id", getPackageName()));
            if(i!=4) views[i] = findViewById(getResources().getIdentifier("detail_info_state_"+(i+1)+"_bar", "id", getPackageName()));
        }
    }

    private void getData(String key){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Parcels").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).child(key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot temp) {
                ParcelModel parcel = new ParcelModel();
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
                    progress.setLocation(new Location(t.child("location").child("name").getValue().toString()));
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

                initViews(parcel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setListeners(){
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackDetailActivity.this, CarrierTelPopupActivity.class);
                intent.putExtra("tel", v.getTag().toString());
                startActivity(intent);
            }
        });
    }

    // 값 초기화
    private void initViews(ParcelModel data) {
        title.setText(data.getTitle());
        from.setText(data.getFrom().getName());
        to.setText(data.getTo().getName());
        carrier.setText(data.getCarrier().getName());
        callButton.setTag(data.getCarrier().getTel());
        waybill.setText(data.getWaybill());

        List<String> dateList = new ArrayList<>();
        List<TrackDetail> trackDetailList = new ArrayList<>();

        int size = data.getProgresses().size();
        Log.d("size", size+"");
        if(size > 0){
            if(size == 1) {
                setState(0, true, (dateConvert(data.getProgresses().get(0).getTime())));
            }
            else {
                boolean move = true;
                boolean arrv = true;
                setState(0, false, (dateConvert(data.getProgresses().get(0).getTime())));

                // 날짜 정보 가져오기
                // 배송정보 형식 만들기
                for(int i = 0; i < data.getProgresses().size(); i++){

                    Progress p = data.getProgresses().get(i);
                    String id = p.getStatus().getId();
                    String desc = p.getDescription();

                    String time = p.getTime();
                    time = time.substring(time.indexOf("T")+1, time.indexOf("T")+6);

                    if(i == 0){
                        TrackDetail tr = new TrackDetail();
                        tr.setDate(dateConvertForDetail(p.getTime()));
                        trackDetailList.add(tr);
                    }else if(!dateConvert(p.getTime()).equals(dateConvert(data.getProgresses().get(i-1).getTime()))){
                        TrackDetail tr = new TrackDetail();
                        tr.setDate(dateConvertForDetail(p.getTime()));
                        trackDetailList.add(tr);
                    }
                    TrackDetail tr = new TrackDetail();
                    tr.setTime(time);
                    tr.setProgress(p);
                    trackDetailList.add(tr);

                    if(id.equals("in_transit")){
                        if(desc.contains("접수")) {
                            dateList.add(dateConvert(p.getTime()));
                        }else if(desc.contains("발송")&&move||desc.contains("이동")&&move||desc.contains("출발")&&move){
                            dateList.add(dateConvert(p.getTime()));
                            move = false;
                        }else if(desc.contains("도착")&&arrv){
                            dateList.add(dateConvert(p.getTime()));
                            arrv = false;
                        }
                    }else{
                        System.out.println("status "+p.getStatus().getId());
                        dateList.add(dateConvert(p.getTime()));
                    }
                }

            }
            // ListAdapter 바인딩
            Log.d("dateList", dateList.toString());

            TrackDetailProgressAdapter adapter = new TrackDetailProgressAdapter(trackDetailList);
            progressList.setAdapter(adapter);

            // View에 받아온 정보 반영하기
            for(int i=1; i<dateList.size()-1; i++){
                setState(i, false, dateList.get(i));
            }
            if(dateList.size() != 0)
            setState(dateList.size()-1, true, dateList.get(dateList.size()-1));
        }
    }

    //date 형식 변환
    private String dateConvert(String d){
        String result = "undefined";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(d);
            result = formatter.format(date).substring(5).replace("-", ".");
        }catch(Exception e) {
            Log.e("detail_dateConvert", e.getMessage());
        }
        return result;
    }

    //date 형식 변환
    private String dateConvertForDetail(String d){
        String result = "undefined";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(d);
            result = new SimpleDateFormat("MM월 dd일 E요일", Locale.KOREAN).format(date.getTime());
        }catch(Exception e) {
            Log.e("detail_dateConvert", e.getMessage());
        }
        return result;
    }

    private void setState(int index, boolean now, String date){

        images[index].setImageDrawable(getDrawable( ((now)?R.drawable.std_ico_step2:R.drawable.std_ico_step1) ));
        titles[index].setTextColor(Color.parseColor("#707070"));
        dates[index].setText(date);
        if(index!=0) views[index-1].setBackgroundColor(Color.parseColor("#07C6AF"));
    }
}
