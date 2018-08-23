package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadBasicLogContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadBasicLogPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ApprovalTheLogFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.type_nature)
    TextInputEditText mTypeNature;
    @BindView(R.id.tv_tasktype)
    TextInputEditText mTvTasktype;
    @BindView(R.id.plan_spinner)
    TextInputEditText mPlanSpinner;
    @BindView(R.id.item_spinner)
    TextInputEditText mItemSpinner;
    @BindView(R.id.equipment_textView)
    TextInputEditText mEquipmentTextView;
    @BindView(R.id.tv_patrol)
    TextInputEditText mTvPatrol;
    @BindView(R.id.tv_result)
    TextInputEditText mTvResult;
    @BindView(R.id.tv_law_enforcement)
    TextInputEditText mTvLawEnforcement;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private GreenMissionLog mGreenMissionLog;
    private RxDialogLoading mRxDialogLoading;
    private LoadBasicLogPresenter mLoadBasicLogPresenter;
    private GreenMissionTask mGreenMissionTask;

    public ApprovalTheLogFragment() {
        // Required empty public constructor
    }

    public static ApprovalTheLogFragment newInstance(String param1, String param2) {
        ApprovalTheLogFragment fragment = new ApprovalTheLogFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_approval_the_log, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        return mInflate;
    }

    private void initData() {
        if (mGreenMissionLog == null) {
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
            //{"msg":"查询成功!","datas":[{"OTHER_PART":"交通,城管","EQUIPMENT":"交通工具：001003,001001,001001  ","PLAN":"0","TYPE":"0"}],"status":"1"}
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
                                for (int i = 0; i < datas.length(); i++) {

                                    JSONObject object = datas.getJSONObject(i);
                                    mGreenMissionLog.setEquipment(object.getString("EQUIPMENT"));
                                    mGreenMissionLog.setServer_id(object.getString("LOG_ID"));
                                    mGreenMissionLog.setTask_id(mGreenMissionTask.getTaskid());
                                    mGreenMissionLog.setOther_part(object.getString("OTHER_PART"));
                                    mGreenMissionLog.setPlan(Integer.parseInt(object.getString("PLAN")));
                                    mGreenMissionLog.setPatrol(object.getString("PATROL"));
                                    mGreenMissionLog.setDzyj(object.getString("DZYJ"));

                                }
                                GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao()
                                        .insert(mGreenMissionLog);
                                setTaskLogData();
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
        } else {
            setTaskLogData();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setMissionLog(GreenMissionLog greenMissionLog) {
        mGreenMissionLog = greenMissionLog;
    }

    private void setTaskLogData() {
        mTypeNature.setText(mGreenMissionTask.getTypename());

        if ("0".equals(mGreenMissionTask.getTypeoftask())) {
            mTvTasktype.setText("河道管理");
        } else if ("1".equals(mGreenMissionTask.getTypeoftask())) {
            mTvTasktype.setText("河道采砂");
        } else if ("2".equals(mGreenMissionTask.getTypeoftask())) {
            mTvTasktype.setText("水资源管理");
        } else if ("3".equals(mGreenMissionTask.getTypeoftask())) {
            mTvTasktype.setText("水土保持管理");
        } else if ("4".equals(mGreenMissionTask.getTypeoftask())) {
            mTvTasktype.setText("水利工程管理");
        }

        if (0 == mGreenMissionLog.getPlan()) {
            mPlanSpinner.setText("月计划");
        } else if (1 == mGreenMissionLog.getPlan()) {
            mPlanSpinner.setText("季度计划");
        } else if (2 == mGreenMissionLog.getPlan()) {
            mPlanSpinner.setText("年计划");
        }

        if (0 == mGreenMissionLog.getItem()) {
            mItemSpinner.setText("河道管理执法巡查");
        } else if (1 == mGreenMissionLog.getItem()) {
            mItemSpinner.setText("河道采砂管理执法巡查");
        } else if (2 == mGreenMissionLog.getItem()) {
            mItemSpinner.setText("水资源管理执法巡查");
        } else if (3 == mGreenMissionLog.getItem()) {
            mItemSpinner.setText("水土保持管理执法巡查");
        } else if (4 == mGreenMissionLog.getItem()) {
            mItemSpinner.setText("水利工程管理执法巡查");
        } else if (5 == mGreenMissionLog.getItem()) {
            mItemSpinner.setText("举报案件核查巡查");
        } else if (6 == mGreenMissionLog.getItem()) {
            mItemSpinner.setText("其他");
        }

        String part = mGreenMissionLog.getOther_part();
        mTvLawEnforcement.setText(part);
        mEquipmentTextView.setText(mGreenMissionLog.getEquipment());

        mTvPatrol.setText(mGreenMissionLog.getPatrol());
        mTvResult.setText(mGreenMissionLog.getDzyj());


    }

    public void setMissionTask(GreenMissionTask greenMissionTask) {
        mGreenMissionTask = greenMissionTask;
    }
}
