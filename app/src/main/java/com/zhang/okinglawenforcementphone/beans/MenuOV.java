package com.zhang.okinglawenforcementphone.beans;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class MenuOV {
    private String mTage;
    private List<MenuItemOV> mMenuItemOVS;

    public String getTage() {
        return mTage;
    }

    public void setTage(String tage) {
        mTage = tage;
    }

    public List<MenuItemOV> getMenuItemOVS() {
        return mMenuItemOVS;
    }

    public void setMenuItemOVS(List<MenuItemOV> menuItemOVS) {
        mMenuItemOVS = menuItemOVS;
    }

    @Override
    public String toString() {
        return "MenuOV{" +
                "mTage='" + mTage + '\'' +
                ", mMenuItemOVS=" + mMenuItemOVS +
                '}';
    }
}
