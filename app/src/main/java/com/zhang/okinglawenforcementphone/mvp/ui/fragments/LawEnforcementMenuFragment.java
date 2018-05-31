package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.UserRecyAdaper;
import com.zhang.okinglawenforcementphone.beans.UserItemOV;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.LawEnforcementManagerActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LawEnforcementMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recy_law_menu)
    RecyclerView mRecyLawMenu;
    Unbinder unbinder;
    private UserItemOV mUserItemOV;
    private UserRecyAdaper mLawEnforcementMenuRecyAdaper;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private StopTheIllegalActivitiesFragment mStopTheIllegalActivitiesFragment;
    private PenaltyTheSpotFragment mPenaltyTheSpotFragment;
    private LawEnforcementManagerActivity mLawEnforcementManagerActivity;
    private EnforcementInspectionNormsFragment mEnforcementInspectionNormsFragment;
    private EnforcementLanguageSpecificationFragment mEnforcementLanguageSpecificationFragment;
    private AdministrativeEnforcementFragment mAdministrativeEnforcementFragment;


    public LawEnforcementMenuFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LawEnforcementMenuFragment newInstance(String param1, String param2) {
        LawEnforcementMenuFragment fragment = new LawEnforcementMenuFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_law_enforcement_menu, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListenner();
        return mInflate;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLawEnforcementManagerActivity = (LawEnforcementManagerActivity) context;
    }

    private void setListenner() {
        mLawEnforcementMenuRecyAdaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                fragmentTransaction.hide(LawEnforcementMenuFragment.this);
                switch (position) {
                    case 0:
                        mLawEnforcementManagerActivity.setTitleText("责令停止违法行为通知");
                        if (mAdministrativeEnforcementFragment != null) {
                            fragmentTransaction.hide(mAdministrativeEnforcementFragment);
                        }

                        if (mPenaltyTheSpotFragment != null) {
                            fragmentTransaction.hide(mPenaltyTheSpotFragment);
                        }


                        if (mEnforcementInspectionNormsFragment != null) {
                            fragmentTransaction.hide(mEnforcementInspectionNormsFragment);
                        }

                        if (mEnforcementLanguageSpecificationFragment != null) {
                            fragmentTransaction.hide(mEnforcementLanguageSpecificationFragment);
                        }


                        if (mStopTheIllegalActivitiesFragment == null) {

                            mStopTheIllegalActivitiesFragment = StopTheIllegalActivitiesFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_law_content, mStopTheIllegalActivitiesFragment, "StopTheIllegalActivitiesFragment").commit();
                        } else {
                            if (mStopTheIllegalActivitiesFragment.isAdded()) {
                                fragmentTransaction.show(mStopTheIllegalActivitiesFragment).commit();
                            }
                        }


                        break;
                    case 1:
                        mLawEnforcementManagerActivity.setTitleText("水行政当场处罚决定书");
                        if (mStopTheIllegalActivitiesFragment != null) {
                            fragmentTransaction.hide(mStopTheIllegalActivitiesFragment);
                        }

                        if (mPenaltyTheSpotFragment == null) {

                            mPenaltyTheSpotFragment = PenaltyTheSpotFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_law_content, mPenaltyTheSpotFragment, "PenaltyTheSpotFragment").commit();
                        } else {
                            if (mPenaltyTheSpotFragment.isAdded()) {
                                fragmentTransaction.show(mPenaltyTheSpotFragment).commit();
                            }
                        }
                        break;
                    case 2:
                        mLawEnforcementManagerActivity.setTitleText("水行政执法检查行为规范");
                        if (mStopTheIllegalActivitiesFragment != null) {
                            fragmentTransaction.hide(mStopTheIllegalActivitiesFragment);
                        }

                        if (mPenaltyTheSpotFragment != null) {
                            fragmentTransaction.hide(mPenaltyTheSpotFragment);
                        }


                        if (mEnforcementLanguageSpecificationFragment != null) {
                            fragmentTransaction.hide(mEnforcementLanguageSpecificationFragment);
                        }
                        if (mAdministrativeEnforcementFragment != null) {
                            fragmentTransaction.hide(mAdministrativeEnforcementFragment);
                        }

                        if (mEnforcementInspectionNormsFragment == null) {
                            mEnforcementInspectionNormsFragment = EnforcementInspectionNormsFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_law_content, mEnforcementInspectionNormsFragment, "EnforcementInspectionNormsFragment").commit();
                        } else {
                            if (mEnforcementInspectionNormsFragment.isAdded()) {
                                fragmentTransaction.show(mEnforcementInspectionNormsFragment).commit();
                            }
                        }


                        break;
                    case 3:
                        mLawEnforcementManagerActivity.setTitleText("水行政执法用语规范");
                        if (mStopTheIllegalActivitiesFragment != null) {
                            fragmentTransaction.hide(mStopTheIllegalActivitiesFragment);
                        }

                        if (mPenaltyTheSpotFragment != null) {
                            fragmentTransaction.hide(mPenaltyTheSpotFragment);
                        }

                        if (mEnforcementInspectionNormsFragment != null) {
                            fragmentTransaction.hide(mEnforcementInspectionNormsFragment);
                        }
                        if (mAdministrativeEnforcementFragment != null) {
                            fragmentTransaction.hide(mAdministrativeEnforcementFragment);
                        }

                        if (mEnforcementLanguageSpecificationFragment == null) {
                            mEnforcementLanguageSpecificationFragment = EnforcementLanguageSpecificationFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_law_content, mEnforcementLanguageSpecificationFragment, "EnforcementLanguageSpecificationFragment").commit();
                        } else {
                            if (mEnforcementLanguageSpecificationFragment.isAdded()) {
                                fragmentTransaction.show(mEnforcementLanguageSpecificationFragment).commit();
                            }
                        }


                        break;

                    case 4:
                        mLawEnforcementManagerActivity.setTitleText("水行政执法禁令");
                        if (mStopTheIllegalActivitiesFragment != null) {
                            fragmentTransaction.hide(mStopTheIllegalActivitiesFragment);
                        }

                        if (mPenaltyTheSpotFragment != null) {
                            fragmentTransaction.hide(mPenaltyTheSpotFragment);
                        }


                        if (mEnforcementInspectionNormsFragment != null) {
                            fragmentTransaction.hide(mEnforcementInspectionNormsFragment);
                        }

                        if (mEnforcementLanguageSpecificationFragment != null) {
                            fragmentTransaction.hide(mEnforcementLanguageSpecificationFragment);
                        }

                        if (mAdministrativeEnforcementFragment == null) {
                            mAdministrativeEnforcementFragment = AdministrativeEnforcementFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_law_content, mAdministrativeEnforcementFragment, "AdministrativeEnforcementFragment").commit();
                        } else {
                            if (mAdministrativeEnforcementFragment.isAdded()) {
                                fragmentTransaction.show(mAdministrativeEnforcementFragment).commit();
                            }
                        }

                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void initData() {
        mRecyLawMenu.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mRecyLawMenu.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 20, getResources().getColor(R.color.activity_bg)));

        List<UserItemOV> userItemOVS = new ArrayList<>();
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("责令停止违法行为通知");
        mUserItemOV.setIcon(R.mipmap.zeting);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("水行政当场处罚决定书");
        mUserItemOV.setIcon(R.mipmap.sxzcfjds);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("水行政执法检查行为规范");
        mUserItemOV.setIcon(R.mipmap.falvfaguiku);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("水行政执法用语规范");
        mUserItemOV.setIcon(R.mipmap.zfgf);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("水行政执法禁令");
        mUserItemOV.setIcon(R.mipmap.zfjl);
        userItemOVS.add(mUserItemOV);

        mLawEnforcementMenuRecyAdaper = new UserRecyAdaper(R.layout.user_item, userItemOVS);
        mLawEnforcementMenuRecyAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mRecyLawMenu.setAdapter(mLawEnforcementMenuRecyAdaper);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
