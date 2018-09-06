package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.AllMenuRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.AllMenuItemBean;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcy_all)
    RecyclerView rcyAll;
    private Unbinder mBind;
    private AllMenuItemBean mAllMenuItemBean;
    private List<String> mMenusSub;
    private RxDialogSureCancel mRxDialogSureCancel;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        mBind = ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        rcyAll.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        rcyAll.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 20, getResources().getColor(R.color.activity_bg)));

        getMenu();
        AllMenuRecyAdapter allMenuRecyAdapter = new AllMenuRecyAdapter(R.layout.all_menu_item, getMenu(), new AllMenuRecyAdapter.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(BaseQuickAdapter adapter, View view, int groupPosition, int chilPosition) {
                switch (groupPosition) {
                    case 0:                                     //任务管理
                        switch (chilPosition) {
                            case 0:
                                if (mRxDialogSureCancel == null) {

                                    mRxDialogSureCancel = new RxDialogSureCancel(AllActivity.this);
                                }
                                mRxDialogSureCancel.setContent("请选择发布任务类型");
                                mRxDialogSureCancel.getTvSure().setText("一般任务");
                                mRxDialogSureCancel.getTvCancel().setText("紧急任务");
                                mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mRxDialogSureCancel.cancel();
                                        Intent intent = new Intent(AllActivity.this, TemporaryEmergencyTaskActivity.class);
                                        startActivity(intent);
                                    }
                                });

                                mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mRxDialogSureCancel.cancel();
                                        Intent intent = new Intent(AllActivity.this, PatrolsToReleaseActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                mRxDialogSureCancel.show();
                                break;
                            case 1:
                                intent = new Intent(AllActivity.this, TaskMissionProjectActivity.class);
                                intent.putExtra("activity", "ArrangeMissionActivity");
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(AllActivity.this, TaskMissionProjectActivity.class);
                                intent.putExtra("activity", "TaskExecutionActivity");
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(AllActivity.this, TaskMissionProjectActivity.class);
                                intent.putExtra("activity", "ReportTaskActivity");
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(AllActivity.this, TaskMissionProjectActivity.class);
                                intent.putExtra("activity", "CompleteListActivity");
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 1:                                     //执法管理
                        switch (chilPosition) {
                            case 0:
                                intent = new Intent(AllActivity.this, WrittenRecordActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(AllActivity.this, FromAllLawEnforcementActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(AllActivity.this, FromAllPenaltyTheSpotActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(AllActivity.this, SceneInquestActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 2:                                     //执法指导
                        switch (chilPosition) {
                            case 0:
                                intent = new Intent(AllActivity.this, FromAllLawsAndRegulationsActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(AllActivity.this, FromAllLawEnforcementSpecificationActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(AllActivity.this, PuttedForwardConActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 3:                                     //统计查询
                        switch (chilPosition) {
                            case 0:
                                intent = new Intent(AllActivity.this, StatisticalActivity.class);
                                startActivity(intent);

                                break;
                            case 1:
                                intent = new Intent(AllActivity.this, MapQueryActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(AllActivity.this, MapTaskActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(AllActivity.this, RegionalHistoryEnforcementActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(AllActivity.this, TaskMissionProjectActivity.class);
                                intent.putExtra("activity", "TrajectoryListActivity");
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 4:                                     //案件管理
                        switch (chilPosition) {
                            case 0:
                                intent = new Intent(AllActivity.this, FromAllCaseRegistrationActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(AllActivity.this, FromAllOpenCasesActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(AllActivity.this, FromAllCaseProcessingListActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(AllActivity.this, FromAllCaseComplaintActivity.class);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(AllActivity.this, FromAllCaseInAdvanceActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;


                }
            }
        });
        allMenuRecyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        rcyAll.setAdapter(allMenuRecyAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();

    }

    public List<AllMenuItemBean> getMenu() {
        List<AllMenuItemBean> allMenuItemBeans = new ArrayList<>();
        mAllMenuItemBean = new AllMenuItemBean();
        mAllMenuItemBean.setTitle("任务管理");
        mMenusSub = new ArrayList<>();
        mMenusSub.add("巡查任务发布");
        mMenusSub.add("巡查任接收安排");
        mMenusSub.add("任务执行");
        mMenusSub.add("任务上报");
        mMenusSub.add("日志(已完成)");
        mAllMenuItemBean.setSubList(mMenusSub);
        allMenuItemBeans.add(mAllMenuItemBean);

        mAllMenuItemBean = new AllMenuItemBean();
        mAllMenuItemBean.setTitle("执法管理");
        mMenusSub = new ArrayList<>();
        mMenusSub.add("调查笔录");
        mMenusSub.add("责令停止违法行为通知");
        mMenusSub.add("水行政当场除非决定书");
        mMenusSub.add("现场勘验");
        mAllMenuItemBean.setSubList(mMenusSub);
        allMenuItemBeans.add(mAllMenuItemBean);

        mAllMenuItemBean = new AllMenuItemBean();
        mAllMenuItemBean.setTitle("执法指导");
        mMenusSub = new ArrayList<>();
        mMenusSub.add("法律法规库");
        mMenusSub.add("执法规范");
        mMenusSub.add("案例库");
        mAllMenuItemBean.setSubList(mMenusSub);
        allMenuItemBeans.add(mAllMenuItemBean);

        mAllMenuItemBean = new AllMenuItemBean();
        mAllMenuItemBean.setTitle("统计查询");
        mMenusSub = new ArrayList<>();
        mMenusSub.add("日志统计");
        mMenusSub.add("地图查询");
        mMenusSub.add("地图任务展示");
        mMenusSub.add("区域执法记录查询");
        mMenusSub.add("轨迹管理");
        mAllMenuItemBean.setSubList(mMenusSub);
        allMenuItemBeans.add(mAllMenuItemBean);

        mAllMenuItemBean = new AllMenuItemBean();
        mAllMenuItemBean.setTitle("案件管理");
        mMenusSub = new ArrayList<>();
        mMenusSub.add("案件登记");
        mMenusSub.add("案件受理");
        mMenusSub.add("案件处理");
        mMenusSub.add("案件转办");
        mMenusSub.add("预立案");
        mAllMenuItemBean.setSubList(mMenusSub);
        allMenuItemBeans.add(mAllMenuItemBean);
        return allMenuItemBeans;
    }
}
