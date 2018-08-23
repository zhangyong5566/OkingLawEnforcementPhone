package com.zhang.okinglawenforcementphone.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.DefaultContants;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.AppUtil;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.baselib.utils.PermissionUtil;
import com.zhang.baselib.utils.Util;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.AnswBean;
import com.zhang.okinglawenforcementphone.beans.Level0Item;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.ProblemBean;
import com.zhang.okinglawenforcementphone.beans.SourceArrayOV;
import com.zhang.okinglawenforcementphone.beans.WrittenItemBean;
import com.zhang.okinglawenforcementphone.beans.WrittenRecordLevel0;
import com.zhang.okinglawenforcementphone.db.LawDao;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadRecordContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadRecordPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;
import com.zhang.okinglawenforcementphone.views.MyRecycelerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/27/027.
 */

public class ExpandableWrittenRecordAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> implements OnDateSetListener, CompoundButton.OnCheckedChangeListener {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;
    public static final int TYPE_LEVEL_3 = 3;
    public static final int TYPE_LEVEL_4 = 4;
    private int[] countyIds = {R.array.lv_county_01, R.array.lv_county_02, R.array.lv_county_03
            , R.array.lv_county_04, R.array.lv_county_05, 0, R.array.lv_county_07
            , R.array.lv_county_08, R.array.lv_county_09, R.array.lv_county_10
            , R.array.lv_county_11, R.array.lv_county_12, R.array.lv_county_13
            , R.array.lv_county_14, R.array.lv_county_15, 0, 0, R.array.lv_county_18
            , R.array.lv_county_19, R.array.lv_county_20, R.array.lv_county_21};
    private TextView mSpCity;
    private TextView mSpCounty;
    private TextView mSpCity2;
    private TextView mSpCounty2;
    private TextView mSpType;
    private RxDialogLoading mRxDialogLoading;
    private String mCasetype = "1";
    private ArrayList<ProblemBean> mProblemContent;
    private AskAdapter mAskAdapter;
    private RecyclerView mLvAsk;
    private String[] mCaseTypeArray;
    private String mSelectedItem;
    private BaseActivity activity;
    private EditText mEtAddrdetail1;
    private TextInputEditText mTetAskPerson;
    private TextInputEditText mTetAskEnforcementNumber;             //询问人执法编号
    private TextInputEditText mTetRecorder;                         //记录人
    private TextInputEditText mTetRecorderNumber;               //记录人执法编号
    private RadioButton mRbIllegalPerson;                   //违法行为人
    private RadioButton mRbVictim;                          //受害人
    private RadioButton mRbOther;                           //第三方
    private TextInputEditText mTetAskingPeople;
    private TextInputEditText mTetAskingIdcard;
    private TextInputEditText mTetAskingPosition;
    private TextInputEditText mTetAskingWorkUnits;
    private EditText mEtAddrdetail2;
    private EditText mEtEnforcementName;
    private Button mBtSelectBegintime;
    private Button mBtSelectEndtime;
    private TimePickerDialog mBeginDialogAll;
    private TimePickerDialog mEndDialogAll;
    private long mBeginMillseconds = 0;
    private TextInputEditText mTetContent;
    private ArrayList<AnswBean> mAnswBeans;
    private UploadRecordPresenter mUploadRecordPresenter;
    private Gson mGson;
    private String mBxwrtype = "0";                                   //被询问人类型
    private String mIllegalPerson;
    private DialogUtil mDialogUtil;
    private View mButtonDailog;
    private TextView mTv_title;
    private ArrayList<SourceArrayOV> mCaseTypeArrayOVS;
    private ArrayList<SourceArrayOV> mCountyArrayOVS;
    private ArrayList<SourceArrayOV> mCityArrayOVS;
    private SourceArrayRecyAdapter mSourceArrayRecyAdapter;

