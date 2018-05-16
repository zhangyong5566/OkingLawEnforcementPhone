package com.zhang.okinglawenforcementphone.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2018/4/27/027.
 */

public class WrittenItemBean implements MultiItemEntity {
    private int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
