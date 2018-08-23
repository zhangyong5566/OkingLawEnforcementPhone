package com.zhang.okinglawenforcementphone.mvp.model;

import android.util.Log;

import com.google.gson.Gson;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMemberDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.Mission;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
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
    private Gson mGson = new Gson();
    private ArrayList<GreenMissionTask> mGreenMissionTasks = new ArrayList<>();
    private GreenMissionTask mUnique;
    private List<Mission.RecordsBean> mRecords;

    public LoadHttpMissionModel(LoadHttpMissionContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void loadHttpMission(int classify, final String receiver) {
        BaseHttpFactory.getInstence()
                .createService(GDWaterService.class, Api.BASE_URL)
                .loadHttpMission(receiver, classify, "-1")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, Observable<Mission.RecordsBean>>() {
                    @Override
                    public Observable<Mission.RecordsBean> apply(ResponseBody responseBody) throws Exception {
                        mGreenMissionTasks .clear();
                        mPosition = 0;
                        final String result = responseBody.string();
                        Log.i("Oking1",result);
                        Mission mission = mGson.fromJson(result, Mission.class);
                        mRecords = mission.getRecords();
                        if (mRecords !=null&& mRecords.size()>0){
                            return Observable.fromIterable(mRecords);
                        }else {
                            return Observable.error(new Throwable("NONE"));
                        }

                    }
                }).concatMap(new Function<Mission.RecordsBean, ObservableSource<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> apply(Mission.RecordsBean mission) throws Exception {

                mUnique = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder().where(GreenMissionTaskDao.Properties.Taskid.eq(mission.getID())).unique();
                if (mUnique != null) {
                    mUnique.setApproved_person(mission.getAPPROVED_PERSON());
                    mUnique.setApproved_person_name(mission.getAPPROVED_PERSON_NAME());
                    mUnique.setApproved_time(mission.getAPPROVED_TIME());
                    mUnique.setBegin_time(mission.getBEGIN_TIME());
                    mUnique.setStatus(mission.getSTATUS());
                    mUnique.setCreate_time(mission.getCREATE_TIME());
                    mUnique.setDelivery_time(mission.getDELIVERY_TIME());
                    mUnique.setEnd_time(mission.getEND_TIME());
                    mUnique.setExecute_end_time(mission.getEXECUTE_END_TIME());
                    mUnique.setExecute_start_time(mission.getEXECUTE_START_TIME());
                    mUnique.setFbdw(mission.getFBDW());
                    mUnique.setFbr(mission.getFBR());
                    mUnique.setExamine_status(mission.getEXAMINE_STATUS());
                    mUnique.setJjcd(mission.getJJCD());
                    mUnique.setJsdw(mission.getJSDW());
                    mUnique.setJsr(mission.getJSR());
                    mUnique.setPublisher(mission.getPUBLISHER());
                    mUnique.setPublisher_name(mission.getPUBLISHER_NAME());
                    mUnique.setReceiver(mission.getRECEIVER());
                    mUnique.setReceiver_name(mission.getRECEIVER_NAME());
                    mUnique.setRwly(mission.getRWLY());
                    mUnique.setRwqyms(mission.getRWQYMS());
                    mUnique.setTask_name(mission.getTASK_NAME());
                    mUnique.setSpr(mission.getSPR());
                    mUnique.setSpyj(mission.getSPYJ());
                    mUnique.setTask_area(mission.getRWQYMS());
                    mUnique.setTask_content(mission.getRWMS());
                    mUnique.setTypename(mission.getTYPENAME());
                    mUnique.setTypeoftask(mission.getTYPEOFTASK());
                    mUnique.setTaskid(mission.getID());
                    mUnique.setUserid(OkingContract.CURRENTUSER.getUserid());


                } else {
                    mUnique = new GreenMissionTask();
                    mUnique.setApproved_person(mission.getAPPROVED_PERSON());
                    mUnique.setApproved_person_name(mission.getAPPROVED_PERSON_NAME());
                    mUnique.setApproved_time(mission.getAPPROVED_TIME());
                    mUnique.setBegin_time(mission.getBEGIN_TIME());
                    mUnique.setCreate_time(mission.getCREATE_TIME());
                    mUnique.setDelivery_time(mission.getDELIVERY_TIME());
                    mUnique.setEnd_time(mission.getEND_TIME());
                    mUnique.setExecute_end_time(mission.getEXECUTE_END_TIME());
                    mUnique.setExecute_start_time(mission.getEXECUTE_START_TIME());
                    mUnique.setFbdw(mission.getFBDW());
                    mUnique.setFbr(mission.getFBR());
                    mUnique.setJjcd(mission.getJJCD());
                    mUnique.setJsdw(mission.getJSDW());
                    mUnique.setJsr(mission.getJSR());
                    mUnique.setPublisher(mission.getPUBLISHER());
                    mUnique.setPublisher_name(mission.getPUBLISHER_NAME());
                    mUnique.setReceiver(mission.getRECEIVER());
                    mUnique.setReceiver_name(mission.getRECEIVER_NAME());
                    mUnique.setRwly(mission.getRWLY());
                    mUnique.setRwqyms(mission.getRWQYMS());
                    mUnique.setExamine_status(mission.getEXAMINE_STATUS());
                    mUnique.setSpr(mission.getSPR());
                    mUnique.setSpyj(mission.getSPYJ());
                    mUnique.setStatus(mission.getSTATUS());
                    mUnique.setTask_area(mission.getRWQYMS());
                    mUnique.setTask_name(mission.getTASK_NAME());
                    mUnique.setTask_content(mission.getRWMS());
                    mUnique.setTypename(mission.getTYPENAME());
                    mUnique.setTypeoftask(mission.getTYPEOFTASK());
                    mUnique.setTaskid(mission.getID());
                    mUnique.setUserid(OkingContract.CURRENTUSER.getUserid());
                    long id = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().insert(mUnique);

                    GreenMember leader = new GreenMember();
                    leader.setUsername(mission.getRECEIVER_NAME());
                    leader.setUserid(mission.getRECEIVER());
                    leader.setTaskid(mission.getID());
                    leader.setGreenMemberId(id);
                    leader.setPost("任务负责人");
                    GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().insert(leader);

                }


                return BaseHttpFactory.getInstence()
                        .createService(GDWaterService.class, Api.BASE_URL)
                        .loadMember(mission.getID());
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

                        GreenMember unique = GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().queryBuilder().where(GreenMemberDao.Properties.GreenMemberId.eq(mUnique.getId()),GreenMemberDao.Properties.Userid.eq(userid)).unique();

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
                            GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().insert(greenMember);

                        }
                    }

                }
                mPosition++;
                mPresenter.loadHttpMissionProgress(mRecords.size(), mPosition);

                GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mUnique);
                mGreenMissionTasks.add(mUnique);
                if (mPosition == mRecords.size()) {
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
                        List<GreenMissionTask> list = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid())).list();
                        mPresenter.loadMissionFromLocal(list);
                    }
                });
            }
        });

    }
}
