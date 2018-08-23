package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.TaskReviewContract;

import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/6/6/006.
 */

public class TaskReviewModel implements TaskReviewContract.Model {
    private TaskReviewContract.Presenter mPresenter;

    public TaskReviewModel(TaskReviewContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void taskReview(Map<String, Object> params) {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .taskReview(params)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mPresenter.taskReviewSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.taskReviewFail(throwable);
                    }
                });
    }
}
