/***********************************************************************
 * $Id: PerfTargetConstant.java,v1.0 2013-8-1 下午04:13:34 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lizongtian
 * @created @2013-8-1-下午04:13:34
 * 
 */
public class PerfTargetConstants {

    // 性能指标分组
    // public static String ONLINE_QUALITY = "runningState"; // 在线状态
    // public static String SERVICE_QUALITY = "service"; //服务质量
    // public static String FLOW_QUALITY = "flow"; //流量
    // public static String SIGNAL_QUALITY = "signalQuality"; //信号质量
    // public static String CM_FLAP_GRP = "cmflapGrp";

    // 指标性能开启与关闭
    public static final int TARGET_ENABLE_ON = 1;
    public static final int TARGET_ENABLE_OFF = 0;

    // OLT性能指标分组
    public static final String OLT_SERVICE = "olt_service"; // 服务质量
    public static final String OLT_FLOW = "olt_flow"; // 速率
    public static final String OLT_DEVICESTATUS = "olt_deviceStatus"; // 设备状态

    // CCMTS性能指标分组
    public static final String CMC_SERVICE = "cmc_service"; // 服务质量
    public static final String CMC_FLOW = "cmc_flow"; // 速率
    public static final String CMC_SIGNALQUALITY = "cmc_signalQuality"; // 信号质量
    public static final String CMC_BUSINESSQUALITY = "cmc_businessQuality"; // 业务质量
    public static final String CMC_DEVICESTATUS = "cmc_deviceStatus"; // 设备状态

    // CMTS性能指标分组
    // public static final String CMTS_FLOW = "cmts_flow"; // 速率
    // public static final String CMTS_SERVICE = "cmts_service"; // 服务质量
    // public static final String CMTS_SIGNALQUALITY = "cmts_signalQuality"; // 信号质量
    // public static final String CMTS_DEVICESTATUS = "cmts_deviceStatus"; // 设备状态

    // OLT性能指标
    public static final String OLT_CPUUSED = "olt_cpuUsed";
    public static final String OLT_MEMUSED = "olt_memUsed";
    public static final String OLT_FLASHUSED = "olt_flashUsed";
    public static final String OLT_BOARDTEMP = "olt_boardTemp";
    public static final String OLT_FANSPEED = "olt_fanSpeed";
    public static final String OLT_OPTLINK = "olt_optLink";
    public static final String OLT_PONFLOW = "olt_ponFlow";
    public static final String OLT_SNIFLOW = "olt_sniFlow";
    public static final String OLT_PONUSED = "olt_ponUsed";
    public static final String OLT_SNIUSED = "olt_sniUsed";
    public static final String OLT_ONLINESTATUS = "olt_onlineStatus";
    public static final String OLT_SUBEQUPOLLING = "olt_subEquPolling";

    // CCMTS性能指标
    public static final String CMC_CPUUSED = "cmc_cpuUsed";
    public static final String CMC_FLASHUSED = "cmc_flashUsed";
    public static final String CMC_MEMUSED = "cmc_memUsed";
    public static final String CMC_MODULETEMP = "cmc_moduleTemp";
    public static final String CMC_OPTLINK = "cmc_optLink";
    public static final String CMC_UPLINKFLOW = "cmc_upLinkFlow";
    public static final String CMC_CHANNELSPEED = "cmc_channelSpeed";
    public static final String CMC_MACFLOW = "cmc_macFlow";
    public static final String CMC_BER = "cmc_ber";
    public static final String CMC_SNR = "cmc_snr";
    public static final String CMC_CMFLAP = "cmc_cmflap";
    public static final String CMC_OPTICALRECEIVER = "cmc_opticalReceiver";
    public static final String CMC_ONLINESTATUS = "cmc_onlineStatus";
    public static final String CMC_DOROPTTEMP = "cmc_dorOptTemp";// 光机温度指标
    public static final String CMC_DORLINEPOWER = "cmc_dorLinePower";

    // CMTS性能指标
    // public static final String CMTS_SNR = "cmts_snr";
    // public static final String CMTS_BER = "cmts_ber";
    // public static final String CMTS_UPLINKFLOW = "cmts_upLinkFlow";
    // public static final String CMTS_CHANNELSPEED = "cmts_channelSpeed";
    // public static final String CMTS_ONLINESTATUS = "cmts_onlineStatus";

