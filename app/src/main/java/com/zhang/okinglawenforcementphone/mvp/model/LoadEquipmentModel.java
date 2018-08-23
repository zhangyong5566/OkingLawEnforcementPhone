package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadEquipmentContract;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public class LoadEquipmentModel implements LoadEquipmentContract.Model {
    private LoadEquipmentContract.Presenter mPresenter;

    public LoadEquipmentModel(LoadEquipmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void loadEquipment() {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .loadEquipment()
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mPresenter.loadEquipmentSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.loadEquipmentFail(throwable);
                    }
                });

    }
}
