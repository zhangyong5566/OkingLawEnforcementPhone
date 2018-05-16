package com.zhang.okinglawenforcementphone.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2018/4/24/024.
 */

public class RecorItemBean implements MultiItemEntity {
    private int type;
    private GreenMissionLog mGreenMissionLog;
    private GreenMissionTask mGreenMissionTask;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GreenMissionLog getGreenMissionLog() {
        return mGreenMissionLog;
    }

    public void setGreenMissionLog(GreenMissionLog greenMissionLog) {
        mGreenMissionLog = greenMissionLog;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public GreenMissionTask getGreenMissionTask() {
        return mGreenMissionTask;
    }

    public void setGreenMissionTask(GreenMissionTask greenMissionTask) {
        mGreenMissionTask = greenMissionTask;
    }
}
