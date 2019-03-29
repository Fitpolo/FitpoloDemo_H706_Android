package com.fitpolo.support.task;

import com.fitpolo.support.MokoConstants;
import com.fitpolo.support.MokoSupport;
import com.fitpolo.support.callback.MokoOrderTaskCallback;
import com.fitpolo.support.entity.OrderEnum;
import com.fitpolo.support.entity.OrderType;
import com.fitpolo.support.log.LogModule;
import com.fitpolo.support.utils.DigitalConver;

/**
 * @Date 2019/3/18
 * @Author wenzheng.liu
 * @Description 设置通用通知
 * @ClassPath com.fitpolo.support.task.ZWriteCommonMessageTask
 */
public class ZWriteCommonMessageTask extends OrderTask {

    private byte[] orderData;
    private String unicodeText;
    private int dataCount;
    private int dataIndex;
    private int lastDataLength;
    private boolean isPhoneCall;
    private boolean isOpen;


    public ZWriteCommonMessageTask(MokoOrderTaskCallback callback, boolean isPhoneCall, String showText, boolean isOpen) {
        super(OrderType.WRITE_CHARACTER, OrderEnum.Z_WRITE_COMMON_NOTIFY, callback, OrderTask.RESPONSE_TYPE_WRITE_NO_RESPONSE);
        if (showText.length() != 0) {
            if (showText.length() > 30) {
                // 只取30个字
                String simpleText = showText.substring(0, 30);
                unicodeText = cnToUnicode(simpleText);
            } else {
                unicodeText = cnToUnicode(showText);
            }
            int unicodeTextLength = unicodeText.length();
            dataCount = unicodeTextLength / 24;
            lastDataLength = unicodeTextLength % 24;
            if (lastDataLength > 0) {
                dataCount++;
            }
            if (lastDataLength == 0) {
                lastDataLength = 24;
            }
        } else {
            unicodeText = "";
            dataCount = 1;
            lastDataLength = 0;
        }
        LogModule.w("总包数：" + dataCount);
        dataIndex = 1;
        this.isOpen = isOpen;
        this.isPhoneCall = isPhoneCall;
    }

    @Override
    public byte[] assemble() {
        LogModule.w("发送包序号：" + dataIndex);
        orderData = new byte[dataIndex < dataCount ? 20 : (8 + lastDataLength / 2)];
        orderData[0] = (byte) MokoConstants.HEADER_WRITE_SEND;
        orderData[1] = (byte) order.getOrderHeader();
        orderData[2] = (byte) (dataIndex < dataCount ? 0x11 : (5 + lastDataLength / 2));
        orderData[3] = (byte) (isOpen ? 0x02 : 0x00);
        orderData[4] = (byte) (isPhoneCall ? 0x00 : 0x01);
        orderData[5] = (byte) dataCount;
        orderData[6] = (byte) dataIndex;
        orderData[7] = (byte) (dataIndex < dataCount ? 0x0C : lastDataLength / 2);
        if (unicodeText.length() > 0) {
            String hexText = unicodeText.substring((dataIndex - 1) * 24, dataIndex < dataCount ? dataIndex * 24 : ((dataIndex - 1) * 24 + lastDataLength));
            byte[] showTextBytes = DigitalConver.hex2bytes(hexText);
            System.arraycopy(showTextBytes, 0, orderData, 8, showTextBytes.length);
        }
        return orderData;
    }

    @Override
    public void parseValue(byte[] value) {
        if (order.getOrderHeader() != DigitalConver.byte2Int(value[1])) {
            return;
        }
        if (0x03 != DigitalConver.byte2Int(value[2])) {
            return;
        }
        if (dataCount != DigitalConver.byte2Int(value[3])) {
            return;
        }
        if (0x00 != DigitalConver.byte2Int(value[5])) {
            return;
        }
        dataIndex = DigitalConver.byte2Int(value[4]);
        LogModule.w("接收包序号：" + dataIndex);
        orderStatus = OrderTask.ORDER_STATUS_SUCCESS;
        if (dataIndex == dataCount) {
            LogModule.i(order.getOrderName() + "成功");
            MokoSupport.getInstance().pollTask();
            callback.onOrderResult(response);
            MokoSupport.getInstance().executeTask(callback);
        } else {
            dataIndex++;
            MokoSupport.getInstance().executeTask(callback);
        }
    }

    private String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        StringBuffer returnStr = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            String hexB = Integer.toHexString(chars[i]);
            if (hexB.length() == 1) {
                hexB = "000" + hexB;
            } else if (hexB.length() == 2) {
                hexB = "00" + hexB;
            } else if (hexB.length() == 3) {
                hexB = "0" + hexB;
            }
            if (!"000a".equals(hexB)) {
                // 需要转换
                String hexB1 = hexB.substring(0, 2);
                String hexB2 = hexB.substring(2, 4);
                String hex = hexB2 + hexB1;
                returnStr.append(hex);
            } else {
                returnStr.append(hexB);
            }
        }
        return returnStr.toString();
    }
}
