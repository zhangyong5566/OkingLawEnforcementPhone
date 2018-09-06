package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.NoCompleteTaskListRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ToDoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recy_task)
    RecyclerView mRecyTask;
    @BindView(R.id.layoutSwipeRefresh)
    SwipeRefreshLayout mLayoutSwipeRefresh;
    @BindView(R.id.et_seach)
    EditText mEtSeach;
    private Unbinder mBind;
    private Intent mIntent;
    private NoCompleteTaskListRecyAdapter mNoCompleteTaskListRecyAdapter;
    private List<GreenMissionTask> mGreenMissionTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        mBind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
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
        mLayoutSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNoCompleteGreenMissionTasks();
                mNoCompleteTaskListRecyAdapter.setNewData(mGreenMissionTasks);
                mLayoutSwipeRefresh.setRefreshing(false);
            }
        });

        mNoCompleteTaskListRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GreenMissionTask greenMissionTask = (GreenMissionTask) adapter.getData().get(position);
                if (greenMissionTask.getExamine_status() == -1) {


                    switch (greenMissionTask.getStatus()) {
                        case "0":

                        case "1":
                            mIntent = new Intent(ToDoActivity.this, AuditActivity.class);
                            mIntent.putExtra("id", greenMissionTask.getId());
                            mIntent.putExtra("position", position);
                            startActivity(mIntent);
                            break;

                        case "2":

                            mIntent = new Intent(ToDoActivity.this, ArrangeTeamMembersActivity.class);
                            mIntent.putExtra("id", greenMissionTask.getId());
                            mIntent.putExtra("position", position);
                            startActivity(mIntent);

                            break;
                        case "3":
                        case "4":
                            mIntent = new Intent(ToDoActivity.this, MissionActivity.class);
                            mIntent.putExtra("id", greenMissionTask.getId());
                            mIntent.putExtra("position", position);
                            startActivity(mIntent);
                            break;
                        case "7":
                            mIntent = new Intent(ToDoActivity.this, AuditActivity.class);
                            mIntent.putExtra("id", greenMissionTask.getId());
                            mIntent.putExtra("position", position);

                            startActivity(mIntent);
                            break;
                        case "100":
                        case "5":

                        case "9":
                            mIntent = new Intent(ToDoActivity.this, MissionRecorActivity.class);
                            mIntent.putExtra("id", greenMissionTask.getId());
                            mIntent.putExtra("taskId", greenMissionTask.getTaskid());
                            startActivity(mIntent);
                            break;
                        default:
                            break;
                    }

                } else {
                    if (greenMissionTask.getApproved_person().equals(OkingContract.CURRENTUSER.getUserid())) {
                        //领导批示
                        mIntent = new Intent(ToDoActivity.this, ApprovalActivity.class);
                        mIntent.putExtra("id", greenMissionTask.getId());
                        mIntent.putExtra("position", position);
                        mIntent.putExtra("taskId", greenMissionTask.getTaskid());
                        startActivity(mIntent);
                    } else {
                        mIntent = new Intent(ToDoActivity.this, MissionRecorActivity.class);
                        mIntent.putExtra("id", greenMissionTask.getId());
                        mIntent.putExtra("taskId", greenMissionTask.getTaskid());
                        startActivity(mIntent);
                    }


                }

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
                    mNoCompleteTaskListRecyAdapter.setNewData(mGreenMissionTasks);
                }
            }
        });


        mEtSeach.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ToDoActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    String trim = mEtSeach.getText().toString().trim();
                    if (!TextUtils.isEmpty(trim)) {
                        List<GreenMissionTask> greenMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()), GreenMissionTaskDao.Properties.Task_name.like("%" + trim + "%"))
                                .whereOr(GreenMissionTaskDao.Properties.Examine_status.eq(6), GreenMissionTaskDao.Properties.Examine_status.eq(5), GreenMissionTaskDao.Properties.Examine_status.eq(4), GreenMissionTaskDao.Properties.Examine_status.eq(2), GreenMissionTaskDao.Properties.Examine_status.eq(1), GreenMissionTaskDao.Properties.Examine_status.eq(0), GreenMissionTaskDao.Properties.Status.eq("1"), GreenMissionTaskDao.Properties.Status.eq("2"), GreenMissionTaskDao.Properties.Status.eq("3"), GreenMissionTaskDao.Properties.Status.eq("4"), GreenMissionTaskDao.Properties.Status.eq("7"), GreenMissionTaskDao.Properties.Status.eq("100"))
                                .list();
                        mNoCompleteTaskListRecyAdapter.setNewData(greenMissionTasks);
                    }


                }
                return false;
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent1(UpdateGreenMissionTaskOV event) {
        int type = event.getType();
        int position = event.getPosition();
        if (type == 100) {
            mNoCompleteTaskListRecyAdapter.remove(position);
        } else {

            mNoCompleteTaskListRecyAdapter.setData(position, event.getMissionTask());
        }

    }

    //'执行状态 0-未发布  1-已发布 2-已经审核 3-确认接受  4-任务开始  5-任务完成 7-不通过审核 9-退回修改(日志审核)';

    private void initData() {
        mRecyTask.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mRecyTask.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 20, getResources().getColor(R.color.activity_bg)));
        getNoCompleteGreenMissionTasks();
        mNoCompleteTaskListRecyAdapter = new NoCompleteTaskListRecyAdapter(R.layout.nocomplete_task_item, mGreenMissionTasks);
        mNoCompleteTaskListRecyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mRecyTask.setAdapter(mNoCompleteTaskListRecyAdapter);

    }

    private List<GreenMissionTask> getNoCompleteGreenMissionTasks() {
        mGreenMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                .whereOr(GreenMissionTaskDao.Properties.Examine_status.eq(6), GreenMissionTaskDao.Properties.Examine_status.eq(5), GreenMissionTaskDao.Properties.Examine_status.eq(4), GreenMissionTaskDao.Properties.Examine_status.eq(2), GreenMissionTaskDao.Properties.Examine_status.eq(1), GreenMissionTaskDao.Properties.Examine_status.eq(0), GreenMissionTaskDao.Properties.Status.eq("1"), GreenMissionTaskDao.Properties.Status.eq("2"), GreenMissionTaskDao.Properties.Status.eq("3"), GreenMissionTaskDao.Properties.Status.eq("4"), GreenMissionTaskDao.Properties.Status.eq("7"), GreenMissionTaskDao.Properties.Status.eq("100"))
                .list();
        return mGreenMissionTasks;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mBind.unbind();
    }
}
