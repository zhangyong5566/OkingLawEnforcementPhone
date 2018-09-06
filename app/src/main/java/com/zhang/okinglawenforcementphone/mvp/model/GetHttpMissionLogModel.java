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
import com.zhang.okinglawenforcementphone.beans.GreenMissionLogDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
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
    private GreenMissionLog mUnique;

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
                        Log.i("Oking5", "获取任务到任务" + result);
                        JSONObject object = new JSONObject(result);
                        int count = object.optInt("total");
                        if (count > 0) {
                            JSONObject rows = object.getJSONArray("rows").getJSONObject(0);
                            String mId = rows.optString("id");
                            mUnique = GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().queryBuilder()
                                    .where(GreenMissionLogDao.Properties.Server_id.eq(mId)).unique();
                            if (mUnique == null) {
                                mUnique = new GreenMissionLog();
                                mUnique.setAddr(rows.optString("addr"));
                                mUnique.setArea(rows.optString("area"));
                                mUnique.setDeal(rows.optString("deal"));
                                mUnique.setDzyj(rows.optString("dzyj"));
                                mUnique.setEquipment(rows.optString("equipment"));
                                mUnique.setServer_id(mId);
                                mUnique.setId_card(rows.optString("id_card"));
                                mUnique.setItem(rows.optInt("item"));
                                mUnique.setName(rows.optString("name"));
                                mUnique.setOther_part(rows.optString("other_part"));
                                mUnique.setOther_person(rows.optInt("other_person"));
                                mUnique.setPatrol(rows.optString("patrol"));
                                mUnique.setPlan(rows.optInt("plan"));
                                mUnique.setPost(rows.optString("post"));
                                mUnique.setResult(rows.optString("result"));
                                mUnique.setRoute(rows.optString("route"));
                                mUnique.setStatus(rows.optInt("status"));
                                mUnique.setTask_id(rows.optString("task_id"));
                                mUnique.setTime(rows.optString("time"));
                                mUnique.setType(rows.optInt("type"));
                                mUnique.setWeather(rows.optString("weather"));
                                GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().insert(mUnique);
                            } else {
                                mUnique.setAddr(rows.optString("addr"));
                                mUnique.setArea(rows.optString("area"));
                                mUnique.setDeal(rows.optString("deal"));
                                mUnique.setDzyj(rows.optString("dzyj"));
                                mUnique.setEquipment(rows.optString("equipment"));
                                mUnique.setServer_id(mId);
                                mUnique.setId_card(rows.optString("id_card"));
                                mUnique.setItem(rows.optInt("item"));
                                mUnique.setName(rows.optString("name"));
                                mUnique.setOther_part(rows.optString("other_part"));
                                mUnique.setOther_person(rows.optInt("other_person"));
                                mUnique.setPatrol(rows.optString("patrol"));
                                mUnique.setPlan(rows.optInt("plan"));
                                mUnique.setPost(rows.optString("post"));
                                mUnique.setResult(rows.optString("result"));
                                mUnique.setRoute(rows.optString("route"));
                                mUnique.setStatus(rows.optInt("status"));
                                mUnique.setTask_id(rows.optString("task_id"));
                                mUnique.setTime(rows.optString("time"));
                                mUnique.setType(rows.optInt("type"));
                                mUnique.setWeather(rows.optString("weather"));
                                GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().update(mUnique);
                            }

                            //获取日志图片路径
                            return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL).getMissionRecordPicPath(mId, 0);

                        } else {
                            return Observable.error(new Throwable("NOTASKLOG"));
                        }
                    }
                }).concatMap(new Function<ResponseBody, Observable<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                if (mUnique.getGreenMedia().size() < 1) {
                    String result = responseBody.string();
                    Log.i("Oking", "获取任务到任务图片路径：" + result);
                    JSONArray paths = new JSONArray(result);
                    for (int i = 0; i < paths.length(); i++) {
                        String path = paths.getJSONObject(i).optString("path");
                        GreenMedia unique = GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao()
                                .queryBuilder().where(GreenMediaDao.Properties.Path.eq(Api.BASE_URL + path)).unique();
                        if (unique == null) {
                            GreenMedia greenMedia = new GreenMedia();
                            greenMedia.setType(1);
                            greenMedia.setPath(Api.BASE_URL + path);
                            greenMedia.setGreenMissionLogId(mUnique.getId());
                            GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia);
                        }

                    }
                }


                //获取签名图片
                return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL).getMissionRecordPicPath(mUnique.getServer_id(), 1);
            }
        }).concatMap(new Function<ResponseBody, Observable<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {

                String result = responseBody.string();
                Log.i("Oking", "获取到签名路径：" + result);
                JSONArray paths = new JSONArray(result);
                for (int i = 0; i < paths.length(); i++) {


                    String userid = paths.getJSONObject(i).optString("userid");
                    for (int j = 0; j < mission.getMembers().size(); j++) {
                        GreenMember member = mission.getMembers().get(j);
                        if (member!=null&&userid.equals(member.getUserid())&& member.getSignPic() == null) {
                            String path = paths.getJSONObject(i).optString("path");
                            GreenMedia greenMedia = new GreenMedia();
                            greenMedia.setType(4);
                            greenMedia.setPath(path);
                            greenMedia.setUserid(member.getUserid());
                            GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia);
                        }
                    }
                }
                //获取巡查视频
                return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL).getMissionRecordPicPath(mUnique.getServer_id(), 2);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        Log.i("Oking", "获取任务日志成功");
                        mPresenter.loadHttpMissionLogSucc(mUnique);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        GreenMissionLog unique = GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().queryBuilder()
                                .where(GreenMissionLogDao.Properties.Task_id.eq(mission.getTaskid())).unique();
                        if (unique == null) {
                            mUnique = new GreenMissionLog();
                            mUnique.setTask_id(mission.getTaskid());
                            mUnique.setName(OkingContract.CURRENTUSER.getUserid());
                            mUnique.setStatus(0);
                            GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().insert(mUnique);
                            mPresenter.loadEmpty(mUnique);
                        } else {
                            mPresenter.loadEmpty(unique);
                        }
                    }
                });

    }
}
