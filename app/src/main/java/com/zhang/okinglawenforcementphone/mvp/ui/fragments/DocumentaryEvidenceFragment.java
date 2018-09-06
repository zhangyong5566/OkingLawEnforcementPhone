package com.zhang.okinglawenforcementphone.mvp.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogSure;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.EvidencePicRecyAdapter;
import com.zhang.okinglawenforcementphone.adapter.SourceArrayRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenCase;
import com.zhang.okinglawenforcementphone.beans.GreenEvidence;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceDao;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceMedia;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceSZOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.SaveOrRemoveDataEvent;
import com.zhang.okinglawenforcementphone.beans.SourceArrayOV;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.EvidenceManagerActivity;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

/**
 * 书证录入
 */
public class DocumentaryEvidenceFragment extends Fragment {

    private static final int PHOTO_FROM_CAMERA = 100;
    private static final int PHOTO_FROM_GALLERY = 101;

    private GreenEvidence myEvidence;
    private GreenCase mycase;
    private TextView evidence_name_tv, evidence_source_tv, evidence_content_tv, evidence_remark_tv;
    private TextView evidence_getLocation_tv, evidence_man_textView, evidence_dept_tv, evidence_pagerCount_tv;
    private TextView evidence_source_spinner;
    private Button save_button, close_button;
    private static final String ARG_PARAM3 = "param3";
    private EvidencePicRecyAdapter picAdapter;
    private ArrayList<GreenEvidenceMedia> picList = new ArrayList<>();
//    private Uri picuri;

    private File documentaryStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/case_documentary");
    private RxDialogSureCancel mRxDialogSureCancel;
    private int mType;
    private View mInflate;
    private long mEvidenceId;
    private ImageView mIv_addpic;
    private File mFile;
    private EvidenceManagerActivity mEvidenceManagerActivity;
    private ArrayList<SourceArrayOV> mPlandataOVS;
    private DialogUtil mDialogUtil;
    private View mButtonDailog;
    private TextView mTv_title;
    private SourceArrayRecyAdapter mSourceArrayRecyAdapter;

    public DocumentaryEvidenceFragment() {
        // Required empty public constructor
    }

