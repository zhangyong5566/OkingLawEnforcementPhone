package com.zhang.okinglawenforcementphone.adapter;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.baselib.GlideApp;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;

import java.util.List;


/**
 * Created by zhao on 2016/10/9.
 */

public class PicSimpleAdapter extends BaseQuickAdapter<GreenMedia, BaseViewHolder> {

    private Activity activity;
    private boolean canAdd;
    private String typeName;

    public PicSimpleAdapter(int layoutResId, List<GreenMedia> data, Activity recorActivity, boolean canAdd, String typeName) {
        super(layoutResId, data);
        this.activity = recorActivity;
        this.canAdd = canAdd;
        this.typeName = typeName;
    }

    @Override
    protected void convert(BaseViewHolder helper, final GreenMedia item) {
        ImageView sdv = helper.getView(R.id.sdv);
        String path = item.getPath();
        final Uri uri = Uri.parse(path);
        GlideApp.with(activity)
                .load(uri)
                .placeholder(R.mipmap.ic_launcher_logo)
                .error(R.drawable.loadfail)
                .into(sdv);
        helper.addOnClickListener(R.id.sdv);

        if (canAdd) {
            helper.addOnLongClickListener(R.id.sdv);
        }
        String picName = uri.getPath();

        String s = picName.substring(picName.lastIndexOf("/") + 1, picName.length());
        helper.setText(R.id.tv, s.split("_")[0] + typeName);

    }


}
