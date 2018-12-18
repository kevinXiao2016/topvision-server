/***********************************************************************
 * $Id: EponConstants.java,v1.0 2011-11-13 下午05:41:39 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huqiao
 * @created @2011-11-13-下午05:41:39
 * 
 */
public class EponConstants {
    // 用于ACTION向界面传不能为空值时的空指针的时候，传递一个标志值，表示空Integer
    public static Integer OLT_NULL_INTEGER_VALUE = -7;

    // STRAT ONU 认证的静态变量
    // PON口的ONU认证使能
    public static Integer OLT_AUTHEN_DISABLE = 0;
    public static Integer OLT_AUTHEN_ENABLE = 1;
    // 标示ONU认证中的MAC地址认证方式（数据库中存在的AUTHTYPE字段）
    public static Integer OLT_AUTHEN_MAC = 1;
    // 标示ONU认证中的SN地址认证方式（数据库中存在的AUTHTYPE字段）
    public static Integer OLT_AUTHEN_SN = 2;
    // PON口的ONU认证模式：自动、MAC、混合、SN、SN&PWD
    public static Integer OLT_AUTHEN_PONMODE_AUTO = 1;
    public static Integer OLT_AUTHEN_PONMODE_MAC = 2;
    public static Integer OLT_AUTHEN_PONMODE_MIX = 3;
    public static Integer OLT_AUTHEN_PONMODE_SN = 4;
    public static Integer OLT_AUTHEN_PONMODE_SNPWD = 5;
    // ONU认证SN认证模式：SN+PASSWORD、SN
    public static Integer OLT_AUTHEN_SNMODE_SP = 1;
    public static Integer OLT_AUTHEN_SNMODE_SN = 2;
    // ONU认证MAC认证动作：ACCEPT、REJECT
    public static Integer OLT_AUTHEN_MACACTION_ACCEPT = 1;
    public static Integer OLT_AUTHEN_MACACTION_REJECT = 2;
    // SN认证动作ACCEPT（固定为ACCEPT）
    public static Integer OLT_AUTHEN_SN_ACTION = 1;
    // PON口的ONU最大支持数
    public static Integer OLT_PON_ONU_MAXNUM = 64;
    // ONU的最大PON口（光模块）数目
    public static Integer OLT_PON_ONUPON_MAXNUM = 4;
    // 最大板卡数
    public static Integer OLT_SLOT_MAXNUM = 18;
    // 最大端口数
    public static Integer OLT_SLOT_PORT_MAXNUM = 16;
    // 最大UNI数目
    public static Integer OLT_PON_ONU_UNI_MAXNUM = 32;

    // END ONU 认证的静态变量
    // START IGMP的静态变量
    // topMcSniPortType 静态量
    public static Integer ANYPORT = 0;
    public static Integer PHYPORT = 1;
    public static Integer AGGPORT = 2;
    // IGMP UNI口的组播VLAN处理模式：1：剥离，2：转换，3：保持
    public static Integer IGMP_UNI_MODE_STRIP = 1;
    public static Integer IGMP_UNI_MODE_TRANS = 2;
    public static Integer IGMP_UNI_MODE_KEEP = 3;

    // END IGMP的静态变量
    // STRAT ACL的静态变量
    // 修改ACLRuleNum时的flag；0：删除了rule，ruleNum减1；1：添加了rule，ruleNum加1
    public static Integer ACL_LIST_RULENUM_DEL = 0;
    public static Integer ACL_LIST_RULENUM_ADD = 1;
    // ACLlist的最大数目
    public static Integer ACL_LIST_MAXNUM = 4000;
    // END ACL的静态变量

    // 业务使能开启
    public static Integer ABALITY_ENABLE = 1;
    // 业务使能关闭
    public static Integer ABALITY_DISABLE = 2;
    // SNI口MAC地址管理类型(静态)
    public static Integer SNI_MAC_TYPE_STATIC = 1;
    // SNI口MAC地址管理类型(动态)
    public static Integer SNI_MAC_TYPE_DYNAMIC = 2;
    // SNI口MAC地址老化时间默认值(单位：秒)
    public static Integer SNI_MAC_DEFAULT_AGING_TIME = 250;
    // SNI口MAC地址老化时间默认值(单位：秒)
    public static Integer SYS_MAC_ADDR_AGING_TIME = 20;
    // SNI口MAC地址最大学习数默认值
    public static Long SNI_MAC_DEFAULT_MAX_LEARN_NUM = 0L;

    public static Integer MAC_ADDR_AGING_TIME = 1200;
    public static Integer ARP_AGING_TIME = 1200;

