package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxDialogLoading;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.DeviceUtil;
import com.zhang.okinglawenforcementphone.OkingEMManager;
import com.zhang.okinglawenforcementphone.OkingFileManager;
import com.zhang.okinglawenforcementphone.OkingLocationMannager;
import com.zhang.okinglawenforcementphone.OkingNotificationManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.beans.RefreshTaskMissionEvent;
import com.zhang.okinglawenforcementphone.beans.StopSwipeRefreshEvent;
import com.zhang.okinglawenforcementphone.db.LawDao;
import com.zhang.okinglawenforcementphone.mvp.contract.LoadHttpMissionContract;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadLocationToServerContract;
import com.zhang.okinglawenforcementphone.mvp.presenter.LoadHttpMissionPresenter;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.AddressBookFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.ChatFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.IndexFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.MessageFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.UserFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private Unbinder unbinder;
    @BindView(R.id.bottom_navigation_bar_container)
    BottomBar bottom_navigation_bar_container;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private IndexFragment mIndexFragment;
    //    private MessageFragment mMessageFragment;
//    private AddressBookFragment mAddressBookFragment;
    private UserFragment mUserFragment;
    private Gson gson = new Gson();
    private RxDialogLoading mRxDialogLoading;
    private LoadHttpMissionPresenter mLoadHttpMissionPresenter;
    private EaseContactListFragment mEaseContactListFragment;
    private Map<String, EaseUser> mContacts;
    private int mUnreadMsgCount = 0;
    private MsgListener mMsgListener;
    private EaseConversationListFragment mEaseConversationListFragment;
    private Intent mIntent;
    private boolean mIsLoginIMErro = false;
    private BottomBarTab mNearby;
    private boolean isMessageFragmentShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initData();
        initBottomNavBar();
        setListener();
    }

    private void initData() {
        EventBus.getDefault().register(this);

        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                copyDB();
            }
        });
        final String imei = DeviceUtil.getIMEI(BaseApplication.getApplictaion());
        //初始化文件夹
        OkingFileManager.getInstence().init();

        //初始化通知
        OkingNotificationManager.getInstence().initNotificationChannel();

        //初始化环信
        OkingEMManager.getInstence().initEM();


        login();

        //初始化定位
        OkingLocationMannager.getInstence().init();
        OkingLocationMannager.getInstence().startLocation(new UploadLocationToServerContract.View() {
            @Override
            public void uploadSucc(String result) {
                Log.i("Oking", "上传位置成功" + result);
            }

            @Override
            public void uploadFail(Throwable ex) {
                Log.i("Oking", "上传位置失败" + ex);
            }
        }, imei, gson);

        getHttpMissionList();
    }

    private void login() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                loginIM(e);

            }
        }).compose(RxSchedulersHelper.<String>io_main())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("Oking", "s:" + mUnreadMsgCount);
                        if (s.equals("1")) {
                            mNearby.setBadgeCount(mUnreadMsgCount);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.i("Oking", "获取联系人失败" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loginIM(final ObservableEmitter<String> e) {
        if (EMClient.getInstance().isConnected()) {
            getContacts(e);
        } else {
            EMClient.getInstance().login(OkingContract.CURRENTUSER.getAcount(), "888888", new EMCallBack() {
                @Override
                public void onSuccess() {
                    mIsLoginIMErro = false;
                    Log.i("Oking", "环信登录成功");
                    mMsgListener = new MsgListener();
                    EMClient.getInstance().chatManager().addMessageListener(mMsgListener);
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    getContacts(e);

                }

                @Override
                public void onError(int i, String s) {
                    mIsLoginIMErro = true;
                    Log.i("Oking", "环信登录失败" + s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }

    }


    private void copyDB() {
        //创建一个文件 /data/data/包名/files/address.db
        File file = new File(getFilesDir(), "law.db");
        if (file.exists() && file.length() > 0) {
//            System.out.println("数据库已经拷贝了");
        } else {
            try {
                AssetManager am = getAssets();
                InputStream is = am.open("law.db");
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent1(RefreshTaskMissionEvent event) {
        int type = event.getType();
        if (type == 0) {
            getHttpMissionList();
        }
    }


    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,SerchActivity.class));

            }
        });
        bottom_navigation_bar_container.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                hideAllFrag();//先隐藏所有frag
                switch (tabId) {
                    case R.id.tab_index:            //首页
                        isMessageFragmentShow = false;
                        if (mIndexFragment == null) {

                            mIndexFragment = IndexFragment.newInstance(null, null);
                        }
                        addFrag(mIndexFragment);
                        getSupportFragmentManager().beginTransaction().show(mIndexFragment).commit();
                        break;
                    case R.id.tab_message:          //消息
                        isMessageFragmentShow = true;
                        if (mIsLoginIMErro) {
                            login();
                        }
                        if (mEaseConversationListFragment == null) {
                            mEaseConversationListFragment = new EaseConversationListFragment();
                            mEaseConversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
                                @Override
                                public void onListItemClicked(EMConversation conversation) {
                                    mIntent = new Intent(MainActivity.this, ChatActivity.class);
                                    mIntent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                                    mIntent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.getLastMessage().getUserName());
                                    startActivity(mIntent);
                                }
                            });


                        }

                        mUnreadMsgCount = 0;
                        mNearby.removeBadge();
                        addFrag(mEaseConversationListFragment);
                        getSupportFragmentManager().beginTransaction().show(mEaseConversationListFragment).commit();

                        break;
                    case R.id.tab_addressBook:          //通讯录
                        isMessageFragmentShow = false;
                        if (mIsLoginIMErro) {
                            login();
                        }
                        if (mEaseContactListFragment == null) {

                            mEaseContactListFragment = new EaseContactListFragment();

                            mEaseContactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
                                @Override
                                public void onListItemClicked(EaseUser user) {
                                    mIntent = new Intent(MainActivity.this, ChatActivity.class);
                                    mIntent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                                    mIntent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());
                                    startActivity(mIntent);
                                }
                            });

                        }
                        mEaseContactListFragment.setContactsMap(mContacts);
