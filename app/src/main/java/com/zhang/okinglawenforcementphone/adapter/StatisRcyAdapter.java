package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;

import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */

public class StatisRcyAdapter extends BaseQuickAdapter<GreenMissionTask, BaseViewHolder> {
    public StatisRcyAdapter(int layoutResId, @Nullable List<GreenMissionTask> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GreenMissionTask item) {
        helper.setText(R.id.tv_taskname,"任务名称："+item.getTask_name());
        helper.setText(R.id.tv_taskid,"任务编号："+item.getTaskid());
        helper.setText(R.id.tv_fbr,"发布人："+item.getPublisher_name());

        switch (item.getStatus()) {
            case "0":

            case "1":

            case "2":
                helper.setText(R.id.tv_state,"未安排人员");
                break;
            case "3":
                helper.setText(R.id.tv_state,"已安排，待执行");
                break;
            case "4":
                helper.setText(R.id.tv_state,"巡查中");
                break;
            case "100":
                helper.setText(R.id.tv_state,"巡查结束");
                break;
            case "5":
                helper.setText(R.id.tv_state,"已上报");
                break;
            case "9":
                helper.setText(R.id.tv_state,"退回修改");
                break;
            default:
                break;

        }

    }
}
