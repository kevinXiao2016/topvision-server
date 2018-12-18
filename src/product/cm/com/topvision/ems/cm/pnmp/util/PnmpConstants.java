package com.topvision.ems.cm.pnmp.util;

/**
 * pnmp包下常量
 * 
 * @author w1992wishes
 * @created @2017年8月11日-上午10:18:17
 *
 */
public class PnmpConstants {

    private PnmpConstants() {
    }

    public static final String IP_STRING = "ipAddress";
    public static final String ALIAS = "alias";
    public static final String CMC_INDEX = "cmcIndex";
    public static final String LOCATION = "location";

    // 添加cmMac到高频采集 结果
    public static final String CODE = "code";
    public static final Integer MAC_ADD_FAIL = 5;// mac添加失败
    public static final Integer MAC_HIGH_FULL = 4;// 高频采集CM数量超过1000
    public static final Integer MAC_NOT_IN_EMS = 3;// mac不在网管中
    public static final Integer MAC_DUPLICATE = 2;// mac已在高频采集
    public static final Integer MAC_ADD_OK = 1;// 加入高频采集成功
    public static final Integer MAC_NOT_NULL = 0;// mac不能为空

    // 指标名字
    public static final String TARGET_MTR = "mtr";
    public static final String TARGET_MTC = "mtc";

    // 指标阈值名称
    public static final String THRESHOLD_BAD = "bad";
    public static final String THRESHOLD_HEALTH = "health";
    public static final String THRESHOLD_TOOHIGH = "tooHigh";
    public static final String THRESHOLD_TOOLOW = "tooLow";

}
