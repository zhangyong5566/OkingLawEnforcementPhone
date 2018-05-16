package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.LawsAndRegulationAdapter;
import com.zhang.okinglawenforcementphone.beans.LawBean;
import com.zhang.okinglawenforcementphone.db.LawDao;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.RegulationsDetailsActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 法律法规库
 */
public class LawsAndRegulationsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.rcy_laws)
    RecyclerView mRcyLaws;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;
    private View mInflate;
    private LawsAndRegulationAdapter mLawsAndRegulationAdapter;


    public LawsAndRegulationsFragment() {
        // Required empty public constructor
    }

    public static LawsAndRegulationsFragment newInstance(String param1, String param2) {
        LawsAndRegulationsFragment fragment = new LawsAndRegulationsFragment();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_laws_and_regulations, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListener();
        return mInflate;
    }

    private void setListener() {
        mLawsAndRegulationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<LawBean> data = adapter.getData();
                Intent intent = new Intent(getActivity(), RegulationsDetailsActivity.class);
                intent.putExtra("title", data.get(position).getTitle());
                intent.putExtra("mmid", data.get(position).getMmid());
                intent.putExtra("rulesContent", data.get(position).getRulesContent());
                getActivity().startActivity(intent);
            }
        });
    }


    private void initData() {
        mRcyLaws.setNestedScrollingEnabled(false);
        mRcyLaws.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mRcyLaws.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 10, BaseApplication.getApplictaion().getResources().getColor(R.color.bottom_nav_normal)));

        List<LawBean> laws = LawDao.getLaw();
        mLawsAndRegulationAdapter = new LawsAndRegulationAdapter(R.layout.lawandregulation_item, laws);
        mLawsAndRegulationAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mRcyLaws.setAdapter(mLawsAndRegulationAdapter);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
