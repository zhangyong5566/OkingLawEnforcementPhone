package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.InspectTaskBean;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.mvp.contract.TaskBackContract;
import com.zhang.okinglawenforcementphone.mvp.contract.TaskReviewContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.TaskBackPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.TaskReviewPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 审核
 */
public class AuditActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_item_missionTitle)
    TextInputEditText mListItemMissionTitle;
    @BindView(R.id.list_item_missionState)
    TextInputEditText mListItemMissionState;
    @BindView(R.id.publisher_tv)
    TextInputEditText mPublisherTv;
    @BindView(R.id.approved_person_tv)
    TextInputEditText mApprovedPersonTv;
    @BindView(R.id.begin_time_tv)
    TextInputEditText mBeginTimeTv;
    @BindView(R.id.list_endDate)
    TextInputEditText mEtEndTime;
    @BindView(R.id.mission_type_tv)
    TextInputEditText mMissionTypeTv;
    @BindView(R.id.list_item_missionDetail)
    TextInputEditText tvPatrolAarea;
    @BindView(R.id.ed_approval_opinions)
    TextInputEditText mEdApprovalOpinions;
    @BindView(R.id.bt_ok)
    Button mBtOk;
    @BindView(R.id.bt_modify)
    Button mBtModify;
    private Unbinder mBind;
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private TaskReviewPresenter mTaskReviewPresenter;
    private GreenMissionTask mUnique;
    private RxDialogLoading mRxDialogLoading;
    private int mPosition;
    private TaskBackPresenter mTaskBackPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audit);
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
        Intent intent = getIntent();
        mPosition = intent.getIntExtra("position", -1);
        long id = intent.getLongExtra("id", -1L);
        if (id != -1L) {
            mUnique = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                    .queryBuilder()
                    .where(GreenMissionTaskDao.Properties.Id.eq(id)).unique();
            mListItemMissionTitle.setText(mUnique.getTask_name());

            switch (mUnique.getStatus()) {
                case "0":

                case "1":
                    mListItemMissionState.setText("待审核");
                    mListItemMissionState.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain8));
                    break;
                case "2":
                    mListItemMissionState.setText("未安排人员");
                    mListItemMissionState.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain8));
                    break;
                case "3":
                    mListItemMissionState.setText("已安排，待执行");
                    mListItemMissionState.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain7));
                    break;
                case "4":
                    mListItemMissionState.setText("巡查中");
                    mListItemMissionState.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain5));
                    break;
                case "100":
                    mListItemMissionState.setText("巡查结束");
                    mListItemMissionState.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain6));
                    break;
                case "5":
                    mListItemMissionState.setText("已上报");
                    mListItemMissionState.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain4));
                    break;
                case "9":
                    mListItemMissionState.setText("退回修改");
                    mListItemMissionState.setTextColor(Color.GRAY);
                    break;
                default:
                    break;

            }
            mPublisherTv.setText(mUnique.getPublisher_name());
            mApprovedPersonTv.setText(mUnique.getApproved_person_name());
            mBeginTimeTv.setText(mDateFormat.format(mUnique.getBegin_time()));
            mEtEndTime.setText(mDateFormat.format(mUnique.getEnd_time()));
            mMissionTypeTv.setText(mUnique.getTypename());
            tvPatrolAarea.setText(mUnique.getRwqyms());
            if (mUnique.getStatus().equals("7")) {
                mEdApprovalOpinions.setText(mUnique.getSpyj());
                mEdApprovalOpinions.setEnabled(false);
                mBtOk.setText("重新发布");
                mBtModify.setVisibility(View.GONE);
            } else {
                if (mUnique.getApproved_person().equals(OkingContract.CURRENTUSER.getUserid())) {
                    //审批人是自己

                } else {
                    mBtOk.setEnabled(false);
                    mEdApprovalOpinions.setEnabled(false);
                    mBtOk.setText("等候" + mUnique.getApproved_person_name() + "审批");
                    mBtModify.setVisibility(View.GONE);
                }
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @OnClick({R.id.bt_ok, R.id.bt_modify})
    public void onViewClicked(View view) {
        final String approvalOpinions = mEdApprovalOpinions.getText().toString().trim();
        switch (view.getId()) {

            case R.id.bt_ok:
                if (mBtOk.getText().toString().trim().equals("重新发布")) {
                    Intent intent = new Intent(AuditActivity.this, PatrolsToReleaseActivity.class);
                    InspectTaskBean fromParcel = InspectTaskBean.CREATOR.createFromParcel(Parcel.obtain());
                    fromParcel.setAPPROVED_PERSON(mUnique.getApproved_person());
                    fromParcel.setBEGIN_TIME(mUnique.getBegin_time());
                    fromParcel.setCREATE_TIME(mUnique.getCreate_time());
                    fromParcel.setDELIVERY_TIME(mUnique.getDelivery_time());
                    fromParcel.setDEPT_ID(OkingContract.CURRENTUSER.getDept_id());
                    fromParcel.setEND_TIME(mUnique.getEnd_time());
                    fromParcel.setFBDW(mUnique.getFbdw());
                    fromParcel.setFBR(mUnique.getFbr());
                    fromParcel.setFBRID(OkingContract.CURRENTUSER.getUserid());
                    String taskid = mUnique.getTaskid();

                    fromParcel.setID(taskid);
                    if ("0".equals(mUnique.getJjcd())) {

                        fromParcel.setJJCD("特急");
                    } else if ("1".equals(mUnique.getJjcd())) {
                        fromParcel.setJJCD("紧急");
                    } else if ("2".equals(mUnique.getJjcd())) {
                        fromParcel.setJJCD("一般");
                    }

                    fromParcel.setJSDW(mUnique.getJsdw());
                    fromParcel.setJSR(mUnique.getJsr());
                    fromParcel.setPUBLISHER(mUnique.getPublisher());
                    fromParcel.setRECEIVER(mUnique.getReceiver());

                    if ("0".equals(mUnique.getTypeoftask())) {

                        fromParcel.setRWLX("日常执法");
                    } else if ("1".equals(mUnique.getTask_type())) {
                        fromParcel.setRWLX("联合执法");
                    } else if ("2".equals(mUnique.getTask_type())) {
                        fromParcel.setRWLX("专项执法");
                    } else if ("3".equals(mUnique.getTask_type())) {
                        fromParcel.setRWLX("目标核查");
                    }

                    if ("0".equals(mUnique.getRwly())) {

                        fromParcel.setRWLY("上级交办");
                    } else if ("1".equals(mUnique.getRwly())) {
                        fromParcel.setRWLY("部门移送");
                    } else if ("2".equals(mUnique.getRwly())) {
                        fromParcel.setRWLY("系统报警");
                    } else if ("3".equals(mUnique.getRwly())) {
                        fromParcel.setRWLY("日常巡查");
                    } else if ("4".equals(mUnique.getRwly())) {
                        fromParcel.setRWLY("媒体披露");
                    } else if ("5".equals(mUnique.getRwly())) {
                        fromParcel.setRWLY("群众举报");
                    }

                    fromParcel.setRWMC(mUnique.getTask_name());
                    fromParcel.setRWMS(mUnique.getTask_content());
                    fromParcel.setRWQYMS(mUnique.getRwqyms());
                    fromParcel.setSPR(mUnique.getApproved_person_name());
                    fromParcel.setSPRID(mUnique.getApproved_person());
                    if ("0".equals(mUnique.getStatus())) {
                        fromParcel.setSTATUS("未发布");
                    } else if ("1".equals(mUnique.getStatus())) {
                        fromParcel.setSTATUS("已发布待审核");
                    } else if ("2".equals(mUnique.getStatus())) {
                        fromParcel.setSTATUS("审核通过");
                    } else if ("3".equals(mUnique.getStatus())) {
                        fromParcel.setSTATUS("接收并已分配队员");
                    } else if ("4".equals(mUnique.getStatus())) {
                        fromParcel.setSTATUS("任务开始");
                    } else if ("5".equals(mUnique.getStatus())) {
                        fromParcel.setSTATUS("任务完成");
                    } else if ("7".equals(mUnique.getStatus())) {
                        fromParcel.setSTATUS("退回修改");
                    }


                    if ("0".equals(mUnique.getTypeoftask())) {
                        fromParcel.setTYPEOFTASK("河道管理");
                    } else if ("1".equals(mUnique.getTypeoftask())) {
                        fromParcel.setTYPEOFTASK("河道采砂");
                    } else if ("2".equals(mUnique.getTypeoftask())) {
                        fromParcel.setTYPEOFTASK("水资源管理");
                    } else if ("3".equals(mUnique.getTypeoftask())) {
                        fromParcel.setTYPEOFTASK("水土保持管理");
                    } else if ("4".equals(mUnique.getTypeoftask())) {
                        fromParcel.setTYPEOFTASK("水利工程管理");
                    }

                    fromParcel.setTASK_NAME(mUnique.getTask_name());

                    intent.putExtra("inspectTaskBean", fromParcel);
                    intent.putExtra("position", mPosition);
                    intent.putExtra("id", mUnique.getId());
                    startActivity(intent);
                    finish();
                } else {
                    if (!TextUtils.isEmpty(approvalOpinions)) {
                        if (mTaskReviewPresenter == null) {
                            mTaskReviewPresenter = new TaskReviewPresenter(new TaskReviewContract.View() {
                                @Override
                                public void taskReviewSucc(String result) {
                                    Log.i("Oking", "成功:" + result);
                                    mRxDialogLoading.cancel();
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        String status = jsonObject.getString("status");
                                        String msg = jsonObject.getString("msg");

                                        if (status.equals("1")) {
                                            RxToast.success(msg);
                                            UpdateGreenMissionTaskOV updateGreenMissionTaskOV = new UpdateGreenMissionTaskOV();
                                            updateGreenMissionTaskOV.setType(100);
                                            updateGreenMissionTaskOV.setPosition(mPosition);

                                            EventBus.getDefault().post(updateGreenMissionTaskOV);
                                            GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().delete(mUnique);
                                            finish();

                                        } else {
                                            RxToast.error(msg);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void taskReviewFail(Throwable ex) {
                                    Log.i("Oking", "失败:" + ex.toString());
                                    mRxDialogLoading.cancel();
                                }
                            });
                        }
                        Map<String, Object> params = new HashMap<>();
                        params.put("lx", "updatesp");
//                    params.put("coordinateJson", "");
                        params.put("rwmc", mUnique.getTask_name());
                        params.put("fbr", mUnique.getPublisher_name());
                        params.put("fbdw", mUnique.getFbdw());
                        params.put("jsr", mUnique.getJsr());
                        params.put("jsdw", mUnique.getJsdw());
                        params.put("rwms", mUnique.getTask_content());
                        params.put("rwqyms", mUnique.getTask_area());
                        params.put("sjq", mBeginTimeTv.getText().toString().trim());
                        params.put("sjz", mEtEndTime.getText().toString().trim());
                        params.put("rwlx", mUnique.getTask_type());
                        params.put("jjcd", mUnique.getJjcd());
                        params.put("zt", "2");
                        params.put("rwly", mUnique.getRwly());
                        params.put("fbrid", mUnique.getPublisher());
                        params.put("jsrid", mUnique.getReceiver());
                        params.put("sprid", mUnique.getApproved_person());
                        params.put("spr", mUnique.getApproved_person_name());
                        params.put("deptid", OkingContract.CURRENTUSER.getDept_id());
                        params.put("id", mUnique.getTaskid());
                        params.put("spyj", approvalOpinions);
                        params.put("typeoftask", mUnique.getTypeoftask());
                        if (mRxDialogLoading == null) {
                            mRxDialogLoading = new RxDialogLoading(AuditActivity.this, false, new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.cancel();
                                }
                            });
                            mRxDialogLoading.setLoadingText("正在提交数据请稍候...");
                        }

                        mRxDialogLoading.show();
                        mTaskReviewPresenter.taskReview(params);
                    } else {
                        RxToast.warning("请填入审批意见");
                    }
                }


                break;
            case R.id.bt_modify:

                if (!TextUtils.isEmpty(approvalOpinions)) {
                    if (mTaskBackPresenter == null) {
                        mTaskBackPresenter = new TaskBackPresenter(new TaskBackContract.View() {
                            @Override
                            public void taskBackSucc(String result) {
                                mRxDialogLoading.cancel();
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String status = jsonObject.getString("status");
                                    String msg = jsonObject.getString("msg");

                                    if (status.equals("1")) {
                                        RxToast.success("退回成功");
                                        GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().delete(mUnique);
                                        if (mPosition != -1) {
                                            UpdateGreenMissionTaskOV updateGreenMissionTaskOV = new UpdateGreenMissionTaskOV();
                                            updateGreenMissionTaskOV.setType(100);
                                            updateGreenMissionTaskOV.setPosition(mPosition);
                                            EventBus.getDefault().post(updateGreenMissionTaskOV);
                                        }
                                        finish();

                                    } else {
                                        RxToast.error("退回失败！");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void taskBackFail(Throwable ex) {
                                mRxDialogLoading.cancel();
                            }
                        });
                    }
                    Map<String, Object> params = new HashMap<>();
                    params.put("lx", "updatesp");
//                    params.put("coordinateJson", "");
                    params.put("rwmc", mUnique.getTask_name());
                    params.put("fbr", mUnique.getPublisher_name());
                    params.put("fbdw", mUnique.getFbdw());
                    params.put("jsr", mUnique.getJsr());
                    params.put("jsdw", mUnique.getJsdw());
                    params.put("rwms", mUnique.getTask_content());
                    params.put("rwqyms", mUnique.getTask_area());
                    params.put("sjq", mBeginTimeTv.getText().toString().trim());
                    params.put("sjz", mEtEndTime.getText().toString().trim());
                    params.put("rwlx", mUnique.getTask_type());
                    params.put("jjcd", mUnique.getJjcd());
                    params.put("zt", "7");
                    params.put("rwly", mUnique.getRwly());
                    params.put("fbrid", mUnique.getPublisher());
                    params.put("jsrid", mUnique.getReceiver());
                    params.put("sprid", mUnique.getApproved_person());
                    params.put("spr", mUnique.getApproved_person_name());
                    params.put("deptid", OkingContract.CURRENTUSER.getDept_id());
                    params.put("id", mUnique.getTaskid());
                    params.put("spyj", approvalOpinions);
                    params.put("typeoftask", mUnique.getTypeoftask());

                    if (mRxDialogLoading == null) {
                        mRxDialogLoading = new RxDialogLoading(AuditActivity.this, false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.cancel();
                            }
                        });
                        mRxDialogLoading.setLoadingText("正在提交数据请稍候...");
                    }

                    mRxDialogLoading.show();
                    mTaskBackPresenter.taskBack(params);


                } else {
                    RxToast.warning("请填入审批意见");
                }


                break;
            default:
                break;
        }
    }
}
