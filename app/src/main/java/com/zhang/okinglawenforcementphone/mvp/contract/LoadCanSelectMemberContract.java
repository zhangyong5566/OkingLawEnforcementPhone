package com.zhang.okinglawenforcementphone.mvp.contract;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface LoadCanSelectMemberContract {
    interface Model {
        void loadCanSelectMember();
    }

    interface View {
        void loadCanSelectMemberSucc(String result);

        void loadCanSelectMemberFail(Throwable ex);
    }

    interface Presenter {
        void loadCanSelectMember();
        void loadCanSelectMemberSucc(String result);

        void loadCanSelectMemberFail(Throwable ex);
    }
}
