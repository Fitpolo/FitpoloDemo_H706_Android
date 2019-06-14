package com.fitpolo.support.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Date 2019/4/12
 * @Author wenzheng.liu
 * @Description 运动数据
 * @ClassPath com.fitpolo.support.entity.SportData
 */
public class SportData implements Comparable<SportData> {
    public int sportMode;// 运动模式
    public String startTime;// 运动开始时间，yyyy-MM-dd HH:mm
    public String sportCount;// 运动步数
    public String duration;// 运动时间 min
    public String distance;// 运动距离 km
    public String calories;// 运动卡路里
    public String speed;// 配速 /km


    @Override
    public String toString() {
        return "SportData{" +
                "sportMode=" + sportMode +
                ", startTime='" + startTime + '\'' +
                ", sportCount='" + sportCount + '\'' +
                ", duration='" + duration + '\'' +
                ", distance='" + distance + '\'' +
                ", calories='" + calories + '\'' +
                ", speed='" + speed + '\'' +
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
    public int compareTo(SportData another) {
        Calendar calendar = strDate2Calendar(startTime, "yyyy-yyyy-MM-dd HH:mm-dd");
        Calendar anotherCalendar = strDate2Calendar(another.startTime, "yyyy-MM-dd HH:mm-MM-dd");
        if (calendar.getTime().getTime() > anotherCalendar.getTime().getTime()) {
            return 1;
        }
        if (calendar.getTime().getTime() < anotherCalendar.getTime().getTime()) {
            return -1;
        }
        return 0;
    }
}
