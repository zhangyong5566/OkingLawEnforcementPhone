package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.AppVersionContract;
import com.zhang.okinglawenforcementphone.mvp.model.AppVersionModel;

/**
 * Created by Administrator on 2018/4/18.
 */

public class AppVersionPresenter implements AppVersionContract.Presenter {
    private AppVersionContract.Model mModel;
    private AppVersionContract.View mView;

    public AppVersionPresenter(AppVersionContract.View view) {
        mView = view;
        mModel = new AppVersionModel(this);
    }

    @Override
    public void reqAppVersion() {
        mModel.reqAppVersion();
    }

    @Override
    public void reqSucc(String result) {
        mView.reqSucc(result);

    }

    @Override
    public void reqFail(Throwable ex) {
        mView.reqFail(ex);
    }
}
