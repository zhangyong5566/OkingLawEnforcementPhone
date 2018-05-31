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
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recy_setting_list)
    RecyclerView mRecySettingList;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private UserItemOV mUserItemOV;
    private UserRecyAdaper mSettingListRecyAdaper;
    private PhoneBookFragment mPhoneBookFragment;
    private OfflineMapManagerFragment mOfflineMapManagerFragment;


    public SettingListFragment() {
        // Required empty public constructor
    }

    public static SettingListFragment newInstance(String param1, String param2) {
        SettingListFragment fragment = new SettingListFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_setting_list, container, false);
        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListener();
        return mInflate;
    }

    private void setListener() {
        mSettingListRecyAdaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            private FragmentTransaction mFragmentTransaction;

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mFragmentTransaction = getFragmentManager().beginTransaction();
                mFragmentTransaction.hide(SettingListFragment.this);
                switch (position) {
                    case 0:
                        if (mPhoneBookFragment == null) {

                            mPhoneBookFragment = PhoneBookFragment.newInstance(null, null);
                            mFragmentTransaction.add(R.id.rl_setting_content, mPhoneBookFragment, "PhoneBookFragment").commit();
                        } else {
                            if (mPhoneBookFragment.isAdded()) {
                                mFragmentTransaction.show(mPhoneBookFragment).commit();
                            }
                        }


                        break;
                    case 1:

                        if (mOfflineMapManagerFragment == null) {

                            mOfflineMapManagerFragment = OfflineMapManagerFragment.newInstance(null, null);
                            mFragmentTransaction.add(R.id.rl_setting_content, mOfflineMapManagerFragment, "OfflineMapManagerFragment").commit();
                        } else {
                            if (mOfflineMapManagerFragment.isAdded()) {
                                mFragmentTransaction.show(mOfflineMapManagerFragment).commit();
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
        List<UserItemOV> userItemOVS = new ArrayList<>();
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("电话簿");
        mUserItemOV.setIcon(R.mipmap.phon_book);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("离线地图");
        mUserItemOV.setIcon(R.mipmap.offlin_map);
        userItemOVS.add(mUserItemOV);
        mSettingListRecyAdaper = new UserRecyAdaper(R.layout.user_item, userItemOVS);
        mRecySettingList.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mRecySettingList.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 20, getResources().getColor(R.color.activity_bg)));

        mSettingListRecyAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mRecySettingList.setAdapter(mSettingListRecyAdaper);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
