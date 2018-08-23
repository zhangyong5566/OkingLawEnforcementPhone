package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.TaskBackContract;
import com.zhang.okinglawenforcementphone.mvp.model.TaskBackModel;

import java.util.Map;

import retrofit2.http.FieldMap;

/**
 * Created by Administrator on 2018/6/7/007.
 */

public class TaskBackPresenter implements TaskBackContract.Presenter {
    private TaskBackContract.Model mModel;
    private TaskBackContract.View mView;

    public TaskBackPresenter(TaskBackContract.View view) {
        mView = view;
        mModel = new TaskBackModel(this);
    }

    @Override
    public void taskBack(Map<String, Object> params) {
        mModel.taskBack(params);
    }

    @Override
    public void taskBackSucc(String result) {
        mView.taskBackSucc(result);
    }

    @Override
    public void taskBackFail(Throwable ex) {
        mView.taskBackFail(ex);
    }
}
