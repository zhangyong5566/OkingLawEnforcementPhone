package com.zhang.okinglawenforcementphone.mvp.model;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.beans.GreenUser;
import com.zhang.okinglawenforcementphone.beans.GreenUserDao;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.LoginContract;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/18.
 */

public class LoginModel implements LoginContract.Model {
    private LoginContract.Presenter mPresenter;
    private GreenUser mGreenUser;
    private volatile Long mUid;
    private String mAcount;
    private String mPwd;

    public LoginModel(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void login(final String acount, final String pwd, final SharedPreferences sp) {
        mAcount = acount;
        mPwd = pwd;
        final GDWaterService gdWaterService = BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL);
        gdWaterService.login(acount, pwd)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                        //保存用户数据到数据库
                        String result = responseBody.string();
                        JSONObject jsonObject = new JSONObject(result);
                        String errorType = jsonObject.optString("errorType");
                        if (errorType != null && errorType.equals("2")) {
                            return Observable.error(new Throwable("密码错误"));

                        } else {
                            String dept_id = jsonObject.getString("dept_id");
                            String userid = jsonObject.getString("userid");
                            String userName = jsonObject.getString("userName");
                            String deptname = jsonObject.getString("deptname");
                            String logintime = jsonObject.getString("logintime");
                            String phone = jsonObject.getString("phone");
                            String headimg = jsonObject.getString("headimg");

                            mGreenUser = new GreenUser();
                            mGreenUser.setDept_id(dept_id);
                            mGreenUser.setUserid(userid);
                            mGreenUser.setUserName(userName);
                            mGreenUser.setDeptname(deptname);
                            mGreenUser.setPhone(phone);
                            mGreenUser.setAcount(acount);
                            mGreenUser.setPassword(pwd);
                            mGreenUser.setHeadimg(headimg);
                            mGreenUser.setLogintime(logintime);
                            OkingContract.CURRENTUSER = mGreenUser;

                            GreenUser uniqueOrThrow = GreenDAOManager.getInstence().getDaoSession().getGreenUserDao().queryBuilder().where(GreenUserDao.Properties.Userid.eq(userid)).unique();
                            sp.edit().putString(acount, userid).commit();
                            if (uniqueOrThrow != null) {
                                mUid = uniqueOrThrow.getId();
                                mGreenUser.setId(mUid);

                                GreenDAOManager.getInstence().getDaoSession().getGreenUserDao().update(mGreenUser);
                                Log.i("Oking", "登录成功更新数据");
                            } else {
                                mUid = GreenDAOManager.getInstence().getDaoSession().getGreenUserDao().insert(mGreenUser);
                                Log.i("Oking", "登录成功" + mUid);
                            }

                            return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL).loadMenu();

                        }

                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        final String result = responseBody.string();
                        if (!TextUtils.isEmpty(result)) {
                            mGreenUser.setMenuGroup(result);
                            Schedulers.io().createWorker().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    mGreenUser.setId(mUid);

                                    GreenDAOManager.getInstence().getDaoSession().getGreenUserDao().update(mGreenUser);
                                    mPresenter.loginSucc(result);
                                }
                            });
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if(throwable.getMessage().equals("密码错误")){
                            mPresenter.loginFail(throwable);
                        }else {
                            //尝试离线登录
                            RxToast.warning("正在尝试进行离线登录");
                            loginOfLine();
                        }


                    }
                });

    }


    /*
离线登录
 */
    private void loginOfLine() {
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                GreenUser unique = GreenDAOManager.getInstence().getDaoSession().getGreenUserDao().queryBuilder()
                        .where(GreenUserDao.Properties.Acount.eq(mAcount), GreenUserDao.Properties.Password.eq(mPwd)).unique();
                if (unique != null) {
                    //可以进行离线登录
                    String menuGroup = unique.getMenuGroup();
                    Log.i("Oking", "离线菜单：" + menuGroup);
                    OkingContract.CURRENTUSER = unique;
                    mPresenter.offlineLoginSucc(menuGroup);
                } else {
                    mPresenter.loginFail(new Throwable("无本地数据"));
                }
            }
        });

    }

}
