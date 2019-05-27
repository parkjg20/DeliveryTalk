package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.popup.CarrierTelPopupActivity;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.Models.Progress;
import com.dataflow.deliverytalk.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        ParcelModel data = getIntent().getParcelableExtra("data");
        title = findViewById(R.id.detail_header_text);
        prevButton = findViewById(R.id.detail_prevButton);

        from = findViewById(R.id.detail_info_fromto_from);
        to = findViewById(R.id.detail_info_fromto_to);

        carrier = findViewById(R.id.detail_info_waybill_carrier);
        waybill = findViewById(R.id.detail_info_waybill_waybill);
        callButton = findViewById(R.id.detail_info_waybill_callButton);



        getStateViews();
        setListeners();
        initViews(data);

    }
    private void getStateViews(){
        for(int i =0; i< 5;i++){
            images[i] = findViewById(getResources().getIdentifier("detail_info_state_"+(i+1)+"_image", "id", getPackageName()));
            titles[i] = findViewById(getResources().getIdentifier("detail_info_state_"+(i+1)+"_title", "id", getPackageName()));
            dates[i] = findViewById(getResources().getIdentifier("detail_info_state_"+(i+1)+"_date", "id", getPackageName()));
            if(i!=4) views[i] = findViewById(getResources().getIdentifier("detail_info_state_"+(i+1)+"_bar", "id", getPackageName()));
        }
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
        int size = data.getProgresses().size();
        if(size > 0){
            if(size == 1) {
                setState(0, true, (dateConvert(data.getProgresses().get(0).getTime())));
            }
            else {
                boolean move = true;
                boolean arrv = true;
                setState(0, false, (dateConvert(data.getProgresses().get(0).getTime())));

                // 날짜 정보 가져오기
                for(Progress p : data.getProgresses()){
                    String id = p.getStatus().getId();
                    String desc = p.getDescription();

                    if(id.equals("in_transit")){
                        if(desc.equals("발송")&&move){
                            dateList.add(dateConvert(p.getTime()));
                            move = false;
                        }else if(desc.equals("도착")&&arrv){
                            dateList.add(dateConvert(p.getTime()));
                            arrv = false;
                        }
                    }else{
                        dateList.add(dateConvert(p.getTime()));
                    }
                }
            }
            // View에 받아온 정보 반영하기
            Log.d("detail_size", dateList.size()+"");
            for(int i=1; i<dateList.size()-1; i++){
                setState(i, false, dateList.get(i));
            }
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

    private void setState(int index, boolean now, String date){

        images[index].setImageDrawable(getDrawable( ((now)?R.drawable.std_ico_step2:R.drawable.std_ico_step1) ));
        titles[index].setTextColor(Color.parseColor("#707070"));
        dates[index].setText(date);
        if(index!=0) views[index-1].setBackgroundColor(Color.parseColor("#07C6AF"));
    }
}
