package com.zhang.baselib.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/4/29/029.
 */

public class IntentUtil {
    /**
     * 获取打开App的意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    public static Intent getLaunchAppIntent(Context context, String packageName) {
        return getIntentByPackageName(context, packageName);
    }

    /**
     * 根据包名获取意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    private static Intent getIntentByPackageName(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }
}
