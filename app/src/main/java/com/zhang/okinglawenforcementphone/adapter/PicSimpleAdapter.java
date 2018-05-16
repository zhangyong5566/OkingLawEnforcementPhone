package com.zhang.okinglawenforcementphone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.GreenDAOMannager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenLocation;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.MenuOV;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ImageViewActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhao on 2016/10/9.
 */

public class PicSimpleAdapter extends BaseQuickAdapter<GreenMedia, BaseViewHolder> {

    private OnClickListener onClickListener;
    private Activity activity;
    private boolean canAdd;
    private String typeName;

    public PicSimpleAdapter(int layoutResId, @Nullable List<GreenMedia> data, Activity recorActivity, boolean canAdd, String typeName) {
        super(layoutResId, data);
        this.activity = recorActivity;
        this.canAdd = canAdd;
        this.typeName = typeName;
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final GreenMedia item) {
        SimpleDraweeView sdv = helper.getView(R.id.sdv);
        final int layoutPosition = helper.getLayoutPosition();
            helper.setVisible(R.id.tv, true);
            final Uri uri = Uri.parse(item.getPath());

            sdv.setImageURI(uri.toString());
            sdv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onClickListener != null && canAdd) {
                        onClickListener.onLongItemClick(PicSimpleAdapter.this, item, layoutPosition);
                    }
                    return false;
                }
            });
            sdv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ImageViewActivity.class);
                    GreenLocation location = item.getLocation();
                    if (location!=null){
                        intent.putExtra("picLocation",location.getLongitude()+","+location.getLatitude());
                    }
                    intent.setData(uri);
                    activity.startActivity(intent);
                }
            });
            String path = uri.getPath();

            String s = path.substring(path.lastIndexOf("/") + 1, path.length());
            helper.setText(R.id.tv,s.split("_")[0] + typeName);

    }

    public interface OnClickListener {

        void onLongItemClick(PicSimpleAdapter adapter, GreenMedia greenMedia, int position);
    }

}
