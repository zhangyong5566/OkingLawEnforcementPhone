package com.zhang.okinglawenforcementphone.mvp.model;

import android.widget.Toast;

import com.google.gson.Gson;
import com.zhang.baselib.DefaultContants;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.ProblemBean;
import com.zhang.okinglawenforcementphone.htttp.Api;
import com.zhang.okinglawenforcementphone.htttp.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadRecordContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/28/028.
 */

public class UploadRecordModel implements UploadRecordContract.Model {
    private UploadRecordContract.Presenter mPresenter;

    public UploadRecordModel(UploadRecordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void uploadRecord(Map<String, Object> params) {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .uploadRecord(params)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        mPresenter.uploadRecordSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.uploadRecordFail(throwable);
                    }
                });

    }

}
