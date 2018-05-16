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

    public void showBottomDialog(Context context, View contentView,float height) {
        if (bottomDialog==null){

            bottomDialog = new Dialog(context, R.style.BottomDialog);
            bottomDialog.setContentView(contentView);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
            params.height = DensityUtil.dp2px(context,height);
            params.width = context.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(context, 16f);
            params.bottomMargin = DensityUtil.dp2px(context, 8f);
            contentView.setLayoutParams(params);
            bottomDialog.setCanceledOnTouchOutside(true);
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        }

        bottomDialog.show();
    }
    public void cancelDialog(){
        bottomDialog.dismiss();
    }
}
