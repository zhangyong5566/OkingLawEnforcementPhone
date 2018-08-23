package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.GlideApp;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.OkingNotificationManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.IndexRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.BinnerItem;
import com.zhang.okinglawenforcementphone.beans.EmergencyTaskOV;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.MenuItemOV;
import com.zhang.okinglawenforcementphone.beans.MenuOV;
import com.zhang.okinglawenforcementphone.beans.NewsTaskOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.RefreshTaskMissionEvent;
import com.zhang.okinglawenforcementphone.beans.StopSwipeRefreshEvent;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.AllActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.BinnerContentActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.CaseManagerActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.FromAllLawEnforcementSpecificationActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.FromAllLawsAndRegulationsActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.LawEnforcementManagerActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MainActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MissionActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.PatrolsToReleaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.SceneInquestActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.StatisticalActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.TaskMissionProjectActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.TemporaryEmergencyTaskActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ToDoActivity;
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
    private TextView mMessageCoutTextView;
    private Intent intent;
    private MainActivity mMainActivity;
    private BinnerItem mBinnerItem;
    private List<BinnerItem> mBinnerItems;

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

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BinnerItem binnerItem = mBinnerItems.get(position);
                intent = new Intent(getActivity(), BinnerContentActivity.class);
                intent.putExtra("title",binnerItem.getTitle());
                intent.putExtra("toContent",binnerItem.getToContent());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mLayoutSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.refresh_color));
        mHeadView = View.inflate(BaseApplication.getApplictaion(), R.layout.index_head, null);
        mBanner = mHeadView.findViewById(R.id.banner);

        ryIndex.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ryIndex.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 30, Color.argb(255,236,236,236)));


        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);

    }

    private void initData() {
        EventBus.getDefault().register(this);
        List<MenuOV> menuOVS = new ArrayList<>();
        mMenuOV = new MenuOV();
        mMenuOV.setTage("巡查管理");
        mMenuItemOVS = new ArrayList<>();
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("待办");
        mMenuItemOV.setIcon(R.mipmap.task_sth);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("一般任务");
        mMenuItemOV.setIcon(R.mipmap.taskpush);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("紧急任务");
        mMenuItemOV.setIcon(R.mipmap.approval);
        mMenuItemOVS.add(mMenuItemOV);

//        mMenuItemOV = new MenuItemOV();
//        mMenuItemOV.setTitle("安排");
//        mMenuItemOV.setIcon(R.mipmap.arrange);
//        mMenuItemOVS.add(mMenuItemOV);
//        mMenuItemOV = new MenuItemOV();
//        mMenuItemOV.setTitle("     执行   ");
//        mMenuItemOV.setIcon(R.mipmap.task_excut);
//        mMenuItemOVS.add(mMenuItemOV);
//        mMenuItemOV = new MenuItemOV();
//        mMenuItemOV.setTitle("   上报     ");
//        mMenuItemOV.setIcon(R.mipmap.task_report);
//        mMenuItemOVS.add(mMenuItemOV);
        mMenuOV.setMenuItemOVS(mMenuItemOVS);
        menuOVS.add(mMenuOV);

        mMenuOV = new MenuOV();
        mMenuOV.setTage("案件办理");
        mMenuItemOVS = new ArrayList<>();
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("办案");
        mMenuItemOV.setIcon(R.mipmap.handling);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("笔录");
        mMenuItemOV.setIcon(R.mipmap.task_record);
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
        mMenuOV.setTage("辅助执法");
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
        mMenuItemOV.setTitle("法律法规");
        mMenuItemOV.setIcon(R.mipmap.law_library);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("执法规范");
        mMenuItemOV.setIcon(R.mipmap.enforcement_specification);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuItemOV = new MenuItemOV();
        mMenuItemOV.setTitle("全部");
        mMenuItemOV.setIcon(R.mipmap.all);
        mMenuItemOVS.add(mMenuItemOV);
        mMenuOV.setMenuItemOVS(mMenuItemOVS);
        menuOVS.add(mMenuOV);

        //设置图片集合
        mBinnerItems = new ArrayList<>();
        mBinnerItem = new BinnerItem();
        mBinnerItem.setTitle("学习习近平总书记参加广东团审议重要讲话精神");
        mBinnerItem.setPicPath(R.mipmap.binner01);
        mBinnerItem.setToContent("http://www.gdwater.gov.cn/yszx/ztjj/zthg/2018nzt/xxxzsjzyjhjs/");
        mBinnerItems.add(mBinnerItem);

        mBinnerItem = new BinnerItem();
        mBinnerItem.setTitle("群众办事堵点疏解行动来了！你投票，政府来解决（第二季商事服务）");
        mBinnerItem.setPicPath(R.mipmap.binner02);
        mBinnerItem.setToContent("http://toupiao.www.gov.cn/100dudian/index.htm");
        mBinnerItems.add(mBinnerItem);

        mBinnerItem = new BinnerItem();
        mBinnerItem.setTitle("马兴瑞调研备汛防汛河涌整治及水利基础设施建设工作强调利用信息化手段提高防灾抗灾减灾能力");
        mBinnerItem.setPicPath(R.mipmap.binner03);
        mBinnerItem.setToContent("http://www.gdwater.gov.cn/yszx/slyw/gdtp/201805/t20180501_354104.shtml");
        mBinnerItems.add(mBinnerItem);

        mBinnerItem = new BinnerItem();
        mBinnerItem.setTitle("叶贞琴到我厅调研督导时强调准确把握新时代水利工作发展方向不断提升我省水利现代化水平");
        mBinnerItem.setPicPath(R.mipmap.binner04);
        mBinnerItem.setToContent("http://www.gdwater.gov.cn/yszx/slyw/gdtp/201804/t20180408_324080.shtml");
        mBinnerItems.add(mBinnerItem);

        mBinnerItem = new BinnerItem();
        mBinnerItem.setTitle("许永锞调研广州市实施乡村振兴战略和水功能区达标工作");
        mBinnerItem.setPicPath(R.mipmap.binner05);
        mBinnerItem.setToContent("http://www.gdwater.gov.cn/yszx/slyw/gdtp/201805/t20180504_354177.shtml");
        mBinnerItems.add(mBinnerItem);

        mBinnerItem = new BinnerItem();
        mBinnerItem.setTitle("全面推行河长制");
        mBinnerItem.setPicPath(R.mipmap.binner06);
        mBinnerItem.setToContent("http://www.gdwater.gov.cn/yszx/ztjj/zthg/2017nzt/qmtxhzz/");
        mBinnerItems.add(mBinnerItem);
//        images.add("http://file.juzimi.com/weibopic/jpzlml7.jpg");
//        images.add("http://file.juzimi.com/weibopic/jozimi4.jpg");
//        images.add("http://file.juzimi.com/weibopic/jizuma7.jpg");
//        images.add("http://file.juzimi.com/weibopic/jxzrmp2.jpg");
//        images.add("http://file.juzimi.com/weibopic/jxzpmu4.jpg");
        mBanner.setImages(mBinnerItems);
        //设置图片加载器
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object binnerItem, ImageView imageView) {
                BinnerItem item = (BinnerItem) binnerItem;


                GlideApp.with(mMainActivity)
                        .load(item.getPicPath())
                        .into(imageView);
            }

        });

        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.ForegroundToBackground);
        //设置标题集合（当banner样式有显示title时）
        List<String> titles = new ArrayList<>();
        titles.add("学习习近平总书记参加广东团审议重要讲话精神");
        titles.add("群众办事堵点疏解行动来了！你投票，政府来解决（第二季商事服务）");
        titles.add("马兴瑞调研备汛防汛河涌整治及水利基础设施建设工作强调 利用信息化手段提高防灾抗灾减灾能力");
        titles.add("叶贞琴到我厅调研督导时强调 准确把握新时代水利工作发展方向不断提升我省水利现代化水平");
        titles.add("许永锞调研广州市实施乡村振兴战略和水功能区达标工作");
        titles.add("全面推行河长制");
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
                    case "一般任务":
                        intent = new Intent(getActivity(), PatrolsToReleaseActivity.class);
                        startActivity(intent);
                        break;
                    case "待办":
                        intent = new Intent(getActivity(), ToDoActivity.class);
                        startActivity(intent);
                        break;
