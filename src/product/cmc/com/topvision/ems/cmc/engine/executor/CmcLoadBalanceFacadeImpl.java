/***********************************************************************
 * $Id: CmcLoadBalanceFacadeImpl.java,v1.0 2013-4-24 上午10:49:40 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.engine.executor;

import java.util.List;

import javax.annotation.Resource;

import com.topvision.ems.cmc.loadbalance.facade.CmcLoadBalanceFacade;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalBasicRule;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalCfg;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalChannel;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalExcludeCm;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalGrp;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalPolicy;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalRestrictCm;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalTopPolicy;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author loyal
 * @created @2013-4-24-上午10:49:40
 * 
 */
@Facade("cmcLoadBalanceFacade")
public class CmcLoadBalanceFacadeImpl extends EmsFacade implements CmcLoadBalanceFacade {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#modifyLoadBalConfig(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcLoadBalCfg)
     */
    public void modifyLoadBalConfig(SnmpParam snmpParam, CmcLoadBalCfg cmcLoadBalCfg) {
        snmpExecutorService.setData(snmpParam, cmcLoadBalCfg);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#addLoadBalGroupTable(com.topvision.framework.snmp.SnmpParam, java.lang.Long)
     */
    public Long addLoadBalGroupTable(SnmpParam snmpParam, Long docsLoadBalGrpId) {
        CmcLoadBalGrp cmcLoadBalGrp = new CmcLoadBalGrp();
        cmcLoadBalGrp.setDocsLoadBalGrpId(docsLoadBalGrpId);
        cmcLoadBalGrp.setDocsLoadBalGrpStatus(RowStatus.CREATE_AND_GO);
        cmcLoadBalGrp = snmpExecutorService.setData(snmpParam, cmcLoadBalGrp);
        return cmcLoadBalGrp.getDocsLoadBalGrpId();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#deleteBalGroupTable(com.topvision.framework
     * .snmp.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcLoadBalGrp)
     */
    public void deleteBalGroupTable(SnmpParam snmpParam, Long docsLoadBalGrpId) {
        // 删除mib里面负载均衡组表后mib会自动删除该均衡组关联其它表
        CmcLoadBalGrp cmcLoadBalGrp = new CmcLoadBalGrp();
        cmcLoadBalGrp.setDocsLoadBalGrpId(docsLoadBalGrpId);
        cmcLoadBalGrp.setDocsLoadBalGrpStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, cmcLoadBalGrp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#addLoadBalGroupChannelTable(com.topvision
     * .framework.snmp.SnmpParam, java.lang.Long, java.util.List)
     */
    public void addLoadBalGroupChannelTable(SnmpParam snmpParam, Long docsLoadBalGrpId,
            List<Long> docsLoadBalChannelIfIndexs) {
        for (int i = 0; i < docsLoadBalChannelIfIndexs.size(); i++) {
            CmcLoadBalChannel cmcLoadBalChannel = new CmcLoadBalChannel();
            cmcLoadBalChannel.setDocsLoadBalGrpId(docsLoadBalGrpId);
            cmcLoadBalChannel.setDocsLoadBalChannelIfIndex(docsLoadBalChannelIfIndexs.get(i));
            cmcLoadBalChannel.setDocsLoadBalChannelStatus(RowStatus.CREATE_AND_GO);
            snmpExecutorService.setData(snmpParam, cmcLoadBalChannel);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#deleteLoadBalGroupChannelTable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.util.List)
     */
    public void deleteLoadBalGroupChannelTable(SnmpParam snmpParam, Long docsLoadBalGrpId,
            List<Long> docsLoadBalChannelIfIndexs) {
        for (int i = 0; i < docsLoadBalChannelIfIndexs.size(); i++) {
            CmcLoadBalChannel cmcLoadBalChannel = new CmcLoadBalChannel();
            cmcLoadBalChannel.setDocsLoadBalGrpId(docsLoadBalGrpId);
            cmcLoadBalChannel.setDocsLoadBalChannelIfIndex(docsLoadBalChannelIfIndexs.get(i));
            cmcLoadBalChannel.setDocsLoadBalChannelStatus(RowStatus.DESTORY);
            snmpExecutorService.setData(snmpParam, cmcLoadBalChannel);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#addLoadBalGroupCmRangsTable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Long, java.lang.String)
     */
    public CmcLoadBalRestrictCm addLoadBalGroupCmRangsTable(SnmpParam snmpParam, Long docsLoadBalGrpId,
            Long topLoadBalRestrictCmIndex, String macRang) {
        CmcLoadBalRestrictCm cmcLoadBalRestrictCm = new CmcLoadBalRestrictCm();
        cmcLoadBalRestrictCm.setDocsLoadBalGrpId(docsLoadBalGrpId);
        cmcLoadBalRestrictCm.setTopLoadBalRestrictCmIndex(topLoadBalRestrictCmIndex);
        cmcLoadBalRestrictCm.setTopLoadBalRestrictCmMacRang(macRang);
        cmcLoadBalRestrictCm.setTopLoadBalRestrictCmStatus(RowStatus.CREATE_AND_GO);
        return (CmcLoadBalRestrictCm) snmpExecutorService.setData(snmpParam, cmcLoadBalRestrictCm);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#modifyBalGroupCmRangsTable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Long, java.lang.String)
     */
    public void modifyBalGroupCmRangsTable(SnmpParam snmpParam, Long docsLoadBalGrpId, Long topLoadBalRestrictCmIndex,
            String macRangs) {
        CmcLoadBalRestrictCm cmcLoadBalRestrictCm = new CmcLoadBalRestrictCm();
        cmcLoadBalRestrictCm.setDocsLoadBalGrpId(docsLoadBalGrpId);
        cmcLoadBalRestrictCm.setTopLoadBalRestrictCmIndex(topLoadBalRestrictCmIndex);
        cmcLoadBalRestrictCm.setTopLoadBalRestrictCmMacRang(macRangs);
        snmpExecutorService.setData(snmpParam, cmcLoadBalRestrictCm);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#deleteLoadBalGroupCmRangsTable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.util.List)
     */
    public void deleteLoadBalGroupCmRangsTable(SnmpParam snmpParam, Long docsLoadBalGrpId,
            List<Long> topLoadBalRestrictCmIndexs) {
        for (int i = 0; i < topLoadBalRestrictCmIndexs.size(); i++) {
            CmcLoadBalRestrictCm cmcLoadBalRestrictCm = new CmcLoadBalRestrictCm();
            cmcLoadBalRestrictCm.setDocsLoadBalGrpId(docsLoadBalGrpId);
            cmcLoadBalRestrictCm.setTopLoadBalRestrictCmIndex(topLoadBalRestrictCmIndexs.get(i) - i);
            cmcLoadBalRestrictCm.setTopLoadBalRestrictCmStatus(RowStatus.DESTORY);
            cmcLoadBalRestrictCm = snmpExecutorService.setData(snmpParam, cmcLoadBalRestrictCm);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#addCmcLoadBalExcludeCmTable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.util.List)
     */
    public void addCmcLoadBalExcludeCmTable(SnmpParam snmpParam, Long ifIndex, Long topLoadBalExcludeCmIndex,
            String macRang) {
        CmcLoadBalExcludeCm cmcLoadBalExcludeCm = new CmcLoadBalExcludeCm();
        cmcLoadBalExcludeCm.setIfIndex(ifIndex);
        cmcLoadBalExcludeCm.setTopLoadBalExcludeCmMacRang(macRang);
        cmcLoadBalExcludeCm.setTopLoadBalExcludeCmStatus(RowStatus.CREATE_AND_GO);
        cmcLoadBalExcludeCm.setTopLoadBalExcludeCmIndex(topLoadBalExcludeCmIndex);
        snmpExecutorService.setData(snmpParam, cmcLoadBalExcludeCm);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#modifyCmcExcludeCmRangTable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Long, java.lang.String)
     */
    public void modifyCmcExcludeCmRangTable(SnmpParam snmpParam, Long ifIndex, Long topLoadBalExcludeCmIndex,
            String macRangs) {
        CmcLoadBalExcludeCm cmcLoadBalExcludeCm = new CmcLoadBalExcludeCm();
        cmcLoadBalExcludeCm.setIfIndex(ifIndex);
        cmcLoadBalExcludeCm.setTopLoadBalExcludeCmIndex(topLoadBalExcludeCmIndex);
        cmcLoadBalExcludeCm.setTopLoadBalExcludeCmMacRang(macRangs);
        snmpExecutorService.setData(snmpParam, cmcLoadBalExcludeCm);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#deleteCmcLoadBalExcludeCmTable(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.util.List)
     */
    public void deleteCmcLoadBalExcludeCmTable(SnmpParam snmpParam, Long ifIndex, List<Long> topLoadBalExcludeCmIndexs) {
        for (int i = 0; i < topLoadBalExcludeCmIndexs.size(); i++) {
            CmcLoadBalExcludeCm cmcLoadBalExcludeCm = new CmcLoadBalExcludeCm();
            cmcLoadBalExcludeCm.setIfIndex(ifIndex);
            cmcLoadBalExcludeCm.setTopLoadBalExcludeCmIndex(topLoadBalExcludeCmIndexs.get(i));
            cmcLoadBalExcludeCm.setTopLoadBalExcludeCmStatus(RowStatus.DESTORY);
            snmpExecutorService.setData(snmpParam, cmcLoadBalExcludeCm);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#addCmcLoadBalBasicRule(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcLoadBalBasicRule)
     */
    public void addCmcLoadBalBasicRule(SnmpParam snmpParam, CmcLoadBalBasicRule cmcLoadBalBasicRule) {
        cmcLoadBalBasicRule.setDocsLoadBalBasicRuleRowStatus(RowStatus.CREATE_AND_GO);
        cmcLoadBalBasicRule = snmpExecutorService.setData(snmpParam, cmcLoadBalBasicRule);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#modifyCmcLoadBalBasicRule(com.topvision.framework.snmp.SnmpParam, java.lang.Integer, java.lang.Long, java.lang.Long, java.lang.Long)
     */
    public void modifyCmcLoadBalBasicRule(SnmpParam snmpParam, Long docsLoadBalBasicRuleId,
            Integer docsLoadBalBasicRuleEnable, Long docsLoadBalBasicRuleDisStart, Long docsLoadBalBasicRuleDisPeriod) {
        CmcLoadBalBasicRule cmcLoadBalBasicRule = new CmcLoadBalBasicRule();
        cmcLoadBalBasicRule.setDocsLoadBalBasicRuleId(docsLoadBalBasicRuleId);
        cmcLoadBalBasicRule.setDocsLoadBalBasicRuleEnable(docsLoadBalBasicRuleEnable);
        cmcLoadBalBasicRule.setDocsLoadBalBasicRuleDisStart(docsLoadBalBasicRuleDisStart);
        cmcLoadBalBasicRule.setDocsLoadBalBasicRuleDisPeriod(docsLoadBalBasicRuleDisPeriod);
        snmpExecutorService.setData(snmpParam, cmcLoadBalBasicRule);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#deleteCmcLoadBalBasicRule(com.topvision.framework.snmp.SnmpParam, java.lang.Long)
     */
    public void deleteCmcLoadBalBasicRule(SnmpParam snmpParam, Long docsLoadBalBasicRuleId) {
        CmcLoadBalBasicRule cmcLoadBalBasicRule = new CmcLoadBalBasicRule();
        cmcLoadBalBasicRule.setDocsLoadBalBasicRuleId(docsLoadBalBasicRuleId);
        cmcLoadBalBasicRule.setDocsLoadBalBasicRuleRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, cmcLoadBalBasicRule);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#addCmcLoadBalPolicy(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Long)
     */
    public Long addCmcLoadBalPolicy(SnmpParam snmpParam, Long docsLoadBalPolicyId, Long docsLoadBalPolicyRuleId) {
        CmcLoadBalPolicy cmcLoadBalPolicy = new CmcLoadBalPolicy();
        cmcLoadBalPolicy.setDocsLoadBalPolicyId(docsLoadBalPolicyId);
        cmcLoadBalPolicy.setDocsLoadBalPolicyRuleId(docsLoadBalPolicyRuleId);
        cmcLoadBalPolicy.setDocsLoadBalPolicyRowStatus(RowStatus.CREATE_AND_GO);
        cmcLoadBalPolicy = snmpExecutorService.setData(snmpParam, cmcLoadBalPolicy);
        return cmcLoadBalPolicy.getDocsLoadBalPolicyId();
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#deleteCmcLoadBalPolicy(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Long)
     */
    public void deleteCmcLoadBalPolicy(SnmpParam snmpParam, Long docsLoadBalPolicyId, Long docsLoadBalPolicyRuleId) {
        CmcLoadBalPolicy cmcLoadBalPolicy = new CmcLoadBalPolicy();
        cmcLoadBalPolicy.setDocsLoadBalPolicyId(docsLoadBalPolicyId);
        cmcLoadBalPolicy.setDocsLoadBalPolicyRuleId(docsLoadBalPolicyRuleId);
        cmcLoadBalPolicy.setDocsLoadBalPolicyRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, cmcLoadBalPolicy);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#modifyCmcLoadBalTopPolicy(com.topvision.framework.snmp.SnmpParam, java.lang.Long, java.lang.Long)
     */
    public void modifyCmcLoadBalTopPolicy(SnmpParam snmpParam, Long cmcIndex, Long topLoadBalPolicyId) {
        CmcLoadBalTopPolicy cmcLoadBalTopPolicy = new CmcLoadBalTopPolicy();
        cmcLoadBalTopPolicy.setIfIndex(cmcIndex);
        cmcLoadBalTopPolicy.setTopLoadBalPolicyId(topLoadBalPolicyId);
        snmpExecutorService.setData(snmpParam, cmcLoadBalTopPolicy);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#getCmcLoadBalPolicies(com.topvision.framework.snmp.SnmpParam)
     */
    public List<CmcLoadBalPolicy> getCmcLoadBalPolicies(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcLoadBalPolicy.class);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#getCmcLoadBalBasicRules(com.topvision.framework.snmp.SnmpParam)
     */
    public List<CmcLoadBalBasicRule> getCmcLoadBalBasicRules(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcLoadBalBasicRule.class);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#getCmcLoadBalConfig(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcLoadBalCfg)
     */
    public CmcLoadBalCfg getCmcLoadBalConfig(SnmpParam snmpParam, CmcLoadBalCfg cmcLoadBalCfg) {
        return snmpExecutorService.getTableLine(snmpParam, cmcLoadBalCfg);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#getCmcLoadBalExcludeCm(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.cmc.facade.domain.CmcLoadBalExcludeCm)
     */
    @Override
    public List<CmcLoadBalExcludeCm> getCmcLoadBalExcludeCm(SnmpParam snmpParam) {
        /*List<CmcLoadBalExcludeCm> cmcLoadBalExcludeCms = new ArrayList<CmcLoadBalExcludeCm>();
        // topLoadBalExcludeCmIndex 取值范围1-32,考虑A型设备
        for (int i = 1; i < 33; i++) {
            try {
                CmcLoadBalExcludeCm cmcExcludeCm = new CmcLoadBalExcludeCm();
                cmcExcludeCm.setTopLoadBalExcludeCmIndex(new Long(i));
                cmcExcludeCm.setIfIndex(cmcLoadBalExcludeCm.getIfIndex());
                cmcExcludeCm = snmpExecutorService.getTableLine(snmpParam, cmcExcludeCm);
                cmcLoadBalExcludeCms.add(cmcExcludeCm);
            } catch (SnmpNoSuchInstanceException e) {
                logger.debug("get cmcLoadBalExcludeCm no such instance{}",i);
            }
        }*/
        
        return snmpExecutorService.getTable(snmpParam, CmcLoadBalExcludeCm.class);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#getCmcLoadBalTopPolicies(com.topvision.framework.snmp.SnmpParam)
     */
    public List<CmcLoadBalTopPolicy> getCmcLoadBalTopPolicies(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcLoadBalTopPolicy.class);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.facade.CmcLoadBalanceFacade#getCmcLoadBalGroup(com.topvision.framework.snmp.SnmpParam)
     */
    public List<CmcLoadBalGrp> getCmcLoadBalGroup(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcLoadBalGrp.class);
    }

    @Override
    public List<CmcLoadBalChannel> getCmcLoadBalGroupChannel(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcLoadBalChannel.class);
    }

    @Override
    public List<CmcLoadBalRestrictCm> getCmcLoadBalRestrictCm(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CmcLoadBalRestrictCm.class);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

}
