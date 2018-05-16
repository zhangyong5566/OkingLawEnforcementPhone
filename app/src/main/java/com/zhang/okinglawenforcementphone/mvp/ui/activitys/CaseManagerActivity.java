package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
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
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.OpenCasesFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CaseManagerActivity extends BaseActivity {
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
        setContentView(R.layout.activity_case_manager);
        mBind = ButterKnife.bind(this);
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

//        CaseManagerListFragment caseManagerListFragment = CaseManagerListFragment.newInstance(null, null);
//        getSupportFragmentManager().beginTransaction().replace(R.id.taskmanager_content, caseManagerListFragment).commit();
    }

    private void initPage() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        ArrayList<String> listTitles = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();

        listTitles.add("案件登记");
        CaseRegistrationFragment caseRegistrationFragment = CaseRegistrationFragment.newInstance(null, null);
        fragments.add(caseRegistrationFragment);
        mTabLayout.addTab(mTabLayout.newTab().setText("案件登记"));

        listTitles.add("案件受理");
        OpenCasesFragment openCasesFragment = OpenCasesFragment.newInstance(null, null);
        fragments.add(openCasesFragment);
        mTabLayout.addTab(mTabLayout.newTab().setText("案件受理"));

        listTitles.add("案件处理");
        CaseProcessingListFragment caseManagerListFragment = CaseProcessingListFragment.newInstance(null, null);
        fragments.add(caseManagerListFragment);
        mTabLayout.addTab(mTabLayout.newTab().setText("案件处理"));

        listTitles.add("案件转办");
        CaseComplaintFragment caseComplaintFragment = CaseComplaintFragment.newInstance(null, null);
        fragments.add(caseComplaintFragment);
        mTabLayout.addTab(mTabLayout.newTab().setText("案件转办"));


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
