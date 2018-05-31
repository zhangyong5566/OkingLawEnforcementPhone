package com.zhang.okinglawenforcementphone.mvp.ui.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.animation.EasingFunction;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.zhang.baselib.BaseApplication;
import com.zhang.okinglawenforcementphone.GreenDAOManager;
import com.zhang.okinglawenforcementphone.R;
import com.zhang.okinglawenforcementphone.adapter.StatisRcyAdapter;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTask;
import com.zhang.okinglawenforcementphone.beans.GreenMissionTaskDao;
import com.zhang.okinglawenforcementphone.beans.OkingContract;
import com.zhang.okinglawenforcementphone.mvp.ui.base.BaseActivity;
import com.zhang.okinglawenforcementphone.utils.DialogUtil;
import com.zhang.okinglawenforcementphone.views.DividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    @BindView(R.id.chart)
    PieChart mChart;
    private Unbinder mBind;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy年MM月");
    private SimpleDateFormat mDateFormatForDay = new SimpleDateFormat("yyyy年MM月dd日");
    private String mSelectMonth;
    private List<GreenMissionTask> mGreenMissionTasks;
    private ArrayList<GreenMissionTask>mCompleteMissionTask = new ArrayList<>();
    private ArrayList<GreenMissionTask>mUnfinishedMissionTask = new ArrayList<>();
    private String mSelectDate;
    private DialogUtil mDialogUtil;
    private StatisRcyAdapter mStatisRcyAdapter;
    private View mStatisView;
    private Intent intent;
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
                mSelectDate = mDateFormatForDay.format(dateClicked);
                mChart.setCenterText(mSelectDate);
                getMissionTaskPie(-1);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mSelectMonth = mDateFormat.format(firstDayOfNewMonth);
                calendarTitle.setText(mSelectMonth);
                mChart.setCenterText(mSelectMonth);
                getMissionTaskPie(0);
            }
        });
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                List<GreenMissionTask> greenMissionTasks = (List<GreenMissionTask>) e.getData();
                if (mDialogUtil ==null){
                    mDialogUtil = new DialogUtil();
                    mStatisView = View.inflate(BaseApplication.getApplictaion(), R.layout.statistical_dialog, null);
                    RecyclerView rcyStatis = mStatisView.findViewById(R.id.rcy_statis);
                    rcyStatis.setLayoutManager(new LinearLayoutManager(BaseApplication.getApplictaion(), LinearLayoutManager.VERTICAL, false));
                    rcyStatis.addItemDecoration(new DividerItemDecoration(BaseApplication.getApplictaion(), 0, 10, getResources().getColor(R.color.activity_bg)));
                    mStatisRcyAdapter = new StatisRcyAdapter(R.layout.statistical_dialog_item,greenMissionTasks);
                    mStatisRcyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
                    rcyStatis.setAdapter(mStatisRcyAdapter);
                    mStatisRcyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            List<GreenMissionTask> data = adapter.getData();
                            GreenMissionTask greenMissionTask = data.get(position);
                            switch (greenMissionTask.getStatus()){
                                case "0":

                                case "1":

                                case "2":
                                    intent = new Intent(StatisticalActivity.this, ArrangeTeamMembersActivity.class);
                                    intent.putExtra("id", greenMissionTask.getId());
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                    break;
                                case "3":

                                case "4":
                                case "100":
                                    intent = new Intent(StatisticalActivity.this, MissionActivity.class);
                                    intent.putExtra("id", greenMissionTask.getId());
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                    break;
                                case "5":
                                    intent = new Intent(StatisticalActivity.this, MissionRecorActivity.class);
                                    intent.putExtra("id", greenMissionTask.getId());
                                    intent.putExtra("taskId", greenMissionTask.getTaskid());
                                    startActivity(intent);
                                    break;
                                case "9":
                                    intent = new Intent(StatisticalActivity.this, MissionActivity.class);
                                    intent.putExtra("id", greenMissionTask.getId());
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                    break;
                                default:
                                    break;

                            }
                        }
                    });
                }else {
                    mStatisRcyAdapter.setNewData(greenMissionTasks);
                }
                mDialogUtil.showBottomDialog(StatisticalActivity.this,mStatisView,400f);
            }


            @Override
            public void onNothingSelected() {

            }
        });

    }

    private void initData() {
        materialcalendarview.setFirstDayOfWeek(Calendar.SUNDAY);
        materialcalendarview.setLocale(TimeZone.getDefault(), Locale.getDefault());
        materialcalendarview.setUseThreeLetterAbbreviation(true);
        Date date = new Date();
        materialcalendarview.setCurrentDate(date);
        mSelectMonth = mDateFormat.format(date);
        calendarTitle.setText(mSelectMonth);


        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setCenterTextSize(16f);
        mChart.setCenterText(mSelectMonth);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        moveOffScreen();

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setMaxAngle(180f); // HALF CHART
        mChart.setRotationAngle(180f);
        mChart.setCenterTextOffset(0, -20);
        mGreenMissionTasks = GreenDAOManager.getInstence().getDaoSession().getGreenMissionTaskDao().queryBuilder()
                .where(GreenMissionTaskDao.Properties.Userid.eq(OkingContract.CURRENTUSER.getUserid())).list();
        getMissionTaskPie(0);



        mChart.animateY(1400, EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);

    }

    private void getMissionTaskPie(int type) {
        mCompleteMissionTask.clear();
        mUnfinishedMissionTask.clear();

        for (GreenMissionTask greenMissionTask : mGreenMissionTasks) {
            Log.i("GG",greenMissionTask.getEnd_time()+"");
            Long end_time = greenMissionTask.getEnd_time();
            if (type==0){

               String yearMonth = mDateFormat.format(end_time);
                if (mSelectMonth.equals(yearMonth)) {
                    if (greenMissionTask.getStatus().equals("5") || greenMissionTask.getStatus().equals("100")) {
                        mCompleteMissionTask.add(greenMissionTask);
                    } else {
                        mUnfinishedMissionTask.add(greenMissionTask);
                    }

                }

            }else {
                String yearDay = mDateFormatForDay.format(end_time);
                if (mSelectDate.equals(yearDay)) {
                    if (greenMissionTask.getStatus().equals("5") || greenMissionTask.getStatus().equals("100")) {
                        mCompleteMissionTask.add(greenMissionTask);
                    } else {
                        mUnfinishedMissionTask.add(greenMissionTask);
                    }

                }
            }
        }
        setData(mCompleteMissionTask, mUnfinishedMissionTask);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDateFormat = null;
        mBind.unbind();
    }

    private void setData(ArrayList<GreenMissionTask> completeMissionTask, ArrayList<GreenMissionTask> unfinishedMissionTask) {

        ArrayList<PieEntry> values = new ArrayList<PieEntry>();

        PieEntry completePieEntry = new PieEntry(completeMissionTask.size(), "完成" + completeMissionTask.size());
        completePieEntry.setData(completeMissionTask);
        values.add(completePieEntry);
        PieEntry unfinishedPieEntry = new PieEntry(unfinishedMissionTask.size(), "未完成" + unfinishedMissionTask.size());
        unfinishedPieEntry.setData(unfinishedMissionTask);
        values.add(unfinishedPieEntry);

        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        mChart.invalidate();
    }


    public static final EasingFunction EaseInOutQuad = new EasingFunction() {
        public float getInterpolation(float input) {
            input *= 2f;

            if (input < 1f) {
                return 0.5f * input * input;
            }

            return -0.5f * ((--input) * (input - 2f) - 1f);
        }
    };

    private void moveOffScreen() {

        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();  // deprecated

        int offset = (int) (height * 0.30); /* percent to move */

        RelativeLayout.LayoutParams rlParams =
                (RelativeLayout.LayoutParams) mChart.getLayoutParams();
        rlParams.setMargins(0, 0, 0, -offset);
        mChart.setLayoutParams(rlParams);
    }

}
