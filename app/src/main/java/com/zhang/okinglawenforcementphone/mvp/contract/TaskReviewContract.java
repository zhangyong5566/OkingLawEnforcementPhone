package com.zhang.okinglawenforcementphone.mvp.contract;

import java.util.Map;

/**
 * Created by Administrator on 2018/6/6/006.
 */

public interface TaskReviewContract {
    interface Model {
        void taskReview(Map<String, Object> params);

    }

    interface View {
        void taskReviewSucc(String result);

        void taskReviewFail(Throwable ex);
    }

    interface Presenter {
        void taskReview(Map<String, Object> params);

        void taskReviewSucc(String result);

        void taskReviewFail(Throwable ex);
    }
}
