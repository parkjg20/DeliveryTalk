package com.dataflow.deliverytalk.util.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.TrackModifyActivity;
import com.dataflow.deliverytalk.Models.ParcelModel;
import com.dataflow.deliverytalk.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParcelDeleteListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ParcelModel> listViewItemList;
    private Map<Integer, String> deleteList;
    private boolean flag;
    private Activity activity;


    // ListViewAdapter의 생성자
    public ParcelDeleteListAdapter(List<ParcelModel> datas, Map<Integer, String> deleteList, boolean flag, Activity activity) {
        listViewItemList = (ArrayList) datas;
        this.deleteList = deleteList;
        this.flag = flag;
        this.activity = activity;
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
            convertView = inflater.inflate(R.layout.item_trackmodifylist, null);
        }

        final ConstraintLayout layout = convertView.findViewById(R.id.modifylist_parcel);

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView fromDate = convertView.findViewById(R.id.modifylist_date);
        TextView title = convertView.findViewById(R.id.modifylist_title);
        TextView sender = convertView.findViewById(R.id.modifylist_sender);
        TextView carrier = convertView.findViewById(R.id.modifylist_carrier);
        TextView waybill = convertView.findViewById(R.id.modifylist_waybill);
        TextView status = convertView.findViewById(R.id.modifylist_status);


        // 전체선택, 삭제버튼
        final TextView dt = activity.findViewById(R.id.modify_delText);
        final ImageView di = activity.findViewById(R.id.modify_delImage);

        final ImageView wi = activity.findViewById(R.id.modify_allImage);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ParcelModel parcelInfo = listViewItemList.get(pos);

        // 아이템 내 각 위젯에 데이터 반영
        title.setText(parcelInfo.getTitle());
        if(parcelInfo.getFrom().getName().length() < 1){
            sender.setText("발신자 정보 없음");
        }else{
            sender.setText(parcelInfo.getFrom().getName());
        }
        carrier.setText(parcelInfo.getCarrier().getName());
        waybill.setText(parcelInfo.getWaybill());
        status.setText(parcelInfo.getState().getText());

        String json =
                "{" +
                "   \"flag\": \""+flag+"\"," +
                "   \"position\": \""+pos+"\"," +
                "   \"key\": \""+parcelInfo.getParcelKey()+"\"" +
                "}";
        layout.setTag(json);
        if(flag){
            layout.setBackgroundColor(Color.parseColor("#EFEFEF"));
        }else{
            layout.setBackgroundColor(Color.parseColor((deleteList.containsKey(pos))?"#EFEFEF":"#FFFFFF"));
        }
        buttonStateChange(dt, di, wi);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonParser jp = new JsonParser();
                JsonObject json = (JsonObject) jp.parse(v.getTag().toString());
                boolean selected = json.getAsJsonPrimitive("flag").getAsBoolean();
                int position = json.getAsJsonPrimitive("position").getAsInt();
                String key = json.getAsJsonPrimitive("key").getAsString();


                if(!selected){
                    selected = true;
                    v.setBackgroundColor(Color.parseColor("#EFEFEF"));
                    deleteList.put(position, key);
                }else{
                    selected = false;
                    v.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    deleteList.remove(position);
                }

                buttonStateChange(dt, di, wi);
                String ret =
                        "{" +
                        "   \"flag\": \""+selected+"\"," +
                        "   \"position\": \""+pos+"\"," +
                        "   \"key\": \""+key+"\"" +
                        "}";
                v.setTag(ret);
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

    // 버튼 상태 변경
    public void buttonStateChange(TextView dt, ImageView di, ImageView wi){
        if(deleteList.size() > 0){
            dt.setTextColor(Color.parseColor("#00C5AA"));
            di.setImageDrawable(activity.getDrawable(R.drawable.st_edit_btn_del_p));
            if(flag&&deleteList.size()==listViewItemList.size()){
                wi.setImageDrawable(activity.getDrawable(R.drawable.st_edit_btn_check_p));
            }else{
                wi.setImageDrawable(activity.getDrawable(R.drawable.st_edit_btn_check_n));
            }
        }else{
            dt.setTextColor(Color.parseColor("#707070"));
            di.setImageDrawable(activity.getDrawable(R.drawable.st_edit_btn_del_n));
            wi.setImageDrawable(activity.getDrawable(R.drawable.st_edit_btn_check_n));
        }
    }


    public void deleteItem(int position){
        listViewItemList.remove(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem() {
//        listViewItemList.add();
    }
}