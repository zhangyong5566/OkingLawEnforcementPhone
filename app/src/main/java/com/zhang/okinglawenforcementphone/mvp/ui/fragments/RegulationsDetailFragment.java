package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhang.okinglawenforcementphone.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegulationsDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.tv_itemdetail)
    TextView tvItemdetail;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String articlesContent;
    private String rulesContent;
    private View mInflate;


    public RegulationsDetailFragment() {
        // Required empty public constructor
    }

    public static RegulationsDetailFragment newInstance(String articlesContent, String rulesContent) {
        RegulationsDetailFragment fragment = new RegulationsDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, articlesContent);
        args.putString(ARG_PARAM2, rulesContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articlesContent = getArguments().getString(ARG_PARAM1);
            rulesContent = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_regulations_detail, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListener();
        return mInflate;
    }

    private void initData() {
        tvDetail.setText(rulesContent);
        tvItemdetail.setText(articlesContent);
    }

    private void setListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setRulesContent(String articlesContent, String rulesContent) {
        this.articlesContent = articlesContent;
        this.rulesContent = rulesContent;
        if (tvItemdetail!=null){
            tvDetail.setText(rulesContent);
            tvItemdetail.setText(articlesContent);
        }
    }
}