    // 文件类型(文件夹)
    public static Integer FILE_TYPE_DIR = 2;
    // 文件类型(文件)
    public static Integer FILE_TYPE_FILE = 1;
    // 删除文件操作
    public static Integer FILE_DELETE = 2;
    // 文件正在传输中
    public static Integer FILE_INPROGRESS = 2;
    // 文件空闲
    public static Integer FILE_IDLE = 1;
    // 文件传输成功
    public static Integer FILE_TRANS_SUCCESS = 3;
    // 文件传输失败
    public static Integer FILE_TRANS_FAILURE = 4;
    // 文件传输超时(自定义)
    public static Integer FILE_TRANS_TIMELIMIT = 10;
    public static Integer FILE_TRANS_PRO_FTP = 1;
    public static Integer FILE_OLT_UPLOAD = 2;
    public static Integer FILE_OLT_DOWNLOAD = 3;

    // DHCP Server Index 限制
    public static Integer OLT_DHCP_HOST_MIN_INDEX = 0;
    public static Integer OLT_DHCP_HOST_MAX_INDEX = 1;
    public static Integer OLT_DHCP_CM_MIN_INDEX = 2;
    public static Integer OLT_DHCP_CM_MAX_INDEX = 3;
    public static Integer OLT_DHCP_STB_MIN_INDEX = 4;
    public static Integer OLT_DHCP_STB_MAX_INDEX = 5;
    public static Integer OLT_DHCP_MTA_MIN_INDEX = 6;
    public static Integer OLT_DHCP_MTA_MAX_INDEX = 7;
    public static Integer OLT_DHCP_DEFAULT_MIN_INDEX = 8;
    public static Integer OLT_DHCP_DEFAULT_MAX_INDEX = 9;
    // DHCP Giaddr Index 限制
    public static Integer OLT_DHCP_HOST_GIADDR_INDEX = 0;
    public static Integer OLT_DHCP_CM_GIADDR_INDEX = 1;
    public static Integer OLT_DHCP_STB_GIADDR_INDEX = 2;
    public static Integer OLT_DHCP_MTA_GIADDR_INDEX = 3;
    public static Integer OLT_DHCP_DEFAULT_GIADDR_INDEX = 4;

    // ONU升级模式（ctc）
    public static Integer ONU_UPGRADE_MODE_CTC = 4;

    // PON口端口类型（ge-epon）
    public static Integer PON_PORT_TYPE_GEEPON = 1;
    // PON口端口类型（tenge-epon）
    public static Integer PON_PORT_TYPE_TENGEEPON = 2;
    // PON口端口类型（gpon）
    public static Integer PON_PORT_TYPE_GPON = 3;
    // PON口MAC地址最大学习数默认值
    public static Long PON_MAC_DEFAULT_MAX_LEARN_NUM = 0L;
    // PON口MAC地址最大学习数默认值,针对1.6.X版本
    public static Long PON_MAC_DEFAULT_MAX_LEARN_NUM2 = -1L;
    // SNI口端口类型（geCopper）针对1.3.6.1.4.1.32285.11.2.3.2.1.1.7节点，用来显示面板图的端口类型
    public static Integer SNI_PORT_TYPE_GECOPPER = 1;
    // SNI口端口类型（geFiber）
    public static Integer SNI_PORT_TYPE_GEFIBER = 2;
    // SNI口端口类型（xeFiber）
    public static Integer SNI_PORT_TYPE_XEFIBER = 3;
    // SNI口端口类型(ge-Port) 针对 1.3.6.1.4.1.17409.2.3.2.1.1.15节点
    public static Integer SNI_PORT_TYPE_GEPORT = 1;
    // SNI口端口类型(te-Port)
    public static Integer SNI_PORT_TYPE_XEPORT = 2;
    // SNI口介质类型（twistedPair）
    public static Integer SNI_MEDIA_TYPE_TWISTEDPAIR = 1;
    // SNI口介质类型（fiber）
    public static Integer SNI_MEDIA_TYPE_FIBER = 2;
    // 板卡预配置类型：non-board(0)
    public static Integer BOARD_PRECONFIG_NOBOARD = 0;
    // 板卡预配置类型: mpua(1)
    public static Integer BOARD_PRECONFIG_MPUA = 1;
    // 板卡预配置类型: mpub(2)
    public static Integer BOARD_PRECONFIG_MPUB = 2;
    // 板卡预配置类型：epua(3)
    public static Integer BOARD_PRECONFIG_EPUA = 3;
    // 板卡预配置类型： epub(4)
    public static Integer BOARD_PRECONFIG_EPUB = 4;
    // 板卡预配置类型： geua(5)
    public static Integer BOARD_PRECONFIG_GEUA = 5;
    // 板卡预配置类型： geub(6)
    public static Integer BOARD_PRECONFIG_GEUB = 6;
    // 板卡预配置类型： xgua(7)
    public static Integer BOARD_PRECONFIG_XGUA = 7;
    // 板卡预配置类型： xgub(8)
    public static Integer BOARD_PRECONFIG_XGUB = 8;
    // 板卡预配置类型： xguc(9)
    public static Integer BOARD_PRECONFIG_XGUC = 9;
    // 板卡预配置类型： xpua(10)
    public static Integer BOARD_PRECONFIG_XPUA = 10;
    // 板卡预配置类型： mpu_geua(11)
    public static Integer BOARD_PRECONFIG_MPU_GEUA = 11;
    // 板卡预配置类型： mpu_geub(12)
    public static Integer BOARD_PRECONFIG_MPU_GEUB = 12;
    // 板卡预配置类型： mpu_xguc(13)
    public static Integer BOARD_PRECONFIG_MPU_XGUC = 13;
    // 板卡预配置类型： gpua(14)
    public static Integer BOARD_PRECONFIG_GPUA = 14;
    // 板卡预配置类型： epuc(15)
    public static Integer BOARD_PRECONFIG_EPUC = 15;
    // 板卡预配置类型： epud(16)
    public static Integer BOARD_PRECONFIG_EPUD = 16;
    // 板卡预配置类型： meua(17)
    public static Integer BOARD_PRECONFIG_MEUA = 17;
    // 板卡预配置类型： meub(18)
    public static Integer BOARD_PRECONFIG_MEUB = 18;
    // 板卡预配置类型： mefa(19)
    public static Integer BOARD_PRECONFIG_MEFA = 19;
    // 板卡预配置类型： mefb(20)
    public static Integer BOARD_PRECONFIG_MEFB = 20;
    // 板卡预配置类型： mefc(21)
    public static Integer BOARD_PRECONFIG_MEFC = 21;
    // 板卡预配置类型： mefd(22)
    public static Integer BOARD_PRECONFIG_MEFD = 22;
    // 板卡预配置类型： mgua(23)
    public static Integer BOARD_PRECONFIG_MGUA = 23;
    // 板卡预配置类型： mgub(24)
    public static Integer BOARD_PRECONFIG_MGUB = 24;
    // 板卡预配置类型： mgfa(25)
    public static Integer BOARD_PRECONFIG_MGFA = 25;
    // 板卡预配置类型： mgfb(26)
    public static Integer BOARD_PRECONFIG_MGFB = 26;
    // 板卡预配置类型： unknown(255)
    public static Integer BOARD_PRECONFIG_UNKNOWN = 255;
    public static String[] SLOT_TYPE = { "non-board", "mpua", "mpub", "epua", "epub", "geua", "geub", "xgua", "xgub",
            "xguc", "xpua", "mpu-geua", "mpu-geub", "mpu-xguc", "gpua", "epuc", "epud", "meua", "meub", "mefa", "mefb",
            "mefc", "mefd", "mgua", "mgub", "mgfa", "mgfb" };

