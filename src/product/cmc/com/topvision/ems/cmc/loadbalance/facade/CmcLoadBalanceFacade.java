/***********************************************************************
 * $Id: CmcLoadBalanceFacade.java,v1.0 2013-4-24 上午10:47:47 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.facade;

import java.util.List;

import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalBasicRule;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalCfg;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalChannel;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalExcludeCm;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalGrp;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalPolicy;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalRestrictCm;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalTopPolicy;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author loyal
 * @created @2013-4-24-上午10:47:47
 * 
 */
@EngineFacade(serviceName = "CmcLoadBalanceFacade", beanName = "cmcLoadBalanceFacade")
public interface CmcLoadBalanceFacade extends Facade {
    /**
     * 修改负载均衡配置
     * 
     * @param cmcLoadBalCfg
     * @param snmpParamm
     */
    void modifyLoadBalConfig(SnmpParam snmpParamm, CmcLoadBalCfg cmcLoadBalCfg);

    /**
     * 新建负载均衡组
     * 
     * @param snmpParam
     * @param docsLoadBalGrpId
     * @return
     */
    Long addLoadBalGroupTable(SnmpParam snmpParam, Long docsLoadBalGrpId);

    /**
     * 删除负载均衡组
     * 
     * @param snmpParam
     * @param docsLoadBalGrpId
     */
    void deleteBalGroupTable(SnmpParam snmpParam, Long docsLoadBalGrpId);

    /**
     * 添加信道到负载均衡组
     * 
     * @param snmpParam
     * @param docsLoadBalGrpId
     * @param docsLoadBalChannelIfIndexs
     */
    void addLoadBalGroupChannelTable(SnmpParam snmpParam, Long docsLoadBalGrpId, List<Long> docsLoadBalChannelIfIndexs);

    /**
     * 从负载均衡组中删除信道
     * 
     * @param snmpParam
     * @param docsLoadBalGrpId
     * @param docsLoadBalChannelIfIndexs
     */
    void deleteLoadBalGroupChannelTable(SnmpParam snmpParam, Long docsLoadBalGrpId,
            List<Long> docsLoadBalChannelIfIndexs);

    /**
     * 添加cm mac 段到负载均衡组
     * 
     * @param snmpParam
     * @param docsLoadBalGrpId
     * @param topLoadBalRestrictCmIndex
     * @param macRang
     * @return 
     */
    CmcLoadBalRestrictCm addLoadBalGroupCmRangsTable(SnmpParam snmpParam, Long docsLoadBalGrpId,
            Long topLoadBalRestrictCmIndex, String macRang);

    /**
     * 修改负载均衡组中mac地址段
     * 
     * @param snmpParam
     * @param docsLoadBalGrpId
     * @param macRangs
     */
    void modifyBalGroupCmRangsTable(SnmpParam snmpParam, Long docsLoadBalGrpId, Long topLoadBalRestrictCmIndex,
            String macRangs);

    /**
     * 从负载均衡组删除cm mac段
     * 
     * @param snmpParam
     * @param docsLoadBalGrpId
     * @param topLoadBalRestrictCmIndexs
     */
    void deleteLoadBalGroupCmRangsTable(SnmpParam snmpParam, Long docsLoadBalGrpId,
            List<Long> topLoadBalRestrictCmIndexs);

    /**
     * 添加排除cm mac 段到CC
     * 
     * @param snmpParam
     * @param ifIndex
     * @param topLoadBalExcludeCmIndex
     * @param macRangs
     */
    void addCmcLoadBalExcludeCmTable(SnmpParam snmpParam, Long ifIndex, Long topLoadBalExcludeCmIndex, String macRang);

    /**
     * 修改CC排除cm mac地址段
     * 
     * @param snmpParam
     * @param ifIndex
     * @param topLoadBalExcludeCmIndex
     * @param macRangs
     */
    void modifyCmcExcludeCmRangTable(SnmpParam snmpParam, Long ifIndex, Long topLoadBalExcludeCmIndex, String macRangs);

