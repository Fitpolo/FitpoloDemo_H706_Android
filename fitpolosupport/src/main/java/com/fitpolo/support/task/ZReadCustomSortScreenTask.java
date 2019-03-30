package com.fitpolo.support.task;

import com.fitpolo.support.MokoConstants;
import com.fitpolo.support.MokoSupport;
import com.fitpolo.support.callback.MokoOrderTaskCallback;
import com.fitpolo.support.entity.OrderEnum;
import com.fitpolo.support.entity.OrderType;
import com.fitpolo.support.log.LogModule;
import com.fitpolo.support.utils.DigitalConver;

import java.util.ArrayList;

/**
 * @Date 2019/3/1
 * @Author wenzheng.liu
 * @Description 读取自定义屏幕排序
 * @ClassPath com.fitpolo.support.task.ZReadDateFormatTask
 */
public class ZReadCustomSortScreenTask extends OrderTask {
    private static final int ORDERDATA_LENGTH = 3;

    private byte[] orderData;

    public ZReadCustomSortScreenTask(MokoOrderTaskCallback callback) {
        super(OrderType.READ_CHARACTER, OrderEnum.Z_READ_CUSTOM_SORT_SCREEN, callback, OrderTask.RESPONSE_TYPE_WRITE_NO_RESPONSE);

        orderData = new byte[ORDERDATA_LENGTH];
        orderData[0] = (byte) MokoConstants.HEADER_READ_SEND;
        orderData[1] = (byte) order.getOrderHeader();
        orderData[2] = (byte) 0x00;
    }

    @Override
    public byte[] assemble() {
        return orderData;
    }

    @Override
    public void parseValue(byte[] value) {
        if (order.getOrderHeader() != DigitalConver.byte2Int(value[1])) {
            return;
        }
        if (0x11 != DigitalConver.byte2Int(value[2])) {
            return;
        }
        int shownLength = DigitalConver.byte2Int(value[3]);
        LogModule.w("显示长度：" + shownLength);
        ArrayList<Integer> shownCodes = new ArrayList<>();
        for (int i = 0; i < shownLength; i++) {
            int code = DigitalConver.byte2Int(value[i + 4]);
            shownCodes.add(code);
        }
        MokoSupport.getInstance().setCustomSortScreen(shownCodes);

        LogModule.i(order.getOrderName() + "成功");
        orderStatus = OrderTask.ORDER_STATUS_SUCCESS;

        MokoSupport.getInstance().pollTask();
        callback.onOrderResult(response);
        MokoSupport.getInstance().executeTask(callback);
    }
}
