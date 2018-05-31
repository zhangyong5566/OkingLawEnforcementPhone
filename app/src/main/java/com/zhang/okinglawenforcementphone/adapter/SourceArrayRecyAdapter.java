package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.SourceArrayOV;

import java.util.List;

/**
 * Created by Administrator on 2018/5/25/025.
 */

public class SourceArrayRecyAdapter extends BaseQuickAdapter<SourceArrayOV, BaseViewHolder> {
    public SourceArrayRecyAdapter(int layoutResId, @Nullable List<SourceArrayOV> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SourceArrayOV item) {
        helper.setText(R.id.tv_sub, item.getSource());
    }
}
