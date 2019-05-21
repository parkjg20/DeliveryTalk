package com.dataflow.deliverytalk.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.popup.CarrierTelPopupActivity;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.Models.Progress;
import com.dataflow.deliverytalk.R;

import org.w3c.dom.Text;

import java.util.List;

public class TrackDetailActivity extends AppCompatActivity {


    // in headerlayout....
    private TextView title;
    private ImageButton prevButton;

    // in info...
    private TextView from;
    private TextView to;

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

        setListeners();
        initViews(data);

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


        List<Progress> progresses = data.getProgresses();
        int size = progresses.size();

        Log.d("progress", "size: "+size);
        if(size < 0){
            return;
        }
        Log.d("progress", "last: "+progresses.get(size-1).getStatus().getId());

    }
}
