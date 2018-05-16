package com.zhang.okinglawenforcementphone;

import android.content.IntentFilter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.zhang.baselib.BaseApplication;

/**
 * Created by Administrator on 2018/4/25/025.
 */

public class OkingEMManager {
    private static OkingEMManager mOkingEMManager;
    private CallReceiver callReceiver;
    private OkingEMManager() {
    }

    public static final OkingEMManager getInstence() {
        if (mOkingEMManager == null) {
            synchronized (OkingEMManager.class) {
                if (mOkingEMManager == null) {
                    mOkingEMManager = new OkingEMManager();
                }
            }
        }

        return mOkingEMManager;
    }

    public void initEM(){
        EMOptions options = new EMOptions();
        //        options.setAcceptInvitationAlways(false);
        options.setAutoLogin(false);
        options.setRequireAck(true);
        options.setRequireDeliveryAck(true);
        EaseUI.getInstance().init(BaseApplication.getApplictaion(), options);

        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if(callReceiver == null){
            callReceiver = new CallReceiver();
        }

        //register incoming call receiver
        BaseApplication.getApplictaion().registerReceiver(callReceiver, callFilter);
    }

    public void unRegist(){
        BaseApplication.getApplictaion().unregisterReceiver(callReceiver);
    }
}
