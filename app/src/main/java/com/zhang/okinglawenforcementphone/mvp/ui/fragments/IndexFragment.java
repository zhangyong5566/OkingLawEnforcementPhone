package com.zhang.okinglawenforcementphone.mvp.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.GlideApp;
import com.zhang.baselib.http.BaseHttpFactory;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.utils.ActivityUtil;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.HeadTaskRecyAdapter;
import com.zhang.okinglawenforcementphone.adapter.IndexRecyAdapter;
import com.zhang.okinglawenforcementphone.adapter.PopwindowRecyAdapter;
import com.zhang.okinglawenforcementphone.beans.BinnerItem;
import com.zhang.okinglawenforcementphone.beans.EmergencyTaskOV;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.MenuItemOV;
import com.zhang.okinglawenforcementphone.beans.MenuOV;
import com.zhang.okinglawenforcementphone.beans.Mission;
import com.zhang.okinglawenforcementphone.beans.NewsTaskOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.UpdateGreenMissionTaskOV;
import com.zhang.okinglawenforcementphone.http.Api;
import com.zhang.okinglawenforcementphone.http.service.GDWaterService;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadHttpMissionContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadHttpMissionPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.AllActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ApprovalActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ArrangeTeamMembersActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.AuditActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.BinnerContentActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.CaseManagerActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.FromAllLawEnforcementSpecificationActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.FromAllLawsAndRegulationsActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.LawEnforcementManagerActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MainActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MissionActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.MissionRecorActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.PatrolsToReleaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.SceneInquestActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.StatisticalActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.TaskMissionProjectActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.TemporaryEmergencyTaskActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.ToDoActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.activitys.WrittenRecordActivity;
import com.zhang.okinglawenforcementphone.views.CustomPopWindow;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

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
    private Intent intent;
    private MainActivity mMainActivity;
    private BinnerItem mBinnerItem;
    private List<BinnerItem> mBinnerItems;
    private RecyclerView mRecyHeadtask;
    private HeadTaskRecyAdapter mHeadTaskRecyAdapter;
    private Intent mIntent;
    private List<GreenMissionTask> mGreenHeadMissionTasks = new ArrayList<>();
    private List<GreenMissionTask> mGreenHeadMissionTasks2;
    private RxDialogLoading mRxDialogLoading;
    private LoadHttpMissionPresenter mLoadHttpMissionPresenter;
    private TextView mTv_title;
    private String mPushTaskid;
    private Gson mGson = new Gson();
    private List<Mission.RecordsBean> mRecords;
    private int mPosition = 0;
    private TextView mTv_screening;
    private CustomPopWindow mPopWindow;

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
        mHeadTaskRecyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GreenMissionTask greenMissionTask = (GreenMissionTask) adapter.getData().get(position);
                if (greenMissionTask.getExamine_status() == -1) {


                    switch (greenMissionTask.getStatus()) {
                        case "0":

                        case "1":
                            mIntent = new Intent(getActivity(), AuditActivity.class);
                            mIntent.putExtra("id", greenMissionTask.getId());
                            mIntent.putExtra("position", position);
                            startActivity(mIntent);
                            break;

                        case "2":

                            mIntent = new Intent(getActivity(), ArrangeTeamMembersActivity.class);
                            mIntent.putExtra("id", greenMissionTask.getId());
                            mIntent.putExtra("position", position);
                            startActivity(mIntent);

                            break;
                        case "3":
                        case "4":
                            mIntent = new Intent(getActivity(), MissionActivity.class);
                            mIntent.putExtra("id", greenMissionTask.getId());
                            mIntent.putExtra("position", position);
                            startActivity(mIntent);
                            break;
                        case "7":
                            mIntent = new Intent(getActivity(), AuditActivity.class);
                            mIntent.putExtra("id", greenMissionTask.getId());
                            mIntent.putExtra("position", position);

                            startActivity(mIntent);
                            break;
                        case "100":
                        case "5":

                        case "9":
                            mIntent = new Intent(getActivity(), MissionRecorActivity.class);
                            mIntent.putExtra("id", greenMissionTask.getId());
                            mIntent.putExtra("taskId", greenMissionTask.getTaskid());
                            startActivity(mIntent);
                            break;
                        default:
                            break;
                    }

                } else {
                    if (greenMissionTask.getApproved_person().equals(OkingContract.CURRENTUSER.getUserid())) {
                        //领导批示
                        mIntent = new Intent(getActivity(), ApprovalActivity.class);
                        mIntent.putExtra("id", greenMissionTask.getId());
                        mIntent.putExtra("position", position);
                        startActivity(mIntent);
                    } else {
                        mIntent = new Intent(getActivity(), MissionRecorActivity.class);
                        mIntent.putExtra("id", greenMissionTask.getId());
                        mIntent.putExtra("taskId", greenMissionTask.getTaskid());
                        startActivity(mIntent);
                    }


                }
            }
        });
        mLayoutSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHttpMissionList();
            }
        });

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BinnerItem binnerItem = mBinnerItems.get(position);
                intent = new Intent(getActivity(), BinnerContentActivity.class);
                intent.putExtra("title", binnerItem.getTitle());
                intent.putExtra("toContent", binnerItem.getToContent());
                startActivity(intent);
            }
        });

        mTv_screening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPopWindow ==null){
                    View popwindowView = View.inflate(getActivity(), R.layout.popwindow_view, null);
                   RecyclerView  recyPop = popwindowView.findViewById(R.id.recy_pop);
                    recyPop.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
                    recyPop.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 1, Color.DKGRAY));
                    final ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add("全部");
                    arrayList.add("未发布");
                    arrayList.add("待审核");
                    arrayList.add("待分配队员");
                    arrayList.add("待执行");
                    arrayList.add("任务开始");
                    arrayList.add("审核不通过");
                    arrayList.add("待上传");
                    arrayList.add("退回修改");
                    arrayList.add("等待领导批示");
                    PopwindowRecyAdapter popwindowRecyAdapter = new PopwindowRecyAdapter(R.layout.drop_down_item, arrayList);
                    recyPop.setAdapter(popwindowRecyAdapter);
                    mPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                            .setView(popwindowView)//显示的布局，还可以通过设置一个View
                            //     .size(600,400) //设置显示的大小，不设置就默认包裹内容
                            .setFocusable(true)//是否获取焦点，默认为ture
                            .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                            .create()//创建PopupWindow
                            .showAsDropDown(mTv_screening,0,10);
                    if (mHeadTaskRecyAdapter.getData().size()>0){
                        popwindowRecyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                // '执行状态 0-未发布  1-已发布 2-已经审核 3-确认接受  4-任务开始  5-任务完成 7-不通过审核 9-退回修改(日志审核)';

                                switch (position){
                                    case 0:         //全部
                                        mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                                .whereOr(GreenMissionTaskDao.Properties.Examine_status.eq(6), GreenMissionTaskDao.Properties.Examine_status.eq(5), GreenMissionTaskDao.Properties.Examine_status.eq(4), GreenMissionTaskDao.Properties.Examine_status.eq(2), GreenMissionTaskDao.Properties.Examine_status.eq(1), GreenMissionTaskDao.Properties.Examine_status.eq(0), GreenMissionTaskDao.Properties.Status.eq("1"), GreenMissionTaskDao.Properties.Status.eq("2"), GreenMissionTaskDao.Properties.Status.eq("3"), GreenMissionTaskDao.Properties.Status.eq("4"), GreenMissionTaskDao.Properties.Status.eq("7"), GreenMissionTaskDao.Properties.Status.eq("100"))
                                                .list();
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);

                                        break;
                                    case 1:         //未发布
                                        mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                                .where(GreenMissionTaskDao.Properties.Status.eq(0))
                                                .list();
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                                        break;
                                    case 2:         //待审核
                                        mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                                .where(GreenMissionTaskDao.Properties.Status.eq(1))
                                                .list();
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                                        break;
                                    case 3:         //待分配队员
                                        mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                                .where(GreenMissionTaskDao.Properties.Status.eq(2))
                                                .list();
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                                        break;
                                    case 4:         //待执行
                                        mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                                .where(GreenMissionTaskDao.Properties.Status.eq(3))
                                                .list();
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                                        break;
                                    case 5:         //任务开始
                                        mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                                .where(GreenMissionTaskDao.Properties.Status.eq(4))
                                                .list();
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                                        break;
                                    case 6:         //审核不通过
                                        mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                                .where(GreenMissionTaskDao.Properties.Status.eq(7))
                                                .list();
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                                        break;
                                    case 7:         //待上传
                                        mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                                .where(GreenMissionTaskDao.Properties.Status.eq(100))
                                                .list();
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);

                                        break;
                                    case 8:         //退回修改
                                        mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                                .where(GreenMissionTaskDao.Properties.Status.eq(9))
                                                .list();
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                                        break;
                                    case 9:         //等待领导批示
                                        mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                                .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                                .where(GreenMissionTaskDao.Properties.Status.eq(5))
                                                .list();
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                                        break;

                                    default:
                                        break;
                                }
                                mTv_screening.setText(arrayList.get(position));
                                mTv_title.setText("待办(" + mHeadTaskRecyAdapter.getData().size() + ")");
                                mPopWindow.dissmiss();
                            }
                        });
                    }

                }else {
                    mPopWindow.showAsDropDown(mTv_screening,0,10);
                }
            }
        });
    }

    private void initView() {
        mLayoutSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.refresh_color));
        mHeadView = View.inflate(BaseApplication.getApplictaion(), R.layout.index_head, null);
        mBanner = mHeadView.findViewById(R.id.banner);
        mRecyHeadtask = mHeadView.findViewById(R.id.recy_headtask);
        mTv_title = mHeadView.findViewById(R.id.tv_title);
        mTv_screening = mHeadView.findViewById(R.id.tv_screening);
        mRecyHeadtask.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyHeadtask.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 1, Color.DKGRAY));

        mHeadTaskRecyAdapter = new HeadTaskRecyAdapter(R.layout.index_head_taskitem, null);
        mRecyHeadtask.setAdapter(mHeadTaskRecyAdapter);

        ryIndex.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ryIndex.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 30, Color.argb(255, 236, 236, 236)));


        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);

    }

    private void initData() {
        //获取任务
        getHttpMissionList();

        EventBus.getDefault().register(this);
        List<MenuOV> menuOVS = new ArrayList<>();
        mMenuOV = new MenuOV();
        mMenuOV.setTage("巡查管理");
        mMenuItemOVS = new ArrayList<>();
//        mMenuItemOV = new MenuItemOV();
//        mMenuItemOV.setTitle("待办");
//        mMenuItemOV.setIcon(R.mipmap.task_sth);
//        mMenuItemOVS.add(mMenuItemOV);
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

        });
        indexRecyAdapter.addHeaderView(mHeadView);
        indexRecyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        ryIndex.setAdapter(indexRecyAdapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent2(EmergencyTaskOV event) {
        int type = event.getType();
        if (type == 0) {
            mHeadTaskRecyAdapter.addData(event.getGreenMissionTask());
            mTv_title.setText("待办(" + mHeadTaskRecyAdapter.getData().size() + ")");
        }
    }

    //新任务
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent3(NewsTaskOV event) {
        int type = event.mType;
        mPushTaskid = event.taskid;
        if (type == 1) {
                                //任务完成



        } else if (type == 2) {
            //刷新数据
            BaseHttpFactory.getInstence()
                    .createService(GDWaterService.class, Api.BASE_URL)
                    .loadHttpMission(OkingContract.CURRENTUSER.getUserid(), 2, "-1")
                    .compose(RxSchedulersHelper.<ResponseBody>io_main())
                    .observeOn(Schedulers.io())
                    .concatMap(new Function<ResponseBody, ObservableSource<Mission.RecordsBean>>() {
                        @Override
                        public Observable<Mission.RecordsBean> apply(ResponseBody responseBody) throws Exception {
                            mPosition = 0;
                            final String result = responseBody.string();
                            Log.i("Oking1", result);
                            Mission mission = mGson.fromJson(result, Mission.class);
                            mRecords = mission.getRecords();
                            if (mRecords != null && mRecords.size() > 0) {
                                return Observable.fromIterable(mRecords);
                            } else {
                                return Observable.error(new Throwable("NONE"));
                            }
                        }
                    })
                    .subscribe(new Consumer<Mission.RecordsBean>() {
                        @Override
                        public void accept(Mission.RecordsBean mission) throws Exception {
                            GreenMissionTask mUnique = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder().where(GreenMissionTaskDao.Properties.Taskid.eq(mission.getID())).unique();
                            mPosition++;
                            if (mUnique != null) {
                                mUnique.setApproved_person(mission.getAPPROVED_PERSON());
                                mUnique.setApproved_person_name(mission.getAPPROVED_PERSON_NAME());
                                mUnique.setApproved_time(mission.getAPPROVED_TIME());
                                mUnique.setBegin_time(mission.getBEGIN_TIME());
                                mUnique.setStatus(mission.getSTATUS());
                                mUnique.setCreate_time(mission.getCREATE_TIME());
                                mUnique.setDelivery_time(mission.getDELIVERY_TIME());
                                mUnique.setEnd_time(mission.getEND_TIME());
                                mUnique.setExecute_end_time(mission.getEXECUTE_END_TIME());
                                mUnique.setExecute_start_time(mission.getEXECUTE_START_TIME());
                                mUnique.setFbdw(mission.getFBDW());
                                mUnique.setFbr(mission.getFBR());
                                mUnique.setExamine_status(mission.getEXAMINE_STATUS());
                                mUnique.setJjcd(mission.getJJCD());
                                mUnique.setJsdw(mission.getJSDW());
                                mUnique.setJsr(mission.getJSR());
                                mUnique.setPublisher(mission.getPUBLISHER());
                                mUnique.setPublisher_name(mission.getPUBLISHER_NAME());
                                mUnique.setReceiver(mission.getRECEIVER());
                                mUnique.setReceiver_name(mission.getRECEIVER_NAME());
                                mUnique.setRwly(mission.getRWLY());
                                mUnique.setRwqyms(mission.getRWQYMS());
                                mUnique.setTask_name(mission.getTASK_NAME());
                                mUnique.setSpr(mission.getSPR());
                                mUnique.setSpyj(mission.getSPYJ());
                                mUnique.setTask_area(mission.getRWQYMS());
                                mUnique.setTask_content(mission.getRWMS());
                                mUnique.setTypename(mission.getTYPENAME());
                                mUnique.setTypeoftask(mission.getTYPEOFTASK());
                                mUnique.setTaskid(mission.getID());
                                mUnique.setUserid(OkingContract.CURRENTUSER.getUserid());
                                GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().update(mUnique);

                            } else {
                                mUnique = new GreenMissionTask();
                                mUnique.setApproved_person(mission.getAPPROVED_PERSON());
                                mUnique.setApproved_person_name(mission.getAPPROVED_PERSON_NAME());
                                mUnique.setApproved_time(mission.getAPPROVED_TIME());
                                mUnique.setBegin_time(mission.getBEGIN_TIME());
                                mUnique.setCreate_time(mission.getCREATE_TIME());
                                mUnique.setDelivery_time(mission.getDELIVERY_TIME());
                                mUnique.setEnd_time(mission.getEND_TIME());
                                mUnique.setExecute_end_time(mission.getEXECUTE_END_TIME());
                                mUnique.setExecute_start_time(mission.getEXECUTE_START_TIME());
                                mUnique.setFbdw(mission.getFBDW());
                                mUnique.setFbr(mission.getFBR());
                                mUnique.setJjcd(mission.getJJCD());
                                mUnique.setJsdw(mission.getJSDW());
                                mUnique.setJsr(mission.getJSR());
                                mUnique.setPublisher(mission.getPUBLISHER());
                                mUnique.setPublisher_name(mission.getPUBLISHER_NAME());
                                mUnique.setReceiver(mission.getRECEIVER());
                                mUnique.setReceiver_name(mission.getRECEIVER_NAME());
                                mUnique.setRwly(mission.getRWLY());
                                mUnique.setRwqyms(mission.getRWQYMS());
                                mUnique.setExamine_status(mission.getEXAMINE_STATUS());
                                mUnique.setSpr(mission.getSPR());
                                mUnique.setSpyj(mission.getSPYJ());
                                mUnique.setStatus(mission.getSTATUS());
                                mUnique.setTask_area(mission.getRWQYMS());
                                mUnique.setTask_name(mission.getTASK_NAME());
                                mUnique.setTask_content(mission.getRWMS());
                                mUnique.setTypename(mission.getTYPENAME());
                                mUnique.setTypeoftask(mission.getTYPEOFTASK());
                                mUnique.setTaskid(mission.getID());
                                mUnique.setUserid(OkingContract.CURRENTUSER.getUserid());
                                long id = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().insert(mUnique);

                                GreenMember leader = new GreenMember();
                                leader.setUsername(mission.getRECEIVER_NAME());
                                leader.setUserid(mission.getRECEIVER());
                                leader.setTaskid(mission.getID());
                                leader.setGreenMemberId(id);
                                leader.setPost("任务负责人");
                                GreenDAOManager.getInstence().getDaoSession().getGreenMemberDao().insert(leader);

                            }

                            if (mPosition==mRecords.size()){
                                mGreenHeadMissionTasks2 = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                                        .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                                        .whereOr(GreenMissionTaskDao.Properties.Examine_status.eq(6), GreenMissionTaskDao.Properties.Examine_status.eq(5), GreenMissionTaskDao.Properties.Examine_status.eq(4), GreenMissionTaskDao.Properties.Examine_status.eq(2), GreenMissionTaskDao.Properties.Examine_status.eq(1), GreenMissionTaskDao.Properties.Examine_status.eq(0), GreenMissionTaskDao.Properties.Status.eq("1"), GreenMissionTaskDao.Properties.Status.eq("2"), GreenMissionTaskDao.Properties.Status.eq("3"), GreenMissionTaskDao.Properties.Status.eq("4"), GreenMissionTaskDao.Properties.Status.eq("7"), GreenMissionTaskDao.Properties.Status.eq("100"))
                                        .list();
                                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                    @Override
                                    public void run() {
                                        mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks2);
                                        mTv_title.setText("待办(" + mGreenHeadMissionTasks2.size() + ")");
                                    }
                                });

                                }



                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.i("Oking5", "失败----：" + throwable.toString());
                            AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                                }
                            });

                        }
                    });

        }else if (type == 3){
            Log.i("Oking5","收到消息批示》》》》》》》》"+mPushTaskid);
            GreenMissionTask unique = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder()
                    .where(GreenMissionTaskDao.Properties.Taskid.eq(mPushTaskid)).unique();
            if (unique!=null){
                List<GreenMissionTask> datas = mHeadTaskRecyAdapter.getData();
                for (int i=0;i<datas.size();i++){
                    if (datas.get(i).getTaskid().equals(mPushTaskid)){
                       mHeadTaskRecyAdapter.remove(i);
                        mTv_title.setText("待办(" + mHeadTaskRecyAdapter.getData().size() + ")");
                    }
                }
                GreenDAOManager.getInstence().getDaoSession().delete(unique);

            }


        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent4(UpdateGreenMissionTaskOV event) {
        int type = event.getType();
        int position = event.getPosition();
        if (type == 100) {
            mHeadTaskRecyAdapter.remove(position);

        }else if (type == 200){

            mHeadTaskRecyAdapter.setData(position,event.getMissionTask());

        }else {

            mHeadTaskRecyAdapter.setData(position, event.getMissionTask());
        }
        mTv_title.setText("待办(" + mHeadTaskRecyAdapter.getData().size() + ")");
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


    private void getHttpMissionList() {
        if (mRxDialogLoading == null) {
            mRxDialogLoading = new RxDialogLoading(getActivity(), false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialogInterface.cancel();
                }
            });

            mRxDialogLoading.show();
        }
        mRxDialogLoading.setLoadingText("正在加载数据...");

        if (mLoadHttpMissionPresenter == null) {
            mLoadHttpMissionPresenter = new LoadHttpMissionPresenter(new LoadHttpMissionContract.View() {
                @Override
                public void loadHttpMissionSucc(List<GreenMissionTask> greenMissionTasks) {
                    mGreenHeadMissionTasks.clear();
                    // '执行状态 0-未发布  1-已发布 2-已经审核 3-确认接受  4-任务开始  5-任务完成 7-不通过审核 9-退回修改(日志审核)';
                    mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                            .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                            .whereOr(GreenMissionTaskDao.Properties.Examine_status.eq(6), GreenMissionTaskDao.Properties.Examine_status.eq(5), GreenMissionTaskDao.Properties.Examine_status.eq(4), GreenMissionTaskDao.Properties.Examine_status.eq(2), GreenMissionTaskDao.Properties.Examine_status.eq(1), GreenMissionTaskDao.Properties.Examine_status.eq(0), GreenMissionTaskDao.Properties.Status.eq("1"), GreenMissionTaskDao.Properties.Status.eq("2"), GreenMissionTaskDao.Properties.Status.eq("3"), GreenMissionTaskDao.Properties.Status.eq("4"), GreenMissionTaskDao.Properties.Status.eq("7"), GreenMissionTaskDao.Properties.Status.eq("100"))
                            .list();
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            mRxDialogLoading.cancel();

                            mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                            mTv_title.setText("待办(" + mGreenHeadMissionTasks.size() + ")");
                            mLayoutSwipeRefresh.setRefreshing(false);
                        }


                    });
                }

                @Override
                public void loadHttpMissionProgress(final int total, final int count) {
                    if (mRxDialogLoading.isShowing()) {

                        AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                            @Override
                            public void run() {
                                mRxDialogLoading.setLoadingText(count + "/" + total);
                            }
                        });
                    }
                }

                @Override
                public void loadHttpMissionFail(Throwable ex) {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            mRxDialogLoading.cancel();
                            mLayoutSwipeRefresh.setRefreshing(false);
                        }
                    });
                }

                @Override
                public void loadMissionFromLocal(List<GreenMissionTask> greenMissionTasks) {
                    mGreenHeadMissionTasks.clear();
                    // '执行状态 0-未发布  1-已发布 2-已经审核 3-确认接受  4-任务开始  5-任务完成 7-不通过审核 9-退回修改(日志审核)';
                    mGreenHeadMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao()
                            .queryBuilder().where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid()))
                            .whereOr(GreenMissionTaskDao.Properties.Examine_status.eq(6), GreenMissionTaskDao.Properties.Examine_status.eq(5), GreenMissionTaskDao.Properties.Examine_status.eq(4), GreenMissionTaskDao.Properties.Examine_status.eq(2), GreenMissionTaskDao.Properties.Examine_status.eq(1), GreenMissionTaskDao.Properties.Examine_status.eq(0), GreenMissionTaskDao.Properties.Status.eq("1"), GreenMissionTaskDao.Properties.Status.eq("2"), GreenMissionTaskDao.Properties.Status.eq("3"), GreenMissionTaskDao.Properties.Status.eq("4"), GreenMissionTaskDao.Properties.Status.eq("7"), GreenMissionTaskDao.Properties.Status.eq("100"))
                            .list();
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            mRxDialogLoading.cancel();
                            mHeadTaskRecyAdapter.setNewData(mGreenHeadMissionTasks);
                            mTv_title.setText("待办(" + mGreenHeadMissionTasks.size() + ")");
                            mLayoutSwipeRefresh.setRefreshing(false);
                        }
                    });
                }

            });
        }

        mLoadHttpMissionPresenter.loadHttpMission(2, OkingContract.CURRENTUSER.getUserid());
    }
}
