package com.zhang.okinglawenforcementphone.mvp.contract;

/**
 * Created by Administrator on 2018/6/8/008.
 */

public interface LoadBasicLogContract {
    interface Model {
        void getBasicLog(String taskId);
    }

    interface View {
        void getBasicLogSucc(String result);
        void getBasicLogFail(Throwable ex);
    }

    interface Presenter {
        void getBasicLog(String taskId);
        void getBasicLogSucc(String result);
        void getBasicLogFail(Throwable ex);
    }
}