    // 板卡实际类型
    // bActualType =
    // ["slot","mpua","mpub","epua","epub","geua","geub","xgua","xgub","xguc","xpua","gpua","meua","meub","mefa","mefb"];
    public static Integer BOARD_ONLINE_SLOT = 0;
    public static Integer BOARD_ONLINE_MPUA = 1;
    public static Integer BOARD_ONLINE_MPUB = 2;
    public static Integer BOARD_ONLINE_EPUA = 3;
    public static Integer BOARD_ONLINE_EPUB = 4;
    public static Integer BOARD_ONLINE_GEUA = 5;
    public static Integer BOARD_ONLINE_GEUB = 6;
    public static Integer BOARD_ONLINE_XGUA = 7;
    public static Integer BOARD_ONLINE_XGUB = 8;
    public static Integer BOARD_ONLINE_XGUC = 9;
    public static Integer BOARD_ONLINE_XPUA = 10;
    public static Integer BOARD_ONLINE_GPUA = 11;
    public static Integer BOARD_ONLINE_MEUA = 12;
    public static Integer BOARD_ONLINE_MEUB = 13;
    public static Integer BOARD_ONLINE_MEFA = 14;
    public static Integer BOARD_ONLINE_MEFB = 15;
    public static Integer BOARD_ONLINE_EPUC = 16;
    public static Integer BOARD_ONLINE_EPUD = 17;
    public static Integer BOARD_ONLINE_MEFC = 21;
    public static Integer BOARD_ONLINE_MEFD = 22;

    // 根据韩霞光回复确认：目前gpon 仅在gpua和mgua mgub板卡上支持 2018/10/10
    public static final List<String> GPON_BOARD_TYPE = new ArrayList<>();
    static {
        GPON_BOARD_TYPE.add("mpua");
        GPON_BOARD_TYPE.add("mpub");
        GPON_BOARD_TYPE.add("gpua");
    }
    public static final List<String> EPON_BOARD_TYPE = new ArrayList<>();
    static {
        EPON_BOARD_TYPE.add("epua");
        EPON_BOARD_TYPE.add("epub");
        EPON_BOARD_TYPE.add("geua");
        EPON_BOARD_TYPE.add("geub");
        EPON_BOARD_TYPE.add("xgua");
        EPON_BOARD_TYPE.add("xgub");
        EPON_BOARD_TYPE.add("xguc");
        EPON_BOARD_TYPE.add("xpua");
        EPON_BOARD_TYPE.add("mpu-geua");
        EPON_BOARD_TYPE.add("mpu-geub");
        EPON_BOARD_TYPE.add("mpu-xguc");
        EPON_BOARD_TYPE.add("epuc");
        EPON_BOARD_TYPE.add("epud");
        EPON_BOARD_TYPE.add("meua");
        EPON_BOARD_TYPE.add("meub");
        EPON_BOARD_TYPE.add("mefa");
        EPON_BOARD_TYPE.add("mefb");
        EPON_BOARD_TYPE.add("mefc");
        EPON_BOARD_TYPE.add("mefd");
    }