    // 性能指标
    // public static String CPU_USED = "cpuUsed"; //CPU利用率
    // public static String MEM_USED = "memUsed"; //内存利用率
    // public static String FLASH_USED = "flashUsed"; //flash利用率
    // public static String OPT_LINK = "optLink"; //光链路质量
    public static final String ONLINE_STATUS = "onlineStatus"; // 在线状态

    // public static String BOARD_TEMP = "boardTemp"; //板卡温度
    // public static String FAN_SPEED = "fanSpeed"; //风扇转速
    // public static String SNI_FLOW = "sniFlow"; //SNI口流量
    public static final String SNI_IN_FLOW = "sniInFlow"; // SNI口入流量
    public static final String SNI_OUT_FLOW = "sniOutFlow"; // SNI口出流量
    public static final String SNI_IN_USED = "sniInUsed"; // SNI口入方向利用率
    public static final String SNI_OUT_USED = "sniOutUsed"; // SNI口出方向利用率
    // public static String PON_FLOW = "ponFlow"; //PON口流量
    public static final String PON_IN_FLOW = "ponInFlow"; // PON口入流量
    public static final String PON_OUT_FLOW = "ponOutFlow"; // PON口出流量
    public static final String PON_IN_USED = "ponInUsed"; // PON口入方向利用率
    public static final String PON_OUT_USED = "ponOutUsed"; // PON口出方向利用率
    public static final String ONUPON_FLOW = "onuPonFlow"; // ONU PON口流量
    public static final String ONUPON_IN_FLOW = "onuPonInFlow"; // ONU PON口入流量
    public static final String ONUPON_OUT_FLOW = "onuPonOutFlow"; // ONU PON口出流量
    public static final String UNI_FLOW = "uniFlow"; // UNI口流量
    public static final String UNI_IN_FLOW = "uniInFlow"; // UNI口入流量
    public static final String UNI_OUT_FLOW = "uniOutFlow"; // UNI口出流量
    public static final String ONU_CATV_TARGET = "onuCatv";// onu catv指标

    // public static String UPLINK_FLOW = "upLinkFlow"; // 上联口流量
    public static final String UPLINK_IN_FLOW = "upLinkInFlow";
    public static final String UPLINK_OUT_FLOW = "upLinkOutFlow";
    // public static String MAC_FLOW = "macFlow"; // Mac域流量
    public static final String MAC_IN_FLOW = "macInFlow";
    public static final String MAC_OUT_FLOW = "macOutFlow";
    // public static String CHANNEL_SPEED = "channelSpeed"; // 信道速率
    public static final String UPCHANNEL_SPEED = "upChannelSpeed"; // 信道速率
    public static final String DOWNCHANNEL_SPEED = "downChannelSpeed"; // 信道速率
    // public static String MODULE_TEMP = "moduleTemp"; //模块温度
    // public static String SNR = "snr"; // 信噪比
    // public static String BER = "ber"; // 误码率
    public static final String SYSUPTIME = "sysUptime"; // 启动时长
    // public static String RUNNINGSTATE = "runningState";
    public static final String Group_OPTICAL = "optical"; // 光机分组
    // public static String OPTICAL_RECEIVER = "opticalReceiver";//光机信息 added by huangdongsheng
    // 2013-12-17
    // public static String CM_FLAP = "cmflap";
    public static final String SYSUPTIME_CATEGORY = "CMTS_SYSTEM";
    public static final String SNR_CATEGORY = "CC_NOISE";
    public static final String BER_CATEGORY = "CC_USBITERRORRATE";
    public static final String CHANNEL_SPEED_CATEGORY = "CC_CHANNELSPEEDSTATIC";
    public static final String UPLINK_FLOW_CATEGORY = "UPLINK_FLOW";
    public static final String CC_CMSTATUS_CATEGORY = "CC_CMSTATUS";
    public static final String CPE_CMSTATUS_CATEGORY = "CPE_CMSTATUS";

    // 分组-指标对
    // public static String[] OLT_SERVICE_PERFTARGETS = { CPU_USED, MEM_USED, FLASH_USED,
    // BOARD_TEMP, FAN_SPEED, OPT_LINK };
    // public static String[] OLT_FLOW_PERFTARGETS = { SNI_FLOW, PON_FLOW, ONUPON_FLOW, UNI_FLOW };

