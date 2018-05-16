package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.RegulationsExListViewAdapter;
import com.zhang.okinglawenforcementphone.beans.LawsRegulation;
import com.zhang.okinglawenforcementphone.db.LawDao;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.RegulationsDetailsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RegulationsTitleListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "mmid";
    private static final String ARG_PARAM2 = "rulesContent";
    @BindView(R.id.el)
    ExpandableListView mEl;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mmid;
    private String rulesContent;
    private View mInflate;
    private ArrayList<LawsRegulation> mLawChapter;
    private RegulationsDetailsActivity mActivity;
    private RegulationsDetailFragment mRegulationsDetailFragment;


    public RegulationsTitleListFragment() {
        // Required empty public constructor
    }

    public static RegulationsTitleListFragment newInstance(String mmid, String rulesContent) {
        RegulationsTitleListFragment fragment = new RegulationsTitleListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mmid);
        args.putString(ARG_PARAM2, rulesContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mmid = getArguments().getString(ARG_PARAM1);
            rulesContent = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_regulations_title_list, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListener();
        return mInflate;
    }

    private void initData() {
        mLawChapter = LawDao.getLawChapter(mmid);
        RegulationsExListViewAdapter regulationsExListViewAdapter = new RegulationsExListViewAdapter(mLawChapter);
        mEl.setAdapter(regulationsExListViewAdapter);

    }

    private void setListener() {

        mEl.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                String chapterItem = mLawChapter.get(groupPosition).getChapterDirectory().get(groupPosition).getSection().get(childPosition).getChapterItem();
                String articlesContent = LawDao.getArticlesContent(chapterItem);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.hide(RegulationsTitleListFragment.this);
                if (mRegulationsDetailFragment==null){
                    mRegulationsDetailFragment = RegulationsDetailFragment.newInstance(articlesContent, rulesContent);
                    fragmentTransaction.add(R.id.ll_regulation_content,mRegulationsDetailFragment,"RegulationsDetailFragment");
                }else {
                        mRegulationsDetailFragment.setRulesContent(articlesContent,rulesContent);
                        fragmentTransaction.show(mRegulationsDetailFragment);
                }
                fragmentTransaction.commit();
                return false;
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (RegulationsDetailsActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
