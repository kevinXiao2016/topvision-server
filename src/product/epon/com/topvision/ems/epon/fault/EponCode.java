/***********************************************************************
 * $Id: EponCode.java,v1.0 2012-1-18 上午09:17:24 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault;

import java.lang.reflect.Field;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.annotation.CodeType;
import com.topvision.platform.zetaframework.var.ZetaValueGetter;

/**
 * @author Victor
 * @created @2012-1-18-上午09:17:24 EPON告警Code处理，通过CodeType来兼容三种告警Code类型
 *          CodeType中的type数组顺序为（内部，广电，电信），通过配置文件设置1,2,3来匹配对应内部Code,广电Code，电信Code
 */
@Service("eponCode")
@ZetaValueGetter("EponCode")
public class EponCode implements java.io.Serializable {
    private static final long serialVersionUID = 7583220061986148350L;
    private static final Logger logger = LoggerFactory.getLogger(EponCode.class);
    private int type = 0;
    @CodeType(type = { 1, 10001, 61441 })
    public static int DEVICE_RESET;// 设备重启
    @CodeType(type = { 2, 100002, 61442 })
    public static int DEVICE_START;// 设备启动
    @CodeType(type = { 3, 100003, 61443 })
    public static int DEVICE_SWITCH;// 主备倒换

