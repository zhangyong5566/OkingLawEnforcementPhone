package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadSignaturePicContract;
import com.zhang.okinglawenforcementphone.mvp.model.UploadSignaturePicModel;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public class UploadSignaturePicPresenter implements UploadSignaturePicContract.Presenter {
    private UploadSignaturePicContract.Model mModel;
    private UploadSignaturePicContract.View mView;

    public UploadSignaturePicPresenter(UploadSignaturePicContract.View view) {
        mView = view;
        mModel = new UploadSignaturePicModel(this);
    }

    @Override
    public void uploadSignaturePic(GreenMissionLog mGreenMissionLog, GreenMissionTask missionTask, Map<String, RequestBody> photoParams) {
        mModel.uploadSignaturePic(mGreenMissionLog,missionTask,photoParams);
    }

    @Override
    public void uploadSignaturePicSucc(String result) {
        mView.uploadSignaturePicSucc(result);
    }

    @Override
    public void uploadIsCount(int pos) {
        mView.uploadIsCount(pos);
    }

    @Override
    public void uploadRetry(Throwable ex) {
        mView.uploadRetry(ex);
    }

    @Override
    public void uploadSignatureFail(Throwable ex) {
        mView.uploadSignatureFail(ex);
    }
}
