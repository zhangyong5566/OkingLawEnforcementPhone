package com.zhang.okinglawenforcementphone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.baselib.GlideApp;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceMedia;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ImageViewActivity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public class EvidencePicRecyAdapter extends BaseQuickAdapter<GreenEvidenceMedia, BaseViewHolder> {
    private EvidencePicRecyAdapter.OnClickListener onClickListener;
    private Activity activity;
    private boolean canAdd;
    private String typeName;
    public EvidencePicRecyAdapter(int layoutResId, @Nullable List<GreenEvidenceMedia> data, Activity recorActivity, boolean canAdd, String typeName) {
        super(layoutResId, data);
        this.activity = recorActivity;
        this.canAdd = canAdd;
        this.typeName = typeName;
    }

    @Override
    protected void convert(BaseViewHolder helper, final GreenEvidenceMedia item) {
        ImageView sdv = helper.getView(R.id.sdv);
        final int layoutPosition = helper.getLayoutPosition();
        String path = item.getPath();
        final Uri uri = Uri.parse(path);
        GlideApp.with(activity)
                .load(uri)
                .placeholder(R.mipmap.ic_launcher_logo)
                .error(R.drawable.loadfail)
                .into(sdv);
//        sdv.setImageURI(uri);
        sdv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onClickListener != null && canAdd) {
                    onClickListener.onLongItemClick(EvidencePicRecyAdapter.this, item, layoutPosition);
                }
                return false;
            }
        });
        sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ImageViewActivity.class);
                //file:///storage/emulated/0/oking/mission_pic/e9f8735f-066b-42da-85ed-e8ec0676a18c.jpg
                //file:///storage/emulated/0/oking/mission_pic/e9f8735f-066b-42da-85ed-e8ec0676a18c.jpg
                Log.i("Oking", uri.toString() + ">>>>>>>>");
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
        String picName = uri.getPath();

        String s = picName.substring(picName.lastIndexOf("/") + 1, picName.length());
        helper.setText(R.id.tv, s.split("_")[0] + typeName);
    }

    public void setOnClickListener(EvidencePicRecyAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {

        void onLongItemClick(EvidencePicRecyAdapter adapter, GreenEvidenceMedia greenMedia, int position);
    }
}
