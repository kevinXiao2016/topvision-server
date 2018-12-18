/***********************************************************************
 * $Id: CmcLoadBalanceService.java,v1.0 2011-12-8 下午12:46:20 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.service;

import java.sql.SQLException;
import java.util.List;

import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalPolicyTpl;
import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalanceGroup;
import com.topvision.ems.cmc.loadbalance.exception.LoadBalException;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalCfg;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalExcludeCm;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalPolicy;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalRestrictCm;
import com.topvision.framework.service.Service;

/**
 * 负载均衡功能
 * 
 * @author loyal
 * @created @2011-12-8-下午12:46:20
 * 
 */
public interface CmcLoadBalanceService extends Service {
    /**
     * 获取负载均衡的负生效策略模板
     * @return
     */
    List<CmcLoadBalPolicyTpl> getLoadBalPolicyTplList();

    /**
     * 获取设备的负载均衡时间策略
     * @param entityId
     * @return
     */
    List<CmcLoadBalPolicy> getLoadBalPolicyListByEntityId(Long entityId);

    /**
     * 根据id查询模策略板
     * @param policyTplId
     * @return
     */
    CmcLoadBalPolicyTpl getLoadBalPolicyTplById(Long policyTplId);

    /**
     * 获取CCMTS的负载均衡全局配置
     * @param cmcId
     * @return
     */
    CmcLoadBalCfg getLoadBalanceGlobalCfg(Long cmcId);

    /**
     * 修改CCMTS的负载均衡全局配置
     * @param cmcLoadBalCfg
     * @throws SQLException 
     */
    void modifyLoadBalanceGlobalCfg(CmcLoadBalCfg cmcLoadBalCfg) throws SQLException;

    /**
     * 获取CCMTS的排除MAC地址段
     * @param cmcId
     * @return
     */
    List<CmcLoadBalExcludeCm> getLoadBalanceExcMacRange(Long cmcId);

    /**
     * 添加CCMTS的排除MAC地址段
     * @param cmcId
     * @param cmcLoadBalExcludeCm
     * @return
     * @throws SQLException 
     */
    void addLoadBalanceExcMacRange(CmcLoadBalExcludeCm cmcLoadBalExcludeCm) throws SQLException;

    /**
     * 删除CCMTS的排除MAC地址段
     * @param cmcId
     * @param cmcLoadBalExcludeCm
     * @return
     */
    void deleteLoadBalanceExcMacRange(CmcLoadBalExcludeCm cmcLoadBalExcludeCm);

    /**
     * 添加负载均衡的生效策略模板
     * @param balPolicyTpl
     */
    void addLoadBalPolicyTpl(CmcLoadBalPolicyTpl balPolicyTpl);

    /**
     * 删除负载均衡生效策略模板
     * @param policyTplId
     */
    void deleteLoadBalPolicyTpl(Long policyTplId);

    /**
     * 更新负载均衡生效策略模板
     * @param balPolicyTpl
     */
    void modifyLoadBalPolicyTpl(CmcLoadBalPolicyTpl balPolicyTpl);

    /**
     * 获取负载均衡组列表
     * @param cmcId
     * @return
     * @throws SQLException 
     */
    List<CmcLoadBalanceGroup> selectLoadBalanceGroupList(Long cmcId) throws SQLException;

    /**
     * 添加负载均衡组
     * @param cmcLoadBalanceGroup
     * @throws SQLException 
     */
    void addLoadBalanceGroup(CmcLoadBalanceGroup cmcLoadBalanceGroup);

    /**
     * 修改负载均衡组名称
     * @param groupName
     * @param grpId
     * @throws SQLException 
     */
    void modifyLoadBalanceGroup(String groupName, Long grpId) throws SQLException;

    /**
     * 删除负载均衡组
     * @param cmcLoadBalanceGroup
     * @throws docsLoadBalGrpId 
     * @throws cmcId 
     */
    void deleteLoadBalanceGroup(Long docsLoadBalGrpId, Long cmcId) throws SQLException;

    /**
     * 获取时间策略
     * @param cmcId
     * @return
     */
    CmcLoadBalPolicy getLoadBalanceTimePolicy(Long cmcId);

    /**
     * 修改负载均衡组信道列表
     * @param cmcId
     * @param grpId
     * @param addChannelIndexs
     * @param deleteChannelIndexs
     */
    void modifyLoadBalanceChannel(Long cmcId, Long grpId, List<Long> addChannelIndexs, List<Long> deleteChannelIndexs);

    /**
     * 修改负载均衡组受限cm mac段列表
     * @param cmcId
     * @param grpId
     * @param addMacRanges 
     * @param deleteMacRanges 需要删除的组索引
     */
    void modifyLoadBalanceMacRanges(Long cmcId, Long grpId, List<String> addMacRanges,
            List<Long> topLoadBalRestrictCmIndexs);

    /**
     * 通过cmc id获取对应entity id
     * @param cmcId
     * @return
     */
    Long getEntityIdByCmcId(Long cmcId);

    /**
     * 插入负载均衡时间策略
     * @param balPolicy
     */
    void txAddCmcLoadBalPolicy(CmcLoadBalPolicy balPolicy) throws LoadBalException;

    /**
     * 删除设备上的负载均衡时间策略
     * @param policyId
     */
    void deleteLoadBalPolicy(Long policyId);

    /**
     * 修改负载均衡时间策略
     * @param balPolicy
     */
    void txModifyLoadBalPolicy(CmcLoadBalPolicy balPolicy);

    /**
     * 根据主键查询policy
     * @param policyId
     */
    CmcLoadBalPolicy getLoadBalPolicyById(Long policyId);

    /**
     * 设置Ccmts负载均衡时间策略
     * @param cmcId
     * @param policyId
     */
    void modifyCcmtsPolicy(Long cmcId, Long policyId);

    /**
     * 将设备上的负载均衡策略同步到数据库
     * @param cmcId
     */
    void txSyncLoadBalPolicy(Long cmcId);

    /**
     * 查询指定的负载均衡组
     * @param grpId
     * @return
     */
    CmcLoadBalanceGroup getLoadBalanceGroup(Long grpId);
    
    /**
     * 从设备刷新负载均衡配置信息
     * @param cmcId
     */
    void refreshLoadBalanceConfig(Long cmcId);
    
    /**
     * 从设备刷新负载均衡组信息
     * @param cmcId
     */
    void refreshLoadBalanceGroup(Long cmcId);
    
    /**
     * 从设备刷新负载均衡排除CM信息
     * @param cmcId
     */
    void refreshLoadBalanceExcludeCm(Long cmcId);
    /**
     * 读取数据库中，当前设备上的受限CM列表
     */
    List<CmcLoadBalRestrictCm> queryAllRestrictCms(Long cmcId);
}