    public static Integer BOARD_ONLINE_MGUA = 23;
    public static Integer BOARD_ONLINE_MGUB = 24;
    public static Integer BOARD_ONLINE_MGFA = 25;
    public static Integer BOARD_ONLINE_MGFB = 26;

    // 端口状态(UP,Down,Testing)
    public static Integer PORT_STATUS_UP = 1;
    public static Integer PORT_STATUS_DOWN = 2;
    public static Integer PORT_STATUS_TESTING = 3;

    // IGMP工作模式-可控(2)
    public static Integer IGMP_MODE_WITHCM = 2;
    // IGMP工作模式-关闭(3)
    public static Integer IGMP_MODE_DISABLED = 3;
    // IGMP工作模式-代理(4)
    public static Integer IGMP_MODE_WOCM = 4;

    // ONU升级状态-idle(1)
    public static Integer ONU_UPGRADE_STATUS_IDLE = 1;
    // ONU升级状态-inProgress(2)
    public static Integer ONU_UPGRADE_STATUS_INPROGRESS = 2;
    // ONU升级状态-success(3)
    public static Integer ONU_UPGRADE_STATUS_SUCCESS = 3;
    // ONU升级状态-failure(4)
    public static Integer ONU_UPGRADE_STATUS_FAILURE = 4;

    // OLT保存配置状态-saving(1)
    public static Integer OLT_SAVE_CONFIG_SAVING = 1;
    // OLT保存配置状态-savedOrReady(2)
    public static Integer OLT_SAVE_CONFIG_SAVEDORREADY = 2;
    // OLT保存配置状态-savedFail(3)
    public static Integer OLT_SAVE_CONFIG_SAVEDFAIL = 3;

    // 板卡在位状态-installed(1)
    public static Integer OLT_BOARD_PRESENCE_STATUS_INSTALLED = 1;
    // 板卡在位状态-notInstalled(2)
    public static Integer OLT_BOARD_PRESENCE_STATUS_NOTINSTALLED = 2;

    // 板卡服务状态-up(1)
    public static Integer OLT_BOARD_OPERATION_STATUS_UP = 1;
    // 板卡服务状态-down(2)
    public static Integer OLT_BOARD_OPERATION_STATUS_DOWN = 2;

    // 板卡管理状态-up(1)
    public static Integer OLT_BOARD_ADMIN_STATUS_UP = 1;
    // 板卡管理状态-down(2)
    public static Integer OLT_BOARD_ADMIN_STATUS_DOWN = 2;

    // OLT板卡bAttribute状态-active(1)
    public static Integer OLT_BOARD_ATTRIBUTE_ACTIVE = 1;
    // OLT板卡bAttribute状态-standby(2)
    public static Integer OLT_BOARD_ATTRIBUTE_STANDBY = 2;
    // OLT板卡bAttribute状态-standalone(3)
    public static Integer OLT_BOARD_ATTRIBUTE_STANDALONE = 3;
    // OLT板卡bAttribute状态-notApplicable(4)
    public static Integer OLT_BOARD_ATTRIBUTE_NOTAPPLICABLE = 4;

    public static Integer OLT_BOARD_ACTUALTYPE_NOBOARD = 0;

    public static Integer ONU_STATUS_UP = 1;
    public static Integer ONU_STATUS_DOWN = 2;
    public static Integer ONU_STATUS_TESTING = 3;

    public static Integer SNI_OPERATION_UP = 1;
    public static Integer SNI_OPERATION_DOWN = 2;
    public static Long MASTERINDEX = 0l;
    public static Long SLAVEINDEX = 255l;

    public static String FILE_AUTOWRITE = "ON";
    public static String FILE_AUTOBACK = "ON";

    // EPON各设备类型需要占用的机框号
    public static Map<String, Integer> DEVICE_OCCUPY_FRAME_NUM = new HashMap<String, Integer>();
    static {
        DEVICE_OCCUPY_FRAME_NUM.put("PN8601", 13);
        DEVICE_OCCUPY_FRAME_NUM.put("PN8602", 2);
        DEVICE_OCCUPY_FRAME_NUM.put("PN8603", 8);
    }

