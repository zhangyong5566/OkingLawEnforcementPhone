package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.UpdateMissionStateContract;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/20.
 */

public class UpdateMissionStateModel implements UpdateMissionStateContract.Model {
    private UpdateMissionStateContract.Presenter mPresenter;

    public UpdateMissionStateModel(UpdateMissionStateContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateMissionState(String id, String executeStartTime, String executeEndTime, int status) {
        BaseHttpFactory.getInstence()
                .createService(GDWaterService.class, Api.BASE_URL)
                .updateMissionState(id, executeStartTime, executeEndTime, status)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mPresenter.updateMissionStateSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.updateMissionStateFail(throwable);
                    }
                });
    }
}
