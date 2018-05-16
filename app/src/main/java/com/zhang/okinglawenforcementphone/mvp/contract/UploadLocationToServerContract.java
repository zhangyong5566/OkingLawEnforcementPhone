package com.zhang.okinglawenforcementphone.mvp.contract;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface UploadLocationToServerContract {
    interface Model {
        void upploadLocationToServer(Long loginTime, SimpleDateFormat sdf, String imei, Gson gson);
    }

    interface View {
        void uploadSucc(String result);

        void uploadFail(Throwable ex);
    }

    interface Presenter {
        void upploadLocationToServer(Long loginTime, SimpleDateFormat sdf, String imei, Gson gson);

        void uploadSucc(String result);

        void uploadFail(Throwable ex);
    }
}
