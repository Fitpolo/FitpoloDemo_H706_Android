package com.fitpolo.support.task;

import com.fitpolo.support.MokoConstants;
import com.fitpolo.support.MokoSupport;
import com.fitpolo.support.callback.MokoOrderTaskCallback;
import com.fitpolo.support.entity.DailyDetailStep;
import com.fitpolo.support.entity.OrderEnum;
import com.fitpolo.support.entity.OrderType;
import com.fitpolo.support.log.LogModule;
import com.fitpolo.support.utils.ComplexDataParse;
import com.fitpolo.support.utils.DigitalConver;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @Date 2017/5/11
 * @Author wenzheng.liu
 * @Description 读取记步间隔
 * @ClassPath com.fitpolo.support.task.ZReadStepIntervalTask
 */
public class ZReadStepIntervalTask extends OrderTask {
    private static final int ORDERDATA_LENGTH = 8;
    private static final int HEADER_STEP_COUNT = 0x05;
    private static final int HEADER_STEP = 0x06;

    private byte[] orderData;

    private int stepDetailCount;
    private ArrayList<DailyDetailStep> dailyDetailSteps;

    private boolean isCountSuccess;
    private boolean isReceiveDetail;

    public ZReadStepIntervalTask(MokoOrderTaskCallback callback, Calendar lastSyncTime) {
        super(OrderType.STEP_CHARACTER, OrderEnum.Z_READ_STEP_INTERVAL, callback, OrderTask.RESPONSE_TYPE_WRITE_NO_RESPONSE);

        int year = lastSyncTime.get(Calendar.YEAR) - 2000;
        int month = lastSyncTime.get(Calendar.MONTH) + 1;
        int day = lastSyncTime.get(Calendar.DAY_OF_MONTH);

        int hour = lastSyncTime.get(Calendar.HOUR_OF_DAY);
        int minute = lastSyncTime.get(Calendar.MINUTE);

        orderData = new byte[ORDERDATA_LENGTH];
        orderData[0] = (byte) MokoConstants.HEADER_SETP_SEND;
        orderData[1] = (byte) order.getOrderHeader();
        orderData[2] = (byte) 0x05;
        orderData[3] = (byte) year;
        orderData[4] = (byte) month;
        orderData[5] = (byte) day;
        orderData[6] = (byte) hour;
        orderData[7] = (byte) minute;
    }

    @Override
    public byte[] assemble() {
        return orderData;
    }

    @Override
    public void parseValue(byte[] value) {
        int header = DigitalConver.byte2Int(value[1]);
        int data_length = DigitalConver.byte2Int(value[2]);
        LogModule.i(order.getOrderName() + "成功");
        switch (header) {
            case HEADER_STEP_COUNT:
                if (data_length != 2) {
                    return;
                }
                isCountSuccess = true;
                byte[] count = new byte[2];
                System.arraycopy(value, 3, count, 0, 2);
                stepDetailCount = DigitalConver.byteArr2Int(count);
                MokoSupport.getInstance().setDailyStepCount(stepDetailCount);
                LogModule.i("有" + stepDetailCount + "条记步间隔数据");
                MokoSupport.getInstance().initDetailStepsList();
                dailyDetailSteps = MokoSupport.getInstance().getDailyDetailSteps();
                delayTime = stepDetailCount == 0 ? DEFAULT_DELAY_TIME : DEFAULT_DELAY_TIME + 100 * stepDetailCount;
                // 拿到条数后再启动超时任务
                MokoSupport.getInstance().timeoutHandler(this);
                break;
            case HEADER_STEP:
                if (data_length != 14) {
                    return;
                }
                if (stepDetailCount > 0) {
                    if (dailyDetailSteps == null) {
                        dailyDetailSteps = new ArrayList<>();
                    }
                    ComplexDataParse.parseDailyDetailStep(value, dailyDetailSteps);
                    stepDetailCount--;
                    MokoSupport.getInstance().setDailyDetailSteps(dailyDetailSteps);
                    MokoSupport.getInstance().setDailyDetailStepCount(stepDetailCount);
                    if (stepDetailCount > 0) {
                        LogModule.i("还有" + stepDetailCount + "条记步间隔数据未同步");
                        return;
                    }
                }
                break;
            default:
                return;
        }
        if (stepDetailCount != 0) {
            return;
        }
        MokoSupport.getInstance().setDailyDetailStepCount(stepDetailCount);
        MokoSupport.getInstance().setDailyDetailSteps(dailyDetailSteps);
        orderStatus = OrderTask.ORDER_STATUS_SUCCESS;
        MokoSupport.getInstance().pollTask();
        callback.onOrderResult(response);
        MokoSupport.getInstance().executeTask(callback);
    }

    @Override
    public boolean timeoutPreTask() {
        if (!isReceiveDetail) {
            if (!isCountSuccess) {
                LogModule.i(order.getOrderName() + "个数超时");
            } else {
                isReceiveDetail = true;
                return false;
            }
        }
        return super.timeoutPreTask();
    }
}
