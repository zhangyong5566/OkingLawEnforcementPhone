package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadCaseInAdvanceContract;

import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/5/4/004.
 */

public class UploadCaseInAdvanceModel implements UploadCaseInAdvanceContract.Model {
    private UploadCaseInAdvanceContract.Presenter mPresenter;

    public UploadCaseInAdvanceModel(UploadCaseInAdvanceContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void uploadCaseInAdvance(Map<String, Object> params) {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .uploadCaseInAdvance(params)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mPresenter.uploadCaseInAdvanceSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.uploadCaseInAdvanceFail(throwable);
                    }
                });
    }
}
