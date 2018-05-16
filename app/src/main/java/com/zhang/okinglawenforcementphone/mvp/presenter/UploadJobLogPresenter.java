package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.google.gson.Gson;
import com.zhang.okinglawenforcementphone.beans.RecordLogOV;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadJobLogContract;
import com.zhang.okinglawenforcementphone.mvp.model.UploadJobLogModel;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public class UploadJobLogPresenter implements UploadJobLogContract.Presenter {
    private UploadJobLogContract.Model mModel;
    private UploadJobLogContract.View mView;

    public UploadJobLogPresenter(UploadJobLogContract.View view) {
        mView = view;
        mModel = new UploadJobLogModel(this);
    }

    @Override
    public void uploadJobLog(RecordLogOV recordLogOV, Gson gson) {
        mModel.uploadJobLog(recordLogOV,gson);
    }

    @Override
    public void uploadJobLogSucc(String result) {
        mView.uploadJobLogSucc(result);

    }

    @Override
    public void uploadJobLogFail(Throwable ex) {
        mView.uploadJobLogFail(ex);
    }
}
