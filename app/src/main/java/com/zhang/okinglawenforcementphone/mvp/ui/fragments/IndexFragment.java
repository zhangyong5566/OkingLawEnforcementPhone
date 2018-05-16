package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.okinglawenforcementphone.OkingNotificationManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.IndexRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.MenuItemOV;
import com.zhang.okinglawenforcementphone.beans.MenuOV;
import com.zhang.okinglawenforcementphone.beans.RefreshTaskMissionEvent;
import com.zhang.okinglawenforcementphone.beans.StopSwipeRefreshEvent;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.AllActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.CaseManagerActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.LawEnforcementManagerActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MissionActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.PatrolsToReleaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.SceneInquestActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.StatisticalActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.TaskMissionProjectActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.TemporaryEmergencyTaskActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.WrittenRecordActivity;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 主页面
 */
public class IndexFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.ry_navigation)
    RecyclerView ryIndex;
    @BindView(R.id.layoutSwipeRefresh)
    SwipeRefreshLayout mLayoutSwipeRefresh;
    Unbinder unbinder;

    private String mParam1;
    private String mParam2;
    private View mInflate;
    private View mHeadView;
    private Banner mBanner;
    private List<MenuItemOV> mMenuItemOVS;
    private MenuItemOV mMenuItemOV;
    private MenuOV mMenuOV;

    private RxDialogSureCancel mRxDialogSureCancel;
    private Intent intent;

    public IndexFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static IndexFragment newInstance(String param1, String param2) {
        IndexFragment fragment = new IndexFragment();
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
            mInflate = inflater.inflate(R.layout.fragment_index, container, false);

        }
        unbinder = ButterKnife.bind(this, mInflate);
        initView();
        initData();
        setListener();

        return mInflate;
    }

    private void setListener() {
        mLayoutSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshTaskMissionEvent refreshTaskMissionEvent = new RefreshTaskMissionEvent();
                refreshTaskMissionEvent.setType(0);
                EventBus.getDefault().post(refreshTaskMissionEvent);
            }
        });
    }

    private void initView() {
        mLayoutSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.refresh_color));
        mHeadView = View.inflate(BaseApplication.getApplictaion(), R.layout.index_head, null);
        mBanner = mHeadView.findViewById(R.id.banner);

        ryIndex.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ryIndex.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 25, Color.WHITE));


        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);

    }

    private void initData() {
        EventBus.getDefault().register(this);
        List<MenuOV> menuOVS = new ArrayList<>();
        mMenuOV = new MenuOV();
        mMenuOV.setTage("常用应用");
        mMenuItemOVS = new ArrayList<>();
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("发布");
        mMenuItemOV.setIcon(R.mipmap.taskpush);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("待办");
        mMenuItemOV.setIcon(R.mipmap.task_sth);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("安排");
        mMenuItemOV.setIcon(R.mipmap.arrange);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("审批");
        mMenuItemOV.setIcon(R.mipmap.approval);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("执行");
        mMenuItemOV.setIcon(R.mipmap.task_excut);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("上报");
        mMenuItemOV.setIcon(R.mipmap.task_report);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("笔录");
        mMenuItemOV.setIcon(R.mipmap.task_record);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuOV.setMenuItemOVS(mMenuItemOVS);
        menuOVS.add(mMenuOV);

        mMenuOV = new MenuOV();
        mMenuOV.setTage("水政服务");
        mMenuItemOVS = new ArrayList<>();
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("办案");
        mMenuItemOV.setIcon(R.mipmap.handling);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("勘验");
        mMenuItemOV.setIcon(R.mipmap.inquest);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("统计");
        mMenuItemOV.setIcon(R.mipmap.statistical);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("执法");
        mMenuItemOV.setIcon(R.mipmap.enforcement);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuOV.setMenuItemOVS(mMenuItemOVS);
        menuOVS.add(mMenuOV);

        mMenuOV = new MenuOV();
        mMenuOV.setTage("其他");
        mMenuItemOVS = new ArrayList<>();
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("日志");
        mMenuItemOV.setIcon(R.mipmap.log);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("轨迹");
        mMenuItemOV.setIcon(R.mipmap.trajectory);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("全部");
        mMenuItemOV.setIcon(R.mipmap.all);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuOV.setMenuItemOVS(mMenuItemOVS);
        menuOVS.add(mMenuOV);

        //设置图片集合
        List<String> images = new ArrayList<>();
        images.add("http://file.juzimi.com/weibopic/jpzlml7.jpg");
        images.add("http://file.juzimi.com/weibopic/jozimi4.jpg");
        images.add("http://file.juzimi.com/weibopic/jizuma7.jpg");
        images.add("http://file.juzimi.com/weibopic/jxzrmp2.jpg");
        images.add("http://file.juzimi.com/weibopic/jxzpmu4.jpg");
        mBanner.setImages(images);
        //设置图片加载器
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //用fresco加载图片
                Uri uri = Uri.parse((String) path);
                imageView.setImageURI(uri);
            }

            @Override
            public ImageView createImageView(Context context) {
                SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
                return simpleDraweeView;
            }
        });

        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        List<String> titles = new ArrayList<>();
        titles.add("测试01");
        titles.add("测试02");
        titles.add("测试03");
        titles.add("测试04");
        titles.add("测试05");
        mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();


        IndexRecyAdapter indexRecyAdapter = new IndexRecyAdapter(R.layout.menu_sub_item, menuOVS, new IndexRecyAdapter.OnTagClickListener() {
            @Override
            public void onTagClickListener(MenuItemOV bean) {
                switch (bean.getTitle()) {
                    case "发布":
                        if (mRxDialogSureCancel == null) {

                            mRxDialogSureCancel = new RxDialogSureCancel(getContext());
                        }
                        mRxDialogSureCancel.setContent("请选择发布任务类型");
                        mRxDialogSureCancel.getTvSure().setText("一般任务");
                        mRxDialogSureCancel.getTvCancel().setText("紧急任务");
                        mRxDialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mRxDialogSureCancel.cancel();
                                Intent intent = new Intent(getActivity(), TemporaryEmergencyTaskActivity.class);
                                startActivity(intent);
                            }
                        });

                        mRxDialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mRxDialogSureCancel.cancel();
                                Intent intent = new Intent(getActivity(), PatrolsToReleaseActivity.class);
                                startActivity(intent);
                            }
                        });
                        mRxDialogSureCancel.show();
                        break;
                    case "待办":
                        break;
                    case "安排":
                        intent = new Intent(getActivity(), TaskMissionProjectActivity.class);
                        intent.putExtra("activity", "ArrangeMissionActivity");
                        startActivity(intent);
                        break;
                    case "审批":
                        break;
                    case "执行":
                        intent = new Intent(getActivity(), TaskMissionProjectActivity.class);
                        intent.putExtra("activity", "TaskExecutionActivity");
                        startActivity(intent);
                        break;
                    case "上报":
                        intent = new Intent(getActivity(), TaskMissionProjectActivity.class);
                        intent.putExtra("activity", "ReportTaskActivity");
                        startActivity(intent);
                        break;
                    case "笔录":
                        intent = new Intent(getActivity(), WrittenRecordActivity.class);
                        startActivity(intent);
                        break;

                    case "办案":
                        intent = new Intent(getActivity(), CaseManagerActivity.class);
                        intent.putExtra("position",0);
                        startActivity(intent);
                        break;
                    case "勘验":
                        intent = new Intent(getActivity(), SceneInquestActivity.class);
                        startActivity(intent);
                        break;

                    case "统计":
                        intent = new Intent(getActivity(), StatisticalActivity.class);
                        startActivity(intent);
                        break;
                    case "轨迹":

                        intent = new Intent(getActivity(), TaskMissionProjectActivity.class);
                        intent.putExtra("activity", "TrajectoryListActivity");
                        startActivity(intent);
                        break;
                    case "日志":
                        intent = new Intent(getActivity(), TaskMissionProjectActivity.class);
                        intent.putExtra("activity", "CompleteListActivity");
                        startActivity(intent);
                        break;
                    case "执法":
                        intent = new Intent(getActivity(), LawEnforcementManagerActivity.class);
                        intent.putExtra("position",0);
                        startActivity(intent);
                        break;
                    case "全部":
                        intent = new Intent(getActivity(), AllActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        indexRecyAdapter.addHeaderView(mHeadView);
        indexRecyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        ryIndex.setAdapter(indexRecyAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent1(StopSwipeRefreshEvent event) {
        int type = event.getType();
        if (type == 0) {
            mLayoutSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}