package com.zhang.okinglawenforcementphone.beans;


/**
 * Created by Administrator on 2018/3/26.
 */

public class RecordLogOV {
    private String area;
    private String summary;
    private String leaderSummary;
    private String equipment;
    private String parts;
    private String time;
    private GreenMissionLog mGreenMissionLog;
    private GreenMissionTask mGreenMissionTask;
    private boolean swisopen;
    private int selePlanPos;
    private int seleMattersPos;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLeaderSummary() {
        return leaderSummary;
    }

    public void setLeaderSummary(String leaderSummary) {
        this.leaderSummary = leaderSummary;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public GreenMissionLog getGreenMissionLog() {
        return mGreenMissionLog;
    }

    public void setGreenMissionLog(GreenMissionLog greenMissionLog) {
        mGreenMissionLog = greenMissionLog;
    }

    public GreenMissionTask getGreenMissionTask() {
        return mGreenMissionTask;
    }

    public void setGreenMissionTask(GreenMissionTask greenMissionTask) {
        mGreenMissionTask = greenMissionTask;
    }

    public boolean isSwisopen() {
        return swisopen;
    }

    public void setSwisopen(boolean swisopen) {
        this.swisopen = swisopen;
    }

    public int getSelePlanPos() {
        return selePlanPos;
    }

    public void setSelePlanPos(int selePlanPos) {
        this.selePlanPos = selePlanPos;
    }

    public int getSeleMattersPos() {
        return seleMattersPos;
    }

    public void setSeleMattersPos(int seleMattersPos) {
        this.seleMattersPos = seleMattersPos;
    }

    @Override
    public String toString() {
        return "RecordLogOV{" +
                "area='" + area + '\'' +
                ", summary='" + summary + '\'' +
                ", leaderSummary='" + leaderSummary + '\'' +
                ", equipment='" + equipment + '\'' +
                ", parts='" + parts + '\'' +
                ", time='" + time + '\'' +
                ", mGreenMissionLog=" + mGreenMissionLog +
                ", mGreenMissionTask=" + mGreenMissionTask +
                ", swisopen=" + swisopen +
                ", selePlanPos=" + selePlanPos +
                ", seleMattersPos=" + seleMattersPos +
                '}';
    }
}