//                    case "安排":
//                        intent = new Intent(getActivity(), TaskMissionProjectActivity.class);
//                        intent.putExtra("activity", "ArrangeMissionActivity");
//                        startActivity(intent);
//                        break;
                    case "紧急任务":
                        intent = new Intent(getActivity(), TemporaryEmergencyTaskActivity.class);
                        startActivity(intent);
                        break;
//                    case "     执行   ":
//                        intent = new Intent(getActivity(), TaskMissionProjectActivity.class);
//                        intent.putExtra("activity", "TaskExecutionActivity");
//                        startActivity(intent);
//                        break;
//                    case "   上报     ":
//                        intent = new Intent(getActivity(), TaskMissionProjectActivity.class);
//                        intent.putExtra("activity", "ReportTaskActivity");
//                        startActivity(intent);
//                        break;
                    case "笔录":
                        intent = new Intent(getActivity(), WrittenRecordActivity.class);
                        startActivity(intent);
                        break;

                    case "办案":
                        intent = new Intent(getActivity(), CaseManagerActivity.class);
                        intent.putExtra("position", 0);
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
                        startActivity(intent);
                        break;
                    case "法律法规":
                        intent = new Intent(getActivity(), FromAllLawsAndRegulationsActivity.class);
                        startActivity(intent);
                        break;
                    case "执法规范":
                        intent = new Intent(getActivity(), FromAllLawEnforcementSpecificationActivity.class);
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

            @Override
            public void setMessageCount(TextView textView) {
                mMessageCoutTextView = textView;

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
            long count = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                    .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                    .whereOr(GreenMissionTaskDao.Properties.Examine_status.eq(6),GreenMissionTaskDao.Properties.Examine_status.eq(5),GreenMissionTaskDao.Properties.Examine_status.eq(4),GreenMissionTaskDao.Properties.Examine_status.eq(2),GreenMissionTaskDao.Properties.Examine_status.eq(1),GreenMissionTaskDao.Properties.Examine_status.eq(0),GreenMissionTaskDao.Properties.Status.eq("1"), GreenMissionTaskDao.Properties.Status.eq("2"), GreenMissionTaskDao.Properties.Status.eq("3"), GreenMissionTaskDao.Properties.Status.eq("4"), GreenMissionTaskDao.Properties.Status.eq("7"), GreenMissionTaskDao.Properties.Status.eq("100"))
                    .count();
            if (mMessageCoutTextView != null && count > 0) {
                mMessageCoutTextView.setVisibility(View.VISIBLE);
                mMessageCoutTextView.setText("" + count);
            } else {
                mMessageCoutTextView.setVisibility(View.GONE);
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent2(EmergencyTaskOV event) {
        int type = event.getType();
        if (type == 0) {

            if (mMessageCoutTextView != null) {
                mMessageCoutTextView.setVisibility(View.VISIBLE);
                mMessageCoutTextView.setText("" + (Integer.parseInt(mMessageCoutTextView.getText().toString()) + 1));
            }

        }
    }

    //新任务
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent3(NewsTaskOV event) {
        int type = event.mType;
        if (type == 1) {

            if (mMessageCoutTextView != null) {
                mMessageCoutTextView.setVisibility(View.VISIBLE);
                if (mMessageCoutTextView.getText().toString().trim().equals("1")){
                    mMessageCoutTextView.setText("0");
                    mMessageCoutTextView.setVisibility(View.GONE);
                }else {

                    mMessageCoutTextView.setText("" + (Integer.parseInt(mMessageCoutTextView.getText().toString().trim()) -1));
                }
            }

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        mBanner.stopAutoPlay();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
