package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.LoadTaskPicContract;
import com.zhang.okinglawenforcementphone.mvp.model.LoadTaskPicModel;

/**
 * Created by Administrator on 2018/6/8/008.
 */

public class LoadTaskPicPresenter implements LoadTaskPicContract.Presenter {
    private LoadTaskPicContract.View mView;
    private LoadTaskPicContract.Model mModel;

    public LoadTaskPicPresenter(LoadTaskPicContract.View view) {
        mView = view;
        mModel = new LoadTaskPicModel(this);
    }

    @Override
    public void loadTaskPic(String logId) {
        mModel.loadTaskPic(logId);
    }

    @Override
    public void loadTaskPicSucc(String result) {
        mView.loadTaskPicSucc(result);
    }

    @Override
    public void loadTaskPicFail(Throwable e) {
        mView.loadTaskPicFail(e);
    }
}
