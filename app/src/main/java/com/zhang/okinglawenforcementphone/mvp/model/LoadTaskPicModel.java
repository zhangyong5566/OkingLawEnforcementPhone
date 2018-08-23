package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadTaskPicContract;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/6/8/008.
 */

public class LoadTaskPicModel implements LoadTaskPicContract.Model {
    private LoadTaskPicContract.Presenter mPresenter;

    public LoadTaskPicModel(LoadTaskPicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void loadTaskPic(String logId) {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .loadTaskPic(logId)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mPresenter.loadTaskPicSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.loadTaskPicFail(throwable);
                    }
                });
    }
}
