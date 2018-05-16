package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.PuttedForwardAdapter;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.PuttedForwardConActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PuttedForwardListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rc)
    RecyclerView mRc;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private PuttedForwardAdapter mForwardAdapter;
    private PuttedForwardFragment mPuttedForwardFragment;
    private PuttedForwardConActivity mPuttedForwardConActivity;


    public PuttedForwardListFragment() {
        // Required empty public constructor
    }

    public static PuttedForwardListFragment newInstance(String param1, String param2) {
        PuttedForwardListFragment fragment = new PuttedForwardListFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_putted_forward_list, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListener();
        return mInflate;
    }

    private void setListener() {
        mForwardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.hide(PuttedForwardListFragment.this);
                ArrayList<String> data = (ArrayList<String>) adapter.getData();
                mPuttedForwardConActivity.setTitle(data.get(position));
                if (mPuttedForwardFragment ==null){
                    mPuttedForwardFragment = PuttedForwardFragment.newInstance(null, null);

                }
                fragmentTransaction.add(R.id.ll_putted_content,mPuttedForwardFragment,"PuttedForwardFragment").commit();
            }
        });
    }

    private void initData() {
        mRc.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRc.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 5, getResources().getColor(R.color.activity_bg)));
        final ArrayList<String> items = new ArrayList<>();
        items.add("[案例] 冯某某未经同意在河道管理范围建设建筑物案");
        mForwardAdapter = new PuttedForwardAdapter(R.layout.putted_forward_item,items);
        mForwardAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mRc.setAdapter(mForwardAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPuttedForwardConActivity = (PuttedForwardConActivity) context;
    }
}
