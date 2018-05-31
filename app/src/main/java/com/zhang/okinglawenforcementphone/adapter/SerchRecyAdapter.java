package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.SeachBean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/18/018.
 */

public class SerchRecyAdapter extends BaseMultiItemQuickAdapter<SeachBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SerchRecyAdapter(List<SeachBean> data) {
        super(data);
        addItemType(0, R.layout.arrangemission_task_item);
        addItemType(1, R.layout.search_menu_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, SeachBean item) {
        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {
            case 0:
                helper.setText(R.id.tv_taskname,"任务名称："+item.getTaskName());
                helper.setText(R.id.tv_taskid,"任务编号："+item.getTaskId());
                helper.setText(R.id.tv_fbr,"发布人："+item.getPublisherName());



                if ("0".equals(item.getState())) {
                    helper.setText(R.id.tv_state,"任务状态：未发布");
                } else if ("1".equals(item.getState())) {
                    helper.setText(R.id.tv_state,"任务状态：已发布待审核");
                } else if ("2".equals(item.getState())) {
                    helper.setText(R.id.tv_state,"任务状态：审核通过");
                } else if ("3".equals(item.getState())) {
                    helper.setText(R.id.tv_state,"任务状态：已分配队员待执行");
                } else if ("4".equals(item.getState())) {
                    helper.setText(R.id.tv_state,"任务状态：任务开始");
                } else if ("5".equals(item.getState())) {
                    helper.setText(R.id.tv_state,"任务状态：任务完成");
                }else if ("100".equals(item.getState())){
                    helper.setText(R.id.tv_state,"任务状态：巡查结束，待上传");
                }


                break;
            case 1:
                helper.setText(R.id.tv_menuname,"名称："+item.getMenuItme());
                break;
            default:
                    break;

        }
    }
}