    @CodeType(type = { 9, 100009, 61449 })
    public static int FILE_UPLOAD;// 文件上传成功/失败
    @CodeType(type = { 11, 100011, 61451 })
    public static int FILE_DOWNLOAD;// 文件下载成功/失败
    @CodeType(type = { 13, 100013, 61453 })
    public static int SW_SYNC;// 同步软件映像成功/失败
    @CodeType(type = { 15, 100015, 61455 })
    public static int CONFIG_SYNC;// 同步配置文件成功/失败
    @CodeType(type = { 4098, 101, 4098 })
    public static int BD_PROV_FAIL;// 板卡启动自动配置失败
    @CodeType(type = { 4099, 102, 4099 })
    public static int BD_RESET;// 板卡重启
    @CodeType(type = { 4101, 103, 4101 })
    public static int BD_REMOVE;// 板卡被拔出
    @CodeType(type = { 4103, 104, 4103 })
    public static int BD_TEMP_HIGH;// 板卡温度超过上限
    @CodeType(type = { 4107, 105, 4107 })
    public static int BD_FAN_FAIL;// 风扇转动异常
    @CodeType(type = { 4109, 106, 4109 })
    public static int BD_FAN_REMOVE;// 风扇板被拔出
    @CodeType(type = { 4111, 107, 4111 })
    public static int BD_PWR_FAIL;// 电源板异常
    @CodeType(type = { 4113, 108, 4113 })
    public static int BD_PWR_REMOVE;// 电源板被拔出
    @CodeType(type = { 260, 100260, 260 })
    public static int BD_OFFLINE;// 板离线
    @CodeType(type = { 259, 100259, 259 })
    public static int BD_ONLINE;// 板启动正常（板上线）
    @CodeType(type = { 4115, 109, 4115 })
    public static int BD_PONCHIP_ERROR;// PON芯片固件初始化失败
    @CodeType(type = { 4123, 104123, 4123 })
    public static int BD_PERF_EXCEED_HIGH;// 单板性能统计超过上门限
    @CodeType(type = { 4124, 104124, 4124 })
    public static int BD_PERF_EXCEED_LOW;// 单板性能统计低于下门限
    @CodeType(type = { 12289, 201, 12289 })
    public static int PORT_PON_LOOPBACK;// PON口处于环回状态
    @CodeType(type = { 12290, 202, 12290 })
    public static int PORT_SFP_FAIL;// 光模块失效
    @CodeType(type = { 12293, 204, 12293 })
    public static int PORT_PON_DISABLE;// PON口被禁用
    @CodeType(type = { 12294, 12294, 12294 })
    public static int PORT_PON_DISABLE_OK;// PON口禁用恢复
    @CodeType(type = { 12295, 205, 12295 })
    public static int PORT_PON_LLID_EXCD;// PON口注册的LLID数量溢出
    @CodeType(type = { 12299, 206, 12299 })
    public static int PORT_PON_ROGUEONU;// PON端口检测流氓ONU告警
    @CodeType(type = { 12305, 112305, 257 })
    public static int PORT_SFP_RXPWR_HIGH;// SFP接收光功率超过上限
    @CodeType(type = { 12306, 112306, 258 })
    public static int PORT_SFP_RXPWR_LOW;// SFP接收光功率低于下限
    @CodeType(type = { 12307, 112307, 259 })
    public static int PORT_SFP_TXPWR_HIGH;// SFP发送光功率超过上限
    @CodeType(type = { 12308, 112308, 260 })
    public static int PORT_SFP_TXPWR_LOW;// SFP发送光功率低于下限
    @CodeType(type = { 12309, 112309, 261 })
    public static int PORT_SFP_TXBIAS_HIGH;// SFP发送偏执电流超过上限
    @CodeType(type = { 12310, 112310, 262 })
    public static int PORT_SFP_TXBIAS_LOW;// SFP发送偏执电流低于下限
    @CodeType(type = { 12311, 112311, 263 })
    public static int PORT_SFP_VCC_HIGH;// SFP内部供电电压超过上限
    @CodeType(type = { 12312, 112312, 264 })
    public static int PORT_SFP_VCC_LOW;// SFP内部供电电压低于下限
    @CodeType(type = { 12313, 112313, 265 })
    public static int PORT_SFP_TEMP_HIGH;// SFP温度超过上限
    @CodeType(type = { 12314, 112314, 266 })
    public static int PORT_SFP_TEMP_LOW;// SFP温度低于下限
    @CodeType(type = { 12315, 112315, 267 })
    public static int WARN_PORT_SFP_RXPWR_HIGH;// SFP接收功率高于预警上限
    @CodeType(type = { 12316, 112316, 268 })
    public static int WARN_PORT_SFP_RXPWR_LOW;// SFP接收功率低于预警下限
    @CodeType(type = { 12317, 112317, 269 })
    public static int WARN_PORT_SFP_TXPWR_HIGH;// SFP发送功率高于预警上限
    @CodeType(type = { 12318, 112318, 270 })
    public static int WARN_PORT_SFP_TXPWR_LOW;// SFP发送功率低于预警下限
    @CodeType(type = { 12319, 112319, 271 })
    public static int WARN_PORT_SFP_TXBIAS_HIGH;// SFP发送偏执电流超过预警上限
    @CodeType(type = { 12320, 112320, 272 })
    public static int WARN_PORT_SFP_TXBIAS_LOW;// SFP发送偏执电流低于预警下限
    @CodeType(type = { 12321, 112321, 273 })
    public static int WARN_PORT_SFP_VCC_HIGH;// SFP内部供电电压超过预警上限
    @CodeType(type = { 12322, 112322, 274 })
    public static int WARN_PORT_SFP_VCC_LOW;// SFP内部供电电压低于预警下限
    @CodeType(type = { 12323, 112323, 275 })
    public static int WARN_PORT_SFP_TEMP_HIGH;// SFP温度超过预警上限
    @CodeType(type = { 12324, 112324, 276 })
    public static int WARN_PORT_SFP_TEMP_LOW;// SFP温度低于预警下限
    @CodeType(type = { 16385, 301, 16385 })
    public static int ONU_FATAL_ERROR;// ONU严重错误
    @CodeType(type = { 16386, 302, 16386 })
    public static int ONU_KEY_EXCHANGE_ERROR;// ONU密钥交换失败
    @CodeType(type = { 16387, 303, 16387 })
    public static int ONU_OAM_TIMEOUT;// ONU OAM超时
    @CodeType(type = { 16388, 304, 16388 })
    public static int ONU_MAC_AUTH_ERROR;// ONU MAC认证失败
    @CodeType(type = { 17000, 17000, 17000 })
    public static int ONU_MAC_AUTH_SUCCESS; //ONU MAC认证成功(EMS内部实现)
    @CodeType(type = { 16389, 305, 16389 })
    public static int ONU_US_RXPWR_LOW;// ONU上行接收光功率过低
    @CodeType(type = { 16390, 306, 16390 })
    public static int ONU_US_RXPWR_HIGH;// ONU上行接收光功率过高
    @CodeType(type = { 16391, 307, 16391 })
    public static int ONU_DS_RXPWR_LOW;// ONU下行接收光功率过低
    @CodeType(type = { 16392, 308, 16392 })
    public static int ONU_DS_RXPWR_HIGH;// ONU下行接收光功率过高
    @CodeType(type = { 16393, 310, 2 })
    public static int ONU_PWR_DOWN;// ONU断电
    @CodeType(type = { 16394, 311, 16394 })
    public static int ONU_DS_BER_ERROR;// ONU下行比特误码率超过门限
    @CodeType(type = { 16395, 312, 16395 })
    public static int ONU_DS_FER_ERROR;// OONU下行帧差错率超过门限
    @CodeType(type = { 16396, 313, 16396 })
    public static int ONU_US_BER_ERROR;// ONU上行比特误码率超过门限
    @CodeType(type = { 16397, 314, 16397 })
    public static int ONU_US_FER_ERROR;// ONU上行帧差错率超过门限
    @CodeType(type = { 16398, 315, 16398 })
    public static int ONU_PERF_EXCEED_HIGH;// ONU性能统计上限越界
    @CodeType(type = { 16399, 316, 16399 })
    public static int ONU_PERF_EXCEED_LOW;// ONU性能统计下限越界
    @CodeType(type = { 16417, 116417, 1 })
    public static int ONU_EQUMT_ERROR;// ONU内部端口错误
    @CodeType(type = { 16418, 116418, 3 })
    public static int ONU_POWER_ERROR;// ONU供电故障
    @CodeType(type = { 16419, 116419, 4 })
    public static int ONU_BTRY_MISSING;// ONU在使用过程中检测不到电池
    @CodeType(type = { 16420, 116420, 5 })
    public static int ONU_BTRY_VCC_LOW;// ONU电池电压过低
    @CodeType(type = { 16421, 116421, 6 })
    public static int ONU_PANNEL_ALARM;// 物理面板告警（比如ONT门打开）
    @CodeType(type = { 16422, 116422, 7 })
    public static int ONU_SELFTEST_FAIL;// ONU自检失败
    @CodeType(type = { 16423, 116423, 9 })
    public static int ONU_TEMP_HIGH;// ONU温度超过上限
    @CodeType(type = { 16424, 116424, 10 })
    public static int ONU_TEMP_LOW;// ONU温度低于下限
    @CodeType(type = { 16427, 116427, 11 })
    public static int ONU_IAD_CONT_FAIL;// OUN内嵌IAD与SS连接错误
    @CodeType(type = { 16432, 116432, 12 })
    public static int ONU_PROTECT_SWITCH;// PON口倒换（c/d类保护）
    @CodeType(type = { 16433, 116433, 16433 })
    public static int ONU_OFFLINE;// ONU下线
    @CodeType(type = { 16435, 116435, 16435 })
    public static int ONU_FIBER_BREAK;// ONU断纤
    @CodeType(type = { 257, 13001, 257 })
    public static int ONU_REGISTER;// ONU注册
    @CodeType(type = { 258, 13002, 258 })
    public static int ONU_DEREGISTER;// ONU解注册
    @CodeType(type = { 261, 100261, 261 })
    public static int ONU_AUTO_UPGRADE;// ONU自动升级
    @CodeType(type = { 16425, 116425, 16425 })
    public static int ONU_TEMP_OK;// ONU温度恢复正常
    @CodeType(type = { 16434, 116434, 16434 })
    public static int ONU_ONLINE;// ONU上线
    @CodeType(type = { 20481, 401, 20481 })
    public static int ONU_UNI_LINKDOWN;// UNI LINK DOWN
    @CodeType(type = { 20483, 402, 772 })
    public static int ONU_UNI_LOOP;// ONU下挂设备出现环路（电信：以太网端口环回）
    @CodeType(type = { 20485, 403, 769 })
    public static int ONU_UNI_AUTONEG_FAIL;// ONU以太网口自协商失败
    @CodeType(type = { 20487, 120487, 770 })
    public static int ONU_ETH_LOS;// 以太网端口LOS（光口，电口不支持）
    @CodeType(type = { 513, 14001, 513 })
    public static int ONU_UNI_NEW_CBAT_RECOGNIZED;// 发现新CBAT设备
    @CodeType(type = { 514, 14002, 514 })
    public static int ONU_UNI_CBAT_UPDATE;// CBAT设备更新
    @CodeType(type = { 515, 14003, 515 })
    public static int ONU_UNI_CBAT_REMOVED;// 原CBAT设备去除
    @CodeType(type = { 20489, 120489, 771 })
    public static int ONU_ETH_FAIL;// ONU以太网口错误
    @CodeType(type = { 20491, 120491, 773 })
    public static int ONU_ETH_CONGEST;// ONU以太网口阻塞
    @CodeType(type = { 1, 1001, 61441 })
    public static int SYSTEM_RESET;// 系统重启
    @CodeType(type = { 3, 100003, 61443 })
    public static int SYSTEM_HA_SWITCH;// 主备倒换（自动/强制倒换）
    @CodeType(type = { 5, 100005, 61445 })
    public static int SYSTEM_BD_UPGRADE;// 板卡升级
    @CodeType(type = { 7, 100007, 61447 })
    public static int ONU_UPGRADE_OK;// ONU升级
    @CodeType(type = { 4117, 104117, 4117 })
    public static int BD_TYPE_MISMATCH;// 板卡类型不匹配
    @CodeType(type = { 4119, 104119, 4119 })
    public static int BD_SW_MISMATCH;// 当前运行的板卡软件版本不匹配
    @CodeType(type = { 4121, 104121, 4121 })
    public static int BD_SLOT_MISMATCH;// 板槽位错误（主控板校验，接口板点灯）
    @CodeType(type = { 4125, 104125, 4125 })
    public static int BD_CPU_OVERLOAD;// 板CPU负荷异常（目前没配置项）
    @CodeType(type = { 4102, 104102, 4102 })
    public static int BD_INSERT;// 板卡插入
    @CodeType(type = { 4104, 104104, 4104 })
    public static int BD_TEMP_OK;// 板卡温度恢复正常
    @CodeType(type = { 4105, 104105, 4105 })
    public static int BD_TEMP_LOW;// 单板温度过低
    @CodeType(type = { 4127, 104127, 4127 })
    public static int BD_HEARTBEAT_LOST;// 心跳丢失
    @CodeType(type = { 4108, 104108, 4108 })
    public static int BD_FAN_NORMAL;// 风扇状态恢复正常
    @CodeType(type = { 4110, 104110, 4110 })
    public static int BD_FAN_INSERT;// 风扇板插入
    @CodeType(type = { 4112, 104112, 4112 })
    public static int BD_PWR_OK;// 电源板恢复正常
    @CodeType(type = { 4114, 104114, 4114 })
    public static int BD_PWR_INSERT;// 电源板插入
    // 广电标准告警中，up和down对应告警id均为203，此处做特殊处理。
    @CodeType(type = { 8194, 2032, 8194 })
    public static int PORT_LINK_UP;// 端口链路UP
    @CodeType(type = { 8193, 2031, 8193 })
    public static int PORT_LINK_DOWN;// 端口链路DOWN
    @CodeType(type = { 12325, 112325, 12325 })
    public static int PORT_PON_LOS;// PON口光信号LOS告警
    @CodeType(type = { 12326, 112326, 12326 })
    public static int PORT_PON_LOS_RECOVERY;// PON口光信号恢复告警
    @CodeType(type = { 16435, 116435, 16435 })
    public static int ONU_LOS;// ONU光信号LOS告警
    @CodeType(type = { 16437, 322, 16437 })
    public static int ONU_ROGUE;// ONU长发光告警
    @CodeType(type = { 262, 100262, 61702 })
    public static int ONU_ILLEGALREGISTER;// 非法ONU注册告警
    @CodeType(type = { 61703, 161703, 61703 })
    public static int ONU_LOIDCONFLICT;// ONU LOID冲突告警
    @CodeType(type = { 1537, 101537, 62977 })
    public static int IGMP_CALL_CRD;// IGMP呼叫记录
    @CodeType(type = { 16439, 116439, 16439 })
    public static int ONU_DELETE;// ONU解绑定(ONU删除)

