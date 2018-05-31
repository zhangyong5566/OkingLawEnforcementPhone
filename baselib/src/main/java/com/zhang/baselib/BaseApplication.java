package com.zhang.baselib;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by Administrator on 2018/3/14.
 */

public class BaseApplication extends Application{
    private static  BaseApplication  baseApplication;
    @Override
    public void onCreate() {
        baseApplication = this;
        ARouter.openDebug();
        ARouter.init(this);
        ARouter.openLog();

        super.onCreate();

    }

    public static Application getApplictaion() {
        return baseApplication;
    }
}
