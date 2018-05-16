package com.zhang.okinglawenforcementphone.mvp.contract;

import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public interface UploadSignaturePicContract {
    interface Model {
        void uploadSignaturePic(GreenMissionLog mGreenMissionLog, GreenMissionTask missionTask, Map<String, RequestBody> photoParams);
    }

    interface View {

        void uploadSignaturePicSucc(String result);
        void uploadIsCount(int pos);
        void uploadRetry(Throwable ex);
        void uploadSignatureFail(Throwable ex);
    }

    interface Presenter {
        void uploadSignaturePic(GreenMissionLog mGreenMissionLog, GreenMissionTask missionTask, Map<String, RequestBody> photoParams);
        void uploadSignaturePicSucc(String result);
        void uploadIsCount(int pos);
        void uploadRetry(Throwable ex);
        void uploadSignatureFail(Throwable ex);
    }
}
