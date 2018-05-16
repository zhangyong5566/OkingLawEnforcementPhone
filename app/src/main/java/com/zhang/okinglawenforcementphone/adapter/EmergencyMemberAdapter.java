package com.zhang.okinglawenforcementphone.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/10/9.
 */

public class EmergencyMemberAdapter extends BaseAdapter {
    private ArrayList<GreenMember> emergencyMembers;
    private Activity mActivity;
    private List<GreenMember> checkNames = new ArrayList<GreenMember>();
    private Map<Integer,Boolean> map=new HashMap<>();// 存放已被选中的CheckBox
    public EmergencyMemberAdapter(Activity mActivity, ArrayList<GreenMember> emergencyMembers) {
        this.mActivity = mActivity;
        this.emergencyMembers = emergencyMembers;
    }

    @Override
    public int getCount() {
        return emergencyMembers.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.can_select_item, null);
            holder.name_tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GreenMember emergencyMember = emergencyMembers.get(position);
        if (!(emergencyMember == null)) {
            holder.name_tv.setText(emergencyMember.getUsername());
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        map.put(position,true);


                    } else {
                        map.remove(position);


                    }

                }
            });


            if(map!=null&&map.containsKey(position)){
                holder.cb.setChecked(true);
            }else {
                holder.cb.setChecked(false);
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView name_tv;
        CheckBox cb;
    }

    public List<GreenMember> getCheckName() {

        checkNames.clear();
        Iterator<Integer> iter = map.keySet().iterator();

        while (iter.hasNext()) {

            int key = iter.next();

            checkNames.add(emergencyMembers.get(key));


        }
        return checkNames;
    }
}
