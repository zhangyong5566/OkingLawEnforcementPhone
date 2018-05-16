package com.zhang.baselib.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Administrator on 2018/4/18.
 */

public class LocationUtil {

    /**
     * 判断Gps是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
