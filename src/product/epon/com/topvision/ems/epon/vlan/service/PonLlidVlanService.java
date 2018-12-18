/***********************************************************************
 * $Id: SniVlanService.java,v1.0 2013-10-25 下午4:33:36 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.service;

import java.util.List;

import com.topvision.ems.epon.vlan.domain.VlanLlidAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidOnuQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTrunkRule;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-下午4:33:36
 *
 */
public interface PonLlidVlanService extends Service {

    /**
     * 获取当前PON口下在线ONU MAC地址
     * 
     * @param ponId
     *            PON口ID
     * 
     * @return List<String>
     */
    List<String> getOnuMacAddress(Long ponId);

    /**
     * 获取转换列表(基于LLID)
     * 
     * @param ponId
     *            PON口ID
     * @param mac
     *            ONU MAC地址
     * @return List<VlanLlidTranslationRule>
     */
    List<VlanLlidTranslationRule> getLlidTransList(Long ponId, String mac);

    /**
     * 添加转换规则(基于LLID)
     * 
     * @param vlanLlidTranslationRule
     *            LLID VLAN转换规则
     */
    void addLlidTransRule(VlanLlidTranslationRule vlanLlidTranslationRule);

    /**
     * 删除转换规则(基于LLID)
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            PON口ID
     * @param mac
     *            ONU MAC地址
     * @param vlanId
     *            VLAN ID
     */
    void deleteLlidTransRule(Long entityId, Long ponId, String mac, Integer vlanId);

    /**
     * 获取聚合规则列表（基于LLID）
     * 
     * @param ponId
     *            PON口ID
     * @param mac
     *            ONU MAC地址
     * @return List<VlanLlidAggregationRule>
     */
    List<VlanLlidAggregationRule> getLlidAggrList(Long ponId, String mac);

    /**
     * 添加SVLAN聚合规则（基于LLID）
     * 
     * @param vlanLlidAggregationRule
     *            VLAN聚合规则
     */
    void addLlidSvlanAggrRule(VlanLlidAggregationRule vlanLlidAggregationRule);

    /**
     * 删除SVLAN聚合规则（基于LLID）
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            PON口ID
     * @param mac
     *            ONU MAC地址
     * @param vlanId
     *            VLAN ID
     */
    void deleteLlidSvlanAggrRule(Long entityId, Long ponId, String mac, Integer vlanId);

    /**
     * 删除、添加CVLAN聚合规则（即修改聚合规则）(基于LLID)
     * 
     * @param vlanLlidAggregationRule
     *            LLID VLAN聚合规则
     */
    void modifyLlidCvlanAggrRule(VlanLlidAggregationRule vlanLlidAggregationRule);

    /**
     * 获取Trunk列表(基于LLID)
     * 
     * @param ponId
     *            PON口ID
     * @param mac
     *            ONU MAC地址
     * @return VlanLlidTrunkRule
     */
    VlanLlidTrunkRule getLlidTrunkList(Long ponId, String mac);

    /**
     * 删除、添加Trunk规则（即修改Trunk规则）
     * 
     * @param vlanLlidTrunkRule
     *            VLAN Trunk规则
     */
    void modifyLlidTrunkRule(VlanLlidTrunkRule vlanLlidTrunkRule);

    /**
     * 获取QinQ列表(基于LLID)
     * 
     * @param ponId
     *            PON口ID
     * @param mac
     *            ONU MAC地址
     * @return List<VlanLlidQinQRule>
     */
    List<VlanLlidQinQRule> getLlidQinQList(Long ponId, String mac);

    /**
     * 添加QinQ规则(基于LLID)
     * 
     * @param vlanLlidQinQRule
     *            LLID VLAN QinQ规则
     */
    void addLlidQinQRule(VlanLlidQinQRule vlanLlidQinQRule);

    /**
     * 添加QinQ规则(基于LLID),适用TL1
     * 
     * @param onuQinQRule
     */
    void addLlidOnuQinQRule(VlanLlidOnuQinQRule onuQinQRule);

    /**
     * 删除QinQ规则(基于LLID)
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            PON口ID
     * @param mac
     *            ONU MAC地址
     * @param startVlanId
     *            Start VLAN ID
     * @param endVlanId
     *            End VLAN ID
     */
    void deleteLlidQinQRule(Long entityId, Long ponId, String mac, Integer startVlanId, Integer endVlanId);

    /**
     * 删除QinQ规则(基于LLID，适配TL1)
     * 
     * @param entityId
     * @param onuId
     * @param userVlan
     * @param userVlan2
     */
    void deleteLlidOnuQinQRule(Long entityId, Long onuId, Integer startVlanId, Integer endVlanId);

    /**
     * 从设备重新获取VLAN的LLID列表数据
     * 
     * @param entityId
     *            设备ID
     * 
     */
    void refreshVlanLlidListFromOlt(Long entityId, Long ponId);

}