    // ONU预配置类型
    public static Map<String, Integer> EPON_ONU_PRETYPE = new HashMap<String, Integer>();
    static {
        EPON_ONU_PRETYPE.put("255", 0);
        EPON_ONU_PRETYPE.put("8621", 33);
        EPON_ONU_PRETYPE.put("8622", 34);
        EPON_ONU_PRETYPE.put("8624", 36);
        EPON_ONU_PRETYPE.put("8625", 37);
        EPON_ONU_PRETYPE.put("8626", 38);
        EPON_ONU_PRETYPE.put("8628", 40);
        EPON_ONU_PRETYPE.put("8630", 48);
        EPON_ONU_PRETYPE.put("8631", 49);
        EPON_ONU_PRETYPE.put("8641", 65);
        EPON_ONU_PRETYPE.put("8644", 68);
        EPON_ONU_PRETYPE.put("8647", 71);
        EPON_ONU_PRETYPE.put("8651", 81);
        EPON_ONU_PRETYPE.put("8652", 82);
        EPON_ONU_PRETYPE.put("8653", 83);
        EPON_ONU_PRETYPE.put("8654", 84);
        EPON_ONU_PRETYPE.put("8800", 241);
    }
    // CC8800A-241,CC8800B-242
    public static String EPON_CC_TYPE_STRING_A = "CC8800A";
    public static Integer EPON_CC_TYPE_INTEGER_A = 241;
    public static String EPON_CC_TYPE_STRING_B = "CC8800B";
    public static Integer EPON_CC_TYPE_INTEGER_B = 242;

    // pon口vlan的transparent的模式，0:tag, 1:untag
    public static Integer OLT_PONVLAN_TRANSPARENT_MODE_TAG = 0;
    public static Integer OLT_PONVLAN_TRANSPARENT_MODE_UNTAG = 1;

    // ONU自动升级
    public static Integer EPON_ONU_AUTOUPG_RESTART = 1;// restart 操作
    public static Integer EPON_ONU_AUTOUPG_CANCEL = 1;// 取消升级操作
    public static Integer EPON_ONU_AUTOUPG_ALLNO_RESTART = 0;
    public static Integer EPON_ONU_AUTOUPG_BANDSTAT_NOBAND = 0;// 自动升级模板未绑定到PON口
    public static Integer EPON_ONU_AUTOUPG_BANDSTAT_BAND = 1;// 自动升级模板绑定到了PON口

    // 文件传输速度
    public static Integer EPON_FILE_TRANS_RATE = 60000;
    // 文件传输最大等待时间
    public static Integer EPON_FILE_TRANS_MAX_TIME = 900000;
    public static Integer EPON_FILE_TRANS_WAIT_INTERVAL = 5000;
    // XGUx的板卡类型
    public static List<Integer> EPON_SLOT_XGUx_TYPE = new ArrayList<Integer>();
    static {
        EPON_SLOT_XGUx_TYPE.add(7);// XGUA
        EPON_SLOT_XGUx_TYPE.add(8);// XGUB
        EPON_SLOT_XGUx_TYPE.add(9);// XGUC
        EPON_SLOT_XGUx_TYPE.add(13);// MPU-XGUC
        EPON_SLOT_XGUx_TYPE.add(17);// MEUA
        EPON_SLOT_XGUx_TYPE.add(19);// MEFA
        EPON_SLOT_XGUx_TYPE.add(20);// MEFB
        EPON_SLOT_XGUx_TYPE.add(21);// MEFC
        EPON_SLOT_XGUx_TYPE.add(22);// MEFD
        EPON_SLOT_XGUx_TYPE.add(23);// MGUA
        EPON_SLOT_XGUx_TYPE.add(24);// MGUB
        EPON_SLOT_XGUx_TYPE.add(25);// MGFA
        EPON_SLOT_XGUx_TYPE.add(26);// MGFB
    }
    // SNI口光口类型
    public static List<Integer> EPON_SNI_FIBER_TYPE = new ArrayList<Integer>();
    static {
        EPON_SNI_FIBER_TYPE.add(3);// xeFiber
    }

    // PON LLID VLAN TPID有效值
    public static List<String> EPON_LLID_VLAN_TPID_VALUE = new ArrayList<String>();
    static {
        EPON_LLID_VLAN_TPID_VALUE.add("8100");
        EPON_LLID_VLAN_TPID_VALUE.add("9100");
        EPON_LLID_VLAN_TPID_VALUE.add("88a8");
    }
    public static String EPON_LLID_VLAN_TPID_DEFAULT = "8100";

    // Add by Rod@2013-6-21增加端口类型常量
    public static final Integer SNI_PORT = 1;
    public static final Integer PON_PORT = 2;

    // added by huangdongsheng@2013-6-27 增加DHCP RELAy常量

    public static final String DHCP_RELAY_CM = "CM";
    public static final String DHCP_RELAY_HOST = "HOST";
    public static final String DHCP_RELAY_MTA = "MTA";
    public static final String DHCP_RELAY_STB = "STB";
    public static final Integer DHCP_RELAY_STRICT = 3;
    public static final Integer DHCP_RELAY_VLAN_MAX = 4094;
    public static final Integer DHCP_RELAY_VLANMAP_LENGTH = 4096;

