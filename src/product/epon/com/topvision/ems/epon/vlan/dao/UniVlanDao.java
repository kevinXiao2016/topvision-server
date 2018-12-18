/***********************************************************************
 * $Id: SniVlanDao.java,v1.0 2013-10-25 上午11:42:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.dao;

import java.util.List;

import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2011-9-19-下午01:59:50
 * 
 */
public interface UniVlanDao extends BaseEntityDao<Object> {

    /**
     * 批量更新uni口vlan基本信息
     * 
     * @param list
     */
    void batchInsertOnuPortVlan(List<PortVlanAttribute> list, Long entityId);

    /**
     * 批量更新uni口vlan转换规则
     * 
     * @param list
     */
    void batchInsertOnuVlanTranslation(List<VlanTranslationRule> list, Long entityId);

    /**
     * 批量更新单个uni口vlan转换规则
     * 
     * @param list
     */
    void batchInsertUniVlanTranslation(List<VlanTranslationRule> list, Long uniId);

    /**
     * 批量更新uni口vlan trunk规则
     * 
     * @param list
     */
    void batchInsertOnuVlanTrunk(List<VlanTrunkRule> list, Long entityId);

    /**
     * 批量更新单个uni口vlan聚合规则
     * 
     * @param list
     */
    void batchInsertUniVlanAgg(List<VlanAggregationRule> list, Long uniId);

    /**
     * 批量更新uni口vlan聚合规则
     * 
     * @param list
     */
    void batchInsertOnuVlanAgg(List<VlanAggregationRule> list, Long entityId);

    /**
     * 通过uniId获得uni口VLAN的基本属性
     * 
     * @param uniId
     *            uni端口Id
     * @return PortVlanAttribute 端口VLAN属性
     */
    PortVlanAttribute getPortVlanAttribute(Long uniId);

    /**
     * 更新uni口VLAN基本属性
     * 
     * @param portVlanAttribute
     *            端口VLAN属性
     */
    void updatePortVlanAttribute(PortVlanAttribute portVlanAttribute);

    /**
     * 通过uniId获得uni端口转换规则列表
     * 
     * @param uniId
     *            uni端口Id
     * @return List<VlanTranslationRule> 转换规则列表
     */
    List<VlanTranslationRule> getVlanTranslationRuleList(Long uniId);

    /**
     * 通过uniId和vlanIndex获得uni端口转换规则
     * 
     * @param uniId
     *            uni端口Id
     * @param vlanIndex
     *            转换前VLAN id
     * @return VlanTranslationRule 转换规则对象
     */
    VlanTranslationRule getVlanTranslationRule(Long uniId, Integer vlanIndex);

    /**
     * 通过uniId和newVlan获得uni端口转换规则
     * 
     * @param uniId
     *            uni端口Id
     * @param vlanIndex
     *            转换前VLAN id
     * @param newVlan
     *            转换后VLAN id
     * @return VlanTranslationRule 转换规则对象
     */
    VlanTranslationRule getVlanTranslationRuleByAfter(Long uniId, Integer vlanIndex, Integer newVlan);

    /**
     * 增加一条转换规则
     * 
     * @param vlanTranslationRule
     *            端口VLAN转换规则
     */
    void addVlanTranslationRule(VlanTranslationRule vlanTranslationRule);

    /**
     * 删除一条转换规则
     * 
     * @param vlanTranslationRule
     *            端口VLAN转换规则
     */
    void deleteVlanTranslationRule(VlanTranslationRule vlanTranslationRule);

    /**
     * 通过uniId获得uni端口聚合规则列表
     * 
     * @param uniId
     *            uni端口Id
     * @return List<VlanAggregationRule> 聚合规则列表
     */
    List<VlanAggregationRule> getVlanAggregationRuleList(Long uniId);

    /**
     * 通过uniId和vlanIndex获得uni端口聚合规则
     * 
     * @param uniId
     *            uni端口Id
     * @param vlanIndex
     *            vlanId
     * @return VlanAggregationRule 聚合规则对象
     */
    VlanAggregationRule getVlanAggregationRule(Long uniId, Integer vlanIndex);

    /**
     * 增加一条聚合规则
     * 
     * @param VlanAggregationRule
     *            端口VLAN聚合规则
     */
    void addVlanAggregationRule(VlanAggregationRule vlanAggregationRule);

    /**
     * 修改一条聚合规则
     * 
     * @param VlanAggregationRule
     *            端口VLAN聚合规则
     */
    void modifyCVlanAggregationRule(VlanAggregationRule vlanAggregationRule);

    /**
     * 修改（删除）一条聚合规则
     * 
     * @param VlanAggregationRule
     *            端口VLAN聚合规则
     */
    void deleteVlanAggregationRule(VlanAggregationRule vlanAggregationRule);

    /**
     * 通过uniId获得uni端口Trunk规则列表
     * 
     * @param uniId
     *            uni端口Id
     * @return
     */
    VlanTrunkRule getVlanTrunkRules(Long uniId);

    /**
     * 增加UNI口Trunk规则
     * 
     * @param VlanTrunkRule
     *            端口VLANTrunk规则
     */
    void addVlanTrunkRule(VlanTrunkRule vlanTrunkRule);

    /**
     * 更新UNI口Trunk规则
     * 
     * @param VlanTrunkRule
     *            端口VLANTrunk规则
     */
    void updateVlanTrunkRule(VlanTrunkRule vlanTrunkRule);

    /**
     * 删除UNI口Trunk规则
     * 
     * @param uniId
     *            uni端口id
     */
    void deleteVlanTrunkRule(Long uniId);

    List<PortVlanAttribute> getOnuPortVlanList(Long onuId);
}
