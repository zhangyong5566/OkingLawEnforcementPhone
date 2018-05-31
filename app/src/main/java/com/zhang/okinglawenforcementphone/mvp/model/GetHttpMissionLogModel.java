package com.zhang.okinglawenforcementphone.mvp.model;

import android.os.Environment;
import android.util.Log;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMediaDao;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.htttp.Api;
import com.zhang.okinglawenforcementphone.htttp.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.GetHttpMissionLogContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/23/023.
 */

public class GetHttpMissionLogModel implements GetHttpMissionLogContract.Model {
    private GetHttpMissionLogContract.Presenter mPresenter;
    private GreenMissionLog mGreenMissionLog;
    public GetHttpMissionLogModel(GetHttpMissionLogContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getHttpMissionLog(final GreenMissionTask mission) {
        BaseHttpFactory.getInstence()
                .createService(GDWaterService.class, Api.BASE_URL)
                .getHttpMissionLog("-1", mission.getTaskid())
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, Observable<ResponseBody>>() {


                    @Override
                    public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        Log.i("Oking", "获取任务到任务"+result);
                        JSONObject object = new JSONObject(result);
                        int count = object.optInt("total");
                        if (count > 0) {
                            JSONObject rows = object.getJSONArray("rows").getJSONObject(0);
                            String mId = rows.optString("id");
                            mGreenMissionLog = new GreenMissionLog();
                            mGreenMissionLog.setAddr(rows.optString("addr"));
                            mGreenMissionLog.setArea(rows.optString("area"));
                            mGreenMissionLog.setDeal(rows.optString("deal"));
                            mGreenMissionLog.setDzyj(rows.optString("dzyj"));
                            mGreenMissionLog.setEquipment(rows.optString("equipment"));
                            mGreenMissionLog.setServer_id(mId);
                            mGreenMissionLog.setId_card(rows.optString("id_card"));
                            mGreenMissionLog.setItem(rows.optInt("item"));
                            mGreenMissionLog.setName(rows.optString("name"));
                            mGreenMissionLog.setOther_part(rows.optString("other_part"));
                            mGreenMissionLog.setOther_person(rows.optInt("other_person"));
                            mGreenMissionLog.setPatrol(rows.optString("patrol"));
                            mGreenMissionLog.setPlan(rows.optInt("plan"));
                            mGreenMissionLog.setPost(rows.optString("post"));
                            mGreenMissionLog.setResult(rows.optString("result"));
                            mGreenMissionLog.setRoute(rows.optString("route"));
                            mGreenMissionLog.setStatus(rows.optInt("status"));
                            mGreenMissionLog.setTask_id(rows.optString("task_id"));
                            mGreenMissionLog.setTime(rows.optString("time"));
                            mGreenMissionLog.setType(rows.optInt("type"));
                            mGreenMissionLog.setWeather(rows.optString("weather"));
                            GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().insert(mGreenMissionLog);
                            //获取日志图片路径
                            return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL).getMissionRecordPicPath(mId, 0);

                        } else {
                            return Observable.error(new Throwable("NOTASKLOG"));
                        }
                    }
                }).concatMap(new Function<ResponseBody, Observable<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                String result = responseBody.string();
                Log.i("Oking", "获取任务到任务图片路径：" + result);
                JSONArray paths = new JSONArray(result);
                for (int i = 0;i<paths.length();i++){
                    String path = paths.getJSONObject(i).optString("path");
                    GreenMedia unique = GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().queryBuilder().where(GreenMediaDao.Properties.Path.eq(path)).unique();
                    if(unique==null){
                        GreenMedia greenMedia = new GreenMedia();
                        greenMedia.setType(1);
                        greenMedia.setPath(Api.BASE_URL+path);
                        greenMedia.setGreenMissionLogId(mGreenMissionLog.getId());
                        GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia);
                    }

                }

                //获取签名图片
                return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL).getMissionRecordPicPath(mGreenMissionLog.getServer_id(), 1);
            }
        }).concatMap(new Function<ResponseBody, Observable<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {

                String result = responseBody.string();
                Log.i("Oking", "获取到签名路径：" + result);
                JSONArray paths = new JSONArray(result);
                for (int i = 0; i < paths.length(); i++) {


                    String url = paths.getJSONObject(i).optString("userid");
                    for (int j = 0; j < mission.getMembers().size(); j++){
                        GreenMember member = mission.getMembers().get(j);
                        if (member.getUserid().equals(url)) {
                            String path = paths.getJSONObject(i).optString("path");
                            GreenMedia greenMedia = new GreenMedia();
                            greenMedia.setType(4);
                            greenMedia.setPath(path);
                            greenMedia.setUserid(member.getUserid());
                        }
                    }
                }
                //获取巡查视频
                return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL).getMissionRecordPicPath(mGreenMissionLog.getServer_id(), 2);
            }
        }).doOnNext(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                String result = responseBody.string();
                Log.i("Oking", "巡查视频：" + result);
                JSONArray paths = new JSONArray(result);
                for (int i = 0; i < paths.length(); i++) {
                    String[] p = paths.getJSONObject(i).optString("path").split("/");
                    String fileName = p[p.length - 1];

                    String path = paths.getJSONObject(i).optString("path");
                    File file = new File(Environment.getExternalStorageDirectory()+"/oking/mission_video", fileName);
                    //开始下载视频
                    if (!file.exists()) {
                        BaseHttpFactory.getInstence()
                                .createService(GDWaterService.class, Api.BASE_URL).downloadFile(path).subscribe(new Consumer<ResponseBody>() {
                            @Override
                            public void accept(ResponseBody responseBody) throws Exception {
                                Log.i("Oking", "视频下载成功");
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.i("Oking", "视频下载异常");
                            }
                        });
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        Log.i("Oking", "获取任务日志成功");
                        mPresenter.loadHttpMissionLogSucc(mGreenMissionLog);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mPresenter.loadHttpMissionLogFail(throwable);
                    }
                });

    }
}
