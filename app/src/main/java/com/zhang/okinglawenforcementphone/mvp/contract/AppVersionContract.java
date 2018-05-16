package com.zhang.okinglawenforcementphone.mvp.contract;

/**
 * Created by Administrator on 2018/4/18.
 */

public interface AppVersionContract {
    interface Model {
        void reqAppVersion();
    }

    interface View {
        void reqSucc(String result);

        void reqFail(Throwable ex);
    }

    interface Presenter {
        void reqAppVersion();
        void reqSucc(String result);

        void reqFail(Throwable ex);
    }
}
