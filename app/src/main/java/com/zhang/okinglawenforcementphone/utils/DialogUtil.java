package com.zhang.okinglawenforcementphone.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhang.baselib.utils.DensityUtil;
import com.zhang.okinglawenforcementphone.R;

/**
 * Created by Administrator on 2018/4/19.
 */

public class DialogUtil {

    private Dialog bottomDialog;
    private ViewGroup.MarginLayoutParams mParams;

    public void showBottomDialog(Context context, View contentView,float height) {
        if (bottomDialog==null){

            bottomDialog = new Dialog(context, R.style.BottomDialog);
            bottomDialog.setContentView(contentView);
            mParams = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
            mParams.width = context.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(context, 16f);
            mParams.bottomMargin = DensityUtil.dp2px(context, 8f);
            contentView.setLayoutParams(mParams);
            bottomDialog.setCanceledOnTouchOutside(true);
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        }
        mParams.height = DensityUtil.dp2px(context,height);

        bottomDialog.show();
    }
    public void cancelDialog(){
        bottomDialog.dismiss();
    }
}
