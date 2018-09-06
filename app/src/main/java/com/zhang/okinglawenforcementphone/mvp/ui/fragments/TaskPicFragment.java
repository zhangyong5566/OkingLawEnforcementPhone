package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.interfaces.OnRequestPermissionsListener;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.baselib.utils.PermissionUtil;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.PicSimpleAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenLocation;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ImageViewActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ShootActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

public class TaskPicFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.iv_addpic)
    ImageView mIvAddpic;
    @BindView(R.id.pic_gridView)
    RecyclerView mPicGridView;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GreenMissionTask mGreenMissionTask;
    private GreenMissionLog mGreenMissionLog;
    private View mInflate;
    public static final int PHOTO_FROM_CAMERA = 100;
    private static final int PHOTO_FROM_GALLERY = 101;
    protected static final int REQUEST_CODE_CAMERA = 2;
    private RxDialogSureCancel mRxDialogSureCancel;
    private File mCameraFile;
    private Long mGreenGreenLocationId;
    private Random mRandom = new Random();
    private List<GreenMedia> mPhotoMedias = new ArrayList<>();
    private PicSimpleAdapter picadapter;

    public TaskPicFragment() {
        // Required empty public constructor
    }

    public static TaskPicFragment newInstance(String param1, String param2) {
        TaskPicFragment fragment = new TaskPicFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_task_pic, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListerner();
        return mInflate;
    }

    private void setListerner() {
        picadapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, final int position) {
                List<GreenMedia> medias = adapter.getData();
                final GreenMedia greenMedia = medias.get(position);
                //提示弹窗
                if (mRxDialogSureCancel == null) {

                    mRxDialogSureCancel = new RxDialogSureCancel(getActivity());
                }
                mRxDialogSureCancel.setContent("是否删除原图片？");
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
                        picadapter.remove(position);
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

                return false;
            }
        });
        picadapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                List<GreenMedia> datas = picadapter.getData();
                String path = datas.get(position).getPath();
                final Uri uri = Uri.parse(path);
                GreenLocation location = datas.get(position).getLocation();
                if (location != null) {
                    intent.putExtra("picLocation", location.getLongitude() + "," + location.getLatitude());
                }
                //file:///storage/emulated/0/oking/mission_pic/e9f8735f-066b-42da-85ed-e8ec0676a18c.jpg
                //file:///storage/emulated/0/oking/mission_pic/e9f8735f-066b-42da-85ed-e8ec0676a18c.jpg
                intent.putExtra("time", OkingContract.SDF.format(datas.get(position).getTime()));
                intent.setData(uri);
                startActivity(intent);

            }
        });
    }

    private void initData() {
        if (mGreenMissionTask.getStatus().equals("5")) {
            mIvAddpic.setEnabled(false);
        } else if (mGreenMissionTask.getStatus().equals("9")) {
            mIvAddpic.setEnabled(true);
        }
        mPhotoMedias.clear();
        //清除缓存
        mGreenMissionLog.resetGreenMedia();
        List<GreenMedia> greenMedias = mGreenMissionLog.getGreenMedia();
        Log.i("Oking5",greenMedias.size()+">>>图片");
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
        mPicGridView.setLayoutManager(new GridLayoutManager(BaseApplication.getApplictaion(), 5));
        picadapter = new PicSimpleAdapter(R.layout.pic_item, mPhotoMedias, getActivity(), !mGreenMissionTask.getStatus().equals("5") && !mGreenMissionTask.getStatus().equals("9"), mGreenMissionTask.getTypename());//mission.getPhotoList()为图片uri的list
        mPicGridView.setAdapter(picadapter);
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

    @OnClick(R.id.iv_addpic)
    public void onViewClicked() {
        if (!EaseCommonUtils.isSdcardExist()) {
            RxToast.error("SD卡不存在，不能拍照");
            return;
        }
        PermissionUtil.requestCamer(getActivity(), new OnRequestPermissionsListener() {
            @Override
            public void onRequestBefore() {
                //清单文件没有这个权限需要在清单文件加上
            }

            @Override
            public void onRequestLater() {
                if (mRxDialogSureCancel == null) {
                    mRxDialogSureCancel = new RxDialogSureCancel(getActivity());
                }
                mRxDialogSureCancel.setContent("是否需要高清拍摄？(注：高清拍摄无法连拍)");
                mRxDialogSureCancel.getTvSure().setText("需要");
                mRxDialogSureCancel.getTvCancel().setText("不需要");
                mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRxDialogSureCancel.cancel();

                        String filename = UUID.randomUUID().toString();
                        String filePathname = "/storage/emulated/0/oking/mission_pic/" + filename + ".jpg";
                        mCameraFile = new File(filePathname);
                        startActivityForResult(
                                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile)),
                                REQUEST_CODE_CAMERA);
                    }
                });
                mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRxDialogSureCancel.cancel();

                        Intent intent = new Intent(getActivity(), ShootActivity.class);
                        startActivityForResult(intent, PHOTO_FROM_CAMERA);
                    }
                });

                mRxDialogSureCancel.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_FROM_GALLERY:
                    Uri uri = data.getData();
                    if (uri == null) {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null, null));
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                    }

                    String path = FileUtil.praseUritoPath(BaseApplication.getApplictaion(), uri);
                    FileUtil.compressImage(path);

                    GreenMedia greenMedia = new GreenMedia();
                    greenMedia.setType(1);
                    greenMedia.setPath(uri.getPath());
                    greenMedia.setTime(System.currentTimeMillis());
                    greenMedia.setGreenMissionLogId(mGreenMissionLog.getId());
                    mGreenGreenLocationId = mRandom.nextLong();
                    greenMedia.setGreenGreenLocationId(mGreenGreenLocationId);
                    GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia);
                    if (!TextUtils.isEmpty(OkingContract.LOCATIONRESULT[1]) && !"null".equals(OkingContract.LOCATIONRESULT[1])) {

                        GreenLocation greenLocation1 = new GreenLocation();
                        greenLocation1.setLatitude(OkingContract.LOCATIONRESULT[1]);
                        greenLocation1.setLongitude(OkingContract.LOCATIONRESULT[2]);
                        greenLocation1.setId(mGreenGreenLocationId);
                        greenMedia.setLocation(greenLocation1);
                        long insert = GreenDAOManager.getInstence().getDaoSession().getGreenLocationDao().insert(greenLocation1);
                        Log.i("Oking", "图片位置插入成功：" + insert);
                    }

                    picadapter.addData(greenMedia);
                    break;
                case PHOTO_FROM_CAMERA:
                    ArrayList<String> picpaths = data.getStringArrayListExtra("picpaths");

                    if (picpaths != null && picpaths.size() > 0) {
                        Observable.fromIterable(picpaths)
                                .concatMap(new Function<String, Observable<GreenMedia>>() {
                                    @Override
                                    public Observable<GreenMedia> apply(String s) throws Exception {
                                        GreenMedia greenMedia1 = new GreenMedia();
                                        greenMedia1.setType(1);
                                        greenMedia1.setPath("file://" + s);
                                        greenMedia1.setTime(System.currentTimeMillis());
                                        mGreenGreenLocationId = mRandom.nextLong();
                                        greenMedia1.setGreenMissionLogId(mGreenMissionLog.getId());
                                        greenMedia1.setGreenGreenLocationId(mGreenGreenLocationId);
                                        GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia1);


                                        return Observable.just(greenMedia1);
                                    }
                                }).subscribe(new Consumer<GreenMedia>() {
                            @Override
                            public void accept(GreenMedia media) throws Exception {
                                if (!TextUtils.isEmpty(OkingContract.LOCATIONRESULT[1]) && !"null".equals(OkingContract.LOCATIONRESULT[1])) {
                                    GreenLocation greenLocation2 = new GreenLocation();
                                    greenLocation2.setLatitude(OkingContract.LOCATIONRESULT[1]);
                                    greenLocation2.setLongitude(OkingContract.LOCATIONRESULT[2]);
                                    greenLocation2.setId(mGreenGreenLocationId);
                                    media.setLocation(greenLocation2);

                                    long insert1 = GreenDAOManager.getInstence().getDaoSession().getGreenLocationDao().insert(greenLocation2);
                                    Log.i("Oking", "图片位置插入成功：" + insert1 + greenLocation2.toString());
                                }
                                picadapter.addData(media);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.i("Oking", "插入异常》》》" + throwable.toString());
                            }
                        });
                    }


