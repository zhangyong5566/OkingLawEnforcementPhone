package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.LoadAcceptNumberContract;
import com.zhang.okinglawenforcementphone.mvp.model.LoadAcceptNumberModel;

/**
 * Created by Administrator on 2018/6/9/009.
 */

public class LoadAcceptNumberPresenter implements LoadAcceptNumberContract.Presenter {
    private LoadAcceptNumberContract.Model mModel;
    private LoadAcceptNumberContract.View mView;

    public LoadAcceptNumberPresenter(LoadAcceptNumberContract.View view) {
        mView = view;
        mModel = new LoadAcceptNumberModel(this);
    }

    @Override
    public void loadAcceptNumber() {
        mModel.loadAcceptNumber();
    }

    @Override
    public void loadAcceptNumberSucc(String result) {
        mView.loadAcceptNumberSucc(result);
    }

    @Override
    public void loadAcceptNumberFail(Throwable ex) {
        mView.loadAcceptNumberFail(ex);
    }
}
