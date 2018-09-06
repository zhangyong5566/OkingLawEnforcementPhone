package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenCase;
import com.zhang.okinglawenforcementphone.beans.GreenCaseDao;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.CaseDealFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CaseDealActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;


    private Unbinder mBind;

    private RxDialogSureCancel mRxDialogSureCancel;
    private GreenCase mUnique;
    private CaseDealFragment mCaseDealFragment;
    private String mAjid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_deal);
        mBind = ButterKnife.bind(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }


    private void initData() {

        Intent intent = getIntent();
        mAjid = intent.getStringExtra("AJID");
        if (mAjid != null) {

            mUnique = GreenDAOManager.getInstence().getDaoSession().getGreenCaseDao().queryBuilder().where(GreenCaseDao.Properties.AJID.eq(mAjid)).unique();
        }


        mCaseDealFragment = CaseDealFragment.newInstance(null, null);
        mCaseDealFragment.setGreenCase(mUnique);
        getSupportFragmentManager().beginTransaction().replace(R.id.sub_fragment_root, mCaseDealFragment, "caseDealFragment").commit();

    }

    private void initView() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }


    public GreenCase getCase() {
        return mUnique;
    }

    public String getAJID() {
        return mAjid;
    }

}
