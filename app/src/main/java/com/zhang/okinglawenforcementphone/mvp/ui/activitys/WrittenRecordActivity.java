package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.ExpandableWrittenRecordAdapter;
import com.zhang.okinglawenforcementphone.adapter.ProblemAdapter;
import com.zhang.okinglawenforcementphone.beans.ProblemBean;
import com.zhang.okinglawenforcementphone.beans.WrittenItemBean;
import com.zhang.okinglawenforcementphone.beans.WrittenRecordLevel0;
import com.zhang.okinglawenforcementphone.db.LawDao;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WrittenRecordActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rcy_written_record)
    RecyclerView mRcyWrittenRecord;
    @BindView(R.id.bt_print)
    Button mBtPrint;
    @BindView(R.id.bt_save)
    Button mBtSave;
    @BindView(R.id.bt_problem)
    Button mBtProblem;
    private TextView mTvTypename;
    private ProblemAdapter mProblemAdapter;
    private Unbinder mBind;
    private WrittenRecordLevel0 mWrittenRecordLevel0;
    private WrittenItemBean mWrittenItemBean;
    private ExpandableWrittenRecordAdapter mExpandableWrittenRecordAdapter;
    private String mCasetypeName;
    private ListView mLvQuestions;
    private View mContentView;
    private ArrayList<ProblemBean> mProblemContent;
    private DialogUtil mDialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_written_record);
        mBind = ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

        if (mExpandableWrittenRecordAdapter == null) {

            mExpandableWrittenRecordAdapter = new ExpandableWrittenRecordAdapter(generateData(), this);
            mRcyWrittenRecord.setAdapter(mExpandableWrittenRecordAdapter);
        } else {
            mExpandableWrittenRecordAdapter.setNewData(generateData());
        }
        mExpandableWrittenRecordAdapter.expand(0);
    }

    private List<MultiItemEntity> generateData() {

        ArrayList<MultiItemEntity> res = new ArrayList<>();

        mWrittenRecordLevel0 = new WrittenRecordLevel0("基本信息");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(1);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        mWrittenRecordLevel0 = new WrittenRecordLevel0("询问人情况");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(2);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        mWrittenRecordLevel0 = new WrittenRecordLevel0("被询问人情况");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(3);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        mWrittenRecordLevel0 = new WrittenRecordLevel0("询问问题");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(4);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);


        return res;

    }

    private void initView() {
        mRcyWrittenRecord.setNestedScrollingEnabled(false);
        mRcyWrittenRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRcyWrittenRecord.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 3, BaseApplication.getApplictaion().getResources().getColor(R.color.bottom_nav_normal)));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @OnClick({R.id.bt_print, R.id.bt_save, R.id.bt_problem})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_print:
                print();
                break;
            case R.id.bt_save:
                savaData();
                break;
            case R.id.bt_problem:
                showWindow();
                break;
            default:
                break;
        }
    }


    private void print() {
        mExpandableWrittenRecordAdapter.print();
    }

    private void savaData() {
        mExpandableWrittenRecordAdapter.savaData();
    }

    private void showWindow() {
        if (mContentView == null) {

            mContentView = LayoutInflater.from(this).inflate(R.layout.questions_pupwindow, null, false);
            mLvQuestions = mContentView.findViewById(R.id.lv_questions);
            final TextView tv_add = mContentView.findViewById(R.id.tv_add);
            final TextView tv_back = mContentView.findViewById(R.id.tv_back);
            mTvTypename = mContentView.findViewById(R.id.tv_typename);
            final TextView tv_tag1 = mContentView.findViewById(R.id.tv_tag1);
            final EditText et_ask_content = mContentView.findViewById(R.id.et_ask_content);
            final Button bt_add = mContentView.findViewById(R.id.bt_add);
            tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_add.setVisibility(View.GONE);
                    tv_back.setVisibility(View.VISIBLE);
                    mLvQuestions.setVisibility(View.GONE);
                    mTvTypename.setVisibility(View.VISIBLE);
                    tv_tag1.setVisibility(View.VISIBLE);
                    et_ask_content.setVisibility(View.VISIBLE);
                    bt_add.setVisibility(View.VISIBLE);
                }
            });

            tv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_add.setVisibility(View.VISIBLE);
                    tv_back.setVisibility(View.GONE);
                    mLvQuestions.setVisibility(View.VISIBLE);
                    mTvTypename.setVisibility(View.GONE);
                    tv_tag1.setVisibility(View.GONE);
                    et_ask_content.setVisibility(View.GONE);
                    bt_add.setVisibility(View.GONE);
                }
            });
            bt_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ask_content = et_ask_content.getText().toString().trim();
                    if (!TextUtils.isEmpty(ask_content)) {
                        ProblemBean problemBean = new ProblemBean();

                        problemBean.setAsk(ask_content);
                        problemBean.setTypename(mCasetypeName);
                        problemBean.setType(mExpandableWrittenRecordAdapter.getCasetype());
                        long state = LawDao.insetProblemContent(problemBean);
                        if (state > 0) {
                            RxToast.success(BaseApplication.getApplictaion(), "问题新增成功", Toast.LENGTH_SHORT).show();
                            problemBean.setRowid((int) state);
                            mProblemContent.add(problemBean);
                            mExpandableWrittenRecordAdapter.notyAskData(mProblemContent);
                        } else {
                            RxToast.error(BaseApplication.getApplictaion(), "问题新增失败", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            });
        }
        if (mDialogUtil == null) {
            mDialogUtil = new DialogUtil();

        }
        mDialogUtil.showBottomDialog(WrittenRecordActivity.this, mContentView,400f);
        mCasetypeName = mExpandableWrittenRecordAdapter.getCasetypeName();
        mProblemContent = LawDao.getProblemContent(mCasetypeName);
        if (mProblemAdapter == null) {

            mProblemAdapter = new ProblemAdapter(WrittenRecordActivity.this);
        }
        mTvTypename.setText("案件类型：" + mCasetypeName);
        mProblemAdapter.setDatas(mProblemContent);
        mLvQuestions.setAdapter(mProblemAdapter);
    }

    public void notyAskData() {
        mExpandableWrittenRecordAdapter.notyAskData(mProblemContent);
    }
}
