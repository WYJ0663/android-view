package com.ptwyj.calendar.view;

import android.util.Log;

import java.util.*;

/**
 * Created by WYJ on 2016/12/15.
 */
public class Month {

    public static final int NUMBER_OF_DAYS = 7;

    private int year;
    private int month;

    private int days;//有多少天

    private List<Integer> allDdays;//有多少天

    private int firstWeek;//第一天星期几.星期日为1

    private int lastMonthDays;//上个月

    private int nextMonthDays;//下个月

    public Month(int year, int month) {
        this.year = year;
        this.month = month;

        firstWeek = dayForWeek(year, month);
        days = getMonth(year, month);

        if (month == 1) {
            lastMonthDays = getMonth(year - 1, 12);
            nextMonthDays = getMonth(year, 2);
        } else if (month == 12) {
            lastMonthDays = getMonth(year, 11);
            nextMonthDays = getMonth(year + 1, 1);
        } else {
            lastMonthDays = getMonth(year, month - 1);
            nextMonthDays = getMonth(year, month + 1);
        }

        Log.i("debug", "lastMonthDays " + lastMonthDays);
        Log.i("debug", "nextMonthDays " + nextMonthDays);
        Log.i("debug", "days " + days);

        allDdays = allDays();
    }

    public List<Integer> getAllDdays() {
        return allDdays;
    }

    /**
     * 行数
     *
     * @return
     */
    public int getLineSize() {
        int i = days + firstWeek - 1;
        return i % 7 == 0 ? i / 7 : i / 7 + 1;
    }

    private List<Integer> lastDays() {
        List<Integer> list = new ArrayList<>();
        for (int i = firstWeek - 1; i > 0; i--) {
            list.add(lastMonthDays - i +1);
        }
        Log.i("debug", "lastDays " + list.size());
        return list;
    }

    private List<Integer> nextDays() {
        int all = getLineSize() * 7;
        int remain = all - days - firstWeek + 1;

        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= remain; i++) {
            list.add(i);
        }
        Log.i("debug", "nextDays " + list.size());
        return list;
    }

    //所有显示的日期
    private List<Integer> allDays() {
        List<Integer> list = new ArrayList<>();
        list.addAll(lastDays());
        for (int i = 1; i <= days; i++) {
            list.add(i);
        }
        list.addAll(nextDays());

        Log.i("debug", "allDays " + list.size());
        return list;
    }

    //是否是本月
    public boolean isThisMonth(int n) {
        if (n + 1 >= firstWeek && n + 1 <= firstWeek + days - 1) {
            return true;
        } else {
            return false;
        }
    }

    //是否闰年
    private boolean isLeapYear(int year) {

        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
            return true;
        } else {
            return false;
        }
    }

    //获取月数
    private int getMonth(int year, int month) {

        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        } else {
            return 30;
        }
    }

    /**
     * 判断当前日期是星期几
     */
    public static int dayForWeek(int year, int month) {
        Calendar calendar = new GregorianCalendar(year, month - 1, 1);
        int dayForWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Log.i("debug", "dayForWeek " + dayForWeek);
        return dayForWeek;
    }

}
