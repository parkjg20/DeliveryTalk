package com.dataflow.deliverytalk.util.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.ETCCarriersActivity;
import com.dataflow.deliverytalk.Activities.popup.CarrierTelPopupActivity;
import com.dataflow.deliverytalk.Models.Carrier;
import com.dataflow.deliverytalk.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarrierListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private List<Carrier> listViewItemList;

    // ListViewAdapter의 생성자
    public CarrierListAdapter(List<Carrier> datas) {
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
            convertView = inflater.inflate(R.layout.item_carriers, null);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView logo = convertView.findViewById(R.id.item_carriers_logo);
        TextView name = convertView.findViewById(R.id.item_carriers_name);
        ImageButton callButton = convertView.findViewById(R.id.item_carriers_tel);
        ImageButton homeButton = convertView.findViewById(R.id.item_carriers_homepage);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Carrier carrier = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        logo.setImageDrawable(context.getDrawable(context.getResources().getIdentifier(carrier.getLogo(), "drawable", context.getPackageName())));
        name.setText(carrier.getName());
        callButton.setTag(carrier.getTel());
        homeButton.setTag(carrier.getHomepage());
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context , CarrierTelPopupActivity.class);
                intent.putExtra("tel", v.getTag().toString());
                context.startActivity(intent);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = v.getTag().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
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