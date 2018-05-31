package com.zhang.okinglawenforcementphone.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/18/018.
 */

public class SeachBean implements MultiItemEntity {
    private int itemType;
    private String menuItme;
    private Long Id;
    private String taskId;
    private String taskName;
    private String publisherName;
    private String state;


    public String getMenuItme() {
        return menuItme;
    }

    public void setMenuItme(String menuItme) {
        this.menuItme = menuItme;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "SeachBean{" +
                "itemType=" + itemType +
                ", menuItme='" + menuItme + '\'' +
                ", Id=" + Id +
                ", taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", publisherName='" + publisherName + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
