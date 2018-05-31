package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.GlideApp;
import com.zhang.baselib.utils.DeviceUtil;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.UserFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_sdv)
    ImageView mainSdv;
    @BindView(R.id.tv_tagabout)
    TextView tvTagabout;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mBind = ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initData() {
        GlideApp.with(AboutActivity.this)
                .load(R.mipmap.ic_launcher_logo)
                .circleCrop()
                .error(R.drawable.loadfail)
                .into(mainSdv);
        tvTagabout.setText(Html.fromHtml("<font color=\"#98CF60\">诚信&nbsp;&nbsp;&nbsp;&nbsp;&nbsp</font><font color=\"#ffea64\">勤奋&nbsp;&nbsp;&nbsp;&nbsp;&nbsp</font><font color=\"#FF6C00\">创新&nbsp;&nbsp;&nbsp;&nbsp;&nbsp</font><font color=\"#007FFE\">合作</font>"));
        tvVersion.setText("当前版本：v"+DeviceUtil.getAppVersionName(BaseApplication.getApplictaion()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
