package com.zhang.okinglawenforcementphone.mvp.model;

import android.util.Log;

import com.google.gson.Gson;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.beans.JPushMessageBean;
import com.zhang.okinglawenforcementphone.jpush.clien.JPushAPI;
import com.zhang.okinglawenforcementphone.jpush.clien.JPushClienService;
import com.zhang.okinglawenforcementphone.jpush.clien.JPushHttpFactory;
import com.zhang.okinglawenforcementphone.mvp.contract.JPushMessageContract;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/8/27/027.
 */

public class JPushMessageModel implements JPushMessageContract.Model {
    private JPushMessageContract.Presenter mPresenter;

    public JPushMessageModel(JPushMessageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void pushMessage(JPushMessageBean jPushMessageBean) {

        String s = new Gson().toJson(jPushMessageBean);
        Log.i("Oking5", s);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), s);

        JPushHttpFactory.getInstence().createService(JPushClienService.class, JPushAPI.BASE_URL)
                .pushMessage(requestBody)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        mPresenter.pushMessageSucc("推送成功");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.pushMessageFail(throwable);
                    }
                });
    }
}
