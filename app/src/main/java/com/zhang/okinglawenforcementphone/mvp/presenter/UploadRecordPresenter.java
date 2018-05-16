package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.UploadRecordContract;
import com.zhang.okinglawenforcementphone.mvp.model.UploadRecordModel;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/28/028.
 */

public class UploadRecordPresenter implements UploadRecordContract.Presenter {
    private UploadRecordContract.Model mModel;
    private UploadRecordContract.View mView;

    public UploadRecordPresenter(UploadRecordContract.View view) {
        mView = view;
        mModel = new UploadRecordModel(this);
    }

    @Override
    public void uploadRecord(Map<String, Object> params) {
        mModel.uploadRecord(params);
    }

    @Override
    public void uploadRecordSucc(String result) {
        mView.uploadRecordSucc(result);
    }

    @Override
    public void uploadRecordFail(Throwable ex) {
        mView.uploadRecordFail(ex);
    }
}
