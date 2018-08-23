package com.zhang.okinglawenforcementphone.mvp.contract;

/**
 * Created by Administrator on 2018/6/9/009.
 */

public interface LoadAcceptNumberContract {
    interface Model {
        void loadAcceptNumber();

    }

    interface View {
        void loadAcceptNumberSucc(String result);

        void loadAcceptNumberFail(Throwable ex);
    }

    interface Presenter {
        void loadAcceptNumber();

        void loadAcceptNumberSucc(String result);

        void loadAcceptNumberFail(Throwable ex);
    }
}
