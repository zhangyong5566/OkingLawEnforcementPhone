package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.SettingListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mBind = ButterKnife.bind(this);
        initData();
        setListener();

    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment phoneBookFragment = getSupportFragmentManager().findFragmentByTag("PhoneBookFragment");
                Fragment settingListFragment = getSupportFragmentManager().findFragmentByTag("SettingListFragment");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (!phoneBookFragment.isHidden()) {
                    mTvTitle.setText("设置中心");
                    fragmentTransaction.hide(phoneBookFragment);
                    fragmentTransaction.show(settingListFragment).commit();
                } else {

                    finish();
                }
            }
        });
    }

    private void initData() {
        SettingListFragment settingListFragment = SettingListFragment.newInstance(null, null);
        getSupportFragmentManager().beginTransaction().replace(R.id.rl_setting_content, settingListFragment, "SettingListFragment").commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    public void setTitleText(String title) {
        mTvTitle.setText(title);
    }
}
