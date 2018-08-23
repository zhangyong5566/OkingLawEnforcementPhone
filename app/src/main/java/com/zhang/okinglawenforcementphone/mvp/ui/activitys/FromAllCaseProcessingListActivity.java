package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
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

public class FromAllCaseProcessingListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.case_ry)
    RecyclerView mCaseRy;
    @BindView(R.id.layoutSwipeRefresh)
    SwipeRefreshLayout mLayoutSwipeRefresh;
    @BindView(R.id.tv)
    TextView mTv;
    private Unbinder mBind;
    private CaseListAdapter mCaseListAdapter;
    private List<GreenCase> mCaseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_all_case_processing_list);
        mBind = ButterKnife.bind(this);
        initData();
        setListener();

    }

    private void initData() {
        mLayoutSwipeRefresh.setRefreshing(true);
        mLayoutSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.refresh_color));
        getHttpCaseList();
        mCaseRy.setLayoutManager(new LinearLayoutManager(FromAllCaseProcessingListActivity.this, LinearLayoutManager.VERTICAL, false));
        mCaseRy.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 10, BaseApplication.getApplictaion().getResources().getColor(R.color.activity_bg)));

        mCaseListAdapter = new CaseListAdapter(R.layout.list_item_case, null);
        mCaseRy.setAdapter(mCaseListAdapter);
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                Intent intent = new Intent(FromAllCaseProcessingListActivity.this, CaseDealActivity.class);
                intent.putExtra("AJID",data.get(position).getAJID());
                startActivity(intent);
            }
        });
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
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
