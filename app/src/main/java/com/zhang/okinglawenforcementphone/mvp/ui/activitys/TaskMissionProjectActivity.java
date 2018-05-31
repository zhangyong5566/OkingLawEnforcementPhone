package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.ArrangeMissionRcyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLogDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TaskMissionProjectActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ry_arrange)
    RecyclerView ryArrange;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private Unbinder mBind;
    private List<GreenMissionTask> mGreenMissionTasks;
    private String argActivity;
    private ArrangeMissionRcyAdapter mAarrangeMissionRcyAdapter;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_mission_project);
        mBind = ButterKnife.bind(this);
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
        mAarrangeMissionRcyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            private GreenMissionTask mGreenMissionTask;

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (argActivity) {
                    case "ArrangeMissionActivity":
                        mIntent = new Intent(TaskMissionProjectActivity.this, ArrangeTeamMembersActivity.class);
                        mIntent.putExtra("id", mAarrangeMissionRcyAdapter.getData().get(position).getId());
                        mIntent.putExtra("position", position);
                        startActivity(mIntent);
                        break;
                    case "TaskExecutionActivity":
                    case "ReportTaskActivity":
                        mIntent = new Intent(TaskMissionProjectActivity.this, MissionActivity.class);
                        mIntent.putExtra("id", mAarrangeMissionRcyAdapter.getData().get(position).getId());
                        mIntent.putExtra("position", position);
                        startActivity(mIntent);
                        break;

                    case "TrajectoryListActivity":
                        mGreenMissionTask = mAarrangeMissionRcyAdapter.getData().get(position);
                        GreenMissionLog unique = GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao()
                                .queryBuilder().where(GreenMissionLogDao.Properties.Task_id.eq(mGreenMissionTask.getTaskid())).unique();
                        if (unique == null || unique.getLocJson() == null|| unique.getLocJson().equals("[]")|| TextUtils.isEmpty(unique.getLocJson())) {
                            RxToast.error("抱歉后台哥们不给力，木有轨迹返回给我~~~~");
                        } else {
                            mIntent = new Intent(TaskMissionProjectActivity.this, TrajectoryActivity.class);
                            mIntent.putExtra("id", mAarrangeMissionRcyAdapter.getData().get(position).getTaskid());
                            mIntent.putExtra("taskName", mGreenMissionTask.getTask_name());
                            mIntent.putExtra("publishname", mGreenMissionTask.getPublisher_name());
                            mIntent.putExtra("executeBeginTime", mGreenMissionTask.getExecute_start_time());
                            mIntent.putExtra("executeEndTime", mGreenMissionTask.getExecute_end_time());
                            mIntent.putExtra("area", mGreenMissionTask.getTask_area());
                            mIntent.putExtra("locJson", unique.getLocJson());
                            startActivity(mIntent);
                        }

                        break;
                    case "CompleteListActivity":
                        mGreenMissionTask = mAarrangeMissionRcyAdapter.getData().get(position);
                        Intent intent = new Intent(TaskMissionProjectActivity.this, MissionRecorActivity.class);
                        intent.putExtra("id", mGreenMissionTask.getId());
                        intent.putExtra("taskId", mGreenMissionTask.getTaskid());
                        startActivity(intent);

                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void initData() {
        Intent intent = getIntent();
        argActivity = intent.getStringExtra("activity");
        EventBus.getDefault().register(this);

        switch (argActivity) {
            case "ArrangeMissionActivity":
                mTvTitle.setText("待安排队员列表");
                Schedulers.io().createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        mGreenMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()), GreenMissionTaskDao.Properties.Status.eq(2))
                                .list();
                        if (mGreenMissionTasks.size() > 0) {
                            AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    ryArrange.setVisibility(View.VISIBLE);
                                    tv.setVisibility(View.GONE);
                                    mAarrangeMissionRcyAdapter.setNewData(mGreenMissionTasks);

                                }
                            });
                        } else {
                            AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    ryArrange.setVisibility(View.GONE);
                                    tv.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    }
                });
                break;
            case "TaskExecutionActivity":
                mTvTitle.setText("待执行任务列表");
                mGreenMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                        .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                        .whereOr(GreenMissionTaskDao.Properties.Status.eq(3), GreenMissionTaskDao.Properties.Status.eq(4))
                        .list();
                if (mGreenMissionTasks.size() > 0) {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            ryArrange.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.GONE);
                            mAarrangeMissionRcyAdapter.setNewData(mGreenMissionTasks);

                        }
                    });
                } else {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            ryArrange.setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                        }
                    });

                }
                break;

            case "ReportTaskActivity":
                mTvTitle.setText("待上报任务列表");
                mGreenMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                        .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()), GreenMissionTaskDao.Properties.Status.eq(100))
                        .list();
                if (mGreenMissionTasks.size() > 0) {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            ryArrange.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.GONE);
                            mAarrangeMissionRcyAdapter.setNewData(mGreenMissionTasks);

                        }
                    });
                } else {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            ryArrange.setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                        }
                    });

                }
                break;

            case "TrajectoryListActivity":
                mTvTitle.setText("任务轨迹列表");
                QueryBuilder<GreenMissionTask> greenMissionTaskQueryBuilder = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                        .queryBuilder();
                greenMissionTaskQueryBuilder.where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()));
                greenMissionTaskQueryBuilder.whereOr(GreenMissionTaskDao.Properties.Status.eq(100), GreenMissionTaskDao.Properties.Status.eq(5));
                mGreenMissionTasks = greenMissionTaskQueryBuilder.list();
                if (mGreenMissionTasks.size() > 0) {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            ryArrange.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.GONE);
                            mAarrangeMissionRcyAdapter.setNewData(mGreenMissionTasks);

                        }
                    });
                } else {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            ryArrange.setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                        }
                    });

                }
                break;
            case "CompleteListActivity":
                mTvTitle.setText("任务完成列表");
                mGreenMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                        .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()), GreenMissionTaskDao.Properties.Status.eq(5)).list();
                if (mGreenMissionTasks.size() > 0) {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            ryArrange.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.GONE);
                            mAarrangeMissionRcyAdapter.setNewData(mGreenMissionTasks);

                        }
                    });
                } else {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            ryArrange.setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                        }
                    });

                }
                break;
            default:
                break;
        }

    }

    private void initView() {
        ryArrange.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ryArrange.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 8, BaseApplication.getApplictaion().getResources().getColor(R.color.activity_bg)));
        mAarrangeMissionRcyAdapter = new ArrangeMissionRcyAdapter(R.layout.arrangemission_task_item, null);
        mAarrangeMissionRcyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        ryArrange.setAdapter(mAarrangeMissionRcyAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent1(UpdateGreenMissionTaskOV event) {

        mAarrangeMissionRcyAdapter.setData(event.getPosition(), event.getMissionTask());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        EventBus.getDefault().unregister(this);
    }
}
