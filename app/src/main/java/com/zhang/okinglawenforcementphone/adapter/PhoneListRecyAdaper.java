package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.Dept;
import com.zhang.okinglawenforcementphone.beans.GreenDept;
import com.zhang.okinglawenforcementphone.beans.UserItemOV;

import java.util.List;

/**
 * Created by Administrator on 2018/5/24/024.
 */

public class PhoneListRecyAdaper extends BaseQuickAdapter<GreenDept, BaseViewHolder> {
    public PhoneListRecyAdaper(int layoutResId, @Nullable List<GreenDept> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GreenDept item) {
        helper.setText(R.id.tv_name,"姓名："+item.getUsername());
        helper.setText(R.id.tv_phone,"电话："+item.getPhone());
        helper.setText(R.id.tv_depat,"部门："+item.getDeptname());
        helper.addOnClickListener(R.id.bt_call);
    }
}
