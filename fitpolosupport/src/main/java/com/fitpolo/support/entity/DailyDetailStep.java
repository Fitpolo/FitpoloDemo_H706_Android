package com.fitpolo.support.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Date 2017/5/15
 * @Author wenzheng.liu
 * @Description 记步间隔数据
 * @ClassPath com.fitpolo.support.entity.DailyDetailStep
 */
public class DailyDetailStep implements Comparable<DailyDetailStep> {
    public String date;// 时间，yyyy-MM-dd HH:mm
    public String count;// 步数

    @Override
    public String toString() {
        return "DailyDetailStep{" +
                "date='" + date + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    public Calendar strDate2Calendar(String strDate, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int compareTo(DailyDetailStep another) {
        Calendar calendar = strDate2Calendar(date, "yyyy-MM-dd HH:mm");
        Calendar anotherCalendar = strDate2Calendar(another.date, "yyyy-MM-dd HH:mm");
        if (calendar.getTime().getTime() > anotherCalendar.getTime().getTime()) {
            return 1;
        }
        if (calendar.getTime().getTime() < anotherCalendar.getTime().getTime()) {
            return -1;
        }
        return 0;
    }
}
