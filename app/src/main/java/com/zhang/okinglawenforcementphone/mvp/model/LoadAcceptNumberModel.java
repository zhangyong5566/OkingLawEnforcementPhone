package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadAcceptNumberContract;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/6/9/009.
 */

public class LoadAcceptNumberModel implements LoadAcceptNumberContract.Model {
    private LoadAcceptNumberContract.Presenter mPresenter;

    public LoadAcceptNumberModel(LoadAcceptNumberContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void loadAcceptNumber() {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .loadAcceptNumber()
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mPresenter.loadAcceptNumberSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.loadAcceptNumberFail(throwable);
                    }
                });
    }
}
