package com.zhang.okinglawenforcementphone.views.bottonbar;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/12/012.
 */

public class AlphaTabsIndicator extends LinearLayout {

    private OnTabChangedListner mListner;
    private List<AlphaTabView> mTabViews;
    private boolean ISINIT;
    /**
     * 子View的数量
     */
    private int mChildCounts;
    /**
     * 当前的条目索引
     */
    private int mCurrentItem = 0;

    public AlphaTabsIndicator(Context context) {
        this(context, null);
    }

    public AlphaTabsIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaTabsIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        post(new Runnable() {
            @Override
            public void run() {
                isInit();
            }
        });
    }


    public void setOnTabChangedListner(OnTabChangedListner listner) {
        this.mListner = listner;
        isInit();
    }

    public AlphaTabView getCurrentItemView() {
        isInit();
        return mTabViews.get(mCurrentItem);
    }

    public AlphaTabView getTabView(int tabIndex) {
        isInit();
        return mTabViews.get(tabIndex);
    }

    public void removeAllBadge() {
        isInit();
        for (AlphaTabView alphaTabView : mTabViews) {
            alphaTabView.removeShow();
        }
    }

    public void setCurrentItem(int currentItem){
        isInit();
        resetState();
        mCurrentItem = currentItem;
        mTabViews.get(mCurrentItem).setIconAlpha(1.0f);
    }
    public void setTabCurrenItem(int tabIndex) {
        if (tabIndex < mChildCounts && tabIndex > -1) {
            mTabViews.get(tabIndex).performClick();
        } else {
            throw new IllegalArgumentException("IndexOutOfBoundsException");
        }

    }

    private void isInit() {
        if (!ISINIT) {
            init();
        }
    }

    private void init() {
        ISINIT = true;
        mTabViews = new ArrayList<>();
        mChildCounts = getChildCount();


        for (int i = 0; i < mChildCounts; i++) {
            if (getChildAt(i) instanceof AlphaTabView) {
                AlphaTabView tabView = (AlphaTabView) getChildAt(i);
                mTabViews.add(tabView);
                //设置点击监听
                tabView.setOnClickListener(new MyOnClickListener(i));
            } else {
                throw new IllegalArgumentException("TabIndicator的子View必须是TabView");
            }
        }

        mTabViews.get(mCurrentItem).setIconAlpha(1.0f);
    }


    private class MyOnClickListener implements OnClickListener {

        private int currentIndex;

        public MyOnClickListener(int i) {
            this.currentIndex = i;
        }

        @Override
        public void onClick(View v) {
            //点击前先重置所有按钮的状态
            resetState();
            mTabViews.get(currentIndex).setIconAlpha(1.0f);
            if (null != mListner) {
                mListner.onTabSelected(currentIndex);
            }
            //点击是保存当前按钮索引
            mCurrentItem = currentIndex;
        }
    }

    /**
     * 重置所有按钮的状态
     */
    private void resetState() {
        for (int i = 0; i < mChildCounts; i++) {
            mTabViews.get(i).setIconAlpha(0);
        }
    }

    private static final String STATE_INSTANCE = "instance_state";
    private static final String STATE_ITEM = "state_item";

    /**
     * @return 当View被销毁的时候，保存数据
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_ITEM, mCurrentItem);
        return bundle;
    }

    /**
     * @param state 用于恢复数据使用
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentItem = bundle.getInt(STATE_ITEM);
            if (null == mTabViews || mTabViews.isEmpty()) {
                super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
                return;
            }
            //重置所有按钮状态
            resetState();
            //恢复点击的条目颜色
            mTabViews.get(mCurrentItem).setIconAlpha(1.0f);
            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
