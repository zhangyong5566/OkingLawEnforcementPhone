package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.TaskLogPicRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenLocation;
import com.zhang.okinglawenforcementphone.beans.GreenMedia;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLog;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadBasicLogContract;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadTaskPicContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadBasicLogPresenter;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadTaskPicPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ImageViewActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.PlayVideoOnlineActivity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ApprovalPicVideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.pic_gridView)
    RecyclerView mPicGridView;
    Unbinder unbinder;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mInflate;
    private TaskLogPicRecyAdapter mTaskLogPicRecyAdapter;
    private GreenMissionTask mGreenMissionTask;
    private GreenMissionLog mGreenMissionLog;
    private LoadBasicLogPresenter mLoadBasicLogPresenter;
    private LoadTaskPicPresenter mLoadTaskPicPresenter;
    private boolean mIsLoadMeadiaSucc = false;

    public ApprovalPicVideoFragment() {
        // Required empty public constructor
    }

    public static ApprovalPicVideoFragment newInstance(String param1, String param2) {
        ApprovalPicVideoFragment fragment = new ApprovalPicVideoFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_approval_pic_video, container, false);

        }

        unbinder = ButterKnife.bind(this, mInflate);
        initData();
        setListerner();
        return mInflate;
    }

    private void setListerner() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLogData();
            }
        });
        mTaskLogPicRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<GreenMedia> mediaList = adapter.getData();
                GreenMedia greenMedia = mediaList.get(position);
                if (greenMedia.getType() == 2) {
                    Intent intent = new Intent(getActivity(), PlayVideoOnlineActivity.class);
                    /**
                     * 有毒，请截取掉.MP4后缀！！！请截取掉.MP4后缀！！！（后台框架限制）
                     */

                    intent.putExtra("path", StringUtils.substring(greenMedia.getPath(), 0, greenMedia.getPath().length() - 4));
                    startActivity(intent);


                } else if (greenMedia.getType() == 1) {
                    Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                    intent.putExtra("time", OkingContract.SDF.format(greenMedia.getTime()));
                    intent.putExtra("picLocation", greenMedia.getSouceLocation().getLatitude() + "," + greenMedia.getSouceLocation().getLongitude());
                    intent.setData(Uri.parse(greenMedia.getPath()));
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(getActivity(), ImageViewActivity.class);
                    intent.setData(Uri.parse(greenMedia.getPath()));
                    startActivity(intent);

                }
            }
        });

    }

    private void initData() {
        mPicGridView.setLayoutManager(new GridLayoutManager(BaseApplication.getApplictaion(), 5));
        mTaskLogPicRecyAdapter = new TaskLogPicRecyAdapter(getActivity(), R.layout.pic_item, null);
        mPicGridView.setAdapter(mTaskLogPicRecyAdapter);

        loadLogData();

    }

    private void loadLogData() {
        if (mGreenMissionLog == null) {  //先去获取日志
            if (mLoadBasicLogPresenter == null) {
                mLoadBasicLogPresenter = new LoadBasicLogPresenter(new LoadBasicLogContract.View() {
                    @Override
                    public void getBasicLogSucc(String result) {
                        //{"msg":"查询成功!","datas":[{"OTHER_PART":"交通,城管","EQUIPMENT":"交通工具：001003,001001,001001  ","PLAN":"0","TYPE":"0"}],"status":"1"}

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                JSONArray datas = jsonObject.getJSONArray("datas");
                                mGreenMissionLog = new GreenMissionLog();
                                for (int i = 0; i < datas.length(); i++) {
                                    JSONObject object = datas.getJSONObject(i);
                                    mGreenMissionLog.setEquipment(object.getString("EQUIPMENT"));
                                    mGreenMissionLog.setServer_id(object.getString("LOG_ID"));
                                    mGreenMissionLog.setTask_id(mGreenMissionTask.getTaskid());
                                    mGreenMissionLog.setOther_part(object.getString("OTHER_PART"));
                                    mGreenMissionLog.setPlan(Integer.parseInt(object.getString("PLAN")));
                                    mGreenMissionLog.setPatrol(object.getString("PATROL"));
                                    mGreenMissionLog.setDzyj(object.getString("DZYJ"));
                                }
                                GreenDAOManager.getInstence().getDaoSession().getGreenMissionLogDao()
                                        .insert(mGreenMissionLog);
                                loadLogMedia();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//
                    }

                    @Override
                    public void getBasicLogFail(Throwable ex) {
                        RxToast.error("获取日志失败");

                    }
                });

            }
            mLoadBasicLogPresenter.getBasicLog(mGreenMissionTask.getTaskid());
        } else {
            loadLogMedia();
        }

    }

    private void loadLogMedia() {
        if (!mIsLoadMeadiaSucc&&mGreenMissionLog.getServer_id()!=null) {
            if (mLoadTaskPicPresenter == null) {
                mLoadTaskPicPresenter = new LoadTaskPicPresenter(new LoadTaskPicContract.View() {
                    @Override
                    public void loadTaskPicSucc(String result) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mIsLoadMeadiaSucc = true;
                        List<GreenMedia> mediaList = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(result);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                GreenMedia greenMedia = new GreenMedia();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            String id = jsonObject.optString("ID");
//                            String logId = jsonObject.optString("LOG_ID");
                                String path = jsonObject.optString("PATH");
                                String userId = jsonObject.optString("USER_ID");
                                String ext = jsonObject.optString("EXT");
                                String type = jsonObject.getString("TYPE");
                                String smallimg = jsonObject.getString("SMALLIMG");
                                //文件类型  0-巡查日志照片  1-队员签名图片 2-视频 3-视频缩略图

                                switch (type) {
                                    case "0":
                                        greenMedia.setType(1);//1表示日志图片  2表示视频  3表示语音   4签名图片
                                        greenMedia.setPath(Api.BASE_URL + "/upload" + path);
                                        greenMedia.setGreenMissionLogId(mGreenMissionLog.getId());
                                        if (ext != null) {
                                            JSONObject extObj = new JSONObject(ext);
                                            String datetime = extObj.optString("datetime");

                                            if (datetime != null) {
                                                try {
                                                    greenMedia.setTime(OkingContract.SDF.parse(datetime).getTime());
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            GreenLocation greenLocation = new GreenLocation();
                                            greenLocation.setLatitude(extObj.optString("latitude"));
                                            greenLocation.setLongitude(extObj.optString("longitude"));
                                            greenMedia.setSouceLocation(greenLocation);
                                        }

                                        mediaList.add(greenMedia);
                                        break;
                                    case "1":
                                        greenMedia.setType(4);
                                        greenMedia.setPath(Api.BASE_URL + "/upload" + path);
                                        greenMedia.setUserid(userId);
                                        greenMedia.setGreenMissionLogId(mGreenMissionLog.getId());
                                        mediaList.add(greenMedia);
                                        break;
                                    case "2":
                                        greenMedia.setType(2);
                                        greenMedia.setPath(Api.BASE_URL + "/upload" + smallimg);
                                        greenMedia.setGreenMissionLogId(mGreenMissionLog.getId());
                                        mediaList.add(greenMedia);
                                        break;
                                    default:
                                        break;

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Oking2", e.getMessage());
                        }
                        mTaskLogPicRecyAdapter.setNewData(mediaList);
                    }

                    @Override
                    public void loadTaskPicFail(Throwable e) {
                        Log.i("Oking1", "失败" + e.toString());
                        mSwipeRefreshLayout.setRefreshing(false);
                        mIsLoadMeadiaSucc = false;
                    }
                });
            }

            mLoadTaskPicPresenter.loadTaskPic(mGreenMissionLog.getServer_id());
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setMissionTask(GreenMissionTask greenMissionTask) {
        mGreenMissionTask = greenMissionTask;
    }

    public void setMissionLog(GreenMissionLog greenMissionLog) {
        mGreenMissionLog = greenMissionLog;
    }
}
