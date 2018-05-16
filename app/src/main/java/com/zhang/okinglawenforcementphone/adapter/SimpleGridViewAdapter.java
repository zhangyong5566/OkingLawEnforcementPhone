package com.zhang.okinglawenforcementphone.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhang.okinglawenforcementphone.beans.GreenMedia;

import java.util.ArrayList;


/**
 * Created by zhao on 2016/10/9.
 */

public abstract class SimpleGridViewAdapter extends BaseAdapter {

    private ArrayList<GreenMedia> mGreenMedias;

    public SimpleGridViewAdapter(ArrayList<GreenMedia> greenMedias) {
        this.mGreenMedias = greenMedias;
    }

    @Override
    public int getCount() {
        return mGreenMedias.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);
}
