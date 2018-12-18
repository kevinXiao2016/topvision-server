/***********************************************************************
 * $Id: CmcConfigService.java,v1.0 2012-2-13 下午02:26:44 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.config.service;

import java.util.List;

import com.topvision.ems.cmc.config.facade.domain.CmcEmsConfig;
import com.topvision.ems.cmc.config.facade.domain.CmcSnmpCommunityTable;
import com.topvision.ems.cmc.config.facade.domain.CmcSysConfig;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.cmc.sni.facade.domain.CcmtsSniObject;
import com.topvision.ems.cmc.vlan.domain.CmcPrimaryVlan;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryInterface;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryIp;
import com.topvision.ems.performance.domain.Monitor;
import com.topvision.framework.service.Service;

/**
 * 配置功能
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午02:26:44
 * 
 */
public interface CmcConfigService extends Service {
    /**
     * 轮询设置
     * 
     * @param cmcId Long
     * @return Monitor
     */
    Monitor updateCmcPollingConfig(Long cmcId);

    /**
     * 获取CMC基本配置信息
     * 
     * @param cmcId Long
     * @return CmcSystemBasicInfo
     */
    CmcSystemBasicInfo getCmcBasicInfo(Long cmcId);

    /**
     * 获取CMC IP配置信息
     * 
     * @param entityId Long
     * @return CmcSystemIpInfo
     */
    CmcSystemIpInfo getCmcIpInfo(Long entityId);
    
    /**
     * 修改8800B IP配置信息
     * 
     * @param cmcSystemIpInfo
     */
    void setCmcIpInfo(CmcSystemIpInfo cmcSystemIpInfo);
    

    /**
     * 获取CMC EMS配置信息
     * 
     * @param cmcId Long
     * @return List<CmcEmsConfig>
     */
    List<CmcEmsConfig> getCmcEmsList(Long cmcId);

    /**
     * 修改基本配置信息
     * 
     * @param cmcId Long
     *            name location contact
     * @param cmcName String
     * @param location String
     * @param cmcContact String
     */
    void modifyCmcBasicInfo(Long cmcId, String cmcName, String location, String cmcContact);

    /**
     * 修改IP配置信息
     * 
     * @param cmcId Long
     * @param ipList String
     */
    void modifyCmcIpInfo(Long cmcId, String ipList);

    /**
     * 设置主备优先模式
     * 
     * @param cmcId Long
     * @param physicalInterfaceMode Integer
     */
    void modifyPhysicalInterfacePreferredMode(Long cmcId, Integer physicalInterfaceMode);

    /**
     * 设置主物理口传输模式
     * 
     * @param cmcId Long
     * @param transmissionMode Integer
     */
    void modifyMainTransmissionMode(Long cmcId, Integer transmissionMode);

    /**
     * 设置备用物理口传输模式
     * 
     * @param cmcId Long
     * @param transmissionMode Integer
     */
    void modifyBackupTransmissionMode(Long cmcId, Integer transmissionMode);

    /**
     * 设置CMC EMS配置信息
     * 
     * @param entityId Long
     * @param cmcEmsConfig CmcEmsConfig
     * @param insertFlag boolean
     */
    void modifyCmcEms(Long entityId, CmcEmsConfig cmcEmsConfig, boolean insertFlag);

    /**
     * 设置loglevel动作
     * 
     * @param docsDevEvControl DocsDevEvControl
     */
    void modifyDeviceEventControl(DocsDevEvControl docsDevEvControl);

    /**
     * 获取CC的vlan primary
     * @param cmcId Long
     * @return  CmcVlanPrimaryInterface
     */
    CmcVlanPrimaryInterface getCC8800BVlanPriInterface(Long cmcId);

    /**
     * 获取CC的vlan primary Ip
     * @param cmcId Long
     * @return List<CmcVlanPrimaryIp>
     */
    List<CmcVlanPrimaryIp> getCC8800BVlanPriIpList(Long cmcId);

    /**
     * 根据cmcId和VlanId获取CC的vlan primary Ip
     * @param cmcId Long
     * @param vlanId Integer
     * @return CmcPrimaryVlan
     */
    CmcPrimaryVlan getCC8800BCcPrimaryVlanAsSnmp(Long cmcId, Integer vlanId);

    /**
     * 获取CC的DHCP config
     * @param cmcId Long
     * @return CmcDhcpBaseConfig
     */
    CmcDhcpBaseConfig getCC8800BCmcDhcpBaseConfig(Long cmcId);

    /**
     * 获取CC的基本信息
     * @param cmcId Long
     * @param productType Integer
     * @return CmcAttribute
     */
    CmcAttribute refreshCmcAttribute(Long cmcId, Integer productType);

    /**
     * 获取CC8800B的SNMP信息（指定的主VLAN做SNMP）
     * @param cmcId Long
     * @param productType  Integer
     * @return CmcPrimaryVlan
     */
    CmcPrimaryVlan refreshCmcPrimaryVlanAsSnmp(Long cmcId, Integer productType);

    /**
     * 获取8800B的上联口链路配置信息
     * 
     * @param cmcId Long
     *            CMC ID
     * @return CMC 8800B 上联口配置
     */
    CcmtsSniObject getCC8800BSniConfig(Long cmcId);

    /**
     * 设置8800B上联口链路
     * 
     * @param sni
     *            上联口配置信息
     */
    void setCC8800BSniConfig(CcmtsSniObject sni);
    
    /**
     * 获得8800B的SNMP基本信息
     * 
     * @param entityId
     * @return
     */
    CmcSnmpCommunityTable getCmcSnmpCommunityTable(Long entityId);
    
    /**
     * 设置8800B的SNMP基本信息
     * 
     * @param entityId
     * @param readCommunity
     * @param writeCommunity
     */
    void setCC8800BSnmpInfo(Long entityId, String readCommunity, String writeCommunity);
    
    /**
     * 设置8800B的网络基本信息
     * 
     * @param entityId
     * @param info
     */
    boolean setCC8800BSystemIpInfo(Long entityId, CmcSystemIpInfo info);
    
    /**
     * 设置8800B网关
     * 
     * @param entityId
     * @param info
     */
    boolean setCC8800BGateway(Long entityId, CmcSystemIpInfo info);
    
    /**
     * 获取CCMTS设备的piggyback开关设置
     * @param cmcId
     * @return
     */
    CmcSysConfig getCmcSysConfig(Long cmcId);
    
    /**
     * 修改piggyBack开关
     * @param cmcId
     * @param piggyBack
     */
    void modifyCmcSysPiggyBack(Long cmcId, Integer piggyBack);
    
    

}