    // public static String[] CMC_FLOW_PERFTARGETS = { UPLINK_FLOW, MAC_FLOW, CHANNEL_SPEED };
    // public static String[] CMC_SERVICE_PERFTARGETS = { CPU_USED, MEM_USED, FLASH_USED,
    // MODULE_TEMP, OPT_LINK };
    // public static String[] CMC_SQ_PERFTARGETS = { SNR, BER };

    public static final String[] CMC_UPLINKFLOW_INOUT = { UPLINK_IN_FLOW, UPLINK_OUT_FLOW };

    // 子指标
    public static final String OPT_TXPOWER = "optTxPower"; // 光口发送功率
    public static final String OPT_RXPOWER = "optRePower"; // 光口接收功率
    public static final String OPT_CURRENT = "optCurrent"; // 光口偏置电流
    public static final String OPT_VOLTAGE = "optVoltage"; // 光口电压
    public static final String OPT_TEMP = "optTemp"; // 光口温度
    public static final String MAC_USED = "macUsed"; // Mac域利用率
    public static final String UPLINK_USED = "upLinkUsed"; // 上联口利用率
    public static final String UPLINK_IN_USED = "upLinkInUsed"; // 上联口入方向利用率
    public static final String UPLINK_OUT_USED = "upLinkOutUsed"; // 上联口出方向利用率
    public static final String CHANNEL_USED = "channelUsed"; // 信道利用率
    public static final String US_TEMP = "usTemp"; // 上行模块温度
    public static final String DS_TEMP = "dsTemp"; // 下行模块温度
    public static final String OUTSIDE_TEMP = "outsideTemp"; // MAC模块温度
    public static final String INSIDE_TEMP = "insideTemp"; // MAC芯片温度
    public static final String POWER_TEMP = "powerTemp"; // MAC模块温度
    public static final String CCER = "ccer"; // 可纠错码率
    public static final String UCER = "ucer"; // 不可纠错码率
    public static final String UPCHANNEL_USED = "upChannelUsed";
    public static final String DOWNCHANNEL_USED = "downChannelUsed";
    public static final String PON_RXPOWER = "ponRePower"; // PON口基于LLID接收功率

    public static final String CM_FLAP_INSFAIL = "insfail";
    public static final String CM_FLAP_INSFAILGROW = "failOnlineCounter";
    public static final String CM_FLAP_POWER_ADJ = "poweradj";
    public static final String CM_FLAP_HITP_ERCENTGROW = "hitpercentgrow";

    public static final String CMC_ONLINENUM = "cmc_onlineRatio";
    public static final String CMC_CCUPFLOW = "cmc_ccupFlow";
    public static final String CMC_CCDOWNFLOW = "cmc_cmDownFlow";
    public static final String CMC_CMTXAVG = "cmc_cmTxAvg";
    public static final String CMC_CMTXNOTINRANGE = "cmc_cmTxNotInRange";
    public static final String CMC_CMREAVG = "cmc_cmReAvg";
    public static final String CMC_CMRENOTINRANGE = "cmc_cmReNotInRange";
    public static final String CMC_UPSNRAVG = "cmc_upSnrAvg";
    public static final String CMC_UPSNRNOTINRANGE = "cmc_upSnrNotInRange";
    public static final String CMC_DOWNSNRAVG = "cmc_downSnrAvg";
    public static final String CMC_DOWNSNRNOTINRANGE = "cmc_downSnrNotInRange";

    // 指标及包含指标对
    public static final String[] OPTLINK_PERFTARGETS = { OPT_TXPOWER, OPT_RXPOWER, OPT_CURRENT, OPT_VOLTAGE, OPT_TEMP };
    // public static String[] MACFLOW_PERFTARGETS = { MAC_FLOW, MAC_USED };
    // public static String[] CHANNELSPEED_PERFTARGETS = { CHANNEL_SPEED, CHANNEL_USED };
    public static final String[] MODULETEMP_PERFTARGETS = { US_TEMP, DS_TEMP, OUTSIDE_TEMP };
    public static final String[] BER_PERFTARGETS = { CCER, UCER };
    public static final String[] CMTSCHANNELSPEED_PERFTARGETS = { UPCHANNEL_SPEED, DOWNCHANNEL_SPEED };
    public static final String[] CMTSCHANNELUSED_PERFTARGETS = { UPCHANNEL_USED, DOWNCHANNEL_USED };
    // public static String[] CMC_CMFLAP = { CM_FLAP_INSFAIL, CM_FLAP_INSFAILGROW,
    // CM_FLAP_POWER_ADJ,CM_FLAP_HITP_ERCENTGROW };

