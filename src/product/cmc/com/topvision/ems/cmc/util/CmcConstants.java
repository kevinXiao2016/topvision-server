/***********************************************************************
 * $Id: CmcConstants.java,v1.0 2012-2-14 下午01:27:18 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.util;

/**
 * @author huqiao
 * @created @2012-2-14-下午01:27:18
 * 
 */
public class CmcConstants {
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

    // 需要嵌入单机WEB中下行通道的最小版本号
    public static final String CMC_WEB_DOWNCHANNEL_VER_MIN = "V1.2.10.8";

    public static String CMC_STRING = "CMTS";

    public static String CMC_CMTS = "CMTS";

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

    // 将标准(DOCSIS)中MIB节点的定义写出
    public static String DOCSIFCMTSCMSTATUSINDEX = "1.3.6.1.2.1.10.127.1.3.3.1.1";
    public static Integer MAXCM_ONCC = 512;// 1024
    // 将私有MIB中CM统计总数节点
    // public static String CMTSCMTOTALNUM = "1.3.6.1.4.1.32285.11.1.1.2.2.3.1";
    // 私有MIB中清除某台CC下的所有下线CM(table节点)
    public static String TOPCCMTSCMOFFLINECLEAR = "1.3.6.1.4.1.32285.11.1.1.2.2.4.1.2";
    // 私有MIB中清除某台CC下的所有下线CM flap信息(table节点)
    public static String TOPCCMTSCMFLAPCLEAR = "1.3.6.1.4.1.32285.11.1.1.2.2.4.1.3";
    // 私有MIB中重启某台CC下的所有CM
    public static String TOPCCMTSCMRESET = "1.3.6.1.4.1.32285.11.1.1.2.2.4.1.1";
    // 私有MIB中通过CCMTS重启CM
    public static String TOPCMCTLDROPMODEM = "1.3.6.1.4.1.32285.11.1.1.2.2.5.1.1";

    public static Integer DHCP_PRIMARY = 1;
    public static Integer DHCP_POLICY = 2;
    public static Integer DHCP_STRICT = 3;

    public static Integer DHCP_DEVICETYPE_CM = 1;
    public static Integer DHCP_DEVICETYPE_HOST = 2;
    public static Integer DHCP_DEVICETYPE_MTA = 3;
    public static Integer DHCP_DEVICETYPE_STB = 4;
    public static Integer DHCP_DEVICETYPE_ALL = 5;

    // syslog记录方式
    public static final String RECORDTYPE_LOCALNONVOL = "localnonvol";
    public static final String RECORDTYPE_TRAPS = "traps";
    public static final String RECORDTYPE_SYSLOG = "syslog";
    public static final String RECORDTYPE_LOCALVOLATILE = "localvolatile";

    // 事件等级
    public static final Integer EVTLVL_EMERGENCY = 1;
    public static final Integer EVTLVL_ALERT = 2;
    public static final Integer EVTLVL_CRITICAL = 3;
    public static final Integer EVTLVL_ERROR = 4;
    public static final Integer EVTLVL_WARNING = 5;
    public static final Integer EVTLVL_NOTIFICATION = 6;
    public static final Integer EVTLVL_INFORMATIONAL = 7;
    public static final Integer EVTLVL_DEBUG = 8;
    public static final Integer EVTLVL_NONE = 28;

    // 越界处理方式
    public static final Integer UNCONSTRAINED = 1;
    public static final Integer MAINTAINBELOWTHRESHOLD = 2;
    public static final Integer STOPATTHRESHOLD = 3;
    public static final Integer INHIBITED = 4;
    // CMC VLAN相关常量
    public static Integer PRI_TAG_IS = 1;
    public static Integer PRI_TAG_NO = 2;
    public static Integer PRI_TAG = 0;
    public static Integer DHCP_ALLOC_ON = 1;
    public static Integer DHCP_ALLOC_OFF = 2;
    public static Integer PRIIP_EXIST = 2;
    public static Integer PRIIP_NO_EXIST = 1;
    // DHCP 中的常量
    public static final String BUNDLE_PRE = "bundle";

    /**
     * 负载均衡组最大允许数目
     */
    public static Integer LOADBALGRP_MAX = 16;

    // Row Status of MIB entry
    public static final Integer ROWSTATUS_ACTIVE = 1;
    public static final Integer ROWSTATUS_CREATE = 4;
    public static final Integer ROWSTATUS_DROP = 6;

    public static final Integer UPDATE_IMAGE = 5;
    public static final Integer UPDATE_CONFIG = 4;

    public static final Long CHANNEL_TYPE_UP = 129L;
    public static final Long CHANNEL_TYPE_DOWN = 128L;

    public static final Integer CM_IMMEDIATELY_QUERY = 1;
    public static final Integer CM_REMOTE_QUERY = 2;

    //Load Type
    public static final int LOAD_TYPE_FOLDER = 1;
    public static final int LOAD_TYPE_CMC = 2;
    public static final int LOAD_TYPE_CM = 3;
    //NO AREA
    public static final int NO_AREA_FLAG = -1;
}
