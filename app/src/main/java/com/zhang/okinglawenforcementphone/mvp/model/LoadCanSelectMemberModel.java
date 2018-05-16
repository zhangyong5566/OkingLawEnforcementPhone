package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.htttp.Api;
import com.zhang.okinglawenforcementphone.htttp.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadCanSelectMemberContract;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/20.
 */

public class LoadCanSelectMemberModel implements LoadCanSelectMemberContract.Model {
    private LoadCanSelectMemberContract.Presenter mPresenter;

    public LoadCanSelectMemberModel(LoadCanSelectMemberContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void loadCanSelectMember() {
        BaseHttpFactory.getInstence()
                .createService(GDWaterService.class, Api.BASE_URL)
                .loadCanSelectMember("SZJC,CBR")
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        mPresenter.loadCanSelectMemberSucc(responseBody.string());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.loadCanSelectMemberFail(throwable);
                    }
                });

    }
}
