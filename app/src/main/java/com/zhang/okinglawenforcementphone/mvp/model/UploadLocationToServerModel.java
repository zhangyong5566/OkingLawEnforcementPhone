package com.zhang.okinglawenforcementphone.mvp.model;

import android.text.format.DateFormat;

import com.google.gson.Gson;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.Point;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadLocationToServerContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/20.
 */

public class UploadLocationToServerModel implements UploadLocationToServerContract.Model {
    private UploadLocationToServerContract.Presenter mPresenter;
    private Point mLocation;
    public UploadLocationToServerModel(UploadLocationToServerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void upploadLocationToServer(Long loginTime, SimpleDateFormat sdf, String imei, Gson gson) {
        if (mLocation==null){

            mLocation = new Point();
        }
        mLocation.setLatitude(Double.parseDouble(OkingContract.LOCATIONRESULT[1]));
        mLocation.setLongitude(Double.parseDouble(OkingContract.LOCATIONRESULT[2]));
        try {
            mLocation.setDatetime(sdf.parse(OkingContract.LOCATIONRESULT[3]).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .uploadLocation(loginTime,
                        OkingContract.CURRENTUSER.getUserid(),
                        imei,gson.toJson(mLocation), DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()).toString())
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mPresenter.uploadSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.uploadFail(throwable);
                    }
                });

    }
}
