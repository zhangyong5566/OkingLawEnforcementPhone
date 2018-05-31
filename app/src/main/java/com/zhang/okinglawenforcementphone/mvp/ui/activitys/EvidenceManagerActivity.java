package com.zhang.okinglawenforcementphone.mvp.ui.activitys;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenCase;
import com.zhang.okinglawenforcementphone.beans.GreenCaseDao;
import com.zhang.okinglawenforcementphone.beans.SaveOrRemoveDataEvent;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.CaseAudioVideoEvidenceFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.CaseAudioVideoEvidenceListFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.DocumentaryEvidenceFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.DocumentaryEvidenceListFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 证据管理
 */
public class EvidenceManagerActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.document_tabBtn)
    TextView documentTabBtn;
    @BindView(R.id.audioVideo_tabBtn)
    TextView audioVideoTabBtn;
    private Unbinder mBind;
    private GreenCase mUnique;
    private DocumentaryEvidenceListFragment mDocumentaryListFragment;
    private CaseAudioVideoEvidenceListFragment mCaseAudioVideoEvidenceListFragment;
    private MenuItem mItem;
    private RxDialogSureCancel mRxDialogSureCancel;
    private DocumentaryEvidenceFragment mDocumentaryEvidenceFragment;
    private CaseAudioVideoEvidenceFragment mCaseAudioVideoEvidenceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidence_manager);
        mBind = ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItem.setVisible(true);
                documentTabBtn.setEnabled(true);
                audioVideoTabBtn.setEnabled(true);
                mDocumentaryEvidenceFragment = (DocumentaryEvidenceFragment) getSupportFragmentManager().findFragmentByTag("documentaryEvidenceFragment");
                mCaseAudioVideoEvidenceFragment = (CaseAudioVideoEvidenceFragment) getSupportFragmentManager().findFragmentByTag("caseAudioVideoEvidenceFragment");
                if (mDocumentaryEvidenceFragment !=null&&!mDocumentaryEvidenceFragment.isHidden()){
                    if (mDocumentaryEvidenceFragment.getType()==0) {
                        getSupportFragmentManager().beginTransaction().remove(mDocumentaryEvidenceFragment).commit();
                    }else {
                        saveData(mDocumentaryEvidenceFragment);
                    }
                }else if (mCaseAudioVideoEvidenceFragment !=null&&!mCaseAudioVideoEvidenceFragment.isHidden()){
                    saveData(mCaseAudioVideoEvidenceFragment);
                }else {

                    finish();
                }
            }
        });
    }

    private void initData() {
        mToolbar.inflateMenu(R.menu.toolbar_todo_menu);
        mItem = mToolbar.getMenu().findItem(R.id.material);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!mDocumentaryListFragment.isHidden()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    DocumentaryEvidenceFragment documentaryEvidenceFragment = DocumentaryEvidenceFragment.newInstance(2);
                    documentaryEvidenceFragment.setGreenCase(mUnique, null);
                    ft.add(R.id.rl_sub_content, documentaryEvidenceFragment, "documentaryEvidenceFragment").commit();


                } else if (!mCaseAudioVideoEvidenceListFragment.isHidden()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    CaseAudioVideoEvidenceFragment caseAudioVideoEvidenceFragment = CaseAudioVideoEvidenceFragment.newInstance(2);
                    caseAudioVideoEvidenceFragment.setGreenCase(mUnique,null);
                    ft.add(R.id.rl_sub_content, caseAudioVideoEvidenceFragment,"caseAudioVideoEvidenceFragment").commit();


                }

                return false;
            }
        });
        String ajid = getIntent().getStringExtra("AJID");
        if (ajid != null) {
            mUnique = GreenDAOManager.getInstence().getDaoSession().getGreenCaseDao().queryBuilder().where(GreenCaseDao.Properties.AJID.eq(ajid)).unique();
        }
        if (mDocumentaryListFragment == null) {
            mDocumentaryListFragment = DocumentaryEvidenceListFragment.newInstance(null);
            mDocumentaryListFragment.setGreenCase(mUnique);
            getSupportFragmentManager().beginTransaction().replace(R.id.rl_sub_content, mDocumentaryListFragment, "documentaryEvidenceListFragment").commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @OnClick({R.id.document_tabBtn, R.id.audioVideo_tabBtn})
    public void onViewClicked(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.document_tabBtn:

                documentTabBtn.setTextColor(getResources().getColor(R.color.bottom_nav_selected));
                audioVideoTabBtn.setTextColor(getResources().getColor(R.color.colorMain4));


                if (mCaseAudioVideoEvidenceListFragment != null) {
                    fragmentTransaction.hide(mCaseAudioVideoEvidenceListFragment);
                }

                if (mDocumentaryListFragment == null) {
                    mDocumentaryListFragment = DocumentaryEvidenceListFragment.newInstance(null);
                    mDocumentaryListFragment.setGreenCase(mUnique);
                    fragmentTransaction.add(R.id.rl_sub_content, mDocumentaryListFragment, "documentaryEvidenceListFragment").commit();

                } else {


                        fragmentTransaction.show(mDocumentaryListFragment).commit();
                }
                break;
            case R.id.audioVideo_tabBtn:

                audioVideoTabBtn.setTextColor(getResources().getColor(R.color.bottom_nav_selected));

                documentTabBtn.setTextColor(getResources().getColor(R.color.colorMain4));
                if (mDocumentaryListFragment != null) {
                    fragmentTransaction.hide(mDocumentaryListFragment);
                }
                if (mCaseAudioVideoEvidenceListFragment == null) {
                    mCaseAudioVideoEvidenceListFragment = CaseAudioVideoEvidenceListFragment.newInstance(null);
                    mCaseAudioVideoEvidenceListFragment.setGreenCase(mUnique);
                    fragmentTransaction.add(R.id.rl_sub_content, mCaseAudioVideoEvidenceListFragment, "caseAudioVideoEvidenceListFragment").commit();

                } else {

                        fragmentTransaction.show(mCaseAudioVideoEvidenceListFragment).commit();

                }
                break;
            default:
                break;
        }
    }

    public void setVisibleAdd(boolean visible) {
        mItem.setVisible(visible);
        documentTabBtn.setEnabled(visible);
        audioVideoTabBtn.setEnabled(visible);
    }


    private void saveData(final Fragment fragment) {
        if (mRxDialogSureCancel == null) {
            mRxDialogSureCancel = new RxDialogSureCancel(EvidenceManagerActivity.this);
            mRxDialogSureCancel.setContent("是否需要保存数据？");

        }
        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRxDialogSureCancel.cancel();
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });

        mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRxDialogSureCancel.cancel();
                EventBus.getDefault().post(new SaveOrRemoveDataEvent(-1));
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });
        mRxDialogSureCancel.show();
    }
}