//                if (mAddressBookFragment == null) {
//
//                    mAddressBookFragment = AddressBookFragment.newInstance(null, null);
//                }
                        addFrag(mEaseContactListFragment);
                        getSupportFragmentManager().beginTransaction().show(mEaseContactListFragment).commit();

                        break;
                    case R.id.tab_user:
                        isMessageFragmentShow = false;
                        if (mUserFragment == null) {

                            mUserFragment = UserFragment.newInstance(null, null);
                        }

                        addFrag(mUserFragment);
                        getSupportFragmentManager().beginTransaction().show(mUserFragment).commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 初始化底部导航栏
     */
    private void initBottomNavBar() {
        mTvTitle.setText(OkingContract.CURRENTUSER.getDeptname());
        mNearby = bottom_navigation_bar_container.getTabWithId(R.id.tab_message);


        bottom_navigation_bar_container.selectTabAtPosition(0);

    }


    private void addFrag(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (fragment != null && !fragment.isAdded()) {
            ft.add(R.id.bottom_nav_content, fragment);
        }
        ft.commit();
    }

    private void hideAllFrag() {
        hideFrag(mIndexFragment);
        hideFrag(mEaseConversationListFragment);
        hideFrag(mEaseContactListFragment);
        hideFrag(mUserFragment);
    }


    /**
     * 隐藏fragment
     *
     * @param frag
     */
    private void hideFrag(Fragment frag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (frag != null && frag.isAdded()) {
            ft.hide(frag);
        }
        ft.commit();
    }


    private void getHttpMissionList() {
        if (mRxDialogLoading == null) {
            mRxDialogLoading = new RxDialogLoading(this, false, new DialogInterface.OnCancelListener() {
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
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            mRxDialogLoading.cancel();
                            StopSwipeRefreshEvent stopSwipeRefreshEvent = new StopSwipeRefreshEvent();
                            stopSwipeRefreshEvent.setType(0);
                            EventBus.getDefault().post(stopSwipeRefreshEvent);
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
                            StopSwipeRefreshEvent stopSwipeRefreshEvent = new StopSwipeRefreshEvent();
                            stopSwipeRefreshEvent.setType(0);
                            EventBus.getDefault().post(stopSwipeRefreshEvent);
                        }
                    });
                }

                @Override
                public void loadMissionFromLocal(List<GreenMissionTask> greenMissionTasks) {
                    AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            mRxDialogLoading.cancel();
                            StopSwipeRefreshEvent stopSwipeRefreshEvent = new StopSwipeRefreshEvent();
                            stopSwipeRefreshEvent.setType(0);
                            EventBus.getDefault().post(stopSwipeRefreshEvent);
                        }
                    });
                }
            });
        }

        mLoadHttpMissionPresenter.loadHttpMission(2, OkingContract.CURRENTUSER.getUserid());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkingLocationMannager.getInstence().cancelLocation();
        EventBus.getDefault().unregister(this);
        OkingEMManager.getInstence().unRegist();
        Log.i("Oking", "销毁");
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    private void getContacts(ObservableEmitter<String> emet) {
        mContacts = new HashMap<String, EaseUser>();
        List<String> usernames = new ArrayList<>();
        try {
            usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
        } catch (HyphenateException e) {
            e.printStackTrace();
            emet.onError(e);
        }
        if (usernames != null && usernames.size() > 0) {
            for (String s : usernames) {
                String nick = LawDao.getGdWaterContact(s);
                EaseUser easeUser = new EaseUser(s + "(" + nick + ")");
                easeUser.setNickname(nick);
                mContacts.put(s, easeUser);
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(s);

                if (conversation != null) {
                    mUnreadMsgCount = mUnreadMsgCount + conversation.getUnreadMsgCount();
                }
            }
            emet.onNext("1");
        }


    }

    class MsgListener implements EMMessageListener {

        @Override
        public void onMessageReceived(List<EMMessage> list) {

            /**
             * 巨坑：这个回调方法是子线程，请切换主线程更新UI！！！！！！！！！！！！！1
             */
            if (!isMessageFragmentShow){
                mUnreadMsgCount = mUnreadMsgCount + list.size();
                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        mNearby.setBadgeCount(mUnreadMsgCount);
                    }
                });
            }else {
                mUnreadMsgCount=0;
            }



        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            Log.i("Oking", "读取:" + list.toString());
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }


        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
//
            RxToast.error(getApplicationContext(), "返回键...", Toast.LENGTH_SHORT).show();
            return true;//return true;拦截事件传递,从而屏蔽back键。
        }
        if (KeyEvent.KEYCODE_HOME == keyCode) {
            RxToast.error(getApplicationContext(), "HOME 键已被禁用...", Toast.LENGTH_SHORT).show();
            return true;//同理
        }
        return super.onKeyDown(keyCode, event);
    }
}
