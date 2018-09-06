package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.OkingJPushManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.SourceArrayRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.JPushMessageBean;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.SmameLeveOV;
import com.zhang.okinglawenforcementphone.beans.SourceArrayOV;
import com.zhang.okinglawenforcementphone.beans.TaskCauseActionOV;
import com.zhang.okinglawenforcementphone.beans.UnitOV;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.JPushMessageContract;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadAcceptNumberContract;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadBasicLogContract;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadUsersByDeptIdContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadAcceptNumberPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadBasicLogPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadUsersByDeptIdPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ApprovalActivity;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ApprovalInstructionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rb_01)
    RadioButton mRb01;
    @BindView(R.id.rb_02)
    RadioButton mRb02;
    @BindView(R.id.rb_03)
    RadioButton mRb03;
    @BindView(R.id.tv_depatIn)
    TextView mTvDepatIn;
    @BindView(R.id.tv_depatOut)
    TextView mTvDepatOut;
    @BindView(R.id.tv_depatDetai)
    TextView mTvDepatDetai;
    @BindView(R.id.tv_input)
    EditText mTvInput;
    @BindView(R.id.bt_submit)
    Button mBtSubmit;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private String mStateType = "0";        //0已阅、1立案、2转交
    private RxDialogLoading mRxDialogLoading;
    private Gson mGson = new Gson();
    private boolean mIsAcceptNumber = false;
    private String mSlbh;                   //受理编号
    private GreenMissionTask mGreenMissionTask;
    private GreenMissionLog mGreenMissionLog;
    private DialogUtil mDialogUtil;
    private View mButtonDailog;
    private TextView mTv_title;
    private SourceArrayRecyAdapter mSourceArrayRecyAdapter;
    private ArrayList<SourceArrayOV> mDepatOVS;
    private ArrayList<SourceArrayOV> mMnitOVS;
    private ArrayList<SourceArrayOV> mMemberOVS;
    private ArrayList<SourceArrayOV> mSmameLeveOVS;
    private boolean mIsUsersByDeptIdSucc = false;
    private List<UnitOV.DataBean.RecordsBean> mRecords;
    private boolean mIsUnitSucc = false;
    private List<SmameLeveOV.DataBean.RecordsBean> mSmameLeveRecords;
    private String mDepatUserId;
    private LoadBasicLogPresenter mLoadBasicLogPresenter;
    public ApprovalInstructionsFragment() {
        // Required empty public constructor
    }

    public static ApprovalInstructionsFragment newInstance(String param1, String param2) {
        ApprovalInstructionsFragment fragment = new ApprovalInstructionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_approval_instructions, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        return mInflate;
    }

    private void initData() {
        //受理编号
        if (!mIsAcceptNumber) {
            if (mRxDialogLoading == null) {
                mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                });
            }
            mRxDialogLoading.setLoadingText("正在获取数据中请稍候...");
            mRxDialogLoading.show();
            new LoadAcceptNumberPresenter(new LoadAcceptNumberContract.View() {
                @Override
                public void loadAcceptNumberSucc(String result) {
                    Log.i("Oking1", "受理编号:" + result);
                    mRxDialogLoading.cancel();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            mIsAcceptNumber = true;
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject records = data.getJSONObject("records");
                            mSlbh = records.getString("slbh");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void loadAcceptNumberFail(Throwable ex) {
                    Log.i("Oking1", "受理编号失败:" + ex.toString());
                    mRxDialogLoading.cancel();
                    mIsAcceptNumber = false;
                }
            }).loadAcceptNumber();
        }
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
                    List<SourceArrayOV> datas = adapter.getData();
                    SourceArrayOV sourceArrayOV = datas.get(position);
                    switch (sourceArrayOV.getType()) {
                        case 0:
                            mTvDepatIn.setText(sourceArrayOV.getSource());
                            switch (sourceArrayOV.getSource()) {
                                case "部门内":
                                    mTvDepatOut.setVisibility(View.VISIBLE);
                                    break;
                                case "部门外":
                                    mTvDepatOut.setVisibility(View.GONE);
                                    mTvDepatOut.setText("*请选择");
                                    mTvDepatDetai.setVisibility(View.GONE);
                                    mTvDepatDetai.setText("*请选择");
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 1:
                            mTvDepatOut.setText(sourceArrayOV.getSource());
                            mTvDepatDetai.setText("*请选择");
                            mTvDepatDetai.setVisibility(View.VISIBLE);
                            switch (sourceArrayOV.getSource()) {
                                case "单位内":
                                    if (!mIsUsersByDeptIdSucc) {
                                        if (mRxDialogLoading == null) {
                                            mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    dialog.cancel();
                                                }
                                            });
                                        }
                                        mRxDialogLoading.setLoadingText("正在获取数据中请稍候...");
                                        mRxDialogLoading.show();
                                        new LoadUsersByDeptIdPresenter(new LoadUsersByDeptIdContract.View() {
                                            @Override
                                            public void getUsersByDeptIdSucc(String result) {
                                                mRxDialogLoading.cancel();
                                                UnitOV unitOV = mGson.fromJson(result, UnitOV.class);
                                                String status = unitOV.getStatus();
                                                if (status.equals("1")) {
                                                    mRecords = unitOV.getData().getRecords();
                                                    mIsUsersByDeptIdSucc = true;
                                                } else {

                                                }
                                            }

                                            @Override
                                            public void getUsersByDeptIdFail(Throwable e) {
                                                Log.i("Oking1", e.toString());
                                                mIsUsersByDeptIdSucc = false;
                                                mRxDialogLoading.cancel();
                                            }
                                        }).getUsersByDeptId(OkingContract.CURRENTUSER.getDept_id());
                                    }

                                    break;
                                case "单位外":
                                    if (!mIsUnitSucc) {
                                        if (mRxDialogLoading == null) {
                                            mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    dialog.cancel();
                                                }
                                            });
                                        }
                                        mRxDialogLoading.setLoadingText("正在获取数据中请稍候...");
                                        mRxDialogLoading.show();
                                        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                                                .loadUnit(OkingContract.CURRENTUSER.getDept_id())
                                                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                                                .subscribe(new Consumer<ResponseBody>() {
                                                    @Override
                                                    public void accept(ResponseBody responseBody) throws Exception {
                                                        mRxDialogLoading.cancel();
                                                        String result = responseBody.string();
                                                        SmameLeveOV smameLeveOV = mGson.fromJson(result, SmameLeveOV.class);
                                                        String status = smameLeveOV.getStatus();
                                                        if (status.equals("1")) {
                                                            mSmameLeveRecords = smameLeveOV.getData().getRecords();
                                                            mIsUnitSucc = true;
                                                        } else {

                                                        }
                                                    }
                                                }, new Consumer<Throwable>() {
                                                    @Override
                                                    public void accept(Throwable throwable) throws Exception {
                                                        mRxDialogLoading.cancel();
                                                        mIsUnitSucc = false;
                                                    }
                                                });
                                    }


                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2:
                        case 3:
                            mDepatUserId = sourceArrayOV.getUserId();
                            mTvDepatDetai.setText(sourceArrayOV.getSource());
                            break;
                        default:
                            break;
                    }
                    mDialogUtil.cancelDialog();
                }
            });
        }

    }

    @OnCheckedChanged({R.id.rb_01, R.id.rb_02, R.id.rb_03})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_01:
                if (isChecked) {
                    mStateType = "0";
                    mTvDepatIn.setVisibility(View.GONE);
                    mTvDepatIn.setText("*请选择");
                    mTvDepatOut.setVisibility(View.GONE);
                    mTvDepatOut.setText("*请选择");
                    mTvDepatDetai.setVisibility(View.GONE);

                }
                break;
            case R.id.rb_02:

                if (isChecked) {
                    mStateType = "1";
                    mTvDepatIn.setVisibility(View.GONE);
                    mTvDepatIn.setText("*请选择");
                    mTvDepatOut.setVisibility(View.GONE);
                    mTvDepatOut.setText("*请选择");
                    mTvDepatDetai.setVisibility(View.GONE);
                }
                break;
            case R.id.rb_03:
                if (isChecked) {
                    mStateType = "2";
                    mTvDepatIn.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_submit, R.id.tv_depatDetai, R.id.tv_depatIn, R.id.tv_depatOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                //提交
                final String input = mTvInput.getText().toString().trim();
                switch (mStateType) {
                    case "0":               //已阅

                        if (TextUtils.isEmpty(input)) {
                            RxToast.warning("审批意见不能为空");
                            return;
                        }
                        if (mRxDialogLoading == null) {
                            mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.cancel();
                                }
                            });
                        }
                        mRxDialogLoading.setLoadingText("正在提交数据中请稍候...");
                        mRxDialogLoading.show();
                        examineOthersLogForAndroid(input, "", "");

                        break;
                    case "1":               //立案处罚
                        if (!TextUtils.isEmpty(mSlbh)) {
                            if (TextUtils.isEmpty(input)) {
                                RxToast.warning("审批意见不能为空");
                                return;
                            }
                            if (mRxDialogLoading == null) {
                                mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        dialog.cancel();
                                    }
                                });
                            }
                            mRxDialogLoading.setLoadingText("正在提交数据中请稍候...");
                            mRxDialogLoading.show();

                            if (mGreenMissionLog!=null){
                                //Rx链式调用，解决回调嵌套

                                submitDataRecord(input);

                            }else {
                                //
                                if (mLoadBasicLogPresenter == null) {
                                    mLoadBasicLogPresenter = new LoadBasicLogPresenter(new LoadBasicLogContract.View() {
                                        @Override
                                        public void getBasicLogSucc(String result) {
                                            mRxDialogLoading.cancel();
                                            //{"msg":"查询成功!","datas":[{"OTHER_PART":"交通,城管","EQUIPMENT":"交通工具：001003,001001,001001  ","PLAN":"0","TYPE":"0"}],"status":"1"}

                                            try {
                                                JSONObject jsonObject = new JSONObject(result);
                                                String status = jsonObject.getString("status");
                                                if (status.equals("1")) {
                                                    JSONArray datas = jsonObject.getJSONArray("datas");
                                                    mGreenMissionLog = new GreenMissionLog();
                                                    if (datas.length()>0){
                                                        JSONObject object = datas.getJSONObject(0);
                                                        mGreenMissionLog.setEquipment(object.getString("EQUIPMENT"));
                                                        mGreenMissionLog.setServer_id(object.getString("LOG_ID"));
                                                        mGreenMissionLog.setTask_id(mGreenMissionTask.getTaskid());
                                                        mGreenMissionLog.setOther_part(object.getString("OTHER_PART"));
                                                        mGreenMissionLog.setPlan(Integer.parseInt(object.getString("PLAN")));
                                                        mGreenMissionLog.setPatrol(object.getString("PATROL"));
                                                        mGreenMissionLog.setDzyj(object.getString("DZYJ"));
                                                    }

                                                    submitDataRecord(input);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
//
                                        }

                                        @Override
                                        public void getBasicLogFail(Throwable ex) {
                                            mRxDialogLoading.cancel();
                                            Log.i("Oking5","获取日志失败"+ex.toString());
                                            RxToast.error("获取日志失败");

                                        }
                                    });

                                }
                                mLoadBasicLogPresenter.getBasicLog(mGreenMissionTask.getTaskid());


                            }


                        } else {
                            RxToast.error("受理编号为空");
                        }

                        break;
                    case "2":
                        if (TextUtils.isEmpty(input)) {
                            RxToast.warning("审批意见不能为空");
                            return;
                        }
                        if (mRxDialogLoading == null) {
                            mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.cancel();
                                }
                            });
                        }
                        mRxDialogLoading.setLoadingText("正在提交数据中请稍候...");
                        mRxDialogLoading.show();
                        String depatIn = mTvDepatIn.getText().toString().trim();
                        String depatOut = mTvDepatOut.getText().toString().trim();
                        if (depatIn.equals("部门外")) {
                            examineOthersLogForAndroid(input, "1", "");
                        } else if ((depatIn + depatOut).equals("部门内单位内") || (depatIn + depatOut).equals("部门内单位外")) {
                            examineOthersLogForAndroid(input, "2", mDepatUserId);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case R.id.tv_depatDetai:
                if (mTvDepatOut.getText().toString().trim().equals("单位内")) {
                    if (mRecords != null && mRecords.size() > 0) {
                        if (mMemberOVS == null) {
                            mMemberOVS = new ArrayList<>();
                            for (UnitOV.DataBean.RecordsBean record : mRecords) {
                                SourceArrayOV sourceArrayOV = new SourceArrayOV();
                                sourceArrayOV.setType(2);
                                sourceArrayOV.setSource(record.getText());
                                sourceArrayOV.setUserId(record.getId());
                                mMemberOVS.add(sourceArrayOV);
                            }

                        }
                        mSourceArrayRecyAdapter.setNewData(mMemberOVS);
                        mDialogUtil.showBottomDialog(getActivity(), mButtonDailog, 350f);
                    }
                    mTv_title.setText("选择人员");

                } else if (mTvDepatOut.getText().toString().trim().equals("单位外")) {
                    if (mSmameLeveRecords != null && mSmameLeveRecords.size() > 0) {
                        if (mSmameLeveOVS == null) {
                            mSmameLeveOVS = new ArrayList<>();
                            for (SmameLeveOV.DataBean.RecordsBean record : mSmameLeveRecords) {
                                SourceArrayOV sourceArrayOV = new SourceArrayOV();
                                sourceArrayOV.setType(3);
                                sourceArrayOV.setSource(record.getDEPTNAME());
                                sourceArrayOV.setUserId(record.getDEPTID());
                                mSmameLeveOVS.add(sourceArrayOV);
                            }

                        }
                        mSourceArrayRecyAdapter.setNewData(mSmameLeveOVS);
                        mDialogUtil.showBottomDialog(getActivity(), mButtonDailog, 350f);
                    }
                    mTv_title.setText("选择单位");
                }

                break;
            case R.id.tv_depatIn:
                if (mDepatOVS == null) {
                    mDepatOVS = new ArrayList<>();
                    SourceArrayOV sourceArrayOV1 = new SourceArrayOV();
                    sourceArrayOV1.setSource("部门内");
                    sourceArrayOV1.setType(0);
                    mDepatOVS.add(sourceArrayOV1);
                    SourceArrayOV sourceArrayOV2 = new SourceArrayOV();
                    sourceArrayOV2.setSource("部门外");
                    sourceArrayOV2.setType(0);
                    mDepatOVS.add(sourceArrayOV2);
                }

                mTv_title.setText("选择部门");
                mSourceArrayRecyAdapter.setNewData(mDepatOVS);
                mDialogUtil.showBottomDialog(getActivity(), mButtonDailog, 150f);

                break;
            case R.id.tv_depatOut:
                if (mMnitOVS == null) {
                    mMnitOVS = new ArrayList<>();
                    SourceArrayOV sourceArrayOV1 = new SourceArrayOV();
                    sourceArrayOV1.setSource("单位内");
                    sourceArrayOV1.setType(1);
                    mMnitOVS.add(sourceArrayOV1);
                    SourceArrayOV sourceArrayOV2 = new SourceArrayOV();
                    sourceArrayOV2.setSource("单位外");
                    sourceArrayOV2.setType(1);
                    mMnitOVS.add(sourceArrayOV2);
                }

                mTv_title.setText("选择单位");
                mSourceArrayRecyAdapter.setNewData(mMnitOVS);
                mDialogUtil.showBottomDialog(getActivity(), mButtonDailog, 150f);
                break;
            default:
                break;
        }


    }

    private void submitDataRecord(final String input) {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .getTaskAyByParameterForAndroid(mGreenMissionLog.getServer_id())
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        TaskCauseActionOV taskCauseActionOV = mGson.fromJson(result, TaskCauseActionOV.class);
                        TaskCauseActionOV.DataBean data = taskCauseActionOV.getData();
                        TaskCauseActionOV.DataBean.RecordsBean recordsBean = data.getRecords().get(0);
                        Map<String, Object> putRecordParams = new HashMap<>();
                        putRecordParams.put("option", "add");
                        putRecordParams.put("slbh", mSlbh);
                        putRecordParams.put("slsj", OkingContract.SDF.format(System.currentTimeMillis()));
                        putRecordParams.put("afsj", OkingContract.SDF.format(mGreenMissionTask.getBegin_time()));
                        putRecordParams.put("afdd", recordsBean.getAfdd());
                        putRecordParams.put("aqjy", recordsBean.getSlsjstr() + recordsBean.getAfdd() + recordsBean.getAqjy());
                        putRecordParams.put("wfsx", recordsBean.getAqjy());
                        putRecordParams.put("ajly", "执法检查");
                        List<TaskCauseActionOV.DataBean.RecordsBean> jsonBen = new ArrayList<>();
                        jsonBen.add(recordsBean);
                        putRecordParams.put("ajlylist", mGson.toJson(jsonBen));
                        putRecordParams.put("EXAMINE_STATUS", "4");
                        putRecordParams.put("LDQM", OkingContract.CURRENTUSER.getUserName());
                        putRecordParams.put("ID", mGreenMissionLog.getServer_id());
                        putRecordParams.put("LEADIDEA", input);
                        putRecordParams.put("taskid", mGreenMissionTask.getTaskid());
                        putRecordParams.put("username", OkingContract.CURRENTUSER.getUserName());
                        putRecordParams.put("xcqk", mGreenMissionLog.getPatrol());
                        putRecordParams.put("dzyj", mGreenMissionLog.getDzyj());
                        putRecordParams.put("ajdjzt", "4");
                        return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                                .examineLogForAndroid(putRecordParams);
                    }
                })
                .concatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                        return BaseHttpFactory.getInstence()
                                .createService(GDWaterService.class, Api.BASE_URL).rwdba("update", mGreenMissionTask.getTaskid(), "rwfbldps", mGreenMissionTask.getApproved_person());

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        //发送一个远程通知
                        JPushMessageBean jPushMessageBean = new JPushMessageBean();
                        JPushMessageBean.AudienceBean audienceBean = new JPushMessageBean.AudienceBean();
                        ArrayList<String> alias = new ArrayList<>();
                        alias.add(mGreenMissionTask.getReceiver());
                        audienceBean.setAlias(alias);
                        jPushMessageBean.setAudience(audienceBean);
                        JPushMessageBean.NotificationBean notificationBean = new JPushMessageBean.NotificationBean();
                        notificationBean.setAlert("新消息(已批示)：" + mGreenMissionTask.getTask_name());
                        JPushMessageBean.NotificationBean.AndroidBean androidBean = new JPushMessageBean.NotificationBean.AndroidBean();
                        JPushMessageBean.NotificationBean.AndroidBean.ExtrasBean extrasBean = new JPushMessageBean.NotificationBean.AndroidBean.ExtrasBean();
                        extrasBean.setTaskid(mGreenMissionTask.getTaskid());
                        extrasBean.setOpenType("3");
                        androidBean.setExtras(extrasBean);
                        notificationBean.setAndroid(androidBean);
                        ArrayList<String> platforms = new ArrayList<>();
                        platforms.add("android");
                        jPushMessageBean.setPlatform(platforms);
                        jPushMessageBean.setNotification(notificationBean);
                        OkingJPushManager.getInstence().pushMessage(jPushMessageBean, new JPushMessageContract.View() {
                            @Override
                            public void pushMessageSucc(String result) {
                                Log.i("Oking5", result);
                            }

                            @Override
                            public void pushMessageFail(Throwable ex) {
                                Log.i("Oking5", ex.toString());
                            }
                        });
                        mRxDialogLoading.cancel();
                        RxToast.success("领导批示成功");
                        ApprovalActivity activity = (ApprovalActivity) getActivity();
                        activity.designatorSuccess();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRxDialogLoading.cancel();
                        RxToast.error("领导批示失败" + throwable.toString());
                    }
                });
    }

    private void examineOthersLogForAndroid(final String input, final String ajdjzt, final String bzjrId) {
        if (mGreenMissionLog!=null){
            submitData(input, ajdjzt, bzjrId);
    }else {
//
            if (mLoadBasicLogPresenter == null) {
                mLoadBasicLogPresenter = new LoadBasicLogPresenter(new LoadBasicLogContract.View() {
                    @Override
                    public void getBasicLogSucc(String result) {
                        mRxDialogLoading.cancel();
                        //{"msg":"查询成功!","datas":[{"OTHER_PART":"交通,城管","EQUIPMENT":"交通工具：001003,001001,001001  ","PLAN":"0","TYPE":"0"}],"status":"1"}

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                JSONArray datas = jsonObject.getJSONArray("datas");
                                mGreenMissionLog = new GreenMissionLog();
                                if (datas.length()>0){
                                    JSONObject object = datas.getJSONObject(0);
                                    mGreenMissionLog.setEquipment(object.getString("EQUIPMENT"));
                                    mGreenMissionLog.setServer_id(object.getString("LOG_ID"));
                                    mGreenMissionLog.setTask_id(mGreenMissionTask.getTaskid());
                                    mGreenMissionLog.setOther_part(object.getString("OTHER_PART"));
                                    mGreenMissionLog.setPlan(Integer.parseInt(object.getString("PLAN")));
                                    mGreenMissionLog.setPatrol(object.getString("PATROL"));
                                    mGreenMissionLog.setDzyj(object.getString("DZYJ"));
                                }

                                submitData(input, ajdjzt, bzjrId);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//
                    }

                    @Override
                    public void getBasicLogFail(Throwable ex) {
                        mRxDialogLoading.cancel();
                        Log.i("Oking5","获取日志失败"+ex.toString());
                        RxToast.error("获取日志失败");

                    }
                });

            }
            mLoadBasicLogPresenter.getBasicLog(mGreenMissionTask.getTaskid());

        }
    }

    private void submitData(String input, String ajdjzt, String bzjrId) {
        Map<String, Object> params = new HashMap<>();
        params.put("EXAMINE_STATUS", "1");
        params.put("LDQM", OkingContract.CURRENTUSER.getUserName());
        params.put("ID", mGreenMissionLog.getServer_id());
        params.put("LEADIDEA", input);
        params.put("username", OkingContract.CURRENTUSER.getUserName());
        params.put("xcqk", mGreenMissionLog.getPatrol());
        params.put("dzyj", mGreenMissionLog.getDzyj());
        params.put("userId", OkingContract.CURRENTUSER.getUserid());
        params.put("bzjrId", bzjrId);
        params.put("zjlx", "");
        params.put("glid", mGreenMissionTask.getTaskid());
        params.put("ajdjzt", ajdjzt);
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .examineOthersLogForAndroid(params)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(ResponseBody responseBody) throws Exception {

                        return BaseHttpFactory.getInstence()
                                .createService(GDWaterService.class, Api.BASE_URL).rwdba("update", mGreenMissionTask.getTaskid(), "rwfbldps", mGreenMissionTask.getApproved_person());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        //发送一个远程通知
                        JPushMessageBean jPushMessageBean = new JPushMessageBean();
                        JPushMessageBean.AudienceBean audienceBean = new JPushMessageBean.AudienceBean();
                        ArrayList<String> alias = new ArrayList<>();
                        alias.add(mGreenMissionTask.getReceiver());
                        audienceBean.setAlias(alias);
                        jPushMessageBean.setAudience(audienceBean);
                        JPushMessageBean.NotificationBean notificationBean = new JPushMessageBean.NotificationBean();
                        notificationBean.setAlert("新消息(已批示)：" + mGreenMissionTask.getTask_name());
                        JPushMessageBean.NotificationBean.AndroidBean androidBean = new JPushMessageBean.NotificationBean.AndroidBean();
                        JPushMessageBean.NotificationBean.AndroidBean.ExtrasBean extrasBean = new JPushMessageBean.NotificationBean.AndroidBean.ExtrasBean();
                        extrasBean.setTaskid(mGreenMissionTask.getTaskid());
                        extrasBean.setOpenType("3");
                        androidBean.setExtras(extrasBean);
                        notificationBean.setAndroid(androidBean);
                        ArrayList<String> platforms = new ArrayList<>();
                        platforms.add("android");
                        jPushMessageBean.setPlatform(platforms);
                        jPushMessageBean.setNotification(notificationBean);
                        OkingJPushManager.getInstence().pushMessage(jPushMessageBean, new JPushMessageContract.View() {
                            @Override
                            public void pushMessageSucc(String result) {
                                Log.i("Oking5", result);
                            }

                            @Override
                            public void pushMessageFail(Throwable ex) {
                                Log.i("Oking5", ex.toString());
                            }
                        });
//                        UpdateGreenMissionTaskOV updateGreenMissionTaskOV = new UpdateGreenMissionTaskOV();
//                        updateGreenMissionTaskOV.setType(100);
//                        updateGreenMissionTaskOV.setMissionTask(mGreenMissionTask);
//                        EventBus.getDefault().post(updateGreenMissionTaskOV);
//                        GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().delete(mGreenMissionTask);
                        mRxDialogLoading.cancel();
                        RxToast.success("领导批示成功");
                        ApprovalActivity activity = (ApprovalActivity) getActivity();
                        activity.designatorSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRxDialogLoading.cancel();
                        RxToast.error("领导批示失败" + throwable.toString());
                    }
                });
    }

    public void setMissionTask(GreenMissionTask greenMissionTask) {
        mGreenMissionTask = greenMissionTask;
    }

    public void setMissionLog(GreenMissionLog greenMissionLog) {
        mGreenMissionLog = greenMissionLog;
    }
}
