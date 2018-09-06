package com.zhang.okinglawenforcementphone.beans;

/**
 * Created by Administrator on 2018/5/22/022.
 */

public class NewsTaskOV {
    public int mType;
    public String taskid;
    public GreenMissionTask mGreenMissionTask;

    public NewsTaskOV(int type, String taskid, GreenMissionTask greenMissionTask) {
        mType = type;
        this.taskid = taskid;
        mGreenMissionTask = greenMissionTask;
    }
}
