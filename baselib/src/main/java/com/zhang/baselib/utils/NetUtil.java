package com.zhang.baselib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2018/4/18.
 */

public class NetUtil {
    /**
     * 判断网络是否连接
     * 需添加权限
     *
     * @code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }

    /**
     * 获取活动网络信息
     *
     * @param context 上下文
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
}
