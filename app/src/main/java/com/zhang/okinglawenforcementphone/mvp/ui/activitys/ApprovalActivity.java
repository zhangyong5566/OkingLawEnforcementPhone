package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.OkingJPushManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.NavViewRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLogDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.JPushMessageBean;
import com.zhang.okinglawenforcementphone.beans.NavBean;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.mvp.contract.JPushMessageContract;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.ApprovalInstructionsFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.ApprovalPicVideoFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.ApprovalTaskInfoFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.ApprovalTheLogFragment;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ApprovalActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    RecyclerView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.fab)
    ImageView mFab;
    private Unbinder mBind;
    private GreenMissionTask mGreenMissionTask;
    private GreenMissionLog mGreenMissionLog;
    private NavViewRecyAdapter mNavViewRecyAdapter;
    private int mPosition;
    private ApprovalTaskInfoFragment mApprovalTaskInfoFragment;
    private ApprovalTheLogFragment mApprovalTheLogFragment;
    private ApprovalPicVideoFragment mApprovalPicVideoFragment;
    private ApprovalInstructionsFragment mApprovalInstructionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        mBind = ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        ArrayList<NavBean> navBeans = new ArrayList<>();
        NavBean navBean1 = new NavBean();
        navBean1.setIcon(R.mipmap.icon_taskinfo);
        navBean1.setTitle("任务信息");
        navBeans.add(navBean1);
        NavBean navBean2 = new NavBean();
        navBean2.setIcon(R.mipmap.icon_taskpatrl);
        navBean2.setTitle("日志情况");
        navBeans.add(navBean2);
        NavBean navBean3 = new NavBean();
        navBean3.setIcon(R.mipmap.icon_result);
        navBean3.setTitle("图片视频信息");
        navBeans.add(navBean3);
        NavBean navBean4 = new NavBean();
        navBean4.setIcon(R.mipmap.icon_tagpic);
        navBean4.setTitle("领导批示");
        navBeans.add(navBean4);
        mNavViewRecyAdapter = new NavViewRecyAdapter(R.layout.navview_item, navBeans);
        mNavViewRecyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mNavView.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mNavView.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 1, Color.DKGRAY));
        mNavView.setAdapter(mNavViewRecyAdapter);
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mApprovalTaskInfoFragment = ApprovalTaskInfoFragment.newInstance(null, null);
        mApprovalTaskInfoFragment.setMissionTask(mGreenMissionTask);
        fragmentTransaction.replace(R.id.rl_mision, mApprovalTaskInfoFragment, "ApprovalTaskInfoFragment").commitAllowingStateLoss();
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mNavViewRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (position) {
                    case 0:                     //基本信息
                        mTvTitle.setText("任务信息");
                        if (mApprovalTheLogFragment != null) {
                            fragmentTransaction.hide(mApprovalTheLogFragment);
                        }
                        if (mApprovalPicVideoFragment != null) {

                            fragmentTransaction.hide(mApprovalPicVideoFragment);
                        }
                        if (mApprovalInstructionsFragment != null) {

                            fragmentTransaction.hide(mApprovalInstructionsFragment);
                        }
                        if (mApprovalTaskInfoFragment != null) {
                            fragmentTransaction.show(mApprovalTaskInfoFragment);
                        } else {
                            mApprovalTaskInfoFragment = ApprovalTaskInfoFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_mision, mApprovalTaskInfoFragment, "ApprovalTaskInfoFragment");
                        }
                        fragmentTransaction.commitAllowingStateLoss();

                        break;
                    case 1:                     //巡查情况
                        mTvTitle.setText("日志情况");
                        if (mApprovalTaskInfoFragment != null) {

                            fragmentTransaction.hide(mApprovalTaskInfoFragment);
                        }
                        if (mApprovalPicVideoFragment != null) {

                            fragmentTransaction.hide(mApprovalPicVideoFragment);
                        }
                        if (mApprovalInstructionsFragment != null) {

                            fragmentTransaction.hide(mApprovalInstructionsFragment);
                        }
                        if (mApprovalTheLogFragment != null) {
                            fragmentTransaction.show(mApprovalTheLogFragment);
                        } else {
                            mApprovalTheLogFragment = ApprovalTheLogFragment.newInstance(null, null);
                            mApprovalTheLogFragment.setMissionTask(mGreenMissionTask);
                            mApprovalTheLogFragment.setMissionLog(mGreenMissionLog);
                            fragmentTransaction.add(R.id.rl_mision, mApprovalTheLogFragment, "ApprovalTheLogFragment");

                        }
                        fragmentTransaction.commitAllowingStateLoss();

                        break;
                    case 2:                     //处理结果
                        mTvTitle.setText("图片视频信息");
                        if (mApprovalTaskInfoFragment != null) {

                            fragmentTransaction.hide(mApprovalTaskInfoFragment);
                        }
                        if (mApprovalTheLogFragment != null) {

                            fragmentTransaction.hide(mApprovalTheLogFragment);
                        }
                        if (mApprovalInstructionsFragment != null) {

                            fragmentTransaction.hide(mApprovalInstructionsFragment);
                        }
                        if (mApprovalPicVideoFragment != null) {
                            fragmentTransaction.show(mApprovalPicVideoFragment);
                        } else {
                            mApprovalPicVideoFragment = ApprovalPicVideoFragment.newInstance(null, null);
                            mApprovalPicVideoFragment.setMissionTask(mGreenMissionTask);
                            mApprovalPicVideoFragment.setMissionLog(mGreenMissionLog);
                            fragmentTransaction.add(R.id.rl_mision, mApprovalPicVideoFragment, "ApprovalPicVideoFragment");

                        }
                        fragmentTransaction.commitAllowingStateLoss();


                        break;
                    case 3:                     //拍照
                        mTvTitle.setText("领导批示");
                        if (mApprovalTaskInfoFragment != null) {

                            fragmentTransaction.hide(mApprovalTaskInfoFragment);
                        }
                        if (mApprovalTheLogFragment != null) {

                            fragmentTransaction.hide(mApprovalTheLogFragment);
                        }
                        if (mApprovalPicVideoFragment != null) {

                            fragmentTransaction.hide(mApprovalPicVideoFragment);
                        }
                        if (mApprovalInstructionsFragment != null) {
                            fragmentTransaction.show(mApprovalInstructionsFragment);
                        } else {
                            mApprovalInstructionsFragment = ApprovalInstructionsFragment.newInstance(null, null);
                            mApprovalInstructionsFragment.setMissionTask(mGreenMissionTask);
                            mApprovalInstructionsFragment.setMissionLog(mGreenMissionLog);
                            fragmentTransaction.add(R.id.rl_mision, mApprovalInstructionsFragment, "ApprovalInstructionsFragment");

                        }
                        fragmentTransaction.commitAllowingStateLoss();
                        break;
                    default:
                        break;
                }

                mDrawerLayout.closeDrawers();

            }
        });
    }

    private void initData() {
        long id = getIntent().getLongExtra("id", -1L);
        mPosition = getIntent().getIntExtra("position", -1);


        if (id != -1L) {
            mGreenMissionTask = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                    .queryBuilder()
                    .where(GreenMissionTaskDao.Properties.Id.eq(id)).unique();
            mGreenMissionLog =  GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao()
                    .queryBuilder()
                    .where(GreenMissionLogDao.Properties.Task_id.eq(mGreenMissionTask.getTaskid()))
                    .unique();

        }

//        GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().insert()
        initFragment();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    //批示成功回调
    public void designatorSuccess() {


        UpdateGreenMissionTaskOV updateGreenMissionTaskOV = new UpdateGreenMissionTaskOV();
        updateGreenMissionTaskOV.setType(100);
        updateGreenMissionTaskOV.setPosition(mPosition);

        EventBus.getDefault().post(updateGreenMissionTaskOV);
        GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().delete(mGreenMissionTask);
        finish();
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {

            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
}
