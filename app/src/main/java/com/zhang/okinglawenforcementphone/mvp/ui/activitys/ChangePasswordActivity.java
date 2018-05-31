package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.ActivityUtil;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.htttp.Api;
import com.zhang.okinglawenforcementphone.htttp.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_oldpwd)
    EditText mEtOldpwd;
    @BindView(R.id.et_newpwd)
    EditText mEtNewpwd;
    @BindView(R.id.et_confirm_pwd)
    EditText mEtConfirmPwd;
    @BindView(R.id.bt_submit)
    TextView mBtSubmit;
    private Unbinder mBind;
    private RxDialogLoading mRxDialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mBind = ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {

    }

    @OnClick(R.id.bt_submit)
    public void onViewClicked() {
        String oldPwd = mEtOldpwd.getText().toString().trim();
        String newPwd = mEtNewpwd.getText().toString().trim();
        String confirmPwd = mEtConfirmPwd.getText().toString().trim();
        if (!TextUtils.isEmpty(oldPwd) && !TextUtils.isEmpty(newPwd) && !TextUtils.isEmpty(confirmPwd)) {
            if (newPwd.equals(confirmPwd)) {
                if (mRxDialogLoading == null) {
                    mRxDialogLoading = new RxDialogLoading(ChangePasswordActivity.this, false, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.cancel();
                        }
                    });
                    mRxDialogLoading.setLoadingText("正在提交数据中请稍后...");
                }
                mRxDialogLoading.show();
                Map<String, Object> params = new HashMap<>();
                params.put("userid", OkingContract.CURRENTUSER.getUserid());
                params.put("account", OkingContract.CURRENTUSER.getAcount());
                params.put("oldPassword", oldPwd);
                params.put("password", newPwd);

                BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                        .uploadUserInfo(params)
                        .compose(RxSchedulersHelper.<ResponseBody>io_main())
                        .subscribe(new Consumer<ResponseBody>() {
                            @Override
                            public void accept(ResponseBody responseBody) throws Exception {
                                String result = responseBody.string();
                                mRxDialogLoading.cancel();
                                if ("1".equals(result)) {
                                    RxToast.success("修改密码成功！");
                                    mBtSubmit.setEnabled(false);
                                    OkingContract.CURRENTUSER = null;

                                   new Handler().postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           ActivityUtil.finishAllActivity();
                                           Intent intent = getBaseContext().getPackageManager()
                                                   .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                           startActivity(intent);
                                           Process.killProcess(Process.myPid());
                                       }
                                   }, 500);



                                } else if ("0".equals(result)) {
                                    RxToast.error("修改密码失败！");
                                } else {
                                    RxToast.error("原密码错误！");
                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.i("Oking", "修改失败" + throwable.toString());
                                mRxDialogLoading.cancel();
                                RxToast.error("网络错误！");

                            }
                        });
            } else {
                RxToast.error("两次输入的密码不一致");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBind.unbind();
    }
}
