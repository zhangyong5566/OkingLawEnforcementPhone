package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.VideoSimpleAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenLocation;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.PlayVideoOnlineActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.VideoRecordActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class TaskVideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_addvideo)
    ImageView mIvAddvideo;
    @BindView(R.id.video_gridView)
    RecyclerView mVideoGridView;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private GreenMissionLog mGreenMissionLog;
    private GreenMissionTask mGreenMissionTask;
    private List<GreenMedia> mVideoMedias = new ArrayList<>();
    private VideoSimpleAdapter videoadapter;
    public static final int VIDEO_FROM_CAMERA = 102;
    private static final int VIDEO_FROM_GALLERY = 103;
    private Intent mIntent;
    private Long mGreenGreenLocationId;
    private Random mRandom = new Random();
    private RxDialogSureCancel mRxDialogSureCancel;

    public TaskVideoFragment() {
        // Required empty public constructor
    }

    public static TaskVideoFragment newInstance(String param1, String param2) {
        TaskVideoFragment fragment = new TaskVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_task_video, container, false);
        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListerner();
        return mInflate;
    }

    private void setListerner() {
        videoadapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<GreenMedia> datas = adapter.getData();
                Uri uri = Uri.parse(datas.get(position).getPath());
                Intent intent = new Intent(getActivity(), PlayVideoOnlineActivity.class);
                intent.putExtra("path", uri.getPath());
                startActivity(intent);
            }
        });
        videoadapter.setOnClickListener(new VideoSimpleAdapter.OnClickListener() {
            @Override
            public void onLongItemClick(VideoSimpleAdapter adapter, final GreenMedia greenMedia, final int position) {
                //提示弹窗
                if (mRxDialogSureCancel == null) {

                    mRxDialogSureCancel = new RxDialogSureCancel(getActivity());
                }
                mRxDialogSureCancel.setContent("是否删除原视频？");
                mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(greenMedia.getPath());
                        String path = null;

                        path = FileUtil.praseUritoPath(BaseApplication.getApplictaion(), uri);
                        if (path != null) {

                            File file = new File(path);
                            if (file.exists()) {
                                file.delete();

                            }
                        }
                        if (greenMedia.getGreenGreenLocationId() != null) {

                            GreenDAOManager.getInstence().getDaoSession().getGreenLocationDao().deleteByKey(greenMedia.getGreenGreenLocationId());
                        }
                        GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().delete(greenMedia);
                        videoadapter.remove(position);
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
    }

    private void initData() {
        if (mGreenMissionTask.getStatus().equals("5")) {
            mIvAddvideo.setEnabled(false);
        } else if (mGreenMissionTask.getStatus().equals("9")) {
            mIvAddvideo.setEnabled(true);
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
        mVideoGridView.setLayoutManager(new GridLayoutManager(BaseApplication.getApplictaion(), 5));
        videoadapter = new VideoSimpleAdapter(R.layout.pic_item, mVideoMedias, getActivity(), !mGreenMissionTask.getStatus().equals("5") && !mGreenMissionTask.getStatus().equals("9"), mGreenMissionTask.getTypename());
        mVideoGridView.setAdapter(videoadapter);

    }

    public void setMission(GreenMissionTask mission) {
        mGreenMissionTask = mission;
    }

    public void setGreenMissionLog(GreenMissionLog greenMissionLog) {
        mGreenMissionLog = greenMissionLog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_addvideo)
    public void onViewClicked() {
        mIntent = new Intent(getActivity(), VideoRecordActivity.class);
        startActivityForResult(mIntent, VIDEO_FROM_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case VIDEO_FROM_CAMERA:
                    if (data != null) {
                        Uri videouri = data.getData();
                        Log.i("Oking", "@@@@@@@" + videouri.toString());
                        GreenMedia greenMedia1 = new GreenMedia();
                        greenMedia1.setType(2);
                        greenMedia1.setTime(System.currentTimeMillis());
                        greenMedia1.setPath(videouri.toString());
                        mGreenGreenLocationId = mRandom.nextLong();
                        greenMedia1.setGreenMissionLogId(mGreenMissionLog.getId());
                        greenMedia1.setGreenGreenLocationId(mGreenGreenLocationId);
                        GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia1);
                        if (!TextUtils.isEmpty(OkingContract.LOCATIONRESULT[1]) && !"null".equals(OkingContract.LOCATIONRESULT[1])) {

                            GreenLocation greenLocation = new GreenLocation();
                            greenLocation.setLatitude(OkingContract.LOCATIONRESULT[1]);
                            greenLocation.setLongitude(OkingContract.LOCATIONRESULT[2]);
                            greenLocation.setId(mGreenGreenLocationId);
                            greenMedia1.setLocation(greenLocation);
                            long insert = GreenDAOManager.getInstence().getDaoSession().getGreenLocationDao().insert(greenLocation);
                            Log.i("Oking", "视频位置插入成功：" + insert + greenLocation.toString());

                        }

                        videoadapter.addData(greenMedia1);


//                        localSaveRecord();
                    }

                    break;
                case VIDEO_FROM_GALLERY:
                    Uri videoUri = data.getData();

                    GreenMedia greenMedia1 = new GreenMedia();
                    greenMedia1.setType(2);
                    greenMedia1.setTime(System.currentTimeMillis());
                    greenMedia1.setPath(videoUri.toString());
                    mGreenGreenLocationId = mRandom.nextLong();
                    greenMedia1.setGreenMissionLogId(mGreenMissionLog.getId());
                    greenMedia1.setGreenGreenLocationId(mGreenGreenLocationId);
                    GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia1);
                    if (!TextUtils.isEmpty(OkingContract.LOCATIONRESULT[1]) && !"null".equals(OkingContract.LOCATIONRESULT[1])) {

                        GreenLocation greenLocation = new GreenLocation();
                        greenLocation.setLatitude(OkingContract.LOCATIONRESULT[1]);
                        greenLocation.setLongitude(OkingContract.LOCATIONRESULT[2]);
                        greenLocation.setId(mGreenGreenLocationId);
                        greenMedia1.setLocation(greenLocation);
                        long insert = GreenDAOManager.getInstence().getDaoSession().getGreenLocationDao().insert(greenLocation);
                        Log.i("Oking", "视频位置插入成功：" + insert + greenLocation.toString());
                    }
                    videoadapter.addData(greenMedia1);

                    break;
                default:
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
