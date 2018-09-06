package com.zhang.okinglawenforcementphone;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.DefaultContants;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.beans.JPushMessageBean;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.jpush.clien.JPushAPI;
import com.zhang.okinglawenforcementphone.jpush.clien.JPushClienService;
import com.zhang.okinglawenforcementphone.jpush.clien.JPushHttpFactory;
import com.zhang.okinglawenforcementphone.mvp.contract.JPushMessageContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.JPushMessagePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/8/23/023.
 */

public class OkingJPushManager {
    private static OkingJPushManager mJPushManager;

    private OkingJPushManager() {
    }

    public static final OkingJPushManager getInstence() {
        if (mJPushManager == null) {
            synchronized (OkingJPushManager.class) {
                if (mJPushManager == null) {
                    mJPushManager = new OkingJPushManager();
                }
            }
        }

        return mJPushManager;
    }

    public void init() {
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(BaseApplication.getApplictaion());            // 初始化 JPush

        Log.i("Oking5", JPushInterface.getRegistrationID(BaseApplication.getApplictaion()));
        if (JPushInterface.getRegistrationID(BaseApplication.getApplictaion()) != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\n" +
                    "                \"alias\": \"" + OkingContract.CURRENTUSER.getUserid() + "\"," +
                    "                                    \"tags\": {}\n" +
                    "                            }");
            JPushHttpFactory.getInstence()
                    .createService(JPushClienService.class, JPushAPI.BASE_URL)
                    .setAlias(JPushInterface.getRegistrationID(BaseApplication.getApplictaion()), requestBody)
                    .compose(RxSchedulersHelper.<ResponseBody>io_main())
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(ResponseBody responseBody) throws Exception {
                            Log.i("Oking5", "设置别名成功:" + responseBody.string());
                            RxToast.success("设置别名成功");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.i("Oking5", "设置别名失败:" + throwable.toString());
                            RxToast.error("设置别名失败"+throwable.toString());
                        }
                    });


//            JPushHttpFactory.getInstence().createService(JPushClienService.class, JPushAPI.BASE_URL)
//                    .findAlias(JPushInterface.getRegistrationID(BaseApplication.getApplictaion()))
//                    .compose(RxSchedulersHelper.<ResponseBody>io_main())
//                    .observeOn(Schedulers.io())
//                    .concatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
//                        @Override
//                        public ObservableSource<ResponseBody> apply(ResponseBody responseBody) throws Exception {
//                            Log.i("Oking5", "查询成功" + responseBody.string());
//                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\n" +
//                                    "                \"alias\": \"" + OkingContract.CURRENTUSER.getUserid() + "\"," +
//                                    "                                    \"tags\": {}\n" +
//                                    "                            }");
//                            return JPushHttpFactory.getInstence()
//                                    .createService(JPushClienService.class, JPushAPI.BASE_URL)
//                                    .setAlias(JPushInterface.getRegistrationID(BaseApplication.getApplictaion()), requestBody);
//                        }
//                    })
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<ResponseBody>() {
//                        @Override
//                        public void accept(ResponseBody responseBody) throws Exception {
//                            Log.i("Oking5", "设置别名成功:" + responseBody.string());
//                            JPushMessageBean jPushMessageBean = new JPushMessageBean();
//                            JPushMessageBean.AudienceBean audienceBean = new JPushMessageBean.AudienceBean();
//                            ArrayList<String> alias = new ArrayList<>();
//                            alias.add(OkingContract.CURRENTUSER.getUserid());
//                            audienceBean.setAlias(alias);
//                            jPushMessageBean.setAudience(audienceBean);
//                            JPushMessageBean.NotificationBean notificationBean = new JPushMessageBean.NotificationBean();
//                            notificationBean.setAlert("新任务！！！！！！");
//                            ArrayList<String> platforms = new ArrayList<>();
//                            platforms.add("android");
//                            jPushMessageBean.setPlatform(platforms);
//                            jPushMessageBean.setNotification(notificationBean);
////            String rote = "{\n" +
////                    "    \"audience\": \"all\",\n" +
////                    "    \"notification\": {\n" +
////                    "        \"alert\": \"\",\n" +
////                    "        \"android\": {\n" +
////                    "            \"extras\": {\n" +
////                    "                \"takid\": \""+OkingContract.CURRENTUSER.getUserid()+"\"," +
////                    "                \"data\": \""+OkingContract.CURRENTUSER.getDeptname()+"\"" +
////                    "            }\n" +
////                    "        }\n" +
////                    "    },\n" +
////                    "    \"platform\": [\n" +
////                    "        \"android\"\n" +
////                    "    ]\n" +
////                    "}";
//                            String s = new Gson().toJson(jPushMessageBean);
//                            Log.i("Oking5", s);
//                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), s);
//
//                            JPushHttpFactory.getInstence().createService(JPushClienService.class, JPushAPI.BASE_URL)
//                                    .pushMessage(requestBody)
//                                    .compose(RxSchedulersHelper.<ResponseBody>io_main())
//                                    .subscribe(new Consumer<ResponseBody>() {
//                                        @Override
//                                        public void accept(ResponseBody responseBody) throws Exception {
//                                            Log.i("Oking5", responseBody.string());
//                                        }
//                                    }, new Consumer<Throwable>() {
//                                        @Override
//                                        public void accept(Throwable throwable) throws Exception {
//                                            Log.i("Oking5", "失败" + throwable.toString());
//                                        }
//                                    });
//
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            Log.i("Oking5", "设置别名失败" + throwable.toString());
//                        }
//                    });


        }

    }

    public void stopPush() {
        JPushInterface.stopPush(BaseApplication.getApplictaion());
    }

    public void resumePush() {
        JPushInterface.resumePush(BaseApplication.getApplictaion());
    }

    public void pushMessage(JPushMessageBean jPushMessageBean, JPushMessageContract.View view) {

//            String rote = "{\n" +
//                    "    \"audience\": \"all\",\n" +
//                    "    \"notification\": {\n" +
//                    "        \"alert\": \"\",\n" +
//                    "        \"android\": {\n" +
//                    "            \"extras\": {\n" +
//                    "                \"takid\": \""+OkingContract.CURRENTUSER.getUserid()+"\"," +
//                    "                \"data\": \""+OkingContract.CURRENTUSER.getDeptname()+"\"" +
//                    "            }\n" +
//                    "        }\n" +
//                    "    },\n" +
//                    "    \"platform\": [\n" +
//                    "        \"android\"\n" +
//                    "    ]\n" +
//                    "}";

        JPushMessagePresenter jPushMessagePresenter = new JPushMessagePresenter(view);
        jPushMessagePresenter.pushMessage(jPushMessageBean);

    }

}
