package com.zhang.baselib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.zhang.okinglawenforcementphone.SendEmailManager;
import com.zhang.okinglawenforcementphone.beans.OkingContract;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/5/23/023.
 */

public class CrashUtil implements Thread.UncaughtExceptionHandler {

    private volatile static CrashUtil mInstance;

    private Thread.UncaughtExceptionHandler mHandler;
    private boolean                  mInitialized;
    private String                   crashDir;
    private String                   versionName;
    private int                      versionCode;

    private Context context;
    private CrashUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取单例
     * <p>在Application中初始化{@code RxCrashUtils.getInstance().init(this);}</p>
     *
     * @return 单例
     */
    public static CrashUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CrashUtil.class) {
                if (mInstance == null) {
                    mInstance = new CrashUtil(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean init() {
        if (mInitialized) return true;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            crashDir = context.getExternalCacheDir().getPath() + File.separator + "crash" + File.separator;
        } else {
            crashDir = context.getCacheDir().getPath() + File.separator + "crash" + File.separator;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        String now =  OkingContract.SDF.format(System.currentTimeMillis());
        final String fullPath = crashDir + now + ".txt";
        if (!FileUtil.createOrExistsFile(fullPath)) return;
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(fullPath, false));
                    pw.write(getCrashHead());
                    throwable.printStackTrace(pw);
                    Throwable cause = throwable.getCause();
                    while (cause != null) {
                        cause.printStackTrace(pw);
                        cause = cause.getCause();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    FileUtil.closeIO(pw);
                }

            }
        });
        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        return "\n************* Crash Log Head ****************" +
                "\n设备厂商       : " + Build.MANUFACTURER +// 设备厂商
                "\n设备型号       : " + Build.MODEL +// 设备型号
                "\n系统版本       : " + Build.VERSION.RELEASE +// 系统版本
                "\nSDK版本        : " + Build.VERSION.SDK_INT +// SDK版本
                "\n程序版本名称    : " + versionName +
                "\n程序版本号      : " + versionCode +
                "\n************* Crash Log Head ****************\n\n";
    }
}
