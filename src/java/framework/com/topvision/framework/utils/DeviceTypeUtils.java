/***********************************************************************
 * $Id: DeviceTypeUtils.java,v1.0 2013-4-2 上午10:17:45 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.utils;

/**
 * 判断设备类型工具类
 * 
 * @author lzs
 * @created @2013-4-2-上午10:17:45
 * 
 */
public class DeviceTypeUtils {

    // Modify by Victor@2013-4-8增加设备类型常量
    public static final Long CMC_TYPE = 30000L;
    public static final Long CMC_TYPE_8800A = 30001L;
    public static final Long CMC_TYPE_8800B = 30002L;
    public static final Long CMC_TYPE_8800C = 30003L;
    public static final Long CMC_TYPE_8800D = 30004L;
    public static final Long CMC_TYPE_8800C_A = 30005L;
    public static final Long CMC_TYPE_8800C_B = 30006L;
    public static final Long CMC_TYPE_8800S = 30007L;

    public static final Long CMC_TYPE_CCMTS = 30000L;
    public static final Long CMC_TYPE_CMTS = 40000L;

    public static final Long CMC_TYPE_BSR2000 = 41100L;
    public static final Long CMC_TYPE_UBR7225 = 42100L;
    public static final Long CMC_TYPE_CASAC2200 = 43200L;
    public static final Long CMC_TYPE_CASAC2100 = 43100L;
    public static final Long CMC_TYPE_CASAC3000 = 43300L;
    public static final Long CMC_TYPE_UBR7246 = 42200L;

    // 需要嵌入单机WEB中下行通道的最小版本号
    public static final String CMC_WEB_DOWNCHANNEL_VER_MIN = "V1.2.10.8";

    public static String CMC_STRING = "CCMTS";
    public static String CMC_TYPE_8800A_String = "CC8800A";
    public static String CMC_TYPE_8800B_String = "CC8800B";
    public static String CMC_TYPE_8800C_String = "CC8800C";
    public static String CMC_TYPE_8800D_String = "CC8800D";
    public static String CMC_TYPE_8800S_String = "CC8800S";

    public static String CMC_CMTS = "CMTS";

    public static String CMC_TYPE_8800C_A_String = "CC8800C-A";
    public static String CMC_TYPE_8800C_B_String = "CC8800C-B";

    // CC保存配置状态-saving(1)
    public static Integer CMC_SAVE_CONFIG_SAVING = 1;
    // CC保存配置状态-savedSuccess(2)
    public static Integer CMC_SAVE_CONFIG_SAVEDORREADY = 2;
    // CC保存配置状态-savedFailed(3)
    public static Integer CMC_SAVE_CONFIG_SAVEDFAIL = 3;

    // topCcmtsSysStatus 状态
    public static Integer TOPCCMTSSYSSTATUS_UNBIND = 1;
    public static Integer TOPCCMTSSYSSTATUS_OFFLINE = 2;
    public static Integer TOPCCMTSSYSSTATUS_INCONFIG = 3;
    public static Integer TOPCCMTSSYSSTATUS_ONLINE = 4;
    public static Integer TOPCCMTSSYSSTATUS_CONFIGFAIL = 5;
    public static Integer TOPCCMTSSYSSTATUS_WAITREADY = 6;

    // ifOperStatus 状态
    public static Integer IFOPERSTATUS_UP = 1;
    public static Integer IFOPERSTATUS_DOWN = 2;
    public static Integer IFOPERSTATUS_TESTING = 3;
    public static Integer IFOPERSTATUS_UNKNOWN = 4;
    public static Integer IFOPERSTATUS_DORMANT = 5;
    public static Integer IFOPERSTATUS_NOTPRESENT = 6;
    public static Integer IFOPERSTATUS_LOWERLAYERDOWN = 6;
    public static Integer TOPCCMTSSYSSTATUS_NUM = 8;

    // 下行信道 欧标，美标
    public static Integer ANNEX_A = 8000000;
    public static Integer ANNEX_B = 6000000;
    public static Integer ANNEX_A_VALUE = 3;
    public static Integer ANNEX_B_VALUE = 4;

    /**
     * 判断设备是否是8800A型
     * 
     * @param productType
     * @return
     */
    public static boolean isCmc8800A(Long productType) {
        return CMC_TYPE_8800A.equals(productType);
    }

    /**
     * 根据字符串判断是否是8800A 合法值："11001","CC8800A"
     * 
     * @param deviceType
     * @return
     */
    public static boolean isCmc8800A(String deviceType) {
        return CMC_TYPE_8800A.toString().equals(deviceType) || CMC_TYPE_8800A_String.equals(deviceType);
    }

