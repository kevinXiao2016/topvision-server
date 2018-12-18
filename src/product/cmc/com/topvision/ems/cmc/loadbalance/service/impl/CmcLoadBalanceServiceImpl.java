/***********************************************************************
 * $Id: LoadBalanceServiceImpl.java,v1.0 2011-12-8 上午10:58:54 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.loadbalance.dao.CmcLoadBalPolicyTplDao;
import com.topvision.ems.cmc.loadbalance.dao.CmcLoadBalanceDao;
import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalPolicyTpl;
import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalanceGroup;
import com.topvision.ems.cmc.loadbalance.exception.LoadBalException;
import com.topvision.ems.cmc.loadbalance.facade.CmcLoadBalanceFacade;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalBasicRule;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalCfg;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalChannel;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalExcludeCm;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalGrp;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalPolicy;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalRestrictCm;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalTopPolicy;
import com.topvision.ems.cmc.loadbalance.service.CmcLoadBalanceService;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.util.CmcConstants;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * 负载均衡功能实现
 * 
 * @author loyal
 * @created @2011-12-8-上午10:58:54
 * 
 */
@Service("cmcLoadBalanceService")
public class CmcLoadBalanceServiceImpl extends CmcBaseCommonService implements CmcLoadBalanceService,
        CmcSynchronizedListener {
    @Resource(name = "cmcLoadBalanceDao")
    private CmcLoadBalanceDao cmcLoadBalanceDao;
    @Resource(name = "cmcLoadBalPolicyTplDao")
    private CmcLoadBalPolicyTplDao cmcLoadBalPolicyTplDao;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private MessageService messageService;

    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
    }

    public void setCmcLoadBalPolicyTplDao(CmcLoadBalPolicyTplDao cmcLoadBalPolicyTplDao) {
        this.cmcLoadBalPolicyTplDao = cmcLoadBalPolicyTplDao;
    }

    public CmcLoadBalanceDao getCmcLoadBalanceDao() {
        return cmcLoadBalanceDao;
    }

    public void setCmcLoadBalanceDao(CmcLoadBalanceDao cmcLoadBalanceDao) {
        this.cmcLoadBalanceDao = cmcLoadBalanceDao;
    }

    @Override
    public CmcLoadBalCfg getLoadBalanceGlobalCfg(Long cmcId) {
        try {
            return cmcLoadBalanceDao.queryLoadBalanceGlobalCfg(cmcId);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#getLoadBalPolicyListByEntityId(java.lang
     * .Long)
     */
    @Override
    public List<CmcLoadBalPolicy> getLoadBalPolicyListByEntityId(Long entityId) {
        return cmcLoadBalanceDao.selectLoadBalPolicyByEntityId(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#deleteLoadBalPolicyTpl(java.lang.Long)
     */
    @Override
    public void deleteLoadBalPolicyTpl(Long policyTplId) {
        cmcLoadBalPolicyTplDao.deleteLoadBalPolicyTpl(policyTplId);
    }

    @Override
    public List<CmcLoadBalExcludeCm> getLoadBalanceExcMacRange(Long cmcId) {
        try {
            return cmcLoadBalanceDao.selectLoadBalanceExcMacRange(cmcId);
        } catch (SQLException e) {
            logger.error("", e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#addLoadBalanceExcMacRange(com.topvision
     * .ems.cmc.facade.domain.CmcLoadBalExcludeCm, java.lang.Long)
     */
    public void addLoadBalanceExcMacRange(CmcLoadBalExcludeCm cmcLoadBalExcludeCm) throws SQLException {
        Long cmcId = cmcLoadBalExcludeCm.getCmcId();
        Long topLoadBalExcludeCmIndex;
        String macRang = cmcLoadBalExcludeCm.getTopLoadBalExcludeCmMacRang();
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        Long ifIndex = cmcService.getCmcIndexByCmcId(cmcId);
        List<CmcLoadBalExcludeCm> cmcLoadBalExcludeCms = new ArrayList<CmcLoadBalExcludeCm>();
        cmcLoadBalExcludeCms = cmcLoadBalanceDao.selectLoadBalanceExcMacRange(cmcId);
        if (cmcLoadBalExcludeCms.size() > 0) {
            topLoadBalExcludeCmIndex = cmcLoadBalExcludeCms.get(cmcLoadBalExcludeCms.size() - 1)
                    .getTopLoadBalExcludeCmIndex() + 1;
        } else {
            // 索引由网管维护，所以如果当前一条记录都没有的话，则手动加一条，其索引为1.
            topLoadBalExcludeCmIndex = 1l;
        }
        // TODO 设置到设备可以成功，但抛出异常，导致无法插入数据库，为方便测试，暂时捕获异常
        try {
            getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addCmcLoadBalExcludeCmTable(snmpParam, ifIndex,
                    topLoadBalExcludeCmIndex, macRang);
            cmcLoadBalExcludeCm.setCmcId(cmcId);
            cmcLoadBalExcludeCm.setTopLoadBalExcludeCmIndex(topLoadBalExcludeCmIndex);
            cmcLoadBalanceDao.insertCmcLoadBalExcludeCmTable(cmcLoadBalExcludeCm);
        } catch (SnmpException e) {
            // logger.error("",e);
            throw new LoadBalException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcLoadBalanceService#txSyncLoadBalPolicy(java.lang.Long)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void txSyncLoadBalPolicy(Long cmcId) {
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        List<CmcLoadBalBasicRule> rules = getCmcLoadBalanceFacade(snmpParam.getIpAddress()).getCmcLoadBalBasicRules(
                snmpParam);
        // 实际上取到的是docsLoadBalPolicyId和docsLoadBalPolicyRuleId的关系
        List<CmcLoadBalPolicy> refs = getCmcLoadBalanceFacade(snmpParam.getIpAddress())
                .getCmcLoadBalPolicies(snmpParam);
        List<CmcLoadBalTopPolicy> ccPolicys = getCmcLoadBalanceFacade(snmpParam.getIpAddress())
                .getCmcLoadBalTopPolicies(snmpParam);

        // 数据库端
        cmcLoadBalanceDao.deleteTopCcmtsLoadBalPolicyByCmcId(cmcId);
        cmcLoadBalanceDao.deleteLoadBalPolicyByEntityId(entityId);
        cmcLoadBalanceDao.deleteLoadBalRuleByEntityId(entityId);

        // 向docsloadbalbasicrule表中插入记录
        for (CmcLoadBalBasicRule r : rules) {
            r.setEntityId(entityId);
            cmcLoadBalanceDao.insertLoadBalBasicRule(r);
        }

        // 整理docsLoadBalPolicyId和docsLoadBalPolicyRuleId的对应关系
        Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
        for (CmcLoadBalPolicy ref : refs) {
            Long docsLoadBalPolicyId = ref.getDocsLoadBalPolicyId();
            Long docsLoadBalPolicyRuleId = ref.getDocsLoadBalPolicyRuleId();
            if (map.containsKey(docsLoadBalPolicyId)) {
                map.get(docsLoadBalPolicyId).add(docsLoadBalPolicyRuleId);
            } else {
                List<Long> docsLoadBalPolicyRuleIds = new ArrayList<Long>();
                docsLoadBalPolicyRuleIds.add(docsLoadBalPolicyRuleId);
                map.put(docsLoadBalPolicyId, docsLoadBalPolicyRuleIds);
            }
        }

        // 向docsloadbalpolicy和policyruleref表中插入记录
        List<CmcLoadBalPolicy> insertedPolicys = new ArrayList<CmcLoadBalPolicy>();
        Iterator<Entry<Long, List<Long>>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Long, List<Long>> entry = iter.next();
            Long docsLoadBalPolicyId = entry.getKey();
            List<Long> docsLoadBalPolicyRuleIds = entry.getValue();

            // 插入policy
            CmcLoadBalPolicy p = new CmcLoadBalPolicy();
            p.setEntityId(entityId);
            p.setDocsLoadBalPolicyId(docsLoadBalPolicyId);
            cmcLoadBalanceDao.insertLoadBalPolicy(p);
            insertedPolicys.add(p);

            // 插入policy和rule的关系
            Long policyId = p.getPolicyId();
            Long ruleId = null;
            for (Long docsLoadBalPolicyRuleId : docsLoadBalPolicyRuleIds) {
                for (CmcLoadBalBasicRule r : rules) {
                    if (r.getDocsLoadBalBasicRuleId().equals(docsLoadBalPolicyRuleId)) {
                        ruleId = r.getRuleId();
                        break;
                    }
                }
                if (ruleId != null) {
                    cmcLoadBalanceDao.insertPolicyRuleRef(policyId, ruleId);
                }
            }
        }

        // 插入CC的负载均衡时间策略
        for (CmcLoadBalTopPolicy ccPolicy : ccPolicys) {
            Long policyId = null;
            for (CmcLoadBalPolicy policy : insertedPolicys) {
                if (policy.getDocsLoadBalPolicyId().equals(ccPolicy.getTopLoadBalPolicyId())) {
                    policyId = policy.getPolicyId();
                }
            }
            Map cmcIdMap = new HashMap();
            cmcIdMap.put("entityId", entityId);
            cmcIdMap.put("cmcIndex", ccPolicy.getIfIndex());
            Long _cmcId = cmcLoadBalanceDao.getCmcIdByCmcIndexAndEntityId(cmcIdMap);
            if (policyId != null && _cmcId != null) {
                cmcLoadBalanceDao.insertTopCcmtsLoadBalPolicy(_cmcId, policyId);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#deleteLoadBalanceExcMacRange(com.topvision
     * .ems.cmc.facade.domain.CmcLoadBalExcludeCm)
     */
    public void deleteLoadBalanceExcMacRange(CmcLoadBalExcludeCm cmcLoadBalExcludeCm) {
        List<Long> topLoadBalExcludeCmIndexs = new ArrayList<Long>();
        Long cmcId = cmcLoadBalExcludeCm.getCmcId();
        topLoadBalExcludeCmIndexs.add(cmcLoadBalExcludeCm.getTopLoadBalExcludeCmIndex());
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        Long ifIndex = cmcService.getCmcIndexByCmcId(cmcId);
        CmcLoadBalanceFacade cmcFacade = getCmcLoadBalanceFacade(snmpParam.getIpAddress());
        cmcFacade.deleteCmcLoadBalExcludeCmTable(snmpParam, ifIndex, topLoadBalExcludeCmIndexs);
        cmcLoadBalanceDao.deleteLoadBalanceExcMacRange(cmcLoadBalExcludeCm);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#getLoadBalPolicyTplById(java.lang.Long)
     */
    @Override
    public CmcLoadBalPolicyTpl getLoadBalPolicyTplById(Long policyTplId) {
        return cmcLoadBalPolicyTplDao.selectLoadBalPolicyTplById(policyTplId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#modifyLoadBalanceGlobalCfg(com.topvision
     * .ems.cmc.facade.domain.CmcLoadBalCfg)
     */
    @Override
    public void modifyLoadBalanceGlobalCfg(CmcLoadBalCfg cmcLoadBalCfg) throws SQLException {
        Long entityId = getEntityIdByCmcId(cmcLoadBalCfg.getCmcId());
        snmpParam = getSnmpParamByEntityId(entityId);
        getCmcLoadBalanceFacade(snmpParam.getIpAddress()).modifyLoadBalConfig(snmpParam, cmcLoadBalCfg);
        cmcLoadBalanceDao.updateLoadBalanceGlobalCfg(cmcLoadBalCfg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#modifyLoadBalPolicyTpl(com.topvision.
     * ems.cmc.domain.CmcLoadBalPolicyTpl)
     */
    @Override
    public void modifyLoadBalPolicyTpl(CmcLoadBalPolicyTpl balPolicyTpl) {
        cmcLoadBalPolicyTplDao.updateLoadBalPolicyTpl(balPolicyTpl);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcLoadBalanceService#getLoadBalPolicyTplList()
     */
    @Override
    public List<CmcLoadBalPolicyTpl> getLoadBalPolicyTplList() {
        return cmcLoadBalPolicyTplDao.selectLoadBalPolicyTpl();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#addLoadBalPolicyTpl(com.topvision.ems
     * .cmc.domain.CmcLoadBalPolicyTpl)
     */
    @Override
    public void addLoadBalPolicyTpl(CmcLoadBalPolicyTpl balPolicyTpl) {
        cmcLoadBalPolicyTplDao.insertLoadBalPolicyTpl(balPolicyTpl);
    }

    @Override
    public List<CmcLoadBalanceGroup> selectLoadBalanceGroupList(Long cmcId) throws SQLException {
        return cmcLoadBalanceDao.selectLoadBalanceGroupList(cmcId);
    }

    @Override
    public CmcLoadBalPolicy getLoadBalanceTimePolicy(Long cmcId) {
        return cmcLoadBalanceDao.queryLoadBalanceTimePolicy(cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#addLoadBalanceGroup(com.topvision.ems
     * .cmc.domain.CmcLoadBalanceGroup)
     */
    public void addLoadBalanceGroup(CmcLoadBalanceGroup cmcLoadBalanceGroup) {
        Long cmcId = cmcLoadBalanceGroup.getCmcId();
        List<CmcLoadBalChannel> cmcLoadBalChannels = cmcLoadBalanceGroup.getChannels();
        List<CmcLoadBalRestrictCm> ranges = cmcLoadBalanceGroup.getRanges();
        List<Long> docsLoadBalChannelIfIndexs = new ArrayList<Long>();
        List<String> macRangs = new ArrayList<String>();
        if (cmcLoadBalChannels != null) {
            for (int i = 0; i < cmcLoadBalChannels.size(); i++) {
                docsLoadBalChannelIfIndexs.add(cmcLoadBalChannels.get(i).getDocsLoadBalChannelIfIndex());
            }
        }
        if (ranges != null) {
            for (int i = 0; i < ranges.size(); i++) {
                macRangs.add(ranges.get(i).getTopLoadBalRestrictCmMacRang());
            }
        }
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        // 获取docsLoadBalGrpId
        List<Long> docsLoadBalGrpIds = cmcLoadBalanceDao.selectDocsLoadBalGrpIds(cmcId);
        Long docsLoadBalGrpId = null;
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        // 第一条的docsLoadBalGrpId值
        docsLoadBalGrpId = CmcIndexUtils.getNextDocsLoadBalGrpId(cmcIndex);
        if (docsLoadBalGrpIds.size() > 0 && docsLoadBalGrpIds.size() < CmcConstants.LOADBALGRP_MAX) {
            Long long1 = CmcIndexUtils
                    .getGroupIdFromDocsLoadBalGrpId(docsLoadBalGrpIds.get(docsLoadBalGrpIds.size() - 1));
            if (long1.intValue() == CmcConstants.LOADBALGRP_MAX) {// 如果最后一个是最大组值，则需要寻找中间的
                Long tempId = docsLoadBalGrpId;
                for (int i = 0; i < CmcConstants.LOADBALGRP_MAX; i++) {
                    if (docsLoadBalGrpIds.indexOf(tempId) == -1) {
                        docsLoadBalGrpId = tempId;
                    }
                    tempId = CmcIndexUtils.getNextDocsLoadBalGrpId(tempId);
                }
            } else {
                docsLoadBalGrpId = CmcIndexUtils
                        .getNextDocsLoadBalGrpId(docsLoadBalGrpIds.get(docsLoadBalGrpIds.size() - 1));
            }
        }

        getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addLoadBalGroupTable(snmpParam, docsLoadBalGrpId);
        Long grpId = cmcLoadBalanceDao.insertLoadBalGroup(cmcId, cmcLoadBalanceGroup.getGroupName(), docsLoadBalGrpId);
        getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addLoadBalGroupChannelTable(snmpParam, docsLoadBalGrpId,
                docsLoadBalChannelIfIndexs);
        cmcLoadBalanceDao.batchInsertLoadBalGroupChannels(grpId, docsLoadBalChannelIfIndexs);

        // 添加mac 地址段
        List<CmcLoadBalRestrictCm> cmcLoadBalRestrictCms = new ArrayList<CmcLoadBalRestrictCm>();
        Long beginIndex = 1l;
        for (int i = 0; i < macRangs.size(); i++) {
            // TODO 设备处理有延时，会导致设置第二个mac地址段时失败
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
            long index = beginIndex;
            CmcLoadBalRestrictCm cmcLoadBalRestrictCm = new CmcLoadBalRestrictCm();
            try {
                cmcLoadBalRestrictCm = getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addLoadBalGroupCmRangsTable(
                        snmpParam, docsLoadBalGrpId, index, formatMacRang(macRangs.get(i)));
            } catch (Exception e) {
                throw new LoadBalException(ResourcesUtil.getString("loadbalance.addLBSomeMacFaild"));
                //throw new LoadBalException(ResourcesUtil.getString("loadbalance.macRepeated"));
            }

            cmcLoadBalRestrictCm.setCmcId(cmcId);
            cmcLoadBalRestrictCm.setGrpId(grpId);
            cmcLoadBalRestrictCms.add(cmcLoadBalRestrictCm);

            beginIndex += 1;
        }
        cmcLoadBalanceDao.batchInsertCmcLoadBalRestrictCms(grpId, cmcLoadBalRestrictCms);
    }

    /**
     * 转成MIB要求的00:00:00:00:00:00 00:00:00:00:00:00的格式
     * @param macRang
     * @return
     */
    private String formatMacRang(String macRang) {
        if (macRang == null || macRang.indexOf("#") == -1) {
            return macRang;
        }
        String[] macs = macRang.split("#");
        if (macs.length != 2) {
            return macRang;
        }
        return MacUtils.convertToMaohaoFormat(macs[0]) + " " + MacUtils.convertToMaohaoFormat(macs[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#modifyLoadBalanceGroup(java.lang.String)
     */
    public void modifyLoadBalanceGroup(String groupName, Long grpId) throws SQLException {
        cmcLoadBalanceDao.updateLoadBalanceGroupName(groupName, grpId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#modifyLoadBalanceChannel(java.lang.Long,
     * java.lang.Long, java.util.List, java.util.List)
     */
    public void modifyLoadBalanceChannel(Long cmcId, Long grpId, List<Long> addChannelIndexs,
            List<Long> deleteChannelIndexs) {
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        Long docsLoadBalGrpId = cmcLoadBalanceDao.selectDocsLoadBalGrpIdByGrpId(grpId);
        getCmcLoadBalanceFacade(snmpParam.getIpAddress()).deleteLoadBalGroupChannelTable(snmpParam, docsLoadBalGrpId,
                deleteChannelIndexs);
        cmcLoadBalanceDao.batchDeleteLoadBalanceChannel(grpId, deleteChannelIndexs);
        getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addLoadBalGroupChannelTable(snmpParam, docsLoadBalGrpId,
                addChannelIndexs);
        cmcLoadBalanceDao.batchInsertLoadBalGroupChannels(grpId, addChannelIndexs);
    }

    @Override
    public void txAddCmcLoadBalPolicy(CmcLoadBalPolicy balPolicy) throws LoadBalException {
        snmpParam = getSnmpParamByEntityId(balPolicy.getEntityId());
        // 1,获取当前设备上所有的policies和rules
        // 数据库中的policies--MIB
        List<CmcLoadBalPolicy> cmcLoadBalPoliciesInDB = cmcLoadBalanceDao.selectLoadBalPolicyByEntityId(balPolicy
                .getEntityId());
        int policiesSize = cmcLoadBalPoliciesInDB.size();
        // 数据库中的rules--MIB
        List<CmcLoadBalBasicRule> cmcLoadBalBasicRulesInDB = cmcLoadBalanceDao
                .queryLoadBalanceRulesByEntityId(balPolicy.getEntityId());
        int rulesSize = cmcLoadBalBasicRulesInDB.size();

        // 2,判断policyRule表是否还能添加，判断rules表能否还能添加
        int policyRulesCounter = 0;
        List<CmcLoadBalBasicRule> addRules = new ArrayList<CmcLoadBalBasicRule>();// balPolicy.getRules();
        List<CmcLoadBalBasicRule> relRules = new ArrayList<CmcLoadBalBasicRule>();// balPolicy.getRules();

        for (Iterator<CmcLoadBalPolicy> iterator = cmcLoadBalPoliciesInDB.iterator(); iterator.hasNext();) {
            CmcLoadBalPolicy cmcLoadBalPolicy = iterator.next();
            policyRulesCounter += cmcLoadBalPolicy.getRules().size() * 1;// 每个policy下的rules
        }
        if (policyRulesCounter < 100) {// policy的限制是policy条数*对应的rule条数<100就可以添加，考虑到要添加的这个policy可能不只含有一个rule，所以还要考虑rule的情况
            // 过滤出需要add和需要关联的rule
            for (CmcLoadBalBasicRule r : balPolicy.getRules()) {
                CmcLoadBalBasicRule existRule = cmcLoadBalanceDao.selectLoadBalanceRuleByParam(r.getEntityId(), r
                        .getDocsLoadBalBasicRuleEnable(), r.getDocsLoadBalBasicRuleDisStart() != null ? r
                        .getDocsLoadBalBasicRuleDisStart().intValue() : null,
                        r.getDocsLoadBalBasicRuleDisPeriod() != null ? r.getDocsLoadBalBasicRuleDisPeriod().intValue()
                                : null);
                if (existRule != null) {
                    relRules.add(existRule);
                } else {
                    addRules.add(r);
                }
            }
            long[] policyIndexArr = new long[101];// 命令行限制100条,注意是只有一个rule时
            long[] ruleIndexArr = new long[101];// 命令行限制100条
            if ((addRules.size() + rulesSize) <= 100) {
                // policyIndex
                Long policyIndex = 0l;
                for (int i = 0; i < policiesSize; i++) {
                    Long lbPolicyId = cmcLoadBalPoliciesInDB.get(i).getDocsLoadBalPolicyId();
                    int lbPidInt = lbPolicyId.intValue();
                    policyIndexArr[lbPidInt] = (long) lbPolicyId;
                }
                for (int i = 1; i < policyIndexArr.length; i++) {
                    if (policyIndexArr[i] == 0) {
                        policyIndex = (long) i;
                        break;
                    }
                }
                // 向数据库插入policy
                balPolicy.setDocsLoadBalPolicyId(policyIndex);
                cmcLoadBalanceDao.insertLoadBalPolicy(balPolicy);
                // 获取ruleIndex
                Long ruleIndex = 0l;
                for (Iterator<CmcLoadBalBasicRule> iterator = addRules.iterator(); iterator.hasNext();) {
                    CmcLoadBalBasicRule cmcLoadBalBasicRule = iterator.next();
                    if (rulesSize != 0) {
                        for (int i = 0; i < rulesSize; i++) {
                            Long lbRuleId = cmcLoadBalBasicRulesInDB.get(i).getDocsLoadBalBasicRuleId();
                            int lbRidInt = 0;
                            if (lbRuleId != null) {
                                lbRidInt = lbRuleId.intValue();
                                ruleIndexArr[lbRidInt] = (long) lbRuleId;
                            }
                        }
                        for (int i = 1; i < ruleIndexArr.length; i++) {
                            if (ruleIndexArr[i] == 0) {
                                ruleIndex = (long) i;
                                ruleIndexArr[i] = -1;// 占位
                                break;
                            }
                        }
                    } else {
                        //ruleIndex = 1l;
                        ruleIndex++;
                    }
                    cmcLoadBalBasicRule.setDocsLoadBalBasicRuleId(ruleIndex);
                    getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addCmcLoadBalBasicRule(snmpParam,
                            cmcLoadBalBasicRule);
                    // 向设备插入rule和policy的关系
                    getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addCmcLoadBalPolicy(snmpParam, policyIndex,
                            ruleIndex);
                    // 向数据库插入rule和与policy的关系
                    cmcLoadBalanceDao.insertLoadBalBasicRule(cmcLoadBalBasicRule);
                    cmcLoadBalanceDao.insertPolicyRuleRef(balPolicy.getPolicyId(), cmcLoadBalBasicRule.getRuleId());
                }
                for (Iterator<CmcLoadBalBasicRule> iterator = relRules.iterator(); iterator.hasNext();) {
                    CmcLoadBalBasicRule cmcLoadBalBasicRule = iterator.next();
                    // 向设备插入rule和policy的关系
                    getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addCmcLoadBalPolicy(snmpParam, policyIndex,
                            cmcLoadBalBasicRule.getDocsLoadBalBasicRuleId());
                    cmcLoadBalanceDao.insertPolicyRuleRef(balPolicy.getPolicyId(), cmcLoadBalBasicRule.getRuleId());
                }
            } else {
                throw new LoadBalException(ResourcesUtil.getString("loadbalance.rulelimitedIn100"));
            }
        } else {
            throw new LoadBalException(ResourcesUtil.getString("loadbalance.policylimitedIn100"));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#modifyLoadBalanceMacRanges(java.lang.
     * Long, java.lang.Long, java.util.List, java.util.List)
     */
    public void modifyLoadBalanceMacRanges(Long cmcId, Long grpId, List<String> addMacRanges,
            List<Long> topLoadBalRestrictCmIndexs) {
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        Long docsLoadBalGrpId = cmcLoadBalanceDao.selectDocsLoadBalGrpIdByGrpId(grpId);
        if (topLoadBalRestrictCmIndexs.size() > 0) {
            getCmcLoadBalanceFacade(snmpParam.getIpAddress()).deleteLoadBalGroupCmRangsTable(snmpParam,
                    docsLoadBalGrpId, topLoadBalRestrictCmIndexs);
            cmcLoadBalanceDao.batchDeleteLoadBalanceMacRanges(grpId, topLoadBalRestrictCmIndexs);
        }

        // 添加cm mac段
        List<CmcLoadBalRestrictCm> cmcLoadBalRestrictCms = new ArrayList<CmcLoadBalRestrictCm>();
        List<Long> restrictCmIndexs = cmcLoadBalanceDao.selectRestrictCmIndexs(grpId);
        Long beginIndex;
        // modify by @bravin: 如果之前没有range,则restrictCmIndexs.get(restrictCmIndexs.size() - 1) 会下标越界。
        if (restrictCmIndexs.size() == 0) {
            beginIndex = 1l;
        } else {
            beginIndex = restrictCmIndexs.get(restrictCmIndexs.size() - 1) + 1;
        }
        // modify by @bravin : 如果没有新增range,则不处理
        if (addMacRanges.size() > 0) {

            for (int i = 0; i < addMacRanges.size(); i++) {
                // TODO 设备处理有延时，会导致设置第二个mac地址段时失败
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
                long index = beginIndex;
                CmcLoadBalRestrictCm cmcLoadBalRestrictCm = new CmcLoadBalRestrictCm();
                try {
                    cmcLoadBalRestrictCm = getCmcLoadBalanceFacade(snmpParam.getIpAddress())
                            .addLoadBalGroupCmRangsTable(snmpParam, docsLoadBalGrpId, index,
                                    formatMacRang(addMacRanges.get(i)));
                } catch (Exception e) {
                    logger.error("", e);
                    //throw new LoadBalException(ResourcesUtil.getString("loadbalance.macRepeated"));
                    throw new LoadBalException(ResourcesUtil.getString("loadbalance.addLBSomeMacFaild"));
                }

                cmcLoadBalRestrictCm.setCmcId(cmcId);
                cmcLoadBalRestrictCm.setGrpId(grpId);
                cmcLoadBalRestrictCms.add(cmcLoadBalRestrictCm);

                beginIndex += 1;
            }
            cmcLoadBalanceDao.batchInsertCmcLoadBalRestrictCms(grpId, cmcLoadBalRestrictCms);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#deleteLoadBalanceGroup(java.lang.Long,
     * java.lang.Long)
     */
    public void deleteLoadBalanceGroup(Long docsLoadBalGrpId, Long cmcId) throws SQLException {
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        getCmcLoadBalanceFacade(snmpParam.getIpAddress()).deleteBalGroupTable(snmpParam, docsLoadBalGrpId);
        cmcLoadBalanceDao.deleteLoadBalanceGroup(docsLoadBalGrpId, cmcId);
    }

    @Override
    public void txModifyLoadBalPolicy(CmcLoadBalPolicy balPolicy) {
        snmpParam = getSnmpParamByEntityId(balPolicy.getEntityId());

        CmcLoadBalPolicy oldPolicy = cmcLoadBalanceDao.selectLoadBalPolicyById(balPolicy.getPolicyId());
        Long policyIndex = oldPolicy.getDocsLoadBalPolicyId();
        List<CmcLoadBalBasicRule> rs = oldPolicy.getRules();
        // 删除设备上policy和rule的对应关系--policy
        for (CmcLoadBalBasicRule r : rs) {
            getCmcLoadBalanceFacade(snmpParam.getIpAddress()).deleteCmcLoadBalPolicy(snmpParam, policyIndex,
                    r.getDocsLoadBalBasicRuleId());
            if (cmcLoadBalanceDao.selectLoadBalancePolicyCountByRuleId(r.getRuleId()) <= 1) {// 只有一个使用（当前的policy）
                getCmcLoadBalanceFacade(snmpParam.getIpAddress()).deleteCmcLoadBalBasicRule(snmpParam,
                        r.getDocsLoadBalBasicRuleId());
                cmcLoadBalanceDao.deleteLoadBalRule(r.getRuleId());
            }
        }
        // 删除数据库rule和policy的对应关系
        cmcLoadBalanceDao.deleteLoadBalPolicyRuleRefByPolicyId(balPolicy.getPolicyId());
        List<CmcLoadBalBasicRule> rules = balPolicy.getRules();
        List<CmcLoadBalBasicRule> dbRules = cmcLoadBalanceDao.queryLoadBalanceRulesByEntityId(balPolicy.getEntityId());
        long[] ll = new long[101];// 命令行限制100条
        for (CmcLoadBalBasicRule r : rules) {
            CmcLoadBalBasicRule existRule = cmcLoadBalanceDao.selectLoadBalanceRuleByParam(r.getEntityId(), r
                    .getDocsLoadBalBasicRuleEnable(), r.getDocsLoadBalBasicRuleDisStart() != null ? r
                    .getDocsLoadBalBasicRuleDisStart().intValue() : null,
                    r.getDocsLoadBalBasicRuleDisPeriod() != null ? r.getDocsLoadBalBasicRuleDisPeriod().intValue()
                            : null);
            if (existRule != null) {// 存在，则直接建立关系
                // 设备
                getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addCmcLoadBalPolicy(snmpParam, policyIndex,
                        existRule.getDocsLoadBalBasicRuleId());
                // 数据库
                cmcLoadBalanceDao.insertPolicyRuleRef(balPolicy.getPolicyId(), existRule.getRuleId());
            } else {
                Long ruleIndex = 0l;
                if (dbRules != null) {
                    int ruleSize = dbRules.size();
                    for (int i = 0; i < ruleSize; i++) {
                        Long lbRuleId = dbRules.get(i).getDocsLoadBalBasicRuleId();
                        int lbRidInt = 0;
                        if (lbRuleId != null) {
                            lbRidInt = lbRuleId.intValue();
                            ll[lbRidInt] = (long) lbRuleId;
                        }
                    }
                    for (int i = 1; i < ll.length; i++) {
                        if (ll[i] == 0) {
                            ruleIndex = (long) i;
                            ll[i] = -1;// 站位，表示已用
                            break;
                        }
                    }
                } else {
                    ruleIndex = 1l;
                }

                if (ruleIndex == 0l) {
                    throw new LoadBalException(ResourcesUtil.getString("loadbalance.rulelimitedIn100"));
                } else {
                    r.setDocsLoadBalBasicRuleId(ruleIndex);

                    r.setEntityId(balPolicy.getEntityId());
                    // 设备
                    getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addCmcLoadBalBasicRule(snmpParam, r);
                    getCmcLoadBalanceFacade(snmpParam.getIpAddress()).addCmcLoadBalPolicy(snmpParam, policyIndex,
                            ruleIndex);
                    // 数据库
                    cmcLoadBalanceDao.insertLoadBalBasicRule(r);
                    cmcLoadBalanceDao.insertPolicyRuleRef(balPolicy.getPolicyId(), r.getRuleId());
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcLoadBalanceService#getLoadBalPolicyById(java.lang.Long)
     */
    @Override
    public CmcLoadBalPolicy getLoadBalPolicyById(Long policyId) {
        return cmcLoadBalanceDao.selectLoadBalPolicyById(policyId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcLoadBalanceService#modifyCcmtsPolicy(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void modifyCcmtsPolicy(Long cmcId, Long policyId) {
        Long ifIndex = cmcService.getCmcIndexByCmcId(cmcId);
        snmpParam = getSnmpParamByCmcId(cmcId);// getSnmpParamByEntityId(balPolicy.getEntityId());
        // 设备
        CmcLoadBalPolicy balPolicy = cmcLoadBalanceDao.selectLoadBalPolicyById(policyId);
        long loadBalPolicyId = balPolicy == null ? 0 : balPolicy.getDocsLoadBalPolicyId();
        getCmcLoadBalanceFacade(snmpParam.getIpAddress())
                .modifyCmcLoadBalTopPolicy(snmpParam, ifIndex, loadBalPolicyId);
        // 数据库
        cmcLoadBalanceDao.deleteTopCcmtsLoadBalPolicyByCmcId(cmcId);
        if (balPolicy != null) {
            cmcLoadBalanceDao.insertTopCcmtsLoadBalPolicy(cmcId, policyId);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcLoadBalanceService#deleteLoadBalPolicy(java.lang.Long)
     */
    @Override
    public void deleteLoadBalPolicy(Long policyId) {
        // 删除设备上的数据
        CmcLoadBalPolicy balPolicy = cmcLoadBalanceDao.selectLoadBalPolicyById(policyId);
        snmpParam = getSnmpParamByEntityId(balPolicy.getEntityId());
        Long policyIndex = balPolicy.getDocsLoadBalPolicyId();
        List<CmcLoadBalBasicRule> rs = balPolicy.getRules();
        List<CmcLoadBalBasicRule> rules = new ArrayList<CmcLoadBalBasicRule>();
        // 判断rule是否被其它policy引用，如果引用则不处理，否则删除rule
        for (CmcLoadBalBasicRule cmcLoadBalBasicRule : rs) {
            Long ruleId = cmcLoadBalBasicRule.getRuleId();
            Long docsLoadBalPolicyRuleId = cmcLoadBalBasicRule.getDocsLoadBalBasicRuleId();
            if (cmcLoadBalanceDao.selectLoadBalancePolicyCountByRuleId(ruleId) <= 1) {// 只有一个使用（当前的policy）
                rules.add(cmcLoadBalBasicRule);
                // 删除某条rule，则如果只有某条policy中只有一条rule，则对应的policy在policy中就不存在了--MIB测试得到的信息
                getCmcLoadBalanceFacade(snmpParam.getIpAddress()).deleteCmcLoadBalBasicRule(snmpParam,
                        docsLoadBalPolicyRuleId);
            } else {
                getCmcLoadBalanceFacade(snmpParam.getIpAddress()).deleteCmcLoadBalPolicy(snmpParam, policyIndex,
                        docsLoadBalPolicyRuleId);
            }

        }

        // 删除数据库中的数据
        cmcLoadBalanceDao.deleteLoadBalPolicy(policyId);
        cmcLoadBalanceDao.deleteLoadBalRules(rules);
    }

    @Override
    public CmcLoadBalanceGroup getLoadBalanceGroup(Long grpId) {
        return cmcLoadBalanceDao.selectLoadBalanceGroup(grpId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#refreshLoadBalanceConfig(java.lang.Long)
     */
    public void refreshLoadBalanceConfig(Long cmcId) {
        Long entityId = getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        CmcLoadBalCfg cmcLoadBalCfg = new CmcLoadBalCfg();
        cmcLoadBalCfg.setCmcId(cmcId);
        cmcLoadBalCfg.setTopLoadBalConfigCmcIndex(cmcIndex);
        cmcLoadBalCfg = (CmcLoadBalCfg) getCmcLoadBalanceFacade(snmpParam.getIpAddress()).getCmcLoadBalConfig(
                snmpParam, cmcLoadBalCfg);
        cmcLoadBalanceDao.insertOrUpdateCmcLoadBalCfg(cmcLoadBalCfg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#refreshLoadBalanceGroup(java.lang.Long)
     */
    public void refreshLoadBalanceGroup(Long cmcId) {
        Long entityId = getEntityIdByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        List<CmcLoadBalGrp> cmcLoadBalGrps = getCmcLoadBalanceFacade(snmpParam.getIpAddress()).getCmcLoadBalGroup(
                snmpParam);
        List<CmcLoadBalChannel> cmcLoadBalChannels = getCmcLoadBalanceFacade(snmpParam.getIpAddress())
                .getCmcLoadBalGroupChannel(snmpParam);
        List<CmcLoadBalRestrictCm> cmcLoadBalRestrictCms = getCmcLoadBalanceFacade(snmpParam.getIpAddress())
                .getCmcLoadBalRestrictCm(snmpParam);
        Integer typeId = cmcService.getCmcTypeByCmcId(cmcId);
        if (entityTypeService.isCcmtsWithoutAgent(typeId.longValue())) {
            cmcLoadBalanceDao.batchInsertCmcLoadBalGrp(cmcLoadBalGrps, entityId);
            cmcLoadBalanceDao.batchInsertCmcLoadBalChannel(cmcLoadBalChannels, entityId);
            cmcLoadBalanceDao.batchInsertCmc8800bLoadBalRestrictCm(cmcLoadBalRestrictCms, cmcId);
        } else {
            cmcLoadBalanceDao.batchInsertCmc8800BLoadBalGrp(cmcLoadBalGrps, cmcId);
            cmcLoadBalanceDao.batchInsertCmc8800BLoadBalChannel(cmcLoadBalChannels, cmcId);
            cmcLoadBalanceDao.batchInsertCmc8800bLoadBalRestrictCm(cmcLoadBalRestrictCms, cmcId);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcLoadBalanceService#refreshLoadBalanceExcludeCm(java.lang
     * .Long)
     */
    public void refreshLoadBalanceExcludeCm(Long cmcId) {
        Long entityId = getEntityIdByCmcId(cmcId);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        snmpParam = getSnmpParamByEntityId(entityId);
        List<CmcLoadBalExcludeCm> cmcLoadBalExcludeCms = getCmcLoadBalanceFacade(snmpParam.getIpAddress())
                .getCmcLoadBalExcludeCm(snmpParam);
        List<CmcLoadBalExcludeCm> CmcLoadBalExcludeCmForDB = new ArrayList<CmcLoadBalExcludeCm>();
        if (cmcLoadBalExcludeCms != null) {
            for (int i = 0; i < cmcLoadBalExcludeCms.size(); i++) {
                if (cmcIndex.equals(cmcLoadBalExcludeCms.get(i).getIfIndex())) {
                    CmcLoadBalExcludeCmForDB.add(cmcLoadBalExcludeCms.get(i));
                }
            }
        }
        cmcLoadBalanceDao.batchInsertCmcLoadBalExcludeCm(CmcLoadBalExcludeCmForDB, cmcId);
    }

    private void refreshLoadBalancePolicy(Long entityId) {
        snmpParam = getSnmpParamByEntityId(entityId);
        CmcLoadBalanceFacade facade = getCmcLoadBalanceFacade(snmpParam.getIpAddress());
        List<CmcLoadBalPolicy> policies = facade.getCmcLoadBalPolicies(snmpParam);
        List<CmcLoadBalBasicRule> rules = facade.getCmcLoadBalBasicRules(snmpParam);
        List<CmcLoadBalTopPolicy> topPolicies = facade.getCmcLoadBalTopPolicies(snmpParam);
        refreshLoadBalPolicyAndRules(entityId, rules, policies, topPolicies);
    }

    private void refreshLoadBalPolicyAndRules(Long entityId, List<CmcLoadBalBasicRule> rules,
            List<CmcLoadBalPolicy> policyRuleRefs, List<CmcLoadBalTopPolicy> ccPolicys) {
        // 数据库端
        cmcLoadBalanceDao.deleteLoadBalPolicyByEntityId(entityId);
        cmcLoadBalanceDao.deleteLoadBalRuleByEntityId(entityId);

        // 向docsloadbalbasicrule表中插入记录
        for (CmcLoadBalBasicRule r : rules) {
            r.setEntityId(entityId);
            cmcLoadBalanceDao.insertLoadBalBasicRule(r);
        }

        // 整理docsLoadBalPolicyId和docsLoadBalPolicyRuleId的对应关系
        Map<Long, List<Long>> map = new HashMap<Long, List<Long>>();
        for (CmcLoadBalPolicy ref : policyRuleRefs) {
            Long docsLoadBalPolicyId = ref.getDocsLoadBalPolicyId();
            Long docsLoadBalPolicyRuleId = ref.getDocsLoadBalPolicyRuleId();
            if (map.containsKey(docsLoadBalPolicyId)) {
                map.get(docsLoadBalPolicyId).add(docsLoadBalPolicyRuleId);
            } else {
                List<Long> docsLoadBalPolicyRuleIds = new ArrayList<Long>();
                docsLoadBalPolicyRuleIds.add(docsLoadBalPolicyRuleId);
                map.put(docsLoadBalPolicyId, docsLoadBalPolicyRuleIds);
            }
        }

        // 向docsloadbalpolicy和policyruleref表中插入记录
        List<CmcLoadBalPolicy> insertedPolicys = new ArrayList<CmcLoadBalPolicy>();
        Iterator<Entry<Long, List<Long>>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Long, List<Long>> entry = iter.next();
            Long docsLoadBalPolicyId = entry.getKey();
            List<Long> docsLoadBalPolicyRuleIds = entry.getValue();

            // 插入policy
            CmcLoadBalPolicy p = new CmcLoadBalPolicy();
            p.setEntityId(entityId);
            p.setDocsLoadBalPolicyId(docsLoadBalPolicyId);
            cmcLoadBalanceDao.insertLoadBalPolicy(p);
            insertedPolicys.add(p);

            // 插入policy和rule的关系
            Long policyId = p.getPolicyId();
            Long ruleId = null;
            for (Long docsLoadBalPolicyRuleId : docsLoadBalPolicyRuleIds) {
                for (CmcLoadBalBasicRule r : rules) {
                    if (r.getDocsLoadBalBasicRuleId().equals(docsLoadBalPolicyRuleId)) {
                        ruleId = r.getRuleId();
                        break;
                    }
                }
                if (ruleId != null) {
                    cmcLoadBalanceDao.insertPolicyRuleRef(policyId, ruleId);
                }
            }
        }

        // 插入CC的负载均衡时间策略
        for (CmcLoadBalTopPolicy ccPolicy : ccPolicys) {
            Long policyId = null;
            for (CmcLoadBalPolicy policy : insertedPolicys) {
                if (policy.getDocsLoadBalPolicyId().equals(ccPolicy.getTopLoadBalPolicyId())) {
                    policyId = policy.getPolicyId();
                }
            }
            Map<String, Long> cmcIdMap = new HashMap<String, Long>();
            cmcIdMap.put("entityId", entityId);
            cmcIdMap.put("cmcIndex", ccPolicy.getIfIndex());
            Long _cmcId = cmcLoadBalanceDao.getCmcIdByCmcIndexAndEntityId(cmcIdMap);
            if (policyId != null && _cmcId != null) {
                cmcLoadBalanceDao.deleteTopCcmtsLoadBalPolicyByCmcId(_cmcId);
                cmcLoadBalanceDao.insertTopCcmtsLoadBalPolicy(_cmcId, policyId);
            }
        }
    }

    @Override
    public List<CmcLoadBalRestrictCm> queryAllRestrictCms(Long cmcId) {
        return cmcLoadBalanceDao.queryAllRestrictCms(cmcId);
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        //TODO dhcp relay待重构，沿用之前的刷新方法
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())
                || event.getEntityType().equals(entityTypeService.getCcmtswithoutagentType())) {
            try {
                Long entityId = event.getEntityId();
                List<Long> cmcIndexList = event.getCmcIndexList();
                logger.debug("Begin to discovery Cmc Load Balance Config.");
                for (int i = 0; i < cmcIndexList.size(); i++) {
                    Map<String, Long> map = new HashMap<String, Long>();
                    map.put("entityId", entityId);
                    map.put("cmcIndex", cmcIndexList.get(i));
                    Long cmcId = cmcLoadBalanceDao.getCmcIdByCmcIndexAndEntityId(map);
                    if (cmcId != null) {
                        refreshLoadBalanceConfig(cmcId);
                        refreshLoadBalanceGroup(cmcId);
                        refreshLoadBalanceExcludeCm(cmcId);
                    }
                }
                refreshLoadBalancePolicy(entityId);
                logger.debug("Refresh Cmc Load Balance Config finish.");
            } catch (Exception e) {
                logger.error("Refresh Cmc Load Balance Config wrong.", e);
            }
        }
    }

}