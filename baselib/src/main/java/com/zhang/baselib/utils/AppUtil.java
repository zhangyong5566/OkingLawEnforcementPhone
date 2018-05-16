package com.zhang.baselib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Administrator on 2018/4/29/029.
 */

public class AppUtil {
    /**
     * 安装APK
     *
     * @param context
     * @param APK_PATH
     */
    public static void installAPK(Context context, String APK_PATH) {//提示安装APK
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + APK_PATH), "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    /**
     * 判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    public static boolean isInstallApp(Context context, String packageName) {
        return !DataUtil.isNullString(packageName) && IntentUtil.getLaunchAppIntent(context, packageName) != null;
    }
}
