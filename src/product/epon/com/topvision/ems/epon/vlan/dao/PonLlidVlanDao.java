/***********************************************************************
 * $Id: SniVlanDao.java,v1.0 2013-10-25 上午11:42:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.dao;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.vlan.domain.VlanLlidAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidOnuQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTrunkRule;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-上午11:42:09
 *
 */
public interface PonLlidVlanDao extends BaseEntityDao<Object> {

    /**
     * 获取当前PON口下在线ONU MAC地址
     * 
     * @param ponId
     *            PON口ID
     * 
     * @return List<Long>
     */
    List<Long> getOnuMacAddress(Long ponId);

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
     * @param ponId
     *            PON口ID
     * @param vlanId
     *            VLAN ID
     * @param mac
     *            ONU MAC地址
     */
    void deleteLlidTransRule(Long ponId, Integer vlanId, String mac);

    /**
     * 获取聚合列表(基于LLID)
     * 
     * @param ponId
     *            PON口ID
     * @param mac
     *            ONU MAC
     * @return List<VlanLlidAggregationRule>
     */
    List<VlanLlidAggregationRule> getLlidAggrList(Long ponId, String mac);

    /**
     * 添加聚合规则(基于LLID)
     * 
     * @param vlanLlidAggregationRule
     *            LLID VLAN聚合规则
     */
    void addLlidSvlanAggrRule(VlanLlidAggregationRule vlanLlidAggregationRule);

    /**
     * 删除聚合规则(基于LLID)
     * 
     * @param ponId
     *            PON口ID
     * @param vlanId
     *            VLAN的ID
     * @param mac
     *            ONU MAC
     */
    void deleteLlidSvlanAggrRule(Long ponId, String mac, Integer vlanId);

    /**
     * 修改（添加、删除CVLAN）聚合规则(基于LLID)
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
     * 删除Trunk规则(基于LLID)
     * 
     * @param ponId
     *            PON口ID
     * @param mac
     *            ONU MAC地址
     */
    void deleteLlidTrunkRule(Long ponId, String mac);

    /**
     * 修改（添加(非第一次添加)、删除(非最后一条的删除)Trunk）Trunk规则
     * 
     * @param vlanLlidTrunkRule
     *            VLAN Trunk规则
     */
    void modifyLlidTrunkRule(VlanLlidTrunkRule vlanLlidTrunkRule);

    /**
     * 添加Trunk规则(基于LLID)
     * 
     * @param vlanLlidTrunkRule
     *            LLID VLAN Trunk规则
     */
    void addLlidTrunkRule(VlanLlidTrunkRule vlanLlidTrunkRule);

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
     * 删除QinQ规则(基于LLID)
     * 
     * @param ponId
     *            PON口ID
     * @param mac
     *            ONU MAC地址
     * @param startVlanId
     *            Start VLAN ID
     * @param endVlanId
     *            End VLAN ID
     */
    void deleteLlidQinQRule(Long ponId, String mac, Integer startVlanId, Integer endVlanId);

    /**
     * 获取Onu的Mac地址和名称
     * 
     * @param ponId
     * @return
     */
    public List<OltOnuAttribute> getOnuMacAndName(Long ponId);

    /**
     * 添加QinQ规则(基于LLID) 适用TL1
     * 
     * @param vlanLlidOnuQinQRule
     */
    void addLlidOnuQinQRule(VlanLlidOnuQinQRule vlanLlidOnuQinQRule);

}
