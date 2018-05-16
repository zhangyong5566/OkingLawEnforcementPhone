package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.UpdateMissionStateContract;
import com.zhang.okinglawenforcementphone.mvp.model.UpdateMissionStateModel;

/**
 * Created by Administrator on 2018/4/20.
 */

public class UpdateMissionStatePresenter implements UpdateMissionStateContract.Presenter {
    private UpdateMissionStateContract.View mView;
    private UpdateMissionStateContract.Model mModel;

    public UpdateMissionStatePresenter(UpdateMissionStateContract.View view) {
        mView = view;
        mModel = new UpdateMissionStateModel(this);

    }

    @Override
    public void updateMissionState(String id, String executeStartTime, String executeEndTime, int status) {
        mModel.updateMissionState(id,executeStartTime,executeEndTime,status);
    }

    @Override
    public void updateMissionStateSucc(String result) {
        mView.updateMissionStateSucc(result);
    }

    @Override
    public void updateMissionStateFail(Throwable ex) {
        mView.updateMissionStateFail(ex);
    }
}
