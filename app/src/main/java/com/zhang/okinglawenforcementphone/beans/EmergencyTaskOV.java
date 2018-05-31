package com.zhang.okinglawenforcementphone.beans;

/**
 * Created by Administrator on 2018/5/25/025.
 */

public class EmergencyTaskOV {
    private int mType;
    private GreenMissionTask mGreenMissionTask;

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public GreenMissionTask getGreenMissionTask() {
        return mGreenMissionTask;
    }

    public void setGreenMissionTask(GreenMissionTask greenMissionTask) {
        mGreenMissionTask = greenMissionTask;
    }
}
