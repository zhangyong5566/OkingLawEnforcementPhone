package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.LoadBasicLogContract;
import com.zhang.okinglawenforcementphone.mvp.model.LoadBasicLogModel;

/**
 * Created by Administrator on 2018/6/8/008.
 */

public class LoadBasicLogPresenter implements LoadBasicLogContract.Presenter {
    private LoadBasicLogContract.Model mModel;
    private LoadBasicLogContract.View mView;

    public LoadBasicLogPresenter(LoadBasicLogContract.View view) {
        mView = view;
        mModel = new LoadBasicLogModel(this);
    }

    @Override
    public void getBasicLog(String taskId) {
        mModel.getBasicLog(taskId);
    }

    @Override
    public void getBasicLogSucc(String result) {
        mView.getBasicLogSucc(result);
    }

    @Override
    public void getBasicLogFail(Throwable ex) {
        mView.getBasicLogFail(ex);
    }
}