    public ExpandableWrittenRecordAdapter(List<MultiItemEntity> data, BaseActivity activity) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.activity_mission_recor_level0);
        addItemType(TYPE_LEVEL_1, R.layout.written_record_info1);
        addItemType(TYPE_LEVEL_2, R.layout.written_record_info2);
        addItemType(TYPE_LEVEL_3, R.layout.written_record_info3);
        addItemType(TYPE_LEVEL_4, R.layout.written_record_info4);
        this.activity = activity;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final WrittenRecordLevel0 lv0 = (WrittenRecordLevel0) item;

                helper.setText(R.id.title, lv0.subTitle);
                helper.itemView.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        int adapterPosition = helper.getAdapterPosition();
                        List<WrittenItemBean> subItems = lv0.getSubItems();
                        int subItemType = subItems.get(0).getItemType();

                        if (lv0.isExpanded()) {
                            collapse(adapterPosition);
                            //bug，解决下面的spinner不隐藏

                            if (subItemType == 3 && mSpCity2 != null && mSpCounty2 != null) {
                                mSpCity2.setVisibility(View.INVISIBLE);
                                mSpCounty2.setVisibility(View.INVISIBLE);
                            }

                        } else {
                            expand(adapterPosition);
                            if (subItemType == 3 && mSpCity2 != null && mSpCounty2 != null) {
                                mSpCity2.setVisibility(View.VISIBLE);
                                mSpCounty2.setVisibility(View.VISIBLE);
                            }
                        }


                    }

                });
                break;
            case TYPE_LEVEL_1:
                if (mTetContent == null) {

                    mTetContent = helper.getView(R.id.tet_content);
                }
                if (mBtSelectBegintime == null) {
                    mBtSelectBegintime = helper.getView(R.id.bt_select_begintime);
                    mBtSelectBegintime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mBeginDialogAll == null) {
                                initBeginWheelYearMonthDayDialog();
                            }
                            mBtSelectEndtime.setText("选择");
                            mBeginDialogAll.show(activity.getSupportFragmentManager(), "beginTime");
                        }
                    });
                }

                if (mBtSelectEndtime == null) {
                    mBtSelectEndtime = helper.getView(R.id.bt_select_endtime);
                    mBtSelectEndtime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mEndDialogAll == null) {

                                initEndWheelYearMonthDayDialog();
                            }
                            mEndDialogAll.show(activity.getSupportFragmentManager(), "endTime");
                        }
                    });
                }
                if (mDialogUtil == null) {
                    mDialogUtil = new DialogUtil();
                    mButtonDailog = View.inflate(BaseApplication.getApplictaion(), R.layout.maptask_dialog, null);
                    mTv_title = mButtonDailog.findViewById(R.id.tv_title);
                    RecyclerView recyList = mButtonDailog.findViewById(R.id.recy_task);
                    recyList.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
                    recyList.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 3, Color.TRANSPARENT));
                    mSourceArrayRecyAdapter = new SourceArrayRecyAdapter(R.layout.source_item, null);
                    mSourceArrayRecyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
                    recyList.setAdapter(mSourceArrayRecyAdapter);
                    mSourceArrayRecyAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            List<SourceArrayOV> datas = adapter.getData();
                            SourceArrayOV sourceArrayOV = datas.get(position);
                            switch (sourceArrayOV.getType()) {
                                case 0:
                                    mSelectedItem = sourceArrayOV.getSource();
                                    if (mSelectedItem.equals("无河道采砂许可证案")) {
                                        mCasetype = "1";
                                    } else if (mSelectedItem.equals("水土保持方案违法案")) {
                                        mCasetype = "2";
                                    }
                                    if (mLvAsk != null) {
                                        mProblemContent = LawDao.getProblemContent(mSelectedItem);
                                        if (mAskAdapter == null) {
                                            mAskAdapter = new AskAdapter(R.layout.survey_ask_item, mProblemContent);
                                            mLvAsk.setAdapter(mAskAdapter);
                                        } else {
                                            mAskAdapter.setNewData(mProblemContent);

                                        }
                                    }
                                    mSpType.setText(sourceArrayOV.getSource());
                                    break;
                                case 1:
                                    int countyId = countyIds[position];
                                    if (countyId == 0) {
                                        String[] countyArray = {"无"};
                                        mCountyArrayOVS = new ArrayList<>();
                                        for (String s : countyArray) {
                                            SourceArrayOV sourceArrayOV1 = new SourceArrayOV();
                                            sourceArrayOV1.setType(2);
                                            sourceArrayOV1.setSource(s);
                                            mCountyArrayOVS.add(sourceArrayOV1);
                                        }
                                    } else {
                                        String[] countyArray = BaseApplication.getApplictaion().getResources().getStringArray(countyIds[position]);
                                        mCountyArrayOVS = new ArrayList<>();
                                        for (String s : countyArray) {
                                            SourceArrayOV sourceArrayOV1 = new SourceArrayOV();
                                            sourceArrayOV1.setType(2);
                                            sourceArrayOV1.setSource(s);
                                            mCountyArrayOVS.add(sourceArrayOV1);
                                        }
                                    }
                                    mSpCity.setText(sourceArrayOV.getSource());
                                    break;
                                case 2:


                                    mSpCounty.setText(sourceArrayOV.getSource());
                                    break;
                                case 3:
                                    int countyId2 = countyIds[position];
                                    if (countyId2 == 0) {
                                        String[] countyArray = {"无"};
                                        mCountyArrayOVS = new ArrayList<>();
                                        for (String s : countyArray) {
                                            SourceArrayOV sourceArrayOV1 = new SourceArrayOV();
                                            sourceArrayOV1.setType(4);
                                            sourceArrayOV1.setSource(s);
                                            mCountyArrayOVS.add(sourceArrayOV1);
                                        }
                                    } else {
                                        String[] countyArray = BaseApplication.getApplictaion().getResources().getStringArray(countyIds[position]);
                                        mCountyArrayOVS = new ArrayList<>();
                                        for (String s : countyArray) {
                                            SourceArrayOV sourceArrayOV1 = new SourceArrayOV();
                                            sourceArrayOV1.setType(4);
                                            sourceArrayOV1.setSource(s);
                                            mCountyArrayOVS.add(sourceArrayOV1);
                                        }
                                    }
                                    mSpCity2.setText(sourceArrayOV.getSource());
                                    break;
                                case 4:
                                    mSpCounty2.setText(sourceArrayOV.getSource());
                                    break;
                                default:
                                    break;
                            }
                            mDialogUtil.cancelDialog();
                        }
                    });
                }

                if (mSpType == null) {

                    mSpType = helper.getView(R.id.sp_type);
                    mCaseTypeArray = BaseApplication.getApplictaion().getResources().getStringArray(R.array.spinner_case_type);
                    mCaseTypeArrayOVS = new ArrayList<>();
                    for (String s : mCaseTypeArray) {
                        SourceArrayOV sourceArrayOV = new SourceArrayOV();
                        sourceArrayOV.setType(0);
                        sourceArrayOV.setSource(s);
                        mCaseTypeArrayOVS.add(sourceArrayOV);
                    }

                    mSpType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTv_title.setText("案件类型");
                            mSourceArrayRecyAdapter.setNewData(mCaseTypeArrayOVS);
                            mDialogUtil.showBottomDialog(activity, mButtonDailog, 300);
                        }
                    });

                }
                break;
            case TYPE_LEVEL_2:
                if (mSpCity == null && mSpCounty == null) {
                    mSpCity = helper.getView(R.id.sp_city);
                    mSpCounty = helper.getView(R.id.sp_county);

                    final String[] cityArray = BaseApplication.getApplictaion().getResources().getStringArray(R.array.lv_city);
                    mCityArrayOVS = new ArrayList<>();
                    for (String s : cityArray) {
                        SourceArrayOV sourceArrayOV = new SourceArrayOV();
                        sourceArrayOV.setType(1);
                        sourceArrayOV.setSource(s);
                        mCityArrayOVS.add(sourceArrayOV);
                    }
                    mSpCity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTv_title.setText("选择城市");
                            mSpCounty.setText("*请选择");
                            mSourceArrayRecyAdapter.setNewData(mCityArrayOVS);
                            mDialogUtil.showBottomDialog(activity, mButtonDailog, 300f);
                        }
                    });

                    mSpCounty.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTv_title.setText("选择县区");
                            mSourceArrayRecyAdapter.setNewData(mCountyArrayOVS);
                            mDialogUtil.showBottomDialog(activity, mButtonDailog, 300f);
                        }
                    });
                }
                if (mEtAddrdetail1 == null) {
                    mEtAddrdetail1 = helper.getView(R.id.et_addrdetail);
                }

                if (mTetAskPerson == null) {
                    mTetAskPerson = helper.getView(R.id.tet_ask_person);
                }

                if (mTetAskEnforcementNumber == null) {
                    mTetAskEnforcementNumber = helper.getView(R.id.tet_ask_enforcement_number);
                }

                if (mTetRecorder == null) {
                    mTetRecorder = helper.getView(R.id.tet_recorder);
                }

                if (mTetRecorderNumber == null) {
                    mTetRecorderNumber = helper.getView(R.id.tet_recorder_number);
                }


                break;
            case TYPE_LEVEL_3:
                if (mSpCity2 == null && mSpCounty2 == null) {
                    mSpCity2 = helper.getView(R.id.sp_city2);
                    mSpCounty2 = helper.getView(R.id.sp_county2);
                    final String[] cityArray = BaseApplication.getApplictaion().getResources().getStringArray(R.array.lv_city);

                    mCityArrayOVS = new ArrayList<>();
                    for (String s : cityArray) {
                        SourceArrayOV sourceArrayOV = new SourceArrayOV();
                        sourceArrayOV.setType(3);
                        sourceArrayOV.setSource(s);
                        mCityArrayOVS.add(sourceArrayOV);
                    }
                    mSpCity2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTv_title.setText("选择城市");
                            mSpCounty2.setText("*请选择");
                            mSourceArrayRecyAdapter.setNewData(mCityArrayOVS);
                            mDialogUtil.showBottomDialog(activity, mButtonDailog, 300f);
                        }
                    });

                    mSpCounty2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTv_title.setText("选择县区");
                            mSourceArrayRecyAdapter.setNewData(mCountyArrayOVS);
                            mDialogUtil.showBottomDialog(activity, mButtonDailog, 300f);
                        }
                    });

                }

                if (mRbIllegalPerson == null) {
                    mRbIllegalPerson = helper.getView(R.id.rb_illegal_person);
                    mRbIllegalPerson.setOnCheckedChangeListener(this);
                }

                if (mRbVictim == null) {
                    mRbVictim = helper.getView(R.id.rb_victim);
                    mRbVictim.setOnCheckedChangeListener(this);
                }

                if (mRbVictim == null) {
                    mRbOther = helper.getView(R.id.rb_other);
                    mRbOther.setOnCheckedChangeListener(this);
                }

                if (mTetAskingPeople == null) {
                    mTetAskingPeople = helper.getView(R.id.tet_asking_people);
                }


                if (mTetAskingIdcard == null) {
                    mTetAskingIdcard = helper.getView(R.id.tet_asking_idcard);
                }

                if (mTetAskingPosition == null) {
                    mTetAskingPosition = helper.getView(R.id.tet_asking_position);
                }

                if (mTetAskingWorkUnits == null) {
                    mTetAskingWorkUnits = helper.getView(R.id.tet_asking_work_units);
                }

                if (mEtAddrdetail2 == null) {
                    mEtAddrdetail2 = helper.getView(R.id.et_addrdetail2);
                }


                break;
            case TYPE_LEVEL_4:
                if (mLvAsk == null) {
                    mLvAsk = helper.getView(R.id.lv_ask);
                    mLvAsk.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
//                    mLvAsk.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 4, BaseApplication.getApplictaion().getResources().getColor(R.color.task_other_tx)));

                    mProblemContent = LawDao.getProblemContent(mSelectedItem);
                    if (mAskAdapter == null) {
                        mAskAdapter = new AskAdapter(R.layout.survey_ask_item, mProblemContent);
                        mLvAsk.setAdapter(mAskAdapter);
                    } else {
                        mAskAdapter.setNewData(mProblemContent);

                    }

                }

                if (mEtEnforcementName == null) {
                    mEtEnforcementName = helper.getView(R.id.et_enforcement_name);
                }


                break;
            default:
                break;
        }
    }

    private void initEndWheelYearMonthDayDialog() {
        long tenYears = 5L * 365 * 1000 * 60 * 60 * 24L;

        if (mBeginMillseconds == 0) {
            mBeginMillseconds = System.currentTimeMillis();
        }
        mEndDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("请选择结束时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("点")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(mBeginMillseconds)
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(BaseApplication.getApplictaion().getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(BaseApplication.getApplictaion().getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(BaseApplication.getApplictaion().getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(18)
                .build();

    }

    private void initBeginWheelYearMonthDayDialog() {
        long tenYears = 5L * 365 * 1000 * 60 * 60 * 24L;
        mBeginDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确认")
                .setTitleStringId("请选择开始时间")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("点")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(BaseApplication.getApplictaion().getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(BaseApplication.getApplictaion().getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(BaseApplication.getApplictaion().getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(18)
                .build();

    }


    public String getCasetypeName() {

        return mSelectedItem;
    }

    public String getCasetype() {

        return mCasetype;
    }

    public void notyAskData(ArrayList<ProblemBean> problemContent) {
        if (mAskAdapter != null) {

            mAskAdapter.setNewData(problemContent);
        }
    }


    public void savaData() {

        if (mSpCity == null || mSpCounty == null || mEtAddrdetail1 == null) {
            RxToast.warning("请检查询问人情况是否已填写");
            return;
        }

        if (mTetAskingPeople == null || mTetAskingIdcard == null) {
            RxToast.warning("请检查被询问人情况是否已填写");
            return;
        }

        if (mEtEnforcementName == null) {
            RxToast.warning("请检查被询问问题是否已填写");
            return;
        }

        String begintime = mBtSelectBegintime.getText().toString().trim();
        final String endtime = mBtSelectEndtime.getText().toString().trim();
        if (begintime.equals("选择") || endtime.equals("选择")) {
            RxToast.warning(BaseApplication.getApplictaion(), "请选择笔录时间", Toast.LENGTH_SHORT).show();
            return;
        }
        String askContent = mTetContent.getText().toString().trim();
        String city = mSpCity.getText().toString().trim();
        String county = mSpCounty.getText().toString().trim();
        String addrdetail = mEtAddrdetail1.getText().toString().trim();
        String askPerson = mTetAskPerson.getText().toString().trim();
        String askEnforcementNumber = mTetAskEnforcementNumber.getText().toString().trim();
        final String recorder = mTetRecorder.getText().toString().trim();
        String recorderNumber = mTetRecorderNumber.getText().toString().trim();
        String askingPeople = mTetAskingPeople.getText().toString().trim();
        String askingIdcard = mTetAskingIdcard.getText().toString().trim();
        String askingPosition = mTetAskingPosition.getText().toString().trim();
        String askingWorkUnits = mTetAskingWorkUnits.getText().toString().trim();
        String city2 = mSpCity2.getText().toString().trim();
        String county2 = mSpCounty2.getText().toString().trim();
        String addrdetail2 = mEtAddrdetail2.getText().toString().trim();
        String enforcementName = mEtEnforcementName.getText().toString().trim();

        if (mAnswBeans == null) {
            mAnswBeans = new ArrayList<>();
        }
        for (int i = 0; i < mProblemContent.size(); i++) {
            TextView tv_ask = (TextView) mAskAdapter.getViewByPosition(mLvAsk, i, R.id.tv_ask_content);
            EditText et_answer = (EditText) mAskAdapter.getViewByPosition(mLvAsk, i, R.id.et_answer_content);// 从layout中获得控件,根据其id
            String ask = tv_ask.getText().toString().trim();
            String answ = et_answer.getText().toString().trim();
            if (TextUtils.isEmpty(answ)) {
                RxToast.warning(BaseApplication.getApplictaion(), "笔录问题不能有空", Toast.LENGTH_SHORT).show();
                return;
            }

            AnswBean answBean = new AnswBean(ask, answ, i);
            mAnswBeans.add(answBean);

        }

        if (TextUtils.isEmpty(askContent)) {
            RxToast.warning("询问内容不能为空");
            return;
        }

        if (mSpCity.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择询问地点(城市)");
            return;
        }
        if (mSpCounty.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择询问地点(县区)");
            return;
        }

        if (TextUtils.isEmpty(addrdetail)) {
            RxToast.warning("询问地点不能为空");
            return;
        }

        if (TextUtils.isEmpty(askPerson)) {
            RxToast.warning("询问人不能为空");
            return;
        }

        if (TextUtils.isEmpty(askEnforcementNumber)) {
            RxToast.warning("询问人执法编号不能为空");
            return;
        }

        if (TextUtils.isEmpty(recorder)) {
            RxToast.warning("记录人不能为空");
            return;
        }

        if (TextUtils.isEmpty(recorderNumber)) {
            RxToast.warning("记录人执法编号不能为空");
            return;
        }

        if (TextUtils.isEmpty(askingPeople)) {
            RxToast.warning("被询问人姓名不能为空");
            return;
        }

        if (TextUtils.isEmpty(askingIdcard)) {
            RxToast.warning("被询问人身份证号码不能为空");
            return;
        }

        if (TextUtils.isEmpty(askingPosition)) {
            RxToast.warning("被询问人职务不能为空");
            return;
        }

        if (TextUtils.isEmpty(askingWorkUnits)) {
            RxToast.warning("被询问人工作单位不能为空");
            return;
        }

        if (mSpCity2.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择被询问人住址(城市)");
            return;
        }
        if (mSpCounty2.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("请选择被询问人住址(县区)");
            return;
        }

        if (TextUtils.isEmpty(addrdetail2)) {
            RxToast.warning("被询问人住址不能为空");
            return;
        }

        if (TextUtils.isEmpty(enforcementName)) {
            RxToast.warning("执法人员姓名不能为空");
            return;
        }

        String address1;
        if (county.equals("无")) {
            address1 = "广东省" + city + "市" + addrdetail;
        } else {
            address1 = "广东省" + city + "市" + county + "县" + addrdetail;

        }
        String address2;
        if (county2.equals("无")) {
            address2 = "广东省" + city2 + "市" + addrdetail2;
        } else {
            address2 = "广东省" + city2 + "市" + county2 + "县" + addrdetail2;

        }


        if (mRxDialogLoading == null) {

            mRxDialogLoading = new RxDialogLoading(activity, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });
            mRxDialogLoading.setLoadingText("提交数据中...");
        }
        mRxDialogLoading.show();

        if (mUploadRecordPresenter == null) {

            mUploadRecordPresenter = new UploadRecordPresenter(new UploadRecordContract.View() {
                @Override
                public void uploadRecordSucc(String result) {
                    mRxDialogLoading.cancel();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            RxToast.success(BaseApplication.getApplictaion(), "数据提交成功", Toast.LENGTH_SHORT).show();
                            mTetContent.setText("");
                            mEtAddrdetail1.setText("");
                            mTetAskPerson.setText("");
                            mTetAskEnforcementNumber.setText("");
                            mTetRecorder.setText("");
                            mTetRecorderNumber.setText("");
                            mTetAskingPeople.setText("");
                            mTetAskingIdcard.setText("");
                            mTetAskingPosition.setText("");
                            mTetAskingWorkUnits.setText("");
                            mEtAddrdetail2.setText("");
                            mEtEnforcementName.setText("");

                            for (ProblemBean problemBean : mProblemContent) {
                                problemBean.setContent("");
                            }

                            mAskAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        RxToast.error(BaseApplication.getApplictaion(), "数据提交失败,服务器错误", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void uploadRecordFail(Throwable ex) {
                    Log.i("Oking", "数据提交失败" + ex.getMessage());
                    mRxDialogLoading.cancel();
                    RxToast.error(BaseApplication.getApplictaion(), "数据提交失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
        HashMap<String, Object> params = new HashMap<>();

        params.put("ajlx", mSelectedItem);
        params.put("xwnr", askContent);
        params.put("blbegindate", begintime);
        params.put("blenddate", endtime);
        params.put("xwdd", address1);
        params.put("xwrname", askPerson);
        params.put("xwrcode", askEnforcementNumber);
        params.put("jlrname", recorder);
        params.put("jlrcode", recorderNumber);
        params.put("bxwrtype", mBxwrtype);
        params.put("bxwrname", askingPeople);
        params.put("bxwridcode", askingIdcard);
        params.put("bxwrposition", askingPosition);
        params.put("bxwrdept", askingWorkUnits);
        params.put("bxwraddr", address2);
        params.put("zfryintroduction", "我是" + enforcementName + "执法人员，现在我需要问一些问题，你要如实回答。");
        params.put("jllrrid", OkingContract.CURRENTUSER.getUserid());
        if (mGson == null) {
            mGson = new Gson();

        }
        params.put("dcblnr", mGson.toJson(mAnswBeans));
        mUploadRecordPresenter.uploadRecord(params);

    }


    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String tag = timePickerView.getTag();
        if ("beginTime".equals(tag)) {
            mBeginMillseconds = millseconds;
            mBtSelectBegintime.setText(OkingContract.SDF.format(millseconds));
        } else {
            mBtSelectEndtime.setText(OkingContract.SDF.format(millseconds));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
        switch (compoundButton.getId()) {
            case R.id.rb_illegal_person:
                if (ischecked) {

                    mBxwrtype = "0";
                }
                break;
            case R.id.rb_victim:
                if (ischecked) {

                    mBxwrtype = "1";
                }
                break;
            case R.id.rb_other:
                if (ischecked) {

                    mBxwrtype = "3";
                }
                break;
            default:
                break;

        }

    }

    public void print() {
        if (mSpCity == null || mSpCounty == null || mEtAddrdetail1 == null) {
            RxToast.warning("请检查询问人情况是否已填写");
            return;
        }

        if (mTetAskingPeople == null || mTetAskingIdcard == null) {
            RxToast.warning("请检查被询问人情况是否已填写");
            return;
        }

        if (mEtEnforcementName == null) {
            RxToast.warning("请检查被询问问题是否已填写");
            return;
        }
        boolean installApp = AppUtil.isInstallApp(BaseApplication.getApplictaion(), "com.dynamixsoftware.printershare");
        if (installApp) {

            final String askContent = mTetContent.getText().toString().trim();
            final String selectBegintime = mBtSelectBegintime.getText().toString().trim();
            final String selecEndtime = mBtSelectEndtime.getText().toString().trim();
            String city = mSpCity.getText().toString().trim();
            String city2 = mSpCity2.getText().toString().trim();
            final String type = mSpType.getText().toString().trim();
            String county = mSpCounty.getText().toString().trim();
            String county2 = mSpCounty2.getText().toString().trim();
            String addrdetail = mEtAddrdetail1.getText().toString().trim();
            final String askPerson = mTetAskPerson.getText().toString().trim();
            final String askEnforcementNumber = mTetAskEnforcementNumber.getText().toString().trim();
            final String recorder = mTetRecorder.getText().toString().trim();
            final String recordeNumber = mTetRecorderNumber.getText().toString().trim();
            final String askingPeople = mTetAskingPeople.getText().toString().trim();
            final String askingIdcard = mTetAskingIdcard.getText().toString().trim();
            final String askingPosition = mTetAskingPosition.getText().toString().trim();
            final String askingWorkUnits = mTetAskingWorkUnits.getText().toString().trim();
            String addrdetail2 = mEtAddrdetail2.getText().toString().trim();
            final String enforcementName = mEtEnforcementName.getText().toString().trim();
            if (selectBegintime.equals("选择") || selecEndtime.equals("选择")) {
                RxToast.warning(BaseApplication.getApplictaion(), "请选择笔录时间", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mProblemContent.size(); i++) {

                TextView ask_content = (TextView) mAskAdapter.getViewByPosition(mLvAsk, i, R.id.tv_ask_content);
                EditText et_answer = (EditText) mAskAdapter.getViewByPosition(mLvAsk, i, R.id.et_answer_content);// 从layout中获得控件,根据其id
                String content = ask_content.getText().toString().trim();
                String answ = et_answer.getText().toString().trim();

                if (TextUtils.isEmpty(answ)) {
                    RxToast.warning(BaseApplication.getApplictaion(), "问题笔录不能有空", Toast.LENGTH_SHORT).show();
                    return;
                }
                sb.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;" + content + "</p>");
                sb.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;<u>" + answ + "</u></p>");

            }
            final String askSb = sb.toString();

            if (TextUtils.isEmpty(askContent)) {
                RxToast.warning("询问内容不能为空");
                return;
            }
            if (mSpCity.getText().toString().trim().equals("*请选择")) {
                RxToast.warning("请选择询问地点(城市)");
                return;
            }
            if (mSpCounty.getText().toString().trim().equals("*请选择")) {
                RxToast.warning("请选择询问地点(县区)");
                return;
            }

            if (TextUtils.isEmpty(addrdetail)) {
                RxToast.warning("询问地点不能为空");
                return;
            }

            if (TextUtils.isEmpty(askPerson)) {
                RxToast.warning("询问人不能为空");
                return;
            }

            if (TextUtils.isEmpty(askEnforcementNumber)) {
                RxToast.warning("询问人执法编号不能为空");
                return;
            }

            if (TextUtils.isEmpty(recorder)) {
                RxToast.warning("记录人不能为空");
                return;
            }

            if (TextUtils.isEmpty(recordeNumber)) {
                RxToast.warning("记录人执法编号不能为空");
                return;
            }

            if (TextUtils.isEmpty(askingPeople)) {
                RxToast.warning("被询问人姓名不能为空");
                return;
            }

            if (TextUtils.isEmpty(askingIdcard)) {
                RxToast.warning("被询问人身份证号码不能为空");
                return;
            }

            if (TextUtils.isEmpty(askingPosition)) {
                RxToast.warning("被询问人职务不能为空");
                return;
            }

            if (TextUtils.isEmpty(askingWorkUnits)) {
                RxToast.warning("被询问人工作单位不能为空");
                return;
            }

            if (mSpCity2.getText().toString().trim().equals("*请选择")) {
                RxToast.warning("请选择被询问人住址(城市)");
                return;
            }
            if (mSpCounty2.getText().toString().trim().equals("*请选择")) {
                RxToast.warning("请选择被询问人住址(县区)");
                return;
            }
            if (TextUtils.isEmpty(addrdetail2)) {
                RxToast.warning("被询问人住址不能为空");
                return;
            }

            if (TextUtils.isEmpty(enforcementName)) {
                RxToast.warning("执法人员姓名不能为空");
                return;
            }

            if (mRbIllegalPerson.isChecked()) {
                mIllegalPerson = "<p>被询问人：<input type=\"checkbox\" checked/>违法行为人&nbsp;<input type=\"checkbox\"/>受害人&nbsp;<input type=\"checkbox\"/>第三方\n";
            } else if (mRbVictim.isChecked()) {
                mIllegalPerson = "<p>被询问人：<input type=\"checkbox\"/>违法行为人&nbsp;<input type=\"checkbox\" checked/>受害人&nbsp;<input type=\"checkbox\"/>第三方\n";
            } else if (mRbOther.isChecked()) {
                mIllegalPerson = "<p>被询问人：<input type=\"checkbox\"/>违法行为人&nbsp;<input type=\"checkbox\"/>受害人&nbsp;<input type=\"checkbox\" checked/>第三方\n";

            }

            final String address1;
            if (county.equals("无")) {
                address1 = "广东省" + city + "市" + addrdetail;
            } else {
                address1 = "广东省" + city + "市" + county + "县" + addrdetail;

            }
            final String address2;
            if (county2.equals("无")) {
                address2 = "广东省" + city2 + "市" + addrdetail2;
            } else {
                address2 = "广东省" + city2 + "市" + county2 + "县" + addrdetail2;

            }
            Schedulers.io().createWorker().schedule(new Runnable() {
                @Override
                public void run() {
                    writeHtml(type, askContent, selectBegintime, selecEndtime, address1, askPerson, askEnforcementNumber,
                            recorder, recordeNumber, askingPeople, askingIdcard, askingPosition, askingWorkUnits, address2, enforcementName, askSb);

                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ComponentName comp = new ComponentName("com.dynamixsoftware.printershare", "com.dynamixsoftware.printershare.ActivityWeb");
                Intent intent = new Intent();
                Uri contentUri = FileProvider.getUriForFile(BaseApplication.getApplictaion(), "com.zhang.okinglawenforcementphone.fileProvider", new File(Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp3.html"));
                // 申请临时访问权限
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setComponent(comp);
                intent.setAction("android.intent.action.VIEW");
                intent.setType("text/html");
                intent.setData(contentUri);
                activity.startActivity(intent);
            } else {
                ComponentName comp = new ComponentName("com.dynamixsoftware.printershare", "com.dynamixsoftware.printershare.ActivityWeb");
                Intent intent = new Intent();

                intent.setComponent(comp);
                intent.setAction("android.intent.action.VIEW");
                intent.setType("text/html");
                intent.setData(Uri.parse("file:///" + Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp3.html"));
                activity.startActivity(intent);
            }

        } else {
            if (mRxDialogLoading == null) {

                mRxDialogLoading = new RxDialogLoading(activity, false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.cancel();
                    }
                });
                mRxDialogLoading.setLoadingText("正在解压插件...");
            }
            mRxDialogLoading.show();

            Schedulers.io().createWorker().schedule(new Runnable() {
                @Override
                public void run() {
                    final File assetFileToCacheDir = FileUtil.getAssetFileToCacheDir("PrinterShare.apk");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                            @Override
                            public void run() {
                                mRxDialogLoading.cancel();
                                Intent intent = new Intent();
                                // 申请临时访问权限
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION
                                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(BaseApplication.getApplictaion(), "com.zhang.okinglawenforcementphone.fileProvider", assetFileToCacheDir);
                                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                                activity.startActivity(intent);
                            }
                        });
                    } else {
                        AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                            @Override
                            public void run() {
                                mRxDialogLoading.cancel();

                                AppUtil.installAPK(BaseApplication.getApplictaion(), assetFileToCacheDir.getPath());

                            }
                        });
                    }

                }
            });


        }
    }

    private void writeHtml(String type, String askContent, String selectBegintime, String selecEndtime, String address1, String askPerson, String askEnforcementNumber, String recorder, String recordeNumber, String askingPeople, String askingIdcard, String askingPosition, String askingWorkUnits, String address2, String enforcementName, String askSb) {
        File destDir = new File(Environment.getExternalStorageDirectory().getPath() + "/oking/print/temp3.html");
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE HTML>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n");
        sb.append("    <style>\n");
        sb.append("    </style>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("<h1 align=\"center\">调查询问笔录</h1>\n");
        sb.append("<p>案件类型：" + type + "&nbsp;&nbsp;&nbsp<u>&nbsp;" + askContent + "&nbsp;</u></p>");
        sb.append("<p>笔录时间：" + selectBegintime + "&nbsp;至&nbsp;" + selecEndtime + "</p>");
        sb.append("<p>询问地点：<u>&nbsp;" + address1 + "&nbsp;</u></p>\n");
        sb.append("<p>询问人:<u>&nbsp;" + askPerson + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;执法编号（询问人）: <u>&nbsp;" + askEnforcementNumber + "</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;记录人:<u>&nbsp;" + recorder + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;执法编号（记录人）:\n");
        sb.append("    <u>&nbsp;" + recordeNumber + "&nbsp;</u>\n");
        sb.append("</p>\n");
        sb.append(mIllegalPerson);
        sb.append("</p>\n");
        sb.append("<p>姓名:<u>&nbsp;" + askingPeople + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;身份证号码: <u>&nbsp;" + askingIdcard + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;职务: <u>&nbsp;" + askingPosition + "&nbsp;</u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;工作单位:\n");
        sb.append("    <u>&nbsp;" + askingWorkUnits + "&nbsp;</u>\n");
        sb.append("</p>\n");
        sb.append("<p>住址：<u>&nbsp;" + address2 + "&nbsp;</u></p>");
        sb.append("<p>问：我是&nbsp;<u>&nbsp;" + enforcementName + "&nbsp;</u>&nbsp;执法人员，现在我需要问一些问题，你要如实回答。</p>\n");
        sb.append(askSb);
        sb.append("</body>\n");
        sb.append("</html>");
        FileUtil.writeFileFromString(destDir, sb.toString(), false);

    }
}