    public static final Integer TB_ACTIVE = 1;
    public static final Integer TB_CREATEANDGO = 4;
    public static final Integer TB_DROP = 6;

    // add by @flack from OltPonProtectAction 2013-10-26
    public static final int ADMIN_STATUS_ENABLE = 1;
    public static final int ADMIN_STATUS_DISABLE = 2;
    // noAction(1), switchover(2)
    public static final int SWITCH_OVER = 2;

    // add by @flack from UniVlanAction 2013-10-26
    public static final Integer TRANS = 2;// vlan转换模式
    public static final Integer AGGR = 3;// vlan聚合模式
    public static final Integer TRUNK = 4;// vlan Trunk 模式

    // add by @flack UNI VLAN PROFILE 2013-11-5
    public static final int UNIVALN_UNBIND_PROFILEID = 0; // uni vlan模板未绑定

    // OLT系统文件路径类型
    public static final Integer FILE_MPU = 1;// mpu
    public static final Integer FILE_EPU = 2;// epu
    public static final Integer FILE_XPU = 3;// xpu
    public static final Integer FILE_GPU = 4;// gpu
    public static final Integer FILE_NPU = 5;// npu
    public static final Integer FILE_GEU = 6;// geu
    public static final Integer FILE_XGU = 7;// xgu
    public static final Integer FILE_BOOTROM = 8;// bootrom
    public static final Integer FILE_MPU_BACK = 9;// mpu_back
    public static final Integer FILE_EPU_BACK = 10;// epu_back
    public static final Integer FILE_XPU_BACK = 11;// xpu_back
    public static final Integer FILE_GPU_BACK = 12;// gpu_back
    public static final Integer FILE_NPU_BACK = 13;// npu_back
    public static final Integer FILE_GEU_BACK = 14;// geu_back
    public static final Integer FILE_XGU_BACK = 15;// xgu_back
    public static final Integer FILE_BOOTROM_BACK = 16;// xgu_back
    public static final Integer DIR_ONU = 17;// dir_onu
    public static final Integer FILE_CONFIG = 18;// config
    public static final Integer DIR_LOG = 19;// log
    public static final Integer FILE_MPUB = 20;// mpub
    public static final Integer FILE_MPUB_BACK = 21;// mpub-back
    public static final Integer FILE_OEM = 22;// oem
    public static final Integer FILE_MEU = 25;// meu
    public static final Integer FILE_MEU_BACK = 26;// meu-back
    public static final Integer DIR_DEFAULT_ROOT = 255;// default
    public static final Integer UNKNOWN_ONU_TYPE = 255;// default
    public static final Integer OTHER_ONU_TYPE = 13100;// default
    public static final Integer FILE_MPUB_BOOTROM = 23;// bootrom
    public static final Integer FILE_MPUB_BOOTROM_BACK = 24;// bootrom
    public static final Integer FILE_MEF = 27;// mpuc
    public static final Integer FILE_MEF_BACK = 28;// mpuc-back

    public static final Integer FILE_MGU = 29;// mgu
    public static final Integer FILE_MGU_BACK = 30;// mgu-back

    // ONU类型
    public static final Map<Integer, String> EPON_ONU_TYPE = new HashMap<Integer, String>();
    static {
        EPON_ONU_TYPE.put(255, "UNKNOWN_ONU");
        EPON_ONU_TYPE.put(33, "PN8621");
        EPON_ONU_TYPE.put(34, "PN8622");
        EPON_ONU_TYPE.put(36, "PN8624");
        EPON_ONU_TYPE.put(37, "PN8625");
        EPON_ONU_TYPE.put(38, "PN8626");
        EPON_ONU_TYPE.put(40, "PN8628");
        EPON_ONU_TYPE.put(48, "PN8630");
        EPON_ONU_TYPE.put(49, "PN8631");
        EPON_ONU_TYPE.put(65, "PN8641");
        EPON_ONU_TYPE.put(68, "PN8644");
        EPON_ONU_TYPE.put(71, "PN8647");
        EPON_ONU_TYPE.put(81, "PN8651");
        EPON_ONU_TYPE.put(82, "PN8652");
        EPON_ONU_TYPE.put(83, "PN8653");
        EPON_ONU_TYPE.put(84, "PN8654");
        EPON_ONU_TYPE.put(241, "CC8800A");
    }

    public static final Integer CMC_ONLINE_STATUS = 4;

    // 光功率阀值中操作符
    public static final int OPERATOR_GREATER = 1;
    public static final int OPERATOR_GREATERANDEQUAL = 2;
    public static final int OPERATOR_EQUAL = 3;
    public static final int OPERATOR_LESSANDEQUAL = 4;
    public static final int OPERATOR_LESS = 5;

    // 告警级别
    public static final int ALERT_LEVEL_GENERAL = 2;
    public static final int ALERT_LEVEL_MINOR = 3;
    public static final int ALERT_LEVEL_MAIN = 4;
    public static final int ALERT_LEVEL_SERIOUS = 5;
    public static final int ALERT_LEVEL_EMERGENCY = 6;

