package com.zhang.okinglawenforcementphone.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.GlideApp;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.VideoActivity;

import java.util.ArrayList;


/**
 * Created by zhao on 2016/10/9.
 */

public class CaseSimpleAdapter extends BaseAdapter {

    private ArrayList<GreenEvidenceMedia> greenMedias;
    private OnClickListener onClickListener;
    private Fragment f;
    private boolean canAdd;
    private String typeName;

    public CaseSimpleAdapter(ArrayList<GreenEvidenceMedia> greenMedias, Fragment f, boolean canAdd, String typeName) {
        this.greenMedias = greenMedias;
        this.f = f;
        this.canAdd = canAdd;
        this.typeName = typeName;
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return greenMedias.size()+1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView =  View.inflate(BaseApplication.getApplictaion(), R.layout.pic_item,null);
            viewHolder.iv_pic = convertView.findViewById(R.id.sdv);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            viewHolder.tv.setVisibility(View.GONE);
            GlideApp.with(f)
                    .load(R.drawable.video)
                    .into(viewHolder.iv_pic);

            if (canAdd) {
                viewHolder.iv_pic .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null) {
                            onClickListener.onAddVideo();
                        }
                    }
                });
            }


        } else {
            viewHolder.tv.setVisibility(View.VISIBLE);
            final Uri uri = Uri.parse(greenMedias.get(position - 1).getPath());

            GlideApp.with(f)
                    .load(uri)
                    .into(viewHolder.iv_pic);

            viewHolder.iv_pic .setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onClickListener != null && canAdd) {
                        onClickListener.onLongItemClick(CaseSimpleAdapter.this, greenMedias, position - 1);
                    }
//                    bitmapCache.remove(uri);
                    return false;
                }
            });
            viewHolder.iv_pic .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(f.getActivity(), VideoActivity.class);
                    intent.setData(uri);
                    f.startActivity(intent);
                }
            });
            String path = uri.getPath();

            String s = path.substring(path.lastIndexOf("/") + 1, path.length());
            viewHolder.tv.setText(s.split("_")[0]+typeName);
        }

        return convertView;

    }

    public interface OnClickListener {
        void onAddVideo();

        void onLongItemClick(CaseSimpleAdapter adapter, ArrayList<GreenEvidenceMedia> data, int position);
    }

    class ViewHolder{
        ImageView iv_pic;
        TextView tv;
    }
}
