package com.zhang.okinglawenforcementphone.beans;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2018/4/21.
 */

public class Level0Item extends AbstractExpandableItem<RecorItemBean> implements MultiItemEntity {
    public String subTitle;


    public Level0Item( String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
