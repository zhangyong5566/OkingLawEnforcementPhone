package com.zhang.okinglawenforcementphone;

/**
 * Created by Administrator on 2018/4/24/024.
 */

public interface MissionRecorCallBack {
    void completeThePatrol();       //完成巡查
    void saveTheRecord();           //保存记录
    void reportTask();              //任务上报
    void refreshList(int adapterPosition);           //刷新数据
    void refreshMember();           //刷新队员

}
