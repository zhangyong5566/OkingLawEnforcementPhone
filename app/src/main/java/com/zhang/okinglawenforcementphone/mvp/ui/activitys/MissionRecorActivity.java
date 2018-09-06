package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.DefaultContants;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.ActivityUtil;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.baselib.utils.NetUtil;
import com.zhang.baselib.utils.Util;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.OkingJPushManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.NavViewRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLogDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.JPushMessageBean;
import com.zhang.okinglawenforcementphone.beans.NavBean;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.Point;
import com.zhang.okinglawenforcementphone.beans.RecordLogOV;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.GetHttpMissionLogContract;
import com.zhang.okinglawenforcementphone.mvp.contract.JPushMessageContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UpdateMissionStateContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadJobLogContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadJobLogForPicContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadSignaturePicContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadVideoContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.GetHttpMissionLogPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.UpdateMissionStatePresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadJobLogForPicPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadJobLogPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadSignaturePicPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadVideoPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.TaskInfoFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.TaskPatrolFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.TaskPicFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.TaskProcessingResultFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.TaskVideoFragment;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MissionRecorActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_savetag)
    TextView mTvSavetag;
    @BindView(R.id.nav_view)
    RecyclerView mNavView;
    @BindView(R.id.fab)
    ImageView mFab;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.report_mission_button)
    Button mReportMissionButton;
    @BindView(R.id.complete_mission_button)
    Button mCompleteMissionButton;
    private int atPosition = 0;
    private GreenMissionTask mission;
    private GreenMissionLog mGreenMissionLog;
    private RxDialogLoading mRxDialogLoading;
    private PowerManager.WakeLock mWakeLock;
    private Handler mHandler = new Handler();
    private int picComPostion = 0;
    private int veodComPosion = 0;
    private int logSignPosion = 0;
    private List<GreenMedia> mPhotoMedias = new ArrayList<>();
    private List<GreenMedia> mVideoMedias = new ArrayList<>();
    private boolean uploadLogPic = false, uploadSignPic = false, uploadLogVideo = false;
    private UploadJobLogPresenter mUploadJobLogPresenter;
    private UploadJobLogForPicPresenter mUploadJobLogForPicPresenter;
    private UploadSignaturePicPresenter mUploadSignaturePicPresenter;
    private UploadVideoPresenter mUploadVideoPresenter;
    private Map<String, RequestBody> photoParams;
    private SimpleDateFormat sdfVideo = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private int mDatePoor;
    private long mBeforTime;
    private String mLocJson;
    private Gson mGson = new Gson();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private BroadcastReceiver mNetWokReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean connected = NetUtil.isConnected(BaseApplication.getApplictaion());
            if (connected) {
                RxToast.error(BaseApplication.getApplictaion(), "连接", Toast.LENGTH_SHORT).show();

            } else {
                if (mRxDialogLoading != null) {
                    RxToast.error(BaseApplication.getApplictaion(), "网络断开了~~请检查网络再进行提交数据", Toast.LENGTH_SHORT).show();
                    mRxDialogLoading.cancel();
                }

            }
        }
    };
    private Unbinder mBind;
    private String mTaskId;
    private GetHttpMissionLogPresenter mGetHttpMissionLogPresenter;
    private RxDialogSureCancel mRxDialogSureCancel;
    private NavViewRecyAdapter mNavViewRecyAdapter;
    private TaskInfoFragment mTaskInfoFragment;
    private TaskPatrolFragment mTaskPatrolFragment;
    private TaskProcessingResultFragment mTaskProcessingResultFragment;
    private TaskPicFragment mTaskPicFragment;
    private TaskVideoFragment mTaskVideoFragment;
    private Subscription mSubscription;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private boolean mCanSaveComplete = false;
    private boolean mSummarySwisopen;
    private boolean mLeaderSummarySwisopen;
    private Handler mainHandler;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_recor);
        mBind = ButterKnife.bind(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        initView();
        initData();
        setListerner();
    }

    private void setListerner() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mission.getStatus().equals("5")) {
                    finish();
                } else {
                    saveTheRecord();
                    finish();
                }

            }
        });

        mNavViewRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {

                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        saveTheRecord();
                        e.onNext(200);
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        atPosition = position;

                        switch (position) {
                            case 0:                     //基本信息
                                mTvTitle.setText("基本信息");
                                if (mTaskPatrolFragment != null) {
                                    fragmentTransaction.hide(mTaskPatrolFragment);
                                }
                                if (mTaskProcessingResultFragment != null) {

                                    fragmentTransaction.hide(mTaskProcessingResultFragment);
                                }
                                if (mTaskPicFragment != null) {

                                    fragmentTransaction.hide(mTaskPicFragment);
                                }
                                if (mTaskVideoFragment != null) {

                                    fragmentTransaction.hide(mTaskVideoFragment);
                                }
                                if (mTaskInfoFragment != null) {
                                    fragmentTransaction.show(mTaskInfoFragment);
                                } else {
                                    mTaskInfoFragment = TaskInfoFragment.newInstance(null, null);
                                    mTaskInfoFragment.setMission(mission);
                                    mTaskInfoFragment.setGreenMissionLog(mGreenMissionLog);
                                    fragmentTransaction.add(R.id.rl_mision, mTaskInfoFragment, "TaskInfoFragment");
                                }
                                fragmentTransaction.commitAllowingStateLoss();

                                break;
                            case 1:                     //巡查情况
                                mTvTitle.setText("巡查情况");
                                if (mTaskInfoFragment != null) {

                                    fragmentTransaction.hide(mTaskInfoFragment);
                                }
                                if (mTaskProcessingResultFragment != null) {

                                    fragmentTransaction.hide(mTaskProcessingResultFragment);
                                }
                                if (mTaskPicFragment != null) {

                                    fragmentTransaction.hide(mTaskPicFragment);
                                }
                                if (mTaskVideoFragment != null) {

                                    fragmentTransaction.hide(mTaskVideoFragment);
                                }
                                if (mTaskPatrolFragment != null) {
                                    fragmentTransaction.show(mTaskPatrolFragment);
                                } else {
                                    mTaskPatrolFragment = TaskPatrolFragment.newInstance(null, null);
                                    mTaskPatrolFragment.setMission(mission);
                                    mTaskPatrolFragment.setGreenMissionLog(mGreenMissionLog);
                                    fragmentTransaction.add(R.id.rl_mision, mTaskPatrolFragment, "TaskPatrolFragment");

                                }
                                fragmentTransaction.commitAllowingStateLoss();

                                break;
                            case 2:                     //处理结果
                                switchResults(fragmentTransaction);

                                break;
                            case 3:                     //拍照
                                mTvTitle.setText("拍照");
                                if (mTaskInfoFragment != null) {

                                    fragmentTransaction.hide(mTaskInfoFragment);
                                }
                                if (mTaskPatrolFragment != null) {

                                    fragmentTransaction.hide(mTaskPatrolFragment);
                                }
                                if (mTaskProcessingResultFragment != null) {

                                    fragmentTransaction.hide(mTaskProcessingResultFragment);
                                }
                                if (mTaskVideoFragment != null) {

                                    fragmentTransaction.hide(mTaskVideoFragment);
                                }
                                if (mTaskPicFragment != null) {
                                    fragmentTransaction.show(mTaskPicFragment);
                                } else {
                                    mTaskPicFragment = TaskPicFragment.newInstance(null, null);
                                    mTaskPicFragment.setMission(mission);
                                    mTaskPicFragment.setGreenMissionLog(mGreenMissionLog);
                                    fragmentTransaction.add(R.id.rl_mision, mTaskPicFragment, "TaskPicFragment");

                                }
                                fragmentTransaction.commitAllowingStateLoss();
                                break;
                            case 4:                     //录视频
                                mTvTitle.setText("录视频");
                                if (mTaskInfoFragment != null) {

                                    fragmentTransaction.hide(mTaskInfoFragment);
                                }
                                if (mTaskPatrolFragment != null) {

                                    fragmentTransaction.hide(mTaskPatrolFragment);
                                }
                                if (mTaskProcessingResultFragment != null) {

                                    fragmentTransaction.hide(mTaskProcessingResultFragment);
                                }
                                if (mTaskPicFragment != null) {

                                    fragmentTransaction.hide(mTaskPicFragment);
                                }
                                if (mTaskVideoFragment != null) {
                                    fragmentTransaction.show(mTaskVideoFragment);
                                } else {
                                    mTaskVideoFragment = TaskVideoFragment.newInstance(null, null);
                                    mTaskVideoFragment.setMission(mission);
                                    mTaskVideoFragment.setGreenMissionLog(mGreenMissionLog);
                                    fragmentTransaction.add(R.id.rl_mision, mTaskVideoFragment, "TaskVideoFragment");

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
        });
    }

    private void switchResults(FragmentTransaction fragmentTransaction) {
        mTvTitle.setText("处理结果");
        if (mTaskInfoFragment != null) {

            fragmentTransaction.hide(mTaskInfoFragment);
        }
        if (mTaskPatrolFragment != null) {

            fragmentTransaction.hide(mTaskPatrolFragment);
        }
        if (mTaskPicFragment != null) {

            fragmentTransaction.hide(mTaskPicFragment);
        }
        if (mTaskVideoFragment != null) {

            fragmentTransaction.hide(mTaskVideoFragment);
        }
        if (mTaskProcessingResultFragment != null) {
            mTaskProcessingResultFragment.setMission(mission);
            fragmentTransaction.show(mTaskProcessingResultFragment);
        } else {
            mTaskProcessingResultFragment = TaskProcessingResultFragment.newInstance(null, null);
            mTaskProcessingResultFragment.setMission(mission);
            mTaskProcessingResultFragment.setGreenMissionLog(mGreenMissionLog);
            fragmentTransaction.add(R.id.rl_mision, mTaskProcessingResultFragment, "TaskProcessingResultFragment");

        }
        fragmentTransaction.commitAllowingStateLoss();
    }


    private void initData() {
        BaseApplication.getApplictaion().registerReceiver(mNetWokReceiver, new IntentFilter("oking.network"));
        //定时保存
        if (mission != null) {
            Flowable.interval(5, 10, TimeUnit.SECONDS)
                    .onBackpressureDrop()
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onSubscribe(Subscription s) {
                            mSubscription = s;
                            s.request(Long.MAX_VALUE);
                        }

                        @Override
                        public void onNext(Long aLong) {
                            //判断当前显示的是哪个页面
                            if (mission.getStatus().equals("5") || mission.getStatus().equals("100")) {

                            } else {
                                switch (atPosition) {
                                    case 0:
                                        Log.i("Oking5", "保存基本信息");
                                        saveTaskInfo();
                                        break;
                                    case 1:
                                        Log.i("Oking5", "保存巡查情况");
                                        savePatrol();
                                        break;
                                    case 2:
                                        Log.i("Oking5", "保存处理结果");
                                        saveResults();
                                        break;
                                    case 3:
                                        Log.i("Oking5", "保存拍照");     //不用再去调用保存操作，每次拍照都保存了
                                        break;
                                    case 4:
                                        Log.i("Oking5", "保存视频");
                                        break;
                                    default:
                                        break;
                                }
                                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTvSavetag.setText("上次保存时间：" + mSimpleDateFormat.format(System.currentTimeMillis()));
                                    }
                                });
                            }


                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }

    private void saveResults() {
        mTaskProcessingResultFragment.saveResults();
    }

    private void savePatrol() {
        mTaskPatrolFragment.savePatrol();
    }

    private void saveTaskInfo() {
        mTaskInfoFragment.saveTaskInfo();

    }

    private void initView() {

        ArrayList<NavBean> navBeans = new ArrayList<>();
        NavBean navBean1 = new NavBean();
        navBean1.setIcon(R.mipmap.icon_taskinfo);
        navBean1.setTitle("基本信息");
        navBeans.add(navBean1);
        NavBean navBean2 = new NavBean();
        navBean2.setIcon(R.mipmap.icon_taskpatrl);
        navBean2.setTitle("巡查情况");
        navBeans.add(navBean2);
        NavBean navBean3 = new NavBean();
        navBean3.setIcon(R.mipmap.icon_result);
        navBean3.setTitle("处理结果");
        navBeans.add(navBean3);
        NavBean navBean4 = new NavBean();
        navBean4.setIcon(R.mipmap.icon_tagpic);
        navBean4.setTitle("拍照");
        navBeans.add(navBean4);
        NavBean navBean5 = new NavBean();
        navBean5.setIcon(R.mipmap.icon_tagvideo);
        navBean5.setTitle("录视频");
        navBeans.add(navBean5);
        mNavViewRecyAdapter = new NavViewRecyAdapter(R.layout.navview_item, navBeans);
        mNavViewRecyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mNavView.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mNavView.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 1, Color.DKGRAY));
        mNavView.setAdapter(mNavViewRecyAdapter);

        PowerManager pm = (PowerManager) BaseApplication.getApplictaion().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                "MissionRecordFragment");
        mWakeLock.acquire();

        Intent intent = getIntent();
        mTaskId = intent.getStringExtra("taskId");
        mPosition = intent.getIntExtra("position", -1);


        if (mTaskId != null) {
            //网络获取Log(尝试用http获取服务器的Log，获取不了再单机生成新Log)
            getNetData(mTaskId);
        }


        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    private void getNetData(String taskId) {
        if (mRxDialogLoading == null) {
            mRxDialogLoading = new RxDialogLoading(this, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });
        }
        mRxDialogLoading.setLoadingText("初始化数据中，请稍等...");
        mRxDialogLoading.show();

        mission = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder().where(GreenMissionTaskDao.Properties.Taskid.eq(taskId)).unique();
        if (mGetHttpMissionLogPresenter == null) {
            mGetHttpMissionLogPresenter = new GetHttpMissionLogPresenter(new GetHttpMissionLogContract.View() {
                @Override
                public void loadHttpMissionLogSucc(GreenMissionLog greenMissionLog) {
                    mRxDialogLoading.cancel();
                    mGreenMissionLog = greenMissionLog;
                    initFragment();

                }

                @Override
                public void loadEmpty(GreenMissionLog greenMissionLog) {
                    mRxDialogLoading.cancel();
                    mGreenMissionLog = greenMissionLog;
                    initFragment();

                }
            });
        }
        if (mission != null) {

            mGetHttpMissionLogPresenter.getHttpMissionLog(mission);
        } else {
            mRxDialogLoading.cancel();
            finish();
        }


    }

    private void initFragment() {
        if (mCompleteMissionButton != null && mReportMissionButton != null) {
            if (mission.getStatus().equals("100")) {//任务已完成，不能再完成

                mCompleteMissionButton.setVisibility(View.GONE);

            } else if (mission.getStatus().equals("5")) {
                //任务已上报，不能再上报
                mReportMissionButton.setVisibility(View.GONE);
                mCompleteMissionButton.setVisibility(View.GONE);


            } else if (mission.getStatus().equals("9")) {//任务被退回修改


                mReportMissionButton.setVisibility(View.GONE);
                mCompleteMissionButton.setVisibility(View.GONE);

            }
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mTaskInfoFragment = TaskInfoFragment.newInstance(null, null);
        mTaskInfoFragment.setMission(mission);
        mTaskInfoFragment.setGreenMissionLog(mGreenMissionLog);
        fragmentTransaction.replace(R.id.rl_mision, mTaskInfoFragment, "TaskInfoFragment").commitAllowingStateLoss();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWakeLock == null) {
            // keep screen on
            PowerManager pm = (PowerManager) BaseApplication.getApplictaion().getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                    "MissionRecordFragment");
            mWakeLock.acquire();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getApplictaion().unregisterReceiver(mNetWokReceiver);
        if (mSubscription != null) {
            mSubscription.cancel();
            mSubscription = null;
        }

        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
        mBind.unbind();
    }


    public void invisiabelCompleteMissionButton() {
        if (mRxDialogSureCancel == null) {

            mRxDialogSureCancel = new RxDialogSureCancel(MissionRecorActivity.this);//提示弹窗
        }
        mRxDialogSureCancel.setContent("完成巡查后将停止记录巡查定位，是否继续？");
        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxToast.warning(BaseApplication.getApplictaion(), "请去签名!!", Toast.LENGTH_SHORT, true).show();

                mission.setExecute_end_time(System.currentTimeMillis());
                mission.setStatus("100");
                //把巡查轨迹插入本地数据库
                Schedulers.io().createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        String locationTrajectory = getLocationTrajectory();
                        Log.i("Oking1", ">>>>>>>>>>>>>" + locationTrajectory);

                        mGreenMissionLog.setLocJson(locationTrajectory);
                        GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().update(mGreenMissionLog);
                        GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mission);

                    }
                });

                if (DefaultContants.ISHTTPLOGIN) {
                    //改变任务状态
                    UpdateMissionStatePresenter updateMissionStatePresenter = new UpdateMissionStatePresenter(new UpdateMissionStateContract.View() {
                        @Override
                        public void updateMissionStateSucc(String result) {
                            Log.i("Oking", "巡查完毕" + result);
                            saveTheRecord();
                            mRxDialogSureCancel.cancel();
                        }

                        @Override
                        public void updateMissionStateFail(Throwable ex) {
                            Log.i("Oking", "巡查异常" + ex.getMessage());
                            saveTheRecord();
                            mRxDialogSureCancel.cancel();
                        }
                    });
                    updateMissionStatePresenter.updateMissionState(mission.getTaskid(), "",
                            OkingContract.SDF.format(mission.getExecute_end_time()), 4);

                }

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switchResults(fragmentTransaction);
                mCompleteMissionButton.setVisibility(View.GONE);
            }
        });
        mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxDialogSureCancel.cancel();
            }
        });
        mRxDialogSureCancel.show();
    }


    private String getLocationTrajectory() {
        Log.i("Oking1", ">>>>>>>>>>>>>遍历轨迹");
        Long beginTime = mission.getExecute_start_time();
        if (beginTime == null) {
            beginTime = System.currentTimeMillis() - 1000 * 60 * 20;
        }
        Long endTime = mission.getExecute_end_time();
        if (endTime == null) {
            endTime = System.currentTimeMillis();
            mission.setExecute_end_time(endTime);
        }
        mBeforTime = beginTime - 24 * 60 * 60 * 1000;
        final String file1 = sdf.format(beginTime);

        final ArrayList<Point> locationPath = new ArrayList<>();

        mDatePoor = Util.getDatePoor(beginTime, endTime);
        if (mDatePoor < 1) {        //表示在同一天
//                    Log.i("Oking","是同一天");
            List<String> locationPos = FileUtil.readFile2List(Environment.getExternalStorageDirectory() + "/oking/location/" + file1 + ".txt", "UTF-8");
            if (locationPos != null) {
                for (String s : locationPos) {
                    String[] items = s.split(",");
                    if (items.length != 3) {
                        continue;
                    }

                    String mLatitude = items[0];
                    String mLongitude = items[1];
                    String mDatetime = items[2];

                    if (Long.parseLong(mDatetime) > beginTime && Long.parseLong(mDatetime) < endTime) {
                        Point location = new Point();
                        location.setLatitude(Double.valueOf(mLatitude));
                        location.setLongitude(Double.valueOf(mLongitude));
                        location.setDatetime(Long.valueOf(mDatetime));
                        locationPath.add(location);
                    }
                }
            }


        } else {

            for (int i = 0; i <= mDatePoor; i++) {
                File file = new File(Environment.getExternalStorageDirectory() + "/oking/location/" + getAfterData(mBeforTime) + ".txt");

                if (file.exists()) {
//                            Log.i("Oking","不是同一天"+file.getName());
                    List<String> locationPos = FileUtil.readFile2List(file, "UTF-8");
                    for (String s : locationPos) {
                        String[] items = s.split(",");
                        if (items.length != 3) {
                            continue;
                        }

                        String Latitude = items[0];
                        String Longitude = items[1];
                        String datetime = items[2];

                        if (Long.parseLong(datetime) > beginTime && Long.parseLong(datetime) < endTime) {

                            Point location = new Point();
                            location.setLatitude(Double.valueOf(Latitude));
                            location.setLongitude(Double.valueOf(Longitude));
                            location.setDatetime(Long.valueOf(datetime));
                            locationPath.add(location);
                        }
                    }

                }

            }
        }

        //筛选一下，不然点集太多
        if (locationPath.size() > 100) {
            ArrayList<Point> newLocationPath = new ArrayList<>();
            for (int i = 0; i < locationPath.size(); i = i + 2) {

                newLocationPath.add(locationPath.get(i));
            }

            mLocJson = mGson.toJson(newLocationPath);

        } else {

            mLocJson = mGson.toJson(locationPath);
        }

        return mLocJson;
    }

    @OnClick({R.id.report_mission_button, R.id.complete_mission_button, R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                } else {

                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.report_mission_button:                //上报任务
                reportingTasks();
                break;
            case R.id.complete_mission_button:              //完成巡查
                invisiabelCompleteMissionButton();
                break;
            default:
                break;
        }
    }

    private void reportingTasks() {
        if (mRxDialogSureCancel == null) {

            mRxDialogSureCancel = new RxDialogSureCancel(MissionRecorActivity.this);//提示弹窗
        }
        mRxDialogSureCancel.setContent("上报任务后不能修改日志，是否继续？");
        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubscription.cancel();
                saveTheRecord();
                GreenMissionLog unique = GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().queryBuilder().where(GreenMissionLogDao.Properties.Task_id.eq(mTaskId)).unique();
                if (!mission.getStatus().equals("100") &&
                        !mission.getStatus().equals("9")) {
                    RxToast.warning(BaseApplication.getApplictaion(), "任务未完成，不能上报", Toast.LENGTH_SHORT, true).show();

                    return;
                }

                if (mission.getMembers().size() > 0) {
                    for (GreenMember greenMember : mission.getMembers()) {
                        String signPic = greenMember.getSignPic();
                        if (signPic != null) {
                            File file = new File(signPic);
                            if (signPic != null && file.exists()) {
                                mCanSaveComplete = true;

                            } else {
                                mCanSaveComplete = false;
                                RxToast.warning(BaseApplication.getApplictaion(), "存在成员未签名，不能上报任务！", Toast.LENGTH_SHORT, true).show();
                                return;
                            }
                        } else {
                            mCanSaveComplete = false;
                            RxToast.warning(BaseApplication.getApplictaion(), "存在成员未签名，不能上报任务！", Toast.LENGTH_SHORT, true).show();
                            return;
                        }


                    }
                }


                if (mCanSaveComplete) {
                    mSummarySwisopen = unique.getSummarySwisopen();
                    mLeaderSummarySwisopen = unique.getLeaderSummarySwisopen();
                    String patrol = unique.getPatrol();
                    String dzyj = unique.getDzyj();
                    if (!mSummarySwisopen && TextUtils.isEmpty(patrol)) {
                        RxToast.warning(BaseApplication.getApplictaion(), "巡查情况未填写，不能上报任务！", Toast.LENGTH_SHORT, true).show();

                        return;
                    }

                    if (!mLeaderSummarySwisopen && TextUtils.isEmpty(dzyj)) {
                        RxToast.warning(BaseApplication.getApplictaion(), "处理结果未填写，不能上报任务！", Toast.LENGTH_SHORT, true).show();
                        return;
                    }

                    if (mRxDialogLoading == null) {

                        mRxDialogLoading = new RxDialogLoading(MissionRecorActivity.this, false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                dialogInterface.cancel();
                            }
                        });
                    }
                    unique.resetGreenMedia();
                    List<GreenMedia> greenMedias = unique.getGreenMedia();
                    for (GreenMedia media : greenMedias) {
                        if (media.getType() == 1) {            //1表示日志图片
                            mPhotoMedias.add(media);
                        } else if (media.getType() == 2) {      //2表示视频
                            mVideoMedias.add(media);
                        } else if (media.getType() == 3) {
                            //3表示语音
                        } else {
                            //签名图片
                        }

                    }

                    mRxDialogSureCancel.cancel();
                    mRxDialogLoading.setLoadingText("上传数据中...图片：" + picComPostion + "/" + mPhotoMedias.size() + "视频：" + veodComPosion + "/" + mVideoMedias.size());
                    mRxDialogLoading.show();

                    picComPostion = 0;
                    veodComPosion = 0;
                    logSignPosion = 0;

                    //上传数据
                    httpSaveRecord(unique);

                } else {
                    RxToast.warning(BaseApplication.getApplictaion(), "存在成员未签名，不能上报任务！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

            }
        });
        mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxDialogSureCancel.cancel();
            }
        });
        mRxDialogSureCancel.show();


    }

    private void httpSaveRecord(GreenMissionLog unique) {
        RecordLogOV recordLogOV = new RecordLogOV();
        recordLogOV.setArea(mission.getRwqyms());
        recordLogOV.setEquipment(unique.getEquipment());
        recordLogOV.setGreenMissionLog(mGreenMissionLog);
        recordLogOV.setGreenMissionTask(mission);
        recordLogOV.setLeaderSummary(unique.getDzyj());

        if (mSummarySwisopen || mLeaderSummarySwisopen) {
            recordLogOV.setSwisopen(true);
        } else {
            recordLogOV.setSwisopen(false);
        }
        recordLogOV.setParts(unique.getOther_part());
        recordLogOV.setSeleMattersPos(unique.getItem());
        recordLogOV.setSelePlanPos(unique.getPlan());
        recordLogOV.setSummary(unique.getPatrol());

        recordLogOV.setTime(OkingContract.SDF.format(System.currentTimeMillis()));
        if (mUploadJobLogPresenter == null) {
            mUploadJobLogPresenter = new UploadJobLogPresenter(new UploadJobLogContract.View() {
                @Override
                public void uploadJobLogSucc(String result) {
                    Log.i("Oking", result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            String serverId = jsonObject.getString("id");
                            mGreenMissionLog.setServer_id(serverId);
                            GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().update(mGreenMissionLog);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (mPhotoMedias.size() > 0) {
                        //上传巡查日志的图片
                        uploadPic();
                    } else {
                        uploadLogPic = true;
                    }


                    //上传签名图片
                    uploadSignedPic();

                    //上传巡查视频
                    if (mVideoMedias.size() > 0) {
                        uploadVideo();
                    } else {
                        uploadLogVideo = true;
                    }

                }

                @Override
                public void uploadJobLogFail(Throwable ex) {
                    Log.i("Oking", "异常：" + ex.toString());
                    if (mRxDialogLoading != null) {
                        mRxDialogLoading.cancel();
                    }
                    RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！20", Toast.LENGTH_SHORT, true).show();

                }
            });
        }
        mUploadJobLogPresenter.uploadJobLog(recordLogOV, mGson);

    }

    private void uploadVideo() {
        photoParams = new HashMap<>();
        if (mUploadVideoPresenter == null) {
            mUploadVideoPresenter = new UploadVideoPresenter(new UploadVideoContract.View() {
                @Override
                public void loadVideoSucc(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int code = object.getInt("code");
                        if (code == 200) {
                            veodComPosion++;
                            mRxDialogLoading.getTextView().setText("上传数据中...图片：" + picComPostion + "/" + mPhotoMedias.size() + "视频：" + veodComPosion + "/" + mVideoMedias.size());

                            if (veodComPosion == mVideoMedias.size()) {
                                uploadLogVideo = true;
                                checkChangeState();
                            }
                        } else {
                            if (mRxDialogLoading != null) {
                                mRxDialogLoading.cancel();
                            }
                            RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！1", Toast.LENGTH_SHORT, true).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (mRxDialogLoading != null) {
                            mRxDialogLoading.cancel();
                        }
                        RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！2", Toast.LENGTH_SHORT, true).show();

                    }

                }

                @Override
                public void uploadRetry(Throwable ex) {
                    veodComPosion = 0;
                    RxToast.warning("网络有点开小差~~正在努力重试！！");
                }

                @Override
                public void loadVideoFail(Throwable ex) {

                }

                @Override
                public void uploadIsCount(int pos) {
                    veodComPosion = pos;
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {

                            mRxDialogLoading.getTextView().setText("上传数据中...图片：" + picComPostion + "/" + mPhotoMedias.size() + "视频：" + veodComPosion + "/" + mVideoMedias.size());

                            if (veodComPosion == mVideoMedias.size()) {
                                uploadLogVideo = true;
                                checkChangeState();
                            }
                        }
                    });

                }
            });

        }
        mUploadVideoPresenter.uploadVideo(mGreenMissionLog, mVideoMedias, photoParams, sdfVideo, mGson);

    }

    private void uploadSignedPic() {
        photoParams = new HashMap<>();
        if (mUploadSignaturePicPresenter == null) {
            mUploadSignaturePicPresenter = new UploadSignaturePicPresenter(new UploadSignaturePicContract.View() {
                @Override
                public void uploadSignaturePicSucc(String result) {
                    Log.i("Oking1", "签名上传成功回掉》》》》》》》》》》》》》》》》" + result);
                    try {
                        JSONObject object = new JSONObject(result);
                        int code = object.getInt("code");
                        if (code == 200) {
                            logSignPosion++;
                            if (logSignPosion == mission.getMembers().size()) {
                                uploadSignPic = true;
                                checkChangeState();
                            }
                        } else {
                            if (mRxDialogLoading != null) {
                                mRxDialogLoading.cancel();
                            }
                            RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！14", Toast.LENGTH_SHORT, true).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (mRxDialogLoading != null) {
                            mRxDialogLoading.cancel();
                        }
                        RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！15", Toast.LENGTH_SHORT, true).show();
                    }

                }

                @Override
                public void uploadIsCount(int pos) {
                    logSignPosion = pos;
                    if (logSignPosion == mission.getMembers().size()) {
                        uploadSignPic = true;
                        checkChangeState();
                    }

                }

                @Override
                public void uploadRetry(Throwable ex) {
                    logSignPosion = 0;
                    RxToast.warning("网络有点开小差~~正在努力重试！！");
                }

                @Override
                public void uploadSignatureFail(Throwable ex) {
                    if (mRxDialogLoading != null) {
                        mRxDialogLoading.cancel();
                    }
                    RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！16", Toast.LENGTH_SHORT, true).show();

                }
            });

        }
        mUploadSignaturePicPresenter.uploadSignaturePic(mGreenMissionLog, mission, photoParams);

    }

    private void uploadPic() {
        photoParams = new HashMap<>();
        if (mUploadJobLogForPicPresenter == null) {
            mUploadJobLogForPicPresenter = new UploadJobLogForPicPresenter(new UploadJobLogForPicContract.View() {
                @Override
                public void uploadSucc(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int code = object.getInt("code");
                        if (code == 200) {
                            picComPostion++;
                            mRxDialogLoading.getTextView().setText("上传数据中...图片：" + picComPostion + "/" + mPhotoMedias.size() + "视频：" + veodComPosion + "/" + mVideoMedias.size());
                            if (picComPostion == mPhotoMedias.size()) {
                                uploadLogPic = true;
                                checkChangeState();
                            }
                        } else {
                            if (mRxDialogLoading != null) {
                                mRxDialogLoading.cancel();
                            }
                            RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！1", Toast.LENGTH_SHORT, true).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (mRxDialogLoading != null) {
                            mRxDialogLoading.cancel();
                        }
                        RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！2", Toast.LENGTH_SHORT, true).show();

                    }

                }

                @Override
                public void uploadRetry(Throwable ex) {
                    picComPostion = 0;
                    RxToast.warning("网络有点开小差~~正在努力重试！！");
                }

                @Override
                public void uploadFail(Throwable ex) {
                    if (mRxDialogLoading != null) {
                        mRxDialogLoading.cancel();
                    }
                    Log.i("Oking", "上传失败，请稍后重试！3" + ex.toString());
                    RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！3", Toast.LENGTH_SHORT, true).show();

                }

                @Override
                public void uploadIsCount(int pos) {
                    picComPostion = pos;
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            mRxDialogLoading.getTextView().setText("上传数据中...图片：" + picComPostion + "/" + mPhotoMedias.size() + "视频：" + veodComPosion + "/" + mVideoMedias.size());

                            if (picComPostion == mPhotoMedias.size()) {
                                uploadLogPic = true;
                                checkChangeState();
                            }
                        }
                    });
                }

                @Override
                public void uploadPositionFail(Throwable ex) {
                    Log.i("Oking", "位置数据解析异常" + ex.toString());
                    RxToast.error(BaseApplication.getApplictaion(), "位置数据解析异常", Toast.LENGTH_SHORT, true).show();

                }
            });
        }
        mUploadJobLogForPicPresenter.uploadJobLogForPic(mGson, photoParams, mGreenMissionLog, mPhotoMedias);

    }

    private void checkChangeState() {
        Log.i("Oking", uploadLogPic + "----uploadLogPic");
        Log.i("Oking", uploadSignPic + "----uploadSignPic");
        Log.i("Oking", uploadLogVideo + "----uploadLogVideo");
        if (uploadLogPic && uploadSignPic && uploadLogVideo) {
            httpCompleteMission();
        }

    }

    private void httpCompleteMission() {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .updateMissionRecordState(mGreenMissionLog.getServer_id(), 1)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                        return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                                .updateMissionState(mission.getTaskid(), "", "", 5);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        if (mRxDialogLoading != null) {
                            mRxDialogLoading.cancel();
                        }

                        String string = responseBody.string();
                        mission.setStatus("5");
                        mission.setExamine_status(0);
                        GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mission);
                        UpdateGreenMissionTaskOV greenMissionOV = new UpdateGreenMissionTaskOV();
                        greenMissionOV.setMissionTask(mission);
                        greenMissionOV.setPosition(mPosition);
                        EventBus.getDefault().post(greenMissionOV);


                        //发送一个远程通知
                        JPushMessageBean jPushMessageBean = new JPushMessageBean();
                        JPushMessageBean.AudienceBean audienceBean = new JPushMessageBean.AudienceBean();
                        ArrayList<String> alias = new ArrayList<>();
                        alias.add(mission.getApproved_person());
                        audienceBean.setAlias(alias);
                        jPushMessageBean.setAudience(audienceBean);
                        JPushMessageBean.NotificationBean notificationBean = new JPushMessageBean.NotificationBean();
                        notificationBean.setAlert("新消息：" + mission.getTask_name());
                        JPushMessageBean.NotificationBean.AndroidBean androidBean = new JPushMessageBean.NotificationBean.AndroidBean();
                        JPushMessageBean.NotificationBean.AndroidBean.ExtrasBean extrasBean = new JPushMessageBean.NotificationBean.AndroidBean.ExtrasBean();
                        extrasBean.setOpenType("2");
                        extrasBean.setTaskid(mission.getTaskid());
                        androidBean.setExtras(extrasBean);
                        notificationBean.setAndroid(androidBean);
                        ArrayList<String> platforms = new ArrayList<>();
                        platforms.add("android");
                        jPushMessageBean.setPlatform(platforms);
                        jPushMessageBean.setNotification(notificationBean);
                        OkingJPushManager.getInstence().pushMessage(jPushMessageBean, new JPushMessageContract.View() {
                            @Override
                            public void pushMessageSucc(String result) {


                            }

                            @Override
                            public void pushMessageFail(Throwable ex) {
                                RxToast.error(ex.getMessage());
                            }
                        });

                        if (mainHandler == null) {

                            mainHandler = new Handler();
                        }
                        mainHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Stack<Activity> activityStack = ActivityUtil.getActivityStack();
                                for (Activity activity : activityStack) {
                                    if (activity.getClass().getSimpleName().equals("MissionActivity")) {
                                        activity.finish();
                                    }

                                }
                                finish();

                            }
                        }, 100);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("Oking", ">>>>>>>>>>异常");
                        if (mRxDialogLoading != null) {
                            mRxDialogLoading.cancel();
                        }
                    }
                });

    }


    public void saveTheRecord() {
        if (mission.getStatus().equals("5")) {

        } else {
            switch (atPosition) {
                case 0:
                    saveTaskInfo();
                    break;
                case 1:
                    savePatrol();
                    break;
                case 2:
                    saveResults();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    break;
            }

        }

    }

    private String getAfterData(long time) {
        //如果需要向后计算日期 -改为+
        Date newDate = new Date(time + 24 * 60 * 60 * 1000);
        mBeforTime = newDate.getTime();
        String dateOk = sdf.format(newDate);
        return dateOk;
    }
}
