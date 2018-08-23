package com.zhang.okinglawenforcementphone.mvp.ui.fragments;


import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxDialogSure;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.NetUtil;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.CaseAudioVideoEvidenceListRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenCase;
import com.zhang.okinglawenforcementphone.beans.GreenEvidence;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceMedia;
import com.zhang.okinglawenforcementphone.beans.GreenEvidenceSTZJOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaseAudioVideoEvidenceListFragment extends Fragment {
    private static final String ARG_PARAM2 = "param2";
    private String mParam2;
    private GreenCase mycase;

    private RecyclerView ryMain;
    private ArrayList<GreenEvidence> evidences = new ArrayList<>();

    private boolean uploadSound, uploadVideo;
    private int  uploadSoundCount, uploadVideoCount;

    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private CaseAudioVideoEvidenceListRecyAdapter mCaseAudioVideoEvidenceListRecyAdapter;
    private RxDialogSureCancel mRxDialogSureCancel;
    private RxDialogLoading mRxDialogLoading;
    private ArrayList<GreenEvidenceMedia> mPicGreenMediaList = new ArrayList<>();
    private ArrayList<GreenEvidenceMedia> mVoiceGreenMediaList = new ArrayList<>();
    private ArrayList<GreenEvidenceMedia> mVideoGreenMediaList = new ArrayList<>();
    private View mInflate;

    public CaseAudioVideoEvidenceListFragment() {
        // Required empty public constructor
    }

    public static CaseAudioVideoEvidenceListFragment newInstance( String param2) {
        CaseAudioVideoEvidenceListFragment fragment = new CaseAudioVideoEvidenceListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mInflate ==null){
            mInflate = inflater.inflate(R.layout.fragment_case_audio_video_evidence_list, container, false);

        }
        EventBus.getDefault().register(this);
        initView(mInflate);
        return mInflate;
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    public void initView(View rootView) {
        ryMain = rootView.findViewById(R.id.ry_main);
        ryMain.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mCaseAudioVideoEvidenceListRecyAdapter = new CaseAudioVideoEvidenceListRecyAdapter(R.layout.list_item_audiovideoevidence, null);
        ryMain.setAdapter(mCaseAudioVideoEvidenceListRecyAdapter);
        loadEvidence();
        mCaseAudioVideoEvidenceListRecyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.upload_button:            //上传
                        if (NetUtil.isConnected(BaseApplication.getApplictaion())) {
                            GreenEvidence greenEvidence = evidences.get(position);
                            List<GreenEvidenceMedia> greenMedias = greenEvidence.getGreenMedia();
                            for (GreenEvidenceMedia greenMedia : greenMedias) {
                                if (greenMedia.getType()==1){
                                    mPicGreenMediaList.clear();
                                    mPicGreenMediaList.add(greenMedia);
                                }else if (greenMedia.getType()==2){
                                    mVideoGreenMediaList.clear();
                                    mVideoGreenMediaList.add(greenMedia);
                                }else if (greenMedia.getType()==3){
                                    mVoiceGreenMediaList.clear();
                                    mVoiceGreenMediaList.add(greenMedia);
                                }
                            }

                            saveEvidence(evidences.get(position));
                        } else {
                            RxToast.warning(BaseApplication.getApplictaion(), "网络无连接", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.delete_button:            //删除
                        if (mRxDialogSureCancel == null) {

                            mRxDialogSureCancel = new RxDialogSureCancel(getActivity());
                            mRxDialogSureCancel.setTitle("提示");
                            mRxDialogSureCancel.setContent("是否删除证据");
                        }
                        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceDao().delete(evidences.get(position));
                                evidences.remove(position);
                                mCaseAudioVideoEvidenceListRecyAdapter.setNewData(evidences);
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

                        break;
                    case R.id.edit_button:            //查看编辑
                        if (((Button) view).getText().equals("查看")) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();

                            CaseAudioVideoEvidenceFragment caseAudioVideoEvidenceFragment = CaseAudioVideoEvidenceFragment.newInstance(0);
                            caseAudioVideoEvidenceFragment.setGreenCase(mycase, evidences.get(position));
                            ft.add(R.id.rl_sub_content, caseAudioVideoEvidenceFragment).commit();
                        } else {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            CaseAudioVideoEvidenceFragment caseAudioVideoEvidenceFragment = CaseAudioVideoEvidenceFragment.newInstance(1);
                            caseAudioVideoEvidenceFragment.setGreenCase(mycase, evidences.get(position));
                            ft.add(R.id.rl_sub_content, caseAudioVideoEvidenceFragment).commit();
                        }

                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent1(GreenEvidenceSTZJOV event) {
        if (event.getType() == 2) {            //添加征据
            evidences.add(event.getGreenEvidence());
            mCaseAudioVideoEvidenceListRecyAdapter.setNewData(evidences);
        } else {
            loadEvidence();
        }

    }

    /**
     * 上传数据
     * @param evidence
     */
    private void saveEvidence(final GreenEvidence evidence) {

        if (mRxDialogLoading == null) {

            mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mRxDialogLoading.cancel();
                }
            });
        }
        mRxDialogLoading.setLoadingText("上传数据中...");
        mRxDialogLoading.show();



        Map<String, Object> fields = new HashMap<>();
        if (evidence.getZJID() != null) {

            fields.put("zjid", evidence.getZJID());
        }

        if (evidence.getAJID() != null) {
            fields.put("ajid", evidence.getAJID());
        }
        if (evidence.getZJLX() != null) {
            fields.put("zjlx", evidence.getZJLX());
        }
        if (evidence.getZJMC() != null) {
            fields.put("zjmc", evidence.getZJMC());
        }
        if (evidence.getZJLY() != null) {
            fields.put("zjly", evidence.getZJLY());
        }
        if (evidence.getZJNR() != null) {
            fields.put("zjnr", evidence.getZJNR());
        }
        if (evidence.getSL() != null) {
            fields.put("sl", evidence.getSL());
        }


        if (((Long) evidence.getCJSJ()) != null) {
            fields.put("cjsj", mSimpleDateFormat.format(evidence.getCJSJ()));
        }
        if (evidence.getCJR() != null) {
            fields.put("cjr", evidence.getCJR());
        }
        if (evidence.getCJDD() != null) {
            fields.put("cjdd", evidence.getCJDD());
        }
        if (evidence.getJZR() != null) {
            fields.put("jzr", evidence.getJZR());
        }
        if (evidence.getDW() != null) {
            fields.put("dw", evidence.getDW());
        }
        if (evidence.getBZ() != null) {
            fields.put("bz", evidence.getBZ());
        }
        if (OkingContract.CURRENTUSER != null) {
            fields.put("scr", OkingContract.CURRENTUSER.getUserName());
        }
        fields.put("scsj", mSimpleDateFormat.format(System.currentTimeMillis()));

        if (evidence.getZT() != null) {
            fields.put("zt", evidence.getZT());
        }
        if (evidence.getWSID() != null) {
            fields.put("wsid", evidence.getWSID());
        }
        if (evidence.getLXMC() != null) {
            fields.put("lxmc", evidence.getLXMC());
        }
        if (evidence.getZJLYMC() != null) {
            fields.put("zjlymc", evidence.getZJLYMC());
        }
        if (evidence.getYS() != null) {
            fields.put("ys", evidence.getYS());
        }



        BaseHttpFactory.getInstence().createService(GDWaterService.class, Api.BASE_URL)
                .uploadEvidence(fields)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        if ("success".equals(result)) {
                            uploadEvidenceFile(evidence);
                        } else {
                            RxToast.error(BaseApplication.getApplictaion(), "上传证据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

    }

    private void uploadEvidenceFile(final GreenEvidence evidence) {

        uploadSound = true;
        uploadVideo = true;
        uploadVideoCount = 0;
        uploadSoundCount = 0;

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                for (int i = 0; i < mVideoGreenMediaList.size(); i++) {

                    uploadVideo = false;

                    String videoPath = mVideoGreenMediaList.get(i).getPath();
                    uploadFile(videoPath, "mp4", evidence);
                }

                for (int i = 0; i < mVoiceGreenMediaList.size(); i++) {

                    uploadSound = false;
                    String voicePath = mVoiceGreenMediaList.get(i).getPath();
                    uploadFile(voicePath, "m4a", evidence);
                }
                e.onNext(1);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
                if (value == 1) {
                    checkChangeState(evidence);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    private void uploadFile(String path, final String type, final GreenEvidence evidence) {


        Map<String, RequestBody> fileParams = new HashMap<>();
        fileParams.put("zjid", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), evidence.getZJID()));
        fileParams.put("type", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), type));
        fileParams.put("ajid", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), evidence.getAJID()));
        fileParams.put("userid", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), OkingContract.CURRENTUSER.getUserid()));
        File file = new File(Uri.parse(path).getPath());
        fileParams.put("files" + "\"; filename=\"" + file.getName(), RequestBody.create(MediaType.parse("video/mp4"), file));

        BaseHttpFactory.getInstence().createService(GDWaterService.class,Api.BASE_URL)
                .uploadEvidenceFiles(fileParams)
                .compose(RxSchedulersHelper.<ResponseBody>io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        if ("success".equals(result)) {
                            switch (type) {
                                case "mp4":
                                    uploadVideoCount++;
                                    if (uploadVideoCount == mVideoGreenMediaList.size()) {
                                        uploadVideo = true;
                                        checkChangeState(evidence);
                                    }
                                    break;
                                case "m4a":
                                    uploadSoundCount++;
                                    if (uploadSoundCount == mVoiceGreenMediaList.size()) {
                                        uploadSound = true;
                                        checkChangeState(evidence);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            mRxDialogLoading.cancel();
                            RxToast.error(BaseApplication.getApplictaion(), "上传证据附件失败！", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRxDialogLoading.cancel();
                        RxToast.error(BaseApplication.getApplictaion(), "上传附件失败！2"+throwable.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    public void notifyDataSetChanged() {
        if (mCaseAudioVideoEvidenceListRecyAdapter != null) {
            mCaseAudioVideoEvidenceListRecyAdapter.notifyDataSetChanged();
        }
    }

    private void loadEvidence() {
        evidences.clear();
        if (mycase != null) {
            for (int i = 0; i < mycase.getGreenEvidence().size(); i++) {
                if ("YYSP".equals(mycase.getGreenEvidence().get(i).getOtype()) || "SP".equals(mycase.getGreenEvidence().get(i).getOtype())
                        || "YY".equals(mycase.getGreenEvidence().get(i).getOtype())) {
                    evidences.add(mycase.getGreenEvidence().get(i));
                }
            }
            mCaseAudioVideoEvidenceListRecyAdapter.setNewData(evidences);
        }
    }

    private void checkChangeState(GreenEvidence evidence) {
        if (uploadSound && uploadVideo ) {
            mRxDialogLoading.cancel();
            evidence.setIsUpload(true);
            GreenDAOManager.getInstence().getDaoSession().getGreenEvidenceDao().update(evidence);
            mCaseAudioVideoEvidenceListRecyAdapter.notifyDataSetChanged();
            GreenDAOManager.getInstence().getDaoSession().getGreenCaseDao().update(mycase);
            final RxDialogSure rxDialogSure = new RxDialogSure(getActivity());
            rxDialogSure.setTitle("提示");
            rxDialogSure.setContent("上传成功！");
            rxDialogSure.show();
            rxDialogSure.getTvSure().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rxDialogSure.cancel();
                }
            });

        }
    }


    public void setGreenCase(GreenCase greenCase){
        this.mycase = greenCase;
    }
}
