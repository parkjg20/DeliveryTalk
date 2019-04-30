package com.dataflow.deliverytalk.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dataflow.deliverytalk.R;

import java.util.ArrayList;

/**
 * Created by ium on 14. 2. 26.
 */
public class BaseExpandableAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<String>> groupList = null;
    private ArrayList<String> childList = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;

    public BaseExpandableAdapter(Context c, ArrayList<ArrayList<String>> groupList, ArrayList<String> childList){

        super();
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public ArrayList<String> getGroup(int groupPosition){
        return groupList.get(groupPosition);
    }

    @Override
    public int getGroupCount(){
        return groupList.size();
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.elv_group, parent, false);
            viewHolder.tv_groupName = (TextView)v.findViewById(R.id.tv_group);
            viewHolder.tv_groupWdate = (TextView) v.findViewById(R.id.tv_group_wdate);
            viewHolder.iv_image = (ImageView)v.findViewById(R.id.iv_image);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.tv_groupName.setText(getGroup(groupPosition).get(0));
        viewHolder.tv_groupWdate.setText(getGroup(groupPosition).get(1));
        return v;
    }

    @Override
    public String getChild(int groupPosition, int childPosition){
        return childList.get(groupPosition);
    }

    @Override
    public int getChildrenCount(int groupPosition){
        return childList.size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition){
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent){
        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.elv_group, null);
            viewHolder.tv_childName = (TextView) v.findViewById(R.id.tv_child);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.tv_childName.setText(getChild(groupPosition, childPosition));

        return v;
    }

    @Override
    public boolean hasStableIds(){return true;}

    @Override
    public boolean isChildSelectable(int groupPostion, int childPosition){return true;}


    class ViewHolder{
        public ImageView iv_image;
        public TextView tv_groupName;
        public TextView tv_groupWdate;
        public TextView tv_childName;
    }
}
