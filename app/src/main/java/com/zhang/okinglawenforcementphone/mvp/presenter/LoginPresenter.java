package com.zhang.okinglawenforcementphone.mvp.presenter;

import android.content.SharedPreferences;

import com.zhang.okinglawenforcementphone.mvp.contract.LoginContract;
import com.zhang.okinglawenforcementphone.mvp.model.LoginModel;

/**
 * Created by Administrator on 2018/4/18.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.Model mModel;
    private LoginContract.View mView;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        mModel = new LoginModel(this);
    }

    @Override
    public void login(String acount, String pwd, SharedPreferences sp) {
        mModel.login(acount,pwd,sp);
    }

    @Override
    public void loginSucc(String menuGroup) {
        mView.loginSucc(menuGroup);
    }

    @Override
    public void offlineLoginSucc(String menuGroup) {
       mView.loginSucc(menuGroup);
    }

    @Override
    public void loginFail(Throwable e) {
        mView.loginFail(e);
    }
}
