package com.zhang.okinglawenforcementphone.mvp.model;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.beans.GreenLocation;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.Point;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadJobLogForPicContract;

import org.json.JSONObject;

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
 * Created by Administrator on 2018/4/26/026.
 */

public class UploadJobLogForPicModel implements UploadJobLogForPicContract.Model {
    private UploadJobLogForPicContract.Presenter mPresenter;
    private String mLogResult;
    private int pos = 0;
    private String mLastPathSegment;

    public UploadJobLogForPicModel(UploadJobLogForPicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void uploadJobLogForPic(final Gson gson, final Map<String, RequestBody> photoParams, final GreenMissionLog mLog, final List<GreenMedia> media) {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .getMissionRecordPicPath(mLog.getServer_id(), 0)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, Observable<GreenMedia>>() {

                    @Override
                    public Observable<GreenMedia> apply(ResponseBody responseBody) throws Exception {
                        mLogResult = responseBody.string();

                        Log.i("Oking", "已存在日志图片集合:" + mLogResult);
                        pos = 0;
                        return Observable.fromIterable(media);
                    }
                })
                .concatMap(new Function<GreenMedia, ObservableSource<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> apply(GreenMedia media) throws Exception {
                        mLastPathSegment = Uri.parse(media.getPath()).getLastPathSegment();
                        if (mLogResult.contains(mLastPathSegment)) {
                            //已经存在服务器
                            Log.i("Oking", "日志图片已存在服务器" + mLastPathSegment);
                            pos++;
                            mPresenter.uploadIsCount(pos);
                        } else {

                            //上传图片
                            photoParams.clear();
                            File file = new File(Uri.parse(media.getPath()).getPath());

                            photoParams.put("logId", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), mLog.getServer_id()));
                            photoParams.put("type", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), "0"));
                            photoParams.put("smallImg", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), ""));
                            String ext;
                            GreenLocation mediaLocation = media.getLocation();
                            if (mediaLocation != null) {
                                Point location = new Point();
                                location.setLongitude(Double.parseDouble(mediaLocation.getLongitude()));
                                location.setLatitude(Double.parseDouble(mediaLocation.getLatitude()));
                                location.setDatetime(media.getTime());
                                ext = gson.toJson(location);
                            } else {
                                Map<String, String> map = new HashMap<>();
                                map.put("datetime", OkingContract.SDF.format(media.getTime()));
                                ext = gson.toJson(map);
                            }
                            Log.i("Oking5",ext);
                            photoParams.put("ext", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), ext));

                            photoParams.put("files" + "\"; filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
                            return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                                    .uploadFiles(photoParams);
                        }
                        return Observable.empty();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .retry(5, new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Exception {
                        //最多让被观察者重新发射数据5次，但是这里返回值可以进行处理
                        //返回假就是不让重新发射数据了，调用观察者的onError就终止了。
                        //返回真就是让被观察者重新发射请求

                        mPresenter.uploadRetry(throwable);
                        Log.i("Oking", "日志图片上传异常，重试");

                        return true;
                    }
                })
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        JSONObject jsonObject = new JSONObject(result);
                        String path = jsonObject.getString("path");
                        mLogResult = mLogResult + "," + path;
                        Log.i("Oking", "日志图片上传成功" + result);
                        mPresenter.uploadSucc(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.uploadFail(throwable);
                        Log.i("Oking", "日志图片上传失败" + throwable.toString());
                    }
                });

    }
}
