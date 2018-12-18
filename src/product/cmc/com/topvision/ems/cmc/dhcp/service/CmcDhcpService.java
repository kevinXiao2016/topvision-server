/***********************************************************************
 * $Id: CmcDhcpService.java,v1.0 2012-2-13 下午02:27:29 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.dhcp.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBaseConfig;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpBundle;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpGiAddr;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpIntIp;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpOption60;
import com.topvision.ems.cmc.dhcp.facade.domain.CmcDhcpServerConfig;
import com.topvision.framework.service.Service;

/**
 * DHCP功能
 * 
 * @author zhanglongyang
 * @created @2012-2-13-下午02:27:29
 * 
 */
public interface CmcDhcpService extends Service {
    /**
     * 获取DHCP Bundle
     * 
     * @param map Map<String, Object>
     * @return CmcDhcpBundle
     */
    CmcDhcpBundle getCmcDhcpBundle(Map<String, Object> map);

    /**
     * 获取DHCP Bundle 列表
     *
     * @param map Map<String, Object>
     * @return List<CmcDhcpBundle>
     */
    List<CmcDhcpBundle> getCmcDhcpBundleList(Map<String, Object> map);

    /**
     * 删除一条DHCP Bundle
     *
     * @param map Map<String, Object>
     */
    void deleteDhcpBundle(Map<String, Object> map);

    /**
     * 添加一条DHCP Bundle
     * 
     * @param cmcDhcpBundle CmcDhcpBundle
     */
    void addDhcpBundle(CmcDhcpBundle cmcDhcpBundle);

    /**
     * 修改一条DHCP Bundle
     * 
     * @param cmcDhcpBundle CmcDhcpBundle
     */
    void modifyDhcpBundle(CmcDhcpBundle cmcDhcpBundle);

    /**
     * 获取DHCP Server 列表
     * 
     * @param map Map<String, Object>
     * @return List<CmcDhcpServerConfig>
     */
    List<CmcDhcpServerConfig> getCmcDhcpServerList(Map<String, Object> map);

    /**
     * 删除一条DHCP Server
     * 
     * @param map Map<String, Object>
     */
    void deleteDhcpServer(Map<String, Object> map);

    /**
     * 添加一条DHCP Server
     * 
     * @param cmcDhcpServerConfig   CmcDhcpServerConfig
     */
    void addDhcpServer(CmcDhcpServerConfig cmcDhcpServerConfig);

    /**
     * 修改一条DHCP Server
     * 
     * @param cmcDhcpServerConfig CmcDhcpServerConfig
     */
    void modifyDhcpServer(CmcDhcpServerConfig cmcDhcpServerConfig);

    /**
     * 获取DHCP GiAddr 列表
     * 
     * @param map Map<String, Object>
     * @return List<CmcDhcpGiAddr>
     */
    List<CmcDhcpGiAddr> getCmcDhcpGiAddrList(Map<String, Object> map);

    /**
     * 删除一条DHCP GiAddr
     * 
     * @param map Map<String, Object>
     */
    void deleteDhcpGiAddr(Map<String, Object> map);

    /**
     * 添加一条DHCP GiAddr
     * 
     * @param cmcDhcpGiAddr CmcDhcpGiAddr
     */
    void addDhcpGiAddr(CmcDhcpGiAddr cmcDhcpGiAddr);

    /**
     * 修改一条DHCP GiAddr
     * 
     * @param cmcDhcpGiAddr CmcDhcpGiAddr
     */
    void modifyDhcpGiAddr(CmcDhcpGiAddr cmcDhcpGiAddr);

    /**
     * 获取DHCP IntIp 列表
     * 
     * @param cmcId Long
     * @return List<CmcDhcpIntIp>
     */
    List<CmcDhcpIntIp> getCmcDhcpIntIpList(Long cmcId);

    /**
     * 删除一条DHCP IntIp
     * 
     * @param cmcId Long
     * @param index Integer
     * @param ifIndex Long
     */
    void deleteDhcpIntIp(Long cmcId, Integer index, Long ifIndex);

    /**
     * 添加一条DHCP IntIp
     * 
     * @param cmcDhcpIntIp CmcDhcpIntIp
     */
    void addDhcpIntIp(CmcDhcpIntIp cmcDhcpIntIp);

    /**
     * 修改一条DHCP IntIp
     * 
     * @param cmcDhcpIntIp CmcDhcpIntIp
     */
    void modifyDhcpIntIp(CmcDhcpIntIp cmcDhcpIntIp);

    /**
     * 获取DHCP index的列表
     * 
     * @param cmcId Long
     *            flag
     * @param bundle String
     * @param type Integer
     * @param flag Integer
     * @return List<Integer>
     */
    List<Integer> getCmcDhcpIndexList(Long cmcId, String bundle, Integer type, Integer flag);

    /**
     * 获取DHCP ifIndex的列表
     * 
     * @param cmcId Long
     * @return List<Long>
     */
    List<Long> getCmcDhcpIfIndexList(Long cmcId);

    /**
     * 获取DHCP Option60 列表
     * 
     * @param map Map<String, Object>
     * @return List<CmcDhcpOption60>
     */
    List<CmcDhcpOption60> getCmcDhcpOption60List(Map<String, Object> map);

    /**
     * 删除一条DHCP Option60
     *
     * @param map Map<String, Object>
     */
    void deleteDhcpOption60(Map<String, Object> map);

    /**
     * 添加一条DHCP Option60
     * 
     * @param cmcDhcpOption60 CmcDhcpOption60
     */
    void addDhcpOption60(CmcDhcpOption60 cmcDhcpOption60);

    /**
     * 修改一条DHCP Option60
     * 
     * @param cmcDhcpOption60 CmcDhcpOption60
     */
    void modifyDhcpOption60(CmcDhcpOption60 cmcDhcpOption60);

    /**
     * 刷新cc Dhcp信息
     * 
     * @param cmcId Long
     */
    void refreshDhcpInfo(Long cmcId);

    /**
     * 修改DHCP Relay基本配置
     * 
     * @param cmcDhcpBaseConfigInfo CmcDhcpBaseConfig
     */
    void modifyCmcDhcpRelayBaseInfo(CmcDhcpBaseConfig cmcDhcpBaseConfigInfo);

    /**
     * 刷新DHCP Relay基本配置
     * 
     * @param cmcId Long
     * @return CmcDhcpBaseConfig
     */
    CmcDhcpBaseConfig refreshCmcDhcpBaseConfig(Long cmcId);

    /**
     * 获取DHCP Index flag取1时为DhcpServer flag取2时为DhcpGiAddr flag取3时为DhcpOption60
     * 
     * @param cmcId Long
     * @param bundle String
     * @param type Integerg
     * @param flag Integer
     * @return Integer
     */
    Integer getCcmtsDhcpIndex(Long cmcId, String bundle, Integer type, Integer flag);

}
