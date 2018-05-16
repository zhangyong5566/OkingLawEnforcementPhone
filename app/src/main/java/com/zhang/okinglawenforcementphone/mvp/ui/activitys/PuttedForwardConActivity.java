package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.PuttedForwardListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PuttedForwardConActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Unbinder mBind;
    private PuttedForwardListFragment mPuttedForwardListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putted_forward_con);
        mBind = ButterKnife.bind(this);
        iniData();
        setListener();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment puttedForwardFragment = getSupportFragmentManager().findFragmentByTag("PuttedForwardFragment");
                if (puttedForwardFragment!=null&&!puttedForwardFragment.isHidden()){
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.hide(puttedForwardFragment);
                    fragmentTransaction.show(mPuttedForwardListFragment).commit();
                }else {

                    finish();
                }
            }
        });
    }

    private void iniData() {
        if(mPuttedForwardListFragment ==null){
            mPuttedForwardListFragment = PuttedForwardListFragment.newInstance(null, null);

        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ll_putted_content,mPuttedForwardListFragment,"PuttedForwardListFragment")
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}