    // cmc SUB TYPE
    public static final Long CMC_A = 30001L;
    public static final Long CMC_C_A = 30005L;
    public static final Long CMC_E = 30013L;
    public static final Long CMC_C_E = 30014L;
    public static final Long CMC_D_E = 30015L;
    public static final Long CMC_C10G = 30021L;
    public static final Long CMC_F = 30023L;

    public static final List<Long> CMC_CA_List = new ArrayList<Long>();
    static {
        CMC_CA_List.add(CMC_A);
        CMC_CA_List.add(CMC_C_A);
        CMC_CA_List.add(CMC_E);
        CMC_CA_List.add(CMC_C_E);
        CMC_CA_List.add(CMC_D_E);
        CMC_CA_List.add(CMC_C10G);
        CMC_CA_List.add(CMC_F);
    }

    // E型设备
    public static final int EPON_CC_TYPE_E = 0xF4;
    public static final int EPON_CC_TYPE_CE = 0xF6;
    public static final int EPON_CC_TYPE_DE = 0xF7;
    public static final int EPON_CC_TYPE_10G = 0xF8;
    public static final int EPON_CC_TYPE_F = 0xF9;

    public static final Map<Integer, Long> SUB_CMC_TYPE = new HashMap<Integer, Long>();
    static {
        SUB_CMC_TYPE.put(EPON_CC_TYPE_E, CMC_E);
        SUB_CMC_TYPE.put(EPON_CC_TYPE_CE, CMC_C_E);
        SUB_CMC_TYPE.put(EPON_CC_TYPE_DE, CMC_D_E);
        SUB_CMC_TYPE.put(EPON_CC_TYPE_10G, CMC_C10G);
        SUB_CMC_TYPE.put(EPON_CC_TYPE_F, CMC_F);
    }
    public static final Map<String, Integer> SUB_CMC_STR_TYPE = new HashMap<String, Integer>();
    static {
        SUB_CMC_STR_TYPE.put("880E", EponConstants.EPON_CC_TYPE_E);
        SUB_CMC_STR_TYPE.put("880F", EponConstants.EPON_CC_TYPE_F);
        SUB_CMC_STR_TYPE.put("88CE", EponConstants.EPON_CC_TYPE_CE);
        SUB_CMC_STR_TYPE.put("88DE", EponConstants.EPON_CC_TYPE_DE);
    }

    // 标识onu类型中的CC类型
    public static final List<Integer> CC_ONUFLAG_TYPE = new ArrayList<Integer>();
    static {
        CC_ONUFLAG_TYPE.add(EPON_CC_TYPE_INTEGER_A);
        CC_ONUFLAG_TYPE.add(EPON_CC_TYPE_E);
        CC_ONUFLAG_TYPE.add(EPON_CC_TYPE_CE);
        CC_ONUFLAG_TYPE.add(EPON_CC_TYPE_DE);
        CC_ONUFLAG_TYPE.add(EPON_CC_TYPE_10G);
        CC_ONUFLAG_TYPE.add(EPON_CC_TYPE_F);
    }
    // 未取到类型标识
    public static final String EPON_TYPE_UNKNOWN = "Unknown Type";

    // 标识设备未关联阈值告警
    public static final int TEMPLATE_ENTITY_UNLINK = -1;
    // 性能阈值告警关闭
    public static final int PERF_PTHRESHOLD_OFF = 0;

    public static final long NO_ALERT = -1L;

    // 标识onu类型中的E型设备
    public static final List<Integer> C_E_TYPES = new ArrayList<Integer>();
    static {
        C_E_TYPES.add(EPON_CC_TYPE_E);
        C_E_TYPES.add(EPON_CC_TYPE_CE);
        C_E_TYPES.add(EPON_CC_TYPE_DE);
        C_E_TYPES.add(EPON_CC_TYPE_10G);
        C_E_TYPES.add(EPON_CC_TYPE_F);
    }

    // 模块名称
    public static final String MODULE_CMTS = "cmts";

    // 上行信道调制模板类型
    public static final String[] UP_MODULATION_PROFILETYPES = { "", "QPSK-Fair-Scdma", "QAM16-Fair-Scdma",
            "QAM64-Fair-Scdma", "QAM256-Fair-Scdma", "QPSK-Good-Scdma", "QAM16-Good-Scdma", "QAM64-Good-Scdma",
            "QAM256-Good-Scdma", "QAM64-Best-Scdma", "QAM256-Best-Scdma", "QPSK-Atdma", "QAM16-Atdma", "QAM64-Atdma",
            "QAM256-Atdma", "QAM64-Lowlatency-Scdma", "QAM256-Lowlatency-Scdma", "QAM32-Good-Scdma", "QAM32-Atdma" };
    // 下行信道调制方式
    public static final String[] DOWN_MODULATION_TYPES = { "", "unknown", "QAM1024", "QAM64", "QAM256" };
    // 信道标准
    public static final String[] ANNEXTYPES = { "", "unknown", "other", "annexA", "annexB", "annexC" };

