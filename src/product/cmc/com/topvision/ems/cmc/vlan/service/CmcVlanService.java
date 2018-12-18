/***********************************************************************
 * $Id: CmcVlanService.java,v1.0 2013-4-23 下午02:55:41 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.vlan.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanCfgEntry;
import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanScalarObject;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVifSubIpEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanConfigEntry;

/**
 * @author lzt
 * @created @2013-4-23 下午02:55:41
 * 
 */
public interface CmcVlanService {

    /**
     * 通过CmcId 查询 CCMTS VLAN全局配置
     * 
     * @param cmcId Long
     * @return CmcIpSubVlanScalarObject
     */
    CmcIpSubVlanScalarObject getCmcIpSubVlanScalarById(Long cmcId);

    /**
     * 更新 CCMTS VLAN全局配置
     * 
     * @param cmcId 
     * @param topCcmtsIpSubVlanCfi 
     * @param topCcmtsIpSubVlanTpid 
     */
    void modifyCmcIpSubVlanScalar(Long cmcId, Integer topCcmtsIpSubVlanCfi, String topCcmtsIpSubVlanTpid);

    /**
     * 加载CCMTS子网VLAN配置
     * 
     * @param cmcId 
     */
    List<CmcIpSubVlanCfgEntry> getCmcIpSubVlanCfgList(Map<String, Object> queryMap);

    /**
     * 获得CCMTS子网VLAN配置
     * 
     * @param cmcId 
     */
    CmcIpSubVlanCfgEntry getCmcIpSubVlanCfg(Long cmcId, String cmcVlanIpIndex, String cmcVlanIpMaskIndex);

    /**
     * 新增CCMTS子网VLAN配置
     * 
     * @param cmcIpSubVlanCfgEntry 
     */
    void addCmcIpSubVlanCfg(Long cmcId, String cmcSubVlanIp, String cmcSubVlanMask, Integer cmcSubVlanVlanId,
            Integer cmcSubVlanPri);

    /**
     * 修改CCMTS子网VLAN配置
     * 
     * @param cmcIpSubVlanCfgEntry 
     */
    void modifyCmcIpSubVlanCfg(Long cmcId, String cmcVlanIpIndex, String cmcVlanIpMaskIndex, Integer cmcSubVlanPri);

    /**
     * 删除CCMTS子网VLAN配置
     * 
     * @param cmcIpSubVlanCfgEntry 
     */
    void deleteCmcIpSubVlanCfg(Long cmcId, String cmcVlanIpIndex, String cmcVlanIpMaskIndex);

    /**
     * 刷新CCMTS子网VLAN配置
     * 
     * @param cmdId 
     */
    void refreshCmcIpSubVlanCfg(Long cmcId);

    /**
     * 获取CCMTS VLAN列表
     * 
     * @param cmdId 
     */
    List<CmcVlanConfigEntry> getCmcVlanList(Long cmcId);

    /**
     * 新增CCMTS VLAN
     * 
     * @param cmdId 
     * @param topCcmtsVlanIndex
     */
    CmcVlanConfigEntry getCmcVlanCfgById(Long cmcId, Integer topCcmtsVlanIndex);

    /**
     * 新增CCMTS VLAN
     * 
     * @param cmdId 
     * @param topCcmtsVlanIndex
     */
    void addCmcVlan(Long cmcId, Integer topCcmtsVlanIndex);

    /**
     * 删除CCMTS VLAN
     * 
     * @param cmdId 
     * @param topCcmtsVlanIndex
     */
    void deleteCmcVlan(Long cmcId, Integer topCcmtsVlanIndex);

    /**
     * 刷新CCMTS VLAN
     * 
     * @param cmdId 
     */
    void refreshCmcVlan(Long cmcId);

    /**
     * 查询VLAN0的IP地址
     * 
     * @param entityId
     * @return
     */
    List<CmcVifSubIpEntry> getCmcVlanListFromVlan0(Long entityId);

    /**
     * 新增CCMTS VLAN 主IP
     * 
     * @param cmdId 
     * @param topCcmtsVlanIndex 
     * @param cmcVlanIp
     * @param cmcVlanMask
     */
    void addCmcVlanPriIp(Long cmcId, Integer topCcmtsVlanIndex, String cmcVlanIp, String cmcVlanMask);

    /**
     * 查询CCMTS VLAN 虚接口IP是否存在
     * 
     * @param cmdId 
     * @param cmcVlanIp
     * @param cmcVlanMask
     */
    Boolean checkCmcVlanIpExist(Long cmcId, String cmcVlanIp, String cmcVlanMask);

    /**
     * 修改CCMTS VLAN 主IP地址
     * 
     * @param cmcId
     * @param topCcmtsVlanIndex
     * @param cmcVlanIp
     * @param cmcVlanMask
     */
    void modifyCmcVlanPriIp(Long cmcId, Integer topCcmtsVlanIndex, String cmcVlanIp, String cmcVlanMask);

    /**
     * 新增CCMTS VLAN 从IP
     * 
     * @param cmdId 
     * @param topCcmtsVlanIndex 
     * @param cmcVlanIp
     * @param cmcVlanMask
     */
    void addCmcVlanSecIp(Long cmcId, Integer topCcmtsVlanIndex, String cmcVlanIp, String cmcVlanMask);

    /**
     * 修改CCMTS VLAN 从IP
     * 
     * @param cmdId 
     * @param topCcmtsVlanIndex 
     * @param secVidIndex 
     * @param cmcVlanIp
     * @param cmcVlanMask
     */
    void modifyCmcVlanSecIp(Long cmcId, Integer topCcmtsVlanIndex, Integer secVidIndex, String cmcVlanIp,
            String cmcVlanMask);

    /**
     * 删除CCMTS VLAN 主IP
     * 
     * @param cmdId 
     * @param topCcmtsVlanIndex 
     */
    void deleteCmcVlanPriIp(Long cmcId, Integer topCcmtsVlanIndex);

    /**
     * 删除CCMTS VLAN 从IP
     * 
     * @param cmdId 
     * @param topCcmtsVifSubIpVlanIdx 
     * @param topCcmtsVifSubIpSeqIdx
     */
    void deleteCmcVlanSecIp(Long cmcId, Integer topCcmtsVifSubIpVlanIdx, Integer topCcmtsVifSubIpSeqIdx);

    /**
     * 删除CCMTS VLAN 所有从IP
     * 
     * @param cmdId 
     * @param topCcmtsVifSubIpVlanIdx 
     */
    void deleteAllCmcVlanSubIpByVlan(Long cmcId, Integer topCcmtsVifSubIpVlanIdx);

    /**
     * 更新CCMTS VLAN 主IP Dhcp配置
     * 
     * @param cmdId 
     * @param topCcmtsVlanIndex 
     * @param dhcpAlloc
     * @param option60
     */
    void updateCmcVlanPriIpDhcpCfg(Long cmcId, Integer topCcmtsVlanIndex, Integer dhcpAlloc, String option60);

    /**
     * 从设备获取VLAN配置
     * @param cmcId
     */
    void refreshCmcVlanConfigFromDevice(Long cmcId);

    List<CmcVlanConfigEntry> getVlanConfigList(Long entityId);
}
