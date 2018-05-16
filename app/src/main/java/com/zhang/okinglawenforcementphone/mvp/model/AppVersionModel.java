package com.zhang.okinglawenforcementphone.mvp.model;

import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.utils.DeviceUtil;
import com.zhang.okinglawenforcementphone.htttp.Api;
import com.zhang.okinglawenforcementphone.htttp.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.AppVersionContract;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/18.
 */

public class AppVersionModel implements AppVersionContract.Model {
    private AppVersionContract.Presenter mPresenter;

    public AppVersionModel(AppVersionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void reqAppVersion() {
        GDWaterService service = BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL);

        service.reqAppVersion("app_version")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        JSONArray jsonArray = new JSONArray(responseBody.string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String value = jsonObject.getString("VALUE");
                            String bz = jsonObject.getString("BZ");
                            if (!value.equals(DeviceUtil.getAppVersionName(BaseApplication.getApplictaion()))) {

                                mPresenter.reqSucc(bz);


                            }

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.reqFail(throwable);
                    }
                });

    }
}
