package com.zhang.okinglawenforcementphone.mvp.model;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.okinglawenforcementphone.beans.GreenLocation;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.Point;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadVideoContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
 * Copyright (c) 2018, smuyyh@gmail.com All Rights Reserved.
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG             #
 * #                                                   #
 */

//'执行状态 0-未发布  1-已发布 2-已经审核 3-确认接受  4-任务开始  5-任务完成 7-不通过审核 9-退回修改(日志审核)';

/**
 * Created by Administrator on 2018/3/27.
 * 上传视频
 */

public class UploadVideoModel implements UploadVideoContract.Model {
    private UploadVideoContract.Presenter mPresenter;
    private int uploadLogVideoCount=0;
    private File mBitmapFile;
    private File mVideofile;
    private String mLogResult;
    private GreenLocation mMediaLocation;
    private String msLaststr;
    private Long mTime;
    private Map<String, RequestBody> mVideoParams;

    public UploadVideoModel(UploadVideoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void uploadVideo(final GreenMissionLog greenMissionLog, final List<GreenMedia> greenMedias, final Map<String, RequestBody> photoParams, final SimpleDateFormat videosdf, final Gson gson) {
        Log.i("Oking","日志ID："+greenMissionLog.getServer_id());
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .getMissionRecordPicPath(greenMissionLog.getServer_id(), 2)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, Observable<GreenMedia>>() {
                    @Override
                    public Observable<GreenMedia> apply(ResponseBody responseBody) throws Exception {
                        mLogResult = responseBody.string();
                        uploadLogVideoCount=0;
                        Log.i("Oking", ">>>>>>>>>@@@"+mLogResult);
                        //遍历集合
                        return Observable.fromIterable(greenMedias);

                    }
                }).concatMap(new Function<GreenMedia, ObservableSource<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> apply(GreenMedia media) throws Exception {
                mMediaLocation = media.getLocation();
                mTime = media.getTime();
                msLaststr = Uri.parse(media.getPath()).getLastPathSegment();
                if (mLogResult.contains(msLaststr)){
                    //封面图片已存在服务器
                    Log.i("Oking",photoParams.toString()+">>视频封面已存在服务器>>>>");
                    uploadLogVideoCount++;
                    mPresenter.uploadIsCount(uploadLogVideoCount);
                }else {
                    photoParams.clear();
                    Log.i("Oking",photoParams.toString()+">>遍历2-2>>>>");

                    mVideofile = new File(FileUtil.praseUritoPath(BaseApplication.getApplictaion(), Uri.parse(media.getPath())));
                    photoParams.put("logId", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), greenMissionLog.getServer_id()));
                    photoParams.put("type", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), "3"));
                    photoParams.put("smallImg", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), ""));
                    String fileName = mVideofile.getName();

                    Bitmap bitmap = null;
                    bitmap = ThumbnailUtils.createVideoThumbnail(mVideofile.getPath(),
                            MediaStore.Images.Thumbnails.MICRO_KIND);
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 1000, 569, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                    mBitmapFile = new File(mVideofile.getParent(), fileName.split("\\.")[0] + ".jpg");
                    if (!mBitmapFile.exists()) {
                        try {
                            mBitmapFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.i("Oking", "当前4G网络不稳定，上传失败，请稍后重试！6" + e.getMessage());
                        }
                    }
                    try {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mBitmapFile));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                        bitmap.recycle();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    photoParams.put("files" + "\"; filename=\"" + fileName , RequestBody.create(MediaType.parse("image/png"), mBitmapFile));
                    //上传视频封面图片
                    return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                            .uploadFiles(photoParams);

                }

                return Observable.empty();
            }
        }).concatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                String result = responseBody.string();
                Log.i("Oking","上传封面成功"+result);
                mLogResult = mLogResult+","+msLaststr;
                try {
                    JSONObject object = new JSONObject(result);
                    int code = object.getInt("code");
                    if (code == 200) {
                        if (mBitmapFile.exists()) {
                            mBitmapFile.delete();
                        }

                        String path = object.getString("path");
                        mVideoParams = new HashMap<>();

                        mVideoParams.put("logId", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), greenMissionLog.getServer_id()));
                        mVideoParams.put("type", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), "2"));
                        mVideoParams.put("smallImg", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), path));
                        String fileName = mVideofile.getName().split("\\.")[0];

                        mVideoParams.put("files" + "\"; filename=\"" + fileName , RequestBody.create(MediaType.parse("video/mp4"), mVideofile));


                        String ext = null;

                            if (mMediaLocation!=null){
                                Point location = new Point();
                                location.setLongitude(Double.parseDouble(mMediaLocation.getLongitude()));
                                location.setLatitude(Double.parseDouble(mMediaLocation.getLatitude()));
                                location.setDatetime(mTime);
                                ext = gson.toJson(location);
                            }else {
                                Map<String, String> map = new HashMap<>();
                                map.put("datetime", OkingContract.SDF.format(mTime));
                                ext = gson.toJson(map);
                            }



                        if (!TextUtils.isEmpty(ext)){

                            mVideoParams.put("ext", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), ext));
                        }

                    } else {
                        Log.i("Oking", "当前4G网络不稳定，上传失败，请稍后重试！10");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Oking", "当前4G网络不稳定，上传失败，请稍后重试！11" + e.getMessage());
                }

                return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                        .uploadFiles(mVideoParams);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .retry(5, new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) throws Exception {
                        //最多让被观察者重新发射数据5次，但是这里返回值可以进行处理
                        //返回假就是不让重新发射数据了，调用观察者的onError就终止了。
                        //返回真就是让被观察者重新发射请求
                        Log.i("Oking","视频上传异常，重试");

                        mPresenter.uploadRetry(throwable);
                        return true;
                    }
                })
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result =  responseBody.string();
                        mVideoParams.clear();
                        mVideoParams=null;

                        mPresenter.loadVideoSucc(result);
                        Log.i("Oking", "上传成功");

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.loadVideoFail(throwable);
                        Log.i("Oking", "当前4G网络不稳定，上传失败，请稍后重试！13" + throwable.getMessage());
                    }
                });

    }
}
