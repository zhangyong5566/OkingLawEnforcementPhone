package com.zhang.okinglawenforcementphone.mvp.contract;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface UpdateMissionStateContract {
    interface Model {
        void updateMissionState(String id, String executeStartTime, String executeEndTime, int status);
    }

    interface View {
        void updateMissionStateSucc(String result);

        void updateMissionStateFail(Throwable ex);
    }

    interface Presenter {
        void updateMissionState(String id, String executeStartTime, String executeEndTime, int status);
        void updateMissionStateSucc(String result);

        void updateMissionStateFail(Throwable ex);
    }
}
