package com.zhang.okinglawenforcementphone.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.NewsTaskOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ApprovalActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ArrangeTeamMembersActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.AuditActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MainActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MissionActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MissionRecorActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ToDoActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    private String mOpenType;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();


            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知" + bundle.getString(JPushInterface.EXTRA_EXTRA));
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                String taskid = json.optString("taskid");
                mOpenType = json.optString("openType");

//                mGreenMissionTask = new GreenMissionTask();
//                mGreenMissionTask.setTaskid(data.optString("taskid"));
//                mGreenMissionTask.setExamine_status(data.optInt("examine_status"));
//                mGreenMissionTask.setStatus(data.optString("status"));
//                mGreenMissionTask.setTask_name(data.optString("task_name"));
//                mGreenMissionTask.setJjcd(data.optString("jjcd"));
//                mGreenMissionTask.setTask_content(data.optString("task_content"));
//                mGreenMissionTask.setRwqyms(data.optString("rwqyms"));
//                mGreenMissionTask.setBegin_time(data.optLong("begin_time"));
//                mGreenMissionTask.setEnd_time(data.optLong("end_time"));
//                mGreenMissionTask.setUserid(data.optString("userid"));
//                mGreenMissionTask.setTypeoftask(data.optString("typeoftask"));
//                mGreenMissionTask.setRwly(data.optString("rwly"));
//                mGreenMissionTask.setTask_type(data.optInt("task_type"));
//                mGreenMissionTask.setJsr(data.optString("jsr"));
//                mGreenMissionTask.setJsdw(data.optString("jsdw"));
//                mGreenMissionTask.setTypename(data.optString("typename"));
//                mGreenMissionTask.setApproved_person(data.optString("approved_person"));
//                mGreenMissionTask.setApproved_person_name(data.optString("approved_person_name"));
//                mGreenMissionTask.setPublisher_name(data.optString("publisher_name"));
//                mGreenMissionTask.setPublisher(data.optString("publisher"));
//                mGreenMissionTask.setFbdw(data.optString("fbdw"));
//                mGreenMissionTask.setReceiver(data.optString("receiver"));
//                mGreenMissionTask.setTask_area(data.optString("task_area"));
                if (mOpenType.equals("3")){             //批示
                    NewsTaskOV event = new NewsTaskOV(3, taskid,null);
                    EventBus.getDefault().post(event);
                }else {
                    NewsTaskOV event = new NewsTaskOV(2, taskid,null);
                    EventBus.getDefault().post(event);
                }


            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");




            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

}
