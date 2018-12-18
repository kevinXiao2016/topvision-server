/***********************************************************************
 * $Id: CmcLoadBalanceDao.java,v1.0 2011-12-8 下午03:40:02 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalanceGroup;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalBasicRule;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalCfg;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalChannel;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalExcludeCm;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalGrp;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalPolicy;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalRestrictCm;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2011-12-8-下午03:40:02
 * 
 */
public interface CmcLoadBalanceDao extends BaseEntityDao<Entity> {

    /**
     * 获取CCMTS的负载均衡全局配置
     * @param cmcId
     * @return
     * @throws SQLException 
     */
    CmcLoadBalCfg queryLoadBalanceGlobalCfg(Long cmcId) throws SQLException;

    /**
     * 修改CCMTS的负载均衡全局配置
     * @param cmcId
     * @throws cmcLoadBalCfg 
     */
    void updateLoadBalanceGlobalCfg(CmcLoadBalCfg cmcLoadBalCfg) throws SQLException;

    /**
     * 获取CCMTS的排除MAC地址段
     * @param cmcId
     * @return
     * @throws SQLException 
     */
    List<CmcLoadBalExcludeCm> selectLoadBalanceExcMacRange(Long cmcId) throws SQLException;

    /**
     * 修改负载均衡时间策略的规则
     * @param rules
     */
    void updateCmcLoadBalRules(List<CmcLoadBalBasicRule> rules);

    /**
     * 添加CCMTS的排除MAC地址段
     * @param cmcId
     * @return
     * @throws SQLException 
     */
    void insertCmcLoadBalExcludeCmTable(CmcLoadBalExcludeCm cmcLoadBalExcludeCm);

    /**
     * 删除CCMTS的排除MAC地址段
     * @param cmcLoadBalExcludeCm
     */
    void deleteLoadBalanceExcMacRange(CmcLoadBalExcludeCm cmcLoadBalExcludeCm);

    /**
     * 根据entityId删除rule
     * @param entityId
     */
    void deleteLoadBalRuleByEntityId(Long entityId);

    /**
     * 根据entityId删除policy
     * @param entityId
     */
    void deleteLoadBalPolicyByEntityId(Long entityId);

    /**
     * 获取负载均衡组列表
     * @param cmcId
     * @return
     * @throws SQLException 
     */
    List<CmcLoadBalanceGroup> selectLoadBalanceGroupList(Long cmcId) throws SQLException;

    /**
     * 获取时间策略
     * @param cmcId
     * @return
     */
    CmcLoadBalPolicy queryLoadBalanceTimePolicy(Long cmcId);

    /**
     * 删除负载均衡组
     * @param cmcLoadBalanceGroup
     * @throws docsLoadBalGrpId 
     * @throws cmcId 
     */
    void deleteLoadBalanceGroup(Long docsLoadBalGrpId, Long cmcId) throws SQLException;

    /**
     * 添加负载均衡组表
     * @param cmcId
     * @param groupName
     * @param docsLoadBalGrpId
     * @return
     */
    Long insertLoadBalGroup(Long cmcId, String groupName, Long docsLoadBalGrpId);

    /**
     * 修改负载均衡组表
     * @param groupName
     * @param grpId
     * @return
     */
    void updateLoadBalanceGroupName(String groupName, Long grpId);

    /**
     * 添加负载均衡组表信道表
     * @param grpId
     * @param docsLoadBalChannelIfIndexs
     */
    void batchInsertLoadBalGroupChannels(Long grpId, List<Long> docsLoadBalChannelIfIndexs);

    /**
     * 添加负载均衡组受限mac 地址段信息 
     * @param grpId
     * @param cmcLoadBalRestrictCms
     */
    void batchInsertCmcLoadBalRestrictCms(Long grpId, List<CmcLoadBalRestrictCm> cmcLoadBalRestrictCms);

    /**
     * 删除绑定组信道
     * @param grpId
     * @param deleteChannelIndexs
     */
    void batchDeleteLoadBalanceChannel(Long grpId, List<Long> deleteChannelIndexs);

    /**
     * 获取docsLoadBalGrpIdByGrpId
     * @param grpId
     * @return
     */
    Long selectDocsLoadBalGrpIdByGrpId(Long grpId);

    /**
     * 删除绑定组受限cm mac地址段 
     * @param grpId
     * @param topLoadBalRestrictCmIndexs
     */
    void batchDeleteLoadBalanceMacRanges(Long grpId, List<Long> topLoadBalRestrictCmIndexs);

    /**
     * 获取绑定组首先cm 索引
     * @param grpId
     * * @return
     */
    List<Long> selectRestrictCmIndexs(Long grpId);

    /**
     * 获取绑定组索引 
     * @param cmcId
     * * @return
     */
    List<Long> selectDocsLoadBalGrpIds(Long cmcId);

    /**
     * 查询设备的负载均衡时间策略
     * @param entityId
     * @return
     */
    List<CmcLoadBalPolicy> selectLoadBalPolicyByEntityId(Long entityId);

    /**
     * 删除设备上的负载均衡时间策略
     * @param policyId
     */
    void deleteLoadBalPolicy(Long policyId);

    /**
     * 删除负载均衡时间策略的规则
     * @param rules
     */
    void deleteLoadBalRules(List<CmcLoadBalBasicRule> rules);

