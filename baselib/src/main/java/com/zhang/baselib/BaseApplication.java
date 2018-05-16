package com.zhang.baselib;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Administrator on 2018/3/14.
 */

public class BaseApplication extends Application{
    private static  BaseApplication  baseApplication;
    @Override
    public void onCreate() {
        baseApplication = this;
        Fresco.initialize(this);
        ARouter.openDebug();
        ARouter.init(this);
        ARouter.openLog();



        super.onCreate();

    }

    public static Application getApplictaion() {
        return baseApplication;
    }
}
