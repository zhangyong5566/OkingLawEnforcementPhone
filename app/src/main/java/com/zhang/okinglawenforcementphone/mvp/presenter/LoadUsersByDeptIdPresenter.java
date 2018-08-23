package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.LoadUsersByDeptIdContract;
import com.zhang.okinglawenforcementphone.mvp.model.LoadUsersByDeptIdModel;

/**
 * Created by Administrator on 2018/6/8/008.
 */

public class LoadUsersByDeptIdPresenter implements LoadUsersByDeptIdContract.Presenter {
    private LoadUsersByDeptIdContract.Model mModel;
    private LoadUsersByDeptIdContract.View mView;

    public LoadUsersByDeptIdPresenter(LoadUsersByDeptIdContract.View view) {
        mView = view;
        mModel = new LoadUsersByDeptIdModel(this);
    }

    @Override
    public void getUsersByDeptId(String deptId) {
        mModel.getUsersByDeptId(deptId);
    }

    @Override
    public void getUsersByDeptIdSucc(String result) {
        mView.getUsersByDeptIdSucc(result);
    }

    @Override
    public void getUsersByDeptIdFail(Throwable e) {
        mView.getUsersByDeptIdFail(e);
    }
}
