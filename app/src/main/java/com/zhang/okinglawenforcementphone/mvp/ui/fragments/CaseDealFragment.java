package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zhang.okinglawenforcementphone.GreenDAOMannager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenCase;
import com.zhang.okinglawenforcementphone.beans.GreenCaseDao;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.CaseDealActivity;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.http.PUT;

public class CaseDealFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.ajid_tv)
    TextView mAjidTv;
    @BindView(R.id.ajlx_tv)
    TextView mAjlxTv;
    @BindView(R.id.ajly_tv)
    TextView mAjlyTv;
    @BindView(R.id.ajmc_tv)
    TextView mAjmcTv;
    @BindView(R.id.slsj_tv)
    TextView mSlsjTv;
    @BindView(R.id.dsr_tv)
    TextView mDsrTv;
    @BindView(R.id.afdd_tv)
    TextView mAfddTv;
    @BindView(R.id.aqjy_tv)
    TextView mAqjyTv;
    @BindView(R.id.ajzt_tv)
    TextView mAjztTv;
    @BindView(R.id.change_btn)
    Button mChangeBtn;
    Unbinder unbinder;
    private GreenCase mUnique;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public CaseDealFragment() {
        // Required empty public constructor
    }

    public static CaseDealFragment newInstance(String param1, String param2) {
        CaseDealFragment fragment = new CaseDealFragment();
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
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_case_deal, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        return mInflate;
    }

    private void initData() {
        if (mUnique != null) {
            mAjlxTv.setText(mUnique.getAJLX());
            mAjlyTv.setText(mUnique.getAJLY());
            mAjmcTv.setText(mUnique.getAJMC());

            mSlsjTv.setText(mSimpleDateFormat.format(mUnique.getSLRQ()));
            mDsrTv.setText(mUnique.getDSRQK());
            mAfddTv.setText(mUnique.getAFDD());
            mAqjyTv.setText(mUnique.getAQJY());
            mAjidTv.setText(mUnique.getAJID());
            switch (mUnique.getSLXX_ZT()) {
                case "SL":
                    mAjztTv.setText("受理");
                    break;
                case "CBBDCQZ":
                    mAjztTv.setText("承办并调查取证");
                    break;
                case "ZB":
                    mAjztTv.setText("转办");
                    break;
                case "LA":
                    mAjztTv.setText("立案");
                    break;
                case "AJSC":
                    mAjztTv.setText("案件审查");
                    break;
                case "BYCF":
                    mAjztTv.setText("不予处罚");
                    break;
                case "WSZL":
                    mAjztTv.setText("完善资料");
                    break;
                case "YS":
                    mAjztTv.setText("移送");
                    break;
                case "CFGZHTZ":
                    mAjztTv.setText("处罚告知或听证");
                    break;
                case "TZ":
                    mAjztTv.setText("听证");
                    break;
                case "FH":
                    mAjztTv.setText("复核");
                    break;
                case "CFJD":
                    mAjztTv.setText("处罚决定");
                    break;
                case "ZX":
                    mAjztTv.setText("执行");
                    break;
                case "JABGD":
                    mAjztTv.setText("结案并归档");
                    break;
                default:
                    mAjztTv.setText("");
                    break;
            }

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.change_btn, R.id.evidence_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_btn:
                getActivity().finish();
                break;
            case R.id.evidence_btn:
                Fragment caseDealFragment = getFragmentManager().findFragmentByTag("caseDealFragment");

                if (caseDealFragment.isAdded()){
                    getFragmentManager().beginTransaction().hide(caseDealFragment).commit();
                }

                CaseEvidenceFragment caseEvidenceFragment = CaseEvidenceFragment.newInstance(null);
                getFragmentManager().beginTransaction().add(R.id.sub_fragment_root, caseEvidenceFragment,"caseEvidenceFragment").commit();
//                mChangeBtn.setEnabled(false);
                break;
            default:
                break;
        }

    }



    public void setGreenCase(GreenCase greenCase) {
        this.mUnique = greenCase;
    }
}