//                    localSaveRecord();
                    break;
                case REQUEST_CODE_CAMERA:
                    if (mCameraFile != null && mCameraFile.exists()) {
                        //先压缩
                        File file = new File(mCameraFile.getParent() + "/luban");
                        file.mkdirs();
                        Luban.with(BaseApplication.getApplictaion())
                                .load(mCameraFile.getPath())                                   // 传人要压缩的图片列表
                                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                                .setTargetDir(file.getPath())                        // 设置压缩后文件存储位置
                                .setCompressListener(new OnCompressListener() { //设置回调
                                    @Override
                                    public void onStart() {
                                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        // TODO 压缩成功后调用，返回压缩后的图片文件
                                        Log.i("Oking", "压缩成功：" + file.getPath());
                                        mCameraFile.delete();//删除原文件

                                        GreenMedia greenMedia = new GreenMedia();
                                        greenMedia.setType(1);
                                        greenMedia.setPath("file://" + file.getPath());
                                        greenMedia.setTime(System.currentTimeMillis());
                                        greenMedia.setGreenMissionLogId(mGreenMissionLog.getId());
                                        mGreenGreenLocationId = mRandom.nextLong();
                                        greenMedia.setGreenGreenLocationId(mGreenGreenLocationId);
                                        GreenDAOManager.getInstence().getDaoSession().getGreenMediaDao().insert(greenMedia);
                                        if (!TextUtils.isEmpty(OkingContract.LOCATIONRESULT[1]) && !"null".equals(OkingContract.LOCATIONRESULT[1])) {

                                            GreenLocation greenLocation1 = new GreenLocation();
                                            greenLocation1.setLatitude(OkingContract.LOCATIONRESULT[1]);
                                            greenLocation1.setLongitude(OkingContract.LOCATIONRESULT[2]);
                                            greenLocation1.setId(mGreenGreenLocationId);
                                            greenMedia.setLocation(greenLocation1);
                                            long insert = GreenDAOManager.getInstence().getDaoSession().getGreenLocationDao().insert(greenLocation1);
                                            Log.i("Oking", "图片位置插入成功：" + insert);
                                        }

                                        picadapter.addData(greenMedia);

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

}
