package com.zhang.okinglawenforcementphone.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.MenuOV;

import java.util.List;


/**
 * Created by zhao on 2016/11/16.
 */

public class MissionMemberAdapter extends BaseQuickAdapter<GreenMember, BaseViewHolder>{

    public MissionMemberAdapter(int layoutResId, @Nullable List<GreenMember> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, GreenMember item) {

        String signPic = item.getSignPic();
        if (signPic!=null){
            helper.setTextColor(R.id.name_tv,BaseApplication.getApplictaion().getResources().getColor(R.color.btn_addevidence_enable));
            helper.setTextColor(R.id.post_tv,BaseApplication.getApplictaion().getResources().getColor(R.color.btn_addevidence_enable));
        }

        helper.setText(R.id.name_tv,item.getUsername());
        helper.setText(R.id.post_tv,"职位："+item.getPost());

    }


}
