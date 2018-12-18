/***********************************************************************
 * $Id: OltRealtimeFacade.java,v1.0 2014-7-12 上午9:52:00 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.facade;

import java.util.List;

import com.topvision.ems.epon.realtime.domain.*;
import com.topvision.ems.facade.Facade;
import com.topvision.ems.facade.domain.DeviceBaseInfo;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2014-7-12-上午9:52:00
 *
 */
@EngineFacade(serviceName = "OltRealtimeFacade", beanName = "oltRealtimeFacade")
public interface OltRealtimeFacade extends Facade {

    /**
     * 从设备获取Olt基本信息
     * @param snmpParam
     * @return
     */
    OltBaseInfo getOltBaseInfo(SnmpParam snmpParam);

    /**
     * 从设备获取Olt板卡信息
     * @param entityId
     * @return
     */
    List<OltSlotInfo> getOltSoltInfo(SnmpParam snmpParam);

    /**
     * 从设备获取Pon口统计信息
     * @param entityId
     * @return
     */
    List<OltPonTotalInfo> getPonTotalInfo(SnmpParam snmpParam);

    /**
     * 从设备获取OLT SNI口信息
     * @param entityId
     * @return
     */
    List<OltSniInfo> getOltSniInfo(SnmpParam snmpParam);

    /**
     * 从设备获取下级设备统计信息
     * @param entityId
     * @return
     */
    List<OltSubTotalInfo> getSubTotalInfo(SnmpParam snmpParam);

    /**
     * 从设备获取下级设备统计信息
     * @param entityId
     * @return
     */
    List<OltGponSubTotalInfo> getGponSubTotalInfo(SnmpParam snmpParam);

    /**
     * 从设备获取Pon口详细信息
     * @param entityId
     * @return
     */
    List<OltPonInfo> getOltPonInfo(SnmpParam snmpParam);

    /**
     * 从设备获取下级设备详细信息
     * @param entityId
     * @return
     */
    List<OltSubDeviceInfo> getOltSubInfo(SnmpParam snmpParam);

    /**
     * 从设备获取下级设备详细信息
     * @param entityId
     * @return
     */
    List<OltSubDeviceGponInfo> getOltSubGponInfo(SnmpParam snmpParam);

    List<OltSubDeviceEponInfo> getOltSubEponInfo(SnmpParam snmpParam);

    /**
     * 从设备获取OLT下的CM统计信息
     * @param entityId
     * @return
     */
    OltCmTotalInfo getCmTotalInfo(SnmpParam snmpParam);

    /**
     * 获取端口速率信息
     * @param snmpParam
     * @param speedInfo
     * @return
     */
    OltPortSpeedInfo getPortSpeedInfo(SnmpParam snmpParam, OltPortSpeedInfo speedInfo);

    /**
     * 获取端口信息
     * 同时获取多条
     * @param snmpParam
     * @param speedInfoList
     * @return
     */
    List<OltPortSpeedInfo> getSpeedInfoList(SnmpParam snmpParam, List<OltPortSpeedInfo> speedInfoList);

    /**
     * 从设备获取基于信道统计的Cm数量
     * @param snmpParam
     * @return
     */
    List<ChannelCmNumInfo> getChannelCmNum(SnmpParam snmpParam);

    /**
     * 获取Olt下A/C-A信息
     * @param snmpParam
     * @return
     */
    List<OltCurrentCmcInfo> getOltCurrentCmcInfo(SnmpParam snmpParam);

    /**
     * 获取下级设备的发送/接收光功率
     * @param snmpParam
     * @return
     */
    List<OltSubOpticalInfo> getOltSubOpticalInfo(SnmpParam snmpParam);

    /**
     * 从设备获取设备基本信息
     * 内容全部来自标准MIB
     * @param snmpParam
     * @return
     */
    DeviceBaseInfo getDeviceBaseInfo(SnmpParam snmpParam);

}