    /**
     * 从CC 删除排除cm mac段
     * 
     * @param snmpParam
     * @param docsLoadBalGrpId
     * @param topLoadBalExcludeCmIndexs
     */
    void deleteCmcLoadBalExcludeCmTable(SnmpParam snmpParam, Long ifIndex, List<Long> topLoadBalExcludeCmIndexs);

    /**
     * 添加负载均衡rule
     * 
     * @param snmpParam
     * @param cmcLoadBalBasicRule
     */
    void addCmcLoadBalBasicRule(SnmpParam snmpParam, CmcLoadBalBasicRule cmcLoadBalBasicRule);

    /**
     * 修改负载均衡rule
     * 
     * @param snmpParam
     * @param docsLoadBalBasicRuleEnable
     * @param docsLoadBalBasicRuleId
     * @param docsLoadBalBasicRuleDisStart
     * @param docsLoadBalBasicRuleDisPeriod
     */
    void modifyCmcLoadBalBasicRule(SnmpParam snmpParam, Long docsLoadBalBasicRuleId,
            Integer docsLoadBalBasicRuleEnable, Long docsLoadBalBasicRuleDisStart, Long docsLoadBalBasicRuleDisPeriod);

    /**
     * 删除负载均衡rule
     * 
     * @param snmpParam
     * @param docsLoadBalBasicRuleId
     */
    void deleteCmcLoadBalBasicRule(SnmpParam snmpParam, Long docsLoadBalBasicRuleId);

    /**
     * 新建负载均衡策略
     * 
     * @param snmpParam
     * @param docsLoadBalPolicyId
     * @param docsLoadBalPolicyRuleId
     * @return
     */
    Long addCmcLoadBalPolicy(SnmpParam snmpParam, Long docsLoadBalPolicyId, Long docsLoadBalPolicyRuleId);

    /**
     * 删除负载均衡策略
     * 
     * @param snmpParam
     * @param docsLoadBalPolicyId
     * @param docsLoadBalPolicyRuleId
     */
    void deleteCmcLoadBalPolicy(SnmpParam snmpParam, Long docsLoadBalPolicyId, Long docsLoadBalPolicyRuleId);

    /**
     * 下发cc策略
     * 
     * @param snmpParam
     * @param cmcIndex
     * @param topLoadBalPolicyId
     */
    void modifyCmcLoadBalTopPolicy(SnmpParam snmpParam, Long cmcIndex, Long topLoadBalPolicyId);
    
    /**
     * 从设备获取策略信息
     * @param snmpParam
     * @return
     */
    List<CmcLoadBalPolicy> getCmcLoadBalPolicies(SnmpParam snmpParam);
    
    /**
     * 从设备获取rule信息
     * @param snmpParam
     * @return
     */
    List<CmcLoadBalBasicRule> getCmcLoadBalBasicRules(SnmpParam snmpParam);
    
    /**
     * 从设备获取cc 策略
     * @param snmpParam
     * @return
     */
    List<CmcLoadBalTopPolicy> getCmcLoadBalTopPolicies(SnmpParam snmpParam);
    
    /**
     * 从设备获取负载均衡配置
     * @param snmpParam
     * @param cmcLoadBalCfg
     * @return
     */
    CmcLoadBalCfg getCmcLoadBalConfig(SnmpParam snmpParam, CmcLoadBalCfg cmcLoadBalCfg);
    
    /**
     * 从设备获取排除cm列表信息
     * @param snmpParam
     * @param cmcLoadBalExcludeCm
     * @return
     */
    List<CmcLoadBalExcludeCm> getCmcLoadBalExcludeCm(SnmpParam snmpParam);
    
    /**
     * 从设备获取负载均衡组信息
     * @param snmpParam
     * @return
     */
    List<CmcLoadBalGrp> getCmcLoadBalGroup(SnmpParam snmpParam);
    
    /**
     * 从设备获取负载均衡组信道信息
     * @param snmpParam
     * @return
     */
    List<CmcLoadBalChannel> getCmcLoadBalGroupChannel(SnmpParam snmpParam);
    
    /**
     * 从设备获取负载均衡组受限cm信息
     * @param snmpParam
     * @return
     */
    List<CmcLoadBalRestrictCm> getCmcLoadBalRestrictCm(SnmpParam snmpParam);

}
