package com.zhang.okinglawenforcementphone.mvp.contract;

import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;

import java.io.File;

/**
 * Created by Administrator on 2018/4/23/023.
 */

public interface GetHttpMissionLogContract {
    interface Model {
        void getHttpMissionLog(GreenMissionTask mission);
    }

    interface View {
        void loadHttpMissionLogSucc(GreenMissionLog greenMissionLog);

        void loadHttpMissionLogFail(Throwable ex);
    }

    interface Presenter {
        void getHttpMissionLog(GreenMissionTask mission);

        void loadHttpMissionLogSucc(GreenMissionLog greenMissionLog);

        void loadHttpMissionLogFail(Throwable ex);

    }
}
