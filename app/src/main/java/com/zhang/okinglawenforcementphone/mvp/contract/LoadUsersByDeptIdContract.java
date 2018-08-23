package com.zhang.okinglawenforcementphone.mvp.contract;

/**
 * Created by Administrator on 2018/6/8/008.
 */

public interface LoadUsersByDeptIdContract {
    interface Model {
        void getUsersByDeptId(String deptId);

    }

    interface View {
        void getUsersByDeptIdSucc(String result);

        void getUsersByDeptIdFail(Throwable e);
    }

    interface Presenter {
        void getUsersByDeptId(String deptId);

        void getUsersByDeptIdSucc(String result);

        void getUsersByDeptIdFail(Throwable e);
    }
}
