/***********************************************************************
 * $Id: CmcConfigDao.java,v1.0 2012-2-13 下午04:18:05 $
 *
 * @author: zhanglongyang
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.config.dao;

import java.util.List;

import com.topvision.ems.cmc.config.facade.domain.CmcEmsConfig;
import com.topvision.ems.cmc.config.facade.domain.CmcSnmpCommunityTable;
import com.topvision.ems.cmc.config.facade.domain.CmcSysConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.facade.domain.CmcSystemBasicInfo;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.cmc.sni.facade.domain.CcmtsSniObject;
import com.topvision.ems.cmc.vlan.domain.CmcPrimaryVlan;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryInterface;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryIp;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * 配置相关功能
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午04:18:05
 */
public interface CmcConfigDao extends BaseEntityDao<CmcEntity> {

    /**
     * 获得CMC的基本配置属性
     * 
     * @param cmcId
     *            设备ID
     * @return
     */
    CmcSystemBasicInfo getCmcBasicInfo(Long cmcId);

    /**
     * 获得CMC的IP配置属性
     * 
     * @param cmcId
     *            设备ID
     * @return
     */
    CmcSystemIpInfo getCmcIpInfo(Long cmcId);

    /**
     * 修改基本配置信息
     * 
     * @param cmcId
     *            name location contact
     */
    void updateCmcConfigBasicInfo(Long cmcId, String cmcName, String location, String cmcContact);

    /**
     * 修改IP配置信息
     * 
     * @param
     */
    void updateCmcConfigIpInfo(CmcSystemIpInfo ipInfo);

    /**
     * 修改网关配置
     * 
     * @param
     */
    void updateCmcConfigGateway(CmcSystemIpInfo ipInfo);

    /**
     * 获得CMC的EMS配置属性
     * 
     * @param cmcId
     *            设备ID
     * @return
     */
    List<CmcEmsConfig> getCmcEmsList(Long cmcId);

    /**
     * 设置物理端口主备状态
     */
    void modifyPhysicalInterfacePreferredMode(Long cmcId, Integer physicalInterfaceMode);

    /**
     * 设置主物理口传输模式
     */
    void modifyMainTransmissionMode(Long cmcId, Integer transmissionMode);

    /**
     * 设置备用物理口传输模式
     */
    void modifyBackupTransmissionMode(Long cmcId, Integer transmissionMode);

    /**
     * 插入CMC EMS配置信息
     */
    void insertCmcEmsConfig(CmcEmsConfig cmcEmsConfig);

    /**
     * 修改CMC EMS配置信息
     */
    void updateCmcEmsConfig(CmcEmsConfig cmcEmsConfig);

    /**
     * 插入事件等级控制信息
     */
    void insertDevEvControl(DocsDevEvControl docsDevEvControl);

    /**
     * 修改事件等级控制信息
     */
    void updateDevEvControl(DocsDevEvControl docsDevEvControl);

    /**
     * 获取CC的vlan primary
     */
    CmcVlanPrimaryInterface getCC8800BVlanPriInterface(Long cmcId);

    /**
     * 获取CC的vlan primary Ip
     */
    List<CmcVlanPrimaryIp> getCC8800BVlanPriIpList(Long cmcId);

    /**
     * 获取CC的DHCP config
     */
    CmcDhcpBaseConfig getCC8800BCmcDhcpBaseConfig(Long cmcId);

    /**
     * 根据cmcId和VlanId获取CC的VLAN
     */
    CmcPrimaryVlan getCC8800BCcPrimaryVlanAsSnmp(Long cmcId, Integer vlanId);

    /**
     * 根据cmcId和VlanId获取CC的vlan primary Ip
     */
    CmcVlanPrimaryIp getCC8800BVlanPriIpByCmcIdAndVId(Long cmcId, Integer vlanId);

    /**
     * 更新某个ip类信息
     */
    void updateCC8800BCcPrimaryVlan(CmcVlanPrimaryIp cmcVlanPrimaryIp);

    /**
     * 获取8800B的上联口链路配置信息
     * 
     * @param cmcId
     *            CMC ID
     * @return CMC 8800B 上联口配置
     * @author huangdongsheng
     */
    CcmtsSniObject getCC8800BSniConfig(Long cmcId);

    /**
     * 更新8800B上联口链路
     * 
     * @param sni
     *            上联口配置信息
     * @author huangdongsheng
     */
    void updateCC8800BSniConfig(CcmtsSniObject sni);

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
     * @param snmpCommunityTable
     */
    void updateCC8800BSnmpInfo(CmcSnmpCommunityTable snmpCommunityTable);

    /**
     * 获取CCMTS设备的piggyback开关设置
     * @param cmcId
     * @return
     */
    CmcSysConfig selectCmcSysConfig(Long cmcId);

    /**
     * 插入系统配置
     * 
     * @param cmcId
     * @param cmcSysConfig
     */
    void insertCmcSysConfig(Long cmcId, CmcSysConfig cmcSysConfig);

    /**
     * 插入或更新CC的vlan primary
     */
    void batchInsertOrUpdateCC8800BVlanPriInterface(final CmcVlanPrimaryInterface cmcVlanPrimaryInterface, Long cmcId);

    /**
     * 插入或更新CC的vlan primary Ip
     */
    void batchInsertOrUpdateCC8800BVlanPriIpList(final List<CmcVlanPrimaryIp> cmcVlanPrimaryIpList, Long cmcId);

}
