package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.mvp.contract.GetHttpMissionLogContract;
import com.zhang.okinglawenforcementphone.mvp.model.GetHttpMissionLogModel;

import java.io.File;

/**
 * Created by Administrator on 2018/4/23/023.
 */

public class GetHttpMissionLogPresenter implements GetHttpMissionLogContract.Presenter {
    private GetHttpMissionLogContract.View mView;
    private GetHttpMissionLogContract.Model mModel;

    public GetHttpMissionLogPresenter(GetHttpMissionLogContract.View view) {
        mView = view;
        mModel = new GetHttpMissionLogModel(this);
    }

    @Override
    public void getHttpMissionLog(GreenMissionTask mission) {
        mModel.getHttpMissionLog(mission);
    }

    @Override
    public void loadHttpMissionLogSucc(GreenMissionLog greenMissionLog) {
        mView.loadHttpMissionLogSucc(greenMissionLog);

    }


    @Override
    public void loadEmpty(GreenMissionLog greenMissionLog) {
        mView.loadEmpty(greenMissionLog);
    }
}