    /**
     * 判断设备是否是8800B型
     * 
     * @param productType
     * @return
     */
    public static boolean isCmc8800B(Long productType) {
        return CMC_TYPE_8800B.equals(productType);
    }

    /**
     * 判断设备是否是8800S型
     * 
     * @param productType
     * @return
     */
    public static boolean isCmc8800S(Long productType) {
        return CMC_TYPE_8800S.equals(productType);
    }

    /**
     * 根据字符串判断是否是8800B 合法值："12000","CC8800B"
     * 
     * @param deviceType
     * @return
     */
    public static boolean isCmc8800B(String deviceType) {
        return CMC_TYPE_8800B.toString().equals(deviceType) || CMC_TYPE_8800B_String.equals(deviceType);
    }

    /**
     * 根据字符串判断是否是CMTS 合法值："40000","CMTS"
     * 
     * @param deviceType
     * @return
     */
    public static boolean isCmts(String deviceType) {
        return CMC_TYPE_CMTS.toString().equals(deviceType) || CMC_CMTS.equals(deviceType);
    }

    /**
     * 判断设备是否是8800C型
     * 
     * @param productType
     * @return
     */
    public static boolean isCmc8800C(Long productType) {
        return CMC_TYPE_8800C.equals(productType);
    }

    /**
     * 判断设备是否是8800C_A型
     * 
     * @param productType
     * @return
     */
    public static boolean isCmc8800C_A(Long productType) {
        return CMC_TYPE_8800C_A.equals(productType);
    }

    /**
     * 判断设备是否是8800C_B型
     * 
     * @param productType
     * @return
     */
    public static boolean isCmc8800C_B(Long productType) {
        return CMC_TYPE_8800C_B.equals(productType);
    }

    /**
     * 判断设备是否是8800D型
     * 
     * @param productType
     * @return
     */
    public static boolean isCmc8800D(Long productType) {
        return CMC_TYPE_8800D.equals(productType);
    }

    /**
     * 判断设备是否是CMTS
     * 
     * @param productType
     * @return
     */
    public static boolean isCmts(Long productType) {
        if (productType == null) {
            return false;
        }
        if (CMC_TYPE_CMTS <= productType && productType < 50000L) {
            return true;
        }
        return false;
    }

    /**
     * 判断设备是否是BSR2000
     * 
     * @param productType
     * @return
     */
    public static boolean isBSR2000(Long productType) {
        return CMC_TYPE_BSR2000.equals(productType);
    }

    /**
     * 判断设备是否是UBR7225
     * 
     * @param productType
     * @return
     */
    public static boolean isUBR7225(Long productType) {
        return CMC_TYPE_UBR7225.equals(productType);
    }

    /**
     * 判断设备是否是CASA
     * 
     * @param productType
     * @return
     */
    public static boolean isCASA(Long productType) {
        return CMC_TYPE_CASAC2100.equals(productType) || CMC_TYPE_CASAC2200.equals(productType)
                || CMC_TYPE_CASAC3000.equals(productType);
    }

    /**
     * 判断设备是否是8800A型或者8800C_A
     * 
     * @param productType
     * @return
     */
    public static boolean isCmc8800CAORA(Long productType) {
        return CMC_TYPE_8800A.equals(productType) || CMC_TYPE_8800C_A.equals(productType);
    }

    /**
     * 判断设备是否是8800A型或者8800C_A
     * 
     * @param productType
     * @return
     */
    public static boolean isCmc8800CAORA(Integer productType) {
        return CMC_TYPE_8800A.intValue() == productType.intValue()
                || CMC_TYPE_8800C_A.intValue() == productType.intValue();
    }

    /**
     * 判断设备是否是8800A型或者8800C_A
     * 
     * @param productType
     * @return
     */
    public static boolean isCmc8800CAORA(String productType) {
        return CMC_TYPE_8800A.toString().equals(productType) || CMC_TYPE_8800A_String.equals(productType)
                || CMC_TYPE_8800C_A.toString().equals(productType) || CMC_TYPE_8800C_A_String.equals(productType);
    }

    public static boolean isSameAsCmc8800B(long productType) {
        return CMC_TYPE_8800B.equals(productType) || CMC_TYPE_8800D.equals(productType)
                || CMC_TYPE_8800C_B.equals(productType) || CMC_TYPE_8800S.equals(productType);
    }

    public static boolean isUBR(Long productType){
        return CMC_TYPE_UBR7225.equals(productType) || CMC_TYPE_UBR7246.equals(productType);
    }

}
