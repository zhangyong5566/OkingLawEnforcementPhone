package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.LawEnforcementSpecificationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FromAllLawEnforcementSpecificationActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Unbinder mBind;
    private LawEnforcementSpecificationFragment mLawEnforcementSpecificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_all_law_enforcement_specification);
        mBind = ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment enforcementInspectionNormsFragment = getSupportFragmentManager().findFragmentByTag("EnforcementInspectionNormsFragment");
                Fragment enforcementLanguageSpecificationFragment = getSupportFragmentManager().findFragmentByTag("EnforcementLanguageSpecificationFragment");
                Fragment administrativeEnforcementFragment = getSupportFragmentManager().findFragmentByTag("AdministrativeEnforcementFragment");
                if (enforcementInspectionNormsFragment!=null&&!enforcementInspectionNormsFragment.isHidden()){
                    fragmentTransaction.hide(enforcementInspectionNormsFragment);
                    fragmentTransaction.show(mLawEnforcementSpecificationFragment).commit();
                }else if (enforcementLanguageSpecificationFragment!=null&&!enforcementLanguageSpecificationFragment.isHidden()){
                    fragmentTransaction.hide(enforcementLanguageSpecificationFragment);
                    fragmentTransaction.show(mLawEnforcementSpecificationFragment).commit();
                }else if (administrativeEnforcementFragment!=null&&!administrativeEnforcementFragment.isHidden()){
                    fragmentTransaction.hide(administrativeEnforcementFragment);
                    fragmentTransaction.show(mLawEnforcementSpecificationFragment).commit();
                }else {

                    finish();
                }
            }
        });
    }

    private void initData() {
        mLawEnforcementSpecificationFragment = LawEnforcementSpecificationFragment.newInstance(null, null);
        getSupportFragmentManager().beginTransaction().replace(R.id.rl_administrative_content, mLawEnforcementSpecificationFragment, "LawEnforcementSpecificationFragment").commit();
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
