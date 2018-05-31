package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.RegulationsExListViewAdapter;
import com.zhang.okinglawenforcementphone.beans.LawsRegulation;
import com.zhang.okinglawenforcementphone.db.LawDao;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.RegulationsDetailFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.RegulationsTitleListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegulationsDetailsActivity extends BaseActivity {

    private Unbinder mBind;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.tv_title)
    TextView mTv_title;

    private String mmid;
    private String title;
    private String rulesContent;
    private RegulationsTitleListFragment mRegulationsTitleListFragment;
    private RegulationsDetailFragment mRegulationsDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regulations_details);
        mBind = ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }

    private void setListener() {


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment regulationsDetailFragment = getSupportFragmentManager().findFragmentByTag("RegulationsDetailFragment");
                if (regulationsDetailFragment != null && mRegulationsTitleListFragment!=null&&!regulationsDetailFragment.isHidden()) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.hide(regulationsDetailFragment);
                    fragmentTransaction.show(mRegulationsTitleListFragment);
                    fragmentTransaction.commit();
                } else {

                    finish();
                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();

        mmid = intent.getStringExtra("mmid");
        title = intent.getStringExtra("title");
        rulesContent = intent.getStringExtra("rulesContent");
        mTv_title.setText(title);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (title.equals("广东省河道采砂管理条例")) {
            if (mRegulationsDetailFragment == null) {
                mRegulationsDetailFragment = RegulationsDetailFragment.newInstance(null, rulesContent);

                fragmentTransaction.replace(R.id.ll_regulation_content, mRegulationsDetailFragment, "RegulationsDetailFragment").commit();

            }

        } else {
            if (mRegulationsTitleListFragment == null) {
                mRegulationsTitleListFragment = RegulationsTitleListFragment.newInstance(mmid, rulesContent);
                fragmentTransaction.replace(R.id.ll_regulation_content, mRegulationsTitleListFragment, "RegulationsTitleListFragment").commit();

            }
        }


    }

    private void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBind.unbind();
    }

    public void setTitle(String title) {
        mTv_title.setText(title);
    }
}
