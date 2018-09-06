package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.jaeger.library.StatusBarUtil;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.DefaultContants;
import com.zhang.baselib.http.progress.ProgressListener;
import com.zhang.baselib.http.progress.ProgressManager;
import com.zhang.baselib.http.progress.body.ProgressInfo;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxDialogSure;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.CrashUtil;
import com.zhang.baselib.utils.LocationUtil;
import com.zhang.baselib.utils.NetUtil;
import com.zhang.baselib.utils.PermissionUtil;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.mvp.contract.AppVersionContract;
import com.zhang.okinglawenforcementphone.mvp.contract.LoginContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.AppVersionPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoginPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_button)
    CircularProgressButton loginBtn;
    @BindView(R.id.userName_editText)
    EditText userNameEditText;
    @BindView(R.id.password_editText)
    EditText passwordEditText;
    @BindView(R.id.save_pwd_button)
    CheckBox savePwdCheckBox;
    private String mName;
    private String mPwd;
    private Unbinder mBind;
    private String mNewDownloadUrl;
    private RxDialogSure mRxDialogSure;
    private RxDialogLoading mRxDialogLoading;
    private File mApkFile;
    private SharedPreferences mSp;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setTransparent(this);
        mBind = ButterKnife.bind(this, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPm();
        }

        initView();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER)) {
            finish();
        }
    }

    private void initView() {
        if (!LocationUtil.isGpsEnabled(BaseApplication.getApplictaion())) {

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0); //此为设置完成后返回到获取界面Intent GPSIntent = new Intent();
        }

    }

    private void initData() {
        //初始化化数据存储
        GreenDAOManager.getInstence().initGreenDao(this);
        CrashUtil.getInstance(BaseApplication.getApplictaion()).init();

//        int o =  1/0;
        //检测更新
        detectionUpdate();

        loginBtn.setIndeterminateProgressMode(true);

        mSp = BaseApplication.getApplictaion().getSharedPreferences("user_config", Context.MODE_PRIVATE);
        if (mSp.getBoolean("savePwd", false)) {
            final String spname = mSp.getString("username", "");
            final String spwd = mSp.getString("pwd", "");
            userNameEditText.setText(spname);
            passwordEditText.setText(spwd);
        }

        savePwdCheckBox.setChecked(mSp.getBoolean("savePwd", false));
    }

    private void detectionUpdate() {
        if (NetUtil.isConnected(BaseApplication.getApplictaion())) {
            new AppVersionPresenter(new AppVersionContract.View() {
                @Override
                public void reqSucc(String result) {
                    if (mRxDialogSure == null) {
                        mRxDialogSure = new RxDialogSure(LoginActivity.this, false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                dialogInterface.cancel();
                            }
                        });
                        mRxDialogSure.setTitle("APP需要更新");
                        mRxDialogSure.setContent("本次更新内容：");
                        mRxDialogSure.setContent(result);
                        mRxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mRxDialogSure.cancel();


                                donlowdApk();
                            }
                        });
                    }

                    mRxDialogSure.show();

                }

                @Override
                public void reqFail(Throwable ex) {

                }
            }).reqAppVersion();
        } else {
            RxToast.warning(BaseApplication.getApplictaion(), "网络无连接", Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.login_button)
    public void onClick(View view) {
        mName = userNameEditText.getText().toString().trim();
        mPwd = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mPwd)) {
            RxToast.warning(BaseApplication.getApplictaion(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        userNameEditText.setEnabled(false);
        passwordEditText.setEnabled(false);
        loginBtn.setProgress(50);
        loginBtn.setClickable(false);


        new LoginPresenter(new LoginContract.View() {
            @Override
            public void loginSucc(final String menuGroup) {
                SharedPreferences.Editor edit = mSp.edit();
                edit.putBoolean("savePwd", true);
                edit.putString("username", mName);
                edit.putString("pwd", mPwd);
                edit.commit();
                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        loginBtn.setCompleteText("登录成功");
                        loginBtn.setProgress(100);
                        DefaultContants.ISHTTPLOGIN = true;
                    }
                });
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        startActivity(intent);
                        finish();
                    }
                }, 800);

            }

            @Override
            public void loginFail(Throwable e) {
                if (e.getMessage().equals("密码错误")) {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            userNameEditText.setEnabled(true);
                            passwordEditText.setEnabled(true);
                            loginBtn.setErrorText("密码错误，请重新登录");
                            loginBtn.setProgress(-1);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loginBtn.setClickable(true);
                                    loginBtn.setProgress(0);
                                }
                            }, 1600);
                        }
                    });
                } else {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            userNameEditText.setEnabled(true);
                            passwordEditText.setEnabled(true);
                            loginBtn.setErrorText("登录失败，请重新登录");
                            loginBtn.setProgress(-1);

                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loginBtn.setClickable(true);
                                    loginBtn.setProgress(0);
                                }
                            }, 1600);
                        }
                    });

                }

            }
        }).login(mName, mPwd, mSp);

    }


    //下载apk
    private void donlowdApk() {
        if (mRxDialogLoading == null) {

            mRxDialogLoading = new RxDialogLoading(this, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });
        }
        mRxDialogLoading.setLoadingText("下载进度:0%");
        mRxDialogLoading.show();

        mNewDownloadUrl = ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(Api.BASE_URL + "/gdWater/app/gdWater.apk", getDownloadListener());

        final OkHttpClient okHttpClient = ProgressManager.getInstance().with(new OkHttpClient.Builder())
                .build();

        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = new Request.Builder()
                            .url(mNewDownloadUrl)
                            .build();

                    Response response = okHttpClient.newCall(request).execute();

                    InputStream is = response.body().byteStream();
                    mApkFile = new File(Environment.getExternalStorageDirectory().getPath(), "gdWater.apk");
                    mApkFile.mkdir();
                    if (mApkFile.exists()) {
                        mApkFile.delete();
                    }
                    FileOutputStream fos = new FileOutputStream(mApkFile);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    bis.close();
                    is.close();


                } catch (IOException e) {
                    e.printStackTrace();
                    //当外部发生错误时,使用此方法可以通知所有监听器的 onError 方法
                    ProgressManager.getInstance().notifyOnErorr(Api.BASE_URL + "/gdWater/app/gdWater.apk", e);
                }

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();

    }

    private ProgressInfo mLastDownloadingInfo;

    public ProgressListener getDownloadListener() {
        return new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                // 如果你不屏蔽用户重复点击上传或下载按钮,就可能存在同一个 Url 地址,上一次的上传或下载操作都还没结束,
                // 又开始了新的上传或下载操作,那现在就需要用到 id(请求开始时的时间) 来区分正在执行的进度信息
                // 这里我就取最新的下载进度用来展示,顺便展示下 id 的用法

                if (mLastDownloadingInfo == null) {
                    mLastDownloadingInfo = progressInfo;
                }

                //因为是以请求开始时的时间作为 Id ,所以值越大,说明该请求越新
                if (progressInfo.getId() < mLastDownloadingInfo.getId()) {
                    return;
                } else if (progressInfo.getId() > mLastDownloadingInfo.getId()) {
                    mLastDownloadingInfo = progressInfo;
                }

                int progress = mLastDownloadingInfo.getPercent();

                mRxDialogLoading.setLoadingText("下载进度:" + progress + "%");

                if (progressInfo.isFinish()) {
                    //说明已经下载完成
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            RxToast.success(BaseApplication.getApplictaion(), "下载成功", Toast.LENGTH_SHORT).show();
                            mRxDialogLoading.cancel();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(Uri.fromFile(mApkFile),
                                    "application/vnd.android.package-archive");
                            startActivity(intent);
                        }
                    });


                }
            }

            @Override
            public void onError(long id, final Exception e) {
                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        RxToast.error(BaseApplication.getApplictaion(), "下载失败，请检查网络和SD卡" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        };
    }


    private void requestPm() {
        PermissionUtil.with(this)
                .addPermission(Manifest.permission.READ_PHONE_STATE)
                .addPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.WAKE_LOCK)
                .addPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)
                .addPermission(Manifest.permission.INTERNET)
                .addPermission(Manifest.permission.RECORD_AUDIO)
                .addPermission(Manifest.permission.CAMERA)
                .addPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                .addPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .initPermission();
    }
}
