package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.ExpandableItemCaseRegistAdapter;
import com.zhang.okinglawenforcementphone.beans.WrittenItemBean;
import com.zhang.okinglawenforcementphone.beans.WrittenRecordLevel0;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 案件登记
 */
public class CaseRegistrationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.case_regist_Recy)
    RecyclerView mCaseRegistRecy;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;

    private WrittenRecordLevel0 mWrittenRecordLevel0;
    private WrittenItemBean mWrittenItemBean;
    private ExpandableItemCaseRegistAdapter mExpandableItemCaseRegistAdapter;

    public CaseRegistrationFragment() {
        // Required empty public constructor
    }

    public static CaseRegistrationFragment newInstance(String param1, String param2) {
        CaseRegistrationFragment fragment = new CaseRegistrationFragment();
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

            mInflate = inflater.inflate(R.layout.fragment_case_registration, container, false);
        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        return mInflate;
    }

    private void initData() {
        mCaseRegistRecy.setNestedScrollingEnabled(false);
        mCaseRegistRecy.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mCaseRegistRecy.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 3, BaseApplication.getApplictaion().getResources().getColor(R.color.bottom_nav_normal)));

        if (mExpandableItemCaseRegistAdapter == null) {

            mExpandableItemCaseRegistAdapter = new ExpandableItemCaseRegistAdapter(generateData());
            mCaseRegistRecy.setAdapter(mExpandableItemCaseRegistAdapter);
        }
        mExpandableItemCaseRegistAdapter.expand(0);
    }

    private List<MultiItemEntity> generateData() {

        ArrayList<MultiItemEntity> res = new ArrayList<>();

        mWrittenRecordLevel0 = new WrittenRecordLevel0("案件来源");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(1);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        mWrittenRecordLevel0 = new WrittenRecordLevel0("上传证据");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(2);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        mWrittenRecordLevel0 = new WrittenRecordLevel0("基本信息(可选)");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(3);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        return res;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
