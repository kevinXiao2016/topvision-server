/***********************************************************************
 * $Id: SniVlanDao.java,v1.0 2013-10-25 上午11:42:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.dao;

import java.util.List;

import com.topvision.ems.epon.domain.PonCvidModeRela;
import com.topvision.ems.epon.domain.PonSvidModeRela;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidOnuQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTrunkRule;
import com.topvision.ems.epon.vlan.domain.VlanQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTransparentRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-10-25-上午11:42:09
 *
 */
public interface PonPortVlanDao extends BaseEntityDao<Object> {

    /**
     * 获取转换列表
     * 
     * @param ponId
     *            PON口ID
     * @return List<VlanTranslationRule>
     */
    List<VlanTranslationRule> getTransList(Long ponId);

    /**
     * 添加转换规则
     * 
     * @param vlanTranslationRule
     *            VLAN转换规则
     */
    void addTransRule(VlanTranslationRule vlanTranslationRule);

    /**
     * 删除转换规则
     * 
     * @param ponId
     *            PON口ID
     * @param vlanId
     *            VLAN ID
     */
    void deleteTransRule(Long ponId, Integer vlanId);

    /**
     * 获取聚合列表
     * 
     * @param ponId
     *            PON口ID
     * @return List<VlanAggregationRule>
     */
    List<VlanAggregationRule> getAggrList(Long ponId);

    /**
     * 添加聚合规则
     * 
     * @param vlanAggregationRule
     *            VLAN聚合规则
     */
    void addSvlanAggrRule(VlanAggregationRule vlanAggregationRule);

    /**
     * 删除聚合规则
     * 
     * @param ponId
     *            PON口ID
     * @param vlanId
     *            VLAN的ID
     */
    void deleteSvlanAggrRule(Long ponId, Integer vlanId);

    /**
     * 修改（添加、删除CVLAN）聚合规则
     * 
     * @param vlanAggregationRule
     *            VLAN聚合规则
     */
    void modifyCvlanAggrRule(VlanAggregationRule vlanAggregationRule);

    /**
     * 获取Trunk列表
     * 
     * @param ponId
     *            PON口ID
     * @return VlanTrunkRule
     */
    VlanTrunkRule getTrunkList(Long ponId);

    /**
     * 删除Trunk规则
     * 
     * @param ponId
     *            PON口ID
     */
    void deleteTrunkRule(Long ponId);

    /**
     * 修改（添加(非第一次添加)、删除(非最后一条的删除)Trunk）Trunk规则
     * 
     * @param vlanTrunkRule
     *            VLAN Trunk规则
     */
    void modifyTrunkRule(VlanTrunkRule vlanTrunkRule);

    /**
     * 添加Trunk规则
     * 
     * @param vlanTrunkRule
     *            VLAN Trunk规则
     */
    void addTrunkRule(VlanTrunkRule vlanTrunkRule);

    /**
     * 获取QinQ列表
     * 
     * @param ponId
     *            PON口ID
     * @return List<VlanQinQRule>
     */
    List<VlanQinQRule> getQinQList(Long ponId);

    /**
     * 添加QinQ规则
     * 
     * @param vlanQinQRule
     *            VLAN QinQ规则
     */
    void addQinQRule(VlanQinQRule vlanQinQRule);

    /**
     * 删除QinQ规则
     * 
     * @param ponId
     *            PON口ID
     * @param startVlanId
     *            Start VLAN ID
     * @param endVlanId
     *            End VLAN ID
     */
    void deleteQinQRule(Long ponId, Integer startVlanId, Integer endVlanId);

    /**
     * loadVLAN 透传数据
     * 
     * @param entityId
     * @param ponId
     * @return
     */
    VlanTransparentRule loadTransparentData(Long entityId, Long ponId);

    /**
     * 添加VLAN透传
     * 
     * @param vlanTransparentRule
     */
    void addTransparentRule(VlanTransparentRule vlanTransparentRule);

    /**
     * 删除VLAN透传
     * 
     * @param vlanTransparentRule
     */
    void delTransparentRule(VlanTransparentRule vlanTransparentRule);

    /**
     * 修改VLAN透传
     * 
     * @param vlanTransparentRule
     */
    void modifyTransparentRule(VlanTransparentRule vlanTransparentRule);

    /**
     * 批量更新PON口VLAN转换规则
     * 
     * @param list
     */
    void batchInsertOltVlanTranslation(List<VlanTranslationRule> list, Long entityId);

    /**
     * 批量更新PON口VLAN trunk规则
     * 
     * @param list
     */
    void batchInsertOltVlanTrunk(List<VlanTrunkRule> list, Long entityId);

    /**
     * 批量更新PON口VLAN聚合规则
     * 
     * @param list
     */
    void batchInsertOltVlanAgg(List<VlanAggregationRule> list, Long entityId);

    /**
     * 批量更新VLAN透传
     * 
     * @param list
     * @param entityId
     */
    void batchInsertOltVlanTransparent(List<VlanTransparentRule> list, Long entityId);

    /**
     * 批量更新PON口QinQ规则
     * 
     * @param list
     */
    void batchInsertOltPonQinQ(List<VlanQinQRule> list, Long entityId);

    /**
     * 批量更新PON口VLAN转换规则（基于LLID）
     * 
     * @param list
     */
    void batchInsertTopVlanTrans(List<VlanLlidTranslationRule> list, Long entityId);

    /**
     * 批量更新PON口VLAN trunk规则（基于LLID）
     * 
     * @param list
     */
    void batchInsertTopVlanTrunk(List<VlanLlidTrunkRule> list, Long entityId);

    /**
     * 批量更新PON口VLAN聚合规则（基于LLID）
     * 
     * @param list
     */
    void batchInsertTopVlanAgg(List<VlanLlidAggregationRule> list, Long entityId);

    /**
     * 批量更新PON口QinQ规则（基于LLID）
     * 
     * @param list
     */
    void batchInsertTopVlanQinQ(List<VlanLlidQinQRule> list, Long entityId);

    /**
     * 
     * 批量更新PON口QinQ规则（基于LLID,适配TL1）
     * 
     * @param qinQRules
     * @param entityId
     */
    void batchInsertTopVlanOnuQinQ(List<VlanLlidOnuQinQRule> qinQRules, Long entityId);

    /**
     * 获取SVID与VLAN模式关系
     * 
     * @param ponId
     *            PON口ID
     * @return List<PonSvidModeRela>
     */
    List<PonSvidModeRela> getPonSvidModeRela(Long entityId, Long ponId);

    /**
     * 获取CVID与VLAN模式关系
     * 
     * @param ponId
     *            PON口ID
     * @return List<PonCvidModeRela>
     */
    List<PonCvidModeRela> getPonCvidModeRela(Long ponId);

    /**
     * 批量插入PON口VLAN数据
     * 
     * @param list
     * @param entityId
     */
    void batchInsertPonVlan(List<PortVlanAttribute> list, Long entityId);

    /**
     * 通过entityId和ponIndex获得ponId
     * 
     * @param entityId
     * @param ponIndex
     * @return
     */
    Long getPonIdByPonIndex(Long entityId, Long ponIndex);

    /**
     * 获取PON口VLAN配置
     * 
     * @param ponId
     * @return
     */
    PortVlanAttribute queryPonVlan(Long ponId);

    /**
     * 更新PON口PVID配置
     * 
     * @param ponId
     * @param pvid
     */
    void updatePonPvid(Long ponId, Integer pvid);

    /**
     * 插入或者更新PON口VLAN基本信息
     * 
     * @param ponVlan
     */
    void insertOrUpdatePonVlan(PortVlanAttribute ponVlan);

}
