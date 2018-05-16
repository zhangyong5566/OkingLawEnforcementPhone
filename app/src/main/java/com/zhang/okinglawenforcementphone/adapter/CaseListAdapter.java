package com.zhang.okinglawenforcementphone.adapter;

import android.support.annotation.Nullable;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.beans.GreenCase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * Created by zhao on 2016/9/12.
 */
public class CaseListAdapter extends BaseQuickAdapter<GreenCase, BaseViewHolder> {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public CaseListAdapter(int layoutResId, @Nullable List<GreenCase> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, GreenCase item) {

        helper.setText(R.id.tv_caseid, Html.fromHtml("<font color=\"#98CF60\">案件编号：</font>"+item.getAJID()));
        helper.setText(R.id.tv_casename, Html.fromHtml("<font color=\"#98CF60\">案件名称：</font>"+item.getAJMC()));
        helper.setText(R.id.tv_casetime, Html.fromHtml("<font color=\"#98CF60\">受理时间：</font>"+dateFormat.format(item.getSLRQ())));


        switch (item.getSLXX_ZT()) {
            case "SL":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>受理"));
                break;
            case "CBBDCQZ":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>承办并调查取证"));
                break;
            case "ZB":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>转办"));
                break;
            case "LA":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>立案"));
                break;
            case "AJSC":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>案件审查"));
                break;
            case "BYCF":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>不予处罚"));
                break;
            case "WSZL":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>完善资料"));
                break;
            case "YS":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>移送"));
                break;
            case "CFGZHTZ":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>处罚告知或听证"));
                break;
            case "TZ":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>听证"));
                break;
            case "FH":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>复核"));
                break;
            case "CFJD":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>处罚决定"));
                break;
            case "ZX":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>执行"));
                break;
            case "JABGD":
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>结案并归档"));
                break;
            default:
                helper.setText(R.id.tv_casestate, Html.fromHtml("<font color=\"#98CF60\">状态：</font>未知"));
                break;
        }


    }


}
