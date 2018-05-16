package com.zhang.okinglawenforcementphone.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by Administrator on 2018/4/24/024.
 */

public class MyRecycelerView extends RecyclerView{


    public MyRecycelerView(android.content.Context context, android.util.AttributeSet attrs){
        super(context, attrs);
    }
    public MyRecycelerView(android.content.Context context){
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求所有父控件及祖宗控件不要拦截事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
