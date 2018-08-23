package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.NavBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/16/016.
 */

public class NavViewRecyAdapter extends BaseQuickAdapter<NavBean, BaseViewHolder> {
    public NavViewRecyAdapter(int layoutResId, @Nullable List<NavBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavBean item) {
        helper.setImageResource(R.id.iv_icon,item.getIcon());
        helper.setText(R.id.tv_tag,item.getTitle());
    }
}
