package com.zhang.okinglawenforcementphone.mvp.ui.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.EMError;
import com.hyphenate.easeui.model.EaseVoiceRecorder;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogSure;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.MediaManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.CaseSimpleAdapter;
import com.zhang.okinglawenforcementphone.adapter.SoundSimpleAdapter;
import com.zhang.okinglawenforcementphone.adapter.SourceArrayRecyAdapter;
import com.zhang.okinglawenforcementphone.adapter.SpinnerArrayAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenCase;
import com.zhang.okinglawenforcementphone.beans.GreenEvidence;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceDao;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceMedia;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceSTZJOV;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.SaveOrRemoveDataEvent;
import com.zhang.okinglawenforcementphone.beans.SourceArrayOV;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.EvidenceManagerActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.VideoRecordActivity;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaseAudioVideoEvidenceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_TYPE = "type";
    private static final String ARG_EVIDENCE = "evidence";
    //    private static final int PHOTO_FROM_CAMERA = 100;
//    private static final int PHOTO_FROM_GALLERY = 101;
    private static final int VIDEO_FROM_CAMERA = 102;
    private static final int VIDEO_FROM_GALLERY = 103;

    private GreenEvidence myEvidence;
    private GreenCase mycase;
    private TextView evidence_name_tv, evidence_content_tv, evidence_remark_tv;
    private TextView evidence_getLocation_tv, evidence_man_textView, evidence_dept_tv, evidence_pagerCount_tv;
    private TextView type_spinner, evidence_source_spinner;
    private Button save_button, close_button;
    private GridView video_gridView, sound_gridView;

    private SoundSimpleAdapter soundAdapter;
    private CaseSimpleAdapter videoAdapter;

    private ArrayList<GreenEvidenceMedia> soundList = new ArrayList<>();
    private ArrayList<GreenEvidenceMedia> videoList = new ArrayList<>();
    private int mType;
    private long mEvidenceId;
    //    private Uri photouri, videouri;


    private RxDialogSureCancel mRxDialogSureCancel;
    private View mInflate;
    private TextView mTv_stop;
    private TextView mTv_cancel;
    private AlertDialog mAlertDialog;
    private EvidenceManagerActivity mEvidenceManagerActivity;
    private ArrayList<SourceArrayOV> mPlandataOVS;
    private ArrayList<SourceArrayOV> mTypedataOVS;
    private DialogUtil mDialogUtil;
    private View mButtonDailog;
    private TextView mTv_title;
    private SourceArrayRecyAdapter mSourceArrayRecyAdapter;

    public CaseAudioVideoEvidenceFragment() {
        // Required empty public constructor
    }

    public static CaseAudioVideoEvidenceFragment newInstance(int type) {
        CaseAudioVideoEvidenceFragment fragment = new CaseAudioVideoEvidenceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_case_forensics, container, false);

        }

        initView(mInflate);
        return mInflate;

    }


    @Override
    public void onDestroyView() {
//        getActivity().unregisterReceiver(mReceiver);
        if (MediaManager.mPlayer != null) {

            MediaManager.mPlayer.reset();
            MediaManager.mPlayer = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mEvidenceManagerActivity = (EvidenceManagerActivity) context;
        mEvidenceManagerActivity.setVisibleAdd(false);
    }

    public void initView(View rootView) {


        evidence_name_tv = (TextView) rootView.findViewById(R.id.evidence_name_tv);
        evidence_source_spinner = (TextView) rootView.findViewById(R.id.evidence_source_spinner);
        video_gridView = (GridView) rootView.findViewById(R.id.video_gridView);
        sound_gridView = (GridView) rootView.findViewById(R.id.sound_gridView);

        String[] plandataset = getResources().getStringArray(R.array.spinner_evidence_source);
        mPlandataOVS = new ArrayList<>();
        for (String s : plandataset) {
            SourceArrayOV sourceArrayOV = new SourceArrayOV();
            sourceArrayOV.setType(0);
            sourceArrayOV.setSource(s);
            mPlandataOVS.add(sourceArrayOV);
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
            mSourceArrayRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    List<SourceArrayOV> data = adapter.getData();
                    SourceArrayOV sourceArrayOV = data.get(position);
                    switch (sourceArrayOV.getType()) {
                        case 0:
                            switch (sourceArrayOV.getSource()) {
                                case "当事人提供":
                                    myEvidence.setZJLY("DSRTG");
                                    myEvidence.setZJLYMC(sourceArrayOV.getSource());
                                    break;
                                case "当事人口述":
                                    myEvidence.setZJLY("DSRKS");
                                    myEvidence.setZJLYMC(sourceArrayOV.getSource());
                                    break;
                                case "调查搜集":
                                    myEvidence.setZJLY("DCSJ");
                                    myEvidence.setZJLYMC(sourceArrayOV.getSource());
                                    break;
                                case "执法人员制作":
                                    myEvidence.setZJLY("ZFRYZZ");
                                    myEvidence.setZJLYMC(sourceArrayOV.getSource());
                                    break;
                                case "执法人员拍摄":
                                    myEvidence.setZJLY("ZFRYPS");
                                    myEvidence.setZJLYMC(sourceArrayOV.getSource());
                                    break;
                                case "局审批科室":
                                    myEvidence.setZJLY("JSPKS");
                                    myEvidence.setZJLYMC(sourceArrayOV.getSource());
                                    break;
                                default:
                                    break;
                            }
                            evidence_source_spinner.setText(sourceArrayOV.getSource());
                            break;
                        case 1:
                            switch (position) {
                                case 0:
                                    video_gridView.setVisibility(View.VISIBLE);
                                    ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
                                    break;
                                case 1:
                                    video_gridView.setVisibility(View.INVISIBLE);
                                    ((LinearLayout) sound_gridView.getParent()).setVisibility(View.VISIBLE);
                                    break;
                                default:
                                    break;
                            }
                            type_spinner.setText(sourceArrayOV.getSource());
                            break;
                        default:
                            break;
                    }

                    mDialogUtil.cancelDialog();
                }
            });
        }


        type_spinner = (TextView) rootView.findViewById(R.id.type_spinner);
        String[] typedataset = getResources().getStringArray(R.array.spinner_data_type);
        mTypedataOVS = new ArrayList<>();
        for (String s : typedataset) {
            SourceArrayOV sourceArrayOV = new SourceArrayOV();
            sourceArrayOV.setType(1);
            sourceArrayOV.setSource(s);
            mTypedataOVS.add(sourceArrayOV);
        }

        evidence_content_tv = (TextView) rootView.findViewById(R.id.evidence_content_tv);
        evidence_remark_tv = (TextView) rootView.findViewById(R.id.evidence_remark_tv);
        evidence_getLocation_tv = (TextView) rootView.findViewById(R.id.evidence_getLocation_tv);
        evidence_man_textView = (TextView) rootView.findViewById(R.id.evidence_man_textView);
        evidence_dept_tv = (TextView) rootView.findViewById(R.id.evidence_dept_tv);
        evidence_pagerCount_tv = (TextView) rootView.findViewById(R.id.evidence_pagerCount_tv);
        save_button = (Button) rootView.findViewById(R.id.save_button);
        if (myEvidence != null) {
            mEvidenceId = myEvidence.getId();
            evidence_name_tv.setText(myEvidence.getZJMC());
            evidence_content_tv.setText(myEvidence.getZJNR());
            evidence_remark_tv.setText(myEvidence.getBZ());
            evidence_getLocation_tv.setText(myEvidence.getCJDD());
            evidence_man_textView.setText(myEvidence.getJZR());
            evidence_dept_tv.setText(myEvidence.getDW());
            evidence_pagerCount_tv.setText(myEvidence.getYS());


            myEvidence.resetGreenMedia();
            List<GreenEvidenceMedia> greenMedia = myEvidence.getGreenMedia();
            if (greenMedia != null && greenMedia.size() > 0) {
                videoList.clear();
                soundList.clear();
                for (GreenEvidenceMedia media : greenMedia) {
                    if (media.getType() == 2) {

                        videoList.add(media);
                    }
                }

                for (GreenEvidenceMedia media : greenMedia) {
                    if (media.getType() == 3) {

                        soundList.add(media);
                    }
                }
            }


            if (videoList != null && videoList.size() > 0) {
                type_spinner.setText("视频");
                video_gridView.setVisibility(View.VISIBLE);
                ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
            } else if (soundList != null && soundList.size() > 0) {
                type_spinner.setText("语音");
                video_gridView.setVisibility(View.INVISIBLE);
                ((LinearLayout) sound_gridView.getParent()).setVisibility(View.VISIBLE);
            } else {
                type_spinner.setText("视频");
                video_gridView.setVisibility(View.INVISIBLE);
                ((LinearLayout) sound_gridView.getParent()).setVisibility(View.INVISIBLE);
            }


        } else {

            myEvidence = new GreenEvidence();
            myEvidence.setGreenCaseId(mycase.getId());
            myEvidence.setZJID(UUID.randomUUID().toString());
            myEvidence.setAJID(mycase.getAJID());
            myEvidence.setZJLX("STZL");
            mEvidenceId = GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceDao().insert(myEvidence);


        }

        if (mType == 0) {
            save_button.setVisibility(View.GONE);

        } else {
            save_button.setVisibility(View.VISIBLE);
        }

        evidence_source_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSourceArrayRecyAdapter.setNewData(mPlandataOVS);
                mDialogUtil.showBottomDialog(mEvidenceManagerActivity, mButtonDailog, 300f);
            }
        });

        type_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSourceArrayRecyAdapter.setNewData(mTypedataOVS);
                mDialogUtil.showBottomDialog(mEvidenceManagerActivity, mButtonDailog, 300f);
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localSaveEvidence()) {

                    final RxDialogSure rxDialogSure = new RxDialogSure(getActivity());
                    rxDialogSure.setTitle("提示");
                    rxDialogSure.setContent("保存成功！");
                    rxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rxDialogSure.cancel();
                            mEvidenceManagerActivity.setVisibleAdd(true);
                            GreenEvidenceSTZJOV greenEvidenceOV = new GreenEvidenceSTZJOV();
                            greenEvidenceOV.setType(mType);
                            greenEvidenceOV.setGreenEvidence(myEvidence);
                            EventBus.getDefault().post(greenEvidenceOV);
                            getFragmentManager().beginTransaction().remove(CaseAudioVideoEvidenceFragment.this).commit();

                        }
                    });
                    rxDialogSure.show();


                }
            }
        });
        close_button = (Button) rootView.findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEvidenceManagerActivity.setVisibleAdd(true);
                getFragmentManager().beginTransaction().remove(CaseAudioVideoEvidenceFragment.this).commit();

            }
        });


        initVoiceRecorder();

        setSoundGridView();
        setVideoGridView();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent1(SaveOrRemoveDataEvent event) {

        if (event.type == 0) {             //保存数据

        } else {                            //不保存数据
            GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceDao().deleteByKey(mEvidenceId);
        }
    }


    private PowerManager.WakeLock wakeLock;
    private EaseVoiceRecorder voiceRecorder;
    private Drawable[] micImages;
    private ImageView mMic_image;
    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // change image
            mMic_image.setImageDrawable(micImages[msg.what]);
        }
    };

    /**
     * 初始化录音
     */
    private void initVoiceRecorder() {
        wakeLock = ((PowerManager) BaseApplication.getApplictaion().getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "oking");

        micImages = new Drawable[]{BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_01),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_02),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_03),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_04),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_05),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_06),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_07),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_08),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_09),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_10),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_11),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_12),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_13),
                BaseApplication.getApplictaion().getResources().getDrawable(com.hyphenate.easeui.R.drawable.ease_record_animate_14),};

        voiceRecorder = new EaseVoiceRecorder(micImageHandler);

    }

    private void setSoundGridView() {


        soundAdapter = new SoundSimpleAdapter(soundList, !myEvidence.getIsUpload());
        soundAdapter.setOnClickListener(new SoundSimpleAdapter.OnClickListener() {

            private AlertDialog.Builder mBuilder;

            @Override
            public void onLongItemClick(final SoundSimpleAdapter adapter, final ArrayList<GreenEvidenceMedia> data, final int position) {
                if (mRxDialogSureCancel == null) {

                    mRxDialogSureCancel = new RxDialogSureCancel(getActivity());

                }
                mRxDialogSureCancel.setTitle("提示");
                mRxDialogSureCancel.setContent("是否删除声音文件？");
                mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String path = data.get(position).getPath();
                        File file = new File(path);
                        if (file.exists()) {
                            file.delete();
                            //ACTION_MEDIA_SCANNER_SCAN_FILE

                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED);
                            intent.setData(Uri.fromFile(file));
                            CaseAudioVideoEvidenceFragment.this.getContext().sendBroadcast(intent);
                        }
                        data.remove(position);
                        adapter.notifyDataSetChanged();


                        mRxDialogSureCancel.cancel();
                    }
                });
                mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRxDialogSureCancel.cancel();
                    }
                });
                mRxDialogSureCancel.show();
            }

            @Override
            public void onAddSoundClick() {
                if (mBuilder == null) {
                    mBuilder = new AlertDialog.Builder(getActivity());
                    View recordingView = View.inflate(getActivity(), R.layout.voice_recorder_dialog, null);
                    mMic_image = recordingView.findViewById(R.id.mic_image);
                    mTv_stop = recordingView.findViewById(R.id.tv_stop);
                    mTv_cancel = recordingView.findViewById(R.id.tv_cancel);
                    mBuilder.setView(recordingView);
                    mAlertDialog = mBuilder.create();
                    mAlertDialog.setCanceledOnTouchOutside(false);
                    mAlertDialog.show();
                    WindowManager.LayoutParams params =
                            mAlertDialog.getWindow().getAttributes();
                    params.width = 580;
                    params.height = 530;
                    mAlertDialog.getWindow().setAttributes(params);
                }

                startRecording();
                mTv_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            int length = stopRecoding();
                            if (length > 0) {

                                GreenEvidenceMedia greenMedia = new GreenEvidenceMedia();
                                greenMedia.setGreenEvidenceId(myEvidence.getGreenCaseId());
                                greenMedia.setPath(Environment.getExternalStorageDirectory() + "/oking/mission_voice/" + getVoiceFileName());
                                greenMedia.setType(3);
                                GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceMediaDao().insert(greenMedia);
                                soundList.add(greenMedia);
                                soundAdapter.notifyDataSetChanged();
                            } else if (length == EMError.FILE_INVALID) {
                                RxToast.error(BaseApplication.getApplictaion(), "录音失败", Toast.LENGTH_SHORT).show();
                            } else {
                                RxToast.warning(BaseApplication.getApplictaion(), "录音时间太短", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            RxToast.error(BaseApplication.getApplictaion(), "录音失败", Toast.LENGTH_SHORT).show();
                        }


                        mAlertDialog.cancel();

                    }
                });

                mTv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopRecoding();
                        mAlertDialog.cancel();
                    }
                });

            }
        });

        sound_gridView.setAdapter(soundAdapter);

    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    private int stopRecoding() {
        if (wakeLock.isHeld()) {

            wakeLock.release();
        }
        return voiceRecorder.stopRecoding();
    }


    private void startRecording() {
        if (!EaseCommonUtils.isSdcardExist()) {
            RxToast.error(BaseApplication.getApplictaion(), "请插上sd卡", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            wakeLock.acquire();
            voiceRecorder.setRecordTag(true);
            voiceRecorder.startRecording(BaseApplication.getApplictaion());
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld()) {

                wakeLock.release();
            }
            if (voiceRecorder != null) {

                voiceRecorder.discardRecording();
            }
            RxToast.error(BaseApplication.getApplictaion(), "录音失败,请重试!", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private void setVideoGridView() {

        videoAdapter = new CaseSimpleAdapter(videoList, this, mType != 0, "视听资料");
        videoAdapter.setOnClickListener(new CaseSimpleAdapter.OnClickListener() {
            @Override
            public void onAddVideo() {


                Intent intent = new Intent();
                intent.setClass(getActivity(), VideoRecordActivity.class);
                CaseAudioVideoEvidenceFragment.this.startActivityForResult(intent, VIDEO_FROM_CAMERA);
            }

            @Override
            public void onLongItemClick(final CaseSimpleAdapter adapter, final ArrayList<GreenEvidenceMedia> data, final int position) {

                if (mRxDialogSureCancel == null) {

                    mRxDialogSureCancel = new RxDialogSureCancel(getActivity());

                }
                mRxDialogSureCancel.setTitle("提示");
                mRxDialogSureCancel.setContent("是否删除原视频？");
                mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mRxDialogSureCancel.cancel();
                    }
                });

                mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String path = data.get(position).getPath();
                        File file = new File(path);
                        if (file.exists()) {
                            file.delete();

                        }
                        data.remove(position);
                        adapter.notifyDataSetChanged();
                        mRxDialogSureCancel.cancel();
                    }
                });

                mRxDialogSureCancel.show();
            }
        });

        video_gridView.setAdapter(videoAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case VIDEO_FROM_CAMERA:

                    Uri videouri = data.getData();
                    GreenEvidenceMedia greenMedia = new GreenEvidenceMedia();
                    greenMedia.setType(2);
                    greenMedia.setPath(videouri.toString());
                    greenMedia.setGreenEvidenceId(myEvidence.getId());
                    GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceMediaDao().insert(greenMedia);
                    videoList.add(greenMedia);
                    videoAdapter.notifyDataSetChanged();

                    //通知系统扫描文件
                    Intent intent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent2.setData(videouri);
                    CaseAudioVideoEvidenceFragment.this.getContext().sendBroadcast(intent2);
                    break;
                case VIDEO_FROM_GALLERY:
                    Uri videoUri = data.getData();
                    GreenEvidenceMedia greenMedia2 = new GreenEvidenceMedia();
                    greenMedia2.setType(2);
                    greenMedia2.setPath(videoUri.toString());
                    greenMedia2.setGreenEvidenceId(myEvidence.getId());
                    GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceMediaDao().insert(greenMedia2);
                    videoList.add(greenMedia2);
                    videoAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

//            localSaveEvidence();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean localSaveEvidence() {
        if (type_spinner.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("证据类型不能为空！");
            return false;
        }
        if (evidence_source_spinner.getText().toString().trim().equals("*请选择")) {
            RxToast.warning("证据来源不能为空！");
            return false;
        }

        if ("".equals(evidence_name_tv.getText().toString())) {
            Toast.makeText(getContext(), "证据名称不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (evidence_source_spinner.getText().toString().equals("")) {
//            Toast.makeText(getContext(), "证据来源不能为空！", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if ("".equals(evidence_content_tv.getText().toString())) {
            Toast.makeText(getContext(), "证据内容不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(evidence_getLocation_tv.getText().toString())) {
            Toast.makeText(getContext(), "采集地点不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (soundList.size() > 0 || videoList.size() > 0) {

        } else {
            Toast.makeText(getContext(), "请录入语音或视频！", Toast.LENGTH_SHORT).show();
            return false;
        }


        myEvidence.setZJMC(evidence_name_tv.getText().toString());
        myEvidence.setZJNR(evidence_content_tv.getText().toString());
        myEvidence.setBZ(evidence_remark_tv.getText().toString());
        myEvidence.setCJDD(evidence_getLocation_tv.getText().toString());
        myEvidence.setJZR(evidence_man_textView.getText().toString());
        myEvidence.setDW(evidence_dept_tv.getText().toString());
        myEvidence.setYS(evidence_pagerCount_tv.getText().toString());
        myEvidence.setCJSJ(System.currentTimeMillis());
        myEvidence.setCJR(OkingContract.CURRENTUSER.getUserName());

        if (videoList.size() > 0 && soundList.size() > 0) {
            myEvidence.setOtype("YYSP");            //语音、视频
        } else if (videoList.size() > 0 && soundList.size() < 1) {
            myEvidence.setOtype("SP");          //视频
        } else if (videoList.size() < 1 && soundList.size() > 0) {
            myEvidence.setOtype("YY");          //语音
        }

        if (mType == 0 || mType == 2 && myEvidence != null) {
            mycase.getGreenEvidence().add(myEvidence);
        }

        GreenEvidence unique = GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceDao().queryBuilder().where(GreenEvidenceDao.Properties.Id.eq(mEvidenceId)).unique();
        if (unique == null) {
            myEvidence.setGreenCaseId(mycase.getId());
            GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceDao().insert(myEvidence);
        } else {
            GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceDao().update(myEvidence);
        }

        return true;
    }


    public void setGreenCase(GreenCase greenCase, GreenEvidence greenEvidence) {
        this.mycase = greenCase;
        this.myEvidence = greenEvidence;
    }
}
