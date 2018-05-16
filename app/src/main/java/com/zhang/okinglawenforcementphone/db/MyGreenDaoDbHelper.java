package com.zhang.okinglawenforcementphone.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zhang.baselib.db.GreenDaoCompatibleUpdateHelper;
import com.zhang.okinglawenforcementphone.beans.DaoMaster;
import com.zhang.okinglawenforcementphone.beans.GreenMember;
import com.zhang.okinglawenforcementphone.beans.GreenMemberDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionLogDao;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.GreenUserDao;

import org.greenrobot.greendao.database.Database;


/**
 * Created by Administrator on 2018/3/21.
 */

public class MyGreenDaoDbHelper extends DaoMaster.DevOpenHelper {
    public MyGreenDaoDbHelper(Context context, String name) {
        super(context, name);
    }

    public MyGreenDaoDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    @SuppressWarnings("all")
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.i("Oking", "----"+oldVersion + "---先前和更新之后的版本---" + newVersion+"----");
        if (oldVersion < newVersion) {
            Log.i("Oking","进行数据库升级");
            new GreenDaoCompatibleUpdateHelper()
                    .setCallBack(
                            new GreenDaoCompatibleUpdateHelper.GreenDaoCompatibleUpdateCallBack() {
                                @Override
                                public void onFinalSuccess() {
                                    Log.i("Oking","进行数据库升级 ===> 成功");
                                }

                                @Override
                                public void onFailedLog(String errorMsg) {
                                    Log.i("Oking","升级失败日志 ===> "+errorMsg);
                                }
                            }
                    )
                    .compatibleUpdate(
                           db,
                            GreenMemberDao.class,
                            GreenMissionTaskDao.class
                    );
            Log.i("Oking","进行数据库升级--完成");
        }
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        // 不要调用父类的，它默认是先删除全部表再创建
        // super.onUpgrade(db, oldVersion, newVersion);

    }

}
