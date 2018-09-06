package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

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
import com.zhang.baselib.BaseApplication;
import com.zhang.baselib.http.schedulers.RxSchedulersHelper;
import com.zhang.baselib.ui.views.RxDialogSureCancel;
import com.zhang.baselib.ui.views.RxToast;
import com.zhang.baselib.utils.ActivityUtil;
import com.zhang.baselib.utils.DeviceUtil;
import com.zhang.baselib.utils.FileUtil;
import com.zhang.okinglawenforcementphone.OkingEMManager;
import com.zhang.okinglawenforcementphone.OkingFileManager;
import com.zhang.okinglawenforcementphone.OkingJPushManager;
import com.zhang.okinglawenforcementphone.OkingLocationManager;
import com.zhang.okinglawenforcementphone.OkingNotificationManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.SendEmailManager;
import com.zhang.okinglawenforcementphone.adapter.StatisRcyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.NewsTaskOV;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.db.LawDao;
import com.zhang.okinglawenforcementphone.mvp.contract.UploadLocationToServerContract;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.IndexFragment;
import com.zhang.okinglawenforcementphone.mvp.ui.fragments.UserFragment;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;
import com.zhang.okinglawenforcementphone.views.bottonbar.AlphaTabsIndicator;
import com.zhang.okinglawenforcementphone.views.bottonbar.OnTabChangedListner;

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
import butterknife.OnClick;
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
    @BindView(R.id.alphaIndicator)
    AlphaTabsIndicator mAlphaTabsIndicator;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private IndexFragment mIndexFragment;
    //    private MessageFragment mMessageFragment;
