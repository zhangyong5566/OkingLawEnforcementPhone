package com.zhang.okinglawenforcementphone.beans;

/**
 * Created by Administrator on 2018/4/18.
 */

public class MenuItemOV {
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
        return "MenuItemOV{" +
                "title='" + title + '\'' +
                ", icon=" + icon +
                '}';
    }
}
