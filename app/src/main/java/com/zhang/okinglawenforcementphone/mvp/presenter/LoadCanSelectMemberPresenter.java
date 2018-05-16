package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.LoadCanSelectMemberContract;
import com.zhang.okinglawenforcementphone.mvp.model.LoadCanSelectMemberModel;

/**
 * Created by Administrator on 2018/4/20.
 */

public class LoadCanSelectMemberPresenter implements LoadCanSelectMemberContract.Presenter {
    private LoadCanSelectMemberContract.View mView;
    private LoadCanSelectMemberContract.Model mModel;

    public LoadCanSelectMemberPresenter(LoadCanSelectMemberContract.View view) {
        mView = view;
        mModel = new LoadCanSelectMemberModel(this);
    }

    @Override
    public void loadCanSelectMember() {
        mModel.loadCanSelectMember();
    }

    @Override
    public void loadCanSelectMemberSucc(String result) {
        mView.loadCanSelectMemberSucc(result);
    }

    @Override
    public void loadCanSelectMemberFail(Throwable ex) {
        mView.loadCanSelectMemberFail(ex);
    }
}
