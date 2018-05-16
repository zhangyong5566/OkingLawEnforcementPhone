package com.zhang.okinglawenforcementphone.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.AllMenuItemBean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class AllMenuRecyAdapter extends BaseQuickAdapter<AllMenuItemBean, BaseViewHolder> {
    private OnItemClickListener mOnItemClickListener;

    public AllMenuRecyAdapter(int layoutResId, @Nullable List<AllMenuItemBean> data, OnItemClickListener onItemClickListener) {
        super(layoutResId, data);
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, AllMenuItemBean item) {

        helper.setText(R.id.tv_title, item.getTitle());
        RecyclerView rcyAllMenuSub = helper.getView(R.id.rcy_all_menu_sub);
        rcyAllMenuSub.setLayoutManager(new GridLayoutManager(BaseApplication.getApplictaion(), 4));
        AllMenuSubRecyAdapter allMenuSubRecyAdapter = new AllMenuSubRecyAdapter(R.layout.all_menu_sub, item.getSubList());
        rcyAllMenuSub.setAdapter(allMenuSubRecyAdapter);
        allMenuSubRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mOnItemClickListener.setOnItemClickListener(adapter, view, helper.getLayoutPosition(), position);
            }
        });

    }

    public interface OnItemClickListener {
        void setOnItemClickListener(BaseQuickAdapter adapter, View view, int groupPosition, int chilPosition);
    }
}
