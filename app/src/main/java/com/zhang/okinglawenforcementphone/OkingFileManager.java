package com.zhang.okinglawenforcementphone;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018/5/16.
 */

public class OkingFileManager {
    private static OkingFileManager mOkingFileManager;

    private OkingFileManager() {
    }

    public static final OkingFileManager getInstence() {
        if (mOkingFileManager == null) {
            synchronized (OkingFileManager.class) {
                if (mOkingFileManager == null) {
                    mOkingFileManager = new OkingFileManager();
                }
            }
        }

        return mOkingFileManager;
    }

    public void init() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "oking/mission_pic");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();
        }
        File mediaStorageSignDir = new File(Environment.getExternalStorageDirectory(), "oking/mission_signature");
        if (!mediaStorageSignDir.exists()) {
            mediaStorageSignDir.mkdir();
        }

        File mediaStorageVideoDir = new File(Environment.getExternalStorageDirectory(), "oking/mission_video");

        if (!mediaStorageVideoDir.exists()) {
            mediaStorageVideoDir.mkdir();
        }

        File printStorageVideoDir = new File(Environment.getExternalStorageDirectory(), "oking/print");

        if (!printStorageVideoDir.exists()) {
            printStorageVideoDir.mkdir();
        }

        File locationStorageVideoDir = new File(Environment.getExternalStorageDirectory(), "oking/location");

        if (!locationStorageVideoDir.exists()) {
            locationStorageVideoDir.mkdir();
        }


        File logStorageVideoDir = new File(Environment.getExternalStorageDirectory(), "oking/System_log");

        if (!logStorageVideoDir.exists()) {
            logStorageVideoDir.mkdir();
        }

    }
}
