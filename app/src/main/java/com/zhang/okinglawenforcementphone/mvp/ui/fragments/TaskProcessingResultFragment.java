package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.MemberOV;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MemberSignActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MissionRecorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TaskProcessingResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.sw_leader_summary)
    Switch mSwLeaderSummary;
    @BindView(R.id.leader_summary_editText)
    EditText mLeaderSummaryEditText;
    @BindView(R.id.sign_btn)
    Button mSignBtn;
    @BindView(R.id.member_textView)
    TextView mMemberTextView;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private GreenMissionLog mGreenMissionLog;
    private GreenMissionTask mGreenMissionTask;
    private boolean mLeaderSummarySwisopen = false;
    private Intent mIntent;

    public TaskProcessingResultFragment() {
        // Required empty public constructor
    }

    public static TaskProcessingResultFragment newInstance(String param1, String param2) {
        TaskProcessingResultFragment fragment = new TaskProcessingResultFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_task_processing_result, container, false);

        }
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListerner();
        return mInflate;
    }

    private void setListerner() {
        mSwLeaderSummary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mLeaderSummarySwisopen = isChecked;
                if (isChecked) {
                    mLeaderSummaryEditText.setEnabled(false);
                    mLeaderSummaryEditText.setText("");
                } else {
                    mLeaderSummaryEditText.setEnabled(true);
                }
            }
        });

        if (mGreenMissionTask.getStatus().equals("5")) {
            mSwLeaderSummary.setEnabled(false);
            mLeaderSummaryEditText.setFocusable(false);
        } else if (mGreenMissionTask.getStatus().equals("9")) {
            mSwLeaderSummary.setEnabled(true);
        }


    }

    private void initData() {
        if (mGreenMissionTask.getStatus().equals("100") || mGreenMissionTask.getStatus().equals("5") || mGreenMissionTask.getStatus().equals("9")) {
            mSignBtn.setVisibility(View.VISIBLE);
        }
        mLeaderSummaryEditText.setText(mGreenMissionLog.getDzyj());
        //签名
        String memberStr = "";
        mGreenMissionTask.resetMembers();
        if (mGreenMissionTask.getMembers() != null) {
            for (int i = 0; i < mGreenMissionTask.getMembers().size(); i++) {
                GreenMember member = mGreenMissionTask.getMembers().get(i);
                Log.i("Oking5","队员："+member.toString());
                if (member.getUsername().equals(mGreenMissionTask.getReceiver_name())) {
                    memberStr += "<font color=\"#88000000\">任务负责人：</font>";
                    if (member.getSignPic() == null || !new File(member.getSignPic()).exists()) {
                        memberStr += "<font color=\"#88000000\">" + mGreenMissionTask.getReceiver_name() + "&emsp;&emsp;</font>";
                    } else {
                        memberStr += "<font color=\"#98CF60\">" + mGreenMissionTask.getReceiver_name() + "&emsp;&emsp;</font>";
                    }
                    memberStr += "<font color=\"#88000000\">队员：</font>";
                    continue;
                }


                if (member.getSignPic() == null || !new File(member.getSignPic()).exists()) {
                    //未签名
                    memberStr += "<font color=\"#88000000\">" + member.getUsername() + "&emsp;</font>";
                } else {
                    //已签名
                    memberStr += "<font color=\"#98CF60\">" + member.getUsername() + "&emsp;</font>";
                }
            }
        }

        mMemberTextView.setText(Html.fromHtml(memberStr));
    }

    public void setMission(GreenMissionTask mission) {
        mGreenMissionTask = mission;
        if (mGreenMissionTask.getStatus().equals("100") || mGreenMissionTask.getStatus().equals("5") || mGreenMissionTask.getStatus().equals("9")) {
           if (mSignBtn!=null){

               mSignBtn.setVisibility(View.VISIBLE);
           }
        }
    }

    public void setGreenMissionLog(GreenMissionLog greenMissionLog) {
        mGreenMissionLog = greenMissionLog;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(MemberOV memberOV) {
        refreshMember();
    }

    private void refreshMember() {
        //签名
        String memberStr = "";
        mGreenMissionTask.resetMembers();
        if (mGreenMissionTask.getMembers() != null) {
            for (int i = 0; i < mGreenMissionTask.getMembers().size(); i++) {
                GreenMember member = mGreenMissionTask.getMembers().get(i);
                if (member.getUsername().equals(mGreenMissionTask.getReceiver_name())) {
                    memberStr += "<font color=\"#88000000\">任务负责人：</font>";
                    if (member.getSignPic() == null || !new File(member.getSignPic()).exists()) {
                        memberStr += "<font color=\"#88000000\">" + mGreenMissionTask.getReceiver_name() + "&emsp;&emsp;</font>";
                    } else {
                        memberStr += "<font color=\"#98CF60\">" + mGreenMissionTask.getReceiver_name() + "&emsp;&emsp;</font>";
                    }
                    memberStr += "<font color=\"#88000000\">队员：</font>";
                    continue;
                }


                if (member.getSignPic() == null || !new File(member.getSignPic()).exists()) {
                    //未签名
                    memberStr += "<font color=\"#88000000\">" + member.getUsername() + "&emsp;</font>";
                } else {
                    //已签名
                    memberStr += "<font color=\"#98CF60\">" + member.getUsername() + "&emsp;</font>";
                }
            }
        }
        mMemberTextView.setText(Html.fromHtml(memberStr));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @OnClick(R.id.sign_btn)
    public void onViewClicked() {
        mIntent = new Intent(getActivity(), MemberSignActivity.class);
        mIntent.putExtra("id", mGreenMissionTask.getId());
        startActivity(mIntent);
    }

    public void saveResults() {
        mGreenMissionLog.setLeaderSummarySwisopen(mLeaderSummarySwisopen);
        mGreenMissionLog.setDzyj(mLeaderSummaryEditText.getText().toString().trim());
        GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().update(mGreenMissionLog);
    }

}
