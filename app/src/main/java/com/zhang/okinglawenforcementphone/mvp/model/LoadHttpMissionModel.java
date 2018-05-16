package com.zhang.okinglawenforcementphone.mvp.model;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.utils.DataUtil;
import com.zhang.okinglawenforcementphone.GreenDAOMannager;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMemberDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.Mission;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.htttp.Api;
import com.zhang.okinglawenforcementphone.htttp.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadHttpMissionContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/20.
 */

public class LoadHttpMissionModel implements LoadHttpMissionContract.Model {
    private LoadHttpMissionContract.Presenter mPresenter;
    private int mPosition = 0;
    private ArrayList<GreenMissionTask> mGreenMissionTasks;
    private ArrayList<Mission> missions;
    private GreenMissionTask mUnique;
    public LoadHttpMissionModel(LoadHttpMissionContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void loadHttpMission(int classify, String receiver) {
        BaseHttpFactory.getInstence()
                .createService(GDWaterService.class, Api.BASE_URL)
                .loadHttpMission(receiver, classify, "-1")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, Observable<Mission>>() {
                    @Override
                    public Observable<Mission> apply(ResponseBody responseBody) throws Exception {
                        mGreenMissionTasks = new ArrayList<>();
                        mPosition = 0;
                        final String result = responseBody.string();
                        final JSONObject object = new JSONObject(result);
                        final JSONArray missionJSONArray = object.getJSONArray("rows");


                        missions = DataUtil.praseJson(missionJSONArray.toString(),
                                new TypeToken<ArrayList<Mission>>() {
                                });

                        if (missions.size()>0){
                            return Observable.fromIterable(missions);
                        }else{
                            return Observable.error(new Throwable("NONE"));
                        }

                    }
                }).concatMap(new Function<Mission, ObservableSource<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> apply(Mission mission) throws Exception {


                mUnique = GreenDAOMannager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder().where(GreenMissionTaskDao.Properties.Taskid.eq(mission.getId())).unique();
                if (mUnique != null) {
                    mUnique.setApproved_person(mission.getApproved_person());
                    mUnique.setApproved_person_name(mission.getApproved_person_name());
                    mUnique.setApproved_time(mission.getApproved_time());
                    mUnique.setBegin_time(mission.getBegin_time());
                    mUnique.setCreate_time(mission.getCreate_time());
                    mUnique.setDelivery_time(mission.getDelivery_time());
                    mUnique.setEnd_time(mission.getEnd_time());
                    mUnique.setExecute_end_time(mission.getExecute_end_time());
                    mUnique.setExecute_start_time(mission.getExecute_start_time());
                    mUnique.setFbdw(mission.getFbdw());
                    mUnique.setFbr(mission.getFbr());
                    mUnique.setJjcd(mission.getJjcd());
                    mUnique.setJsdw(mission.getJsdw());
                    mUnique.setJsr(mission.getJsr());
                    mUnique.setPublisher(mission.getPublisher());
                    mUnique.setPublisher_name(mission.getPublisher_name());
                    mUnique.setReceiver(mission.getReceiver());
                    mUnique.setReceiver_name(mission.getReceiver_name());
                    mUnique.setRwly(mission.getRwly());
                    mUnique.setRwqyms(mission.getRwqyms());
                    mUnique.setTask_name(mission.getTask_name());
                    mUnique.setSpr(mission.getSpr());
                    mUnique.setSpyj(mission.getSpyj());
                    mUnique.setTask_area(mission.getTask_content());
                    mUnique.setTask_content(mission.getTask_content());
                    mUnique.setTypename(mission.getTypename());
                    mUnique.setTypeoftask(mission.getTypeoftask());
                    mUnique.setTaskid(mission.getId());
                    mUnique.setUserid(OkingContract.CURRENTUSER.getUserid());


                } else {
                    mUnique = new GreenMissionTask();
                    mUnique.setApproved_person(mission.getApproved_person());
                    mUnique.setApproved_person_name(mission.getApproved_person_name());
                    mUnique.setApproved_time(mission.getApproved_time());
                    mUnique.setBegin_time(mission.getBegin_time());
                    mUnique.setCreate_time(mission.getCreate_time());
                    mUnique.setDelivery_time(mission.getDelivery_time());
                    mUnique.setEnd_time(mission.getEnd_time());
                    mUnique.setExecute_end_time(mission.getExecute_end_time());
                    mUnique.setExecute_start_time(mission.getExecute_start_time());
                    mUnique.setFbdw(mission.getFbdw());
                    mUnique.setFbr(mission.getFbr());
                    mUnique.setJjcd(mission.getJjcd());
                    mUnique.setJsdw(mission.getJsdw());
                    mUnique.setJsr(mission.getJsr());
                    mUnique.setPublisher(mission.getPublisher());
                    mUnique.setPublisher_name(mission.getPublisher_name());
                    mUnique.setReceiver(mission.getReceiver());
                    mUnique.setReceiver_name(mission.getReceiver_name());
                    mUnique.setRwly(mission.getRwly());
                    mUnique.setRwqyms(mission.getRwqyms());
                    mUnique.setSpr(mission.getSpr());
                    mUnique.setSpyj(mission.getSpyj());
                    mUnique.setStatus(mission.getStatus());
                    mUnique.setTask_area(mission.getTask_content());
                    mUnique.setTask_name(mission.getTask_name());
                    mUnique.setTask_content(mission.getTask_content());
                    mUnique.setTypename(mission.getTypename());
                    mUnique.setTypeoftask(mission.getTypeoftask());
                    mUnique.setTaskid(mission.getId());
                    mUnique.setUserid(OkingContract.CURRENTUSER.getUserid());
                    long id = GreenDAOMannager.getInstence().getDaoSession().getGreenMissionTaskDao().insert(mUnique);

                    GreenMember leader = new GreenMember();
                    leader.setUsername(mission.getReceiver_name());
                    leader.setUserid(mission.getReceiver());
                    leader.setTaskid(mission.getId());
                    leader.setGreenMemberId(id);
                    leader.setPost("任务负责人");
                    GreenDAOMannager.getInstence().getDaoSession().getGreenMemberDao().insert(leader);

                }


                return BaseHttpFactory.getInstence()
                        .createService(GDWaterService.class, Api.BASE_URL)
                        .loadMember(mission.getId());
            }
        }).retry(1, new Predicate<Throwable>() {
            @Override
            public boolean test(Throwable throwable) throws Exception {
                //最多让被观察者重新发射数据5次，但是这里返回值可以进行处理
                //返回假就是不让重新发射数据了，调用观察者的onError就终止了。
                //返回真就是让被观察者重新发射请求
                Log.i("Oking", "任务数据获取异常，正在重试");
                return true;
            }
        }).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                String membersJson = responseBody.string();
                JSONObject object = new JSONObject(membersJson);
                JSONArray memberJSONArray = object.getJSONArray("rows");
                if (memberJSONArray.length() > 0) {
                    for (int i = 0; i < memberJSONArray.length(); i++) {
                        JSONObject jsonObject = memberJSONArray.getJSONObject(i);
                        String userid = jsonObject.getString("userid");

                        GreenMember unique = GreenDAOMannager.getInstence().getDaoSession().getGreenMemberDao().queryBuilder().where(GreenMemberDao.Properties.GreenMemberId.eq(mUnique.getId()),GreenMemberDao.Properties.Userid.eq(userid)).unique();

                        if (unique == null) {
                            GreenMember greenMember = new GreenMember();
                            String account = jsonObject.getString("account");
                            greenMember.setAccount(account);
                            greenMember.setGreenMemberId(mUnique.getId());
                            String taskid = jsonObject.getString("taskid");
                            greenMember.setTaskid(taskid);
                            greenMember.setUserid(userid);
                            greenMember.setPost("队员");
                            String username = jsonObject.getString("username");
                            greenMember.setUsername(username);
                            GreenDAOMannager.getInstence().getDaoSession().getGreenMemberDao().insert(greenMember);

                        }
                    }

                }
                mPosition++;
                mPresenter.loadHttpMissionProgress(missions.size(), mPosition);

                GreenDAOMannager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mUnique);
                mGreenMissionTasks.add(mUnique);
                if (mPosition == missions.size()) {
                    mPresenter.loadHttpMissionSucc(mGreenMissionTasks);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

                Log.i("Oking", "22异常" + throwable.getMessage());
                if(throwable.getMessage().equals("NONE")){

                }else {

                }

                //从本地数据库获取数据
                Schedulers.io().createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        List<GreenMissionTask> list = GreenDAOMannager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid())).list();
                        Log.i("Oking", ">>>" + list.size());
                        mPresenter.loadMissionFromLocal(list);
                    }
                });
            }
        });

    }
}
