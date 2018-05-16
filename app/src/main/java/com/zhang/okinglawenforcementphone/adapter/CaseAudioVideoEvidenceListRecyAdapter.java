package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenEvidence;

import java.util.List;


/**
 * Created by Administrator on 2018/2/24.
 */

public class CaseAudioVideoEvidenceListRecyAdapter extends BaseQuickAdapter<GreenEvidence, BaseViewHolder> {
    public CaseAudioVideoEvidenceListRecyAdapter(int layoutResId, @Nullable List<GreenEvidence> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GreenEvidence item) {

        helper.setText(R.id.zjmc_tv, item.getZJMC());
        helper.setText(R.id.cjdd_tv, item.getCJDD());
        helper.setText(R.id.zjnr_tv, item.getZJNR());

        if (item.getOtype().equals("YYSP")) {
            helper.setText(R.id.lx_tv, "语音、视频");
        } else if (item.getOtype().equals("SP")) {
            helper.setText(R.id.lx_tv, "视频");
        } else if (item.getOtype().equals("YY")) {
            helper.setText(R.id.lx_tv, "语音");
        }

        helper.addOnClickListener(R.id.upload_button)
                .addOnClickListener(R.id.delete_button)
                .addOnClickListener(R.id.edit_button);

        if (item.getIsUpload()) {
            helper.setVisible(R.id.upload_button, false);
            helper.setVisible(R.id.delete_button, false);
            helper.setText(R.id.edit_button, "查看");
        }

    }
}
