package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.TextUtil;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadCaseInAdvanceContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadCaseInAdvancePresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FromAllCaseInAdvanceActivity extends BaseActivity implements OnDateSetListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_cause_action)
    TextInputEditText mEtCauseAction;
    @BindView(R.id.bt_time)
    Button mBtTime;
    @BindView(R.id.et_scene)
    TextInputEditText mEtScene;
    @BindView(R.id.cb_examination_revealed)
    CheckBox mCbExaminationRevealed;
    @BindView(R.id.cb_superior_specified)
    CheckBox mCbSuperiorSpecified;
    @BindView(R.id.cb_department_transfer)
    CheckBox mCbDepartmentTransfer;
    @BindView(R.id.cb_masses_report)
    CheckBox mCbMassesReport;
    @BindView(R.id.cb_media_disclosure)
    CheckBox mCbMediaDisclosure;
    @BindView(R.id.cb_other)
    CheckBox mCbOther;
    @BindView(R.id.et_other)
    EditText mEtOther;
    @BindView(R.id.et_situation)
    TextInputEditText mEtSituation;
    @BindView(R.id.et_case_brief)
    TextInputEditText mEtCaseBrief;
    @BindView(R.id.et_opinion)
    TextInputEditText mEtOpinion;
    @BindView(R.id.bt_ok)
    Button mBtOk;
    private Unbinder mBind;
    private RxDialogLoading mRxDialogLoading;
    private TimePickerDialog mBeginDialogAll;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String sourceCaseType = "";             //案件来源类型  0检查发现；1上级指定；2部门移送；3群众举报；4媒体披露；5其他';
    private String sourceCase = "";
    private UploadCaseInAdvancePresenter mUploadCaseInAdvancePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_all_case_in_advance);
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
        TextUtil.setEditTextInhibitInputSpace(mEtCauseAction);
        TextUtil.setEditTextInhibitInputSpeChat(mEtCauseAction);
    }

    @OnClick({R.id.bt_time, R.id.bt_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_time:
                mBtTime.setText("选择");
                initBeginWheelYearMonthDayDialog();
                mBeginDialogAll.show(getSupportFragmentManager(), "time");
                break;
            case R.id.bt_ok:
                String causeAction = mEtCauseAction.getText().toString().trim();
                String time = mBtTime.getText().toString().trim();
                String scene = mEtScene.getText().toString().trim();
                String other = mEtOther.getText().toString().trim();
                String situation = mEtSituation.getText().toString().trim();
                String caseBrief = mEtCaseBrief.getText().toString().trim();
                String opinion = mEtOpinion.getText().toString().trim();
                if (mCbExaminationRevealed.isChecked()) {
                    sourceCaseType = "0";
                    sourceCase = "检查发现";
                } else if (mCbSuperiorSpecified.isChecked()) {
                    sourceCaseType = "1";
                    sourceCase = "上级指定";
                } else if (mCbDepartmentTransfer.isChecked()) {
                    sourceCaseType = "2";
                    sourceCase = "部门移送";
                } else if (mCbMassesReport.isChecked()) {
                    sourceCaseType = "3";
                    sourceCase = "群众举报";
                } else if (mCbMediaDisclosure.isChecked()) {
                    sourceCaseType = "4";
                    sourceCase = "媒体披露";
                } else if (mCbOther.isChecked()) {
                    sourceCaseType = "5";
                    sourceCase = other;
                } else {
                    sourceCaseType = "";
                    sourceCase = "";
                }

                if (time.equals("选择")) {
                    RxToast.warning(BaseApplication.getApplictaion(), "请选择案发时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(causeAction) && !TextUtils.isEmpty(scene)
                        && !TextUtils.isEmpty(situation) && !TextUtils.isEmpty(caseBrief)
                        && !TextUtils.isEmpty(opinion) && !TextUtils.isEmpty(sourceCase)) {
                    if (sourceCaseType.equals("5") && TextUtils.isEmpty(other)) {
                        RxToast.warning(BaseApplication.getApplictaion(), "提交内容不能有空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sourceCase = other;

                    if (mRxDialogLoading == null) {

                        mRxDialogLoading = new RxDialogLoading(FromAllCaseInAdvanceActivity.this, false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                dialogInterface.cancel();
                            }
                        });
                        mRxDialogLoading.setLoadingText("提交数据中...");
                    }
                    mRxDialogLoading.show();
                    Map<String, Object> params = new HashMap<>();
                    params.put("ay", causeAction);
                    params.put("afsj", time);
                    params.put("afdd", scene);
                    params.put("ajlylx", sourceCaseType);
                    params.put("ajly", sourceCase);
                    params.put("dsrqk", situation);
                    params.put("aqjy", caseBrief);
                    params.put("cbryj", opinion);
                    params.put("cjrid", OkingContract.CURRENTUSER.getUserid());

                    if (mUploadCaseInAdvancePresenter == null) {
                        mUploadCaseInAdvancePresenter = new UploadCaseInAdvancePresenter(new UploadCaseInAdvanceContract.View() {
                            @Override
                            public void uploadCaseInAdvanceSucc(String result) {
                                mRxDialogLoading.cancel();
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        RxToast.success(BaseApplication.getApplictaion(), "数据提交成功", Toast.LENGTH_LONG).show();
                                        mEtCauseAction.setText("");
                                        mBtTime.setText("选择");
                                        mEtScene.setText("");
                                        mEtOther.setText("");
                                        mEtSituation.setText("");
                                        mEtCaseBrief.setText("");
                                        mEtOpinion.setText("");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    RxToast.error(BaseApplication.getApplictaion(), "服务器内部错误", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void uploadCaseInAdvanceFail(Throwable ex) {
                                mRxDialogLoading.cancel();
                                RxToast.error(BaseApplication.getApplictaion(), "数据提交失败" + ex.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    mUploadCaseInAdvancePresenter.uploadCaseInAdvance(params);

                }
                break;
        }
    }

    @OnCheckedChanged({R.id.cb_examination_revealed, R.id.cb_superior_specified, R.id.cb_department_transfer, R.id.cb_masses_report, R.id.cb_media_disclosure, R.id.cb_other})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_examination_revealed:
                if (isChecked) {

                    mEtOther.setText("");
                    mEtOther.setEnabled(false);
                    mCbSuperiorSpecified.setChecked(false);
                    mCbDepartmentTransfer.setChecked(false);
                    mCbMassesReport.setChecked(false);
                    mCbMediaDisclosure.setChecked(false);
                    mCbOther.setChecked(false);
                }
                break;
            case R.id.cb_superior_specified:
                if (isChecked) {

                    mEtOther.setText("");
                    mEtOther.setEnabled(false);
                    mCbExaminationRevealed.setChecked(false);
                    mCbDepartmentTransfer.setChecked(false);
                    mCbMassesReport.setChecked(false);
                    mCbMediaDisclosure.setChecked(false);
                    mCbOther.setChecked(false);
                }

                break;
            case R.id.cb_department_transfer:
                if (isChecked) {

                    mEtOther.setText("");
                    mEtOther.setEnabled(false);
                    mCbSuperiorSpecified.setChecked(false);
                    mCbExaminationRevealed.setChecked(false);
                    mCbMassesReport.setChecked(false);
                    mCbMediaDisclosure.setChecked(false);
                    mCbOther.setChecked(false);
                }

                break;
            case R.id.cb_masses_report:
                if (isChecked) {

                    mEtOther.setText("");
                    mEtOther.setEnabled(false);
                    mCbSuperiorSpecified.setChecked(false);
                    mCbExaminationRevealed.setChecked(false);
                    mCbDepartmentTransfer.setChecked(false);
                    mCbMediaDisclosure.setChecked(false);
                    mCbOther.setChecked(false);
                }
                break;
            case R.id.cb_media_disclosure:
                if (isChecked) {

                    mEtOther.setText("");
                    mEtOther.setEnabled(false);
                    mCbSuperiorSpecified.setChecked(false);
                    mCbExaminationRevealed.setChecked(false);
                    mCbDepartmentTransfer.setChecked(false);
                    mCbMassesReport.setChecked(false);
                    mCbOther.setChecked(false);
                }

                break;
            case R.id.cb_other:
                if (isChecked) {
                    mEtOther.setText("");
                    mEtOther.setEnabled(true);
                    mCbSuperiorSpecified.setChecked(false);
                    mCbExaminationRevealed.setChecked(false);
                    mCbDepartmentTransfer.setChecked(false);
                    mCbMediaDisclosure.setChecked(false);
                    mCbMassesReport.setChecked(false);
                }
                break;
            default:
                break;
        }
    }

    private void initBeginWheelYearMonthDayDialog() {
        long tenYears = 5L * 365 * 1000 * 60 * 60 * 24L;
        mBeginDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("请选择时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("点")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis() - tenYears)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(18)
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        mBtTime.setText(sf.format(millseconds));
    }
}
