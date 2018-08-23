package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaskPatrolFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.sw_summary)
    Switch mSwSummary;
    @BindView(R.id.summary_editText)
    EditText mSummaryEditText;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private GreenMissionLog mGreenMissionLog;
    private GreenMissionTask mGreenMissionTask;
    private boolean mSummarySwisopen = false;
    public TaskPatrolFragment() {
        // Required empty public constructor
    }

    public static TaskPatrolFragment newInstance(String param1, String param2) {
        TaskPatrolFragment fragment = new TaskPatrolFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_task_patrol, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListerner();
        return mInflate;
    }

    private void setListerner() {
        mSwSummary.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mSummarySwisopen = isChecked;
                if (isChecked) {
                    mSummaryEditText.setEnabled(false);
                    mSummaryEditText.setText("");
                } else {
                    mSummaryEditText.setEnabled(true);
                }
            }
        });
        if (mGreenMissionTask.getStatus().equals("5")) {
            mSwSummary.setEnabled(false);
            mSummaryEditText.setFocusable(false);
        } else if (mGreenMissionTask.getStatus().equals("9")) {
            mSwSummary.setEnabled(true);
        }
    }

    private void initData() {
        mSummaryEditText.setText(mGreenMissionLog.getPatrol());
    }

    public void setMission(GreenMissionTask mission) {
        mGreenMissionTask = mission;
    }

    public void setGreenMissionLog(GreenMissionLog greenMissionLog) {
        mGreenMissionLog = greenMissionLog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void savePatrol() {
        mGreenMissionLog.setSummarySwisopen(mSummarySwisopen);
        mGreenMissionLog.setPatrol(mSummaryEditText.getText().toString().trim());
        GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().update(mGreenMissionLog);
    }
}
