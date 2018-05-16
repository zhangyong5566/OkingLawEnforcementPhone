package com.zhang.okinglawenforcementphone.beans;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhang.okinglawenforcementphone.adapter.ExpandableItemAdapter;
import com.zhang.okinglawenforcementphone.adapter.ExpandableWrittenRecordAdapter;

/**
 * Created by Administrator on 2018/4/27/027.
 */

public class WrittenRecordLevel0 extends AbstractExpandableItem<WrittenItemBean> implements MultiItemEntity {
    public String subTitle;


    public WrittenRecordLevel0( String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public int getItemType() {
        return ExpandableWrittenRecordAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
