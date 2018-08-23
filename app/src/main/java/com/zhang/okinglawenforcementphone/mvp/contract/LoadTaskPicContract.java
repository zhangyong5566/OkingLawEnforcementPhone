package com.zhang.okinglawenforcementphone.mvp.contract;

/**
 * Created by Administrator on 2018/6/8/008.
 */

public interface LoadTaskPicContract {
    interface Model {
        void loadTaskPic(String logId);
    }

    interface View {
        void loadTaskPicSucc(String result);

        void loadTaskPicFail(Throwable e);
    }

    interface Presenter {
        void loadTaskPic(String logId);

        void loadTaskPicSucc(String result);

        void loadTaskPicFail(Throwable e);
    }
}


