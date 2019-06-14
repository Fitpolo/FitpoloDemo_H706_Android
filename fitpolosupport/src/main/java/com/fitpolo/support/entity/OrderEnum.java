package com.fitpolo.support.entity;

import java.io.Serializable;

/**
 * @Date 2017/5/11
 * @Author wenzheng.liu
 * @Description 命令枚举
 * @ClassPath com.fitpolo.support.entity.OrderEnum
 */
public enum OrderEnum implements Serializable {
    READ_NOTIFY("打开读取通知", 0),
    WRITE_NOTIFY("打开设置通知", 0),
    STEP_NOTIFY("打开记步通知", 0),
    HEART_RATE_NOTIFY("打开心率通知", 0),

    Z_READ_ALARMS("读取闹钟", 0x01),
    Z_READ_SIT_ALERT("读取久坐提醒", 0x04),
    Z_READ_STEP_TARGET("读取记步目标", 0x06),
    Z_READ_UNIT_TYPE("读取单位类型", 0x07),
    Z_READ_TIME_FORMAT("读取时间格式", 0x08),
    Z_READ_LAST_SCREEN("读取显示上次屏幕", 0x0A),
    Z_READ_HEART_RATE_INTERVAL("读取心率间隔", 0x0B),
    Z_READ_AUTO_LIGHTEN("读取翻腕亮屏", 0x0D),
    Z_READ_USER_INFO("读取个人信息", 0x0E),
    Z_READ_PARAMS("读取硬件参数", 0x10),
    Z_READ_VERSION("读取版本", 0x11),
    Z_READ_SLEEP_GENERAL("读取睡眠概况", 0x12),
    Z_READ_SLEEP_DETAIL("读取睡眠详情", 0x14),
    Z_READ_LAST_CHARGE_TIME("读取上次充电时间", 0x18),
    Z_READ_BATTERY("读取电量", 0x19),
    Z_READ_DIAL("读取表盘", 0x0F),
    Z_READ_SHAKE_STRENGTH("读取震动强度", 0x1E),
    Z_READ_DATE_FORMAT("读取日期制式", 0x1D),
    Z_READ_CUSTOM_SORT_SCREEN("读取自定义屏幕排序", 0x1F),
    Z_READ_NODISTURB("读取勿扰模式", 0x0C),
    Z_READ_SPORTS("读取运动数据", 0x16),
    Z_WRITE_FIND_PHONE("打开寻找手机", 0x16),
    Z_WRITE_LANGUAGE("设置手环语言", 0x1B),

    Z_WRITE_ALARMS("设置闹钟", 0x01),
    Z_WRITE_SIT_ALERT("设置久坐提醒", 0x04),
    Z_WRITE_STEP_TARGET("设置记步目标", 0x06),
    Z_WRITE_UNIT_TYPE("设置单位类型", 0x07),
    Z_WRITE_TIME_FORMAT("设置时间格式", 0x08),
    Z_WRITE_LAST_SCREEN("设置显示上次屏幕", 0x0A),
    Z_WRITE_HEART_RATE_INTERVAL("设置心率间隔", 0x0B),
    Z_WRITE_AUTO_LIGHTEN("设置翻腕亮屏", 0x0D),
    Z_WRITE_USER_INFO("设置个人信息", 0x0E),
    Z_WRITE_SYSTEM_TIME("设置系统时间", 0x0F),
    Z_WRITE_SHAKE("设置手环震动", 0x13),
    Z_WRITE_NOTIFY("设置通知", 0x14),
    Z_WRITE_COMMON_NOTIFY("设置通用通知", 0x1F),
    Z_WRITE_DIAL("设置表盘", 0x10),
    Z_WRITE_SHAKE_STRENGTH("设置震动强度", 0x1D),
    Z_WRITE_DATE_FORMAT("设置日期制式", 0x1C),
    Z_WRITE_CUSTOM_SORT_SCREEN("设置自定义屏幕排序", 0x1E),
    Z_WRITE_NODISTURB("设置勿扰模式", 0x0C),
    Z_WRITE_STEP_INTERVAL("设置记步间隔", 0x21),

    Z_READ_STEPS("读取记步", 0x01),
    Z_STEPS_CHANGES_LISTENER("监听记步", 0x03),
    Z_READ_STEP_INTERVAL("读取记步间隔", 0x05),
    Z_READ_HEART_RATE("读取心率", 0x01),
    Z_READ_SPORTS_HEART_RATE("读取运动心率", 0x04),

    RESET_DATA("恢复出厂设置", 0x0A),
    Z_WRITE_CLOSE("关机", 0x12),
    Z_WRITE_RESET("恢复出厂设置", 0x20),
    ;


    private String orderName;
    private int orderHeader;

    OrderEnum(String orderName, int orderHeader) {
        this.orderName = orderName;
        this.orderHeader = orderHeader;
    }

    public int getOrderHeader() {
        return orderHeader;
    }

    public String getOrderName() {
        return orderName;
    }
}
