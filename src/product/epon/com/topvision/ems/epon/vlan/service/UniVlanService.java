/***********************************************************************
o * $Id: SniVlanService.java,v1.0 2013-10-25 下午4:33:36 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.service;

import java.util.List;

import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.framework.service.Service;

/**
 * @author huqiao
 * @created @2011-10-24-19:01:45
 */
public interface UniVlanService extends Service {
    /**
     * 通过uniId获得uni口VLAN的基本属性
     * 
     * @param uniId
     *            uni端口Id
     * @return PortVlanAttribute 端口Vlan基本属性
     */
    PortVlanAttribute getUniVlanAttribute(Long uniId);

    void updateUniVlanAttribute(PortVlanAttribute portVlanAttribute);

    /**
     * 通过uniId获得uni端口转换规则列表
     * 
     * @param uniId
     *            uni端口Id
     * @return List<VlanTranslationRule> vlan转换规则列表
     */
    List<VlanTranslationRule> getVlanTranslationRuleList(Long uniId);

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
     * @param uniId
     *            uni端口Id
     * @param vlanIndex
     *            vlan索引
     */
    void deleteVlanTranslationRule(VlanTranslationRule vlanTranslationRule);

    /**
     * 删除DB一条转换规则
     * 
     * @param uniId
     *            uni端口Id
     * @param vlanIndex
     *            vlan索引
     */
    void deleteVlanTransDB(Long uniId, Integer vlanIndex);

    /**
     * 删除一条转换规则
     * 
     * @param uniId
     *            uni端口Id
     * @param vlanIndex
     *            vlan索引
     * @param newVlan
     *            转换后vlan索引
     */
    void deleteVlanTranslationRuleByAfter(Long uniId, Integer vlanIndex, Integer newVlan);

    /**
     * 通过uniId获得uni端口聚合规则列表
     * 
     * @param uniId
     *            uni端口Id
     * @return
     */
    List<VlanAggregationRule> getVlanAggregationRuleList(Long uniId);

    /**
     * 增加一条聚合规则
     * 
     * @param VlanAggregationRule
     *            端口VLAN聚合规则
     */
    void addSVlanAggregationRule(VlanAggregationRule vlanAggregationRule);

    /**
     * 删除一条聚合规则
     * 
     * @param uniId
     *            uni端口Id
     * @param portAggregationVidIndex
     *            聚合vlan索引
     */
    void deleteSVlanAggregationRule(VlanAggregationRule vlanAggregationRule);

    /**
     * 删除DB一条聚合规则
     * 
     * @param uniId
     *            uni端口Id
     * @param portAggregationVidIndex
     *            聚合vlan索引
     */
    void deleteSVlanAggrDB(Long uniId, Integer portAggregationVidIndex);

    /**
     * 修改（新增）一条CVLAN规则
     * 
     * @param vlanAggregationRule
     */
    void modifyCVlanAggregationRule(VlanAggregationRule vlanAggregationRule);

    /**
     * 通过entityId和uniID获得uni端口Trunk规则列表
     * 
     * @param entityId
     *            设备Id
     * @param uniId
     *            uni端口Id
     * @return
     */
    VlanTrunkRule getVlanTrunkRules(Long uniId);

    /**
     * 增加一条Trunk规则
     * 
     * @param VlanTrunkRule
     *            端口VLANTrunk规则
     */
    void updateUniVlanTrunkRule(VlanTrunkRule vlanTrunkRule);

    /**
     * 刷新单个端口Trunk规则
     * 
     * @param VlanTrunkRule
     *            端口VLANTrunk规则
     */
    //void refreshSingleUniVlanTrunkRule(VlanTrunkRule vlanTrunkRule);

    /**
     * 更新设备UNI口VLAN基本属性
     * 
     * @param snmpParam
     */
    void refreshUniPortVlanAttribute(Long entityId);

    /**
     * 更新单个UNI端口VLAN基本属性
     * 
     * @param snmpParam
     */
    void refreshSingleUniVlanAttribute(Long entityId, Long uniId);

    /**
     * 更新设备UNI口 转换规则
     * 
     * @param snmpParam
     */
    void refreshUniTranslationRule(Long entityId);

    /**
     * 更新单个UNI端口 转换规则
     * 
     * @param snmpParam
     */
    void refreshSingleUniTranslationRule(Long entityId, Long uniId);

    /**
     * 更新设备UNI口 Trunk规则
     * 
     * @param snmpParam
     */
    void refreshUniTrunkRule(Long entityId);

    /**
     * 更新单个UNI口 Trunk规则
     * 
     * @param snmpParam
     */
    void refreshSingleUniTrunkRule(Long entityId, Long uniId);

    /**
     * 更新设备UNI口 聚合规则
     * 
     * @param snmpParam
     */
    void refreshUniAggRule(Long entityId);

    /**
     * 更新单个UNI口 聚合规则
     * 
     * @param snmpParam
     */
    void refreshSingleUniAggRule(Long entityId, Long uniId);

    void copyUniVlanAttribute(Long entityId, Long sourceIndex, Long uniIndex);

    List<PortVlanAttribute> getOnuPortVlanList(Long onuId);

}
