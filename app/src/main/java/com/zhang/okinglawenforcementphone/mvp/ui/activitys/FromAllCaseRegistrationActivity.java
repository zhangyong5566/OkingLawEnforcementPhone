package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.ExpandableItemCaseRegistAdapter;
import com.zhang.okinglawenforcementphone.beans.WrittenItemBean;
import com.zhang.okinglawenforcementphone.beans.WrittenRecordLevel0;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FromAllCaseRegistrationActivity extends BaseActivity {

    @BindView(R.id.case_regist_Recy)
    RecyclerView mCaseRegistRecy;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Unbinder mBind;

    private WrittenRecordLevel0 mWrittenRecordLevel0;
    private WrittenItemBean mWrittenItemBean;
    private ExpandableItemCaseRegistAdapter mExpandableItemCaseRegistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_all_case_registration);
        mBind = ButterKnife.bind(this);
        initData();
        setListener();

    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        mCaseRegistRecy.setNestedScrollingEnabled(false);
        mCaseRegistRecy.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mCaseRegistRecy.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 3, BaseApplication.getApplictaion().getResources().getColor(R.color.bottom_nav_normal)));

        if (mExpandableItemCaseRegistAdapter == null) {

            mExpandableItemCaseRegistAdapter = new ExpandableItemCaseRegistAdapter(generateData());
            mCaseRegistRecy.setAdapter(mExpandableItemCaseRegistAdapter);
        }
        mExpandableItemCaseRegistAdapter.expand(0);
    }

    private List<MultiItemEntity> generateData() {

        ArrayList<MultiItemEntity> res = new ArrayList<>();

        mWrittenRecordLevel0 = new WrittenRecordLevel0("案件来源");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(1);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        mWrittenRecordLevel0 = new WrittenRecordLevel0("上传证据");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(2);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        mWrittenRecordLevel0 = new WrittenRecordLevel0("基本信息(可选)");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(3);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        return res;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
