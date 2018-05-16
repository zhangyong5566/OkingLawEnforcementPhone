package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.LoadEquipmentContract;
import com.zhang.okinglawenforcementphone.mvp.model.LoadEquipmentModel;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public class LoadEquipmentPresenter implements LoadEquipmentContract.Presenter {
    private LoadEquipmentContract.Model mModel;
    private LoadEquipmentContract.View mView;

    public LoadEquipmentPresenter(LoadEquipmentContract.View view) {
        mView = view;
        mModel = new LoadEquipmentModel(this);
    }

    @Override
    public void loadEquipment() {
        mModel.loadEquipment();

    }

    @Override
    public void loadEquipmentSucc(String result) {
        mView.loadEquipmentSucc(result);
    }

    @Override
    public void loadEquipmentFail(Throwable ex) {
        mView.loadEquipmentFail(ex);
    }
}
