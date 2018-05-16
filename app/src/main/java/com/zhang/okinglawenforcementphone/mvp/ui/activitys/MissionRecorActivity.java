package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.baselib.utils.NetUtil;
import com.zhang.okinglawenforcementphone.GreenDAOMannager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.EquipmentRecyAdapter;
import com.zhang.okinglawenforcementphone.adapter.ExpandableItemAdapter;
import com.zhang.okinglawenforcementphone.adapter.SpinnerArrayAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenEquipment;
import com.zhang.okinglawenforcementphone.beans.GreenEquipmentDao;
import com.zhang.okinglawenforcementphone.beans.GreenLocation;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLogDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.Level0Item;
import com.zhang.okinglawenforcementphone.beans.MemberOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.RecorItemBean;
import com.zhang.okinglawenforcementphone.mvp.contract.GetHttpMissionLogContract;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadEquipmentContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.GetHttpMissionLogPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadEquipmentPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MissionRecorActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    public static final int PHOTO_FROM_CAMERA = 100;
    private static final int PHOTO_FROM_GALLERY = 101;
    public static final int VIDEO_FROM_CAMERA = 102;
    private static final int VIDEO_FROM_GALLERY = 103;
    @BindView(R.id.report_mission_button)
    Button mReportMissionButton;
    @BindView(R.id.upload_button)
    Button mUploadButton;
    @BindView(R.id.complete_mission_button)
    Button mCompleteMissionButton;
    @BindView(R.id.mision_Recy)
    RecyclerView mMisionRecy;
    private GreenMissionTask mission;
    private GreenMissionLog mGreenMissionLog;
    private RxDialogLoading mRxDialogLoading;
    private PowerManager.WakeLock mWakeLock;
    private Handler mHandler = new Handler();
    private Level0Item mLevel0Item;
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
    private ExpandableItemAdapter mExpandableItemAdapter;
    private RecorItemBean mRecorItemBean;
    private RxDialogSureCancel mRxDialogSureCancel;
    private Intent mIntent;
    private DialogUtil mDialogUtil;
    private View mEquipentView;
    private Spinner mSpinnerEquipentType;
    private RecyclerView mCanSelectEquipentRcy;
    private Button mSaveBtn;
    private Button mCloseBtn;
    private EquipmentRecyAdapter mEquipmentRecyAdapter;
    private LoadEquipmentPresenter mLoadEquipmentPresenter;
    private ArrayList<GreenEquipment> canSelectList = new ArrayList<>();
    private int adapterPostion;
    private Long mGreenGreenLocationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_recor);
        mBind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }


    private void initData() {
        BaseApplication.getApplictaion().registerReceiver(mNetWokReceiver, new IntentFilter("oking.network"));

    }

    private void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mission.getStatus().equals("5")) {
                    finish();
                } else {

                    //提示弹窗
                    if (mRxDialogSureCancel == null) {

                        mRxDialogSureCancel = new RxDialogSureCancel(MissionRecorActivity.this);
                    }
                    mRxDialogSureCancel.setContent("未保存日志，是否保存？");
                    mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRxDialogSureCancel.cancel();
                            localSaveRecord();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 300);

                        }
                    });
                    mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRxDialogSureCancel.cancel();
                            finish();
                        }
                    });
                    mRxDialogSureCancel.show();
                }

            }
        });
        mMisionRecy.setNestedScrollingEnabled(false);

        mMisionRecy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mMisionRecy.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 3, BaseApplication.getApplictaion().getResources().getColor(R.color.bottom_nav_normal)));


        PowerManager pm = (PowerManager) BaseApplication.getApplictaion().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                "MissionRecordFragment");
        mWakeLock.acquire();

        Intent intent = getIntent();
        mTaskId = intent.getStringExtra("taskId");
        long id = intent.getLongExtra("id", -1L);

        if (mTaskId != null) {

            GreenMissionLog unique = GreenDAOMannager.getInstence().getDaoSession().getGreenMissionLogDao().queryBuilder().where(GreenMissionLogDao.Properties.Task_id.eq(mTaskId)).unique();

            if (unique == null) {
                //网络获取Log(尝试用http获取服务器的Log，获取不了再单机生成新Log)
                getHttpMissionLog(id);

            } else {
                mGreenMissionLog = unique;
                if (mission == null) {

                    mission = GreenDAOMannager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder().where(GreenMissionTaskDao.Properties.Taskid.eq(mTaskId)).unique();
                }
                initUI();

            }
        }


    }

    private void localSaveRecord() {
        mExpandableItemAdapter.saveTheRecord();
    }

    private void initUI() {

        if (mission.getStatus().equals("100")) {//任务已完成，不能再完成
            rlTop.setVisibility(View.VISIBLE);
            mCompleteMissionButton.setVisibility(View.GONE);

        } else if (mission.getStatus().equals("5")) {
            //任务已上报，不能再上报
            rlTop.setVisibility(View.GONE);


        } else if (mission.getStatus().equals("9")) {//任务被退回修改
            rlTop.setVisibility(View.VISIBLE);
            mUploadButton.setVisibility(View.GONE);
            mCompleteMissionButton.setVisibility(View.GONE);

        }

        if (mExpandableItemAdapter == null) {

            mExpandableItemAdapter = new ExpandableItemAdapter(generateData(), MissionRecorActivity.this);
            mMisionRecy.setAdapter(mExpandableItemAdapter);
        } else {
            mExpandableItemAdapter.setNewData(generateData());
        }
        mExpandableItemAdapter.expandAll();

        mExpandableItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.sign_btn:
                        mExpandableItemAdapter.saveTheRecord();
                        mIntent = new Intent(MissionRecorActivity.this, MemberSignActivity.class);
                        mIntent.putExtra("id", mission.getId());
                        startActivity(mIntent);
                        break;
                    case R.id.edit_Equipment_Btn:


                        if (mDialogUtil == null) {

                            mDialogUtil = new DialogUtil();
                            mEquipentView = View.inflate(BaseApplication.getApplictaion(), R.layout.set_equipent_dialog, null);
                            mSpinnerEquipentType = mEquipentView.findViewById(R.id.type_spinner);

                            String[] typeArray = getResources().getStringArray(R.array.spinner_equipment_type);
                            SpinnerArrayAdapter typeArrayAdapter = new SpinnerArrayAdapter(typeArray);
                            mSpinnerEquipentType.setAdapter(typeArrayAdapter);
                            mSpinnerEquipentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    setCanSelectList();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            mCanSelectEquipentRcy = mEquipentView.findViewById(R.id.canSelect_rcy);
                            mCanSelectEquipentRcy.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
                            mCanSelectEquipentRcy.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 1, BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain4)));

                            mEquipmentRecyAdapter = new EquipmentRecyAdapter(null);
                            mCanSelectEquipentRcy.setAdapter(mEquipmentRecyAdapter);
                            mSaveBtn = mEquipentView.findViewById(R.id.save_button);
                            mCloseBtn = mEquipentView.findViewById(R.id.close_button);
                            getEquipentData();

                            mSaveBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    List<GreenEquipment> checkItem = mEquipmentRecyAdapter.getCheckItem();
                                    if (checkItem != null && checkItem.size() > 0) {

                                        mExpandableItemAdapter.refreshEquipment(checkItem);
                                    }
                                    mDialogUtil.cancelDialog();
                                }
                            });

                            mCloseBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mDialogUtil.cancelDialog();
                                }
                            });
                        }

                        mDialogUtil.showBottomDialog(MissionRecorActivity.this, mEquipentView, 400f);
                        break;
                    case R.id.iv_addvideo:
                        mIntent = new Intent(MissionRecorActivity.this, VideoRecordActivity.class);
                        startActivityForResult(mIntent, MissionRecorActivity.VIDEO_FROM_CAMERA);
                        break;
                    case R.id.iv_addpic:
                        adapterPostion = position - 1;
                        Intent intent = new Intent(MissionRecorActivity.this, ShootActivity.class);
                        startActivityForResult(intent, MissionRecorActivity.PHOTO_FROM_CAMERA);

                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getEquipentData() {
        if (mRxDialogLoading == null) {
            mRxDialogLoading = new RxDialogLoading(MissionRecorActivity.this, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mRxDialogLoading.cancel();
                }
            });
        }
        mRxDialogLoading.setLoadingText("正在获取装备数据...");
        mRxDialogLoading.show();
        if (mLoadEquipmentPresenter == null) {
            mLoadEquipmentPresenter = new LoadEquipmentPresenter(new LoadEquipmentContract.View() {
                @Override
                public void loadEquipmentSucc(final String result) {
                    canSelectList.clear();
                    mRxDialogLoading.cancel();
                    Schedulers.io().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                List<GreenEquipment> greenEquipments = GreenDAOMannager.getInstence().getDaoSession().getGreenEquipmentDao().queryBuilder().where(GreenEquipmentDao.Properties.DeptId.eq(OkingContract.CURRENTUSER.getDept_id())).list();
                                if (greenEquipments.size() > 0) {
                                    for (GreenEquipment greenEquipment : greenEquipments) {
                                        GreenDAOMannager.getInstence().getDaoSession().getGreenEquipmentDao().deleteByKey(greenEquipment.getId());
                                    }

                                }
                                JSONArray jsonArray = new JSONArray(result);

                                GreenEquipment equipmentHead = new GreenEquipment();
                                equipmentHead.setItemType(0);
                                canSelectList.add(equipmentHead);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    GreenEquipment equipment = new GreenEquipment();
                                    equipment.setItemType(1);
                                    equipment.setDeptId(OkingContract.CURRENTUSER.getDept_id());
                                    equipment.setType(jsonObject.getString("type"));
                                    equipment.setType2(jsonObject.getString("type2"));
                                    equipment.setMc1(jsonObject.getString("mc1"));
                                    equipment.setMc2(jsonObject.getString("mc2"));
                                    equipment.setLy(jsonObject.getString("ly"));
                                    equipment.setValue(jsonObject.getString("value"));
                                    equipment.setRemarks(jsonObject.getString("remarks"));
                                    canSelectList.add(equipment);
                                }

                                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        mEquipmentRecyAdapter.setNewData(canSelectList);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }

                @Override
                public void loadEquipmentFail(Throwable ex) {
                    mRxDialogLoading.cancel();
                    Schedulers.io().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            final List<GreenEquipment> greenEquipments = GreenDAOMannager.getInstence().getDaoSession().getGreenEquipmentDao().queryBuilder().where(GreenEquipmentDao.Properties.DeptId.eq(OkingContract.CURRENTUSER.getDept_id())).list();

                            AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    if (greenEquipments.size() > 0) {
                                        mEquipmentRecyAdapter.setNewData(greenEquipments);
                                        GreenEquipment equipmentHead = new GreenEquipment();
                                        equipmentHead.setItemType(0);
                                        mEquipmentRecyAdapter.addData(0, equipmentHead);
                                    } else {
                                        //空
                                    }
                                }
                            });
                        }
                    });

                }
            });

        }
        mLoadEquipmentPresenter.loadEquipment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(MemberOV memberOV) {
        mExpandableItemAdapter.refreshMember();
    }

    private void getHttpMissionLog(final Long id) {

        mission = GreenDAOMannager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder().where(GreenMissionTaskDao.Properties.Id.eq(id)).unique();

        if (mission != null) {
            mRxDialogLoading = new RxDialogLoading(this, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });
            mRxDialogLoading.setLoadingText("初始化数据中，请稍等...");
            mRxDialogLoading.show();
            if (mGetHttpMissionLogPresenter == null) {
                mGetHttpMissionLogPresenter = new GetHttpMissionLogPresenter(new GetHttpMissionLogContract.View() {
                    @Override
                    public void loadHttpMissionLogSucc(GreenMissionLog greenMissionLog) {
                        mRxDialogLoading.cancel();
                        if (greenMissionLog != null) {
                            Log.i("Oking", "获取任务数据成功");
                            mGreenMissionLog = greenMissionLog;
                            initUI();
                        } else {

                        }
                    }

                    @Override
                    public void loadHttpMissionLogFail(Throwable ex) {
                        mRxDialogLoading.cancel();
                        Log.i("Oking", "没有获取到任务日志" + ex.toString());

                        mGreenMissionLog = new GreenMissionLog();
                        mGreenMissionLog.setTask_id(mission.getTaskid());
                        mGreenMissionLog.setName(OkingContract.CURRENTUSER.getUserid());
                        mGreenMissionLog.setStatus(0);

                        GreenMissionLog unique = GreenDAOMannager.getInstence().getDaoSession().getGreenMissionLogDao().queryBuilder().where(GreenMissionLogDao.Properties.Task_id.eq(mTaskId)).unique();
                        if (unique == null) {
                            GreenDAOMannager.getInstence().getDaoSession().getGreenMissionLogDao().insert(mGreenMissionLog);
                        }
                        initUI();
                    }
                });
            }
            mGetHttpMissionLogPresenter.getHttpMissionLog(mission);


        } else {
            mGreenMissionLog = new GreenMissionLog();
            mGreenMissionLog.setTask_id(mTaskId);
            mGreenMissionLog.setName(OkingContract.CURRENTUSER.getUserid());
            mGreenMissionLog.setStatus(0);

            GreenMissionLog unique = GreenDAOMannager.getInstence().getDaoSession().getGreenMissionLogDao().queryBuilder().where(GreenMissionLogDao.Properties.Task_id.eq(mTaskId)).unique();
            if (unique == null) {
                GreenDAOMannager.getInstence().getDaoSession().getGreenMissionLogDao().insert(mGreenMissionLog);
            }
            initUI();
        }


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
        EventBus.getDefault().unregister(this);
        mBind.unbind();
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_GALLERY:
                    Uri uri = data.getData();
                    if (uri == null) {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                    }

                    String path = FileUtil.praseUritoPath(this, uri);
                    FileUtil.compressImage(path);

                    GreenMedia greenMedia = new GreenMedia();
                    greenMedia.setType(1);
                    greenMedia.setPath(uri.getPath());
                    greenMedia.setTime(System.currentTimeMillis());
                    greenMedia.setGreenMissionLogId(mGreenMissionLog.getId());
                    mGreenGreenLocationId = (long) (Math.random() * 1000);
                    greenMedia.setGreenGreenLocationId(mGreenGreenLocationId);
                    GreenDAOMannager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia);
                    if (!TextUtils.isEmpty(OkingContract.LOCATIONRESULT[1]) && !"null".equals(OkingContract.LOCATIONRESULT[1])) {

                        GreenLocation greenLocation1 = new GreenLocation();
                        greenLocation1.setLatitude(OkingContract.LOCATIONRESULT[1]);
                        greenLocation1.setLongitude(OkingContract.LOCATIONRESULT[2]);
                        greenLocation1.setId(mGreenGreenLocationId);
                        greenMedia.setLocation(greenLocation1);
                        long insert = GreenDAOMannager.getInstence().getDaoSession().getGreenLocationDao().insert(greenLocation1);
                        Log.i("Oking", "图片位置插入成功：" + insert);
                    }


                    mExpandableItemAdapter.getPhotoMedias().add(greenMedia);
                    mExpandableItemAdapter.refreshList(adapterPostion);
                    break;
                case PHOTO_FROM_CAMERA:
                    ArrayList<String> picpaths = data.getStringArrayListExtra("picpaths");
                    if (picpaths != null && picpaths.size() > 0) {
                        for (String s : picpaths) {
                            GreenMedia greenMedia1 = new GreenMedia();
                            greenMedia1.setType(1);
                            greenMedia1.setPath("file://" + s);
                            greenMedia1.setTime(System.currentTimeMillis());
                            mGreenGreenLocationId = (long) (Math.random() * 1000);
                            greenMedia1.setGreenMissionLogId(mGreenMissionLog.getId());
                            greenMedia1.setGreenGreenLocationId(mGreenGreenLocationId);
                            GreenDAOMannager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia1);
                            if (!TextUtils.isEmpty(OkingContract.LOCATIONRESULT[1]) && !"null".equals(OkingContract.LOCATIONRESULT[1])) {
                                GreenLocation greenLocation2 = new GreenLocation();
                                greenLocation2.setLatitude(OkingContract.LOCATIONRESULT[1]);
                                greenLocation2.setLongitude(OkingContract.LOCATIONRESULT[2]);
                                greenLocation2.setId(mGreenGreenLocationId);
                                greenMedia1.setLocation(greenLocation2);
                                long insert1 = GreenDAOMannager.getInstence().getDaoSession().getGreenLocationDao().insert(greenLocation2);
                                Log.i("Oking", "图片位置插入成功：" + insert1 + greenLocation2.toString());
                            }

                            mExpandableItemAdapter.getPhotoMedias().add(greenMedia1);
                            mExpandableItemAdapter.refreshList(adapterPostion);
                        }
