package com.zhang.okinglawenforcementphone.beans;

import java.util.List;

/**
 * Created by Administrator on 2018/5/25/025.
 */

public class SourceArrayOV {
    private int mType;
    private String mSource;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    @Override
    public String toString() {
        return "SourceArrayOV{" +
                "mType=" + mType +
                ", mSource='" + mSource + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