    // 方向
    public static final Integer DIRECTION_IN = -1;
    public static final Integer DIRECTION_OUT = 1;

    // 信道类型
    public static final String CHANNEL_TYPE_ALL = "All";
    public static final String CHANNEL_TYPE_US = "US";
    public static final String CHANNEL_TYPE_DS = "DS";
    public static final String CHANNEL_TYPE_UPLINK = "UPLINK";// 上联口

    public static boolean isPerfOnlyFor8800B(String perfName) {
        boolean result = false;
        List<String> perfsFor8800B = new ArrayList<String>();
        perfsFor8800B.add(CMC_UPLINKFLOW);
        perfsFor8800B.add(US_TEMP);
        perfsFor8800B.add(DS_TEMP);
        perfsFor8800B.add(OUTSIDE_TEMP);
        perfsFor8800B.add(INSIDE_TEMP);
        perfsFor8800B.add(CMC_ONLINESTATUS);
        // perfsFor8800B.add(POWER_TEMP);
        for (String perf : perfsFor8800B) {
            if (perfName.equals(perf)) {
                result = true;
            }
        }
        return result;

    }

    public static final String TOTALCM = "totalCm"; // CM数总数
    public static final String ONLINECM = "onlineCm"; // CM在线数
    public static final String OFFLINECM = "offlineCm"; // CM离线数

    // onu性能指标
    public static final String ONU_DEVICESTATUS = "onu_deviceStatus"; // 设备状态
    public static final String ONU_ONLINESTATUS = "onu_onlineStatus";
    public static final String ONU_OPTLINK = "onu_optLink";
    public static final String ONU_FLOW = "onu_flow";
    public static final String ONU_PORTFLOW = "onu_portFlow";
    public static final String ONU_CATV = "onuCatvInfo";

    public static final String PERF_OLT_SERVICE = "eponStatsService";
    public static final String PERF_CCMTS_SERVICE = "cmcPerfService";
    public static final String PERF_CMTS_SERVICE = "cmtsPerfService";
    public static final String PERF_ONU_SERVICE = "onuPerfService";
    // ONU光链路信息相关指标
    public static final String ONU_PON_REPOWER = "onuPonRePower";
    public static final String ONU_PON_TXPOWER = "onuPonTxPower";
    public static final String OLT_PON_REPOWER = "oltPonRePower";
    public static final String ONU_CATV_REPOWER = "onuCATVRePower";
    // ONU速率相关指标
    public static final String ONU_PON_IN_SPEED = "onuPonInSpeed";
    public static final String ONU_PON_OUT_SPEED = "onuPonOutSpeed";
    public static final String ONU_UNI_IN_SPEED = "onuUniInSpeed";
    public static final String ONU_UNI_OUT_SPEED = "onuUniOutSpeed";
    // 指标及包含指标对
    public static final String[] ONU_OPTLINK_PERFTARGETSWithCATV = { ONU_PON_REPOWER, ONU_PON_TXPOWER, OLT_PON_REPOWER,
            ONU_CATV_REPOWER };
    public static final String[] ONU_OPTLINK_PERFTARGETS = { ONU_PON_REPOWER, ONU_PON_TXPOWER, OLT_PON_REPOWER };
    public static final String[] ONU_FLOW_PERFTARGETS = { ONU_PON_IN_SPEED, ONU_PON_OUT_SPEED, ONU_UNI_IN_SPEED,
            ONU_UNI_OUT_SPEED };
    // 端口类型
    public static final String PORTTYPE_PON = "PON";
    public static final String PORTTYPE_UNI = "UNI";
    // 端口方向
    public static final int PORTDIRECTION_IN = 1;
    public static final int PORTDIRECTION_OUT = 2;

    // 默认ONU发送与接收光功率阈值规则
    public static final String ONU_REV_RULE = "1_-7_2_4#5_-24_3_5#5_-27_4_6";
    public static final String ONU_TRANS_RULE = "1_8_3_4#5_2_4_5";

}