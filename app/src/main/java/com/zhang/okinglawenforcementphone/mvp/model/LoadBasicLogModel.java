package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadBasicLogContract;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/6/8/008.
 */

public class LoadBasicLogModel implements LoadBasicLogContract.Model {
    private LoadBasicLogContract.Presenter mPresenterl;

    public LoadBasicLogModel(LoadBasicLogContract.Presenter presenterl) {
        mPresenterl = presenterl;
    }

    @Override
    public void getBasicLog(String taskId) {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .getBasicLog(taskId)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mPresenterl.getBasicLogSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenterl.getBasicLogFail(throwable);
                    }
                });
    }
}
