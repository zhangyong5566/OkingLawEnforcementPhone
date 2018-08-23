package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhang.baselib.GlideApp;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.utils.DisplayManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ittiger.player.VideoPlayerView;


public class PlayVideoOnlineActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.video_player_view)
    VideoPlayerView mVideoPlayerView;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_online);
        mBind = ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void initData() {
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        if(!TextUtils.isEmpty(path)){
            //以宽高比16:9的比例设置播放器的尺寸
            int width = DisplayManager.screenWidthPixel(this);
            int height = (int) (width * 1.0f / 16 * 9 + 0.5f);
            RelativeLayout .LayoutParams params = (RelativeLayout.LayoutParams) mVideoPlayerView.getLayoutParams();
            params.height = height;
            params.width = width;
            mVideoPlayerView.setLayoutParams(params);
            //绑定视频地址和标题，这样视频播放功能就集成完成了，剩下的工作全部交由VideoPlayerView完成
            //显示视频预览图
            GlideApp.with(this).load(path).into(mVideoPlayerView.getThumbImageView());
            Log.i("Oking", path);
            mVideoPlayerView.bind(path);
        }else{
            this.finish();
        }


    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
