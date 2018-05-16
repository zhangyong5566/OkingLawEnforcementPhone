package com.zhang.baselib.utils;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2018/4/25/025.
 */

public class Util {
    public static List<Camera.Size> getResolutionList(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        return parameters.getSupportedPreviewSizes();
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;

    }

    public static class ResolutionComparator implements Comparator<Camera.Size> {

        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            if (lhs.height != rhs.height) {
                return lhs.height - rhs.height;
            } else {
                return lhs.width - rhs.width;
            }
        }

    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getDatePoor(long time1, long time2) {
        long nd = 1000 * 24 * 60 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = time2 - time1;
        // 计算差多少天
        int day = (int) (diff / nd);
        return day;

    }
}
