package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.DeviceUtil;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.PhoneListRecyAdaper;
import com.zhang.okinglawenforcementphone.beans.Dept;
import com.zhang.okinglawenforcementphone.beans.GreenDept;
import com.zhang.okinglawenforcementphone.beans.GreenDeptDao;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.SettingActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class PhoneBookFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.recy_phone_list)
    RecyclerView mRecyPhoneList;
    Unbinder unbinder;
    @BindView(R.id.layoutSwipeRefresh)
    SwipeRefreshLayout mLayoutSwipeRefresh;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;

    private List<GreenDept> mGreenDepts = new ArrayList<>();
    private PhoneListRecyAdaper mPhoneListRecyAdaper;

    private int limit = 10;
    private int offset = 0;
    private boolean loadMoreEnd = false;
    private boolean isLoadMor = false;
    private Gson gson = new Gson();
    private SettingActivity mSettingActivity;
    private BaseQuickAdapter.RequestLoadMoreListener mRequestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            mRecyPhoneList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLoadMor = true;
                    if (loadMoreEnd) {
                        //数据全部加载完毕
                        mPhoneListRecyAdaper.loadMoreEnd();
                    } else {
                        offset += 10;
                        getNetData();

                        mPhoneListRecyAdaper.loadMoreComplete();
                    }
                }

            }, 2000);
        }
    };

    public PhoneBookFragment() {
        // Required empty public constructor
    }

    public static PhoneBookFragment newInstance(String param1, String param2) {
        PhoneBookFragment fragment = new PhoneBookFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_phone_book, container, false);

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
                if (!mPhoneListRecyAdaper.isLoading()) {
                    isLoadMor = false;
                    loadMoreEnd = false;
                    offset = 0;
                    mPhoneListRecyAdaper.setOnLoadMoreListener(mRequestLoadMoreListener, mRecyPhoneList);
                    getNetData();
                }


            }
        });

        mPhoneListRecyAdaper.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<GreenDept> datas = adapter.getData();
                DeviceUtil.callPhone(mSettingActivity, datas.get(position).getPhone());
            }
        });
    }

    private void initData() {
        mSettingActivity.setTitleText("电话簿");
        mLayoutSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.refresh_color));
        mPhoneListRecyAdaper = new PhoneListRecyAdaper(R.layout.phone_item, null);
        mRecyPhoneList.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mRecyPhoneList.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 20, getResources().getColor(R.color.activity_bg)));
        mPhoneListRecyAdaper.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mRecyPhoneList.setAdapter(mPhoneListRecyAdaper);
        mLayoutSwipeRefresh.setRefreshing(true);
        mPhoneListRecyAdaper.setOnLoadMoreListener(mRequestLoadMoreListener, mRecyPhoneList);
        getNetData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getNetData() {


        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .loadDepatContacts(limit, offset)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();



                        Dept dept = gson.fromJson(result, Dept.class);

                        List<Dept.RowsBean> rows = dept.getRows();
                        if (rows != null && rows.size() > 0) {
                            mGreenDepts.clear();
                            for (Dept.RowsBean row : rows) {
                                GreenDept unique = GreenDAOManager.getInstence().getDaoSession().getGreenDeptDao()
                                        .queryBuilder().where(GreenDeptDao.Properties.Userid.eq(row.getUserid())).unique();

                                if (unique == null) {
                                    GreenDept greenDept = new GreenDept();
                                    greenDept.setAccount(row.getAccount());
                                    greenDept.setDeptid(row.getDeptid());
                                    greenDept.setDeptname(row.getDeptname());
                                    greenDept.setNum(row.getNum());
                                    greenDept.setPhone(row.getPhone());
                                    greenDept.setRemark(row.getRemark());
                                    greenDept.setSex(row.getSex());
                                    greenDept.setTel(row.getTel());
                                    greenDept.setUserid(row.getUserid());
                                    greenDept.setUsername(row.getUsername());
                                    greenDept.setUsertype(row.getUsertype());
                                    greenDept.setXh(row.getXh());
                                    GreenDAOManager.getInstence().getDaoSession().getGreenDeptDao().insert(greenDept);
                                    mGreenDepts.add(greenDept);
                                } else {
                                    unique.setAccount(row.getAccount());
                                    unique.setDeptid(row.getDeptid());
                                    unique.setDeptname(row.getDeptname());
                                    unique.setNum(row.getNum());
                                    unique.setPhone(row.getPhone());
                                    unique.setRemark(row.getRemark());
                                    unique.setSex(row.getSex());
                                    unique.setTel(row.getTel());
                                    unique.setUserid(row.getUserid());
                                    unique.setUsername(row.getUsername());
                                    unique.setUsertype(row.getUsertype());
                                    unique.setXh(row.getXh());
                                    mGreenDepts.add(unique);
                                    GreenDAOManager.getInstence().getDaoSession().getGreenDeptDao().update(unique);
                                }

                            }

                            if (!isLoadMor) {
                                mPhoneListRecyAdaper.setNewData(mGreenDepts);

                            } else {
                                mPhoneListRecyAdaper.addData(mGreenDepts);
                            }
                            RxToast.success("数据获取成功");
                        } else {

                            Log.i("Oking","服务器无数据");
                            loadMoreEnd = true;
                        }
                        mLayoutSwipeRefresh.setEnabled(true);
                        mLayoutSwipeRefresh.setRefreshing(false);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mLayoutSwipeRefresh.setRefreshing(false);

                        List<GreenDept> list = GreenDAOManager.getInstence().getDaoSession().getGreenDeptDao()
                                .queryBuilder()
                                .where(GreenDeptDao.Properties.Deptid.eq(OkingContract.CURRENTUSER.getDept_id()))
                                .limit(limit)
                                .offset(offset)
                                .list();
                        if (list != null && list.size() > 0) {
                            if (!isLoadMor) {
                                mPhoneListRecyAdaper.setNewData(list);

                            } else {
                                mPhoneListRecyAdaper.addData(list);
                            }
                        } else {

                            loadMoreEnd = true;
                        }


                        mLayoutSwipeRefresh.setEnabled(true);
                    }

                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSettingActivity = (SettingActivity) context;
    }
}
