package com.zhang.baselib.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.zhang.baselib.interfaces.OnRequestPermissionsListener;

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


    /**
     * 拨打电话
     * 需添加权限 {@code <uses-permission android:name="android.permission.CALL_PHONE"/>}
     *
     * @param context     上下文
     * @param phoneNumber 电话号码
     */
    public static void callPhone(final Context context, String phoneNumber) {
        if (!DataUtil.isNullString(phoneNumber)) {
            final String phoneNumber1 = phoneNumber.trim();// 删除字符串首部和尾部的空格
            // 调用系统的拨号服务实现电话拨打功能
            // 封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入

            PermissionUtil.requestCall(context, new OnRequestPermissionsListener() {
                @Override
                public void onRequestBefore() {
                   //清单文件没有这个权限需要在清单文件加上
                }

                @Override
                public void onRequestLater() {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber1));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(intent);// 内部类
                }
            });


        }
    }


}
