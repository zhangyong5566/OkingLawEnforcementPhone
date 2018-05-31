package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.google.gson.reflect.TypeToken;
import com.zhang.baselib.utils.DataUtil;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenLocation;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLogDao;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 轨迹
 */
public class TrajectoryActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, AMap.OnMarkerClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.map)
    MapView mMap;
    @BindView(R.id.tv_taskid)
    TextView mTvTaskid;
    @BindView(R.id.tv_taskname)
    TextView mTvTaskname;
    @BindView(R.id.tv_taskpublish)
    TextView mTvTaskpublish;
    @BindView(R.id.tv_exebegintime)
    TextView mTvExebegintime;
    @BindView(R.id.tv_exeendtime)
    TextView mTvExeendtime;
    @BindView(R.id.tv_taskarea)
    TextView mTvTaskarea;
    private Unbinder mBind;
    private AMap mAMap;
    private UiSettings mUiSettings;
    private Polyline mPolyline;
    private String mLocJson;
    private LatLngBounds mBounds;
    private List<LatLng> mLatLngs;
    private List<GreenMedia> mGreenMedias;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajectory);
        mBind = ButterKnife.bind(this);
        mMap.onCreate(savedInstanceState);// 此方法必须重写
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        intData();
        setListener();
    }

    private void intData() {
        Intent intent = getIntent();
        String taskid = intent.getStringExtra("id");


        if (taskid != null) {
            GreenMissionLog unique = GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().queryBuilder()
                    .where(GreenMissionLogDao.Properties.Task_id.eq(taskid)).unique();
            mGreenMedias = unique.getGreenMedia();
            String taskName = intent.getStringExtra("taskName");
            String publisher = intent.getStringExtra("publishname");
            Long executeBeginTime = intent.getLongExtra("executeBeginTime", 0L);
            Long executeEndTime = intent.getLongExtra("executeEndTime", 0L);
            String area = intent.getStringExtra("area");
            mTvTaskid.setText(Html.fromHtml("<font color=\"#98CF60\">任务编号：</font>" + taskid));
            mTvTaskname.setText(Html.fromHtml("<font color=\"#98CF60\">任务名称：</font>" + taskName));
            mTvTaskpublish.setText(Html.fromHtml("<font color=\"#98CF60\">发布人：</font>" + publisher));
            mTvExebegintime.setText(Html.fromHtml("<font color=\"#98CF60\">开始时间：</font>" + executeBeginTime));
            mTvExeendtime.setText(Html.fromHtml("<font color=\"#98CF60\">结束时间：</font>" + executeEndTime));
            mTvTaskarea.setText(Html.fromHtml("<font color=\"#98CF60\">巡查区域：</font>" + area));
        }


        mLocJson = intent.getStringExtra("locJson");
        initMap();
    }

    private void initMap() {
        if (mAMap == null) {
            mAMap = mMap.getMap();
            mUiSettings = mAMap.getUiSettings();

        }
        mAMap.setMaxZoomLevel(20);
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// MAP_TYPE_SATELLITE卫星地图模式
        mUiSettings.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
//        mAMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        setupLocationStyle();
        addPolylineInPlayGround();
        addMarkersToMap();// 往地图上添加marker
        mAMap.setOnMyLocationChangeListener(this);
    }

    private void addMarkersToMap() {
        changeCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        mLatLngs.get(0), 18, 30, 30)));
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.poi_marker_pressed))
                .position(mLatLngs.get(mLatLngs.size()/2))
                .title("任务所在区域")
                .draggable(true);
        Marker marker = mAMap.addMarker(markerOption);
        marker.setClickable(false);
        marker.showInfoWindow();

        //添加起点
        MarkerOptions markerOptionBegin = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_start))
                .position(mLatLngs.get(0))
                .draggable(true);
        Marker markerBegin = mAMap.addMarker(markerOptionBegin);
        markerBegin.setClickable(false);
        markerBegin.showInfoWindow();


        //添加终点

        MarkerOptions markerOptionEnd = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_end))
                .position(mLatLngs.get(0))
                .draggable(true);
        Marker markerEnd = mAMap.addMarker(markerOptionEnd);
        markerEnd.setClickable(false);
        markerEnd.showInfoWindow();


        if (mGreenMedias != null && mGreenMedias.size() > 0) {
            for (GreenMedia greenMedia : mGreenMedias) {
                Integer type = greenMedia.getType();
                switch (type) {
                    case 1:                     //图片
                        GreenLocation picLocation = greenMedia.getLocation();
                        if (picLocation != null) {
                            markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.taking_picture))
                                    .position(new LatLng(Double.valueOf(picLocation.getLatitude()), Double.valueOf(picLocation.getLongitude())))
                                    .draggable(true);
                            marker = mAMap.addMarker(markerOption);
                            marker.setObject(greenMedia);
                            marker.showInfoWindow();
                        }

                        break;
                    case 2:                     //视频
                        GreenLocation videoLocation = greenMedia.getLocation();
                        if (videoLocation != null) {
                            markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.record_screen))
                                    .position(new LatLng(Double.valueOf(videoLocation.getLatitude()), Double.valueOf(videoLocation.getLongitude())))
                                    .draggable(true);
                            marker = mAMap.addMarker(markerOption);
                            marker.setObject(greenMedia);
                            marker.showInfoWindow();
                        }

                        break;
                    default:
                        break;
                }
            }
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMap.onPause();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
        mBind.unbind();
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMyLocationChange(Location location) {

    }


    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround() {
        List<LatLng> list = readLatLngs();
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.custtexture));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolyline = mAMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .width(18));

        if (list.size() > 0) {

            mBounds = new LatLngBounds(list.get(0), list.get(list.size() - 1));
            mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBounds, 100));
        }
    }


    /**
     * 读取坐标点
     *
     * @return
     */
    private List<LatLng> readLatLngs() {

        mLatLngs = DataUtil.praseJson(mLocJson, new TypeToken<List<LatLng>>() {

        });
        Log.i("Oking", "经纬度：" + mLatLngs.toString());
        return mLatLngs;
    }


    private void changeCamera(CameraUpdate cameraUpdate) {
        mAMap.moveCamera(cameraUpdate);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        GreenMedia greenMedia = (GreenMedia) marker.getObject();
        switch (greenMedia.getType()) {
            case 1:
                mIntent = new Intent(TrajectoryActivity.this, ImageViewActivity.class);
                GreenLocation location = greenMedia.getLocation();
                if (location != null) {
                    mIntent.putExtra("picLocation", location.getLongitude() + "," + location.getLatitude());
                }
                mIntent.setData(Uri.parse(greenMedia.getPath()));
                startActivity(mIntent);
                break;
            case 2:
                mIntent = new Intent(Intent.ACTION_VIEW);
                mIntent.setDataAndType(Uri.parse(greenMedia.getPath()),"video/mp4");
                startActivity(mIntent);
                break;
            default:
                break;
        }

        return true;
    }
}
