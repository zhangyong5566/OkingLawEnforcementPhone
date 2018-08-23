package com.zhang.okinglawenforcementphone.beans;


/**
 * Created by Administrator on 2018/4/8.
 */

public class UpdateGreenMissionTaskOV {
    private int type;
    private int position;
    private GreenMissionTask missionTask;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public GreenMissionTask getMissionTask() {
        return missionTask;
    }

    public void setMissionTask(GreenMissionTask missionTask) {
        this.missionTask = missionTask;
    }
}
