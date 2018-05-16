package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 统计查询
 */
public class StatisticalActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.calendar_title)
    TextView calendarTitle;
    @BindView(R.id.materialcalendarview2)
    CompactCalendarView materialcalendarview;
    private Unbinder mBind;
    private SimpleDateFormat mDateFormat =  new SimpleDateFormat("yyyy年MM月");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        mBind = ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        materialcalendarview.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                calendarTitle.setText(mDateFormat.format(firstDayOfNewMonth));
            }
        });
    }

    private void initData() {
        materialcalendarview.setFirstDayOfWeek(Calendar.SUNDAY);
        materialcalendarview.setLocale(TimeZone.getDefault(), Locale.getDefault());
        materialcalendarview.setUseThreeLetterAbbreviation(true);
        Date date = new Date();
        materialcalendarview.setCurrentDate(date);
        calendarTitle.setText(mDateFormat.format(date));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDateFormat=null;
        mBind.unbind();
    }
}
