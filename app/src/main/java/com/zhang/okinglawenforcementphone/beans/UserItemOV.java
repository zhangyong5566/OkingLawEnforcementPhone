package com.zhang.okinglawenforcementphone.beans;

/**
 * Created by Administrator on 2018/4/19.
 */

public class UserItemOV {
    private String title;
    private int icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "UserItemOV{" +
                "title='" + title + '\'' +
                ", icon=" + icon +
                '}';
    }
}
