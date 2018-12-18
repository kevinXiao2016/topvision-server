/***********************************************************************
 * $Id: CmcVlanDao.java,v1.0 2013-4-23 上午11:16:19 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.vlan.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanCfgEntry;
import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanScalarObject;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVifSubIpEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanConfigEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanDhcpAllocEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryIp;

/**
 * @author lzt
 * @created @2013-4-23 上午11:16:19
 * 
 */
public interface CmcVlanDao {

    /**
     * 新增 CCMTS VLAN全局配置
     * 
     * @param obj
     */
    void addCmcIpSubVlanScalar(CmcIpSubVlanScalarObject obj);

    /**
     * 通过CmcId 查询 CCMTS VLAN全局配置
     * 
     * @param cmcId
     * @return CmcIpSubVlanScalarObject
     */
    CmcIpSubVlanScalarObject getCmcIpSubVlanScalarById(Long cmcId);

    /**
     * 更新CCMTS VLAN全局配置
     * 
     * @param obj
     */
    void updateCmcIpSubVlanScalar(CmcIpSubVlanScalarObject obj);

    /**
     * 更新CCMTS VLAN全局配置
     * 
     * @param obj
     */
    void deleteCmcIpSubVlanScalar(CmcIpSubVlanScalarObject obj);

    /**
     * 获得CCMTS 所有子网VLAN配置
     * 
     * @param cmcId
     * @return List<CmcIpSubVlanCfgEntry>
     */
    List<CmcIpSubVlanCfgEntry> getCmcIpSubVlanCfgList(Map<String, Object> queryMap);

    /**
     * 获得CCMTS 子网VLAN配置
     * 
     * @param cmcId
     * @param cmcVlanIpIndex
     * @param cmcVlanIpMaskIndex
     * @return CmcIpSubVlanCfgEntry
     */
    CmcIpSubVlanCfgEntry getCmcIpSubVlanCfg(Long cmcId, String cmcVlanIpIndex, String cmcVlanIpMaskIndex);

    /**
     * 新增CCMTS 子网VLAN配置
     * 
     * @param cmcIpSubVlanCfgEntry
     */
    void addCmcIpSubVlanCfg(CmcIpSubVlanCfgEntry cmcIpSubVlanCfgEntry);

    /**
     * 更新CCMTS 子网VLAN配置
     * 
     * @param cmcIpSubVlanCfgEntry
     */
    void updateCmcIpSubVlanCfgEntry(CmcIpSubVlanCfgEntry cmcIpSubVlanCfgEntry);

    /**
     * 删除CCMTS 单个子网VLAN配置
     * 
     * @param cmcId
     * @param cmcVlanIpIndex
     * @param cmcVlanIpMaskIndex
     */
    void deleteCmcIpSubVlanCfg(Long cmcId, String cmcVlanIpIndex, String cmcVlanIpMaskIndex);

    /**
     * 删除CCMTS 所有子网VLAN配置
     * 
     * @param cmcId
     */
    void deleteAllCmcIpSubVlanCfg(Long cmcId);

    /**
     * 刷新CCMTS 子网VLAN配置
     * 
     * @param cmcId
     * @param list
     */
    void refreshCmcIpSubVlanCfg(Long cmcId, List<CmcIpSubVlanCfgEntry> list);

    /**
     * 获得CCMTS VLAN列表
     * 
     * @param cmcId
     */
    List<CmcVlanConfigEntry> getCmcVlanList(Long cmcId);

    /**
     * 获得CCMTS VLAN列表
     * 
     * @param cmcId
     * @param topCcmtsVlanIndex
     */
    CmcVlanConfigEntry getCmcVlanCfgById(Long cmcId, Integer topCcmtsVlanIndex);

    /**
     * 获得CCMTS VLAN主 IP 列表
     * 
     * @param cmcId
     */
    List<CmcVlanPrimaryIp> getCmcVlanPriIpList(Long cmcId);

    /**
     * 更新CCMTS VLAN主 IP 列表
     * 
     * @param cmcVlanPrimaryIps
     * @param cmcId
     */
    void batchUpdateCmcVlanPriIp(List<CmcVlanPrimaryIp> cmcVlanPrimaryIps, Long cmcId);

    /**
     * 获得CCMTS VLAN从 IP 列表
     * 
     * @param cmcId
     */
    List<CmcVifSubIpEntry> getCmcVlanVisIpList(Long cmcId);

