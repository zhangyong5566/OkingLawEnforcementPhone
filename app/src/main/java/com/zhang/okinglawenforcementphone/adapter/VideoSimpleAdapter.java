package com.zhang.okinglawenforcementphone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.GlideApp;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.VideoActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/25/025.
 */

public class VideoSimpleAdapter extends BaseQuickAdapter<GreenMedia, BaseViewHolder> {
    private OnClickListener onClickListener;
    private boolean canAdd;
    private HashMap<Uri, Bitmap> bitmapCache;
    private String typeName;
    private Activity activity;

    public VideoSimpleAdapter(int layoutResId, @Nullable List<GreenMedia> data, Activity recorActivity, boolean canAdd, String typeName) {
        super(layoutResId, data);
        this.activity = recorActivity;
        this.canAdd = canAdd;
        bitmapCache = new HashMap<>();
        this.typeName = typeName;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final GreenMedia item) {
        ImageView sdv = helper.getView(R.id.sdv);
        final int layoutPosition = helper.getLayoutPosition();
        helper.setVisible(R.id.tv, true);
        final Uri uri = Uri.parse(item.getPath());
        Bitmap bitmap = bitmapCache.get(uri);
        if (bitmap == null) {
            bitmap = ThumbnailUtils.createVideoThumbnail(FileUtil.praseUritoPath(BaseApplication.getApplictaion(), uri),
                    MediaStore.Images.Thumbnails.MICRO_KIND);
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 120, 180, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            bitmapCache.put(uri, bitmap);
        }

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(BaseApplication.getApplictaion().getResources(), R.drawable.loadfail);
        }

        GlideApp.with(activity)
                .load(bitmap)
                .placeholder(R.mipmap.ic_launcher_logo)
                .error(R.drawable.loadfail)
                .into(sdv);

        sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity, VideoActivity.class);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
        sdv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onClickListener != null && canAdd) {
                    onClickListener.onLongItemClick(VideoSimpleAdapter.this, item, layoutPosition);
                }

                bitmapCache.remove(uri);


                return false;
            }
        });

        String path = uri.getPath();

        String s = path.substring(path.lastIndexOf("/") + 1, path.length());
        helper.setText(R.id.tv, s.split("_")[0] + typeName);

    }

    public interface OnClickListener {

        void onLongItemClick(VideoSimpleAdapter adapter, GreenMedia greenMedia, int position);
    }
}
