package com.zhang.okinglawenforcementphone.mvp.contract;

import com.google.gson.Gson;
import com.zhang.okinglawenforcementphone.beans.RecordLogOV;

/**
 * Created by Administrator on 2018/4/26/026.
 */

public interface UploadJobLogContract {
    interface Model {
        void uploadJobLog(RecordLogOV recordLogOV, Gson gson);
    }

    interface View {
        void uploadJobLogSucc(String result);

        void uploadJobLogFail(Throwable ex);
    }

    interface Presenter {
        void uploadJobLog(RecordLogOV recordLogOV, Gson gson);

        void uploadJobLogSucc(String result);

        void uploadJobLogFail(Throwable ex);
    }
}
