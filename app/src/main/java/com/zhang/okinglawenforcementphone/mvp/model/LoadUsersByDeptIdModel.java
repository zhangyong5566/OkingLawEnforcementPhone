package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadUsersByDeptIdContract;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/6/8/008.
 */

public class LoadUsersByDeptIdModel implements LoadUsersByDeptIdContract.Model {
    private LoadUsersByDeptIdContract.Presenter mPresenter;

    public LoadUsersByDeptIdModel(LoadUsersByDeptIdContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getUsersByDeptId(String deptId) {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .getUsersByDeptId(deptId)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mPresenter.getUsersByDeptIdSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.getUsersByDeptIdFail(throwable);
                    }
                });
    }
}
