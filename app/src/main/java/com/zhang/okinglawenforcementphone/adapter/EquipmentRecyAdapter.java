package com.zhang.okinglawenforcementphone.adapter;

import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenEquipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/4/10.
 */

public class EquipmentRecyAdapter extends BaseMultiItemQuickAdapter<GreenEquipment, BaseViewHolder> {
    private List<GreenEquipment> checkItems = new ArrayList<GreenEquipment>();
    private Map<Integer, Boolean> map = new HashMap<>();// 存放已被选中的CheckBox

    public EquipmentRecyAdapter(List<GreenEquipment> data) {
        super(data);
        addItemType(0, R.layout.equipment_item_head);
        addItemType(1, R.layout.equipment_item_layout);
    }


    @Override
    protected void convert(final BaseViewHolder helper, GreenEquipment item) {
        if (item.getItemType() == 1) {
            helper.setText(R.id.type_textView, item.getMc1());
            helper.setText(R.id.value_textView, item.getValue());

            switch (item.getLy()) {
                case "0":
                    helper.setText(R.id.tv_attribute, "自有");
                    break;
                case "1":
                    helper.setText(R.id.tv_attribute, "租借");
                    break;

                default:
                    break;
            }

            helper.setText(R.id.remarks_textView, item.getRemarks());
            helper.setText(R.id.tv_mc, item.getMc2());
            helper.setOnCheckedChangeListener(R.id.cb, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        map.put(helper.getLayoutPosition(), true);


                    } else {
                        map.remove(helper.getLayoutPosition());


                    }
                }
            });

            if (map != null && map.containsKey(helper.getLayoutPosition())) {
                helper.setChecked(R.id.cb, true);
            } else {
                helper.setChecked(R.id.cb, false);
            }

        }


    }


    public List<GreenEquipment> getCheckItem() {
        List<GreenEquipment> equipmentArrayList = getData();
        checkItems.clear();
        Iterator<Integer> iter = map.keySet().iterator();

        while (iter.hasNext()) {

            int key = iter.next();

            checkItems.add(equipmentArrayList.get(key));


        }
        return checkItems;
    }
}
