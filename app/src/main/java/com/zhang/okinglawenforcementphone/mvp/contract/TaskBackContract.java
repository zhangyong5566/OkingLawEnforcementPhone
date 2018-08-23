package com.zhang.okinglawenforcementphone.mvp.contract;

import java.util.Map;

import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/6/7/007.
 */

public interface TaskBackContract {
    interface Model {
       void taskBack(Map<String, Object> params);


    }

    interface View {
        void taskBackSucc(String result);
        void taskBackFail(Throwable ex);
    }

    interface Presenter {
        void taskBack(Map<String, Object> params);
        void taskBackSucc(String result);
        void taskBackFail(Throwable ex);
    }

}
