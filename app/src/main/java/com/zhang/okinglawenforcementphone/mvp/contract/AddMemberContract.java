package com.zhang.okinglawenforcementphone.mvp.contract;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface AddMemberContract {
    interface Model {
        void addMember(String userid, String mtaskId, String userids);
    }

    interface View {
        void addMemberSucc(String result);

        void addMemberFail(Throwable ex);
    }

    interface Presenter {
        void addMember(String userid, String mtaskId, String userids);
        void addMemberSucc(String result);

        void addMemberFail(Throwable ex);
    }

}
