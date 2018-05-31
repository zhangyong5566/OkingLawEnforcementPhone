package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.MapTaskInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/21/021.
 */

public class MapTaskRecyAdapter extends BaseQuickAdapter<MapTaskInfo, BaseViewHolder> {
    public MapTaskRecyAdapter(int layoutResId, @Nullable List<MapTaskInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MapTaskInfo item) {
            helper.setText(R.id.tv_title,item.getTaskName());

    }
}
