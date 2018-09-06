package com.zhang.okinglawenforcementphone.mvp.contract;

import com.zhang.okinglawenforcementphone.beans.JPushMessageBean;

/**
 * Created by Administrator on 2018/8/27/027.
 */

public interface JPushMessageContract {
    interface Model {
        void pushMessage(JPushMessageBean jPushMessageBean);
    }

    interface View {
        void pushMessageSucc(String result);

        void pushMessageFail(Throwable ex);
    }

    interface Presenter {
        void pushMessage(JPushMessageBean jPushMessageBean);

        void pushMessageSucc(String result);

        void pushMessageFail(Throwable ex);
    }
}