    // UNI VLAN配置方式
    public static final int UNIVLAN_PROFILECONFIG = 1; // ONU业务模板配置
    public static final int UNIVLAN_HANDLECONFIG = 2; // 离散配置

    public static final String VLAN_MODE_TAG = "tag";
    public static final String VLAN_MODE_TRANSLATION = "translation";
    public static final String VLAN_MODE_AGGREGATION = "aggregation";
    public static final String VLAN_MODE_TRUNK = "trunk";
    public static final String VLAN_MODE_STACKING = "stacking";

    public static final int TRANSPARENT_MODE = 0;
    public static final int TAG_MODE = 1;
    public static final int TRANSLATION_MODE = 2;
    public static final int AGGREGATION_MODE = 3;
    public static final int TRUNK_MODE = 4;
    public static final int STACKING_MODE = 5;
    public static final long TX_POWER = -2147483648L;
    public static final long RE_POWER = -2147483648L;
    public static final long OPT_TEMP = -2147483648L;
    public static final long OPT_CURRENT = 0L;
    public static final long OPT_VOLTAGE = 0L;
    public static final Float INVALID_VALUE = null;

    // SNI端口类型
    public static final Map<Integer, String> SNI_TYPE_MAP = new HashMap<Integer, String>();
    static {
        SNI_TYPE_MAP.put(SNI_PORT_TYPE_GECOPPER, "GE");
        SNI_TYPE_MAP.put(SNI_PORT_TYPE_GEFIBER, "GE");
        SNI_TYPE_MAP.put(SNI_PORT_TYPE_XEFIBER, "XE");
    }
    // SNI端口下发实际类型
    public static final Map<Integer, String> SNI_ACTURETYPE_MAP = new HashMap<Integer, String>();
    static {
        SNI_ACTURETYPE_MAP.put(SNI_PORT_TYPE_GEPORT, "GE");
        SNI_ACTURETYPE_MAP.put(SNI_PORT_TYPE_XEPORT, "XE");
    }

    // ONU WAN连接 IP模式
    public static final int ONU_WANCONNNECT_IPMODE_BRIGE = 0;
    public static final int ONU_WANCONNNECT_IPMODE_DHCP = 1;
    public static final int ONU_WANCONNNECT_IPMODE_STATIC = 2;
    public static final int ONU_WANCONNNECT_IPMODE_PPPOE = 3;

    // ONU WAN连接 WAN模式
    public static final int ONU_WANCONNNECT_WANMODE_ROUTER = 1;
    public static final int ONU_WANCONNNECT_WANMODE_BRIDGE = 2;

    public static final String EPON_ONU = "E";

    public static final int UPGRADE_ONU = 1024;
    public static final int UPGRADE_ONU_MAX = 2048;
    public static final int UPGRADE_PORT = 128;
    public static final int UPGRADE_PORT_MAX = 256;

    public static Integer onuPreTypeFromEquipmentID(String onuEquipmentID) {
        Integer onuType = 255;
        try {
            if (onuEquipmentID != null && !onuEquipmentID.equalsIgnoreCase("")) {
                if (onuEquipmentID.startsWith("88")) {
                    if (SUB_CMC_STR_TYPE.containsKey(onuEquipmentID)) {
                        onuType = SUB_CMC_STR_TYPE.get(onuEquipmentID);
                    }
                } else {
                    if (onuEquipmentID.contains("PN")) {
                        onuType = Integer.parseInt(
                                onuEquipmentID.substring(onuEquipmentID.length() - 2, onuEquipmentID.length()), 16);
                    }
                }
            }
        } catch (Exception e) {

        }
        return onuType;
    }

    public static final int PERF_TYPE_OLT_ETH = 0;
    public static final int PERF_TYPE_OLT_PON = 1;
    public static final int PERF_TYPE_ONU_PON = 2;
    public static final int PERF_TYPE_ONU_UNI = 3;
    public static final int PERF_TYPE_OLT_TEMP = 4;
    public static final int PERF_TYPE_ONU_TEMP = 5;
    public static final int PERF_TYPE_OLT_GPON = 6;
    public static final int PERF_TYPE_ONU_GPON = 7;
    public static final int PERF_TYPE_OLT_CPU = 8;
    public static final int PERF_TYPE_MPLS_L2VPN = 9;
    public static final int PERF_TYPE_ONU_GEMPORT = 10;
    public static final int NO_ROGUE_ONU = 0;
    public static final int IS_ROGUE_ONU = 1;

    // 标识CC告警类型
    public static final List<Integer> CC_ALERT_TYPE = new ArrayList<Integer>();
    static {
        CC_ALERT_TYPE.add(-8);
        CC_ALERT_TYPE.add(-50002);

    }

}
