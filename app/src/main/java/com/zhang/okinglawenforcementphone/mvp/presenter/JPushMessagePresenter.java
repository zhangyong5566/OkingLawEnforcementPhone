package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.beans.JPushMessageBean;
import com.zhang.okinglawenforcementphone.mvp.contract.JPushMessageContract;
import com.zhang.okinglawenforcementphone.mvp.model.JPushMessageModel;

/**
 * Created by Administrator on 2018/8/27/027.
 */

public class JPushMessagePresenter implements JPushMessageContract.Presenter {
    private JPushMessageContract.Model mModel;
    private JPushMessageContract.View mView;

    public JPushMessagePresenter(JPushMessageContract.View view) {
        mView = view;
        mModel = new JPushMessageModel(this);
    }

    @Override
    public void pushMessage(JPushMessageBean jPushMessageBean) {
        mModel.pushMessage(jPushMessageBean);
    }

    @Override
    public void pushMessageSucc(String result) {
        mView.pushMessageSucc(result);
    }

    @Override
    public void pushMessageFail(Throwable ex) {
        mView.pushMessageFail(ex);
    }
}
