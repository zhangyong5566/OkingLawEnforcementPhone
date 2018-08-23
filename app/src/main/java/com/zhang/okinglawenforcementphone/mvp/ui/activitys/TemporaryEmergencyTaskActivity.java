package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.zhang.okinglawenforcementphone.OkingNotificationManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.EmergencyMemberAdapter;
import com.zhang.okinglawenforcementphone.adapter.SourceArrayRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.ApproverBean;
import com.zhang.okinglawenforcementphone.beans.EmergencyMemberGson;
import com.zhang.okinglawenforcementphone.beans.EmergencyTaskOV;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.LatLngListOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.SourceArrayOV;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.utils.ApproverPinyinComparator;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.utils.EmergencyPinyinComparator;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class TemporaryEmergencyTaskActivity extends BaseActivity implements OnDateSetListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_taskname)
    TextInputEditText mEtTaskname;
    @BindView(R.id.publisher_tv)
    TextInputEditText mPublisherTv;
    @BindView(R.id.sp_tasktype)
    TextView mSpTasktype;
    @BindView(R.id.sp_approver)
    TextView mSpApprover;
    @BindView(R.id.sp_source)
    TextView mSpSource;
    @BindView(R.id.sp_tasknature)
    TextView mSpTasknature;
    @BindView(R.id.list_item_missionMember)
    TextInputEditText mListItemMissionMember;
    @BindView(R.id.bt_select_begintime)
    Button mBtSelectBegintime;
    @BindView(R.id.bt_select_endtime)
    Button mBtSelectEndtime;
    @BindView(R.id.list_item_missionDetail)
    TextInputEditText mListItemMissionDetail;
    @BindView(R.id.et_description)
    TextInputEditText mEtDescription;
    @BindView(R.id.bt_ok)
    Button mBtOk;
    @BindView(R.id.bt_select_members)
    Button mBtSelectMembers;
    @BindView(R.id.ib_map)
    ImageButton mIbMap;

    private TimePickerDialog mBeginDialogAll;
    private TimePickerDialog mEndDialogAll;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private long mBeginMillseconds = 0;
    private long mEndMillseconds = 0;

    private String mApproverId;    //选中的审批人ID
    private String mApprover;    //选中的审批人
    private String mSource;     //选中的线索来源
    private String mTasknature;   //选中的任务性质
    private List<ApproverBean.SZJCBean> mSzjc;
    private String mMembersid;
    private RxDialogLoading mSubRxDialogLoading;
    private Handler mainHandler;
    private String[] mTasktypeArray;
    private String mTasktype;
    private RxDialogLoading mRxDialogLoading;
    private Gson gson = new Gson();
    private String mMcoordinateJson;
    private ArrayList<GreenMember> mEmergencyMembers;
    private EmergencyMemberAdapter mEmergencyMemberAdapter;
    private View mButtomContentView;
    private ListView mLv_members;
    private DialogUtil mButtomDialogUtil;
    private Button mBtOkselect;
    private List<GreenMember> mCheckName;
    private Intent mIntent;
    private Unbinder mBind;
    private LatLngListOV mLatLngListOV;
    private DialogUtil mDialogUtil;
    private View mButtonDailog;
    private TextView mTv_title;
    private ArrayList<SourceArrayOV> mSourceArrayOVS;
    private SourceArrayRecyAdapter mSourceArrayRecyAdapter;
    private ArrayList<SourceArrayOV> mTasknatureArrayOVS;
    private List<SourceArrayOV> mTasktypeArrayOVS;
    private List<SourceArrayOV> mApproversOVS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary_emergency_task);
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
    }

    private void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        EventBus.getDefault().register(this);
        mPublisherTv.setText(OkingContract.CURRENTUSER.getUserName());

        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .loadEmergencyName(OkingContract.CURRENTUSER.getDept_id())
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int code = jsonObject.getInt("code");
                            if (code == 400) {
                                JSONArray msg = jsonObject.getJSONArray("msg");
                                JSONObject jsonObject1 = msg.getJSONObject(0);
                                String rwmc = jsonObject1.getString("rwmc");
                                mEtTaskname.setText(rwmc);
                                TextUtil.setEditTextInhibitInputSpace(mEtTaskname);
                                TextUtil.setEditTextInhibitInputSpeChat(mEtTaskname);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

        initButtomDialog();
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

        String[] tasktypeArray =  getResources().getStringArray(R.array.spinner_tasktype);
        mTasktypeArrayOVS = new ArrayList<>();
        for (String s : tasktypeArray) {
            SourceArrayOV sourceArrayOV = new SourceArrayOV();
            sourceArrayOV.setType(2);
            sourceArrayOV.setSource(s);
            mTasktypeArrayOVS.add(sourceArrayOV);
        }

        if (mRxDialogLoading == null) {
            initWaitingDialog();
        }

        mRxDialogLoading.show();
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .loadPersonnel("SZJC")
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mRxDialogLoading.cancel();

                        ApproverBean approverBean = gson.fromJson(result, ApproverBean.class);
                        mSzjc = approverBean.getSZJC();
                        Collections.sort(mSzjc, new ApproverPinyinComparator());
                        mApproversOVS = new ArrayList<>();
                        for (int i = 0; i < mSzjc.size(); i++) {
                            SourceArrayOV sourceArrayOV = new SourceArrayOV();
                            sourceArrayOV.setType(3);
                            sourceArrayOV.setSource(mSzjc.get(i).getUSERNAME());
                            mApproversOVS.add(sourceArrayOV);
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("Oking", "失败：" + throwable.getMessage());
                        RxToast.error(BaseApplication.getApplictaion(), "数据获取失败", Toast.LENGTH_SHORT).show();
                        mRxDialogLoading.cancel();
                    }
                });

    }

    private void initButtomDialog() {

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

                            mSpSource.setText(sourceArrayOV.getSource());

                            break;
                        case 1:

                            if ("日常执法".equals(sourceArrayOV.getSource())) {
                                mTasknature = "0";
                            } else if ("联合执法".equals(sourceArrayOV.getSource())) {
                                mTasknature = "1";
                            } else if ("专项执法".equals(sourceArrayOV.getSource())) {
                                mTasknature = "2";
                            }
                            mSpTasknature.setText(sourceArrayOV.getSource());
                            break;
                        case 2:

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
                            mSpTasktype.setText(sourceArrayOV.getSource());
                            break;
                        case 3:
                            mApproverId = mSzjc.get(position).getUSERID();
                            mApprover = sourceArrayOV.getSource();
                            mSpApprover.setText(sourceArrayOV.getSource());
                            break;
                        default:
                            break;
                    }

                    mDialogUtil.cancelDialog();
                }
            });

        }

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

    @OnClick({R.id.sp_tasktype, R.id.sp_approver, R.id.sp_source, R.id.sp_tasknature,R.id.bt_select_begintime, R.id.bt_select_endtime, R.id.bt_ok, R.id.bt_select_members, R.id.ib_map})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sp_tasktype:
                mTv_title.setText("任务类型");
                mSourceArrayRecyAdapter.setNewData(mTasktypeArrayOVS);
                mDialogUtil.showBottomDialog(TemporaryEmergencyTaskActivity.this, mButtonDailog, 300f);
                break;
            case R.id.sp_approver:
                mTv_title.setText("审批领导");
                mSourceArrayRecyAdapter.setNewData(mApproversOVS);
                mDialogUtil.showBottomDialog(TemporaryEmergencyTaskActivity.this, mButtonDailog, 300f);
                break;
            case R.id.sp_source:
                mTv_title.setText("线索来源");
                mSourceArrayRecyAdapter.setNewData(mSourceArrayOVS);
                mDialogUtil.showBottomDialog(TemporaryEmergencyTaskActivity.this, mButtonDailog, 300f);
                break;
            case R.id.sp_tasknature:
                mTv_title.setText("任务性质");
                mSourceArrayRecyAdapter.setNewData(mTasknatureArrayOVS);
                mDialogUtil.showBottomDialog(TemporaryEmergencyTaskActivity.this, mButtonDailog, 300f);
                break;
            case R.id.bt_select_begintime:          //选择开始时间
                if (mBeginDialogAll == null) {
                    initBeginWheelYearMonthDayDialog();
                }
                mBtSelectEndtime.setText("选择");
                mBeginDialogAll.show(getSupportFragmentManager(), "beginTime");
                break;
            case R.id.bt_select_endtime:            //选择结束时间
                initEndWheelYearMonthDayDialog();
                mEndDialogAll.show(getSupportFragmentManager(), "endTime");
                break;
            case R.id.bt_ok:                //提交
                submitDataToServer();
                break;
            case R.id.bt_select_members:        //选择成员
                if (mRxDialogLoading == null) {

                    initWaitingDialog();
                }
                mRxDialogLoading.show();
                httpGetCanSelectMember();
                break;
            case R.id.ib_map:
                if (mIntent == null) {
                    mIntent = new Intent(TemporaryEmergencyTaskActivity.this, MapByPointActivity.class);
                }
                if (mLatLngListOV != null) {
                    mIntent.putExtra("centerPoint", mLatLngListOV.getCenterLatLng());
                    mIntent.putExtra("drawLaLoType", mLatLngListOV.getType());
                    mIntent.putExtra("left", mLatLngListOV.getLeft());
                    mIntent.putExtra("right", mLatLngListOV.getRight());
                    mIntent.putExtra("top", mLatLngListOV.getTop());
                    mIntent.putExtra("bottom", mLatLngListOV.getBottom());
                    mIntent.putExtra("zoom", mLatLngListOV.getZoom());
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

    private void httpGetCanSelectMember() {
        if (mButtomContentView == null) {
            mButtomContentView = LayoutInflater.from(TemporaryEmergencyTaskActivity.this).inflate(R.layout.dialog_content_circle, null);

            mLv_members = mButtomContentView.findViewById(R.id.lv_members);
            mBtOkselect = mButtomContentView.findViewById(R.id.bt_okselect);
            mBtOkselect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCheckName = mEmergencyMemberAdapter.getCheckName();
                    String members = OkingContract.CURRENTUSER.getUserName();
                    mMembersid = "";
                    for (GreenMember m : mCheckName) {
                        members = members + "," + m.getUsername();
                        mMembersid = mMembersid + "," + m.getUserid();
                    }
                    mListItemMissionMember.setText(members);
                    mButtomDialogUtil.cancelDialog();
                }
            });
        }

        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .loadPersonnel("SZJC,CBR")
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mRxDialogLoading.cancel();

                        if (mEmergencyMembers == null) {

                            mEmergencyMembers = new ArrayList<GreenMember>();
                        }
                        mEmergencyMembers.clear();


                        EmergencyMemberGson emergencyMemberGson = gson.fromJson(result, EmergencyMemberGson.class);
                        List<EmergencyMemberGson.CBRBean> cbrs = emergencyMemberGson.getCBR();
                        for (EmergencyMemberGson.CBRBean cbrBean : cbrs) {


                            if (cbrBean.getUSERNAME().equals(OkingContract.CURRENTUSER.getUserName())) {
                                continue;
                            }
                            GreenMember member = new GreenMember();
                            member.setUsername(cbrBean.getUSERNAME());
                            member.setDepatid(cbrBean.getDEPTID());
                            member.setDepatname(cbrBean.getDEPTNAME());
                            member.setRemark(cbrBean.getREMARK());
                            member.setUserid(cbrBean.getUSERID());
                            member.setZfzh(cbrBean.getZFZH());
                            mEmergencyMembers.add(member);
                        }
                        List<EmergencyMemberGson.SZJCBean> szjcs = emergencyMemberGson.getSZJC();


                        for (EmergencyMemberGson.SZJCBean szjcBean : szjcs) {


                            if (szjcBean.getUSERNAME().equals(OkingContract.CURRENTUSER.getUserName())) {
                                continue;
                            }
                            GreenMember member = new GreenMember();
                            member.setUsername(szjcBean.getUSERNAME());
                            member.setDepatid(szjcBean.getDEPTID());
                            member.setDepatname(szjcBean.getDEPTNAME());
                            member.setRemark(szjcBean.getREMARK());
                            member.setUserid(szjcBean.getUSERID());
                            member.setZfzh(szjcBean.getZFZH());
                            mEmergencyMembers.add(member);
                        }


                        Collections.sort(mEmergencyMembers, new EmergencyPinyinComparator());
                        if (mButtomDialogUtil == null) {

                            mButtomDialogUtil = new DialogUtil();
                        }
                        mButtomDialogUtil.showBottomDialog(TemporaryEmergencyTaskActivity.this, mButtomContentView, 400f);
                        mEmergencyMemberAdapter = new EmergencyMemberAdapter(TemporaryEmergencyTaskActivity.this, mEmergencyMembers);
                        mLv_members.setAdapter(mEmergencyMemberAdapter);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRxDialogLoading.cancel();
                        if (mEmergencyMemberAdapter != null) {

                            mEmergencyMemberAdapter.notifyDataSetChanged();
                        }
                    }
                });

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


        final String taskName = mEtTaskname.getText().toString().trim();
        String members = mListItemMissionMember.getText().toString().trim();
        final String missionDetail = mListItemMissionDetail.getText().toString().trim();
        final String description = mEtDescription.getText().toString().trim();
        final String beginTime = mBtSelectBegintime.getText().toString();
        final String endTime = mBtSelectEndtime.getText().toString();
        if (mSpTasktype.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择任务类型");
            return;
        }
        if (mSpApprover.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择审批领导");
            return;
        }
        if (mSpSource.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择线索来源");
            return;
        }
        if (mSpTasknature.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择任务性质");
            return;
        }

        if (!TextUtils.isEmpty(taskName) && !TextUtils.isEmpty(members)
                && !TextUtils.isEmpty(missionDetail) && !TextUtils.isEmpty(description)
                && !"选择".equals(beginTime) && !"选择".equals(endTime)) {
            mBtOk.setEnabled(false);
            mSubRxDialogLoading.show();

            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("rwmc", taskName);
            stringObjectHashMap.put("fid", "0");
            stringObjectHashMap.put("rwms", description);
            stringObjectHashMap.put("fbrid", OkingContract.CURRENTUSER.getUserid());
            stringObjectHashMap.put("sjq", beginTime);
            stringObjectHashMap.put("sjz", endTime);
            stringObjectHashMap.put("jsrid", OkingContract.CURRENTUSER.getUserid());
            stringObjectHashMap.put("rwlx", mTasknature);
            stringObjectHashMap.put("sprid", mApproverId);
            stringObjectHashMap.put("zt", "3");
            stringObjectHashMap.put("jjcd", "1");
            stringObjectHashMap.put("deptid", OkingContract.CURRENTUSER.getDept_id());
            stringObjectHashMap.put("rwqyms", missionDetail);
            stringObjectHashMap.put("rwly", mSource);
            stringObjectHashMap.put("jsr", OkingContract.CURRENTUSER.getUserName());
            stringObjectHashMap.put("jsdw", OkingContract.CURRENTUSER.getDeptname());
            stringObjectHashMap.put("fbr", OkingContract.CURRENTUSER.getUserName());
            stringObjectHashMap.put("fbdw", OkingContract.CURRENTUSER.getDeptname());
            stringObjectHashMap.put("spr", mApprover);
            stringObjectHashMap.put("rwcd", "1");
            stringObjectHashMap.put("typeoftask", mTasktype);
            stringObjectHashMap.put("yxry", mMembersid.substring(1, mMembersid.length()));
            BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                    .addEmergencyRelease(stringObjectHashMap)
                    .compose(RxSchedulersHelper.<ResponseBody>io_main())
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(ResponseBody responseBody) throws Exception {
                            String result = responseBody.string();
                            mSubRxDialogLoading.cancel();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int code = jsonObject.getInt("code");
                                if (code == 400) {
                                    final String taskid = jsonObject.getString("taskid");
                                    Schedulers.io().createWorker().schedule(new Runnable() {
                                        @Override
                                        public void run() {
                                            GreenMissionTask greenMissionTask = new GreenMissionTask();
                                            //返回个id
                                            greenMissionTask.setTaskid(taskid);
                                            greenMissionTask.setStatus("3");
                                            greenMissionTask.setTask_name(taskName);
                                            greenMissionTask.setJjcd("1");
                                            greenMissionTask.setTask_content(description);
                                            greenMissionTask.setRwqyms(missionDetail);
                                            greenMissionTask.setBegin_time(mBeginMillseconds);
                                            greenMissionTask.setEnd_time(mEndMillseconds);
                                            greenMissionTask.setUserid(OkingContract.CURRENTUSER.getUserid());
                                            greenMissionTask.setTypeoftask(mTasktype);
                                            greenMissionTask.setRwly(mSource);
                                            greenMissionTask.setExamine_status(-1);
                                            greenMissionTask.setTypename(mSpTasknature.getText().toString());
                                            greenMissionTask.setApproved_person_name(mApprover);
                                            greenMissionTask.setApproved_person(OkingContract.CURRENTUSER.getUserid());
                                            greenMissionTask.setPublisher_name(OkingContract.CURRENTUSER.getUserName());
                                            greenMissionTask.setFbdw(OkingContract.CURRENTUSER.getDeptname());
                                            greenMissionTask.setTask_area(missionDetail);
                                            if (mLatLngListOV != null) {
                                                greenMissionTask.setDrawLaLoType(mLatLngListOV.getType());

                                                greenMissionTask.setMcoordinateJson(gson.toJson(mLatLngListOV.getLatLngs()));
                                            }
                                            long insert = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().insert(greenMissionTask);

                                            GreenMember greenMember = new GreenMember();
                                            greenMember.setGreenMemberId(insert);
                                            greenMember.setUsername(OkingContract.CURRENTUSER.getUserName());
                                            greenMember.setUserid(OkingContract.CURRENTUSER.getUserid());
                                            greenMember.setPost("负责人");
                                            greenMember.setAccount(OkingContract.CURRENTUSER.getAcount());
                                            GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().insert(greenMember);

                                            for (GreenMember checkName : mCheckName) {
                                                checkName.setPost("组员");
                                                checkName.setGreenMemberId(insert);
                                                long insert1 = GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().insert(checkName);
                                            }
                                            Intent intent = new Intent(BaseApplication.getApplictaion(), MissionActivity.class);
                                            intent.putExtra("id", insert);
                                            PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getApplictaion(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                            OkingNotificationManager.getInstence().showTaskNotification(greenMissionTask, pendingIntent);
                                            EmergencyTaskOV emergencyTaskOV = new EmergencyTaskOV();
                                            emergencyTaskOV.setType(0);
                                            emergencyTaskOV.setGreenMissionTask(greenMissionTask);
                                            EventBus.getDefault().post(emergencyTaskOV);

                                        }
                                    });
                                    if (mainHandler == null) {

                                        mainHandler = new Handler();
                                    }
                                    mainHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();

                                        }
                                    }, 100);
                                    RxToast.success(BaseApplication.getApplictaion(), "紧急任务发布成功", Toast.LENGTH_LONG).show();

                                } else {
                                    RxToast.error(BaseApplication.getApplictaion(), "服务器系统内部出错了", Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                RxToast.error(BaseApplication.getApplictaion(), "服务器系统内部出错了", Toast.LENGTH_LONG).show();

                            }
                            mBtOk.setEnabled(true);

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            mBtOk.setEnabled(true);
                            mSubRxDialogLoading.cancel();
                            RxToast.error(BaseApplication.getApplictaion(), "紧急任务发布失败" + throwable.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        } else {
            RxToast.warning(BaseApplication.getApplictaion(), "提交内容不能有空", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String tag = timePickerView.getTag();
        if ("beginTime".equals(tag)) {
            mBeginMillseconds = millseconds;
            mBtSelectBegintime.setText(getDateToString(millseconds));
        } else {
            mEndMillseconds = millseconds;
            mBtSelectEndtime.setText(getDateToString(millseconds));
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(LatLngListOV latLngListOV) {
        this.mLatLngListOV = latLngListOV;
        mMcoordinateJson = gson.toJson(latLngListOV.getLatLngs());
        Log.i("Oking", latLngListOV.getLatLngs().size() + "" + latLngListOV.getLatLngs().toString());
    }

    private String getDateToString(long millseconds) {
        Date d = new Date(millseconds);
        return sf.format(d);
    }
}
