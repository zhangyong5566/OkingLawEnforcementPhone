package com.zhang.okinglawenforcementphone.mvp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhang.baselib.utils.ActivityUtil;


/**
 * Created by Administrator on 2018/2/27.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtil.addActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.finishActivity(this);
    }

}
