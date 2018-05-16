package com.zhang.okinglawenforcementphone.mvp.contract;

import java.util.Map;

/**
 * Created by Administrator on 2018/5/4/004.
 */

public interface UploadCaseInAdvanceContract {
    interface Model {
        void uploadCaseInAdvance(Map<String, Object> params);
    }

    interface View {
        void uploadCaseInAdvanceSucc(String result);

        void uploadCaseInAdvanceFail(Throwable ex);
    }

    interface Presenter {
        void uploadCaseInAdvance(Map<String, Object> params);

        void uploadCaseInAdvanceSucc(String result);

        void uploadCaseInAdvanceFail(Throwable ex);
    }
}
