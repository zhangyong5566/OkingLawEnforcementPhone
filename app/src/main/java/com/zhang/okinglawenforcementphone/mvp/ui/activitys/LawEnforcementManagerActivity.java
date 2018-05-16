package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.TitleAdapter;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.CaseComplaintFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.CaseInAdvanceFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.CaseProcessingListFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.CaseRegistrationFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.LawEnforcementSpecificationFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.LawsAndRegulationsFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.OpenCasesFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.PenaltyTheSpotFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.StopTheIllegalActivitiesFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 执法管理
 */
public class LawEnforcementManagerActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Unbinder mBind;

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
                finish();
            }
        });
    }

    private void initData() {
        initPage();
    }

    private void initPage() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        ArrayList<String> listTitles = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();

        listTitles.add("责令停止违法行为通知");
        StopTheIllegalActivitiesFragment stopTheIllegalActivitiesFragment = StopTheIllegalActivitiesFragment.newInstance(null, null);
        fragments.add(stopTheIllegalActivitiesFragment);
        mTabLayout.addTab(mTabLayout.newTab().setText("责令停止违法行为通知"));

        listTitles.add("水行政当场处罚决定书");
        PenaltyTheSpotFragment penaltyTheSpotFragment = PenaltyTheSpotFragment.newInstance(null, null);
        fragments.add(penaltyTheSpotFragment);
        mTabLayout.addTab(mTabLayout.newTab().setText("水行政当场处罚决定书"));

//        listTitles.add("一般水行政处罚");
//        CaseProcessingListFragment caseManagerListFragment = CaseProcessingListFragment.newInstance(null, null);
//        fragments.add(caseManagerListFragment);
//        mTabLayout.addTab(mTabLayout.newTab().setText("一般水行政处罚"));

        listTitles.add("法律法规库");
        LawsAndRegulationsFragment lawsAndRegulationsFragment = LawsAndRegulationsFragment.newInstance(null, null);
        fragments.add(lawsAndRegulationsFragment);
        mTabLayout.addTab(mTabLayout.newTab().setText("法律法规库"));


        listTitles.add("执法规范");
        LawEnforcementSpecificationFragment lawEnforcementSpecificationFragment = LawEnforcementSpecificationFragment.newInstance(null, null);
        fragments.add(lawEnforcementSpecificationFragment);
        mTabLayout.addTab(mTabLayout.newTab().setText("执法规范"));


//        listTitles.add("案例库");
//        CaseComplaintFragment caseComplaintFragment = CaseComplaintFragment.newInstance(null,null);
//        fragments.add(caseComplaintFragment);
//        mTabLayout.addTab(mTabLayout.newTab().setText("案例库"));


        listTitles.add("预立案");
        CaseInAdvanceFragment caseInAdvanceFragment = CaseInAdvanceFragment.newInstance(null, null);
        fragments.add(caseInAdvanceFragment);
        mTabLayout.addTab(mTabLayout.newTab().setText("预立案"));


        TitleAdapter titleTabAdapter = new TitleAdapter(getSupportFragmentManager(), fragments, listTitles);
        mViewPager.setAdapter(titleTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(position);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