//                        picadapter.notifyDataSetChanged();
                    }



//                    localSaveRecord();
                    break;
                case VIDEO_FROM_CAMERA:
                    if (data != null) {
                        Uri videouri = data.getData();

                        GreenMedia greenMedia1 = new GreenMedia();
                        greenMedia1.setType(2);
                        greenMedia1.setTime(System.currentTimeMillis());
                        greenMedia1.setTime(System.currentTimeMillis());
                        greenMedia1.setPath(videouri.toString());
                        mGreenGreenLocationId = (long) (Math.random() * 1000);
                        greenMedia1.setGreenMissionLogId(mGreenMissionLog.getId());
                        greenMedia1.setGreenGreenLocationId(mGreenGreenLocationId);
                         GreenDAOMannager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia1);
                        if (!TextUtils.isEmpty(OkingContract.LOCATIONRESULT[1]) && !"null".equals(OkingContract.LOCATIONRESULT[1])) {

                            GreenLocation greenLocation = new GreenLocation();
                            greenLocation.setLatitude(OkingContract.LOCATIONRESULT[1]);
                            greenLocation.setLongitude(OkingContract.LOCATIONRESULT[2]);
                            greenLocation.setId(mGreenGreenLocationId);
                            greenMedia1.setLocation(greenLocation);
                            long insert = GreenDAOMannager.getInstence().getDaoSession().getGreenLocationDao().insert(greenLocation);
                            Log.i("Oking", "视频位置插入成功：" + insert + greenLocation.toString());

                        }

                        mExpandableItemAdapter.getVideoMedias().add(greenMedia1);
