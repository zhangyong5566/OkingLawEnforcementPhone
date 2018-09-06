package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.TextUtil;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.OkingJPushManager;
import com.zhang.okinglawenforcementphone.OkingNotificationManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.SourceArrayRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.ApproverBean;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.InspectTaskBean;
import com.zhang.okinglawenforcementphone.beans.JPushMessageBean;
import com.zhang.okinglawenforcementphone.beans.LatLngListOV;
import com.zhang.okinglawenforcementphone.beans.NewsTaskOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.RecipientBean;
import com.zhang.okinglawenforcementphone.beans.SourceArrayOV;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.JPushMessageContract;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.utils.ApproverPinyinComparator;
import com.zhang.okinglawenforcementphone.utils.CBRPinyinComparator;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;
import io.reactivex.functions.Consumer;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class PatrolsToReleaseActivity extends BaseActivity implements OnDateSetListener {
    private Unbinder mBind;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_taskname)
    EditText mEt_taskname;          //任务名称
    @BindView(R.id.list_item_missionDetail)
    EditText mList_item_missionDetail;          //巡查区域
    @BindView(R.id.et_description)
    EditText mEt_description;           //任务描述
    @BindView(R.id.sp_recipient)
    TextView mSp_recipient;     //接收人
    @BindView(R.id.sp_emergency)
    TextView mSp_emergency;      //紧急程度

    @BindView(R.id.sp_approver)
    TextView mSp_approver;   //审批人
    @BindView(R.id.sp_source)
    TextView mSp_source;     //线索来源
    @BindView(R.id.sp_tasknature)
    TextView mSp_tasknature;   //任务性质
    @BindView(R.id.bt_select_begintime)
    Button mBt_select_begintime;
    @BindView(R.id.bt_select_endtime)
    Button mBt_select_endtime;
    @BindView(R.id.sp_tasktype)
    TextView mSp_tasktype;       //任务类型
    @BindView(R.id.publisher_tv)
    TextView mPublisher_tv;         //申请人
    @BindView(R.id.ib_map)
    ImageButton ibMap;
    @BindView(R.id.bt_ok)
    Button mBt_ok;
    private TimePickerDialog mBeginDialogAll;
    private TimePickerDialog mEndDialogAll;
    private List<ApproverBean.SZJCBean> mSzjc;

    private RxDialogLoading mSubRxDialogLoading;
    private RxDialogLoading mRxDialogLoading;
    private List<ApproverBean.CBRBean> mCbr;
    private String mSelecRecipient;         //选中的接收人名称
    private String mEmergency;
    private String mApproverId;    //选中的审批人ID
    private String mApprover;    //选中的审批人
    private String mSource;     //选中的线索来源
    private int mTasknature;   //选中的任务性质
    private String mMcoordinateJson;
    private ArrayList<RecipientBean> mRecipientBeans;
    private RecipientBean mReBean;
    private InspectTaskBean inspectTaskBean;
    private long mBeginMillseconds = 0;
    private long mEndMillseconds = 0;
    private String mTasktype;
    private Intent mIntent;
    private DialogUtil mDialogUtil;
    private View mButtonDailog;
    private TextView mTv_title;
    private ArrayList<SourceArrayOV> mSourceArrayOVS;
    private SourceArrayRecyAdapter mSourceArrayRecyAdapter;
    private ArrayList<SourceArrayOV> mTasknatureArrayOVS;
    private ArrayList<SourceArrayOV> mEmergencyArrayOVS;
    private List<SourceArrayOV> mTasktypeArrayOVS;
    private List<SourceArrayOV> mApproversOVS;
    private List<SourceArrayOV> mRecipientsOVS;
    private Handler mainHandler;
    private int mPosition;
   private Gson gson = new Gson();
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrols_to_release);
        mBind = ButterKnife.bind(this);
        initView();
        initData();
        initLister();
    }

    private void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        mPosition = intent.getIntExtra("position", -1);
        mId = intent.getLongExtra("id", -1L);
        inspectTaskBean = (InspectTaskBean) intent.getParcelableExtra("inspectTaskBean");

        TextUtil.setEditTextInhibitInputSpace(mEt_taskname);
        TextUtil.setEditTextInhibitInputSpeChat(mEt_taskname);

        if (inspectTaskBean != null) {
            mEt_taskname.setText(inspectTaskBean.getRWMC());
            mPublisher_tv.setText(inspectTaskBean.getFBR());
            mBt_select_begintime.setText(sf.format(inspectTaskBean.getBEGIN_TIME()));
            mBt_select_endtime.setText(sf.format(inspectTaskBean.getEND_TIME()));
            mList_item_missionDetail.setText(inspectTaskBean.getRWQYMS());
            mEt_description.setText(inspectTaskBean.getRWMS());
        } else {
            mPublisher_tv.setText(OkingContract.CURRENTUSER.getUserName());

        }
        initBottomDialog();
        initWebView();

    }

    private void initBottomDialog() {


        if (mDialogUtil == null) {
            mDialogUtil = new DialogUtil();
            mButtonDailog = View.inflate(BaseApplication.getApplictaion(), R.layout.maptask_dialog, null);
            mTv_title = mButtonDailog.findViewById(R.id.tv_title);
            RecyclerView recyList = mButtonDailog.findViewById(R.id.recy_task);
            recyList.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
            recyList.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 3, Color.TRANSPARENT));
            mSourceArrayRecyAdapter = new SourceArrayRecyAdapter(R.layout.source_item, null);
            mSourceArrayRecyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
            recyList.setAdapter(mSourceArrayRecyAdapter);
            mSourceArrayRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    List<SourceArrayOV> sourceArrayOVS = adapter.getData();
                    SourceArrayOV sourceArrayOV = sourceArrayOVS.get(position);
                    switch (sourceArrayOV.getType()) {
                        case 0:

                            if ("上级交办".equals(sourceArrayOV.getSource())) {
                                mSource = "0";
                            } else if ("部门移送".equals(sourceArrayOV.getSource())) {
                                mSource = "1";
                            } else if ("系统报警".equals(sourceArrayOV.getSource())) {
                                mSource = "2";
                            } else if ("日常巡查".equals(sourceArrayOV.getSource())) {
                                mSource = "3";
                            } else if ("媒体披露".equals(sourceArrayOV.getSource())) {
                                mSource = "4";
                            } else if ("群众举报".equals(sourceArrayOV.getSource())) {
                                mSource = "5";
                            }

                            mSp_source.setText(sourceArrayOV.getSource());

                            break;
                        case 1:

                            if ("日常执法".equals(sourceArrayOV.getSource())) {
                                mTasknature = 0;
                            } else if ("联合执法".equals(sourceArrayOV.getSource())) {
                                mTasknature = 1;
                            } else if ("专项执法".equals(sourceArrayOV.getSource())) {
                                mTasknature = 2;
                            }
                            mSp_tasknature.setText(sourceArrayOV.getSource());
                            break;
                        case 2:

                            if ("特急".equals(sourceArrayOV.getSource())) {
                                mEmergency = "0";
                            } else if ("紧急".equals(sourceArrayOV.getSource())) {
                                mEmergency = "1";
                            } else if ("一般".equals(sourceArrayOV.getSource())) {
                                mEmergency = "2";
                            }
                            mSp_emergency.setText(sourceArrayOV.getSource());
                            break;
                        case 3:

                            if ("河道管理".equals(sourceArrayOV.getSource())) {
                                mTasktype = "0";
                            } else if ("河道采砂".equals(sourceArrayOV.getSource())) {
                                mTasktype = "1";
                            } else if ("水资源管理".equals(sourceArrayOV.getSource())) {
                                mTasktype = "2";
                            } else if ("水土保持管理".equals(sourceArrayOV.getSource())) {
                                mTasktype = "3";
                            } else if ("水利工程管理".equals(sourceArrayOV.getSource())) {
                                mTasktype = "4";
                            }
                            mSp_tasktype.setText(sourceArrayOV.getSource());
                            break;
                        case 4:
                            mApproverId = mSzjc.get(position).getUSERID();
                            mApprover = sourceArrayOV.getSource();
                            mSp_approver.setText(sourceArrayOV.getSource());
                            break;
                        case 5:
                            mSelecRecipient = sourceArrayOV.getSource();
                            mReBean = mRecipientBeans.get(position);
                            mSp_recipient.setText(sourceArrayOV.getSource());
                            break;
                        default:
                            break;
                    }

                    mDialogUtil.cancelDialog();
                }
            });
        }


        String[] sourceArray = getResources().getStringArray(R.array.spinner_source);
        mSourceArrayOVS = new ArrayList<>();
        for (String s : sourceArray) {
            SourceArrayOV sourceArrayOV = new SourceArrayOV();
            sourceArrayOV.setType(0);
            sourceArrayOV.setSource(s);
            mSourceArrayOVS.add(sourceArrayOV);
        }
        String[] tasknatureArray = getResources().getStringArray(R.array.spinner_tasknature);
        mTasknatureArrayOVS = new ArrayList<>();
        for (String s : tasknatureArray) {
            SourceArrayOV sourceArrayOV = new SourceArrayOV();
            sourceArrayOV.setType(1);
            sourceArrayOV.setSource(s);
            mTasknatureArrayOVS.add(sourceArrayOV);
        }
        String[] emergencyArray = getResources().getStringArray(R.array.spinner_emergency);
        mEmergencyArrayOVS = new ArrayList<>();
        for (String s : emergencyArray) {
            SourceArrayOV sourceArrayOV = new SourceArrayOV();
            sourceArrayOV.setType(2);
            sourceArrayOV.setSource(s);
            mEmergencyArrayOVS.add(sourceArrayOV);
        }
        String[] tasktypeArray = getResources().getStringArray(R.array.spinner_tasktype);
        mTasktypeArrayOVS = new ArrayList<>();
        for (String s : tasktypeArray) {
            SourceArrayOV sourceArrayOV = new SourceArrayOV();
            sourceArrayOV.setType(3);
            sourceArrayOV.setSource(s);
            mTasktypeArrayOVS.add(sourceArrayOV);
        }
        if (mRxDialogLoading == null) {
            initWaitingDialog();
        }

        mRxDialogLoading.show();
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .loadCanSelectMember("SZJC")
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        mRxDialogLoading.cancel();
                        String result = responseBody.string();

                        ApproverBean approverBean = gson.fromJson(result, ApproverBean.class);
                        mSzjc = approverBean.getSZJC();
                        Collections.sort(mSzjc, new ApproverPinyinComparator());
                        mApproversOVS = new ArrayList<>();
                        for (int i = 0; i < mSzjc.size(); i++) {
                            SourceArrayOV sourceArrayOV = new SourceArrayOV();
                            sourceArrayOV.setType(4);
                            sourceArrayOV.setSource(mSzjc.get(i).getUSERNAME());
                            mApproversOVS.add(sourceArrayOV);
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        RxToast.error(BaseApplication.getApplictaion(), "数据获取失败", Toast.LENGTH_SHORT).show();
                        mRxDialogLoading.cancel();
                    }
                });
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .loadCanSelectMember("SZJC,CBR")
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        ApproverBean approverBean = gson.fromJson(result, ApproverBean.class);
                        List<ApproverBean.SZJCBean> szjc = approverBean.getSZJC();
                        mCbr = approverBean.getCBR();
                        mRecipientBeans = new ArrayList<>();
                        for (ApproverBean.CBRBean cbrBean : mCbr) {
                            RecipientBean recipientBean = new RecipientBean();
                            recipientBean.setDEPTID(cbrBean.getDEPTID());
                            recipientBean.setDEPTNAME(cbrBean.getDEPTNAME());
                            recipientBean.setREMARK(cbrBean.getREMARK());
                            recipientBean.setUSERID(cbrBean.getUSERID());
                            recipientBean.setUSERNAME(cbrBean.getUSERNAME());
                            recipientBean.setZFZH(cbrBean.getZFZH());
                            mRecipientBeans.add(recipientBean);
                        }

                        for (ApproverBean.SZJCBean szjcBean : szjc) {
                            RecipientBean recipientBean = new RecipientBean();
                            recipientBean.setDEPTID(szjcBean.getDEPTID());
                            recipientBean.setDEPTNAME(szjcBean.getDEPTNAME());
                            recipientBean.setREMARK(szjcBean.getREMARK());
                            recipientBean.setUSERID(szjcBean.getUSERID());
                            recipientBean.setUSERNAME(szjcBean.getUSERNAME());
                            recipientBean.setZFZH(szjcBean.getZFZH());
                            mRecipientBeans.add(recipientBean);
                        }

                        Collections.sort(mRecipientBeans, new CBRPinyinComparator());
                        mRecipientsOVS = new ArrayList<>();
                        for (RecipientBean bean : mRecipientBeans) {
                            SourceArrayOV sourceArrayOV = new SourceArrayOV();
                            sourceArrayOV.setType(5);
                            sourceArrayOV.setSource(bean.getUSERNAME());
                            mRecipientsOVS.add(sourceArrayOV);
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        RxToast.error(BaseApplication.getApplictaion(), "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void initWaitingDialog() {
        mRxDialogLoading = new RxDialogLoading(this, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.cancel();
            }
        });
        mRxDialogLoading.setLoadingText("获取数据中,请稍等...");

    }

    private void initLister() {


    }


    private void initWebView() {

//        long timeMillis = System.currentTimeMillis();
//        OkingContract.syncCookie(OkingContract.SERVER_HOST + "/arcgis/xcdgl/task_select_area.jsp?rev=99&uuid=" + timeMillis);
//        mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
//                .setAgentWebParent(mFl_web, new FrameLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
//                .useDefaultIndicator()//
//                .createAgentWeb()
//                .ready()
//                .go(OkingContract.SERVER_HOST+"/arcgis/xcdgl/task_select_area.jsp?rev=99&uuid=" + timeMillis);


    }

    @OnClick({R.id.sp_tasktype, R.id.sp_approver, R.id.sp_source, R.id.sp_tasknature, R.id.sp_recipient, R.id.sp_emergency, R.id.bt_select_begintime, R.id.bt_select_endtime, R.id.bt_ok, R.id.ib_map})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sp_tasktype:
                mTv_title.setText("任务类型");
                mSourceArrayRecyAdapter.setNewData(mTasktypeArrayOVS);
                mDialogUtil.showBottomDialog(PatrolsToReleaseActivity.this, mButtonDailog, 300f);
                break;
            case R.id.sp_approver:
                mTv_title.setText("审批领导");
                mSourceArrayRecyAdapter.setNewData(mApproversOVS);
                mDialogUtil.showBottomDialog(PatrolsToReleaseActivity.this, mButtonDailog, 300f);
                break;
            case R.id.sp_source:
                mTv_title.setText("线索来源");
                mSourceArrayRecyAdapter.setNewData(mSourceArrayOVS);
                mDialogUtil.showBottomDialog(PatrolsToReleaseActivity.this, mButtonDailog, 300f);
                break;
            case R.id.sp_tasknature:
                mTv_title.setText("任务性质");
                mSourceArrayRecyAdapter.setNewData(mTasknatureArrayOVS);
                mDialogUtil.showBottomDialog(PatrolsToReleaseActivity.this, mButtonDailog, 300f);
                break;
            case R.id.sp_recipient:
                mTv_title.setText("接收人");
                mSourceArrayRecyAdapter.setNewData(mRecipientsOVS);
                mDialogUtil.showBottomDialog(PatrolsToReleaseActivity.this, mButtonDailog, 300f);
                break;
            case R.id.sp_emergency:
                mTv_title.setText("紧急程度");
                mSourceArrayRecyAdapter.setNewData(mEmergencyArrayOVS);
                mDialogUtil.showBottomDialog(PatrolsToReleaseActivity.this, mButtonDailog, 300f);
                break;
            case R.id.bt_select_begintime:     //选择开始时间
                if (mBeginDialogAll == null) {
                    initBeginWheelYearMonthDayDialog();
                }
                mBt_select_endtime.setText("选择");
                mBeginDialogAll.show(getSupportFragmentManager(), "beginTime");
                break;
            case R.id.bt_select_endtime:        //选择结束时间

                initEndWheelYearMonthDayDialog();
                mEndDialogAll.show(getSupportFragmentManager(), "endTime");
                break;

            case R.id.bt_ok:            //提交
                if (inspectTaskBean != null) {
                    submitWithdrawDataToServer();
                } else {
                    submitDataToServer();
                }
                //与js交互
