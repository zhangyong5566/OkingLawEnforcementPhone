package com.zhang.okinglawenforcementphone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4/004.
 */

public class TitleAdapter extends FragmentPagerAdapter {
    private List<Fragment> list_fragment;
    private List<String> list_Title;

    public TitleAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return list_Title.get(position % list_Title.size());
    }
}