//                        mExpandableItemAdapter.refreshList();


                        //通知系统扫描文件
                        Intent intent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent2.setData(videouri);
                        sendBroadcast(intent2);

//                        localSaveRecord();
                    }

                    break;
                case VIDEO_FROM_GALLERY:
                    Uri videoUri = data.getData();

                    GreenMedia greenMedia1 = new GreenMedia();
                    greenMedia1.setType(2);
                    greenMedia1.setTime(System.currentTimeMillis());
                    greenMedia1.setPath(videoUri.toString());
                    mGreenGreenLocationId = (long) (Math.random() * 1000);
                    greenMedia1.setGreenMissionLogId(mGreenMissionLog.getId());
                    greenMedia1.setGreenGreenLocationId(mGreenGreenLocationId);
                    GreenDAOMannager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia1);
                    if (!TextUtils.isEmpty(OkingContract.LOCATIONRESULT[1]) && !"null".equals(OkingContract.LOCATIONRESULT[1])) {

                        GreenLocation greenLocation = new GreenLocation();
                        greenLocation.setLatitude(OkingContract.LOCATIONRESULT[1]);
                        greenLocation.setLongitude(OkingContract.LOCATIONRESULT[2]);
                        greenLocation.setId(mGreenGreenLocationId);
                        greenMedia1.setLocation(greenLocation);
                        long insert = GreenDAOMannager.getInstence().getDaoSession().getGreenLocationDao().insert(greenLocation);
                        Log.i("Oking", "视频位置插入成功：" + insert + greenLocation.toString());
                    }
                    mExpandableItemAdapter.getVideoMedias().add(greenMedia1);
