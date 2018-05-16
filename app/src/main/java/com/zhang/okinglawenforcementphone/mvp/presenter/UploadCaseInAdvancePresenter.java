package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.UploadCaseInAdvanceContract;
import com.zhang.okinglawenforcementphone.mvp.model.UploadCaseInAdvanceModel;

import java.util.Map;

/**
 * Created by Administrator on 2018/5/4/004.
 */

public class UploadCaseInAdvancePresenter implements UploadCaseInAdvanceContract.Presenter {
    private UploadCaseInAdvanceContract.Model mModel;
    private UploadCaseInAdvanceContract.View mView;

    public UploadCaseInAdvancePresenter(UploadCaseInAdvanceContract.View view) {
        mView = view;
        mModel = new UploadCaseInAdvanceModel(this);
    }

    @Override
    public void uploadCaseInAdvance(Map<String, Object> params) {
        mModel.uploadCaseInAdvance(params);
    }

    @Override
    public void uploadCaseInAdvanceSucc(String result) {
        mView.uploadCaseInAdvanceSucc(result);
    }

    @Override
    public void uploadCaseInAdvanceFail(Throwable ex) {
        mView.uploadCaseInAdvanceFail(ex);
    }
}
