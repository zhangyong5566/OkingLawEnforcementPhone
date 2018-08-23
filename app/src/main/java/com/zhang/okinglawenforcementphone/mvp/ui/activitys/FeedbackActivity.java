package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.AppUtil;
import com.zhang.baselib.utils.DeviceUtil;
import com.zhang.baselib.utils.NetUtil;
import com.zhang.baselib.utils.RegUtil;
import com.zhang.baselib.utils.TextUtil;
import com.zhang.baselib.utils.Util;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.SendEmailManager;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.type_flow_layout)
    TagFlowLayout mTypeFlowLayout;
    @BindView(R.id.et_contet)
    EditText mEtContet;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.bt_submit)
    TextView mBtSubmit;
    private Unbinder mBind;
    private ArrayList<String> mPartList;
    private String mAdviceType = "无";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
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
        mPartList = new ArrayList<>();
        mPartList.add("bug");
        mPartList.add("产品建议");
        mPartList.add("吐槽");
        mPartList.add("其他");
        TagAdapter<String> tagAdapter = new TagAdapter<String>(mPartList) {
            @Override
            public View getView(FlowLayout parent, int position, String parts) {
                View inflate = View.inflate(BaseApplication.getApplictaion(), R.layout.feedback_tag_item, null);
                TextView tv_tag = inflate.findViewById(R.id.tv_tag);
                tv_tag.setText(parts);
                return inflate;
            }
        };
        mTypeFlowLayout.setAdapter(tagAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();

    }

    @OnClick(R.id.bt_submit)
    public void onViewClicked(View view) {
        String advice = mEtContet.getText().toString().trim();
        String addr = mEtPhone.getText().toString().trim();

        if (!TextUtils.isEmpty(advice) && NetUtil.isConnected(BaseApplication.getApplictaion())) {
            if (!TextUtils.isEmpty(addr)) {
                if (RegUtil.isEmail(addr) || RegUtil.isMobileSimple(addr)) {

                } else {
                    RxToast.warning("请输入正确的联系方式");
                    return;
                }

            }

            mBtSubmit.setEnabled(false);
            Set<Integer> selectedList = mTypeFlowLayout.getSelectedList();
            Iterator<Integer> it = selectedList.iterator();
            while (it.hasNext()) {
                Integer next = it.next();
                mAdviceType = mPartList.get(next);
            }
            final String content = "用户名：" + OkingContract.CURRENTUSER.getUserName() + "\n"
                    + "设备厂商：" + Build.MANUFACTURER + "\n"
                    + "设备型号：" + Build.MODEL + "\n"
                    + "系统版本：" + Build.VERSION.RELEASE + "\n"
                    + "程序版本：" + DeviceUtil.getAppVersionName(BaseApplication.getApplictaion()) + "\n"
                    + "意见类型：" + mAdviceType + "\n"
                    + "意见内容：" + advice + "\n"
                    + "联系方式：" + addr;
            Schedulers.io().createWorker().schedule(new Runnable() {
                @Override
                public void run() {
                    boolean succ = SendEmailManager.send("zhy842667166@qq.com", "水政执法意见反馈", content);
                    if (succ) {
                        AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                            @Override
                            public void run() {
                                RxToast.success("您的反馈我们已经收到~~");
                                mBtSubmit.setEnabled(true);
                            }
                        });
                    }
                }
            });

        }

    }
}
