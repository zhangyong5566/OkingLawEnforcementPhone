package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenCase;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.CaseDealActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by zhao on 2017-3-30.
 */

public class CaseEvidenceFragment extends Fragment {
    private static final String ARG_PARAM2 = "param2";
    private String mParam2;
    private GreenCase mycase;

    private ViewPager mViewPager;
    //private TabLayout mTabLayout;

    private Button add_evidence_button, document_tabBtn, audioVideo_tabBtn;
    private int selectIndex, selectFragmentIndex = 0;
    private FragmentStatePagerAdapter fragmentPagerAdapter;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();
    private SimpleDateFormat mSimpleDateFormat;
    private View mInflate;

    public static CaseEvidenceFragment newInstance(String param2) {
        CaseEvidenceFragment fragment = new CaseEvidenceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_case_evidence, container, false);

        }
        initView(mInflate);
        return mInflate;
    }


    public void initView(View rootView) {
        CaseDealActivity caseDealActivity = ((CaseDealActivity) getActivity());
        mycase = caseDealActivity.getCase();
        mViewPager = (ViewPager) rootView.findViewById(R.id.vp_view);

        fragmentPagerAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        titleList.clear();
        titleList.add("书证");
        titleList.add("视听资料");

        fragments.clear();
        DocumentaryEvidenceListFragment documentaryListFragment = DocumentaryEvidenceListFragment.newInstance(null);
        documentaryListFragment.setGreenCase(mycase);
        fragments.add(documentaryListFragment);
        CaseAudioVideoEvidenceListFragment caseAudioVideoListFragment = CaseAudioVideoEvidenceListFragment.newInstance(null);
        caseAudioVideoListFragment.setGreenCase(mycase);
        fragments.add(caseAudioVideoListFragment);

        //mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectFragmentIndex = position;
                if (position == 0) {
                    document_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg3));
                    document_tabBtn.setTextColor(getResources().getColor(R.color.colorMain6));
                    audioVideo_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg2));
                    audioVideo_tabBtn.setTextColor(getResources().getColor(R.color.colorMain4));

                } else {
                    audioVideo_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg3));
                    audioVideo_tabBtn.setTextColor(getResources().getColor(R.color.colorMain6));
                    document_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg2));
                    document_tabBtn.setTextColor(getResources().getColor(R.color.colorMain4));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        mViewPager.setCurrentItem(selectFragmentIndex);

        document_tabBtn = (Button) rootView.findViewById(R.id.document_tabBtn);
        audioVideo_tabBtn = (Button) rootView.findViewById(R.id.audioVideo_tabBtn);
        document_tabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                document_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg3));
                document_tabBtn.setTextColor(getResources().getColor(R.color.colorMain6));
                audioVideo_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg2));
                audioVideo_tabBtn.setTextColor(getResources().getColor(R.color.colorMain4));

                mViewPager.setCurrentItem(0);
            }
        });
        audioVideo_tabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioVideo_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg3));
                audioVideo_tabBtn.setTextColor(getResources().getColor(R.color.colorMain6));
                document_tabBtn.setBackground(getResources().getDrawable(R.drawable.fast_btn_bg2));
                document_tabBtn.setTextColor(getResources().getColor(R.color.colorMain4));

                mViewPager.setCurrentItem(1);
            }
        });
        if (selectFragmentIndex == 0) {
            document_tabBtn.callOnClick();
        } else if (selectFragmentIndex == 1) {
            audioVideo_tabBtn.callOnClick();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        CaseDealActivity caseDealActivity = (CaseDealActivity) context;
        caseDealActivity.setToolbarText("证据管理");
    }


}
