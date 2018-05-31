package com.zhang.okinglawenforcementphone.mvp.model;

import android.net.Uri;
import android.util.Log;

import com.zhang.baselib.DefaultContants;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.beans.GreenEvidence;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.htttp.Api;
import com.zhang.okinglawenforcementphone.htttp.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadEvidenceContract;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/5/8/008.
 */

public class UploadEvidenceModel implements UploadEvidenceContract.Model {
    private UploadEvidenceContract.Presenter mPresenter;
    private int uploadPicCount = 0;
    public UploadEvidenceModel(UploadEvidenceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void uploadEvidence(Map<String, Object> fields, final GreenEvidence evidence, final List<GreenEvidenceMedia> picGreenMedias) {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .uploadEvidence(fields)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, ObservableSource<GreenEvidenceMedia>>() {
                    @Override
                    public Observable<GreenEvidenceMedia> apply(ResponseBody responseBody) throws Exception {
                        //上传文件
                        String result = responseBody.string();
                        Log.i("Oking", "证据日志上传成功" + result);
                        uploadPicCount = 0;

                        return Observable.fromIterable(picGreenMedias);

                    }
                }).concatMap(new Function<GreenEvidenceMedia, ObservableSource<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> apply(GreenEvidenceMedia media) throws Exception {
                uploadPicCount++;
                Map<String, RequestBody> picParams = new HashMap<>();
                picParams.put("zjid", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), evidence.getZJID()));
                picParams.put("type", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), "jpg"));
                picParams.put("ajid", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), evidence.getAJID()));
                picParams.put("userid", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), OkingContract.CURRENTUSER.getUserid()));
                File picFile = new File(Uri.parse(media.getPath()).getPath());
                picParams.put("files" + "\"; filename=\"" + picFile.getName(), RequestBody.create(MediaType.parse("image/png"), picFile));
                return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                        .uploadEvidenceFiles(picParams);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .retry(5, new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Exception {
                        //最多让被观察者重新发射数据5次，但是这里返回值可以进行处理
                        //返回假就是不让重新发射数据了，调用观察者的onError就终止了。
                        //返回真就是让被观察者重新发射请求
                        Log.i("Oking", "上传证据文件异常，重试");

                        return true;
                    }
                })
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();

                        if (picGreenMedias.size() == uploadPicCount) {
                            mPresenter.uploadEvidenceSucc(result);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("Oking", "上传证据文件失败" + throwable.toString());
                        mPresenter.uploadEvidenceFail(throwable);

                    }
                });

    }
}
