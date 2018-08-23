package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.AddMemberContract;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/20.
 */

public class AddMemberModel implements AddMemberContract.Model {
    private AddMemberContract.Presenter mPresenter;

    public AddMemberModel(AddMemberContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void addMember(String userid, String mtaskId, String userids) {

        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .addMember(userid,mtaskId,userids)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        mPresenter.addMemberSucc(responseBody.string());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.addMemberFail(throwable);
                    }
                });
    }
}