    /**
     * 新增CCMTS VLAN
     * 
     * @param cmcVlan
     */
    void addCmcVlan(CmcVlanConfigEntry cmcVlan);

    /**
     * 删除CCMTS VLAN
     * 
     * @param cmcVlan
     */
    void deleteCmcVlan(CmcVlanConfigEntry cmcVlan);

    /**
     * 删除CCMTS 所有VLAN
     * 
     * @param cmcId
     */
    void deleteAllCmcVlan(Long cmcId);

    /**
     * 刷新CCMTS VLAN
     * 
     * @param cmcVlanConfigEntries
     * @param cmcId
     */
    void batchInsertCmcVlan(List<CmcVlanConfigEntry> cmcVlanConfigEntries, Long cmcId);

    /**
     * 查询VLAN0的IP地址
     * 
     * @param cmcId
     * @return
     */
    List<CmcVifSubIpEntry> getCmcVlanListFromVlan0(Long cmcId);

    /**
     * 更新CCMTS VLAN 主IP
     * 
     * @param cmcVlanPriIp
     */
    void updateCmcVlanPriIp(CmcVlanPrimaryIp cmcVlanPriIp);

    /**
     * 查询CCMTS VLAN 虚接口主IP
     * 
     * @param cmcVlanPriIp
     */
    List<CmcVlanConfigEntry> selectCmcVlanPriIp(Long cmcId, String cmcVlanIp, String cmcVlanIpMask);

    /**
     * 查询CCMTS VLAN 虚接口子IP
     * 
     * @param CmcVifSubIpEntry
     */
    List<CmcVifSubIpEntry> selectCmcVlanSubIp(Long cmcId, String cmcVlanIp, String cmcVlanIpMask);

    /**
     * 新增CCMTS VLAN 从IP
     * 
     * @param cmcVifSubIp
     */
    void addCmcVlanSubIp(CmcVifSubIpEntry cmcVifSubIp);

    /**
     * 修改CCMTS VLAN 从IP
     * 
     * @param cmcVifSubIp
     */
    void updateCmcVlanSubIp(CmcVifSubIpEntry cmcVifSubIp);

    /**
     * 删除CCMTS VLAN 主IP
     * 
     * @param cmcVlanPri
     */
    void deleteCmcVlanPriIp(CmcVlanPrimaryIp cmcVlanPri);

    /**
     * 删除CCMTS VLAN 主IP的所有从IP
     * 
     * @param cmcVlanPri
     * @param vlanId
     */
    void deleteCmcVlanSubIpByPriIp(Long cmcId, Integer vlanId);

    /**
     * 删除CCMTS VLAN 从IP
     * 
     * @param cmcVlanIpSec
     */
    void deleteCmcVlanSubIp(CmcVifSubIpEntry cmcVlanIpSec);

    /**
     * 删除CCMTS VLAN 所有从IP
     * 
     * @param cmcId
     * @param topCcmtsVifSubIpVlanIdx
     */
    void deleteAllCmcVlanSubIpByVlan(Long cmcId, Integer topCcmtsVifSubIpVlanIdx);

    /**
     * 删除CCMTS VLAN 所有从IP
     * 
     * @param cmcId
     */
    void deleteAllCmcVlanSubIp(Long cmcId);

    /**
     * 刷新CCMTS VLAN 所有从IP
     * 
     * @param cmcVifSubIpEntries
     * @param cmcId
     */
    void batchInsertCmcVlanSubIp(Long cmcId, List<CmcVifSubIpEntry> cmcVifSubIpEntries);

    /**
     * 更新CCMTS VLAN 主IP Dhcp配置
     * 
     * @param cmcDhcpAlloc
     */
    void updateCmcVlanDhcpAlloc(CmcVlanDhcpAllocEntry cmcDhcpAlloc);

    /**
     * 刷新CCMTS VLAN 主IP Dhcp配置
     * 
     * @param cmcDhcpAlloc
     * @param cmcId
     */
    void batchUpdateCmcVlanDhcpAlloc(List<CmcVlanDhcpAllocEntry> cmcDhcpAllocs, Long cmcId);

    /**
     * 获得CCMTS VLAN 子IP 存在索引列表
     * 
     * @param cmcId
     */
    List<Integer> getCmcVlanSubIpIndexList(Long cmcId);

}
