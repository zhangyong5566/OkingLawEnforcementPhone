package com.zhang.okinglawenforcementphone;

import android.content.Context;

import com.zhang.okinglawenforcementphone.beans.DaoMaster;
import com.zhang.okinglawenforcementphone.beans.DaoSession;
import com.zhang.okinglawenforcementphone.db.MyGreenDaoDbHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Administrator on 2018/4/18.
 */

public class GreenDAOManager {
    public static final boolean ENCRYPTED = true;
    private static GreenDAOManager mGreenDAOMannager;
    private DaoSession daoSession;
    private MyGreenDaoDbHelper mDevOpenHelper;
    private GreenDAOManager() {
    }

    public static GreenDAOManager getInstence() {
        if (mGreenDAOMannager == null) {
            synchronized (GreenDAOManager.class) {
                if (mGreenDAOMannager == null) {
                    mGreenDAOMannager = new GreenDAOManager();
                }
            }
        }
        return mGreenDAOMannager;
    }

   public void initGreenDao(Context context){
       MyGreenDaoDbHelper helper = new MyGreenDaoDbHelper(context, ENCRYPTED ? "gdWater-db-encrypted" : "gdWater-db");
       Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
       daoSession = new DaoMaster(db).newSession();
       mDevOpenHelper = new MyGreenDaoDbHelper(context, "gdWater-db");
    }

    public DaoSession getDaoSession(){
       return daoSession;
    }

    //升级表
    public void updateTable(){
       mDevOpenHelper.onUpgrade(mDevOpenHelper.getWritableDatabase(), 1, 1);
    }
}
