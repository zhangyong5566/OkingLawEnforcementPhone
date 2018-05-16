package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.UserRecyAdaper;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.UserItemOV;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.layoutSwipeRefresh)
    SwipeRefreshLayout mLayoutSwipeRefresh;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private View mUserHead;
    private UserItemOV mUserItemOV;
    private SimpleDraweeView mSimpleDraweeView;
    private TextView mTvName;
    private TextView mTvDepat;
private Handler mHandler = new Handler();

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
        Log.i("Oking","UserFragment>>>>onCreateView");
        if (mInflate == null) {
            Log.i("Oking","UserFragment>>>>mInflate==null");
            mInflate = inflater.inflate(R.layout.fragment_user, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initView();
        intData();
        setListener();
        return mInflate;
    }

    private void setListener() {
        mLayoutSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLayoutSwipeRefresh.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void initView() {
        mLayoutSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.refresh_color));
        mUserHead = View.inflate(BaseApplication.getApplictaion(), R.layout.user_head, null);
        mSimpleDraweeView = mUserHead.findViewById(R.id.main_sdv);
        mTvName = mUserHead.findViewById(R.id.tv_name);
        mTvDepat = mUserHead.findViewById(R.id.tv_depat);

        mRyUser.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRyUser.setItemAnimator(new DefaultItemAnimator());
        mRyUser.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 1, 5, Color.DKGRAY));

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
        mUserItemOV.setTitle("关于");
        mUserItemOV.setIcon(R.mipmap.about);
        userItemOVS.add(mUserItemOV);
        mUserItemOV = new UserItemOV();
        mUserItemOV.setTitle("设置");
        mUserItemOV.setIcon(R.mipmap.setting);
        userItemOVS.add(mUserItemOV);

        UserRecyAdaper userRecyAdaper = new UserRecyAdaper(R.layout.user_item, userItemOVS);
        userRecyAdaper.addHeaderView(mUserHead);
        userRecyAdaper.openLoadAnimation();
        mRyUser.setAdapter(userRecyAdaper);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
