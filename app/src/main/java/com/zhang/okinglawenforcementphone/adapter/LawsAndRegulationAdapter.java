package com.zhang.okinglawenforcementphone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.LawBean;

import java.util.List;


/**
 * Created by Administrator on 2017/10/27.
 */

public class LawsAndRegulationAdapter extends BaseQuickAdapter<LawBean, BaseViewHolder> {

    public LawsAndRegulationAdapter(int layoutResId, @Nullable List<LawBean> data) {
        super(layoutResId, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, LawBean item) {
        helper.setText(R.id.tv_title, Html.fromHtml("<font color=\"#98CF60\">标题：</font>"+item.getTitle()));
        helper.setText(R.id.tv_level_effectiveness,Html.fromHtml("<font color=\"#98CF60\">效力级别：</font>"+item.getLevelEffectiveness()));
        helper.setText(R.id.tv_publishing_department,Html.fromHtml("<font color=\"#98CF60\">发布部门：</font>"+item.getPublishingDepartment()));
        helper.setText(R.id.tv_release_time,Html.fromHtml("<font color=\"#98CF60\">发布时间：</font>"+item.getReleaseTime()));
        helper.setText(R.id.tv_implementation_time,Html.fromHtml("<font color=\"#98CF60\">实施时间：</font>"+item.getImplementationTime()));

    }

}
