package com.zhang.okinglawenforcementphone.mvp.contract;

import java.util.Map;

/**
 * Created by Administrator on 2018/4/28/028.
 */

public interface UploadRecordContract {
    interface Model {
       void uploadRecord(Map<String, Object> params);
    }

    interface View {
        void uploadRecordSucc(String result);
        void uploadRecordFail(Throwable ex);
    }

    interface Presenter {
        void uploadRecord(Map<String, Object> params);
        void uploadRecordSucc(String result);
        void uploadRecordFail(Throwable ex);
    }
}
