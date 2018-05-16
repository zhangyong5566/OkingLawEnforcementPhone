package com.zhang.okinglawenforcementphone.beans;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhang.okinglawenforcementphone.adapter.ExpandableItemAdapter;

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
        return ExpandableItemAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
