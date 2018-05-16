package com.zhang.okinglawenforcementphone.mvp.contract;

import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/4/18.
 */

public interface LoginContract {
    interface Model {
        void login( String acount,String pwd, SharedPreferences sp);
    }

    interface View {
        void loginSucc(String menuGroup);
        void loginFail(Throwable e);
    }

    interface Presenter {
        void login( String acount,  String pwd,  SharedPreferences sp);
        void loginSucc(String menuGroup);

        void offlineLoginSucc(String menuGroup);

        void loginFail(Throwable e);
    }

}
