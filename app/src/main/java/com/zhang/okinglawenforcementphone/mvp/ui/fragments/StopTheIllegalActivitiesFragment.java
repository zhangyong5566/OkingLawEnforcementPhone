package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.DefaultContants;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.AppUtil;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.ExpandableStopIllegalAdapter;
import com.zhang.okinglawenforcementphone.beans.WrittenItemBean;
import com.zhang.okinglawenforcementphone.beans.WrittenRecordLevel0;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 责令停止违法行为
 */
public class StopTheIllegalActivitiesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_CAMERA = 102;
    @BindView(R.id.rcy_stop_illegal)
    RecyclerView mRcyStopIllegal;
    Unbinder unbinder;
    private WrittenRecordLevel0 mWrittenRecordLevel0;
    private WrittenItemBean mWrittenItemBean;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mTv_time;
    private View mInflate;
    private ExpandableStopIllegalAdapter mExpandableStopIllegalAdapter;

    public StopTheIllegalActivitiesFragment() {
        // Required empty public constructor
    }

    public static StopTheIllegalActivitiesFragment newInstance(String param1, String param2) {
        StopTheIllegalActivitiesFragment fragment = new StopTheIllegalActivitiesFragment();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mInflate == null) {
            mInflate = inflater.inflate(R.layout.fragment_stop_the_illegal_activities, container, false);

        }

        unbinder = ButterKnife.bind(this, mInflate);
        initView();
        initData();
        setListener();
        return mInflate;
    }

    private void setListener() {
        mExpandableStopIllegalAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.bt_idcard:
                        if (!checkTokenStatus()) {
                            //初始化OCR
                            initAccessToken();
                        } else {

                            Intent intent = new Intent(getActivity(), CameraActivity.class);
                            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                                    FileUtil.getSaveFile(BaseApplication.getApplictaion()).getAbsolutePath());
                            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                            startActivityForResult(intent, REQUEST_CODE_CAMERA);
                        }

                        break;
                    case R.id.bt_prin:
                        mExpandableStopIllegalAdapter.print();

                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initView() {
        mRcyStopIllegal.setNestedScrollingEnabled(false);
        mRcyStopIllegal.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
        mRcyStopIllegal.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 3, BaseApplication.getApplictaion().getResources().getColor(R.color.bottom_nav_normal)));

        mExpandableStopIllegalAdapter = new ExpandableStopIllegalAdapter(getActivity(),generateData());
        mExpandableStopIllegalAdapter.expand(0);
        mRcyStopIllegal.setAdapter(mExpandableStopIllegalAdapter);
    }

    private void initData() {
        //初始化OCR
        if (!DefaultContants.HASGOTTOKEN) {

            initAccessToken();
        }


    }

    private void initAccessToken() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                DefaultContants.HASGOTTOKEN = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
//                RxToast.error(MyApp.getApplictaion(), "licence方式获取token失败" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, BaseApplication.getApplictaion());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(BaseApplication.getApplictaion()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    }
                }
            }
        }
    }

    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    RxToast.success(BaseApplication.getApplictaion(), "识别成功!", Toast.LENGTH_SHORT).show();
                    mExpandableStopIllegalAdapter.setOCRData(result);

                }
            }

            @Override
            public void onError(OCRError error) {
                RxToast.error(BaseApplication.getApplictaion(), "证件识别失败" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            RxToast.warning(BaseApplication.getApplictaion(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }


    }

    private List<MultiItemEntity> generateData() {

        ArrayList<MultiItemEntity> res = new ArrayList<>();

        mWrittenRecordLevel0 = new WrittenRecordLevel0("当事人基本信息");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(1);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        mWrittenRecordLevel0 = new WrittenRecordLevel0("违法条款");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(2);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);

        mWrittenRecordLevel0 = new WrittenRecordLevel0("联系方式");
        mWrittenItemBean = new WrittenItemBean();
        mWrittenItemBean.setItemType(3);
        mWrittenRecordLevel0.addSubItem(mWrittenItemBean);
        res.add(mWrittenRecordLevel0);


        return res;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 释放内存资源
        if (DefaultContants.HASGOTTOKEN && OCR.getInstance().hasGotToken()) {

            OCR.getInstance().release();
        }
        unbinder.unbind();
    }

    private boolean checkTokenStatus() {
        if (!DefaultContants.HASGOTTOKEN) {
            RxToast.warning(BaseApplication.getApplictaion(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return DefaultContants.HASGOTTOKEN;
    }
}
