package com.yiyanf.fang.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.widget.wheel.adapter.AbstractWheelTextAdapter;
import com.yiyanf.fang.ui.widget.wheel.views.OnWheelScrollListener;
import com.yiyanf.fang.ui.widget.wheel.views.WheelView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 时间选择器    年-月-日 时：分
 *
 * Created by Hition on 2017/10/16.
 */

public class ChooseTimeDialog extends Dialog implements View.OnClickListener {


    @Bind(R.id.wh_year)
    WheelView whYear;
    @Bind(R.id.wh_month)
    WheelView whMonth;
    @Bind(R.id.wh_day)
    WheelView whDay;
    @Bind(R.id.wh_hour)
    WheelView whHour;
    @Bind(R.id.wh_minute)
    WheelView whMinute;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.btn_ok)
    Button btnOk;


    private List<String> arry_years = new ArrayList<>();
    private List<String> arry_months = new ArrayList<>();
    private List<String> arry_days = new ArrayList<>();
    private List<String> arry_hour = new ArrayList<>();
    private List<String> arry_minute = new ArrayList<>();

    private CalendarTextAdapter mYearAdapter;
    private CalendarTextAdapter mMonthAdapter;
    private CalendarTextAdapter mDaydapter;
    private CalendarTextAdapter mHourAdapter;
    private CalendarTextAdapter mMinutedapter;
    private Context context;

    private int day;
    private int currentYear = getYear();
    private int currentMonth = 1;
    private int currentDay = 1;
    private int currentHour = 1;
    private int currentMinu = 1;

    private int maxTextSize = 24;
    private int minTextSize = 14;


    private String selectYear;
    private String selectMonth;
    private String selectDay;
    private String selectHour;
    private String selectMinu;

    private OnWheelOKListener okListener;

    public ChooseTimeDialog(@NonNull Context context) {
        super(context, R.style.TimeDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time_choose);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
//        initYears();

        arry_years.clear();
        for (int i = currentYear; i <currentYear+2 ; i++) {
            arry_years.add(String.valueOf(i)+"年");
        }
        mYearAdapter = new CalendarTextAdapter(context, arry_years, setYear(currentYear), maxTextSize, minTextSize);
        whYear.setVisibleItems(2);
        whYear.setViewAdapter(mYearAdapter);

//        initMonths(month);
        for (int i = 1; i <=12 ; i++) {
            if(i<10){
                arry_months.add("0"+i+"月");
            }else{
                arry_months.add(i+"月");
            }
        }
        currentMonth = getMonth();
        mMonthAdapter = new CalendarTextAdapter(context, arry_months, setMonth(currentMonth), maxTextSize, minTextSize);
        whMonth.setVisibleItems(5);
        whMonth.setViewAdapter(mMonthAdapter);
        whMonth.setCurrentItem(setMonth(currentMonth));

        initDays(day);
        currentDay = getDay();
        mDaydapter = new CalendarTextAdapter(context, arry_days, currentDay-1, maxTextSize, minTextSize);
        whDay.setVisibleItems(5);
        whDay.setViewAdapter(mDaydapter);
        whDay.setCurrentItem(currentDay-1);

        // init hour
        arry_hour.clear();
        for (int i = 0; i <= 23; i++) {
            if(i<10){
                arry_hour.add("0"+i+"时");
            }else{
                arry_hour.add(i+"时");
            }

        }
        currentHour = getHour();
        mHourAdapter = new CalendarTextAdapter(context, arry_hour, currentHour, maxTextSize, minTextSize);
        whHour.setVisibleItems(5);
        whHour.setViewAdapter(mHourAdapter);
        whHour.setCurrentItem(currentHour);

        // init minute
        arry_minute.clear();
        for (int i = 0; i < 60; i++) {
            if(i<10){
                arry_minute.add("0"+i+"分");
            }else{
                arry_minute.add(i+"分");
            }

        }
        currentMinu = geMinute();
        mMinutedapter = new CalendarTextAdapter(context, arry_minute, currentMinu, maxTextSize, minTextSize);
        whMinute.setVisibleItems(5);
        whMinute.setViewAdapter(mMinutedapter);
        whMinute.setCurrentItem(currentMinu);

        /*whYear.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                selectYear = currentText;
                setTextviewSize(currentText, mYearAdapter);
                currentYear = Integer.parseInt(currentText);
                setYear(currentYear);
                initMonths(month);
                mMonthAdapter = new CalendarTextAdapter(context, arry_months, 0, maxTextSize, minTextSize);
                whMonth.setVisibleItems(5);
                whMonth.setViewAdapter(mMonthAdapter);
                whMonth.setCurrentItem(0);
            }
        });*/
        whYear.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mYearAdapter);
            }
        });

        /*whMonth.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                selectMonth = currentText;
                setTextviewSize(currentText, mMonthAdapter);
                setMonth(Integer.parseInt(currentText));
                initDays(day);
                mDaydapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
                whDay.setVisibleItems(5);
                whDay.setViewAdapter(mDaydapter);
                whDay.setCurrentItem(0);
            }
        });*/
        whMonth.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMonthAdapter);
            }
        });

        /*whDay.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mDaydapter);
                selectDay = currentText;
            }
        });*/

        whDay.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mDaydapter);
            }
        });


        whHour.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mHourAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mHourAdapter);
            }
        });

        whMinute.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mMinutedapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMinutedapter);
            }
        });

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    public void setOnWheelOKListener(OnWheelOKListener okListener) {
        this.okListener = okListener;
    }

    public interface OnWheelOKListener {
        void onClick(String year, String month, String day,String hour,String minute);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_ok:
                if(null != okListener){
                    selectYear = mYearAdapter.getItemText(whYear.getCurrentItem()).toString();
                    selectMonth = mMonthAdapter.getItemText(whMonth.getCurrentItem()).toString();
                    selectDay = mDaydapter.getItemText(whDay.getCurrentItem()).toString();
                    selectHour = mHourAdapter.getItemText(whHour.getCurrentItem()).toString();
                    selectMinu = mMinutedapter.getItemText(whMinute.getCurrentItem()).toString();

                    okListener.onClick(selectYear.replace("年","-"), selectMonth.replace("月","-"), selectDay.replace("日"," "),selectHour.replace("时",":"),selectMinu.replace("分",""));
                    dismiss();
                }
                break;

            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }



    private void initYears() {
        for (int i = getYear(); i < 2; i++) {
            arry_years.add(String.valueOf(i)+"年");
        }
    }

    private void initMonths(int months) {
        arry_months.clear();
        for (int i = 1; i <= months; i++) {
            arry_months.add(String.valueOf(i)+"月");
        }
    }

    private void initDays(int days) {
        arry_days.clear();
        for (int i = 1; i <= days; i++) {
            if(i<10){
                arry_days.add("0"+i+"日");
            }else{
                arry_days.add(i+"日");
            }

        }
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        protected CalendarTextAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_wheelview, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            return super.getItem(index, cachedView, parent);
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    private int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    private int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    private int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    private int getHour() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.HOUR_OF_DAY);
    }

    private int geMinute() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MINUTE);
    }

    /**
     * 设置年份
     *
     * @param year
     */
    public int setYear(int year) {
        int yearIndex = 0;
        /*if (year != getYear()) {
            this.month = 12;
        } else {
            this.month = getMonth();
        }*/

        for (int i = getYear(); i <getYear()+2; i++) {
            if (i == year) {
                break ;
            }
            yearIndex++;
        }
        return yearIndex;
    }

    /**
     * 设置月份
     *
     * @param month
     * @return
     */
    public int setMonth(int month) {
        int monthIndex = 0;
        calDays(currentYear, month);
        for (int i = 1; i <= 12; i++) {
            if (month == i) {
                break ;
            }
            monthIndex++;
        }
        return monthIndex;
    }


    /**
     * 计算每月多少天
     *
     * @param month
     * @param year
     */
    public void calDays(int year, int month) {
        boolean leayyear;
        if (year % 4 == 0 && year % 100 != 0) {
            leayyear = true;
        } else {
            leayyear = false;
        }
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    this.day = 31;
                    break;
                case 2:
                    if (leayyear) {
                        this.day = 29;
                    } else {
                        this.day = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    this.day = 30;
                    break;
                default:
                    break;
            }
        }
        /*if (year == getYear() && month == getMonth()) {
            this.day = getDay();
        }*/
    }

    public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
        List<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }



}
