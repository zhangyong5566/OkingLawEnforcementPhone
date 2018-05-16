package com.zhang.okinglawenforcementphone;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.navi.AMapNavi;
import com.google.gson.Gson;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.DefaultContants;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.baselib.utils.NetUtil;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadLocationToServerContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadLocationToServerPresenter;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/20.
 */

public class OkingLocationMannager {
    private static OkingLocationMannager mOkingLocationMannager;
    private Subscription mSubscription;
    private SimpleDateFormat locationFileSdf = new SimpleDateFormat("yyyyMMdd");
    private UploadLocationToServerPresenter mUploadLocationToServerPresenter;

    private OkingLocationMannager() {
    }

    public static final OkingLocationMannager getInstence() {
        if (mOkingLocationMannager == null) {
            synchronized (OkingLocationMannager.class) {
                if (mOkingLocationMannager == null) {
                    mOkingLocationMannager = new OkingLocationMannager();
                }
            }
        }

        return mOkingLocationMannager;
    }

    public void init() {
        //设置高德地图apikey
        AMapNavi.setApiKey(BaseApplication.getApplictaion(), "26c7c12d8952b9db18af6439616b25aa");
    }

    @SuppressLint("WrongConstant")
    public void startLocation(final UploadLocationToServerContract.View mView, final String imei, final Gson gson) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.zhang.okinglawenforcementphone", "com.zhang.okinglawenforcementphone.service.IRemoteLocationService"));
        BaseApplication.getApplictaion().bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                final AmapLocationAidlInterface amapLocationAidlInterface = AmapLocationAidlInterface.Stub.asInterface(iBinder);
                if (amapLocationAidlInterface != null) {
                    Flowable.interval(1, 4, TimeUnit.SECONDS)
                            .onBackpressureDrop()
                            .subscribe(new Subscriber<Long>() {
                                @Override
                                public void onSubscribe(Subscription s) {
                                    mSubscription = s;
                                    s.request(Long.MAX_VALUE);
                                }

                                @Override
                                public void onNext(Long aLong) {
                                    try {
                                        String[] location = amapLocationAidlInterface.getLocation();
                                        OkingContract.LOCATIONRESULT = location;
                                        OkingContract.MARQUEEVIEWINFO.clear();
                                        OkingContract.MARQUEEVIEWINFO.add("当前定位类型：" + OkingContract.LOCATIONRESULT[0]);
                                        OkingContract.MARQUEEVIEWINFO.add("经纬度：" + OkingContract.LOCATIONRESULT[1] + "," + OkingContract.LOCATIONRESULT[2]);
                                        OkingContract.MARQUEEVIEWINFO.add("定位时间：" + OkingContract.LOCATIONRESULT[3]);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }


                                    try {
                                        if (aLong % 5 == 0) {
                                            amapLocationAidlInterface.refreshNotification();
                                            if (NetUtil.isConnected(BaseApplication.getApplictaion())
                                                    && OkingContract.CURRENTUSER != null
                                                    && !"".equals(OkingContract.CURRENTUSER.getUserid()) &&
                                                    DefaultContants.ISHTTPLOGIN && !TextUtils.isEmpty(OkingContract.LOCATIONRESULT[1])) {

                                                if (mUploadLocationToServerPresenter == null) {
                                                    mUploadLocationToServerPresenter = new UploadLocationToServerPresenter(mView);

                                                }

                                                mUploadLocationToServerPresenter.upploadLocationToServer(Long.parseLong(OkingContract.CURRENTUSER.getLogintime()), OkingContract.SDF, imei, gson);

                                            }

                                            //把定位经纬度保存text


                                            if (!TextUtils.isEmpty(OkingContract.LOCATIONRESULT[3]) && !OkingContract.LOCATIONRESULT[0].equals("返回上次定位")) {

                                                Schedulers.io().createWorker().schedule(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        writeToLogFile();
                                                    }
                                                });
                                            }

                                        }
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onError(Throwable t) {
                                    Log.i("Oking", "定位异常" + t.getMessage());
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                if (mSubscription != null) {
                    mSubscription.cancel();
                }


            }
        }, 1);

    }

    public void cancelLocation() {
        if (mSubscription!=null){

            mSubscription.cancel();
            mSubscription=null;
        }
    }

    private void writeToLogFile() {
        String filePath = Environment.getExternalStorageDirectory() + "/oking/location/" + locationFileSdf.format(System.currentTimeMillis()) + ".txt";


        String cont = null;

        try {
            long time = OkingContract.SDF.parse(OkingContract.LOCATIONRESULT[3]).getTime();
            cont = OkingContract.LOCATIONRESULT[1] + "," + OkingContract.LOCATIONRESULT[2] + "," + time + "\n";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean flag = FileUtil.writeFileFromString(filePath, cont, true);
        if (flag) {
//            System.out.println("文件写入成功");
        } else {
//            System.out.println("文件写入失败");
        }

    }
}
