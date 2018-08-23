package com.zhang.okinglawenforcementphone.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.GlideApp;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;

import java.util.List;

/**
 * Created by Administrator on 2018/6/8/008.
 */

public class TaskLogPicRecyAdapter extends BaseQuickAdapter<GreenMedia, BaseViewHolder> {
    private Activity mActivity;

    public TaskLogPicRecyAdapter(Activity activity, int layoutResId, @Nullable List<GreenMedia> data) {
        super(layoutResId, data);
        this.mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, GreenMedia item) {
        ImageView sdv = helper.getView(R.id.sdv);
        String path = item.getPath();
        GlideApp.with(BaseApplication.getApplictaion())
                .load(path)
                .placeholder(R.mipmap.ic_launcher_logo)
                .error(R.drawable.loadfail)
                .into(sdv);



//        switch (item.getType()) {
//            case 1:
//            case 4:
//                break;
//            case 2:                 //视频
//                break;
//            default:
//                break;
//        }
    }
}
