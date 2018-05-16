package com.zhang.okinglawenforcementphone.mvp.model;

import android.net.Uri;
import android.util.Log;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.htttp.Api;
import com.zhang.okinglawenforcementphone.htttp.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadSignaturePicContract;

import org.json.JSONObject;

import java.io.File;
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

public class UploadSignaturePicModel implements UploadSignaturePicContract.Model {
    private UploadSignaturePicContract.Presenter mPresenter;
    private String memberResult;
    private int uploadSignaturePicCount=0;
    private String mLastPathSegment;

    public UploadSignaturePicModel(UploadSignaturePicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void uploadSignaturePic(final GreenMissionLog mGreenMissionLog, final GreenMissionTask missionTask, final Map<String, RequestBody> photoParams) {
        BaseHttpFactory
                .getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .getMissionRecordPicPath(mGreenMissionLog.getServer_id(), 1)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, ObservableSource<GreenMember>>() {

                    @Override
                    public Observable<GreenMember> apply(ResponseBody responseBody) throws Exception {
                        memberResult = responseBody.string();
                        uploadSignaturePicCount=0;
                        Log.i("Oking1",memberResult+">>>>服务器返回member>>");
                        Log.i("Oking1","需要签名人员:"+missionTask.getMembers().size()+missionTask.getMembers().toString());
                        return Observable.fromIterable(missionTask.getMembers());
                    }
                })
                .concatMap(new Function<GreenMember, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(GreenMember greenMember) throws Exception {
                        mLastPathSegment = Uri.parse(greenMember.getSignPic()).getLastPathSegment();
                        Log.i("Oking1","遍历签名"+ mLastPathSegment);
                        if (memberResult.contains(mLastPathSegment)) {
                            //存在服务器
                            Log.i("Oking1",mLastPathSegment+"签名在服务器存在》》》》》》》》》"+memberResult);
                            uploadSignaturePicCount++;
                            mPresenter.uploadIsCount(uploadSignaturePicCount);
                        } else {
                            if (greenMember.getSignPic() != null) {
                                photoParams.clear();
                                File file = new File(greenMember.getSignPic());
                                photoParams.put("logId", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), mGreenMissionLog.getServer_id()));
                                photoParams.put("type", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), "1"));
                                photoParams.put("smallImg", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), ""));
                                photoParams.put("user_id", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), greenMember.getUserid()!=null?greenMember.getUserid():"880088"));
                                String fileName = file.getName();
                                photoParams.put("files" + "\"; filename=\"" + fileName, RequestBody.create(MediaType.parse("image/png"), file));

                                return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                                        .uploadFiles(photoParams);
                            }
                        }
                        return Observable.empty();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .retry(5, new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Exception {
                        //最多让被观察者重新发射数据5次，但是这里返回值可以进行处理
                        //返回假就是不让重新发射数据了，调用观察者的onError就终止了。
                        //返回真就是让被观察者重新发射请求
                        Log.i("Oking1","签名图片上传异常，重试");

                        mPresenter.uploadRetry(throwable);
                        return true;
                    }
                })
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        uploadSignaturePicCount++;
                        String result = responseBody.string();
                        JSONObject jsonObject = new JSONObject(result);
                        String path = jsonObject.getString("path");
                        memberResult = memberResult+","+path;
                        mPresenter.uploadSignaturePicSucc(result);

                        Log.i("Oking1","签名上传成功"+result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("Oking1","签名上传失败"+throwable.toString());
                        mPresenter.uploadSignatureFail(throwable);
                    }
                });

    }
}
