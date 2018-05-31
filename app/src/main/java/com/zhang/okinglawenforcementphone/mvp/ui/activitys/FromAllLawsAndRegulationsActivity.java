package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.LawsAndRegulationAdapter;
import com.zhang.okinglawenforcementphone.beans.LawBean;
import com.zhang.okinglawenforcementphone.db.LawDao;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FromAllLawsAndRegulationsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rcy_laws)
    RecyclerView mRcyLaws;
    private Unbinder mBind;
    private LawsAndRegulationAdapter mLawsAndRegulationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_all_laws_and_regulations);
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
        mLawsAndRegulationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<LawBean> data = adapter.getData();
                Intent intent = new Intent(FromAllLawsAndRegulationsActivity.this, RegulationsDetailsActivity.class);
                intent.putExtra("title", data.get(position).getTitle());
                intent.putExtra("mmid", data.get(position).getMmid());
                intent.putExtra("rulesContent", data.get(position).getRulesContent());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mRcyLaws.setNestedScrollingEnabled(false);
        mRcyLaws.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mRcyLaws.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 10, BaseApplication.getApplictaion().getResources().getColor(R.color.bottom_nav_normal)));

        List<LawBean> laws = LawDao.getLaw();
        mLawsAndRegulationAdapter = new LawsAndRegulationAdapter(R.layout.lawandregulation_item, laws);
        mLawsAndRegulationAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        mRcyLaws.setAdapter(mLawsAndRegulationAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
