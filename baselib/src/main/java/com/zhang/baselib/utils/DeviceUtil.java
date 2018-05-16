package com.zhang.baselib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2018/4/18.
 */

public class DeviceUtil {

    /**
     * 获取App版本名称
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }


    /**
     * IMEI （唯一标识序列号）
     * <p>需与{@link #isPhone(Context)}一起使用</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     *
     * @param context 上下文
     * @return IMEI
     */
    public static String getIMEI(Context context) {
        String deviceId;
        if (isPhone(context)) {
            deviceId = getDeviceIdIMEI(context);
        } else {
            deviceId = getAndroidId(context);
        }
        return deviceId;
    }

    /**
     * 判断设备是否是手机
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPhone(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取设备的IMEI
     *
     * @param context
     * @return
     */
    public static String getDeviceIdIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }


    /**
     * 获取ANDROID ID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