    /**
     * 查询设备上所有负载均衡策略规则
     * @p
     */
    List<CmcLoadBalBasicRule> queryLoadBalanceRulesByEntityId(Long entityId);

    /**
     * 获取策略的rules
     * @param policyId
     * @return
     */
    List<CmcLoadBalBasicRule> getLoadBalanceRulesByPolicyId(Long policyId);

    /**
     * 根据查询条件，查询rule
     * @param entityId
     * @param docsLoadBalBasicRuleEnable
     * @param docsLoadBalBasicRuleDisStart
     * @param docsLoadBalBasicRuleDisPeriod
     * @return
     */
    CmcLoadBalBasicRule selectLoadBalanceRuleByParam(Long entityId, Integer docsLoadBalBasicRuleEnable,
            Integer docsLoadBalBasicRuleDisStart, Integer docsLoadBalBasicRuleDisPeriod);

    /**
     * 根据policyId删除rules
     * @param policyId
     */
    void deleteLoadBalRulesByPolicyId(Long policyId);

    /**
     * 删除policy和rule的对应关系
     * @param policyId
     */
    void deleteLoadBalPolicyRuleRefByPolicyId(Long policyId);

    /**
     * 插入policy
     * @param balPolicy
     */
    void insertLoadBalPolicy(CmcLoadBalPolicy balPolicy);

    /**
     * 插入policy rule
     * @param balBasicRule
     */
    void insertLoadBalBasicRule(CmcLoadBalBasicRule balBasicRule);

    /**
     * 插入rule和policy对应关系
     * @param policyId
     * @param ruleId
     */
    void insertPolicyRuleRef(Long policyId, Long ruleId);

    /**
     * 根据主键查询policy
     * @param policyId
     */
    CmcLoadBalPolicy selectLoadBalPolicyById(Long policyId);

    /**
     * 设置ccmts的负载均衡策略
     * @param cmcId
     * @param policyId
     */
    void insertTopCcmtsLoadBalPolicy(Long cmcId, Long policyId);

    /**
     * 删除ccmts的负载均衡策略
     * @param cmcId
     */
    void deleteTopCcmtsLoadBalPolicyByCmcId(Long cmcId);

    /**
     * 获取数据库中最大的rule索引
     * @return
     */
    Long selectMaxLoadBalBasicRuleId();

    /**
     * 获取数据库中最大的policy索引
     * @return
     */
    Long selectMaxLoadBalPolicyId();

    /**
     * 查询指定的负载均衡组
     * @param grpId
     * @return
     */
    CmcLoadBalanceGroup selectLoadBalanceGroup(Long grpId);

    /**
     * 查询指定的rule有几条不同的policy引用
     * @param ruleId
     * @return
     */
    Integer selectLoadBalancePolicyCountByRuleId(Long ruleId);

    /**
     * 读取数据库中，当前设备上的受限CM列表
     */
    List<CmcLoadBalRestrictCm> queryAllRestrictCms(Long cmcId);

    /**
     * 删除指定rule
     */
    void deleteLoadBalRule(Long ruleId);

    /**
     * 批量插入cc 负载均衡组信道信息
     * 
     * @param cmcLoadBalChannels
     * @param entityId
     *            ，olt entityId
     */
    void batchInsertCmcLoadBalChannel(final List<CmcLoadBalChannel> cmcLoadBalChannels, Long entityId);

    /**
     * 批量插入cc 负载均衡组信息
     * 
     * @param cmcLoadBalGrps
     * @param entityId
     *            ，olt entityId
     */
    void batchInsertCmcLoadBalGrp(final List<CmcLoadBalGrp> cmcLoadBalGrps, Long entityId);

    /**
     * 批量插入 8800b负载均衡排除cm mac表
     * 
     * @param cmcLoadBalExcludeCms
     * @param cmcId
     */
    void batchInsertCmcLoadBalExcludeCm(final List<CmcLoadBalExcludeCm> cmcLoadBalExcludeCms, Long cmcId);

    /**
     * 批量插入 受限cm mac表
     * 
     * @param cmcLoadBalRestrictCms
     * @param cmcId
     */
    void batchInsertCmc8800bLoadBalRestrictCm(final List<CmcLoadBalRestrictCm> cmcLoadBalRestrictCms, Long cmcId);

    /**
     * 批量插入负载均衡组信道信息
     * 
     * @param cmcLoadBalChannels
     * @param cmcId
     */
    void batchInsertCmc8800BLoadBalChannel(final List<CmcLoadBalChannel> cmcLoadBalChannels, Long cmcId);

    /**
     * 8800B批量插入负载均衡组
     * 
     * @param cmcLoadBalGrps
     * @param cmcId
     */
    void batchInsertCmc8800BLoadBalGrp(final List<CmcLoadBalGrp> cmcLoadBalGrps, Long cmcId);

    /**
     * 批量插入或更新负载均衡配置信息
     * 
     * @param cmcLoadBalCfg
     */
    void insertOrUpdateCmcLoadBalCfg(final CmcLoadBalCfg cmcLoadBalCfg);

    /**
     * 根据cmcIndex获取cmcId
     * 
     * @param map
     * @return
     */
    Long getCmcIdByCmcIndexAndEntityId(Map<String, Long> map);
}
