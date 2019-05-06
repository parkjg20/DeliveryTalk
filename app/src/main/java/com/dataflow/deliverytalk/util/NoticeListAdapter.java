package com.dataflow.deliverytalk.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dataflow.deliverytalk.Activities.ETCNoticeActivity;
import com.dataflow.deliverytalk.Activities.MainActivity;
import com.dataflow.deliverytalk.Models.NoticeModel;
import com.dataflow.deliverytalk.R;

import java.util.ArrayList;

public class NoticeListAdapter extends BaseExpandableListAdapter {

    private Context c;
    private ArrayList<NoticeModel> groupDatas;
    private LayoutInflater inflater;

    public NoticeListAdapter(Context c, ArrayList<NoticeModel> groupData)
    {
        this.c = c;
        groupDatas = groupData;
        inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    /* Child List Data */

    @Override // groupPostion과 childPosition을 통해 childList의 원소를 얻어옴
    public Object getChild(int groupPos, int childPos) {
        return groupDatas.get(groupPos).getContent();
    }

    @Override // ChildList의 ID로 long 형 값을 반환
    public long getChildId(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override // ChildList의 View. Layout 연결 후, layout 내 데이터를 연결
    public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView, ViewGroup parent)
    {
        //ONLY INFLATER XML ROW LAYOUT IF ITS NOT PRESENT,OTHERWISE REUSE IT
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.elv_child, null);
        }

        //GET CHILD
        String  child= (String) getChild(groupPos, childPos);

        //SET CHILD NAME
        TextView content =(TextView) convertView.findViewById(R.id.tv_child);

        // 데이터 set
        content.setText(child);

        return convertView;
    }

    @Override // ChildList의 크기를 int 형으로 반환
    public int getChildrenCount(int groupPosw) {
        // TODO Auto-generated method stub
        return 1;
    }

    /* Paren tList View */

    @Override  // groupDatas의 position을 받아 해당 view에 반영될 String을 반환
    public Object getGroup(int groupPos) {
        // TODO Auto-generated method stub
        return groupDatas.get(groupPos);
    }

    @Override // groupDatas의 원소 개수를 반환
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groupDatas.size();
    }

    //GET TEAM ID
    @Override // ParentList의 position을 받아 long값으로 반환
    public long getGroupId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    //GET TEAM ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        //ONLY INFLATE XML TEAM ROW MODEL IF ITS NOT PRESENT,OTHERWISE REUSE IT
        if(convertView == null)
        {
            convertView=inflater.inflate(R.layout.elv_group, null);
        }



        //GET GROUP
        NoticeModel t=(NoticeModel) getGroup(groupPosition);

        // DATA SET
        TextView title = convertView.findViewById(R.id.tv_group);
        TextView wdate = convertView.findViewById(R.id.tv_group_wdate);

        title.setText(t.getTitle());
        wdate.setText(t.getWdate());

        ImageView image = convertView.findViewById(R.id.iv_image);

        if(isExpanded){
            image.setImageDrawable(c.getDrawable(R.drawable.etc_notice_btn_u));
        }else{
            image.setImageDrawable(c.getDrawable(R.drawable.etc_notice_btn_d));
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override // ChildView 선택 가능 여부
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        // false : 선택 불가능
        //  true : 선택 가능
        return false;
    }

}