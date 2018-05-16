package com.zhang.okinglawenforcementphone.mvp.contract;


import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface LoadHttpMissionContract {
    interface Model {
        void loadHttpMission(int classify, String receiver);
    }

    interface View {
        void loadHttpMissionSucc(List<GreenMissionTask> greenMissionTasks);

        void loadHttpMissionProgress(int total, int count);

        void loadHttpMissionFail(Throwable ex);

        void loadMissionFromLocal(List<GreenMissionTask> greenMissionTasks);
    }

    interface Presenter {
        void loadHttpMissionSucc(List<GreenMissionTask> greenMissionTasks);

        void loadHttpMission(int classify, String receiver);

        void loadHttpMissionProgress(int total, int count);

        void loadMissionFromLocal(List<GreenMissionTask> greenMissionTasks);

        void loadHttpMissionFail(Throwable ex);
    }
}