    public static DocumentaryEvidenceFragment newInstance(int type) {
        DocumentaryEvidenceFragment fragment = new DocumentaryEvidenceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM3, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_PARAM3);
        }
    }

    public int getType() {
        return mType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_documentary_evidence, container, false);
        }
        EventBus.getDefault().register(this);
        initView(mInflate);
        return mInflate;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent1(SaveOrRemoveDataEvent event) {

        if (event.type == 0) {             //保存数据

        } else {                            //不保存数据
            GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceDao().deleteByKey(mEvidenceId);
        }
    }

    public void initView(View rootView) {
        evidence_name_tv = (TextView) rootView.findViewById(R.id.evidence_name_tv);
        evidence_source_spinner = (TextView) rootView.findViewById(R.id.evidence_source_spinner);
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
            mTv_title.setText("证据来源");
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
                    switch (sourceArrayOV.getSource()){
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
                    mDialogUtil.cancelDialog();
                }
            });
        }
        evidence_content_tv = (TextView) rootView.findViewById(R.id.evidence_content_tv);
        evidence_remark_tv = (TextView) rootView.findViewById(R.id.evidence_remark_tv);
        evidence_getLocation_tv = (TextView) rootView.findViewById(R.id.evidence_getLocation_tv);
        evidence_man_textView = (TextView) rootView.findViewById(R.id.evidence_man_textView);
        evidence_dept_tv = (TextView) rootView.findViewById(R.id.evidence_dept_tv);
        evidence_pagerCount_tv = (TextView) rootView.findViewById(R.id.evidence_pagerCount_tv);
        mIv_addpic = rootView.findViewById(R.id.iv_addpic);
        save_button = (Button) rootView.findViewById(R.id.save_button);

        if (myEvidence != null) {
            mEvidenceId = myEvidence.getId();
            evidence_name_tv.setText(myEvidence.getZJMC());
//            evidence_source_spinner.setText(myEvidence.getZJLYMC());
            evidence_content_tv.setText(myEvidence.getZJNR());
            evidence_remark_tv.setText(myEvidence.getBZ());
            evidence_getLocation_tv.setText(myEvidence.getCJDD());
            evidence_man_textView.setText(myEvidence.getJZR());
            evidence_dept_tv.setText(myEvidence.getDW());
            evidence_pagerCount_tv.setText(myEvidence.getYS());

            List<GreenEvidenceMedia> greenMedia = myEvidence.getGreenMedia();
            if (greenMedia != null && greenMedia.size() > 0) {
                picList.clear();
                for (GreenEvidenceMedia media : greenMedia) {
                    if (media.getType() == 1) {

                        picList.add(media);
                    }
                }
            }

        } else {
            myEvidence = new GreenEvidence();
            myEvidence.setGreenCaseId(mycase.getId());
            myEvidence.setZJID(UUID.randomUUID().toString());
            myEvidence.setAJID(mycase.getAJID());
            myEvidence.setZJLX("SZ");
            mEvidenceId = GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceDao().insert(myEvidence);
        }

        if (mType == 0) {
            save_button.setVisibility(View.GONE);
            mIv_addpic.setVisibility(View.GONE);
        } else {
            save_button.setVisibility(View.VISIBLE);
            mIv_addpic.setVisibility(View.VISIBLE);
        }

        evidence_source_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSourceArrayRecyAdapter.setNewData(mPlandataOVS);
                mDialogUtil.showBottomDialog(mEvidenceManagerActivity, mButtonDailog, 300f);
            }
        });
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (localSaveEvidence()) {
                    final RxDialogSure rxDialogSure = new RxDialogSure(getActivity(), false, null);
                    rxDialogSure.setTitle("提示");
                    rxDialogSure.setContent("保存成功");
                    rxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rxDialogSure.cancel();
                            mEvidenceManagerActivity.setVisibleAdd(true);
                            GreenEvidenceSZOV greenEvidenceOV = new GreenEvidenceSZOV();
                            greenEvidenceOV.setType(mType);
                            greenEvidenceOV.setGreenEvidence(myEvidence);
                            EventBus.getDefault().post(greenEvidenceOV);
                            getFragmentManager().beginTransaction().remove(DocumentaryEvidenceFragment.this).commit();
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
                GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceDao().deleteByKey(mEvidenceId);
                getFragmentManager().beginTransaction().remove(DocumentaryEvidenceFragment.this).commit();

            }
        });

        mIv_addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!documentaryStorageDir.exists()) {
                    documentaryStorageDir.mkdirs();
                }

                mDataPicFile = new File(documentaryStorageDir.getPath(), android.text.format.DateFormat
                        .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
                        + ".jpg");
                mDataPicFile.getParentFile().mkdirs();
                startActivityForResult(
                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mDataPicFile)),
                        PHOTO_FROM_CAMERA);
            }
        });
        setPicGridView(rootView);
    }


    private File mDataPicFile;

    private void setPicGridView(View rootView) {
        RecyclerView picGridView = (RecyclerView) rootView.findViewById(R.id.pic_gridView);
        picGridView.setLayoutManager(new GridLayoutManager(BaseApplication.getApplictaion(), 5));
        picAdapter = new EvidencePicRecyAdapter(R.layout.pic_item, picList, getActivity(), mType != 0, "书证");
        picAdapter.setOnClickListener(new EvidencePicRecyAdapter.OnClickListener() {
            @Override
            public void onLongItemClick(final EvidencePicRecyAdapter adapter, final GreenEvidenceMedia greenMedia, final int position) {
                if (mRxDialogSureCancel == null) {
                    mRxDialogSureCancel = new RxDialogSureCancel(getContext());
                    mRxDialogSureCancel.setTitle("提示");
                    mRxDialogSureCancel.setContent("是否删除原图片？");
                }

                mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String greenMediaPath = greenMedia.getPath();


                        File file = new File(greenMediaPath);
                        if (file.exists()) {
                            file.delete();

                        }
                        picList.remove(position);
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
        });

        picGridView.setAdapter(picAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mEvidenceManagerActivity = (EvidenceManagerActivity) context;
        mEvidenceManagerActivity.setVisibleAdd(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case PHOTO_FROM_GALLERY:
                    Uri picUri = data.getData();
                    if (picUri == null) {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        picUri = Uri.parse(MediaStore.Images.Media.insertImage(DocumentaryEvidenceFragment.this.getActivity().getContentResolver(), bitmap, null, null));
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                    }


                    String path = FileUtil.praseUritoPath(BaseApplication.getApplictaion(), picUri);
                    FileUtil.compressImage(path);

                    GreenEvidenceMedia greenMedia = new GreenEvidenceMedia();
                    greenMedia.setType(1);
                    greenMedia.setPath(path);
                    greenMedia.setGreenEvidenceId(myEvidence.getId());
                    GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceMediaDao().insert(greenMedia);
                    picList.add(greenMedia);
                    picAdapter.notifyDataSetChanged();
                    break;
                case PHOTO_FROM_CAMERA:

                    if (mDataPicFile != null && mDataPicFile.exists()) {
                        if (mFile == null) {
                            mFile = new File(mDataPicFile.getParentFile().getPath() + "/luban");
                            if (!mFile.exists()) {
                                mFile.mkdir();
                            }
                        }
                        Luban.with(BaseApplication.getApplictaion())
                                .load(mDataPicFile.getPath())                                   // 传人要压缩的图片列表
                                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                                .setTargetDir(mFile.getPath())                        // 设置压缩后文件存储位置
                                .setCompressListener(new OnCompressListener() { //设置回调
                                    @Override
                                    public void onStart() {
                                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        // TODO 压缩成功后调用，返回压缩后的图片文件
                                        Log.i("Oking", "压缩成功：" + file.getPath());
                                        Log.i("Oking", "path:" + file.getPath());
                                        GreenEvidenceMedia greenMedia1 = new GreenEvidenceMedia();
                                        greenMedia1.setType(1);
                                        greenMedia1.setPath("file://" + file.getPath());
                                        greenMedia1.setGreenEvidenceId(mEvidenceId);
                                        GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceMediaDao().insert(greenMedia1);
                                        picList.add(greenMedia1);
                                        picAdapter.notifyDataSetChanged();
                                        //通知系统扫描文件
                                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        intent.setData(Uri.fromFile(file));
                                        DocumentaryEvidenceFragment.this.getContext().sendBroadcast(intent);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        // TODO 当压缩过程出现问题时调用
                                    }
                                }).launch();    //启动压缩

                    }

                    break;
                default:
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean localSaveEvidence() {
        if (evidence_source_spinner.getText().toString().trim().equals("*请选择")){
            RxToast.warning("证据来源不能为空！");
            return false;
        }

        if ("".equals(evidence_name_tv.getText().toString())) {
            RxToast.warning("证据名称不能为空！");
            return false;
        }
        if ("".equals(evidence_content_tv.getText().toString())) {
            RxToast.warning("证据内容不能为空！");
            return false;
        }
        if ("".equals(evidence_getLocation_tv.getText().toString())) {
            RxToast.warning("采集地点不能为空！");
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
