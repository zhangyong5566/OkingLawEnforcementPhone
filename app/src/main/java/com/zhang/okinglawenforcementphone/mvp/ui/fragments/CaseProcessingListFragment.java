package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.CaseListAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenCase;
import com.zhang.okinglawenforcementphone.beans.GreenCaseDao;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.htttp.Api;
import com.zhang.okinglawenforcementphone.htttp.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.CaseDealActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.CaseManagerActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * 案件处理列表
 */
public class CaseProcessingListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.case_ry)
    RecyclerView mCaseRy;
    @BindView(R.id.layoutSwipeRefresh)
    SwipeRefreshLayout mLayoutSwipeRefresh;
    Unbinder unbinder;
    @BindView(R.id.tv)
    TextView mTv;
    private CaseManagerActivity mActivity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private CaseListAdapter mCaseListAdapter;
    private List<GreenCase> mCaseList = new ArrayList<>();


    public CaseProcessingListFragment() {
        // Required empty public constructor
    }

    public static CaseProcessingListFragment newInstance(String param1, String param2) {
        CaseProcessingListFragment fragment = new CaseProcessingListFragment();
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

            mInflate = inflater.inflate(R.layout.fragment_caseprocessing_list, container, false);
        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListener();
        return mInflate;
    }

    private void setListener() {
        mLayoutSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHttpCaseList();
            }
        });
        mCaseListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<GreenCase> data = adapter.getData();
                Intent intent = new Intent(getActivity(), CaseDealActivity.class);
                intent.putExtra("AJID",data.get(position).getAJID());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mLayoutSwipeRefresh.setRefreshing(true);
        mLayoutSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.refresh_color));
        getHttpCaseList();
        mCaseRy.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mCaseRy.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 10, BaseApplication.getApplictaion().getResources().getColor(R.color.activity_bg)));

        mCaseListAdapter = new CaseListAdapter(R.layout.list_item_case, null);
        mCaseRy.setAdapter(mCaseListAdapter);
    }

    private void getHttpCaseList() {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .loadCaseList(OkingContract.CURRENTUSER.getUserid())
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody requestBody) throws Exception {
                        String result = requestBody.string();
                        JSONArray jsonArray = new JSONArray(result);
                        mLayoutSwipeRefresh.setRefreshing(false);
                        if (jsonArray.length() > 0) {
                            mCaseList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String ajid = jsonObject.getString("AJID");
                                GreenCase unique = GreenDAOManager.getInstence().getDaoSession().getGreenCaseDao().queryBuilder().where(GreenCaseDao.Properties.AJID.eq(ajid)).unique();

                                if (unique == null) {
                                    GreenCase greenCase = new GreenCase();
                                    greenCase.setAJID(ajid);
                                    greenCase.setAFDD(jsonObject.getString("AFDD"));
                                    greenCase.setAFSJ(jsonObject.optLong("AFSJ"));

                                    greenCase.setAJLX(jsonObject.getString("AJLX"));
                                    greenCase.setAJLXID(jsonObject.getString("AJLXID"));
                                    greenCase.setAJLY(jsonObject.getString("AJLY"));
                                    greenCase.setAJMC(jsonObject.getString("AJMC"));
                                    greenCase.setAQJY(jsonObject.getString("AQJY"));
                                    greenCase.setAY(jsonObject.getString("AY"));
                                    greenCase.setCBR1(jsonObject.getString("CBR1"));
                                    greenCase.setCBR2(jsonObject.getString("CBR2"));
                                    greenCase.setCBRDW1(jsonObject.getString("CBRDW1"));
                                    greenCase.setCBRDW2(jsonObject.getString("CBRDW2"));
                                    greenCase.setCBRID1(jsonObject.getString("CBRID1"));
                                    greenCase.setCBRID2(jsonObject.getString("CBRID2"));
                                    greenCase.setCFNR(jsonObject.getString("CFNR"));
                                    greenCase.setCFYJ(jsonObject.getString("CFYJ"));
                                    greenCase.setDSRQK(jsonObject.getString("DSRQK"));
                                    greenCase.setFLYJ(jsonObject.getString("FLYJ"));
                                    greenCase.setJGD(jsonObject.getString("JGD"));
                                    greenCase.setSLR(jsonObject.getString("SLR"));
                                    greenCase.setSLRQ(jsonObject.optLong("SLRQ"));
                                    greenCase.setSLXX_ZT(jsonObject.getString("SLXX_ZT"));
                                    greenCase.setSQWTR(jsonObject.getString("SQWTR"));
                                    greenCase.setSSD(jsonObject.getString("SSD"));
                                    greenCase.setWHJGFSD(jsonObject.getString("WHJGFSD"));
                                    greenCase.setXWZSD(jsonObject.getString("XWZSD"));
                                    greenCase.setZFBM(jsonObject.getString("ZFBM"));
                                    greenCase.setZFZH1(jsonObject.getString("ZFZH1"));
                                    greenCase.setZFZH2(jsonObject.getString("ZFZH2"));
                                    greenCase.setZT(jsonObject.getString("ZT"));
                                    GreenDAOManager.getInstence().getDaoSession().getGreenCaseDao().insert(greenCase);
                                    mCaseList.add(greenCase);
                                } else {
                                    unique.setAJID(ajid);
                                    unique.setAFDD(jsonObject.getString("AFDD"));
                                    unique.setAFSJ(jsonObject.optLong("AFSJ"));
                                    unique.setAJLX(jsonObject.getString("AJLX"));
                                    unique.setAJLXID(jsonObject.getString("AJLXID"));
                                    unique.setAJLY(jsonObject.getString("AJLY"));
                                    unique.setAJMC(jsonObject.getString("AJMC"));
                                    unique.setAQJY(jsonObject.getString("AQJY"));
                                    unique.setAY(jsonObject.getString("AY"));
                                    unique.setCBR1(jsonObject.getString("CBR1"));
                                    unique.setCBR2(jsonObject.getString("CBR2"));
                                    unique.setCBRDW1(jsonObject.getString("CBRDW1"));
                                    unique.setCBRDW2(jsonObject.getString("CBRDW2"));
                                    unique.setCBRID1(jsonObject.getString("CBRID1"));
                                    unique.setCBRID2(jsonObject.getString("CBRID2"));
                                    unique.setCFNR(jsonObject.getString("CFNR"));
                                    unique.setCFYJ(jsonObject.getString("CFYJ"));
                                    unique.setDSRQK(jsonObject.getString("DSRQK"));
                                    unique.setFLYJ(jsonObject.getString("FLYJ"));
                                    unique.setJGD(jsonObject.getString("JGD"));
                                    unique.setSLR(jsonObject.getString("SLR"));
                                    unique.setSLRQ(jsonObject.optLong("SLRQ"));
                                    unique.setSLXX_ZT(jsonObject.getString("SLXX_ZT"));
                                    unique.setSQWTR(jsonObject.getString("SQWTR"));
                                    unique.setSSD(jsonObject.getString("SSD"));
                                    unique.setWHJGFSD(jsonObject.getString("WHJGFSD"));
                                    unique.setXWZSD(jsonObject.getString("XWZSD"));
                                    unique.setZFBM(jsonObject.getString("ZFBM"));
                                    unique.setZFZH1(jsonObject.getString("ZFZH1"));
                                    unique.setZFZH2(jsonObject.getString("ZFZH2"));
                                    unique.setZT(jsonObject.getString("ZT"));
                                    GreenDAOManager.getInstence().getDaoSession().getGreenCaseDao().update(unique);
                                    mCaseList.add(unique);
                                }

                            }

                            mCaseListAdapter.setNewData(mCaseList);
                        } else {
                            mLayoutSwipeRefresh.setVisibility(View.GONE);
                            mTv.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mLayoutSwipeRefresh.setRefreshing(false);
                        List<GreenCase> greenCases = GreenDAOManager.getInstence().getDaoSession().getGreenCaseDao().queryBuilder().where(GreenCaseDao.Properties.CBRID1.eq(OkingContract.CURRENTUSER.getUserid())).list();
                        mCaseListAdapter.setNewData(greenCases);

                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            mActivity = (CaseManagerActivity) getActivity();
        }
    }
}
