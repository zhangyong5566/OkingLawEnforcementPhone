package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.google.gson.Gson;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadVideoContract;
import com.zhang.okinglawenforcementphone.mvp.model.UploadVideoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public class UploadVideoPresenter implements UploadVideoContract.Presenter {
    private UploadVideoContract.Model mModel;
    private UploadVideoContract.View mView;

    public UploadVideoPresenter(UploadVideoContract.View view) {
        mView = view;
        mModel = new UploadVideoModel(this);
    }

    @Override
    public void uploadVideo(GreenMissionLog greenMissionLog, List<GreenMedia> greenMedias, Map<String, RequestBody> photoParams, SimpleDateFormat videosdf, Gson gson) {
        mModel.uploadVideo(greenMissionLog, greenMedias, photoParams, videosdf, gson);
    }

    @Override
    public void loadVideoSucc(String result) {
        mView.loadVideoSucc(result);
    }

    @Override
    public void uploadRetry(Throwable ex) {
        mView.uploadRetry(ex);
    }

    @Override
    public void loadVideoFail(Throwable ex) {
        mView.loadVideoFail(ex);
    }

    @Override
    public void uploadIsCount(int pos) {
        mView.uploadIsCount(pos);
    }
}
