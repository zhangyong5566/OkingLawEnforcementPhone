package com.zhang.okinglawenforcementphone.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.MenuItemOV;
import com.zhang.okinglawenforcementphone.beans.MenuOV;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class IndexRecyAdapter extends BaseQuickAdapter<MenuOV, BaseViewHolder> {

    private OnTagClickListener mOnTagClickListener;

    public IndexRecyAdapter(int layoutResId, @Nullable List<MenuOV> data, OnTagClickListener onTagClickListener) {
        super(layoutResId, data);
        this.mOnTagClickListener = onTagClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MenuOV item) {
        helper.setText(R.id.tv_title, item.getTage());
        TagFlowLayout flowlayout = helper.getView(R.id.flowlayout);
        final List<MenuItemOV> menuItemOVS = item.getMenuItemOVS();
        flowlayout.setAdapter(new TagAdapter<MenuItemOV>(menuItemOVS) {
            @Override
            public View getView(FlowLayout parent, int position, MenuItemOV dataBean) {
                View inflate = View.inflate(BaseApplication.getApplictaion(), R.layout.hot_tagflow_item, null);
                TextView tv_tag = inflate.findViewById(R.id.tv_tag);
                if (dataBean.getTitle().equals("待办")) {
                    TextView tv_msgcout = inflate.findViewById(R.id.tv_msgcout);
                    mOnTagClickListener.setMessageCount(tv_msgcout);
                }

                ImageView icon = inflate.findViewById(R.id.iv_icon);
                tv_tag.setText(dataBean.getTitle());
                icon.setImageResource(dataBean.getIcon());
                return inflate;
            }
        });

        flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                mOnTagClickListener.onTagClickListener(menuItemOVS.get(position));
                return false;
            }
        });
    }


    public interface OnTagClickListener {
        void onTagClickListener(MenuItemOV bean);

        void setMessageCount(TextView textView);
    }
}
