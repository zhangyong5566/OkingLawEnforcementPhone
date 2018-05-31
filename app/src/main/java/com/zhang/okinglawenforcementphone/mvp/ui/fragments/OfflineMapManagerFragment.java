package com.zhang.okinglawenforcementphone.mvp.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amap.api.maps.AMapException;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.OfflineMapRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.OfflineMapDownload;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.SettingActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineMapManagerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<OfflineMapCity> cityList;
    private ArrayList<OfflineMapDownload> offlineMapDownloads = new ArrayList<>();
    private OfflineMapManager aMapManager;

    private RecyclerView offlineMap_ry;
    private Button checkUpdateBtn;
    private OfflineMapRecyAdapter mOfflineMapRecyAdapter;
    private View mInflate;
    private SettingActivity mSettingActivity;

    public OfflineMapManagerFragment() {
        // Required empty public constructor
    }

    public static OfflineMapManagerFragment newInstance(String param1, String param2) {
        OfflineMapManagerFragment fragment = new OfflineMapManagerFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_offline_map_manager, container, false);

        }
        initView(mInflate);
        return mInflate;
    }

    public void initView(View rootView) {
        mSettingActivity.setTitleText("离线地图");
        MapsInitializer.sdcardDir = Environment.getExternalStorageDirectory() + "/oking/amap";

        aMapManager = new OfflineMapManager(getContext(), new OfflineMapManager.OfflineMapDownloadListener() {
            @Override
            public void onDownload(int i, int i1, String s) {

                for (int j = 0; j < offlineMapDownloads.size(); j++) {
                    OfflineMapDownload omp = offlineMapDownloads.get(j);
                    if (omp.getCity().getCity().equals(s)) {

                        omp.setProgress(i1);

                        if (i == OfflineMapStatus.LOADING) {
                            omp.setState(OfflineMapDownload.OnDownload);
                        } else if (i == OfflineMapStatus.WAITING) {
                            omp.setState(OfflineMapDownload.Waiting);
                        } else if (i == OfflineMapStatus.UNZIP) {
                            omp.setState(OfflineMapDownload.OnUnZIP);
                        } else if (i == OfflineMapStatus.SUCCESS) {
                            omp.setState(OfflineMapDownload.Normal);
                        } else if (i == OfflineMapStatus.ERROR ||
                                i == OfflineMapStatus.EXCEPTION_NETWORK_LOADING ||
                                i == OfflineMapStatus.EXCEPTION_SDCARD ||
                                i == OfflineMapStatus.EXCEPTION_AMAP) {
                            omp.setState(OfflineMapDownload.Error);
                        }

                        mOfflineMapRecyAdapter.notifyDataSetChanged();

                        break;
                    }
                }
            }

            @Override
            public void onCheckUpdate(boolean b, String s) {
                if (b) {
                    for (int i = 0; i < offlineMapDownloads.size(); i++) {
                        OfflineMapDownload omp = offlineMapDownloads.get(i);
                        if (omp.getCity().getCity().equals(s)) {
                            omp.setState(OfflineMapDownload.NewVersionOrUnDownload);
                            break;
                        }
                    }

                    mOfflineMapRecyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onRemove(boolean b, String s, String s1) {

            }
        });

        cityList = aMapManager.getItemByProvinceName("广东省").getCityList();
        setOfflineMapDownloads();

        offlineMap_ry = rootView.findViewById(R.id.offlineMap_listView);
        offlineMap_ry.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        offlineMap_ry.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 6, BaseApplication.getApplictaion().getResources().getColor(R.color.activity_bg)));
        mOfflineMapRecyAdapter = new OfflineMapRecyAdapter(R.layout.list_item_offlinemaps, offlineMapDownloads);
        mOfflineMapRecyAdapter.openLoadAnimation();
        offlineMap_ry.setAdapter(mOfflineMapRecyAdapter);
        mOfflineMapRecyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                downloadOffLoadMap(offlineMapDownloads.get(position).getCity());
            }
        });

        checkUpdateBtn = (Button) rootView.findViewById(R.id.checkUpdate_btn);
        checkUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadOffLoadMap();
            }
        });
    }

    private void setOfflineMapDownloads() {
        for (int i = 0; i < cityList.size(); i++) {
            if (offlineMapDownloads != null) {
                OfflineMapDownload omp = new OfflineMapDownload(cityList.get(i), 100, OfflineMapDownload.Normal);
                offlineMapDownloads.add(omp);
            }
        }

        updateOffLoadMap();
    }

    private void downloadOffLoadMap() {

        try {
            if (cityList != null) {
                for (int i = 0; i < cityList.size(); i++) {
                    aMapManager.downloadByCityName(cityList.get(i).getCity());
                }
            }
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    private void downloadOffLoadMap(OfflineMapCity city) {

        try {
            if (city != null) {
                aMapManager.downloadByCityName(city.getCity());
            }
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    private void updateOffLoadMap() {

        try {
            if (cityList != null) {
                for (int i = 0; i < cityList.size(); i++) {
                    aMapManager.updateOfflineCityByName(cityList.get(i).getCity());
                }
            }
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSettingActivity = (SettingActivity) context;
    }
}