//                    mExpandableItemAdapter.refreshList();


//                    localSaveRecord();
                    break;

                default:
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.report_mission_button, R.id.upload_button, R.id.complete_mission_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.report_mission_button:
                mExpandableItemAdapter.reportTask();
                break;
            case R.id.upload_button:
                localSaveRecord();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 300);
                break;
            case R.id.complete_mission_button:          //完成巡查
                mExpandableItemAdapter.completeThePatrol();

                break;
            default:
                break;
        }
    }

    private ArrayList<MultiItemEntity> generateData() {


        ArrayList<MultiItemEntity> res = new ArrayList<>();

        mLevel0Item = new Level0Item("基本信息");
        mRecorItemBean = new RecorItemBean();
        mRecorItemBean.setGreenMissionTask(mission);
        mRecorItemBean.setGreenMissionLog(mGreenMissionLog);
        mRecorItemBean.setType(1);
        mLevel0Item.addSubItem(mRecorItemBean);
        res.add(mLevel0Item);

        mLevel0Item = new Level0Item("巡查情况");
        mRecorItemBean = new RecorItemBean();
        mRecorItemBean.setGreenMissionTask(mission);
        mRecorItemBean.setGreenMissionLog(mGreenMissionLog);
        mRecorItemBean.setType(2);
        mLevel0Item.addSubItem(mRecorItemBean);
        res.add(mLevel0Item);

        mLevel0Item = new Level0Item("处理结果");
        mRecorItemBean = new RecorItemBean();
        mRecorItemBean.setGreenMissionTask(mission);
        mRecorItemBean.setGreenMissionLog(mGreenMissionLog);
        mRecorItemBean.setType(3);
        mLevel0Item.addSubItem(mRecorItemBean);
        res.add(mLevel0Item);

        mLevel0Item = new Level0Item("拍照");
        mRecorItemBean = new RecorItemBean();
        mRecorItemBean.setGreenMissionTask(mission);
        mRecorItemBean.setGreenMissionLog(mGreenMissionLog);
        mRecorItemBean.setType(4);
        mLevel0Item.addSubItem(mRecorItemBean);
        res.add(mLevel0Item);

        mLevel0Item = new Level0Item("录视频");
        mRecorItemBean = new RecorItemBean();
        mRecorItemBean.setGreenMissionTask(mission);
        mRecorItemBean.setGreenMissionLog(mGreenMissionLog);
        mRecorItemBean.setType(5);
        mLevel0Item.addSubItem(mRecorItemBean);
        res.add(mLevel0Item);

        return res;
    }

    public void invisiabelCompleteMissionButton() {
        mCompleteMissionButton.setVisibility(View.GONE);
    }

    private void setCanSelectList() {
        ArrayList<GreenEquipment> aList = new ArrayList<GreenEquipment>();
        if ("0".equals(String.valueOf(mSpinnerEquipentType.getSelectedItemPosition()))) {           //全部
            for (GreenEquipment equipment : canSelectList) {
                aList.add(equipment);
            }

        } else if ("1".equals(String.valueOf(mSpinnerEquipentType.getSelectedItemPosition()))) {     //
            for (GreenEquipment equipment : canSelectList) {
                if ("交通工具".equals(equipment.getMc1()) || equipment.getItemType() == 0) {
                    aList.add(equipment);
                }
            }
        } else if ("2".equals(String.valueOf(mSpinnerEquipentType.getSelectedItemPosition()))) {
            for (GreenEquipment equipment : canSelectList) {
                if ("通讯工具".equals(equipment.getMc1()) || equipment.getItemType() == 0) {
                    aList.add(equipment);
                }
            }

        } else if ("3".equals(String.valueOf(mSpinnerEquipentType.getSelectedItemPosition()))) {
            for (GreenEquipment equipment : canSelectList) {
                if ("取证工具".equals(equipment.getMc1()) || equipment.getItemType() == 0) {
                    aList.add(equipment);
                }
            }

        } else if ("4".equals(String.valueOf(mSpinnerEquipentType.getSelectedItemPosition()))) {
            for (GreenEquipment equipment : canSelectList) {
                if ("办公设备及场所".equals(equipment.getMc1()) || equipment.getItemType() == 0) {
                    aList.add(equipment);
                }
            }

        }
        mEquipmentRecyAdapter.setNewData(aList);
    }
}
