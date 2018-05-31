package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.MapTaskInfo;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 区域历史执法记录查询
 */
public class RegionalHistoryEnforcementActivity extends BaseActivity implements AMap.OnMapLoadedListener, AMap.OnCameraChangeListener, AMap.OnMyLocationChangeListener, AMap.OnMarkerClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.map)
    MapView mMap;
    @BindView(R.id.tv_taskname)
    TextView mTvTaskname;
    @BindView(R.id.tv_state)
    TextView mTvState;
    @BindView(R.id.tv_pelease_people)
    TextView mTvPeleasePeople;
    @BindView(R.id.tv_approver)
    TextView mTvApprover;
    @BindView(R.id.tv_description)
    TextView mTvDescription;
    @BindView(R.id.tv_are)
    TextView mTvAre;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.rl)
    RelativeLayout mRl;
    private AMap mAMap;
    private Unbinder mBind;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private ArrayList<LatLng> mLatLngs;
    private Point mPoint;
    private Point mScreenPosition;
    private boolean mIsShowToast = true;
    private Circle mCircle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regional_history_enforcement);
        mBind = ButterKnife.bind(this);
        mMap.onCreate(savedInstanceState);// 此方法必须重写
        initData();
        setListener();
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mAMap.setOnMapLoadedListener(this);
        mAMap.setOnCameraChangeListener(this);

//        setMapCustomStyleFile();
        //设置SDK 自带定位消息监听
        mAMap.setOnMyLocationChangeListener(this);
    }

    private void initData() {
        mAMap = mMap.getMap();
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// MAP_TYPE_SATELLITE卫星地图模式
        mAMap.setMinZoomLevel(10);
        mAMap.setMaxZoomLevel(20);
        setUpMap();



        mLatLngs = new ArrayList<>();
        mLatLngs.add(OkingContract.PAD_1);
        mLatLngs.add(OkingContract.PAD_2);
        mLatLngs.add(OkingContract.PAD_3);
        mLatLngs.add(OkingContract.PAD_4);
        mLatLngs.add(OkingContract.PAD_5);
        mLatLngs.add(OkingContract.PAD_6);
        mLatLngs.add(OkingContract.PAD_7);
        mLatLngs.add(OkingContract.PAD_8);
        mLatLngs.add(OkingContract.PAD_9);
    }

    private void setUpMap() {
        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_point));
        // 定位的类型为跟随模式LOCATION_TYPE_FOLLOW;  定位的类型为只定位模式模式LOCATION_TYPE_SHOW
        mAMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        mAMap.setMapCustomEnable(true); //开启自定义样式


    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
        mBind.unbind();
    }

    @Override
    public void onMapLoaded() {
        mAMap.setOnMarkerClickListener(this);
        addMarkersToMap();// 往地图上添加marker
    }

    //地图移动监听

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    //地图移动完成监听
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        toGeoLocation();
    }

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if (location != null) {
            LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                changeCamera(
                        CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                lng, 18, 30, 30)));
                /*
                errorCode
                errorInfo
                locationType
                */
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private void toGeoLocation() {
        mIsShowToast = true;
        mPoint = new Point(mScreenPosition.x, mScreenPosition.y);
        LatLng mLatlng = mAMap.getProjection().fromScreenLocation(mPoint);

        // 绘制一个圆形
        if (mCircle != null) {
            mCircle.remove();
            mCircle = null;
        }
        mCircle = mAMap.addCircle(new CircleOptions().center(mLatlng)
                .radius(1000).strokeColor(Color.argb(20, 1, 1, 1))
                .fillColor(Color.argb(20, 1, 1, 1)).strokeWidth(3));
        //圆形范围内是否有任务
        for (int i = 0; i < mLatLngs.size(); i++) {

            if (mCircle.contains(mLatLngs.get(i))) {
                if (mIsShowToast) {
                    RxToast.success(BaseApplication.getApplictaion(), "当前范围内有任务记录", Toast.LENGTH_SHORT).show();
                    mIsShowToast = false;
                }
                MarkerOptions markerOptions = new MarkerOptions().anchor(0.5f, 0.5f)

                        .position(mLatLngs.get(i)).title("巡查任务" + i)
                        .snippet("经纬度：" + mLatLngs.get(i)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_wb_cloudy))
                        .draggable(false).period(10);
                Marker marker = mAMap.addMarker(markerOptions);
                MapTaskInfo mapTaskInfo = new MapTaskInfo();
                mapTaskInfo.setTaskName("任务名称：巡查任务" + i);
                mapTaskInfo.setTaskState("任务状态：已审核待执行");
                mapTaskInfo.setTaskApprover("审批人：Dev2");
                mapTaskInfo.setTaskAre("巡查区域：珠江上游");
                mapTaskInfo.setTaskDescription("任务描述：违法采砂巡查");
                mapTaskInfo.setTaskPeleasePeople("发布人：Dev1");
                mapTaskInfo.setTaskTime("发布时间：2017年12月14日");
                marker.setObject(mapTaskInfo);
            }
        }
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {

        mAMap.moveCamera(update);

    }

    private void addMarkersToMap() {
        MarkerOptions markerOption0 = new MarkerOptions().anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(false).period(10);
        Marker marker = mAMap.addMarker(markerOption0);
        LatLng latLng = mAMap.getCameraPosition().target;
        mScreenPosition = mAMap.getProjection().toScreenLocation(latLng);
        //设置Marker在屏幕上,不跟随地图移动
        marker.setPositionByPixels(mScreenPosition.x, mScreenPosition.y);
    }
}
