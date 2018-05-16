package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/4/9.
 */

public class MemberRecyAdapter extends BaseQuickAdapter<GreenMember, BaseViewHolder> {
    private List<GreenMember> checkNames = new ArrayList<GreenMember>();
    private Map<Integer,Boolean> map=new HashMap<>();// 存放已被选中的CheckBox
    public MemberRecyAdapter(int layoutResId, @Nullable List<GreenMember> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, GreenMember item) {

        helper.setText(R.id.tv_name,item.getUsername());
        helper.setOnCheckedChangeListener(R.id.cb, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    map.put(helper.getLayoutPosition(),true);


                } else {
                    map.remove(helper.getLayoutPosition());


                }
            }
        });

        if(map!=null&&map.containsKey(helper.getLayoutPosition())){
            helper.setChecked(R.id.cb,true);
        }else {
            helper.setChecked(R.id.cb,false);
        }

    }

    public List<GreenMember> getCheckName() {
        List<GreenMember> memberArrayList = getData();
        checkNames.clear();
        Iterator<Integer> iter = map.keySet().iterator();

        while (iter.hasNext()) {

            int key = iter.next();

            checkNames.add(memberArrayList.get(key));


        }
        return checkNames;
    }
}
