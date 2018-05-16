package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadHttpMissionContract;
import com.zhang.okinglawenforcementphone.mvp.model.LoadHttpMissionModel;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public class LoadHttpMissionPresenter implements LoadHttpMissionContract.Presenter {
    private LoadHttpMissionContract.Model mModel;
    private LoadHttpMissionContract.View mView;

    public LoadHttpMissionPresenter(LoadHttpMissionContract.View view) {
        mView = view;
        mModel = new LoadHttpMissionModel(this);
    }

    @Override
    public void loadHttpMissionSucc(List<GreenMissionTask> greenMissionTasks) {
        mView.loadHttpMissionSucc(greenMissionTasks);
    }

    @Override
    public void loadHttpMission(int classify, String receiver) {
        mModel.loadHttpMission(classify,receiver);
    }

    @Override
    public void loadHttpMissionProgress(int total, int count) {
        mView.loadHttpMissionProgress(total,count);
    }

    @Override
    public void loadMissionFromLocal(List<GreenMissionTask> greenMissionTasks) {
            mView.loadMissionFromLocal(greenMissionTasks);
    }

    @Override
    public void loadHttpMissionFail(Throwable ex) {
    mView.loadHttpMissionFail(ex);
    }
}
