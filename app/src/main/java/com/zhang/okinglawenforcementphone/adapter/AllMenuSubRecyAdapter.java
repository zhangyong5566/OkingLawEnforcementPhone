package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.AllMenuItemBean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class AllMenuSubRecyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public AllMenuSubRecyAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_sub, item);

    }
}
