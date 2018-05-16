package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by Administrator on 2018/4/8.
 */

public class ArrangeMissionRcyAdapter extends BaseQuickAdapter<GreenMissionTask, BaseViewHolder> {
    public ArrangeMissionRcyAdapter(int layoutResId, @Nullable List<GreenMissionTask> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GreenMissionTask item) {

        helper.setText(R.id.tv_taskname,"任务名称："+item.getTask_name());
        helper.setText(R.id.tv_taskid,"任务编号："+item.getTaskid());
        helper.setText(R.id.tv_fbr,"发布人："+item.getPublisher_name());



        if ("0".equals(item.getStatus())) {
            helper.setText(R.id.tv_state,"任务状态：未发布");
        } else if ("1".equals(item.getStatus())) {
            helper.setText(R.id.tv_state,"任务状态：已发布待审核");
        } else if ("2".equals(item.getStatus())) {
            helper.setText(R.id.tv_state,"任务状态：审核通过");
        } else if ("3".equals(item.getStatus())) {
            helper.setText(R.id.tv_state,"任务状态：已分配队员待执行");
        } else if ("4".equals(item.getStatus())) {
            helper.setText(R.id.tv_state,"任务状态：任务开始");
        } else if ("5".equals(item.getStatus())) {
            helper.setText(R.id.tv_state,"任务状态：任务完成");
        }else if ("100".equals(item.getStatus())){
            helper.setText(R.id.tv_state,"任务状态：巡查结束，待上传");
        }


    }
}
