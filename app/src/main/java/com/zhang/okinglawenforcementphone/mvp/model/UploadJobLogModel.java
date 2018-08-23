package com.zhang.okinglawenforcementphone.mvp.model;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.baselib.utils.Util;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.Point;
import com.zhang.okinglawenforcementphone.beans.RecordLogOV;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadJobLogContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public class UploadJobLogModel implements UploadJobLogContract.Model {
    private UploadJobLogContract.Presenter mPresenter;
    private int mDatePoor;
    private long mBeforTime;
    private String mLocJson;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public UploadJobLogModel(UploadJobLogContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void uploadJobLog(final RecordLogOV recordLogOV, final Gson mGson) {

        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .getHttpMissionLog("-1", recordLogOV.getGreenMissionTask().getTaskid())
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {


                    @Override
                    public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                        //提交文本资料
                        String result = responseBody.string();
                        Log.i("Oking", ">>>>>>>>>>>>>>>>>1" + result);
                        Log.i("Oking5", ">>>>>>>>>>>>>>>>>1" + recordLogOV.toString());
                        JSONObject jsonObject = new JSONObject(result);
                        int count = jsonObject.getInt("total");
                        HashMap<String, Object> stringStringHashMap = new HashMap<>();
                        if (count > 0) {
                            JSONArray rows = jsonObject.getJSONArray("rows");
                            String oldId = rows.getJSONObject(0).getString("id");
                            stringStringHashMap.put("mode", 1);
                            stringStringHashMap.put("id", oldId);
                            recordLogOV.getGreenMissionLog().setServer_id(oldId);
                        } else {
                            stringStringHashMap.put("mode", 0);
                        }

                        stringStringHashMap.put("task_id", recordLogOV.getGreenMissionTask().getTaskid());
                        stringStringHashMap.put("name", OkingContract.CURRENTUSER.getUserid());
                        stringStringHashMap.put("time", recordLogOV.getTime());
                        stringStringHashMap.put("plan", recordLogOV.getSelePlanPos());
                        stringStringHashMap.put("item", recordLogOV.getSeleMattersPos());
                        stringStringHashMap.put("type", recordLogOV.getGreenMissionTask().getTask_type());
                        stringStringHashMap.put("area", recordLogOV.getArea());
                        boolean swisopen = recordLogOV.isSwisopen();
                        if (swisopen) {
                            stringStringHashMap.put("whetherComplete", "0");
                        } else {
                            stringStringHashMap.put("whetherComplete", "1");
                        }
                        stringStringHashMap.put("patrol", recordLogOV.getSummary());
                        stringStringHashMap.put("dzyj", recordLogOV.getLeaderSummary());
                        stringStringHashMap.put("status", 0);
                        stringStringHashMap.put("other_part", recordLogOV.getParts()==null?"":recordLogOV.getParts());
                        stringStringHashMap.put("examine_status", 0);
                        stringStringHashMap.put("equipment", recordLogOV.getEquipment()==null?"":recordLogOV.getEquipment());

                        //获取轨迹
                        getLocationTrajectory(recordLogOV, mGson);
                        stringStringHashMap.put("route", mLocJson);
                        stringStringHashMap.put("tbr", OkingContract.CURRENTUSER.getUserName());
                        stringStringHashMap.put("tbrid", OkingContract.CURRENTUSER.getUserid());

                        return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL).uploadJobLogForText(stringStringHashMap);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        Log.i("Oking", "位置数据、文本数据上传成功>>>>>>>>>>>>>>>>>2" + result);
                        mPresenter.uploadJobLogSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("Oking", "异常>>>>>>>>>>>>>>>>>3" + throwable.getLocalizedMessage() + throwable.toString());
                        mPresenter.uploadJobLogFail(throwable);

                    }
                });

    }

    private void getLocationTrajectory(RecordLogOV recordLogOV, Gson mGson) {
        Long beginTime = recordLogOV.getGreenMissionTask().getExecute_start_time();
        if (beginTime == null) {
            beginTime = System.currentTimeMillis() - 1000 * 60 * 20;
        }
        Long endTime = recordLogOV.getGreenMissionTask().getExecute_end_time();
        if (endTime == null) {
            endTime = System.currentTimeMillis();
            recordLogOV.getGreenMissionTask().setExecute_end_time(endTime);
        }
        mBeforTime = beginTime - 24 * 60 * 60 * 1000;
        final String file1 = sdf.format(beginTime);

        final ArrayList<Point> locationPath = new ArrayList<>();

        mDatePoor = Util.getDatePoor(beginTime, endTime);
        if (mDatePoor < 1) {        //表示在同一天
//                    Log.i("Oking","是同一天");
            List<String> locationPos = FileUtil.readFile2List(Environment.getExternalStorageDirectory() + "/oking/location/" + file1 + ".txt", "UTF-8");
            if (locationPos != null) {
                for (String s : locationPos) {
                    String[] items = s.split(",");
                    if (items.length != 3) {
                        continue;
                    }

                    String mLatitude = items[0];
                    String mLongitude = items[1];
                    String mDatetime = items[2];

                    if (Long.parseLong(mDatetime) > beginTime && Long.parseLong(mDatetime) < endTime) {
                        Point location = new Point();
                        location.setLatitude(Double.valueOf(mLatitude));
                        location.setLongitude(Double.valueOf(mLongitude));
                        location.setDatetime(Long.valueOf(mDatetime));
                        locationPath.add(location);
                    }
                }
            }


        } else {

            for (int i = 0; i <= mDatePoor; i++) {
                File file = new File(Environment.getExternalStorageDirectory() + "/oking/location/" + getAfterData(mBeforTime) + ".txt");
                if (file.exists()) {
//                            Log.i("Oking","不是同一天"+file.getName());
                    List<String> locationPos = FileUtil.readFile2List(file, "UTF-8");
                    for (String s : locationPos) {
                        String[] items = s.split(",");
                        if (items.length != 3) {
                            continue;
                        }

                        String Latitude = items[0];
                        String Longitude = items[1];
                        String datetime = items[2];

                        if (Long.parseLong(datetime) > beginTime && Long.parseLong(datetime) < endTime) {

                            Point location = new Point();
                            location.setLatitude(Double.valueOf(Latitude));
                            location.setLongitude(Double.valueOf(Longitude));
                            location.setDatetime(Long.valueOf(datetime));
                            locationPath.add(location);
                        }
                    }

                }

            }
        }

        //筛选一下，不然点集太多
        if (locationPath.size() > 100) {
            ArrayList<Point> newLocationPath = new ArrayList<>();
            for (int i = 0; i < locationPath.size(); i = i + 2) {

                newLocationPath.add(locationPath.get(i));
            }

            mLocJson = mGson.toJson(newLocationPath);

        } else {

            mLocJson = mGson.toJson(locationPath);
        }
    }

    private String getAfterData(long time) {
        //如果需要向后计算日期 -改为+
        Date newDate = new Date(time + 24 * 60 * 60 * 1000);
        mBeforTime = newDate.getTime();
        String dateOk = sdf.format(newDate);
        return dateOk;
    }


}
