package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.google.gson.reflect.TypeToken;
import com.yinghe.whiteboardlib.bean.StrokeRecord;
import com.yinghe.whiteboardlib.view.SketchView;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.utils.DataUtil;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.LatLngListOV;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.views.MapDraw;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MapByPointActivity extends BaseActivity implements AMap.OnMyLocationChangeListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.map)
    MapView mMap;
    @BindView(R.id.rb_circular)
    RadioButton mRbCircular;
    @BindView(R.id.rb_polygon)
    RadioButton mRbPolygon;
    @BindView(R.id.rb_move_map)
    RadioButton mRbMoveMap;
    @BindView(R.id.md)
    MapDraw mMd;
    private Unbinder mBind;
    private AMap mAMap;
    private UiSettings mUiSettings;
    private LatLng mLng;

    private int mDrawType;

    private StrokeRecord mStrokeRecord;
    private RxDialogSureCancel mRxDialogSureCancel;
    private String mMcoordinateJson;
    private int mDrawLaLoType;
    private LatLng mCenterPoint;
    private float mZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_by_point);
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
        mRbCircular.setOnCheckedChangeListener(this);
        mRbPolygon.setOnCheckedChangeListener(this);
        mRbMoveMap.setOnCheckedChangeListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        mMcoordinateJson = intent.getStringExtra("mcoordinateJson");
        mDrawLaLoType = intent.getIntExtra("drawLaLoType", 4);
        float left = intent.getFloatExtra("left", 0);
        float right = intent.getFloatExtra("right", 0);
        float top = intent.getFloatExtra("top", 0);
        float bottom = intent.getFloatExtra("bottom", 0);
        mZoom = intent.getFloatExtra("zoom", 0);
        mCenterPoint = (LatLng) intent.getParcelableExtra("centerPoint");
        initMap();

        mStrokeRecord = new StrokeRecord();
        mStrokeRecord.setType(mDrawLaLoType);
        Paint strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setDither(true);
        strokePaint.setColor(Color.RED);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeJoin(Paint.Join.ROUND);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setStrokeWidth(3);
        if (left != 0) {
            RectF rect = new RectF(left, top, right, bottom);
            mStrokeRecord.rect = rect;
        }

        mStrokeRecord.paint = strokePaint;
        mMd.setOnDrawChangedListener(new MapDraw.OnDrawChangedListener() {
            @Override
            public void onDrawChanged(final LatLngListOV latLngListOV) {
                if (mRxDialogSureCancel == null) {
                    mRxDialogSureCancel = new RxDialogSureCancel(MapByPointActivity.this, false, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            dialogInterface.cancel();
                        }
                    });

                    mRxDialogSureCancel.setContent("确认巡查范围已标记无误？");
                    mRxDialogSureCancel.getTvCancel().setText("重来");
                    mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRxDialogSureCancel.cancel();
                        }
                    });
                    mRxDialogSureCancel.getTvSure().setText("完成");
                    mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRxDialogSureCancel.cancel();

                            EventBus.getDefault().post(latLngListOV);
                            finish();
                        }
                    });
                }

                mRxDialogSureCancel.show();
            }
        });
        mMd.setAmap(mAMap);
        mMd.setStrokeRecord(mStrokeRecord);
    }

    private void initMap() {
        if (mAMap == null) {
            mAMap = mMap.getMap();
            mUiSettings = mAMap.getUiSettings();
        }
        mAMap.setMaxZoomLevel(20);
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// MAP_TYPE_SATELLITE卫星地图模式
        mUiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();
        mAMap.setOnMyLocationChangeListener(this);

        Log.i("Oking",">>>"+mMcoordinateJson);
    }

    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(180, 3, 145, 255));
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(10, 0, 0, 180));
        // 将自定义的 myLocationStyle 对象添加到地图上
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        mAMap.setMyLocationStyle(myLocationStyle);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
        mBind.unbind();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMap.onResume();
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
    public void onMyLocationChange(Location location) {
        if (location != null) {
            mLng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            Bundle bundle = location.getExtras();
            if (bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                /*
                errorCode
                errorInfo
                locationType
                */

                if (mCenterPoint == null) {
                    changeCamera(
                            CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                    mLng, 18, 30, 30)));
                } else {
                    changeCamera(
                            CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                    mCenterPoint, mZoom, 30, 30)));
                }

                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType);
            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
        }

    }

    private void changeCamera(CameraUpdate cameraUpdate) {
        mAMap.moveCamera(cameraUpdate);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        switch (compoundButton.getId()) {

            case R.id.rb_circular:
                if (b) {
                    mUiSettings.setScrollGesturesEnabled(false);
                    mMd.setVisibility(View.VISIBLE);
                    mDrawType = 4;
                    mStrokeRecord.setType(mDrawType);
                    mMd.setStrokeRecord(mStrokeRecord);
                }
                break;
            case R.id.rb_polygon:
                if (b) {
                    mUiSettings.setScrollGesturesEnabled(false);
                    mMd.setVisibility(View.VISIBLE);
                    mDrawType = 5;
                    mStrokeRecord.setType(mDrawType);
                    mMd.setStrokeRecord(mStrokeRecord);

                }
                break;
            case R.id.rb_move_map:
                if (b) {
                    mUiSettings.setScrollGesturesEnabled(true);
                    mMd.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }


}
