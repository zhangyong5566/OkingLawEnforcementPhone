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
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.LawEnforcementMenuFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 执法管理
 */
public class LawEnforcementManagerActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private Unbinder mBind;
    private LawEnforcementMenuFragment mLawEnforcementMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_enforcement_manager);
        mBind = ButterKnife.bind(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        initData();
        setListenner();
    }

    private void setListenner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment lawEnforcementMenuFragment = getSupportFragmentManager().findFragmentByTag("StopTheIllegalActivitiesFragment");
                Fragment penaltyTheSpotFragment = getSupportFragmentManager().findFragmentByTag("PenaltyTheSpotFragment");
                Fragment enforcementInspectionNormsFragment = getSupportFragmentManager().findFragmentByTag("EnforcementInspectionNormsFragment");
                Fragment enforcementLanguageSpecificationFragment = getSupportFragmentManager().findFragmentByTag("EnforcementLanguageSpecificationFragment");
                Fragment administrativeEnforcementFragment = getSupportFragmentManager().findFragmentByTag("AdministrativeEnforcementFragment");

                if (lawEnforcementMenuFragment != null && !lawEnforcementMenuFragment.isHidden()) {
                    mTvTitle.setText("辅助执法");
                    fragmentTransaction.hide(lawEnforcementMenuFragment);
                    fragmentTransaction.show(mLawEnforcementMenuFragment).commit();
                } else if (penaltyTheSpotFragment != null && !penaltyTheSpotFragment.isHidden()) {
                    mTvTitle.setText("辅助执法");
                    fragmentTransaction.hide(penaltyTheSpotFragment);
                    fragmentTransaction.show(mLawEnforcementMenuFragment).commit();
                } else if (enforcementInspectionNormsFragment != null && !enforcementInspectionNormsFragment.isHidden()) {
                    mTvTitle.setText("辅助执法");
                    fragmentTransaction.hide(enforcementInspectionNormsFragment);
                    fragmentTransaction.show(mLawEnforcementMenuFragment).commit();
                } else if (enforcementLanguageSpecificationFragment != null && !enforcementLanguageSpecificationFragment.isHidden()) {
                    mTvTitle.setText("辅助执法");
                    fragmentTransaction.hide(enforcementLanguageSpecificationFragment);
                    fragmentTransaction.show(mLawEnforcementMenuFragment).commit();
                } else if (administrativeEnforcementFragment != null && !administrativeEnforcementFragment.isHidden()) {
                    mTvTitle.setText("辅助执法");
                    fragmentTransaction.hide(administrativeEnforcementFragment);
                    fragmentTransaction.show(mLawEnforcementMenuFragment).commit();
                } else {
                    finish();
                }

            }
        });
    }

    private void initData() {
        mLawEnforcementMenuFragment = LawEnforcementMenuFragment.newInstance(null, null);
        getSupportFragmentManager().beginTransaction().replace(R.id.rl_law_content, mLawEnforcementMenuFragment, "LawEnforcementMenuFragment").commit();

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
