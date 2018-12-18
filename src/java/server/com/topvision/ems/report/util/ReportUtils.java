package com.topvision.ems.report.util;

import java.util.UUID;

public class ReportUtils {

    public static String buildTempImgName(String format) {
        return ReportConstants.REPORT_TEMP_PNG + UUID.randomUUID().toString() + "." + format;
    }

    /**
     * 将PONINDEX转化为3/4格式
     * 
     * @param ponIndex
     * @return
     * @author fanzidong at 2013-09-11
     */
    public static String getPonIndexStr(Long ponIndex) {
        String resultStr = "";

        String hexStr = Long.toHexString(ponIndex);
        // 补全为10位
        int hexStrLen = hexStr.length();
        for (int i = 0; i < 10 - hexStrLen; i++) {
            hexStr = "0" + hexStr;
        }
        // 转成3/4类似格式
        resultStr = Integer.valueOf(hexStr.substring(0, 2)) + "/" + Integer.valueOf(hexStr.substring(2, 4));
        return resultStr;
    }

}
