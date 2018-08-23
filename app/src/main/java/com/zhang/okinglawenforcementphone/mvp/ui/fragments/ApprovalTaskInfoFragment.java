package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ApprovalTaskInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tv_taskname)
    TextInputEditText mTvTaskname;
    @BindView(R.id.tv_source)
    TextInputEditText mTvSource;
    @BindView(R.id.publisher_tv)
    TextInputEditText mPublisherTv;
    @BindView(R.id.tv_approver)
    TextInputEditText mTvApprover;
    @BindView(R.id.tv_recipient)
    TextInputEditText mTvRecipient;
    @BindView(R.id.tv_emergency)
    TextInputEditText mTvEmergency;
    @BindView(R.id.tv_begintime)
    TextInputEditText mTvBegintime;
    @BindView(R.id.tv_endtime)
    TextInputEditText mTvEndtime;
    @BindView(R.id.list_item_missionDetail)
    TextInputEditText mListItemMissionDetail;
    @BindView(R.id.tv_description)
    TextInputEditText mTvDescription;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private GreenMissionTask mGreenMissionTask;
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ApprovalTaskInfoFragment() {
        // Required empty public constructor
    }

    public static ApprovalTaskInfoFragment newInstance(String param1, String param2) {
        ApprovalTaskInfoFragment fragment = new ApprovalTaskInfoFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_approval_task_info, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        return mInflate;
    }

    private void initData() {
        mTvTaskname.setText(mGreenMissionTask.getTask_name());
        if ("0".equals(mGreenMissionTask.getRwly())) {
            mTvSource.setText("上级交办");
        } else if ("1".equals(mGreenMissionTask.getRwly())) {
            mTvSource.setText("部门移送");
        } else if ("2".equals(mGreenMissionTask.getRwly())) {
            mTvSource.setText("系统报警");
        } else if ("3".equals(mGreenMissionTask.getRwly())) {
            mTvSource.setText("日常巡查");
        } else if ("4".equals(mGreenMissionTask.getRwly())) {
            mTvSource.setText("媒体披露");
        } else if ("5".equals(mGreenMissionTask.getRwly())) {
            mTvSource.setText("群众举报");
        }
        mPublisherTv.setText(mGreenMissionTask.getPublisher_name());
        mTvApprover.setText(mGreenMissionTask.getApproved_person_name());
        mTvRecipient.setText(mGreenMissionTask.getFbr());
        if ("0".equals(mGreenMissionTask.getJjcd())) {
            mTvEmergency.setText("特急");
        } else if ("紧急".equals(mGreenMissionTask.getJjcd())) {
            mTvEmergency.setText("紧急");
        } else if ("一般".equals(mGreenMissionTask.getJjcd())) {
            mTvEmergency.setText("一般");
        } else {
            mTvEmergency.setText("紧急");
        }
        mTvBegintime.setText(mDateFormat.format(mGreenMissionTask.getBegin_time()));
        mTvEndtime.setText(mDateFormat.format(mGreenMissionTask.getEnd_time()));
        mListItemMissionDetail.setText(mGreenMissionTask.getTask_area());
        mTvDescription.setText(mGreenMissionTask.getTask_content());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setMissionTask(GreenMissionTask missionTask) {
        mGreenMissionTask = missionTask;
    }
}
