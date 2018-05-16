package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;


/**
 * 水行政执法用语规范
 */
public class EnforcementLanguageSpecificationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private ListView mLv;


    public EnforcementLanguageSpecificationFragment() {
        // Required empty public constructor
    }
    public static EnforcementLanguageSpecificationFragment newInstance(String param1, String param2) {
        EnforcementLanguageSpecificationFragment fragment = new EnforcementLanguageSpecificationFragment();
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
        if (mInflate==null){

            mInflate = inflater.inflate(R.layout.fragment_enforcement_language_specification, container, false);
        }
        initView(mInflate);
        return mInflate;
    }


    public void initView(View rootView) {
        mLv = rootView.findViewById(R.id.lv);
        initData();
        setListener();
    }

    private void setListener() {

    }

    private void initData() {
        final String[] languageSpecificationArray = getResources().getStringArray(R.array.lv_language_specification);
        mLv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return languageSpecificationArray.length;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int position, View contentView, ViewGroup viewGroup) {
                TextView textView = new TextView(BaseApplication.getApplictaion());
                textView.setPadding(10,10,10,10);
                textView.setTextSize(12);
                textView.setTextColor(Color.argb(136,0,0,0));
                textView.setText(languageSpecificationArray[position]);
                return textView;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mInflate != null) {
            ViewGroup parent = (ViewGroup) mInflate.getParent();
            if (parent != null) {
                parent.removeView(mInflate);
            }

        }
    }
}
