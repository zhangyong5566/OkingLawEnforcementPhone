package com.zhang.baselib.ui.views;

import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhang.baselib.R;
import com.zhang.baselib.utils.RegUtil;
import com.zhang.baselib.utils.TextUtil;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogSure extends RxDialog {

//    private TextView mIvLogo;
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvSure;


    public TextView getTvTitle() {
        return mTvTitle;
    }

    public TextView getTvSure() {
        return mTvSure;
    }

    public TextView getTvContent() {
        return mTvContent;
    }


    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setSure(String content) {
        mTvSure.setText(content);
    }

//    public TextView getIvLogo() {
//        return mIvLogo;
//    }

//    public void setIvLogo(String ivLogo) {
//        mIvLogo.setText(ivLogo);
//    }

    public void setContent(String str) {
        if (RegUtil.isURL(str)) {
            // 响应点击事件的话必须设置以下属性
            mTvContent.setMovementMethod(LinkMovementMethod.getInstance());
            mTvContent.setText(TextUtil.getBuilder("").setBold().append(str).setUrl(str).create());//当内容为网址的时候，内容变为可点击
        } else {
            mTvContent.setText(str);
        }

    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure, null);
        mTvSure = (TextView) dialog_view.findViewById(R.id.tv_sure);
        mTvTitle = (TextView) dialog_view.findViewById(R.id.tv_title);
        mTvTitle.setTextIsSelectable(true);
        mTvContent = (TextView) dialog_view.findViewById(R.id.tv_content);
        mTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTvContent.setTextIsSelectable(true);
//        mIvLogo = (TextView) dialog_view.findViewById(R.id.iv_logo);
        setContentView(dialog_view);
    }

    public RxDialogSure(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogSure(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSure(Context context) {
        super(context);
        initView();
    }

    public RxDialogSure(Activity context) {
        super(context);
        initView();
    }

    public RxDialogSure(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

}
