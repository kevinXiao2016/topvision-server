package com.topvision.ems.epon.onu.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.topvision.ems.epon.onu.constants.OnuConstants;
import com.topvision.ems.epon.onu.domain.OnuEventLogEntry;
import com.topvision.ems.epon.onu.domain.OnuOnOffRecord;

public class OnuOnOffRecordUtil {
    /**
     * 解析从设备获取的历史记录的对应字节
     * 
     * @param onuEventLogEntry
     * @return
     */
    public static List<OnuOnOffRecord> parseOnuEventLogList(OnuEventLogEntry onuEventLogEntry) {
        List<OnuOnOffRecord> onuOnOffRecords = new ArrayList<OnuOnOffRecord>();
        if (onuEventLogEntry.getTopOnuEventLogList() == null || "".equals(onuEventLogEntry.getTopOnuEventLogList()))
            return onuOnOffRecords;
        
        // topOnuEventLogList
        // 是变长字节，以12个为一组，最多八组(即96个字节)，对应最多八条上下线记录
        // 如：'AA:BB:CC:DD:EE:FF:GG:HH:II:JJ:KK:LL:MM:NN'
        String[] array = onuEventLogEntry.getTopOnuEventLogList().split(":");
        if (array.length == 0)
            return onuOnOffRecords;
        
        // 上下线记录条数
        int recordNums = array.length / OnuConstants.BYTE_LENGTH_BY_RECORD;
        // 每组12个字节，0-3表示上线时间，4-7表示下线时间，8-11表示下线原因
        for (int i = 0; i < recordNums; i++) {
            OnuOnOffRecord onuOnOffRecord = new OnuOnOffRecord();
            
            int startIndex = i * OnuConstants.BYTE_LENGTH_BY_RECORD;
            String[] onLineTimeArray = Arrays.copyOfRange(array, startIndex, startIndex + OnuConstants.BYTE_STEP);
            String onLineTimeHex = arrayToString(onLineTimeArray);
            //如果上线时间为00000000，说明还没有上线记录，则直接跳过此次循环
            if (OnuConstants.NON_TIME.equals(onLineTimeHex)) {
                continue;
            }else {
                onuOnOffRecord.setOnTime(transferTime(onLineTimeHex));
            }
            
            String[] offLineTimeArray = Arrays.copyOfRange(array, startIndex + OnuConstants.BYTE_STEP, startIndex + 2
                    * OnuConstants.BYTE_STEP);
            String offLineTimeHex = arrayToString(offLineTimeArray);
            if (OnuConstants.NON_TIME.equals(offLineTimeHex)) {
                onuOnOffRecord.setOffTime(null);
            }else {
                onuOnOffRecord.setOffTime(transferTime(offLineTimeHex));
            }
            
            String[] offReasonArray = Arrays.copyOfRange(array, startIndex + 2 * OnuConstants.BYTE_STEP, startIndex + 3
                    * OnuConstants.BYTE_STEP);
            onuOnOffRecord.setOffReason(transferToShort(offReasonArray));

            // 截取每条记录对应的字节--获取到的是16进制的字符串形式（字节记录如AA:BB:CC:DD...）
            // 每条记录12个字节，对应的字符串长度是35
            // 36的整数倍位置对应的是:，舍弃
            String onOffRecordByteList = onuEventLogEntry.getTopOnuEventLogList().substring(OnuConstants.BYTE_INDEX * i,
                    OnuConstants.BYTE_INDEX * (i + 1) - 1);
            onuOnOffRecord.setOnOffRecordByteList(onOffRecordByteList);
            
            onuOnOffRecord.setCollectTime(new Timestamp(new Date().getTime()));
            onuOnOffRecord.setOnuIndex(onuEventLogEntry.getOnuIndex());
            
            onuOnOffRecords.add(onuOnOffRecord);
        }
        return onuOnOffRecords;
    }
    
    private static String arrayToString(String[] strs){
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    /**
     * 将十六进制解析为时间
     * 因为java是根据系统的时区解析时间，而c语言是根据GMT标准时区解析时间，所以这里设置时区为GMT
     * 
     * @param timeArray
     * @return
     */
    private static Timestamp transferTime(String timeArrayHex) {
        //解析出来的是距1970/1/1 00:00的秒数
        long times = Long.parseLong(timeArrayHex, OnuConstants.HEX_SYSTEM);
        Timestamp date = new Timestamp(times*1000);
        TimeZone oldZone = Calendar.getInstance().getTimeZone();
        TimeZone newZone = TimeZone.getTimeZone("GMT");
        return changeTimeZone(date, oldZone, newZone);
    }

    /**
     * 消除时区的影响
     * 
     * @param date
     * @param oldZone
     * @param newZone
     * @return
     */
    public static Timestamp changeTimeZone(Timestamp date, TimeZone oldZone, TimeZone newZone) {  
        Timestamp dateTmp = null;  
        if (date != null) {  
            int timeOffset = oldZone.getRawOffset() - newZone.getRawOffset();  
            dateTmp = new Timestamp(date.getTime() - timeOffset);  
        }  
        return dateTmp;  
    }  
    
    private static short transferToShort(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (String item : array) {
            sb.append(item);
        }
        return Short.parseShort(sb.toString(), OnuConstants.HEX_SYSTEM);
    }
}
