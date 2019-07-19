package com.dataflow.deliverytalk.util.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.popup.CarrierTelPopupActivity;
import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.Models.Progress;
import com.dataflow.deliverytalk.Models.TrackDetail;
import com.dataflow.deliverytalk.R;

import java.util.ArrayList;
import java.util.List;

public class TrackDetailProgressAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private List<TrackDetail> listViewItemList;

    // ListViewAdapter의 생성자
    public TrackDetailProgressAdapter(List<TrackDetail> datas) {
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

        TrackDetail trackDetail = listViewItemList.get(pos);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if(trackDetail.getDate() != null ){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_progress_date, null);

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            TextView textView = convertView.findViewById(R.id.progress_message_date);
            textView.setText(trackDetail.getDate());
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_progress_message, null);

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            TextView textView = convertView.findViewById(R.id.progress_message_text);

            String detailText = trackDetail.getTime()+"   ["+trackDetail.getProgress().getLocation().getName().trim()+"] "+trackDetail.getProgress().getDescription();
            textView.setText(detailText);
        }



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