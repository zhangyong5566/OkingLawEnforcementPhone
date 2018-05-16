package com.zhang.okinglawenforcementphone.beans;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class AllMenuItemBean {
    private String title;
    private List<String> mSubList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getSubList() {
        return mSubList;
    }

    public void setSubList(List<String> subList) {
        mSubList = subList;
    }

    @Override
    public String toString() {
        return "AllMenuItemBean{" +
                "title='" + title + '\'' +
                ", mSubList=" + mSubList +
                '}';
    }
}
