package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.HandlingMenuFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.LawEnforcementMenuFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CaseManagerActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private Unbinder mBind;
    private HandlingMenuFragment mHandlingMenuFragment;

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
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment caseRegistrationFragment = getSupportFragmentManager().findFragmentByTag("CaseRegistrationFragment");
                Fragment openCasesFragment = getSupportFragmentManager().findFragmentByTag("OpenCasesFragment");
                Fragment caseProcessingListFragment = getSupportFragmentManager().findFragmentByTag("CaseProcessingListFragment");
                Fragment caseComplaintFragment = getSupportFragmentManager().findFragmentByTag("CaseComplaintFragment");
                Fragment caseInAdvanceFragment = getSupportFragmentManager().findFragmentByTag("CaseInAdvanceFragment");

                if (caseRegistrationFragment != null && !caseRegistrationFragment.isHidden()) {
                    mTvTitle.setText("办案");
                    fragmentTransaction.hide(caseRegistrationFragment);
                    fragmentTransaction.show(mHandlingMenuFragment).commit();
                }else if (openCasesFragment != null && !openCasesFragment.isHidden()) {
                    mTvTitle.setText("办案");
                    fragmentTransaction.hide(openCasesFragment);
                    fragmentTransaction.show(mHandlingMenuFragment).commit();
                }else if (caseProcessingListFragment != null && !caseProcessingListFragment.isHidden()) {
                    mTvTitle.setText("办案");
                    fragmentTransaction.hide(caseProcessingListFragment);
                    fragmentTransaction.show(mHandlingMenuFragment).commit();
                }else if (caseComplaintFragment != null && !caseComplaintFragment.isHidden()) {
                    mTvTitle.setText("办案");
                    fragmentTransaction.hide(caseComplaintFragment);
                    fragmentTransaction.show(mHandlingMenuFragment).commit();
                }else if (caseInAdvanceFragment != null && !caseInAdvanceFragment.isHidden()) {
                    mTvTitle.setText("办案");
                    fragmentTransaction.hide(caseInAdvanceFragment);
                    fragmentTransaction.show(mHandlingMenuFragment).commit();
                }else {
                    finish();
                }
            }
        });
    }

    private void initData() {
        mHandlingMenuFragment = HandlingMenuFragment.newInstance(null, null);
        getSupportFragmentManager().beginTransaction().replace(R.id.rl_case_content, mHandlingMenuFragment, "HandlingMenuFragment").commit();

//        initPage();

//        CaseManagerListFragment caseManagerListFragment = CaseManagerListFragment.newInstance(null, null);
//        getSupportFragmentManager().beginTransaction().replace(R.id.taskmanager_content, caseManagerListFragment).commit();
    }

//    private void initPage() {
//        ArrayList<String> listTitles = new ArrayList<>();
//        ArrayList<Fragment> fragments = new ArrayList<>();
//
//        listTitles.add("案件登记");
//        CaseRegistrationFragment caseRegistrationFragment = CaseRegistrationFragment.newInstance(null, null);
//        fragments.add(caseRegistrationFragment);
//        mTabLayout.addTab(mTabLayout.newTab().setText("案件登记"));
//
//        listTitles.add("案件受理");
//        OpenCasesFragment openCasesFragment = OpenCasesFragment.newInstance(null, null);
//        fragments.add(openCasesFragment);
//        mTabLayout.addTab(mTabLayout.newTab().setText("案件受理"));
//
//        listTitles.add("案件处理");
//        CaseProcessingListFragment caseManagerListFragment = CaseProcessingListFragment.newInstance(null, null);
//        fragments.add(caseManagerListFragment);
//        mTabLayout.addTab(mTabLayout.newTab().setText("案件处理"));
//
//        listTitles.add("案件转办");
//        CaseComplaintFragment caseComplaintFragment = CaseComplaintFragment.newInstance(null, null);
//        fragments.add(caseComplaintFragment);
//        mTabLayout.addTab(mTabLayout.newTab().setText("案件转办"));
//
//
//        listTitles.add("预立案");
//        CaseInAdvanceFragment caseInAdvanceFragment = CaseInAdvanceFragment.newInstance(null, null);
//        fragments.add(caseInAdvanceFragment);
//        mTabLayout.addTab(mTabLayout.newTab().setText("预立案"));
//
//
//        TitleAdapter titleTabAdapter = new TitleAdapter(getSupportFragmentManager(), fragments, listTitles);
//        mViewPager.setAdapter(titleTabAdapter);
//        mTabLayout.setupWithViewPager(mViewPager);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    public void setTitleText(String title) {
        mTvTitle.setText(title);
    }

}
