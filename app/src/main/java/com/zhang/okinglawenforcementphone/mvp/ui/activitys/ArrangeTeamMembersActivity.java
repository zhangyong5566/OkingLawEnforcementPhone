package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxDialogSure;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.EmergencyMemberAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMemberDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.ReceptionStaffBean;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.AddMemberContract;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadCanSelectMemberContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.AddMemberPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadCanSelectMemberPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.utils.ArrangeMissionPinyinComparator;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;

public class ArrangeTeamMembersActivity extends BaseActivity {

    private GreenMissionTask mGreenMissionTask;
    private Unbinder mBind;
    private int mPosition;
    private Gson mGson;
    private RxDialogLoading mRxDialogLoading;
    private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private RxDialogSure rxDialogSure;
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

    @BindView(R.id.sw)
    Switch sw;

    private boolean isChecked;
    @BindView(R.id.list_item_missionRecord)
    Button list_item_missionRecord;
    @BindView(R.id.bt_getmember)
    Button btGetmember;
    private LoadCanSelectMemberPresenter mLoadCanSelectMemberPresenter;

    private DialogUtil mButtomDialogUtil;
    private View mButtomContentView;
    private EmergencyMemberAdapter mEmergencyMemberAdapter;
    private ListView mLv_members;
    private Button mBtOkselect;
    private String mMembersid;
    private int mCheckNameSize;
    private int mCheckPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_team_members);
        mBind = ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked = b;
            }
        });
    }

    private void initData() {


    }

    private void loadNetData() {
        if (mRxDialogLoading == null) {
            mRxDialogLoading = new RxDialogLoading(this, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });
        }
        mRxDialogLoading.setLoadingText("获取数据中,请稍等...");
        mRxDialogLoading.show();

        if (mButtomContentView == null) {
            mButtomContentView = LayoutInflater.from(ArrangeTeamMembersActivity.this).inflate(R.layout.dialog_content_circle, null);

            mLv_members = mButtomContentView.findViewById(R.id.lv_members);
            mBtOkselect = mButtomContentView.findViewById(R.id.bt_okselect);
            mBtOkselect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<GreenMember> checkName = mEmergencyMemberAdapter.getCheckName();
                    String members = OkingContract.CURRENTUSER.getUserName();
                    mMembersid = "";
                    for (GreenMember m : checkName) {
                        members = members + "," + m.getUsername();
                        mMembersid = mMembersid + "," + m.getUserid();
                    }
                    tv_members.setText(members);
                    mButtomDialogUtil.cancelDialog();
                }
            });
        }
        if (mLoadCanSelectMemberPresenter == null) {
            mLoadCanSelectMemberPresenter = new LoadCanSelectMemberPresenter(new LoadCanSelectMemberContract.View() {
                @Override
                public void loadCanSelectMemberSucc(String result) {
                    mRxDialogLoading.cancel();
                    if (mGson == null) {
                        mGson = new Gson();
                    }
                    ReceptionStaffBean receptionStaff = mGson.fromJson(result, ReceptionStaffBean.class);
                    List<ReceptionStaffBean.SZJCBean> szjc = receptionStaff.getSZJC();
                    List<ReceptionStaffBean.CBRBean> cbr = receptionStaff.getCBR();
                    ArrayList<GreenMember> mbList = new ArrayList<>();
                    if (szjc != null && szjc.size() > 0) {
                        for (ReceptionStaffBean.SZJCBean szjcBean : szjc) {
                            GreenMember member = new GreenMember();
                            member.setUsername(szjcBean.getUSERNAME());
                            member.setUserid(szjcBean.getUSERID());
                            mbList.add(member);
                        }
                    }

                    if (cbr != null && cbr.size() > 0) {
                        for (ReceptionStaffBean.CBRBean cbrBean : cbr) {
                            GreenMember member = new GreenMember();
                            member.setUsername(cbrBean.getUSERNAME());
                            member.setUserid(cbrBean.getUSERID());
                            mbList.add(member);
                        }
                    }


                    if (mbList != null && mbList.size() > 0) {
                        if (mButtomDialogUtil == null) {

                            mButtomDialogUtil = new DialogUtil();
                        }
                        mButtomDialogUtil.showBottomDialog(ArrangeTeamMembersActivity.this, mButtomContentView, 400f);

                        Collections.sort(mbList, new ArrangeMissionPinyinComparator());
                        mEmergencyMemberAdapter = new EmergencyMemberAdapter(ArrangeTeamMembersActivity.this, mbList);

                        mLv_members.setAdapter(mEmergencyMemberAdapter);
                    }

                }

                @Override
                public void loadCanSelectMemberFail(Throwable ex) {
                    mRxDialogLoading.cancel();
                    if (mEmergencyMemberAdapter != null) {

                        mEmergencyMemberAdapter.notifyDataSetChanged();
                    }
                    Log.i("Oking", "获取成员失败" + ex.getMessage());
                }
            });

        }
        mLoadCanSelectMemberPresenter.loadCanSelectMember();
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
                tv_state.setText("未安排人员");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain8));
                break;
            case "3":
                tv_state.setText("已安排，待执行");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain7));
                break;
            case "4":
                tv_state.setText("巡查中");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain5));
                break;
            case "100":
                tv_state.setText("巡查结束");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain6));
                break;
            case "5":
                tv_state.setText("已上报");
                tv_state.setTextColor(BaseApplication.getApplictaion().getResources().getColor(R.color.colorMain4));
                break;
            case "9":
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
        switch (mGreenMissionTask.getStatus()) {
            case "0":

            case "1":

            case "2":
                if (mGreenMissionTask.getReceiver().equals(OkingContract.CURRENTUSER.getUserid())) {
                    //接收人是自己才能去安排人员
                    list_item_missionRecord.setEnabled(true);
                    list_item_missionRecord.setText("确认安排");
                    btGetmember.setVisibility(View.VISIBLE);
                    sw.setEnabled(false);
                } else {
                    list_item_missionRecord.setEnabled(false);
                    list_item_missionRecord.setText("等候" + mGreenMissionTask.getReceiver_name() + "安排人员");
                    btGetmember.setVisibility(View.GONE);
                    sw.setEnabled(false);
                }


                break;
            case "3":
                list_item_missionRecord.setText("开始任务");
                break;
            case "4":
                list_item_missionRecord.setText("任务日志");
                break;
            case "100":
                list_item_missionRecord.setText("任务日志");
                break;
            case "5":
                list_item_missionRecord.setText("任务日志");
                break;
            case "9":
                list_item_missionRecord.setText("任务日志");
                break;
            default:
                break;
        }


    }

    @OnClick({R.id.list_item_missionRecord, R.id.bt_getmember})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.list_item_missionRecord:
                if (!isChecked) {
                    confirmMission();
                } else {
                    mGreenMissionTask.setStatus("3");
                    GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mGreenMissionTask);
                    if (mPosition == -100) {
                        UpdateGreenMissionTaskOV updateGreenMissionTaskOV = new UpdateGreenMissionTaskOV();
                        updateGreenMissionTaskOV.setPosition(mPosition);
                        updateGreenMissionTaskOV.setMissionTask(mGreenMissionTask);
                        EventBus.getDefault().post(updateGreenMissionTaskOV);
                    } else if (mPosition != -1) {
                        UpdateGreenMissionTaskOV updateGreenMissionTaskOV = new UpdateGreenMissionTaskOV();
                        updateGreenMissionTaskOV.setPosition(mPosition);
                        updateGreenMissionTaskOV.setMissionTask(mGreenMissionTask);
                        EventBus.getDefault().post(updateGreenMissionTaskOV);
                    }
                    finish();
                }

                break;
            case R.id.bt_getmember:
                loadNetData();
                break;
            default:
                break;
        }

    }


    //确认
    private void confirmMission() {
        list_item_missionRecord.setEnabled(false);
        if (mEmergencyMemberAdapter != null) {
            final List<GreenMember> checkName = mEmergencyMemberAdapter.getCheckName();
            GreenMember greenMember = new GreenMember();
            greenMember.setUsername(OkingContract.CURRENTUSER.getUserName());
            greenMember.setUserid(OkingContract.CURRENTUSER.getUserid());
            greenMember.setPost("负责人");
            greenMember.setAccount(OkingContract.CURRENTUSER.getAcount());
            checkName.add(greenMember);
            mCheckPos = 0;
            mCheckNameSize = checkName.size();
            if (checkName != null && checkName.size() > 0) {
                for (int i = 0; i < checkName.size(); i++){
                    Observable.just(checkName.get(i))
                            .compose(RxSchedulersHelper.<GreenMember>io_main())
                            .observeOn(Schedulers.io())
                            .concatMap(new Function<GreenMember, ObservableSource<ResponseBody>>() {
                                @Override
                                public Observable<ResponseBody> apply(GreenMember greenMember) throws Exception {
                                    mCheckPos++;
                                    if (!greenMember.getUserid().equals(mGreenMissionTask.getReceiver())) {
                                        greenMember.setTaskid(mGreenMissionTask.getTaskid());
                                        greenMember.setPost("组员");
                                        greenMember.setGreenMemberId(mGreenMissionTask.getId());
                                        GreenMember unique = GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().queryBuilder().where(GreenMemberDao.Properties.GreenMemberId.eq(mGreenMissionTask.getId()), GreenMemberDao.Properties.Userid.eq(greenMember.getUserid())).unique();

                                        if (unique == null) {
                                            long insert = GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().insert(greenMember);
                                            Log.i("Oking5","队员插入成功"+insert+"-----"+greenMember.getUsername());
                                            return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                                                    .addMember(OkingContract.CURRENTUSER.getUserid(), greenMember.getTaskid(), greenMember.getUserid());
                                        } else {
                                            return Observable.error(new Throwable("该队员已经选择"));
                                        }

                                    } else {
                                        return Observable.error(new Throwable("该队员不用选择"));
                                    }

                                }

                            })
                            .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends ResponseBody>>() {
                                @Override
                                public ObservableSource<? extends ResponseBody> apply(Throwable throwable) throws Exception {
                                    if (throwable.getMessage().equals("该队员已经选择")||throwable.getMessage().equals("该队员不用选择")){
                                        return Observable.create(new ObservableOnSubscribe<ResponseBody>() {
                                            @Override
                                            public void subscribe(ObservableEmitter<ResponseBody> e) throws Exception {
                                                e.onNext(new RealResponseBody(null,null));
                                            }
                                        });
                                    }else {

                                        return Observable.error(new Throwable("NONE"));
                                    }
                                }
                            })
                            .concatMap(new Function<ResponseBody, Observable<ResponseBody>>() {


                                @Override
                                public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                                    if (mCheckPos==mCheckNameSize){
                                        return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                                                .addQRMission("updateqr", mGreenMissionTask.getTaskid(), OkingContract.CURRENTUSER.getUserid());

                                    }else {
                                        return Observable.error(new Throwable("NONE"));
                                    }

                                }
                            })
                            .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends ResponseBody>>() {
                                @Override
                                public ObservableSource<? extends ResponseBody> apply(Throwable throwable) throws Exception {
                                    if (throwable.getMessage().equals("NONE")){
                                        return Observable.create(new ObservableOnSubscribe<ResponseBody>() {
                                            @Override
                                            public void subscribe(ObservableEmitter<ResponseBody> e) throws Exception {
                                                e.onNext(new RealResponseBody(null,null));
                                            }
                                        });
                                    }else {
                                        return Observable.error(new Throwable("NONE"));
                                    }

                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<ResponseBody>() {
                                @Override
                                public void accept(ResponseBody responseBody) throws Exception {
                                    if (mCheckPos==mCheckNameSize){
                                        mGreenMissionTask.setStatus("3");
                                        GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mGreenMissionTask);
//                                                mGreenMissionTask.resetMembers();0
                                        if (list_item_missionRecord!=null){

                                            list_item_missionRecord.setEnabled(true);
                                        }
                                        RxToast.success("队员分配成功");
                                        if (mPosition == -100) {
                                            UpdateGreenMissionTaskOV updateGreenMissionTaskOV = new UpdateGreenMissionTaskOV();
                                            updateGreenMissionTaskOV.setPosition(mPosition);
                                            updateGreenMissionTaskOV.setMissionTask(mGreenMissionTask);
                                            EventBus.getDefault().post(updateGreenMissionTaskOV);
                                        } else if (mPosition != -1) {
                                            UpdateGreenMissionTaskOV updateGreenMissionTaskOV = new UpdateGreenMissionTaskOV();
                                            updateGreenMissionTaskOV.setPosition(mPosition);
                                            updateGreenMissionTaskOV.setMissionTask(mGreenMissionTask);
                                            EventBus.getDefault().post(updateGreenMissionTaskOV);
                                        }
                                        finish();
                                    }

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Log.i("Oking5","失败"+throwable.toString());
                                    if (throwable.getMessage().equals("NONE")){
                                        if (mCheckPos==mCheckNameSize){
                                            list_item_missionRecord.setEnabled(true);
                                        }
                                    }else {
                                        list_item_missionRecord.setEnabled(true);
                                    }

                                }
                            });

                }

            } else {
                RxToast.warning("没有选择队员");
            }

        } else {
            RxToast.warning("没有选择队员");
        }


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
