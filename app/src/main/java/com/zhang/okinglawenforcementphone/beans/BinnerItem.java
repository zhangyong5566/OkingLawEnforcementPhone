package com.zhang.okinglawenforcementphone.beans;

/**
 * Created by Administrator on 2018/5/28/028.
 */

public class BinnerItem {
    private String title;
    private int picPath;
    private String toContent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPicPath() {
        return picPath;
    }

    public void setPicPath(int picPath) {
        this.picPath = picPath;
    }

    public String getToContent() {
        return toContent;
    }

    public void setToContent(String toContent) {
        this.toContent = toContent;
    }

    @Override
    public String toString() {
        return "BinnerItem{" +
                "title='" + title + '\'' +
                ", picPath='" + picPath + '\'' +
                ", toContent='" + toContent + '\'' +
                '}';
    }
}
