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
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.CaseManagerActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HandlingMenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recy_law_menu)
    RecyclerView mRecyLawMenu;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private Fragment mHandlingMenuFragment;

    private UserItemOV mUserItemOV;
    private UserRecyAdaper mHandingMenuRecyAdaper;
    private CaseManagerActivity mCaseManagerActivity;
    private CaseRegistrationFragment mCaseRegistrationFragment;
    private OpenCasesFragment mOpenCasesFragment;
    private CaseProcessingListFragment mCaseProcessingListFragment;
    private CaseComplaintFragment mCaseComplaintFragment;
    private CaseInAdvanceFragment mCaseInAdvanceFragment;

    public HandlingMenuFragment() {
        // Required empty public constructor
    }

    public static HandlingMenuFragment newInstance(String param1, String param2) {
        HandlingMenuFragment fragment = new HandlingMenuFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_handling_menu, container, false);

        }

        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListenner();
        return mInflate;
    }

    private void setListenner() {
        mHandingMenuRecyAdaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                if (!mHandlingMenuFragment.isHidden()) {

                    fragmentTransaction.hide(mHandlingMenuFragment);
                }
                switch (position) {
                    case 0:
                        mCaseManagerActivity.setTitleText("案件登记");
                        if (mOpenCasesFragment != null) {
                            fragmentTransaction.hide(mOpenCasesFragment);
                        }

                        if (mCaseProcessingListFragment != null) {
                            fragmentTransaction.hide(mCaseProcessingListFragment);
                        }

                        if (mCaseComplaintFragment != null) {
                            fragmentTransaction.hide(mCaseComplaintFragment);
                        }

                        if (mCaseInAdvanceFragment != null) {
                            fragmentTransaction.hide(mCaseInAdvanceFragment);
                        }
                        if (mCaseRegistrationFragment == null) {

                            mCaseRegistrationFragment = CaseRegistrationFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_case_content, mCaseRegistrationFragment, "CaseRegistrationFragment").commit();
                        } else {
                            if (mCaseRegistrationFragment.isAdded()) {
                                fragmentTransaction.show(mCaseRegistrationFragment).commit();
                            }
                        }


                        break;
                    case 1:
                        mCaseManagerActivity.setTitleText("案件受理");
                        if (mCaseRegistrationFragment != null) {
                            fragmentTransaction.hide(mCaseRegistrationFragment);
                        }

                        if (mCaseProcessingListFragment != null) {
                            fragmentTransaction.hide(mCaseProcessingListFragment);
                        }

                        if (mCaseComplaintFragment != null) {
                            fragmentTransaction.hide(mCaseComplaintFragment);
                        }
                        if (mCaseInAdvanceFragment != null) {
                            fragmentTransaction.hide(mCaseInAdvanceFragment);
                        }
                        if (mOpenCasesFragment == null) {

                            mOpenCasesFragment = OpenCasesFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_case_content, mOpenCasesFragment, "OpenCasesFragment").commit();
                        } else {
                            if (mOpenCasesFragment.isAdded()) {
                                fragmentTransaction.show(mOpenCasesFragment).commit();
                            }
                        }
                        break;
                    case 2:
                        mCaseManagerActivity.setTitleText("案件处理");
                        if (mCaseRegistrationFragment != null) {
                            fragmentTransaction.hide(mCaseRegistrationFragment);
                        }

                        if (mOpenCasesFragment != null) {
                            fragmentTransaction.hide(mOpenCasesFragment);
                        }

                        if (mCaseComplaintFragment != null) {
                            fragmentTransaction.hide(mCaseComplaintFragment);
                        }
                        if (mCaseInAdvanceFragment != null) {
                            fragmentTransaction.hide(mCaseInAdvanceFragment);
                        }

                        if (mCaseProcessingListFragment == null) {

                            mCaseProcessingListFragment = CaseProcessingListFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_case_content, mCaseProcessingListFragment, "CaseProcessingListFragment").commit();
                        } else {
                            if (mCaseProcessingListFragment.isAdded()) {
                                fragmentTransaction.show(mCaseProcessingListFragment).commit();
                            }
                        }
                        break;
                    case 3:
                        mCaseManagerActivity.setTitleText("案件转办");
                        if (mCaseRegistrationFragment != null) {
                            fragmentTransaction.hide(mCaseRegistrationFragment);
                        }

                        if (mOpenCasesFragment != null) {
                            fragmentTransaction.hide(mOpenCasesFragment);
                        }

                        if (mCaseProcessingListFragment != null) {
                            fragmentTransaction.hide(mCaseProcessingListFragment);
                        }

                        if (mCaseInAdvanceFragment != null) {
                            fragmentTransaction.hide(mCaseInAdvanceFragment);
                        }

                        if (mCaseComplaintFragment == null) {
                            mCaseComplaintFragment = CaseComplaintFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_case_content, mCaseComplaintFragment, "CaseComplaintFragment").commit();

                        } else {
                            if (mCaseComplaintFragment.isAdded()) {
                                fragmentTransaction.show(mCaseComplaintFragment).commit();
                            }
                        }
                        break;
                    case 4:
                        mCaseManagerActivity.setTitleText("预立案");
                        if (mCaseRegistrationFragment != null) {
                            fragmentTransaction.hide(mCaseRegistrationFragment);
                        }

                        if (mOpenCasesFragment != null) {
                            fragmentTransaction.hide(mOpenCasesFragment);
                        }
                        if (mCaseComplaintFragment != null) {
                            fragmentTransaction.hide(mCaseComplaintFragment);
                        }
                        if (mCaseProcessingListFragment != null) {
                            fragmentTransaction.hide(mCaseProcessingListFragment);
                        }

                        if (mCaseInAdvanceFragment == null) {
                            mCaseInAdvanceFragment = CaseInAdvanceFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.rl_case_content, mCaseInAdvanceFragment, "CaseInAdvanceFragment").commit();

                        } else {
                            if (mCaseInAdvanceFragment.isAdded()) {
                                fragmentTransaction.show(mCaseInAdvanceFragment).commit();
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
        mHandlingMenuFragment = getFragmentManager().findFragmentByTag("HandlingMenuFragment");
        mRecyLawMenu.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mRecyLawMenu.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 20, getResources().getColor(R.color.activity_bg)));

        List<UserItemOV> userItemOVS = new ArrayList<>();
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("案件登记");
        mUserItemOV.setIcon(R.mipmap.ajdj);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("案件受理");
        mUserItemOV.setIcon(R.mipmap.ajsl);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("案件处理");
        mUserItemOV.setIcon(R.mipmap.ajcl);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("案件转办");
        mUserItemOV.setIcon(R.mipmap.ajzb);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("预立案");
        mUserItemOV.setIcon(R.mipmap.yla);
        userItemOVS.add(mUserItemOV);

        mHandingMenuRecyAdaper = new UserRecyAdaper(R.layout.user_item, userItemOVS);
        mHandingMenuRecyAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mRecyLawMenu.setAdapter(mHandingMenuRecyAdaper);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCaseManagerActivity = (CaseManagerActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
