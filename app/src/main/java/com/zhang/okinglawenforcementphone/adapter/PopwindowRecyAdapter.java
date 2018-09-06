package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;

import java.util.List;

/**
 * Created by Administrator on 2018/8/31/031.
 */

public class PopwindowRecyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PopwindowRecyAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_title,item);
        helper.addOnClickListener(R.id.tv_title);
    }
}
