package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.UserItemOV;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class UserRecyAdaper extends BaseQuickAdapter<UserItemOV, BaseViewHolder> {
    public UserRecyAdaper(int layoutResId, @Nullable List<UserItemOV> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserItemOV item) {
        helper.setText(R.id.tv_title,item.getTitle());
        helper.setImageResource(R.id.iv_icon,item.getIcon());

    }
}
