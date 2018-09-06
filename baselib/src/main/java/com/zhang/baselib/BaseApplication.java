package com.zhang.baselib;

import android.app.Application;



/**
 * Created by Administrator on 2018/3/14.
 */

public class BaseApplication extends Application{
    private static  BaseApplication  baseApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;

    }

    public static Application getApplictaion() {
        return baseApplication;
    }
}