//                mAgentWeb.getJsAccessEntrace().callJs("getSelectArea", new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String coordinateJson) {
//
//                        if ("null".equals(coordinateJson)) {
//                            mMcoordinateJson = null;
//                        } else {
//                            String replace = coordinateJson.replace("\\", "");
//                            mMcoordinateJson = replace.substring(1, replace.length() - 1);
//                        }
//
//
//                        if (inspectTaskBean != null) {
//                            submitWithdrawDataToServer();
//                        } else {
//                            submitDataToServer();
//                        }
//                    }
//                });


                break;
            case R.id.ib_map:
                if (mIntent == null) {
                    mIntent = new Intent(PatrolsToReleaseActivity.this, MapByPointActivity.class);
                }
                startActivity(mIntent);

                break;
            default:
                break;

        }
    }


    private void initEndWheelYearMonthDayDialog() {
        long tenYears = 5L * 365 * 1000 * 60 * 60 * 24L;

        if (mBeginMillseconds == 0) {
            mBeginMillseconds = System.currentTimeMillis();
        }
        mEndDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("请选择结束时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("点")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(mBeginMillseconds)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(18)
                .build();

    }

    //show出开始时间选取器
    private void initBeginWheelYearMonthDayDialog() {
        long tenYears = 5L * 365 * 1000 * 60 * 60 * 24L;
        mBeginDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("请选择开始时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("点")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(18)
                .build();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 发布撤回的数据
     */
    private void submitWithdrawDataToServer() {
        if (mSubRxDialogLoading == null) {

            mSubRxDialogLoading = new RxDialogLoading(this, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });
            mSubRxDialogLoading.setLoadingText("提交数据中,请稍等...");
        }


        if (mSp_tasktype.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择任务类型");
            return;
        }
        if (mSp_approver.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择审批领导");
            return;
        }
        if (mSp_source.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择线索来源");
            return;
        }
        if (mSp_tasknature.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择任务性质");
            return;
        }
        if (mSp_recipient.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择接收人");
            return;
        }
        if (mSp_emergency.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择紧急程度");
            return;
        }

        final String taskName = mEt_taskname.getText().toString().trim();
        String missionDetail = mList_item_missionDetail.getText().toString().trim();
        String description = mEt_description.getText().toString().trim();
        String beginTime = mBt_select_begintime.getText().toString().trim();
        String endTime = mBt_select_endtime.getText().toString().trim();
        if (!TextUtils.isEmpty(taskName) && !TextUtils.isEmpty(missionDetail)
                && !TextUtils.isEmpty(description)
                && !"选择".equals(beginTime) && !"选择".equals(endTime)) {
            mBt_ok.setEnabled(false);
            mSubRxDialogLoading.show();


            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("fid", "0");
            stringObjectMap.put("id", inspectTaskBean.getID());
            stringObjectMap.put("rwms", description);
            stringObjectMap.put("rwmc", taskName);
            stringObjectMap.put("fbrid", mReBean.getUSERID());
            stringObjectMap.put("sjq", beginTime);
            stringObjectMap.put("sjz", endTime);
            stringObjectMap.put("jsrid", mReBean.getUSERID());
            stringObjectMap.put("rwlx", mTasknature);
            stringObjectMap.put("sprid", mApproverId);
            stringObjectMap.put("zt", "1");
            stringObjectMap.put("deptid", OkingContract.CURRENTUSER.getDept_id());
            stringObjectMap.put("rwqyms", missionDetail);
            stringObjectMap.put("jjcd", mEmergency);
            stringObjectMap.put("rwly", mSource);
            stringObjectMap.put("jsr", mSelecRecipient);
            stringObjectMap.put("jsdw", mReBean.getDEPTNAME());
            stringObjectMap.put("fbr", OkingContract.CURRENTUSER.getUserName());
            stringObjectMap.put("fbdw", OkingContract.CURRENTUSER.getDeptname());
            stringObjectMap.put("spr", mApprover);
            stringObjectMap.put("rwcd", "0");
            stringObjectMap.put("typeoftask", mTasktype);
            stringObjectMap.put("receiver", mReBean.getUSERID());
            if (mMcoordinateJson != null) {

                stringObjectMap.put("coordinateJson", RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), mMcoordinateJson));
            }
            BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                    .updateOrdinaryTask(stringObjectMap)
                    .compose(RxSchedulersHelper.<ResponseBody>io_main())
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(ResponseBody responseBody) throws Exception {
                            mBt_ok.setEnabled(true);
                            mSubRxDialogLoading.cancel();
                            String result = responseBody.string();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int code = jsonObject.getInt("code");
                                if (code == 400) {
                                    UpdateGreenMissionTaskOV updateGreenMissionTaskOV = new UpdateGreenMissionTaskOV();
                                    updateGreenMissionTaskOV.setType(100);
                                    updateGreenMissionTaskOV.setPosition(mPosition);
                                    EventBus.getDefault().post(updateGreenMissionTaskOV);
                                    GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().deleteByKey(mId);
                                    finish();
                                    RxToast.success(BaseApplication.getApplictaion(), "巡查任务发布成功", Toast.LENGTH_LONG).show();
                                    //发送一个远程通知
                                    JPushMessageBean jPushMessageBean = new JPushMessageBean();
                                    JPushMessageBean.AudienceBean audienceBean = new JPushMessageBean.AudienceBean();
                                    ArrayList<String> alias = new ArrayList<>();
                                    alias.add(mApproverId);
                                    audienceBean.setAlias(alias);
                                    jPushMessageBean.setAudience(audienceBean);
                                    JPushMessageBean.NotificationBean notificationBean = new JPushMessageBean.NotificationBean();
                                    notificationBean.setAlert("新消息："+taskName);
                                    JPushMessageBean.NotificationBean.AndroidBean androidBean = new JPushMessageBean.NotificationBean.AndroidBean();
                                    JPushMessageBean.NotificationBean.AndroidBean.ExtrasBean extrasBean = new JPushMessageBean.NotificationBean.AndroidBean.ExtrasBean();
                                    extrasBean.setTaskid(inspectTaskBean.getID());
                                    extrasBean.setOpenType("0");
                                    androidBean.setExtras(extrasBean);
                                    notificationBean.setAndroid(androidBean);
                                    ArrayList<String> platforms = new ArrayList<>();
                                    platforms.add("android");
                                    jPushMessageBean.setPlatform(platforms);
                                    jPushMessageBean.setNotification(notificationBean);
                                    OkingJPushManager.getInstence().pushMessage(jPushMessageBean, new JPushMessageContract.View() {
                                        @Override
                                        public void pushMessageSucc(String result) {
                                            Log.i("Oking5",result);
                                        }

                                        @Override
                                        public void pushMessageFail(Throwable ex) {
                                            Log.i("Oking5",ex.toString());
                                        }
                                    });
                                } else {
                                    Log.i("Oking", "服务器系统内部出错了1");
                                    RxToast.error(BaseApplication.getApplictaion(), "服务器系统内部出错了", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("Oking", "服务器系统内部出错了2");
                                RxToast.error(BaseApplication.getApplictaion(), "服务器系统内部出错了", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mBt_ok.setEnabled(true);
                            mSubRxDialogLoading.cancel();
                            RxToast.error(BaseApplication.getApplictaion(), "巡查任务发布失败" + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });


        } else {
            RxToast.warning(BaseApplication.getApplictaion(), "提交内容不能有空", Toast.LENGTH_LONG).show();
        }
    }

    private void submitDataToServer() {


        if (mSubRxDialogLoading == null) {

            mSubRxDialogLoading = new RxDialogLoading(this, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });
            mSubRxDialogLoading.setLoadingText("提交数据中,请稍等...");
        }

        if (mSp_tasktype.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择任务类型");
            return;
        }
        if (mSp_approver.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择审批领导");
            return;
        }
        if (mSp_source.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择线索来源");
            return;
        }
        if (mSp_tasknature.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择任务性质");
            return;
        }
        if (mSp_recipient.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择接收人");
            return;
        }
        if (mSp_emergency.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择紧急程度");
            return;
        }

        final String taskName = mEt_taskname.getText().toString().trim();
        final String missionDetail = mList_item_missionDetail.getText().toString().trim();
        final String description = mEt_description.getText().toString().trim();
        final String beginTime = mBt_select_begintime.getText().toString();
        final String endTime = mBt_select_endtime.getText().toString();


        if (!TextUtils.isEmpty(taskName) && !TextUtils.isEmpty(missionDetail)
                && !TextUtils.isEmpty(description)
                && !"选择".equals(beginTime) && !"选择".equals(endTime)) {
            mBt_ok.setEnabled(false);
            mSubRxDialogLoading.show();
            final Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("fid", "0");
            stringObjectMap.put("rwms", description);
            stringObjectMap.put("rwmc", taskName);
            stringObjectMap.put("fbrid", OkingContract.CURRENTUSER.getUserid());
            stringObjectMap.put("sjq", beginTime);
            stringObjectMap.put("sjz", endTime);
            stringObjectMap.put("jsrid", mReBean.getUSERID());
            stringObjectMap.put("rwlx", mTasknature);
            stringObjectMap.put("sprid", mApproverId);
            stringObjectMap.put("zt", "1");
            stringObjectMap.put("deptid", OkingContract.CURRENTUSER.getDept_id());
            stringObjectMap.put("rwqyms", missionDetail);
            stringObjectMap.put("jjcd", mEmergency);
            stringObjectMap.put("rwly", mSource);
            stringObjectMap.put("jsr", mSelecRecipient);
            stringObjectMap.put("jsdw", mReBean.getDEPTNAME());
            stringObjectMap.put("fbr", OkingContract.CURRENTUSER.getUserName());
            stringObjectMap.put("fbdw", OkingContract.CURRENTUSER.getDeptname());
            stringObjectMap.put("spr", mApprover);
            stringObjectMap.put("rwcd", "0");
            stringObjectMap.put("typeoftask", mTasktype);
            stringObjectMap.put("receiver", mReBean.getUSERID());
            Log.i("Oking", stringObjectMap.toString() + ">>>>");
            if (mMcoordinateJson != null) {

                stringObjectMap.put("coordinateJson", RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), mMcoordinateJson));
            }
            BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                    .pushOrdinaryTask(stringObjectMap)
                    .compose(RxSchedulersHelper.<ResponseBody>io_main())
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(ResponseBody responseBody) throws Exception {
                            mSubRxDialogLoading.cancel();
                            String result = responseBody.string();
                            //需要返回个id
                            //{"code":400,"taskid":"15282003115825056957","msg":"数据添加成功"}
                            Log.i("Oking", "巡查任务发布成功：" + result);

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int code = jsonObject.getInt("code");
                                if (code == 400) {
                                    String taskid = jsonObject.getString("taskid");
                                    GreenMissionTask greenMissionTask = new GreenMissionTask();
                                    greenMissionTask.setExamine_status(-1);
                                    greenMissionTask.setTaskid(taskid);
                                    greenMissionTask.setStatus("1");
                                    greenMissionTask.setTask_name(taskName);
                                    greenMissionTask.setJjcd(mEmergency);
                                    greenMissionTask.setTask_content(description);
                                    greenMissionTask.setRwqyms(missionDetail);
                                    greenMissionTask.setBegin_time(mBeginMillseconds);
                                    greenMissionTask.setEnd_time(mEndMillseconds);
                                    greenMissionTask.setUserid(OkingContract.CURRENTUSER.getUserid());
                                    greenMissionTask.setTypeoftask(mTasktype);
                                    greenMissionTask.setRwly(mSource);
                                    greenMissionTask.setTask_type(mTasknature);
                                    greenMissionTask.setJsr(mSelecRecipient);
                                    greenMissionTask.setJsdw(mReBean.getDEPTNAME());
                                    greenMissionTask.setTypename(mSp_tasknature.getText().toString());
                                    greenMissionTask.setApproved_person(mApproverId);
                                    greenMissionTask.setApproved_person_name(mApprover);
                                    greenMissionTask.setPublisher_name(OkingContract.CURRENTUSER.getUserName());
                                    greenMissionTask.setPublisher(OkingContract.CURRENTUSER.getUserid());
                                    greenMissionTask.setFbdw(OkingContract.CURRENTUSER.getDeptname());
                                    greenMissionTask.setReceiver(mReBean.getUSERID());
                                    greenMissionTask.setTask_area(missionDetail);
                                    if (mApproverId.equals(OkingContract.CURRENTUSER.getUserid())) {
                                        //发送一个本地通知
                                        long insert = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().insert(greenMissionTask);
                                            Intent intent = new Intent(BaseApplication.getApplictaion(), AuditActivity.class);
                                        intent.putExtra("id", insert);
                                        PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getApplictaion(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        OkingNotificationManager.getInstence().showTaskNotification(greenMissionTask, pendingIntent);
                                        NewsTaskOV event = new NewsTaskOV(2,null,greenMissionTask);
                                        EventBus.getDefault().post(event);

                                    } else {
                                        //发送一个远程通知
                                        JPushMessageBean jPushMessageBean = new JPushMessageBean();
                                        JPushMessageBean.AudienceBean audienceBean = new JPushMessageBean.AudienceBean();
                                        ArrayList<String> alias = new ArrayList<>();
                                        alias.add(mApproverId);
                                        audienceBean.setAlias(alias);
                                        jPushMessageBean.setAudience(audienceBean);
                                        JPushMessageBean.NotificationBean notificationBean = new JPushMessageBean.NotificationBean();
                                        notificationBean.setAlert("新消息："+taskName);
                                        JPushMessageBean.NotificationBean.AndroidBean androidBean = new JPushMessageBean.NotificationBean.AndroidBean();
                                        JPushMessageBean.NotificationBean.AndroidBean.ExtrasBean extrasBean = new JPushMessageBean.NotificationBean.AndroidBean.ExtrasBean();
                                        extrasBean.setTaskid(taskid);
                                        extrasBean.setOpenType("0");
                                        androidBean.setExtras(extrasBean);
                                        notificationBean.setAndroid(androidBean);
                                        ArrayList<String> platforms = new ArrayList<>();
                                        platforms.add("android");
                                        jPushMessageBean.setPlatform(platforms);
                                        jPushMessageBean.setNotification(notificationBean);
                                        OkingJPushManager.getInstence().pushMessage(jPushMessageBean, new JPushMessageContract.View() {
                                            @Override
                                            public void pushMessageSucc(String result) {
                                                Log.i("Oking5",result);
                                            }

                                            @Override
                                            public void pushMessageFail(Throwable ex) {
                                                Log.i("Oking5",ex.toString());
                                            }
                                        });
                                    }


                                    if (mainHandler == null) {

                                        mainHandler = new Handler();
                                    }
                                    mainHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();

                                        }
                                    }, 100);
                                    RxToast.success(BaseApplication.getApplictaion(), "巡查任务发布成功", Toast.LENGTH_LONG).show();

                                } else {
                                    Log.i("Oking", "服务器系统内部出错了3" + result);
                                    RxToast.error(BaseApplication.getApplictaion(), "服务器系统内部出错了", Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("Oking", "服务器系统内部出错了4");
                                RxToast.error(BaseApplication.getApplictaion(), "服务器系统内部出错了", Toast.LENGTH_LONG).show();

                            }
                            mBt_ok.setEnabled(true);

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mBt_ok.setEnabled(true);
                            mSubRxDialogLoading.cancel();
                            RxToast.error(BaseApplication.getApplictaion(), "巡查任务发布失败" + throwable.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        } else {
            RxToast.warning(BaseApplication.getApplictaion(), "提交内容不能有空", Toast.LENGTH_LONG).show();
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(LatLngListOV latLngListOV) {
        Log.i("Oking", latLngListOV.getLatLngs().size() + ">>" + gson.toJson(latLngListOV.getLatLngs()));
    }

    @Override
    public void onDateSet(com.jzxiang.pickerview.TimePickerDialog timePickerView, long millseconds) {
        String tag = timePickerView.getTag();
        if ("beginTime".equals(tag)) {
            mBeginMillseconds = millseconds;
            mBt_select_begintime.setText(sf.format(millseconds));
        } else {
            mBt_select_endtime.setText(sf.format(millseconds));
            mEndMillseconds = millseconds;
        }
    }

}
