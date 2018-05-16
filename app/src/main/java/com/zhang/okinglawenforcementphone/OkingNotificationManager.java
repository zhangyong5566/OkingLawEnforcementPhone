package com.zhang.okinglawenforcementphone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;

/**
 * Created by Tian on 17/10/21.
 */

public class OkingNotificationManager {
    private static OkingNotificationManager mOkingNotificationManager;
    private NotificationManager mNewTaskNotificationManager;
    private NotificationManager mOtherNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private static final String NEWTASKNOTIFICATION = "1";
    private static final String OTHERNOTIFICATION = "2";

    private OkingNotificationManager() {
    }

    public static final OkingNotificationManager getInstence() {
        if (mOkingNotificationManager == null) {
            synchronized (OkingNotificationManager.class) {
                if (mOkingNotificationManager == null) {
                    mOkingNotificationManager = new OkingNotificationManager();
                }
            }
        }

        return mOkingNotificationManager;
    }

    public void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //新任务通知
            mNewTaskNotificationManager = (NotificationManager) BaseApplication.getApplictaion().getSystemService(Context.NOTIFICATION_SERVICE);
            mNewTaskNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup("newTask", "newTask"));
            NotificationChannel channel = new NotificationChannel(NEWTASKNOTIFICATION,
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.setShowBadge(true);
            mNewTaskNotificationManager.createNotificationChannel(channel);

            //其他类型通知
            mOtherNotificationManager = (NotificationManager) BaseApplication.getApplictaion().getSystemService(Context.NOTIFICATION_SERVICE);
            mOtherNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup("other", "other"));
            NotificationChannel channe2 = new NotificationChannel(OTHERNOTIFICATION,
                    "Channel2", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.setShowBadge(true);
            mOtherNotificationManager.createNotificationChannel(channel);

        } else {
            if (mBuilder == null) {
                mBuilder = new NotificationCompat.Builder(BaseApplication.getApplictaion());
            }

        }


    }


    public void showTaskNotification(GreenMissionTask greenMissionTask, PendingIntent pendingIntent) {
        String cont = "";
        switch (greenMissionTask.getStatus()) {
            case "1"://已发布
                cont = "有任务待审核";
                break;
            case "2"://已审核
                cont = "有任务待安排人员";
                break;
            case "3"://已确认
                cont = "有任务待执行";
                break;
            case "9"://退回修改
                cont = "有任务日志退回修改";
                break;
            case "100"://巡查结束
                cont = "有任务巡查结束，待上传";
                break;
            default:
                break;
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            Notification.Builder builder = new Notification.Builder(BaseApplication.getApplictaion(), "1");
            builder.setSmallIcon(R.mipmap.ic_launcher_logo)
                    .setContentText(cont)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("新消息:任务" + greenMissionTask.getTask_name());


            mNewTaskNotificationManager.notify(1000, builder.build());

//            Notification.Builder builder = new Notification.Builder(BaseApplication.getApplictaion(), "2");
//            Notification notification =
//                    builder.setContentTitle("新消息:任务" + greenMissionTask.getTask_name())
//                            .setContentText(cont)
//                            .setWhen(System.currentTimeMillis())
//                            .setSmallIcon(R.mipmap.ic_launcher_logo)
//                            .setLargeIcon(BitmapFactory.decodeResource(BaseApplication.getApplictaion().getResources(), R.mipmap.ic_launcher))
//                            .setAutoCancel(true)
//                            .setContentIntent(null)
//                            .build();
        } else {

            mBuilder.setContentTitle("新消息:任务" + greenMissionTask.getTask_name()).
                    setContentText(cont).
                    setSmallIcon(R.mipmap.ic_launcher_logo);

            Notification notification = mBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(BaseApplication.getApplictaion());
            notificationManagerCompat.notify(10000, notification);
        }


    }

    public void showGPSNotification(Context context, int satellites, String time, RemoteViews remoteViews) {
        int notificationId = 0x1235;
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, "2");
            builder.setContentTitle("广东水政定位:").
                    setDefaults(NotificationCompat.FLAG_NO_CLEAR).
                    setContentText("当前在用卫星数：" + satellites).
                    setSmallIcon(R.mipmap.ic_launcher_logo);
            builder.setSubText("最新定位时间：" + time);
            builder.setCustomBigContentView(remoteViews);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId, builder.build());
        }


    }

}