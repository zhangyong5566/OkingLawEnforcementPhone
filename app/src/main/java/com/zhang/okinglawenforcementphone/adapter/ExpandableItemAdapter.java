package com.zhang.okinglawenforcementphone.adapter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.DefaultContants;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.baselib.utils.Util;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.MissionRecorCallBack;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenEquipment;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLogDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.Level0Item;
import com.zhang.okinglawenforcementphone.beans.NewsTaskOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.Point;
import com.zhang.okinglawenforcementphone.beans.RecorItemBean;
import com.zhang.okinglawenforcementphone.beans.RecordLogOV;
import com.zhang.okinglawenforcementphone.beans.SourceArrayOV;
import com.zhang.okinglawenforcementphone.beans.StopSwipeRefreshEvent;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.htttp.Api;
import com.zhang.okinglawenforcementphone.htttp.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.UpdateMissionStateContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadJobLogContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadJobLogForPicContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadSignaturePicContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadVideoContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.UpdateMissionStatePresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadJobLogForPicPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadJobLogPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadSignaturePicPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.UploadVideoPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MissionRecorActivity;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by luoxw on 2016/8/9.
 */
public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> implements MissionRecorCallBack {
    private Map<String, RequestBody> photoParams;
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;
    public static final int TYPE_LEVEL_3 = 3;
    public static final int TYPE_LEVEL_4 = 4;
    public static final int TYPE_LEVEL_5 = 5;
    private int picComPostion = 0;
    private int veodComPosion = 0;
    private int logSignPosion = 0;
    private boolean mCanSaveComplete = false;
    private int mSelePlanPos;
    private int mSeleMattersPos;
    private TagAdapter partTagAdapter;
    private String parts = "";
    private List<String> partList = new ArrayList<String>();
    private SimpleDateFormat sdfVideo = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private boolean mLeaderSummarySwisopen = false;
    private boolean mSummarySwisopen = false;
    private GreenMissionTask mGreenMissionTask;
    private GreenMissionLog mGreenMissionLog;
    private PicSimpleAdapter picadapter;
    private VideoSimpleAdapter videoadapter;
    private List<GreenMedia> mPhotoMedias = new ArrayList<>();
    private List<GreenMedia> mVideoMedias = new ArrayList<>();
    private MissionRecorActivity activity;
    private RxDialogSureCancel mRxDialogSureCancel;
    private RecyclerView mPicGridView;
    private RecyclerView mVideoGridView;
    private EditText mSummaryEditText;
    private EditText mLeaderSummaryEditText;
    private Switch mLeaderSummarySw;
    private Switch mSummarySw;
    private TextView mEquipmentTextView;
    private String mFlowTagPos;
    private boolean mIsSelected = false;
    private Button mSignBtn;
    private RxDialogLoading mRxDialogLoading;
    private Gson mGson = new Gson();
    private boolean uploadLogPic = false, uploadSignPic = false, uploadLogVideo = false;
    private UploadJobLogPresenter mUploadJobLogPresenter;
    private UploadJobLogForPicPresenter mUploadJobLogForPicPresenter;
    private UploadSignaturePicPresenter mUploadSignaturePicPresenter;
    private UploadVideoPresenter mUploadVideoPresenter;
    private TextView mMemberTextView;
    private String[] mPlanArray;
    private TextView mPlanSpinner;
    private TextView mItemSpinner;
    private String[] mMattersArray;
    private int mDatePoor;
    private long mBeforTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private String mLocJson;
    private EditText mPartOtherEditText;
    private TagFlowLayout mPartFlowTagLayout;
    private DialogUtil mDialogUtil;
    private View mButtonDailog;
    private TextView mTv_title;
    private ArrayList<SourceArrayOV> mPlanArrayOVS;
    private ArrayList<SourceArrayOV> mMattersArrayOVS;
    private SourceArrayRecyAdapter mSourceArrayRecyAdapter;
    public ExpandableItemAdapter(List<MultiItemEntity> data, MissionRecorActivity recorActivity) {
        super(data);
        this.activity = recorActivity;
        addItemType(TYPE_LEVEL_0, R.layout.activity_mission_recor_level0);
        addItemType(TYPE_LEVEL_1, R.layout.activity_mission_recor_basic_information);
        addItemType(TYPE_LEVEL_2, R.layout.activity_mission_patrol_situation);
        addItemType(TYPE_LEVEL_3, R.layout.activity_mission_processing_results);
        addItemType(TYPE_LEVEL_4, R.layout.activity_mission_audio_info);
        addItemType(TYPE_LEVEL_5, R.layout.activity_mission_video_info);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final Level0Item lv0 = (Level0Item) item;

                helper.setText(R.id.title, lv0.subTitle);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = helper.getAdapterPosition();

                        if (lv0.isExpanded()) {
                            collapse(adapterPosition);
                        } else {
                            expand(adapterPosition);
                        }

                    }
                });
                break;
            case TYPE_LEVEL_1:
                final RecorItemBean recorItemBean = (RecorItemBean) item;
                mGreenMissionTask = recorItemBean.getGreenMissionTask();
                mGreenMissionLog = recorItemBean.getGreenMissionLog();
                helper.setText(R.id.type_nature, mGreenMissionTask.getTypename());
                if (mEquipmentTextView == null) {

                    mEquipmentTextView = helper.getView(R.id.equipment_textView);
                    mEquipmentTextView.setText(mGreenMissionLog.getEquipment());
                }
                if (mGreenMissionTask != null) {
                    if ("0".equals(mGreenMissionTask.getTypeoftask())) {
                        helper.setText(R.id.tv_tasktype, "河道管理");
                    } else if ("1".equals(mGreenMissionTask.getTypeoftask())) {
                        helper.setText(R.id.tv_tasktype, "河道采砂");
                    } else if ("2".equals(mGreenMissionTask.getTypeoftask())) {
                        helper.setText(R.id.tv_tasktype, "水资源管理");
                    } else if ("3".equals(mGreenMissionTask.getTypeoftask())) {
                        helper.setText(R.id.tv_tasktype, "水土保持管理");
                    } else if ("4".equals(mGreenMissionTask.getTypeoftask())) {
                        helper.setText(R.id.tv_tasktype, "水利工程管理");
                    }
                }

                if (mPlanArray == null) {

                    mPlanArray = BaseApplication.getApplictaion().getResources().getStringArray(R.array.spinner_plan);
                    mPlanArrayOVS = new ArrayList<>();
                    for (String s : mPlanArray) {
                        SourceArrayOV sourceArrayOV = new SourceArrayOV();
                        sourceArrayOV.setType(0);
                        sourceArrayOV.setSource(s);
                        mPlanArrayOVS.add(sourceArrayOV);
                    }
                }

                if (mDialogUtil ==null){
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
                            switch (sourceArrayOV.getType()){
                                case 0:
                                    mSelePlanPos = position;
                                    mPlanSpinner.setText(sourceArrayOV.getSource());
                                    break;
                                case 1:
                                    mSeleMattersPos = position;
                                    mItemSpinner.setText(sourceArrayOV.getSource());
                                    break;
                                default:
                                    break;
                            }
                            mDialogUtil.cancelDialog();
                        }
                    });
                }


                if (mPlanSpinner == null) {
                    mPlanSpinner = helper.getView(R.id.plan_spinner);
                    mPlanSpinner.setText(mPlanArray[mGreenMissionLog.getPlan()]);
                    mPlanSpinner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTv_title.setText("计划");
                            mSourceArrayRecyAdapter.setNewData(mPlanArrayOVS);
                            mDialogUtil.showBottomDialog(activity,mButtonDailog,300f);
                        }
                    });



                }



                if (mMattersArray == null) {

                    mMattersArray = BaseApplication.getApplictaion().getResources().getStringArray(R.array.spinner_matters);

                    mMattersArrayOVS = new ArrayList<>();
                    for (String s : mMattersArray) {
                        SourceArrayOV sourceArrayOV = new SourceArrayOV();
                        sourceArrayOV.setType(1);
                        sourceArrayOV.setSource(s);
                        mMattersArrayOVS.add(sourceArrayOV);
                    }
                }

                if (mItemSpinner == null) {

                    mItemSpinner = helper.getView(R.id.item_spinner);
                    mItemSpinner.setText(mMattersArray[mGreenMissionLog.getItem()]);
                    mItemSpinner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTv_title.setText("事项");
                            mSourceArrayRecyAdapter.setNewData(mMattersArrayOVS);
                            mDialogUtil.showBottomDialog(activity,mButtonDailog,300f);
                        }
                    });

                }

                helper.setText(R.id.area_TextView, mGreenMissionTask.getRwqyms());


                if (mPartOtherEditText ==null){
                    mPartOtherEditText = helper.getView(R.id.part_other_editText);

                }

                if (mPartFlowTagLayout==null){

                    mPartFlowTagLayout = helper.getView(R.id.part_flow_layout);
                }


                if (partTagAdapter == null) {
                    partList = Arrays.asList("公安", "海事", "环保", "航道", "交通", "国土", "城管", "纪检");
                    partTagAdapter = new TagAdapter<String>(partList) {
                        @Override
                        public View getView(FlowLayout parent, int position, String parts) {
                            View inflate = View.inflate(BaseApplication.getApplictaion(), R.layout.tag_item, null);
                            TextView tv_tag = inflate.findViewById(R.id.tv_tag);
                            tv_tag.setText(parts);
                            return inflate;
                        }
                    };
                    String flowTagPos = mGreenMissionLog.getFlowTagPos();
                    if (!TextUtils.isEmpty(flowTagPos)) {
                        String[] split = flowTagPos.split(",");
                        Set<Integer> integers = new HashSet<>();
                        for (String s : split) {
                            integers.add(Integer.parseInt(s));
                        }
                        partTagAdapter.setSelectedList(integers);
                    }

                    mPartFlowTagLayout.setAdapter(partTagAdapter);
                    mPartFlowTagLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                        @Override
                        public void onSelected(Set<Integer> selectPosSet) {
                            mIsSelected = true;
                            mFlowTagPos = "";
                            parts = "";
                            Iterator<Integer> it = selectPosSet.iterator();
                            while (it.hasNext()) {
                                Integer next = it.next();
                                parts += partList.get(next) + ",";
                                mFlowTagPos += next + ",";
                            }
                            if (!"".equals(mPartOtherEditText.getText().toString())) {
                                parts += mPartOtherEditText.getText().toString() + ",";
                            }
                        }
                    });

                    if (mGreenMissionTask.getStatus().equals("100")) {
                        mPartFlowTagLayout.setEnabled(false);
                    } else if (mGreenMissionTask.getStatus().equals("5")) {
                        mPartFlowTagLayout.setEnabled(false);
                        helper.setVisible(R.id.edit_Equipment_Btn, false);
                        mPlanSpinner.setClickable(false);
                        mItemSpinner.setClickable(false);
                    } else if (mGreenMissionTask.getStatus().equals("9")) {
                        mPartFlowTagLayout.setEnabled(false);
                        mPlanSpinner.setEnabled(false);
                        mItemSpinner.setEnabled(false);
                        helper.setVisible(R.id.edit_Equipment_Btn, false);
                    }
                }

                helper.addOnClickListener(R.id.edit_Equipment_Btn);

                break;
            case TYPE_LEVEL_2:
                if (mSummarySw==null){
                    mSummarySw = helper.getView(R.id.sw_summary);

                }
                if (mSummaryEditText == null) {

                    mSummaryEditText = helper.getView(R.id.summary_editText);
                    mSummaryEditText.setText(mGreenMissionLog.getPatrol());
                }
                mSummarySw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        mSummarySwisopen = isChecked;
                        if (isChecked) {
                            mSummaryEditText.setEnabled(false);
                            mSummaryEditText.setText("");
                        } else {
                            mSummaryEditText.setEnabled(true);
                        }
                    }
                });
                if (mGreenMissionTask.getStatus().equals("5")) {
                    mSummarySw.setEnabled(false);
                    mSummaryEditText.setFocusable(false);
                } else if (mGreenMissionTask.getStatus().equals("9")) {
                    mSummarySw.setEnabled(true);
                }
                break;
            case TYPE_LEVEL_3:
                if (mLeaderSummarySw == null) {

                    mLeaderSummarySw = helper.getView(R.id.sw_leader_summary);
                }
                if (mSignBtn == null) {
                    mSignBtn = helper.getView(R.id.sign_btn);
                    helper.addOnClickListener(R.id.sign_btn);
                }
                if (mGreenMissionTask.getStatus().equals("100") || mGreenMissionTask.getStatus().equals("5") || mGreenMissionTask.getStatus().equals("9")) {
                    mSignBtn.setVisibility(View.VISIBLE);
                }
                if (mLeaderSummaryEditText == null) {

                    mLeaderSummaryEditText = helper.getView(R.id.leader_summary_editText);
                    mLeaderSummaryEditText.setText(mGreenMissionLog.getDzyj());
                }
                mLeaderSummarySw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        mLeaderSummarySwisopen = isChecked;
                        if (isChecked) {
                            mLeaderSummaryEditText.setEnabled(false);
                            mLeaderSummaryEditText.setText("");
                        } else {
                            mLeaderSummaryEditText.setEnabled(true);
                        }
                    }
                });

                if (mGreenMissionTask.getStatus().equals("5")) {
                    mLeaderSummarySw.setEnabled(false);
                    mLeaderSummaryEditText.setFocusable(false);
                } else if (mGreenMissionTask.getStatus().equals("9")) {
                    mLeaderSummarySw.setEnabled(true);
                }

                //签名
                String memberStr = "";
                mGreenMissionTask.resetMembers();
                if (mGreenMissionTask.getMembers() != null) {
                    for (int i = 0; i < mGreenMissionTask.getMembers().size(); i++) {
                        GreenMember member = mGreenMissionTask.getMembers().get(i);
                        if (member.getUsername().equals(mGreenMissionTask.getReceiver_name())) {
                            memberStr += "<font color=\"#88000000\">任务负责人：</font>";
                            if (member.getSignPic() == null || !new File(member.getSignPic()).exists()) {
                                memberStr += "<font color=\"#88000000\">" + mGreenMissionTask.getReceiver_name() + "&emsp;&emsp;</font>";
                            } else {
                                memberStr += "<font color=\"#98CF60\">" + mGreenMissionTask.getReceiver_name() + "&emsp;&emsp;</font>";
                            }
                            memberStr += "<font color=\"#88000000\">队员：</font>";
                            continue;
                        }


                        if (member.getSignPic() == null || !new File(member.getSignPic()).exists()) {
                            //未签名
                            memberStr += "<font color=\"#88000000\">" + member.getUsername() + "&emsp;</font>";
                        } else {
                            //已签名
                            memberStr += "<font color=\"#98CF60\">" + member.getUsername() + "&emsp;</font>";
                        }
                    }
                }
                if (mMemberTextView == null) {

                    mMemberTextView = helper.getView(R.id.member_textView);
                }
                mMemberTextView.setText(Html.fromHtml(memberStr));
                break;
            case TYPE_LEVEL_4:


                if (mGreenMissionTask.getStatus().equals("5")) {
                    helper.getView(R.id.iv_addpic).setEnabled(false);
                } else if (mGreenMissionTask.getStatus().equals("9")) {
                    helper.getView(R.id.iv_addpic).setEnabled(true);
                }

                mPhotoMedias.clear();
                //清除缓存
                mGreenMissionLog.resetGreenMedia();
                List<GreenMedia> greenMedias = mGreenMissionLog.getGreenMedia();
                for (GreenMedia media : greenMedias) {
                    if (media.getType() == 1) {            //1表示日志图片
                        mPhotoMedias.add(media);
                    } else if (media.getType() == 2) {      //2表示视频
                    } else if (media.getType() == 3) {
                        //3表示语音
                    } else {
                        //签名图片
                    }

                }

                helper.addOnClickListener(R.id.iv_addpic);

                if (mPicGridView == null) {

                    mPicGridView = helper.getView(R.id.pic_gridView);
                    mPicGridView.setLayoutManager(new GridLayoutManager(BaseApplication.getApplictaion(), 5));
                }
                if (picadapter == null) {
                    picadapter = new PicSimpleAdapter(R.layout.pic_item, mPhotoMedias, activity, !mGreenMissionTask.getStatus().equals("5") && !mGreenMissionTask.getStatus().equals("9"), mGreenMissionTask.getTypename());//mission.getPhotoList()为图片uri的list
                    mPicGridView.setAdapter(picadapter);
                    picadapter.setOnClickListener(new PicSimpleAdapter.OnClickListener() {

                        @Override
                        public void onLongItemClick(final PicSimpleAdapter adapter, final GreenMedia media, final int position) {
                            //提示弹窗
                            if (mRxDialogSureCancel == null) {

                                mRxDialogSureCancel = new RxDialogSureCancel(activity);
                            }
                            mRxDialogSureCancel.setContent("是否删除原图片？");
                            mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Uri uri = Uri.parse(media.getPath());
                                    String path = null;

                                    path = FileUtil.praseUritoPath(BaseApplication.getApplictaion(), uri);
                                    if (path != null) {

                                        File file = new File(path);
                                        if (file.exists()) {
                                            file.delete();

                                        }
                                    }
                                    mPhotoMedias.remove(position);
                                    if (media.getGreenGreenLocationId() != null) {
                                        GreenDAOManager.getInstence().getDaoSession().getGreenLocationDao().deleteByKey(media.getGreenGreenLocationId());
                                    }
                                    GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().delete(media);
                                    collapse(helper.getLayoutPosition() - 1);


                                    mRxDialogSureCancel.cancel();

                                }
                            });
                            mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mRxDialogSureCancel.cancel();
                                }
                            });
                            mRxDialogSureCancel.show();

                        }
                    });

                } else {
                    picadapter.setNewData(mPhotoMedias);
                }


                break;
            case TYPE_LEVEL_5:
                if (mGreenMissionTask.getStatus().equals("5")) {
                    helper.getView(R.id.iv_addvideo).setEnabled(false);
                } else if (mGreenMissionTask.getStatus().equals("9")) {
                    helper.getView(R.id.iv_addvideo).setEnabled(true);
                }
                mVideoMedias.clear();
                //清除缓存
                mGreenMissionLog.resetGreenMedia();
                List<GreenMedia> greenMedias1 = mGreenMissionLog.getGreenMedia();
                for (GreenMedia media : greenMedias1) {
                    if (media.getType() == 1) {            //1表示日志图片
                    } else if (media.getType() == 2) {      //2表示视频
                        mVideoMedias.add(media);
                    } else if (media.getType() == 3) {
                        //3表示语音
                    } else {
                        //签名图片
                    }

                }
                helper.addOnClickListener(R.id.iv_addvideo);
                if (mVideoGridView == null) {

                    mVideoGridView = helper.getView(R.id.video_gridView);
                    mVideoGridView.setLayoutManager(new GridLayoutManager(BaseApplication.getApplictaion(), 5));
                }
                if (videoadapter == null) {
                    videoadapter = new VideoSimpleAdapter(R.layout.pic_item, mVideoMedias, activity, !mGreenMissionTask.getStatus().equals("5") && !mGreenMissionTask.getStatus().equals("9"), mGreenMissionTask.getTypename());
                    mVideoGridView.setAdapter(videoadapter);
                    videoadapter.setOnClickListener(new VideoSimpleAdapter.OnClickListener() {
                        @Override
                        public void onLongItemClick(final VideoSimpleAdapter adapter, final GreenMedia media, final int position) {

                            //提示弹窗
                            if (mRxDialogSureCancel == null) {

                                mRxDialogSureCancel = new RxDialogSureCancel(activity);
                            }
                            mRxDialogSureCancel.setContent("是否删除原视频？");
                            mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Uri uri = Uri.parse(media.getPath());
                                    String path = null;

                                    path = FileUtil.praseUritoPath(BaseApplication.getApplictaion(), uri);
                                    if (path != null) {

                                        File file = new File(path);
                                        if (file.exists()) {
                                            file.delete();

                                        }
                                    }
                                    mVideoMedias.remove(position);
                                    if (media.getGreenGreenLocationId() != null) {

                                        GreenDAOManager.getInstence().getDaoSession().getGreenLocationDao().deleteByKey(media.getGreenGreenLocationId());
                                    }
                                    GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().delete(media);
                                    collapse(helper.getLayoutPosition() - 1);
                                    mRxDialogSureCancel.cancel();

                                }
                            });
                            mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mRxDialogSureCancel.cancel();
                                }
                            });
                            mRxDialogSureCancel.show();

                        }
                    });

                } else {
                    videoadapter.setNewData(mVideoMedias);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void completeThePatrol() {
        if (mRxDialogSureCancel == null) {

            mRxDialogSureCancel = new RxDialogSureCancel(activity);//提示弹窗
        }
        mRxDialogSureCancel.setContent("完成巡查后将停止记录巡查定位，是否继续？");
        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.invisiabelCompleteMissionButton();
                RxToast.warning(BaseApplication.getApplictaion(), "请去签名!!", Toast.LENGTH_SHORT, true).show();

                mGreenMissionTask.setExecute_end_time(System.currentTimeMillis());
                mGreenMissionTask.setStatus("100");
                //把巡查轨迹插入本地数据库
                String locationTrajectory = getLocationTrajectory();
                mGreenMissionLog.setLocJson(locationTrajectory);
                GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().update(mGreenMissionLog);
                GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mGreenMissionTask);
                if (mSignBtn != null) {

                    mSignBtn.setVisibility(View.VISIBLE);
                }

                if (DefaultContants.ISHTTPLOGIN) {

                    //改变任务状态
                    UpdateMissionStatePresenter updateMissionStatePresenter = new UpdateMissionStatePresenter(new UpdateMissionStateContract.View() {
                        @Override
                        public void updateMissionStateSucc(String result) {
                            Log.i("Oking", "巡查完毕" + result);
                            saveTheRecord();
                            mRxDialogSureCancel.cancel();
                        }

                        @Override
                        public void updateMissionStateFail(Throwable ex) {
                            Log.i("Oking", "巡查异常" + ex.getMessage());
                            saveTheRecord();
                            mRxDialogSureCancel.cancel();
                        }
                    });
                    updateMissionStatePresenter.updateMissionState(mGreenMissionTask.getTaskid(), "",
                            OkingContract.SDF.format(mGreenMissionTask.getExecute_end_time()), 4);

                }


            }
        });
        mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxDialogSureCancel.cancel();
            }
        });
        mRxDialogSureCancel.show();

    }

    @Override
    public void saveTheRecord() {
        GreenMissionLog unique = GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().queryBuilder().where(GreenMissionLogDao.Properties.Task_id.eq(mGreenMissionTask.getTaskid())).unique();
        if (unique != null) {
            unique.setPlan(mSelePlanPos);
            unique.setItem(mSeleMattersPos);
            if (mIsSelected) {

                unique.setFlowTagPos(mFlowTagPos);
            }
            unique.setArea(mGreenMissionTask.getRwqyms());
            if (mSummaryEditText != null) {

                unique.setPatrol(mSummaryEditText.getText().toString().trim());
            }

            if (mLeaderSummaryEditText != null) {

                unique.setDzyj(mLeaderSummaryEditText.getText().toString().trim());
            }

            if (mEquipmentTextView != null) {

                unique.setEquipment(mEquipmentTextView.getText().toString().trim());
            }


            unique.setOther_part(parts);
            GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().update(unique);
        } else {
            GreenMissionLog greenMissionLog = new GreenMissionLog();
            greenMissionLog.setPlan(mSelePlanPos);
            greenMissionLog.setItem(mSeleMattersPos);
            if (mIsSelected) {

                greenMissionLog.setFlowTagPos(mFlowTagPos);
            }
            greenMissionLog.setArea(mGreenMissionTask.getRwqyms());

            if (mSummaryEditText != null) {
                greenMissionLog.setPatrol(mSummaryEditText.getText().toString().trim());
            }

            if (mLeaderSummaryEditText != null) {
                greenMissionLog.setDzyj(mLeaderSummaryEditText.getText().toString().trim());
            }

            if (mEquipmentTextView != null) {
                greenMissionLog.setEquipment(mEquipmentTextView.getText().toString().trim());
            }

            greenMissionLog.setOther_part(parts);
            GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().insert(greenMissionLog);

        }

    }

    @Override
    public void reportTask() {
        if (mRxDialogSureCancel == null) {

            mRxDialogSureCancel = new RxDialogSureCancel(activity);//提示弹窗
        }
        mRxDialogSureCancel.setContent("上报任务后不能修改日志，是否继续？");
        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mGreenMissionTask.getStatus().equals("100") &&
                        !mGreenMissionTask.getStatus().equals("9")) {
                    RxToast.warning(BaseApplication.getApplictaion(), "任务未完成，不能上报", Toast.LENGTH_SHORT, true).show();

                    return;
                }

                if (mGreenMissionTask.getMembers().size() > 0) {
                    for (GreenMember greenMember : mGreenMissionTask.getMembers()) {
                        String signPic = greenMember.getSignPic();
                        if (signPic != null) {
                            File file = new File(signPic);
                            if (signPic != null && file.exists()) {
                                mCanSaveComplete = true;
                            } else {
                                mCanSaveComplete = false;
                            }
                        } else {
                            mCanSaveComplete = false;
                        }


                    }
                }


                if (mCanSaveComplete) {
                    if (!mSummarySwisopen && mSummaryEditText != null && TextUtils.isEmpty(mSummaryEditText.getText().toString().trim())) {
                        RxToast.warning(BaseApplication.getApplictaion(), "巡查情况未填写，不能上报任务！", Toast.LENGTH_SHORT, true).show();

                        return;
                    }

                    if (!mLeaderSummarySwisopen && mLeaderSummaryEditText != null && TextUtils.isEmpty(mLeaderSummaryEditText.getText().toString().trim())) {
                        RxToast.warning(BaseApplication.getApplictaion(), "处理结果未填写，不能上报任务！", Toast.LENGTH_SHORT, true).show();
                        return;
                    }

                    if (mRxDialogLoading == null) {

                        mRxDialogLoading = new RxDialogLoading(activity, false, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                dialogInterface.cancel();
                            }
                        });
                    }
                    mRxDialogSureCancel.cancel();
                    mRxDialogLoading.setLoadingText("上传数据中...图片：" + picComPostion + "/" + mPhotoMedias.size() + "视频：" + veodComPosion + "/" + mVideoMedias.size());
                    mRxDialogLoading.show();

                    picComPostion = 0;
                    veodComPosion = 0;
                    logSignPosion = 0;
                    //先保存数据
                    saveTheRecord();
                    //上传数据

                    httpSaveRecord();

                } else {
                    RxToast.warning(BaseApplication.getApplictaion(), "存在成员未签名，不能上报任务！", Toast.LENGTH_SHORT, true).show();
                    return;
                }

            }
        });
        mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxDialogSureCancel.cancel();
            }
        });
        mRxDialogSureCancel.show();

    }

    //上传数据
    private void httpSaveRecord() {
        RecordLogOV recordLogOV = new RecordLogOV();
        recordLogOV.setArea(mGreenMissionTask.getRwqyms());
        recordLogOV.setEquipment(mEquipmentTextView.getText().toString().trim());
        recordLogOV.setGreenMissionLog(mGreenMissionLog);
        recordLogOV.setGreenMissionTask(mGreenMissionTask);
        if (mLeaderSummaryEditText != null) {
            recordLogOV.setLeaderSummary(mLeaderSummaryEditText.getText().toString().trim());
        } else {
            RxToast.warning("请检查处理结果是否已经填写");
            mRxDialogLoading.cancel();
            return;
        }

        if (mSummarySwisopen || mLeaderSummarySwisopen) {
            recordLogOV.setSwisopen(true);
        } else {
            recordLogOV.setSwisopen(false);
        }

        if (mIsSelected) {

            recordLogOV.setParts(parts);
        } else {
            recordLogOV.setParts(mGreenMissionLog.getOther_part());
        }
        recordLogOV.setSeleMattersPos(mSeleMattersPos);
        recordLogOV.setSelePlanPos(mSelePlanPos);
        if (mSummaryEditText != null) {
            recordLogOV.setSummary(mSummaryEditText.getText().toString().trim());
        } else {
            RxToast.warning("请检查巡查情况是否已经填写");
            mRxDialogLoading.cancel();
            return;
        }

        recordLogOV.setTime(OkingContract.SDF.format(System.currentTimeMillis()));
        if (mUploadJobLogPresenter == null) {
            mUploadJobLogPresenter = new UploadJobLogPresenter(new UploadJobLogContract.View() {
                @Override
                public void uploadJobLogSucc(String result) {
                    Log.i("Oking", result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            String serverId = jsonObject.getString("id");
                            mGreenMissionLog.setServer_id(serverId);
                            GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao().update(mGreenMissionLog);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (mPhotoMedias.size() > 0) {
                        //上传巡查日志的图片
                        uploadPic();
                    } else {
                        uploadLogPic = true;
                    }


                    //上传签名图片
                    uploadSignedPic();


                    //上传巡查视频
                    if (mVideoMedias.size() > 0) {
                        uploadVideo();
                    } else {
                        uploadLogVideo = true;
                    }

                }

                @Override
                public void uploadJobLogFail(Throwable ex) {
                    if (mRxDialogLoading != null) {
                        mRxDialogLoading.cancel();
                    }
                    RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！20", Toast.LENGTH_SHORT, true).show();

                }
            });
        }
        mUploadJobLogPresenter.uploadJobLog(recordLogOV, mGson);

    }

    private void uploadVideo() {
        photoParams = new HashMap<>();
        if (mUploadVideoPresenter == null) {
            mUploadVideoPresenter = new UploadVideoPresenter(new UploadVideoContract.View() {
                @Override
                public void loadVideoSucc(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int code = object.getInt("code");
                        if (code == 200) {
                            veodComPosion++;
                            mRxDialogLoading.getTextView().setText("上传数据中...图片：" + picComPostion + "/" + mPhotoMedias.size() + "视频：" + veodComPosion + "/" + mVideoMedias.size());

                            if (veodComPosion == mVideoMedias.size()) {
                                uploadLogVideo = true;
                                checkChangeState();
                            }
                        } else {
                            if (mRxDialogLoading != null) {
                                mRxDialogLoading.cancel();
                            }
                            RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！1", Toast.LENGTH_SHORT, true).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (mRxDialogLoading != null) {
                            mRxDialogLoading.cancel();
                        }
                        RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！2", Toast.LENGTH_SHORT, true).show();

                    }

                }

                @Override
                public void uploadRetry(Throwable ex) {
                    veodComPosion = 0;
                    RxToast.warning("网络有点开小差~~正在努力重试！！");
                }

                @Override
                public void loadVideoFail(Throwable ex) {

                }

                @Override
                public void uploadIsCount(int pos) {
                    veodComPosion = pos;
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {

                            mRxDialogLoading.getTextView().setText("上传数据中...图片：" + picComPostion + "/" + mPhotoMedias.size() + "视频：" + veodComPosion + "/" + mVideoMedias.size());

                            if (veodComPosion == mVideoMedias.size()) {
                                uploadLogVideo = true;
                                checkChangeState();
                            }
                        }
                    });

                }
            });

        }
        mUploadVideoPresenter.uploadVideo(mGreenMissionLog, mVideoMedias, photoParams, sdfVideo, mGson);

    }

    private void uploadSignedPic() {
        photoParams = new HashMap<>();
        if (mUploadSignaturePicPresenter == null) {
            mUploadSignaturePicPresenter = new UploadSignaturePicPresenter(new UploadSignaturePicContract.View() {
                @Override
                public void uploadSignaturePicSucc(String result) {
                    Log.i("Oking1", "签名上传成功回掉》》》》》》》》》》》》》》》》" + result);
                    try {
                        JSONObject object = new JSONObject(result);
                        int code = object.getInt("code");
                        if (code == 200) {
                            logSignPosion++;
                            if (logSignPosion == mGreenMissionTask.getMembers().size()) {
                                uploadSignPic = true;
                                checkChangeState();
                            }
                        } else {
                            if (mRxDialogLoading != null) {
                                mRxDialogLoading.cancel();
                            }
                            RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！14", Toast.LENGTH_SHORT, true).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (mRxDialogLoading != null) {
                            mRxDialogLoading.cancel();
                        }
                        RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！15", Toast.LENGTH_SHORT, true).show();
                    }

                }

                @Override
                public void uploadIsCount(int pos) {
                    logSignPosion = pos;
                    if (logSignPosion == mGreenMissionTask.getMembers().size()) {
                        uploadSignPic = true;
                        checkChangeState();
                    }

                }

                @Override
                public void uploadRetry(Throwable ex) {
                    logSignPosion = 0;
                    RxToast.warning("网络有点开小差~~正在努力重试！！");
                }

                @Override
                public void uploadSignatureFail(Throwable ex) {
                    if (mRxDialogLoading != null) {
                        mRxDialogLoading.cancel();
                    }
                    RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！16", Toast.LENGTH_SHORT, true).show();

                }
            });

        }
        mUploadSignaturePicPresenter.uploadSignaturePic(mGreenMissionLog, mGreenMissionTask, photoParams);

    }

    private void uploadPic() {
        photoParams = new HashMap<>();
        if (mUploadJobLogForPicPresenter == null) {
            mUploadJobLogForPicPresenter = new UploadJobLogForPicPresenter(new UploadJobLogForPicContract.View() {
                @Override
                public void uploadSucc(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int code = object.getInt("code");
                        if (code == 200) {
                            picComPostion++;
                            mRxDialogLoading.getTextView().setText("上传数据中...图片：" + picComPostion + "/" + mPhotoMedias.size() + "视频：" + veodComPosion + "/" + mVideoMedias.size());
                            if (picComPostion == mPhotoMedias.size()) {
                                uploadLogPic = true;
                                checkChangeState();
                            }
                        } else {
                            if (mRxDialogLoading != null) {
                                mRxDialogLoading.cancel();
                            }
                            RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！1", Toast.LENGTH_SHORT, true).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (mRxDialogLoading != null) {
                            mRxDialogLoading.cancel();
                        }
                        RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！2", Toast.LENGTH_SHORT, true).show();

                    }

                }

                @Override
                public void uploadRetry(Throwable ex) {
                    picComPostion = 0;
                    RxToast.warning("网络有点开小差~~正在努力重试！！");
                }

                @Override
                public void uploadFail(Throwable ex) {
                    if (mRxDialogLoading != null) {
                        mRxDialogLoading.cancel();
                    }
                    Log.i("Oking", "上传失败，请稍后重试！3" + ex.toString());
                    RxToast.error(BaseApplication.getApplictaion(), "当前4G网络不稳定，上传失败，请稍后重试！3", Toast.LENGTH_SHORT, true).show();

                }

                @Override
                public void uploadIsCount(int pos) {
                    picComPostion = pos;
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            mRxDialogLoading.getTextView().setText("上传数据中...图片：" + picComPostion + "/" + mPhotoMedias.size() + "视频：" + veodComPosion + "/" + mVideoMedias.size());

                            if (picComPostion == mPhotoMedias.size()) {
                                uploadLogPic = true;
                                checkChangeState();
                            }
                        }
                    });
                }

                @Override
                public void uploadPositionFail(Throwable ex) {
                    Log.i("Oking", "位置数据解析异常" + ex.toString());
                    RxToast.error(BaseApplication.getApplictaion(), "位置数据解析异常", Toast.LENGTH_SHORT, true).show();

                }
            });
        }
        mUploadJobLogForPicPresenter.uploadJobLogForPic(mGson, photoParams, mGreenMissionLog, mPhotoMedias);

    }

    private void checkChangeState() {
        Log.i("Oking", uploadLogPic + "----uploadLogPic");
        Log.i("Oking", uploadSignPic + "----uploadSignPic");
        Log.i("Oking", uploadLogVideo + "----uploadLogVideo");
        if (uploadLogPic && uploadSignPic && uploadLogVideo) {
            httpCompleteMission();
        }

    }

    private void httpCompleteMission() {
        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .updateMissionRecordState(mGreenMissionLog.getServer_id(), 1)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .observeOn(Schedulers.io())
                .concatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                        return BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                                .updateMissionState(mGreenMissionTask.getTaskid(), "", "", 5);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        if (mRxDialogLoading != null) {
                            mRxDialogLoading.cancel();
                        }
                        String string = responseBody.string();
                        StopSwipeRefreshEvent stopSwipeRefreshEvent = new StopSwipeRefreshEvent();
                        stopSwipeRefreshEvent.setType(0);
                        EventBus.getDefault().post(stopSwipeRefreshEvent);
                        Log.i("Oking", "》》》》》》》更新任务状态成功：" + string);
                        EventBus.getDefault().post(new NewsTaskOV(1,mGreenMissionTask));
                        mGreenMissionTask.setStatus("5");
                        GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mGreenMissionTask);
                        UpdateGreenMissionTaskOV greenMissionOV = new UpdateGreenMissionTaskOV();
                        greenMissionOV.setMissionTask(mGreenMissionTask);
                        EventBus.getDefault().post(greenMissionOV);
                        activity.finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("Oking", ">>>>>>>>>>异常");
                        if (mRxDialogLoading != null) {
                            mRxDialogLoading.cancel();
                        }
                    }
                });

    }

    @Override
    public void refreshList(int adapterPosition) {

        collapse(adapterPosition);
    }

    @Override
    public void refreshMember() {
        //签名
        String memberStr = "";
        mGreenMissionTask.resetMembers();
        if (mGreenMissionTask.getMembers() != null) {
            for (int i = 0; i < mGreenMissionTask.getMembers().size(); i++) {
                GreenMember member = mGreenMissionTask.getMembers().get(i);
                if (member.getUsername().equals(mGreenMissionTask.getReceiver_name())) {
                    memberStr += "<font color=\"#88000000\">任务负责人：</font>";
                    if (member.getSignPic() == null || !new File(member.getSignPic()).exists()) {
                        memberStr += "<font color=\"#88000000\">" + mGreenMissionTask.getReceiver_name() + "&emsp;&emsp;</font>";
                    } else {
                        memberStr += "<font color=\"#98CF60\">" + mGreenMissionTask.getReceiver_name() + "&emsp;&emsp;</font>";
                    }
                    memberStr += "<font color=\"#88000000\">队员：</font>";
                    continue;
                }


                if (member.getSignPic() == null || !new File(member.getSignPic()).exists()) {
                    //未签名
                    memberStr += "<font color=\"#88000000\">" + member.getUsername() + "&emsp;</font>";
                } else {
                    //已签名
                    memberStr += "<font color=\"#98CF60\">" + member.getUsername() + "&emsp;</font>";
                }
            }
        }
        mMemberTextView.setText(Html.fromHtml(memberStr));
    }


    public List<GreenMedia> getPhotoMedias() {
        return mPhotoMedias;
    }

    public List<GreenMedia> getVideoMedias() {
        return mVideoMedias;
    }

    public void refreshEquipment(List<GreenEquipment> checkItem) {
        String equipmentString = createEquipmentString(checkItem);
        mEquipmentTextView.setText(equipmentString);
    }

    private String createEquipmentString(List<GreenEquipment> checkItem) {
        String dataStr = "", trafficStr = "", communicationStr = "", forensicsStr = "", officeStr = "";

        for (int i = 0; i < checkItem.size(); i++) {
            GreenEquipment e = checkItem.get(i);
            if ("交通工具".equals(e.getMc1())) {
                trafficStr = trafficStr + e.getValue() + ",";
            } else if ("通讯工具".equals(e.getMc1())) {
                communicationStr = communicationStr + e.getValue() + ",";
            } else if ("取证工具".equals(e.getMc1())) {
                forensicsStr = forensicsStr + e.getValue() + ",";
            } else if ("办公设备及场所".equals(e.getMc1())) {
                officeStr = officeStr + e.getValue() + ",";
            }
        }

        if (!"".equals(trafficStr)) {
            dataStr += "交通工具：" + trafficStr.subSequence(0, trafficStr.length() - 1) + "  ";
        }

        if (!"".equals(communicationStr)) {
            dataStr += "通讯工具：" + communicationStr.subSequence(0, communicationStr.length() - 1) + "  ";
        }

        if (!"".equals(forensicsStr)) {
            dataStr += "取证工具：" + forensicsStr.subSequence(0, forensicsStr.length() - 1);
        }

        if (!"".equals(officeStr)) {
            dataStr += "办公设备及场所：" + officeStr.subSequence(0, officeStr.length() - 1);
        }


        return dataStr;
    }


    private String getLocationTrajectory() {

        Long beginTime = mGreenMissionTask.getExecute_start_time();
        if (beginTime == null) {
            beginTime = System.currentTimeMillis() - 1000 * 60 * 20;
        }
        Long endTime = mGreenMissionTask.getExecute_end_time();
        if (endTime == null) {
            endTime = System.currentTimeMillis();
            mGreenMissionTask.setExecute_end_time(endTime);
        }
        mBeforTime = beginTime - 24 * 60 * 60 * 1000;
        final String file1 = sdf.format(beginTime);

        final ArrayList<Point> locationPath = new ArrayList<>();

        mDatePoor = Util.getDatePoor(beginTime, endTime);
        if (mDatePoor < 1) {        //表示在同一天
//                    Log.i("Oking","是同一天");
            List<String> locationPos = FileUtil.readFile2List(Environment.getExternalStorageDirectory() + "/oking/location/" + file1 + ".txt", "UTF-8");
            if (locationPos != null) {
                for (String s : locationPos) {
                    String[] items = s.split(",");
                    if (items.length != 3) {
                        continue;
                    }

                    String mLatitude = items[0];
                    String mLongitude = items[1];
                    String mDatetime = items[2];

                    if (Long.parseLong(mDatetime) > beginTime && Long.parseLong(mDatetime) < endTime) {
                        Point location = new Point();
                        location.setLatitude(Double.valueOf(mLatitude));
                        location.setLongitude(Double.valueOf(mLongitude));
                        location.setDatetime(Long.valueOf(mDatetime));
                        locationPath.add(location);
                    }
                }
            }


        } else {

            for (int i = 0; i <= mDatePoor; i++) {
                File file = new File(Environment.getExternalStorageDirectory() + "/oking/location/" + getAfterData(mBeforTime) + ".txt");

                if (file.exists()) {
//                            Log.i("Oking","不是同一天"+file.getName());
                    List<String> locationPos = FileUtil.readFile2List(file, "UTF-8");
                    for (String s : locationPos) {
                        String[] items = s.split(",");
                        if (items.length != 3) {
                            continue;
                        }

                        String Latitude = items[0];
                        String Longitude = items[1];
                        String datetime = items[2];

                        if (Long.parseLong(datetime) > beginTime && Long.parseLong(datetime) < endTime) {

                            Point location = new Point();
                            location.setLatitude(Double.valueOf(Latitude));
                            location.setLongitude(Double.valueOf(Longitude));
                            location.setDatetime(Long.valueOf(datetime));
                            locationPath.add(location);
                        }
                    }

                }

            }
        }

        //筛选一下，不然点集太多
        if (locationPath.size() > 100) {
            ArrayList<Point> newLocationPath = new ArrayList<>();
            for (int i = 0; i < locationPath.size(); i = i + 2) {

                newLocationPath.add(locationPath.get(i));
            }

            mLocJson = mGson.toJson(newLocationPath);

        } else {

            mLocJson = mGson.toJson(locationPath);
        }

        return mLocJson;
    }

    private String getAfterData(long time) {
        //如果需要向后计算日期 -改为+
        Date newDate = new Date(time + 24 * 60 * 60 * 1000);
        mBeforTime = newDate.getTime();
        String dateOk = sdf.format(newDate);
        return dateOk;
    }
}
