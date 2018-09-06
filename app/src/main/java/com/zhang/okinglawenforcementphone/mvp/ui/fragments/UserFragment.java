package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMClient;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.GlideApp;
import com.zhang.baselib.utils.ActivityUtil;
import com.zhang.baselib.utils.DeviceUtil;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.UserRecyAdaper;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.UserItemOV;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.AboutActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ChangePasswordActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.FeedbackActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.SettingActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的
 */
public class UserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.ry_user)
    RecyclerView mRyUser;
    Unbinder unbinder;
    @BindView(R.id.bt_loginout)
    TextView btLoginout;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private View mUserHead;
    private UserItemOV mUserItemOV;
    private ImageView mSimpleDraweeView;
    private TextView mTvName;
    private TextView mTvDepat;
    private UserRecyAdaper mUserRecyAdaper;
    private Intent mIntent;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_user, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initView();
        intData();
        setListener();
        return mInflate;
    }

    private void setListener() {


        mUserRecyAdaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        mIntent = new Intent(getActivity(), ChangePasswordActivity.class);
                        startActivity(mIntent);
                        break;
                    case 1:                         //意见反馈
                        mIntent = new Intent(getActivity(), FeedbackActivity.class);
                        startActivity(mIntent);
                        break;
                    case 2:
                        mIntent = new Intent(getActivity(), AboutActivity.class);
                        startActivity(mIntent);
                        break;
                    case 3:
                        mIntent = new Intent(getActivity(), SettingActivity.class);
                        startActivity(mIntent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initView() {
        mUserHead = View.inflate(BaseApplication.getApplictaion(), R.layout.user_head, null);
        mSimpleDraweeView = mUserHead.findViewById(R.id.main_sdv);
        GlideApp.with(UserFragment.this)
                .load(R.mipmap.headerimg)
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher_logo)
                .error(R.drawable.loadfail)
                .into(mSimpleDraweeView);
        mTvName = mUserHead.findViewById(R.id.tv_name);
        mTvDepat = mUserHead.findViewById(R.id.tv_depat);

        mRyUser.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRyUser.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 1, 10, Color.DKGRAY));

    }

    private void intData() {
        mTvName.setText(OkingContract.CURRENTUSER.getUserName());
        mTvDepat.setText(OkingContract.CURRENTUSER.getDeptname());
        List<UserItemOV> userItemOVS = new ArrayList<>();
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("修改密码");
        mUserItemOV.setIcon(R.mipmap.reset_pwd);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("意见反馈");
        mUserItemOV.setIcon(R.mipmap.feedback);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("关于      v"+ DeviceUtil.getAppVersionName(BaseApplication.getApplictaion()));
        mUserItemOV.setIcon(R.mipmap.about);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("设置");
        mUserItemOV.setIcon(R.mipmap.setting);
        userItemOVS.add(mUserItemOV);

        mUserRecyAdaper = new UserRecyAdaper(R.layout.user_item, userItemOVS);
        mUserRecyAdaper.addHeaderView(mUserHead);
        mUserRecyAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mRyUser.setAdapter(mUserRecyAdaper);
    }


    @OnClick(R.id.bt_loginout)
    public void onViewClicked(View view) {
        EMClient.getInstance().logout(true);
        ActivityUtil.AppExit(BaseApplication.getApplictaion());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