    @CodeType(type = { 1016439, 1016439, 1016439 })
    public static int CCMTS_DELETE;// CCMTS解绑定(CCMTS删除)

    @CodeType(type = { 12297, 112297, 12297 })
    public static int PON_OPTICAL_REMOVE;//光模块拔出
    @CodeType(type = { 12298, 112298, 12298 })
    public static int PON_OPTICAL_INSERT;//光模块插入

    @CodeType(type = { 1027, 101027, 1027 })
    public static int GPON_ONU_OFFLINE;//GPON ONU 离线
    @CodeType(type = { 1025, 101025, 1025 })
    public static int GPON_ONU_ONLINE;//GPON ONU 上线

    @CodeType(type = { 1026, 101026, 1026 })
    public static int GPON_ONU_UNAUTH;//GPON ONU 认证失败
    @CodeType(type = { 1028, 101028, 1028 })
    public static int GPON_ONU_CONFLICT;//GPON ONU 冲突
    @CodeType(type = { 1029, 101029, 1029 })
    public static int GPON_ONU_CONF_FAIL;//GPON ONU 配置恢复失败
    @CodeType(type = { 1030, 101030, 1030 })
    public static int GPON_ONU_ACTIVE_OK;//GPON ONU ACTIVE成功
    @CodeType(type = { 1031, 101031, 1031 })
    public static int GPON_ONU_ACTIVE_FAIL;//GPON ONU ACTIVE 失败

