package com.zhang.okinglawenforcementphone.mvp.contract;

import com.google.gson.Gson;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public interface UploadJobLogForPicContract {
    interface Model {
        void uploadJobLogForPic(Gson gson, Map<String, RequestBody> photoParams, GreenMissionLog Log, final List<GreenMedia> media);
    }

    interface View {
        void uploadSucc(String result);

        void uploadRetry(Throwable ex);

        void uploadFail(Throwable ex);

        void uploadIsCount(int pos);

        void uploadPositionFail(Throwable ex);
    }

    interface Presenter {
        void uploadJobLogForPic(Gson gson, Map<String, RequestBody> photoParams, GreenMissionLog Log, final List<GreenMedia> media);

        void uploadSucc(String result);

        void uploadRetry(Throwable ex);

        void uploadFail(Throwable ex);

        void uploadIsCount(int pos);

        void uploadPositionFail(Throwable ex);
    }
}
