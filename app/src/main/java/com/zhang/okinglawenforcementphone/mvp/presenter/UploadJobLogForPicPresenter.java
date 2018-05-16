package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.google.gson.Gson;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadJobLogForPicContract;
import com.zhang.okinglawenforcementphone.mvp.model.UploadJobLogForPicModel;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public class UploadJobLogForPicPresenter implements UploadJobLogForPicContract.Presenter {
    private UploadJobLogForPicContract.Model mModel;
    private UploadJobLogForPicContract.View mView;

    public UploadJobLogForPicPresenter(UploadJobLogForPicContract.View view) {
        mView = view;
        mModel = new UploadJobLogForPicModel(this);
    }

    @Override
    public void uploadJobLogForPic(Gson gson, Map<String, RequestBody> photoParams, GreenMissionLog greenMissionLog, List<GreenMedia> media) {
        mModel.uploadJobLogForPic(gson, photoParams, greenMissionLog, media);
    }

    @Override
    public void uploadSucc(String result) {

        mView.uploadSucc(result);
    }

    @Override
    public void uploadRetry(Throwable ex) {
        mView.uploadRetry(ex);
    }

    @Override
    public void uploadFail(Throwable ex) {
        mView.uploadFail(ex);
    }

    @Override
    public void uploadIsCount(int pos) {
        mView.uploadIsCount(pos);
    }

    @Override
    public void uploadPositionFail(Throwable ex) {
        mView.uploadPositionFail(ex);
    }
}
