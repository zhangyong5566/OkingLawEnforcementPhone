package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.google.gson.Gson;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadLocationToServerContract;
import com.zhang.okinglawenforcementphone.mvp.model.UploadLocationToServerModel;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2018/4/20.
 */

public class UploadLocationToServerPresenter implements UploadLocationToServerContract.Presenter {
    private UploadLocationToServerContract.Model mModel;
    private UploadLocationToServerContract.View mView;

    public UploadLocationToServerPresenter(UploadLocationToServerContract.View view) {
        mView = view;
        mModel = new UploadLocationToServerModel(this);
    }

    @Override
    public void upploadLocationToServer(Long loginTime, SimpleDateFormat sdf, String imei, Gson gson) {
        mModel.upploadLocationToServer(loginTime,sdf,imei,gson);
    }

    @Override
    public void uploadSucc(String result) {
        mView.uploadSucc(result);
    }

    @Override
    public void uploadFail(Throwable ex) {
        mView.uploadFail(ex);
    }
}
