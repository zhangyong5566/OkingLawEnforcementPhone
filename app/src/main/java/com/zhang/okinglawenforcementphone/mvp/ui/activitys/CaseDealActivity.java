package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.okinglawenforcementphone.GreenDAOMannager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenCase;
import com.zhang.okinglawenforcementphone.beans.GreenCaseDao;
import com.zhang.okinglawenforcementphone.beans.SaveOrRemoveDataEvent;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.CaseDealFragment;

import org.greenrobot.eventbus.EventBus;

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
                Fragment documentaryEvidenceFragment = getSupportFragmentManager().findFragmentByTag("documentaryEvidenceFragment");
                Fragment caseEvidenceFragment = getSupportFragmentManager().findFragmentByTag("caseEvidenceFragment");
                Fragment caseAudioVideoEvidenceFragment = getSupportFragmentManager().findFragmentByTag("caseAudioVideoEvidenceFragment");
                if (documentaryEvidenceFragment != null && documentaryEvidenceFragment.getUserVisibleHint()) {
                    saveData(documentaryEvidenceFragment);

                } else if (caseAudioVideoEvidenceFragment != null && caseAudioVideoEvidenceFragment.getUserVisibleHint()) {
                    saveData(caseAudioVideoEvidenceFragment);
                }else if (caseEvidenceFragment!=null&&caseEvidenceFragment.getUserVisibleHint()){
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.show(mCaseDealFragment);
                    fragmentTransaction.remove(caseEvidenceFragment).commit();
                }else {
                    finish();
                }

            }
        });
    }

    private void saveData(final Fragment fragment) {
        if (mRxDialogSureCancel == null) {
            mRxDialogSureCancel = new RxDialogSureCancel(CaseDealActivity.this);
            mRxDialogSureCancel.setContent("是否需要保存数据？");
            mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRxDialogSureCancel.cancel();
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            });

            mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRxDialogSureCancel.cancel();
                    EventBus.getDefault().post(new SaveOrRemoveDataEvent(-1));
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            });
        }

        mRxDialogSureCancel.show();
    }

    private void initData() {

        Intent intent = getIntent();
        String ajid = intent.getStringExtra("AJID");
        if (ajid != null) {

            mUnique = GreenDAOMannager.getInstence().getDaoSession().getGreenCaseDao().queryBuilder().where(GreenCaseDao.Properties.AJID.eq(ajid)).unique();
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

    public void setToolbarText(String title) {
        mTvTitle.setText(title);
    }
}
