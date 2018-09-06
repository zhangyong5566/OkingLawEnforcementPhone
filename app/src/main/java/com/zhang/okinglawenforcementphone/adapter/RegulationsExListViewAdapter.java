package com.zhang.okinglawenforcementphone.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.ChapterDomain;
import com.zhang.okinglawenforcementphone.beans.LawsRegulation;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/10/30.
 */

public class RegulationsExListViewAdapter extends BaseExpandableListAdapter {
    private ArrayList<LawsRegulation>  lawChapter;

    public RegulationsExListViewAdapter(ArrayList<LawsRegulation>  lawChapter) {
        this.lawChapter = lawChapter;
    }

    public void setDataList(ArrayList dataList) {

    }

    @Override
    public int getGroupCount() {
        return lawChapter.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return lawChapter.get(i).getChapterDirectory().get(i).getSection().size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHold groupHold;
        if (convertView==null){
            groupHold = new GroupHold();
            convertView = View.inflate(BaseApplication.getApplictaion(), R.layout.gegulation_group_item,null);
            groupHold.title = convertView.findViewById(R.id.tv_title);
            groupHold.ivGoToChildLv = convertView.findViewById(R.id.iv_goToChildLV);
            convertView.setTag(groupHold);
        }

        groupHold = (GroupHold) convertView.getTag();
        ChapterDomain chapterDomain = lawChapter.get(groupPosition).getChapterDirectory().get(groupPosition);
        groupHold.title.setText(chapterDomain.getChapterDirectory());
        //取消默认的groupIndicator后根据方法中传入的isExpand判断组是否展开并动态自定义指示器
        if (isExpanded) {   //如果组展开
            groupHold.ivGoToChildLv.setImageResource(R.mipmap.arrow_down);
        } else {
            groupHold.ivGoToChildLv.setImageResource(R.mipmap.arrow_right);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ChildHold childHold;
        if (convertView==null){
            childHold = new ChildHold();
            convertView = View.inflate(BaseApplication.getApplictaion(),R.layout.gegylation_child_item,null);
            childHold.itemTitle = convertView.findViewById(R.id.tv_title);
            convertView.setTag(childHold);
        }
        childHold = (ChildHold) convertView.getTag();
        String itemTitle = lawChapter.get(groupPosition).getChapterDirectory().get(groupPosition).getSection().get(childPosition).getItemTitle();
        childHold.itemTitle.setText(itemTitle);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHold {
        TextView title;
        ImageView ivGoToChildLv;
    }

    class ChildHold {
        TextView itemTitle;
    }
}
