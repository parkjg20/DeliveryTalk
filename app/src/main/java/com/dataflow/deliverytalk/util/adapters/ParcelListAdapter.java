package com.dataflow.deliverytalk.util.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.R;

import java.util.ArrayList;
import java.util.List;

public class ParcelListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ParcelModel> listViewItemList;

    // ListViewAdapter의 생성자
    public ParcelListAdapter(List<ParcelModel> datas) {
        listViewItemList = (ArrayList) datas;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_tracklist, null);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView fromDate = convertView.findViewById(R.id.tracklist_date);
        TextView title = convertView.findViewById(R.id.tracklist_title);
        TextView sender = convertView.findViewById(R.id.tracklist_sender);
        TextView carrier = convertView.findViewById(R.id.tracklist_carrier);
        TextView waybill = convertView.findViewById(R.id.tracklist_waybill);
        CheckBox alarm = convertView.findViewById(R.id.tracklist_alarm);
        TextView status = convertView.findViewById(R.id.tracklist_status);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ParcelModel parcelInfo = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        title.setText(parcelInfo.getTitle());
        if(parcelInfo.getFrom().getName().length() < 1){
            sender.setText("발신자 정보 없음");
        }else{
            sender.setText(parcelInfo.getFrom().getName());
        }
        carrier.setText(parcelInfo.getCarrier().getName());
        waybill.setText(parcelInfo.getWaybill());
        alarm.setChecked(parcelInfo.isAlarm());
        status.setText(parcelInfo.getState().getText());

        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem() {
//        listViewItemList.add();
    }
}