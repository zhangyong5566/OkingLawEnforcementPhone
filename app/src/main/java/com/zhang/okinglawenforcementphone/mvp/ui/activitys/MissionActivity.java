package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.DefaultContants;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.NewsTaskOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.mvp.contract.UpdateMissionStateContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.UpdateMissionStatePresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MissionActivity extends BaseActivity {


    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.list_item_missionTitle)
    TextInputEditText tv_taskname;

    @BindView(R.id.list_item_missionDate)
    TextInputEditText tv_entime;

    @BindView(R.id.list_item_missionState)
    TextInputEditText tv_state;

    @BindView(R.id.publisher_tv)
    TextInputEditText tv_sued_people;

    @BindView(R.id.approved_person_tv)
    TextInputEditText tv_approver_people;

    @BindView(R.id.begin_time_tv)
    TextInputEditText tv_statime;

    @BindView(R.id.mission_type_tv)
    TextInputEditText tv_type;

    @BindView(R.id.list_item_missionMember)
    TextInputEditText tv_members;

    @BindView(R.id.list_item_missionDetail)
    TextInputEditText tv_patrol_area;


    @BindView(R.id.list_item_missionRecord)
    Button list_item_missionRecord;

    private GreenMissionTask mGreenMissionTask;
    private Unbinder mBind;
    private int mPosition;
    private UpdateMissionStatePresenter mUpdateMissionStatePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        mBind = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView() {

        Intent intent = getIntent();
        mPosition = intent.getIntExtra("position", -1);

        long id = intent.getLongExtra("id", -1L);
        if (id != -1L) {
            mGreenMissionTask = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder().where(GreenMissionTaskDao.Properties.Id.eq(id)).unique();
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String groupName = mGreenMissionTask.getTask_name();
        String endTime = mDateFormat.format(mGreenMissionTask.getEnd_time());
        tv_taskname.setText(groupName);
        tv_entime.setText(endTime);
        switch (mGreenMissionTask.getStatus()) {
            case "0":

            case "1":

            case "2":
                list_item_missionRecord.setText("NONE");
                list_item_missionRecord.setEnabled(false);
                tv_state.setText("未安排人员");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain8));

                break;
            case "3":
                list_item_missionRecord.setEnabled(true);
                list_item_missionRecord.setText("开始任务");
                tv_state.setText("已安排，待执行");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain7));

                break;
            case "4":
                list_item_missionRecord.setEnabled(true);
                list_item_missionRecord.setText("任务日志");
                tv_state.setText("巡查中");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain5));

                break;
            case "100":
                list_item_missionRecord.setEnabled(true);
                list_item_missionRecord.setText("任务日志");
                tv_state.setText("巡查结束");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.law_enforcement_bt_enable));

                break;
            case "5":
                list_item_missionRecord.setEnabled(true);
                list_item_missionRecord.setText("任务日志");
                tv_state.setText("已上报");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.task_other_tx));

                break;
            case "9":
                list_item_missionRecord.setEnabled(true);
                list_item_missionRecord.setText("任务日志");
                tv_state.setText("退回修改");
                tv_state.setTextColor(Color.GRAY);

                break;
            default:
                break;

        }


        tv_sued_people.setText(mGreenMissionTask.getPublisher_name());
        tv_approver_people.setText(mGreenMissionTask.getApproved_person_name());
        tv_statime.setText(mDateFormat.format(mGreenMissionTask.getBegin_time()));
        tv_type.setText(mGreenMissionTask.getTypename());

        String memberStr = "";
        //清除dao缓存
        mGreenMissionTask.resetMembers();
        if (mGreenMissionTask.getMembers() != null) {
            for (int j = 0; j < mGreenMissionTask.getMembers().size(); j++) {
                memberStr += mGreenMissionTask.getMembers().get(j).getUsername() + ",";
            }
        }
        if (!"".equals(memberStr)) {
            memberStr = memberStr.substring(0, memberStr.length() - 1);
        }

        tv_members.setText(memberStr);

        if (mGreenMissionTask.getRwqyms() != null) {
            tv_patrol_area.setText(mGreenMissionTask.getRwqyms());
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent1(UpdateGreenMissionTaskOV event) {

        switch (event.getMissionTask().getStatus()) {
            case "0":

            case "1":

            case "2":

                list_item_missionRecord.setText("NONE");
                list_item_missionRecord.setEnabled(false);
                tv_state.setText("未安排人员");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain8));

                break;
            case "3":
                list_item_missionRecord.setEnabled(true);
                list_item_missionRecord.setText("开始任务");
                tv_state.setText("已安排，待执行");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain7));

                break;
            case "4":
                list_item_missionRecord.setEnabled(true);
                list_item_missionRecord.setText("任务日志");
                tv_state.setText("巡查中");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain5));

                break;
            case "100":
                list_item_missionRecord.setEnabled(true);
                list_item_missionRecord.setText("任务日志");
                tv_state.setText("巡查结束");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.law_enforcement_bt_enable));

                break;
            case "5":
                list_item_missionRecord.setEnabled(true);
                list_item_missionRecord.setText("任务日志");
                tv_state.setText("已上报");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.task_other_tx));

                break;
            case "9":
                list_item_missionRecord.setEnabled(true);
                list_item_missionRecord.setText("任务日志");
                tv_state.setText("退回修改");
                tv_state.setTextColor(Color.GRAY);

                break;
            default:
                break;
        }

    }

    private void initData() {


    }

    @OnClick(R.id.list_item_missionRecord)
    public void onClick(View view) {
        if ("开始任务".equals(list_item_missionRecord.getText().toString())) {


            if (mGreenMissionTask.getBegin_time() > System.currentTimeMillis()) {
                RxToast.warning("未到达任务开始时间，不能开始任务！");
                return;
            }

            Calendar c = Calendar.getInstance();
            c.setTime(new Date(mGreenMissionTask.getEnd_time()));
            if (c.getTime().getTime() < System.currentTimeMillis()) {
                RxToast.warning("超出任务的预计结束时间，不能开始任务！");
                return;
            }

            if (DefaultContants.ISHTTPLOGIN) {
                if (mUpdateMissionStatePresenter ==null){
                    mUpdateMissionStatePresenter = new UpdateMissionStatePresenter(new UpdateMissionStateContract.View() {
                        @Override
                        public void updateMissionStateSucc(String result) {
                            try {
                                JSONObject object = new JSONObject(result);
                                int code = object.getInt("code");
                                if (code == 0) {
                                    mGreenMissionTask.setExecute_start_time(System.currentTimeMillis());
                                    mGreenMissionTask.setStatus("4");
                                    GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mGreenMissionTask);
                                    list_item_missionRecord.setText("任务日志");
                                    tv_state.setText("巡查中");
                                    tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain5));
                                    if (mPosition != -1) {
                                        UpdateGreenMissionTaskOV updateGreenMissionTaskOV = new UpdateGreenMissionTaskOV();
                                        updateGreenMissionTaskOV.setPosition(mPosition);
                                        updateGreenMissionTaskOV.setMissionTask(mGreenMissionTask);
                                        EventBus.getDefault().post(updateGreenMissionTaskOV);
                                    }

                                } else {
                                    RxToast.error(object.getString("msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void updateMissionStateFail(Throwable ex) {
                            mGreenMissionTask.setExecute_start_time(System.currentTimeMillis());
                            mGreenMissionTask.setStatus("4");
                            GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mGreenMissionTask);
                            list_item_missionRecord.setText("任务日志");
                            tv_state.setText("巡查中");
                            tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain5));

                        }
                    });

                }
                mUpdateMissionStatePresenter.updateMissionState(mGreenMissionTask.getTaskid(), OkingContract.SDF.format(System.currentTimeMillis())
                        , "", 4);

                EventBus.getDefault().post(new NewsTaskOV(0,mGreenMissionTask));
            } else {

                mGreenMissionTask.setExecute_start_time(System.currentTimeMillis());
                mGreenMissionTask.setStatus("4");
                GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mGreenMissionTask);

            }

        } else if ("任务日志".equals(list_item_missionRecord.getText().toString())) {
            Intent intent = new Intent(MissionActivity.this, MissionRecorActivity.class);
            intent.putExtra("id", mGreenMissionTask.getId());
            intent.putExtra("taskId", mGreenMissionTask.getTaskid());
            startActivity(intent);

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        EventBus.getDefault().unregister(this);
    }


}
