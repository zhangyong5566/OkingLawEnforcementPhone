package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.SerchRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.GreenSearchHistory;
import com.zhang.okinglawenforcementphone.beans.GreenSearchHistoryDao;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.SeachBean;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SerchActivity extends BaseActivity {

    @BindView(R.id.et_seach)
    EditText mEtSeach;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rcy_serch)
    RecyclerView mRcySerch;
    @BindView(R.id.seach_flow_layout)
    TagFlowLayout mSeachFlowLayout;
    @BindView(R.id.tv_tag)
    TextView mTvTag;
    @BindView(R.id.tv_clear)
    TextView mTvClear;
    private Unbinder mBind;
    private SerchRecyAdapter mSerchRecyAdapter;
    private List<SeachBean> seachBeans = new ArrayList<>();
    private String[] mStringArray;
    private TagAdapter<GreenSearchHistory> mTagAdapter;
    private List<GreenSearchHistory> mGreenSearchHistories;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch);
        mBind = ButterKnife.bind(this);
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
        mEtSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    seachBeans.clear();
                    mRcySerch.setVisibility(View.GONE);
                    mTvTag.setText("搜索结果");
                    mSeachFlowLayout.setVisibility(View.GONE);
                }
            }
        });
        mEtSeach.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SerchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String trim = mEtSeach.getText().toString().trim();
                    if (!TextUtils.isEmpty(trim)) {

                        search(trim);
                    }


                }
                return false;
            }
        });
        mSeachFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                search(mGreenSearchHistories.get(position).getSearchText());
                return false;
            }
        });

    }

    private void search(String trim) {
        GreenSearchHistory unique = GreenDAOManager.getInstence().getDaoSession().getGreenSearchHistoryDao()
                .queryBuilder().where(GreenSearchHistoryDao.Properties.UserId.eq(OkingContract.CURRENTUSER.getUserid()), GreenSearchHistoryDao.Properties.SearchText.eq(trim)).unique();
        if (unique == null) {
            GreenSearchHistory greenSearchHistory = new GreenSearchHistory();
            greenSearchHistory.setSearchText(trim);
            greenSearchHistory.setUserId(OkingContract.CURRENTUSER.getUserid());
            greenSearchHistory.setTime(System.currentTimeMillis());
            GreenDAOManager.getInstence().getDaoSession().getGreenSearchHistoryDao().insert(greenSearchHistory);
        }
        //查询任务
        List<GreenMissionTask> greenMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()), GreenMissionTaskDao.Properties.Task_name.like("%" + trim + "%"))
                .list();
        seachBeans.clear();
        if (greenMissionTasks.size() > 0) {
            for (GreenMissionTask greenMissionTask : greenMissionTasks) {
                SeachBean seachBean = new SeachBean();
                seachBean.setItemType(0);
                seachBean.setPublisherName(greenMissionTask.getPublisher_name());
                seachBean.setState(greenMissionTask.getStatus());
                seachBean.setId(greenMissionTask.getId());
                seachBean.setTaskId(greenMissionTask.getTaskid());
                seachBean.setTaskName(greenMissionTask.getTask_name());
                seachBeans.add(seachBean);

            }


        } else {
            //去搜索菜单
            for (String s : mStringArray) {

                if (s.contains(trim)) {
                    SeachBean seachBean = new SeachBean();
                    seachBean.setItemType(1);
                    seachBean.setMenuItme(s);
                    seachBeans.add(seachBean);
                } else {

                }
            }

            if (seachBeans.size() == 0) {

                RxToast.warning("没有搜索到结果");
            }
        }

        mRcySerch.setVisibility(View.VISIBLE);
        mTvTag.setText("搜索结果");
        mSeachFlowLayout.setVisibility(View.GONE);
        if (mSerchRecyAdapter == null) {

            mSerchRecyAdapter = new SerchRecyAdapter(seachBeans);
            mSerchRecyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
            mRcySerch.setAdapter(mSerchRecyAdapter);

            mSerchRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    List<SeachBean> seachBeans = adapter.getData();
                    SeachBean seachBean = seachBeans.get(position);
                    switch (seachBean.getItemType()) {
                        case 0:
                            switch (seachBean.getState()) {
                                case "0":

                                case "1":

                                case "2":

                                    mIntent = new Intent(SerchActivity.this, ArrangeTeamMembersActivity.class);
                                    mIntent.putExtra("id", seachBean.getId());
                                    mIntent.putExtra("position", position);
                                    startActivity(mIntent);

                                    break;
                                case "3":
                                case "4":
                                    mIntent = new Intent(SerchActivity.this, MissionActivity.class);
                                    mIntent.putExtra("id", seachBean.getId());
                                    mIntent.putExtra("position", position);
                                    startActivity(mIntent);
                                    break;
                                case "100":
                                case "5":
                                case "9":
                                    mIntent = new Intent(SerchActivity.this, MissionRecorActivity.class);
                                    mIntent.putExtra("id", seachBean.getId());
                                    mIntent.putExtra("taskId", seachBean.getTaskId());
                                    startActivity(mIntent);

                                    break;
                                default:
                                    break;
                            }
                            break;

                        case 1:
                            switch (seachBean.getMenuItme()) {
                                case "一般任务":
                                    mIntent = new Intent(SerchActivity.this, PatrolsToReleaseActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "紧急任务":
                                    mIntent = new Intent(SerchActivity.this, TemporaryEmergencyTaskActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "待办":
                                    mIntent = new Intent(SerchActivity.this, ToDoActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "安排":
                                case "巡查任务接收安排":
                                    mIntent = new Intent(SerchActivity.this, TaskMissionProjectActivity.class);
                                    mIntent.putExtra("activity", "ArrangeMissionActivity");
                                    startActivity(mIntent);
                                    break;
                                case "任务执行":
                                case "执行":
                                    mIntent = new Intent(SerchActivity.this, TaskMissionProjectActivity.class);
                                    mIntent.putExtra("activity", "TaskExecutionActivity");
                                    startActivity(mIntent);
                                    break;
                                case "任务上报":
                                case "上报":
                                    mIntent = new Intent(SerchActivity.this, TaskMissionProjectActivity.class);
                                    mIntent.putExtra("activity", "ReportTaskActivity");
                                    startActivity(mIntent);
                                    break;
                                case "调查笔录":
                                case "笔录":
                                    mIntent = new Intent(SerchActivity.this, WrittenRecordActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "办案":
                                    mIntent = new Intent(SerchActivity.this, CaseManagerActivity.class);
                                    mIntent.putExtra("position", 0);
                                    startActivity(mIntent);
                                    break;
                                case "现场勘验":
                                case "勘验":
                                    mIntent = new Intent(SerchActivity.this, SceneInquestActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "日志统计":
                                case "统计":
                                    mIntent = new Intent(SerchActivity.this, StatisticalActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "执法":
                                    mIntent = new Intent(SerchActivity.this, LawEnforcementManagerActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "日志(已完成)":
                                case "日志":
                                    mIntent = new Intent(SerchActivity.this, TaskMissionProjectActivity.class);
                                    mIntent.putExtra("activity", "CompleteListActivity");
                                    startActivity(mIntent);
                                    break;
                                case "轨迹管理":
                                case "轨迹":
                                    mIntent = new Intent(SerchActivity.this, TaskMissionProjectActivity.class);
                                    mIntent.putExtra("activity", "TrajectoryListActivity");
                                    startActivity(mIntent);
                                    break;
                                case "全部":
                                    mIntent = new Intent(SerchActivity.this, AllActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "责令停止违法行为通知":
                                    mIntent = new Intent(SerchActivity.this, FromAllLawEnforcementActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "水行政当场处罚决定书":
                                    mIntent = new Intent(SerchActivity.this, FromAllPenaltyTheSpotActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "法律法规库":
                                    mIntent = new Intent(SerchActivity.this, FromAllLawsAndRegulationsActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "执法规范":
                                    mIntent = new Intent(SerchActivity.this, FromAllLawEnforcementSpecificationActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "预立案":
                                    mIntent = new Intent(SerchActivity.this, FromAllCaseInAdvanceActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "案例库":
                                    mIntent = new Intent(SerchActivity.this, PuttedForwardConActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "地图查询":
                                    mIntent = new Intent(SerchActivity.this, MapQueryActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "地图任务展示":
                                    mIntent = new Intent(SerchActivity.this, MapTaskActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "区域执法记录查询":
                                    mIntent = new Intent(SerchActivity.this, RegionalHistoryEnforcementActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "案件登记":
                                    mIntent = new Intent(SerchActivity.this, FromAllCaseRegistrationActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "案件受理":
                                    mIntent = new Intent(SerchActivity.this, FromAllOpenCasesActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "案件处理":
                                    mIntent = new Intent(SerchActivity.this, FromAllCaseProcessingListActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "案件转办":
                                    mIntent = new Intent(SerchActivity.this, FromAllCaseComplaintActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "修改密码":
                                    mIntent = new Intent(SerchActivity.this, ChangePasswordActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "意见反馈":
                                    mIntent = new Intent(SerchActivity.this, FeedbackActivity.class);
                                    startActivity(mIntent);
                                    break;
                                case "关于":
                                    mIntent = new Intent(SerchActivity.this, AboutActivity.class);
                                    startActivity(mIntent);
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

        } else {
            mSerchRecyAdapter.setNewData(seachBeans);
        }

    }

    private void initData() {
        mStringArray = getResources().getStringArray(R.array.menus);
        mRcySerch.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mRcySerch.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 20, getResources().getColor(R.color.activity_bg)));
        mGreenSearchHistories = GreenDAOManager.getInstence().getDaoSession().getGreenSearchHistoryDao()
                .queryBuilder().where(GreenSearchHistoryDao.Properties.UserId.eq(OkingContract.CURRENTUSER.getUserid()))
                .list();
        if (mGreenSearchHistories.size() > 0) {
            mTagAdapter = new TagAdapter<GreenSearchHistory>(mGreenSearchHistories) {
                @Override
                public View getView(FlowLayout parent, int position, GreenSearchHistory greenSearchHistory) {
                    View inflate = View.inflate(BaseApplication.getApplictaion(), R.layout.search_history_item, null);
                    TextView tvTitle = inflate.findViewById(R.id.tv_title);
                    tvTitle.setText(mGreenSearchHistories.get(position).getSearchText());
                    return inflate;
                }
            };
            mSeachFlowLayout.setAdapter(mTagAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @OnClick(R.id.tv_clear)
    public void onViewClicked() {
        if (mGreenSearchHistories.size() > 0) {
            GreenDAOManager.getInstence().getDaoSession().getGreenSearchHistoryDao().deleteAll();
            mGreenSearchHistories.clear();
            mTagAdapter.notifyDataChanged();

        }

    }
}