    // 在启动时会读取配置文件，设置type 在新建某个OLT或者刷新的时候会根据设备实际读取type
    /**
     * 根据当前OLT告警Code类型设置告警Code值,启动时先注入当前支持Code类型，然后设置对应值
     * 
     */
    @PostConstruct
    public void initialize() {
        Field[] fs = EponCode.class.getFields();
        for (Field f : fs) {
            if (f.getType() != int.class) {
                continue;
            }
            f.setAccessible(true);
            CodeType code = f.getAnnotation(CodeType.class);
            try {
                // 由于重构为内部告警Code 暂时采用0，正确处理方法为根本不需要这一步
                f.set(EponCode.this, code.type()[0]);
            } catch (IllegalArgumentException e) {
                logger.debug("translation", e);
            } catch (IllegalAccessException e) {
                logger.debug("translation", e);
            }
        }
    }

    /**
     * 
     * 
     * @param type
     */
    public static void setEponCode(int type) {
        Field[] fs = EponCode.class.getFields();
        for (Field f : fs) {
            if (f.getType() != int.class) {
                continue;
            }
            f.setAccessible(true);
            CodeType code = f.getAnnotation(CodeType.class);
            try {
                f.set(EponCode.class, code.type()[type - 1]);
            } catch (IllegalArgumentException e) {
                logger.debug("translation", e);
            } catch (IllegalAccessException e) {
                logger.debug("translation", e);
            }
        }
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type) {
        this.type = type;
    }
}
