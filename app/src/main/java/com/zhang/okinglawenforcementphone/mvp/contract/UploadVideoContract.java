package com.zhang.okinglawenforcementphone.mvp.contract;

import com.google.gson.Gson;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public interface UploadVideoContract {
    interface Model {
        void uploadVideo(GreenMissionLog greenMissionLog, List<GreenMedia> greenMedias, Map<String, RequestBody> photoParams, SimpleDateFormat videosdf, Gson gson);
    }

    interface View {
        void loadVideoSucc(String result);

        void uploadRetry(Throwable ex);

        void loadVideoFail(Throwable ex);

        void uploadIsCount(int pos);
    }

    interface Presenter {
        void uploadVideo(GreenMissionLog greenMissionLog, List<GreenMedia> greenMedias, Map<String, RequestBody> photoParams, SimpleDateFormat videosdf, Gson gson);

        void loadVideoSucc(String result);

        void uploadRetry(Throwable ex);

        void loadVideoFail(Throwable ex);

        void uploadIsCount(int pos);
    }
}
