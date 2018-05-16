package com.zhang.okinglawenforcementphone.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.WrittenItemBean;
import com.zhang.okinglawenforcementphone.beans.WrittenRecordLevel0;

import java.util.List;


/**
 * Created by Administrator on 2018/5/7/007.
 */

public class ExpandableItemCaseRegistAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;
    public static final int TYPE_LEVEL_3 = 3;

    public ExpandableItemCaseRegistAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.activity_mission_recor_level0);
        addItemType(TYPE_LEVEL_1, R.layout.case_regist_info1);
        addItemType(TYPE_LEVEL_2, R.layout.case_regist_info2);
        addItemType(TYPE_LEVEL_3, R.layout.case_regist_info3);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final WrittenRecordLevel0 lv0 = (WrittenRecordLevel0) item;
                helper.setText(R.id.title, lv0.subTitle);
                helper.itemView.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        int adapterPosition = helper.getAdapterPosition();
                        List<WrittenItemBean> subItems = lv0.getSubItems();
                        int subItemType = subItems.get(0).getItemType();

                        if (lv0.isExpanded()) {
                            collapse(adapterPosition);


                        } else {
                            expand(adapterPosition);
                        }


                    }

                });

                break;
            case TYPE_LEVEL_1:
                break;
            case TYPE_LEVEL_2:
                break;
            case TYPE_LEVEL_3:
                break;
            default:
                break;
        }
    }
}
