/***********************************************************************
 * $Id: SniVlanService.java,v1.0 2013-10-25 下午4:33:36 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.service;

import java.util.List;

import com.topvision.ems.epon.domain.PonCvidModeRela;
import com.topvision.ems.epon.domain.PonSvidModeRela;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTransparentRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.framework.service.Service;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author huqiao
 * @created @2011-10-24-19:01:45
 */
public interface PonPortVlanService extends Service {
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
     * @param entityId
     *            设备ID
     * @param ponId
     *            PON口ID
     * @param beforeTransVid
     *            转换前VLAN ID
     */
    void deleteTransRule(Long entityId, Long ponId, Integer beforeTransVid);

    /**
     * 获取聚合规则列表
     * 
     * @param ponId
     *            PON口ID
     * @return List<VlanAggregationRule>
     */
    List<VlanAggregationRule> getAggrList(Long ponId);

    /**
     * 添加SVLAN聚合规则
     * 
     * @param vlanAggregationRule
     *            VLAN聚合规则
     */
    void addSvlanAggrRule(VlanAggregationRule vlanAggregationRule);

    /**
     * 删除SVLAN聚合规则
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            PON口ID
     * @param afterAggrVid
     *            聚合后VLAN ID
     */
    void deleteSvlanAggrRule(Long entityId, Long ponId, Integer afterAggrVid);

    /**
     * 删除、添加CVLAN聚合规则（即修改聚合规则）
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
     * 删除、添加Trunk规则（即修改Trunk规则）
     * 
     * @param vlanTrunkRule
     *            VLAN Trunk规则
     */
    void modifyTrunkRule(VlanTrunkRule vlanTrunkRule);

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
     *            VLANQinQ规则
     */
    void addQinQRule(VlanQinQRule vlanQinQRule);

    /**
     * 删除QinQ规则
     * 
     * @param entityId
     *            设备ID
     * @param ponId
     *            PON口ID
     * @param startVlanId
     *            Start VLAN ID
     * @param endVlanId
     *            End VLAN ID
     */
    void deleteQinQRule(Long entityId, Long ponId, Integer startVlanId, Integer endVlanId);

    VlanTransparentRule loadTransparentData(Long entityId, Long ponId);

    void addTransparentRule(VlanTransparentRule vlanTransparentRule);

    void delTransparentRule(VlanTransparentRule vlanTransparentRule);

    void modifyTransparentRule(VlanTransparentRule vlanTransparentRule);

    /**
     * 获取CVID与VLAN模式关系
     * 
     * @param ponId
     *            PON口ID
     * @return List<PonCvidModeRela>
     */
    List<PonCvidModeRela> getPonCvidModeRela(Long ponId);

    /**
     * 获取SVID与VLAN模式关系
     * 
     * @param ponId
     *            PON口ID
     * @return List<PonSvidModeRela>
     */
    List<PonSvidModeRela> getPonSvidModeRela(Long entityId, Long ponId);

    /**
     * 从设备重新获取VLAN数据
     * 
     * @param entityId
     *            设备ID
     * 
     */
    void refreshVlanDataFromOlt(Long entityId);

    /**
     * 更新PON口 转换规则
     * 
     * @param snmpParam
     */
    void refreshPonTranslation(SnmpParam snmpParam);

    /**
     * 更新PON口 Trunk规则
     * 
     * @param snmpParam
     */
    void refreshPonTrunk(SnmpParam snmpParam);

    /**
     * 更新PON口 聚合规则
     * 
     * @param snmpParam
     */
    void refreshPonAgg(SnmpParam snmpParam);

    /**
     * 更新PON口 QinQ规则
     * 
     * @param snmpParam
     */
    void refreshPonQinQ(SnmpParam snmpParam);

    /**
     * 更新PON口 转换规则(基于LLID)
     * 
     * @param snmpParam
     */
    void refreshPonLlidTranslation(SnmpParam snmpParam);

    /**
     * 更新PON口 Trunk规则(基于LLID)
     * 
     * @param snmpParam
     */
    void refreshPonLlidTrunk(SnmpParam snmpParam);

    /**
     * 更新PON口 聚合规则(基于LLID)
     * 
     * @param snmpParam
     */
    void refreshPonLlidAgg(SnmpParam snmpParam);

    /**
     * 更新PON口 QinQ规则(基于LLID)
     * 
     * @param snmpParam
     */
    void refreshPonLlidQinQ(SnmpParam snmpParam);
    
    /**
     * 更新PON口 QinQ规则(基于LLID,适配TL1)
     * 
     * @param snmpParam
     */
    void refreshPonLlidOnuQinQ(SnmpParam snmpParam);

    /**
     * 更新PON口Transparent规则
     * @param snmpParam
     */
    void refreshPonTransparent(SnmpParam snmpParam);

    /**
     * 获取PON口VLAN配置
     * @param ponId
     * @return
     */
    PortVlanAttribute getPonVlan(Long ponId);

    /**
     * 更新PON口PVID配置
     * @param ponVlan
     */
    void updatePonPvid(PortVlanAttribute ponVlan);

    /**
     * 刷新PON口VLAN基本信息
     * @param entityId
     * @param ponId
     * @param portIndex
     */
    void refreshPonVlanInfo(Long entityId, Long ponId, Long portIndex);

}
