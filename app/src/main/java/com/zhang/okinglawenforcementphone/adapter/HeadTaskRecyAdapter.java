package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;

import java.util.List;

/**
 * Created by Administrator on 2018/8/27/027.
 */

public class HeadTaskRecyAdapter extends BaseQuickAdapter<GreenMissionTask, BaseViewHolder> {
    public HeadTaskRecyAdapter(int layoutResId, @Nullable List<GreenMissionTask> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GreenMissionTask item) {
        if (item.getExamine_status() == -1) {

            if ("0".equals(item.getStatus())) {

                helper.setText(R.id.tv_taskname,Html.fromHtml("<font color=\"#98CF60\">未发布：</font>"+item.getTask_name()));
            } else if ("1".equals(item.getStatus())) {
                helper.setText(R.id.tv_taskname,Html.fromHtml("<font color=\"#98CF60\">待审核：</font>"+item.getTask_name()));
            } else if ("2".equals(item.getStatus())) {
                helper.setText(R.id.tv_taskname,Html.fromHtml("<font color=\"#98CF60\">待分配队员：</font>"+item.getTask_name()));
            } else if ("3".equals(item.getStatus())) {
                helper.setText(R.id.tv_taskname,Html.fromHtml("<font color=\"#98CF60\">待执行：</font>"+item.getTask_name()));
            } else if ("4".equals(item.getStatus())) {
                helper.setText(R.id.tv_taskname,Html.fromHtml("<font color=\"#98CF60\">任务开始：</font>"+item.getTask_name()));
            } else if ("5".equals(item.getStatus())) {
                helper.setText(R.id.tv_taskname,Html.fromHtml("<font color=\"#98CF60\">任务完成：</font>"+item.getTask_name()));
            } else if ("7".equals(item.getStatus())) {
                helper.setText(R.id.tv_taskname,Html.fromHtml("<font color=\"#98CF60\">审核不通过：</font>"+item.getTask_name()));
            } else if ("100".equals(item.getStatus())) {
                helper.setText(R.id.tv_taskname,Html.fromHtml("<font color=\"#98CF60\">待上传：</font>"+item.getTask_name()));
            }

        } else {
            helper.setText(R.id.tv_taskname,Html.fromHtml("<font color=\"#98CF60\">等待领导批示：</font>"+item.getTask_name()));

        }

    }
}