//    private AddressBookFragment mAddressBookFragment;
    private UserFragment mUserFragment;
    private Gson gson = new Gson();

    private EaseContactListFragment mEaseContactListFragment;
    private Map<String, EaseUser> mContacts;
    private int mUnreadMsgCount = 0;
    private MsgListener mMsgListener;
    private EaseConversationListFragment mEaseConversationListFragment;
    private Intent mIntent;
    private boolean mIsLoginIMErro = false;
    private boolean isMessageFragmentShow = false;
    private String mCrashDir;
    private long mExitTime;
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

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mCrashDir = getExternalCacheDir().getPath() + File.separator + "crash" + File.separator;
        } else {
            mCrashDir = getCacheDir().getPath() + File.separator + "crash" + File.separator;
        }
        final String content = "用户名：" + OkingContract.CURRENTUSER.getUserName() + "\n";
        //获取奔溃日志发送邮件
        Schedulers.io().createWorker().schedule(new Runnable() {
            @Override
            public void run() {
                List<File> files = FileUtil.listFilesInDir(mCrashDir, false);
                if (files != null) {
                    for (File file : files) {
                        boolean succ = SendEmailManager.send(file, "zhy842667166@qq.com", "水政执法崩溃日志", content);
                        if (succ) {
                            file.delete();
                        }
                    }
                }

            }
        });


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

        OkingJPushManager.getInstence().init();

        login();

        //初始化定位
        OkingLocationManager.getInstence().init();
        OkingLocationManager.getInstence().startLocation(new UploadLocationToServerContract.View() {
            @Override
            public void uploadSucc(String result) {
                Log.i("Oking", "上传位置成功" + result);

            }

            @Override
            public void uploadFail(Throwable ex) {
                Log.i("Oking", "上传位置失败" + ex);
            }
        }, imei, gson);


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
                            mAlphaTabsIndicator.getTabView(1).showNumber(mUnreadMsgCount);
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




    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SerchActivity.class));

            }
        });

        mAlphaTabsIndicator.setOnTabChangedListner(new OnTabChangedListner() {
            @Override
            public void onTabSelected(int tabNum) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (tabNum) {
                    case 0:                 //首页
                        isMessageFragmentShow = false;
                        if (mEaseConversationListFragment != null) {

                            fragmentTransaction.hide(mEaseConversationListFragment);
                        }
                        if (mEaseContactListFragment != null) {

                            fragmentTransaction.hide(mEaseContactListFragment);
                        }
                        if (mUserFragment != null) {

                            fragmentTransaction.hide(mUserFragment);
                        }
                        if (mIndexFragment != null) {
                            fragmentTransaction.show(mIndexFragment);
                        } else {
                            mIndexFragment = IndexFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.bottom_nav_content, mIndexFragment, "IndexFragment");
                        }
                        fragmentTransaction.commitAllowingStateLoss();
                        break;
                    case 1:                 //消息
                        isMessageFragmentShow = true;
                        if (mIsLoginIMErro) {
                            login();
                        }
                        if (mIndexFragment != null) {

                            fragmentTransaction.hide(mIndexFragment);
                        }
                        if (mEaseContactListFragment != null) {

                            fragmentTransaction.hide(mEaseContactListFragment);
                        }
                        if (mUserFragment != null) {

                            fragmentTransaction.hide(mUserFragment);
                        }
                        if (mEaseConversationListFragment != null) {
                            fragmentTransaction.show(mEaseConversationListFragment);
                        } else {
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
                            fragmentTransaction.add(R.id.bottom_nav_content, mEaseConversationListFragment, "EaseConversationListFragment");
                        }
                        mUnreadMsgCount = 0;
                        mAlphaTabsIndicator.removeAllBadge();
                        fragmentTransaction.commitAllowingStateLoss();
                        break;
                    case 2:                     //通讯录
                        isMessageFragmentShow = false;
                        if (mIsLoginIMErro) {
                            login();
                        }
                        if (mIndexFragment != null) {

                            fragmentTransaction.hide(mIndexFragment);
                        }
                        if (mEaseConversationListFragment != null) {

                            fragmentTransaction.hide(mEaseConversationListFragment);
                        }
                        if (mUserFragment != null) {

                            fragmentTransaction.hide(mUserFragment);
                        }

                        if (mEaseContactListFragment != null) {

                            fragmentTransaction.show(mEaseContactListFragment);

                        } else {
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
                            mEaseContactListFragment.setContactsMap(mContacts);
                            fragmentTransaction.add(R.id.bottom_nav_content, mEaseContactListFragment, "EaseContactListFragment");

                        }
                        fragmentTransaction.commitAllowingStateLoss();
                        break;
                    case 3:
                        isMessageFragmentShow = false;
                        if (mIndexFragment != null) {

                            fragmentTransaction.hide(mIndexFragment);
                        }
                        if (mEaseConversationListFragment != null) {

                            fragmentTransaction.hide(mEaseConversationListFragment);
                        }
                        if (mEaseContactListFragment != null) {

                            fragmentTransaction.hide(mEaseContactListFragment);
                        }
                        if (mUserFragment != null) {
                            fragmentTransaction.show(mUserFragment);
                        } else {
                            mUserFragment = UserFragment.newInstance(null, null);
                            fragmentTransaction.add(R.id.bottom_nav_content, mUserFragment, "UserFragment");

                        }
                        fragmentTransaction.commitAllowingStateLoss();
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
        mIndexFragment = IndexFragment.newInstance(null, null);
        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_nav_content, mIndexFragment, "IndexFragment").commit();
        mAlphaTabsIndicator.setCurrentItem(0);
    }







    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        OkingLocationManager.getInstence().cancelLocation();
        OkingEMManager.getInstence().unRegist();

        Process.killProcess(Process.myPid());
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
            if (!isMessageFragmentShow) {
                mUnreadMsgCount = mUnreadMsgCount + list.size();
                AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        mAlphaTabsIndicator.getTabView(1).showNumber(mUnreadMsgCount);
                    }
                });
            } else {
                mUnreadMsgCount = 0;
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

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {//System.currentTimeMillis()无论何时调用，肯定大于2000
                RxToast.warning("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                if (EMClient.getInstance().isLoggedInBefore()) {

                    EMClient.getInstance().logout(true);
                }
                ActivityUtil.AppExit(BaseApplication.getApplictaion());
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
