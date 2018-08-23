package com.zhang.okinglawenforcementphone.mvp.presenter;

import com.zhang.okinglawenforcementphone.mvp.contract.TaskReviewContract;
import com.zhang.okinglawenforcementphone.mvp.model.TaskReviewModel;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/6/006.
 */

public class TaskReviewPresenter implements TaskReviewContract.Presenter {
    private TaskReviewContract.Model mModel;
    private TaskReviewContract.View mView;

    public TaskReviewPresenter(TaskReviewContract.View view) {
        mView = view;
        mModel = new TaskReviewModel(this);
    }

    @Override
    public void taskReview(Map<String, Object> params) {
        mModel.taskReview(params);
    }

    @Override
    public void taskReviewSucc(String result) {
        mView.taskReviewSucc(result);
    }

    @Override
    public void taskReviewFail(Throwable ex) {
        mView.taskReviewFail(ex);
    }
}
