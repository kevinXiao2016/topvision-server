/***********************************************************************
 * $Id: CmcLoadBalanceDaoImpl.java,v1.0 2011-12-8 上午11:05:39 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.dao.mybatis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.loadbalance.dao.CmcLoadBalanceDao;
import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalanceGroup;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalBasicRule;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalCfg;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalChannel;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalExcludeCm;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalGrp;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalPolicy;
import com.topvision.ems.cmc.loadbalance.facade.domain.CmcLoadBalRestrictCm;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2011-12-8-上午11:05:39
 * 
 */

@Repository("cmcLoadBalanceDao")
public class CmcLoadBalanceDaoImpl extends MyBatisDaoSupport<Entity> implements CmcLoadBalanceDao {
    private static Logger logger = LoggerFactory.getLogger(CmcLoadBalanceDaoImpl.class);

    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalance";
    }

    @Override
    public CmcLoadBalCfg queryLoadBalanceGlobalCfg(Long cmcId) throws SQLException {
        return (CmcLoadBalCfg) getSqlSession().selectOne(getNameSpace() + "queryLoadBalanceGlobalCfg", cmcId);
    }

    @Override
    public List<CmcLoadBalExcludeCm> selectLoadBalanceExcMacRange(Long cmcId) throws SQLException {
        return getSqlSession().selectList(getNameSpace() + "selectLoadBalanceExcMacRange", cmcId);
    }

    @Override
    public List<CmcLoadBalanceGroup> selectLoadBalanceGroupList(final Long cmcId) throws SQLException {

        List<CmcLoadBalanceGroup> groups = getSqlSession().selectList(getNameSpace() + "selectLoadBalanceGroupList",
                cmcId);
        for (CmcLoadBalanceGroup group : groups) {
            try {
                List<CmcLoadBalChannel> channels = getSqlSession().selectList(getNameSpace() + "selectLBChannelList",
                        group);
                group.setChannels(channels);
            } catch (Exception e) {
                logger.error("fetch load balance group's channel list error : {}", e);
            }

            try {
                List<CmcLoadBalRestrictCm> ranges = getSqlSession().selectList(getNameSpace() + "selectLBMacRangeList",
                        group);
                group.setRanges(ranges);
            } catch (Exception e) {
                logger.error("fetch load balance group's mac range list error : {}", e);
            }
        }
        return groups;

    }

    @Override
    public CmcLoadBalPolicy queryLoadBalanceTimePolicy(final Long cmcId) {

        CmcLoadBalPolicy policy = (CmcLoadBalPolicy) getSqlSession().selectOne(
                getNameSpace() + "selectLoadBalancePolicy", cmcId);
        if (policy != null) {
            Long policyId = policy.getPolicyId();
            CmcLoadBalPolicy po = (CmcLoadBalPolicy) getSqlSession().selectOne(
                    getNameSpace() + "selectLoadBalPolicyById", policyId);
            policy.setDocsLoadBalPolicyId(po.getDocsLoadBalPolicyId());
            List<CmcLoadBalBasicRule> rules = getSqlSession().selectList(getNameSpace() + "selectLoadBalanceRules",
                    policyId);
            policy.setRules(rules);
        }

        return policy;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#updateLoadBalanceGlobalCfg
     * (com.topvision.ems.cmc.facade.domain.CmcLoadBalCfg)
     */
    public void updateLoadBalanceGlobalCfg(CmcLoadBalCfg cmcLoadBalCfg) throws SQLException {
        getSqlSession()
                .update("com.topvision.ems.cmc.discovery.domain.CmcDiscovery.updateCmcLoadBalCfg", cmcLoadBalCfg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#addCmcLoadBalExcludeCmTable
     * (com.topvision.ems.cmc.facade.domain.CmcLoadBalExcludeCm)
     */
    public void insertCmcLoadBalExcludeCmTable(CmcLoadBalExcludeCm cmcLoadBalExcludeCm) {
        getSqlSession().update("com.topvision.ems.cmc.discovery.domain.CmcDiscovery.insertCmcCmcLoadBalExcludeCm",
                cmcLoadBalExcludeCm);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#deleteLoadBalanceExcMacRange
     * (com.topvision.ems.cmc.facade.domain.CmcLoadBalExcludeCm)
     */
    public void deleteLoadBalanceExcMacRange(CmcLoadBalExcludeCm cmcLoadBalExcludeCm) {
        getSqlSession().update(getNameSpace() + "deleteLoadBalanceExcMacRange", cmcLoadBalExcludeCm);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#deleteLoadBalanceGroup(java
     * .lang.Long, java.lang.Long)
     */
    public void deleteLoadBalanceGroup(Long docsLoadBalGrpId, Long cmcId) throws SQLException {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("docsLoadBalGrpId", docsLoadBalGrpId);
        map.put("cmcId", cmcId);
        getSqlSession().update(getNameSpace() + "deleteLoadBalanceGroup", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#addLoadBalGroup(java.lang
     * .Long, java.lang.String, java.lang.Long)
     */
    public Long insertLoadBalGroup(Long cmcId, String groupName, Long docsLoadBalGrpId) {
        CmcLoadBalGrp cmcLoadBalGrp = new CmcLoadBalGrp();
        cmcLoadBalGrp.setCmcId(cmcId);
        cmcLoadBalGrp.setGroupName(groupName);
        cmcLoadBalGrp.setDocsLoadBalGrpId(docsLoadBalGrpId);
        SqlSession sqlSession = getSqlSession();
        Long grpId = -1l;
        sqlSession.insert("com.topvision.ems.cmc.discovery.domain.CmcDiscovery.insertCmcLoadBalGrp", cmcLoadBalGrp);
        grpId = sqlSession.selectOne(getNameSpace() + "selectLoadBalanceGroupId", cmcLoadBalGrp);

        return grpId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#batchInsertLoadBalGroupChannels
     * (java.lang.Long, java.util.List)
     */
    public void batchInsertLoadBalGroupChannels(final Long grpId, final List<Long> docsLoadBalChannelIfIndexs) {

        //SqlSession sqlSession = getBatchSession();
        try {
            for (Long docsLoadBalChannelIfIndex : docsLoadBalChannelIfIndexs) {
                CmcLoadBalChannel cmcLoadBalChannel = new CmcLoadBalChannel();
                cmcLoadBalChannel.setGrpId(grpId);
                cmcLoadBalChannel.setDocsLoadBalChannelIfIndex(docsLoadBalChannelIfIndex);
                getSqlSession().insert(getNameSpace() + "insertLoadBalGroupChannel", cmcLoadBalChannel);
            }
            //sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            //sqlSession.rollback();
        } finally {
            //sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#batchInsertCmcLoadBalRestrictCms
     * (java.lang.Long, java.util.List)
     */
    public void batchInsertCmcLoadBalRestrictCms(final Long grpId,
            final List<CmcLoadBalRestrictCm> cmcLoadBalRestrictCms) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (CmcLoadBalRestrictCm cmcLoadBalRestrictCm : cmcLoadBalRestrictCms) {
                cmcLoadBalRestrictCm.setGrpId(grpId);
                sqlSession.insert(
                        "com.topvision.ems.cmc.discovery.domain.CmcDiscovery." + "insertCmcLoadBalRestrictCm",
                        cmcLoadBalRestrictCm);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#updateLoadBalanceGroupName
     * (java.lang.String, java.lang.Long)
     */
    public void updateLoadBalanceGroupName(String groupName, Long grpId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupName", groupName);
        map.put("grpId", grpId);
        getSqlSession().update(getNameSpace() + "updateLoadBalanceGroupName", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#batchDeleteLoadBalanceChannel
     * (java.lang.Long, java.util.List)
     */
    public void batchDeleteLoadBalanceChannel(final Long grpId, final List<Long> deleteChannelIndexs) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (Long deleteChannelIndex : deleteChannelIndexs) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("grpId", grpId);
                map.put("docsLoadBalChannelIfIndex", deleteChannelIndex);
                sqlSession.update(getNameSpace() + "deleteLoadBalanceChannel", map);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#selectDocsLoadBalGrpIdByGrpId
     * (java.lang.Long)
     */
    public Long selectDocsLoadBalGrpIdByGrpId(Long grpId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "selectDocsLoadBalGrpIdByGrpId", grpId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#batchDeleteLoadBalanceMacRanges
     * (java.lang.Long, java.util.List)
     */
    public void batchDeleteLoadBalanceMacRanges(final Long grpId, final List<Long> topLoadBalRestrictCmIndexs) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (Long topLoadBalRestrictCmIndex : topLoadBalRestrictCmIndexs) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("grpId", grpId);
                map.put("topLoadBalRestrictCmIndex", topLoadBalRestrictCmIndex);
                sqlSession.update(getNameSpace() + "deleteLoadBalanceMacRange", map);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#selectRestrictCmIndexs(java
     * .lang.Long)
     */
    public List<Long> selectRestrictCmIndexs(Long grpId) {
        return getSqlSession().selectList(getNameSpace() + "selectRestrictCmIndexs", grpId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#selectDocsLoadBalGrpIds(java
     * .lang.Long)
     */
    public List<Long> selectDocsLoadBalGrpIds(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "selectDocsLoadBalGrpIds", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#insertLoadBalPolicy(com.topvision
     * .ems.cmc.facade.domain.CmcLoadBalPolicy)
     */
    @Override
    public void insertLoadBalPolicy(CmcLoadBalPolicy balPolicy) {
        getSqlSession().insert(getNameSpace() + "insertLoadBalPolicy", balPolicy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#insertPolicyRuleRef(java.
     * lang.Long, java.lang.Long)
     */
    @Override
    public void insertPolicyRuleRef(Long policyId, Long ruleId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ruleId", ruleId);
        params.put("policyId", policyId);
        getSqlSession().insert(getNameSpace() + "insertPolicyRuleRef", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#insertLoadBalBasicRule(com
     * .topvision.ems.cmc.facade.domain.CmcLoadBalBasicRule)
     */
    @Override
    public void insertLoadBalBasicRule(CmcLoadBalBasicRule balBasicRule) {
        getSqlSession().insert(getNameSpace() + "insertLoadBalBasicRule", balBasicRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.framework.dao.ibatis.IbatisDaoSupport#insertEntity(java
     * .lang.Object)
     */
    @Override
    public void insertEntity(Entity entity) {
        super.insertEntity(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#selectLoadBalPolicyByEntityId
     * (java.lang.Long)
     */
    @Override
    public List<CmcLoadBalPolicy> selectLoadBalPolicyByEntityId(final Long entityId) {

        List<CmcLoadBalPolicy> policyList = getSqlSession().selectList(
                getNameSpace() + "selectLoadBalPolicyByEntityId", entityId);
        for (CmcLoadBalPolicy p : policyList) {
            List<Object> rulesList = getSqlSession().selectList(getNameSpace() + "selectLoadBalanceRules",
                    p.getPolicyId());
            List<CmcLoadBalBasicRule> temp = new ArrayList<CmcLoadBalBasicRule>();
            for (Object obj : rulesList) {
                temp.add((CmcLoadBalBasicRule) obj);
            }
            p.setRules(temp);
        }
        return policyList;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#deleteLoadBalPolicy(java.
     * lang.Long)
     */
    @Override
    public void deleteLoadBalPolicy(final Long policyId) {

        SqlSession sqlSession = getBatchSession();
        try {

            sqlSession.delete(getNameSpace() + "deleteLoadBalPolicyRuleRefByPolicyId", policyId);
            sqlSession.delete(getNameSpace() + "deleteLoadBalPolicy", policyId);
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#updateCmcLoadBalRules(java
     * .util.List)
     */
    @Override
    public void updateCmcLoadBalRules(final List<CmcLoadBalBasicRule> rules) {
        SqlSession sqlSession = getBatchSession();
        try {

            for (CmcLoadBalBasicRule r : rules) {
                sqlSession.update(getNameSpace() + "updateLoadBalRule", r);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#deleteLoadBalRules(java.util
     * .List)
     */
    @Override
    public void deleteLoadBalRules(final List<CmcLoadBalBasicRule> rules) {

        SqlSession sqlSession = getBatchSession();
        try {
            for (CmcLoadBalBasicRule r : rules) {
                sqlSession.update(getNameSpace() + "deleteLoadBalRule", r.getRuleId());
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#getLoadBalanceRulesByPolicyId
     * (java.lang.Long)
     */
    @Override
    public List<CmcLoadBalBasicRule> getLoadBalanceRulesByPolicyId(Long policyId) {
        return getSqlSession().selectList(getNameSpace() + "selectLoadBalanceRules", policyId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#selectLoadBalanceRuleByParam
     * (java.lang.Long, java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public CmcLoadBalBasicRule selectLoadBalanceRuleByParam(Long entityId, Integer docsLoadBalBasicRuleEnable,
            Integer docsLoadBalBasicRuleDisStart, Integer docsLoadBalBasicRuleDisPeriod) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("docsLoadBalBasicRuleEnable", docsLoadBalBasicRuleEnable);
        params.put("docsLoadBalBasicRuleDisStart", docsLoadBalBasicRuleDisStart);
        params.put("docsLoadBalBasicRuleDisPeriod", docsLoadBalBasicRuleDisPeriod);
        List<CmcLoadBalBasicRule> rules = getSqlSession().selectList(getNameSpace() + "selectLoadBalanceRuleByParam",
                params);
        return rules != null && rules.size() != 0 ? rules.get(0) : null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#deleteLoadBalRulesByPolicyId
     * (java.lang.Long)
     */
    @Override
    public void deleteLoadBalRulesByPolicyId(Long policyId) {
        getSqlSession().delete(getNameSpace() + "deleteLoadBalRulesByPolicyId", policyId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcLoadBalanceDao#
     * deleteLoadBalPolicyRuleRefByPolicyId(java.lang.Long)
     */
    @Override
    public void deleteLoadBalPolicyRuleRefByPolicyId(Long policyId) {
        getSqlSession().delete(getNameSpace() + "deleteLoadBalPolicyRuleRefByPolicyId", policyId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#selectLoadBalPolicyById(java
     * .lang.Long)
     */
    @Override
    public CmcLoadBalPolicy selectLoadBalPolicyById(Long policyId) {
        CmcLoadBalPolicy policy = (CmcLoadBalPolicy) getSqlSession().selectOne(
                getNameSpace() + "selectLoadBalPolicyById", policyId);
        if (policy != null) {
            policy.setRules(this.getLoadBalanceRulesByPolicyId(policy.getPolicyId()));
        }
        return policy;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcLoadBalanceDao#
     * deleteTopCcmtsLoadBalPolicyByCmcId(java.lang.Long)
     */
    @Override
    public void deleteTopCcmtsLoadBalPolicyByCmcId(Long cmcId) {
        getSqlSession().delete(getNameSpace() + "deleteTopCcmtsLoadBalPolicyByCmcId", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#deleteLoadBalPolicyByEntityId
     * (java.lang.Long)
     */
    @Override
    public void deleteLoadBalPolicyByEntityId(Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteLoadBalPolicyByEntityId", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#deleteLoadBalRuleByEntityId
     * (java.lang.Long)
     */
    @Override
    public void deleteLoadBalRuleByEntityId(Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteLoadBalRuleByEntityId", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#insertTopCcmtsLoadBalPolicy
     * (java.lang.Long, java.lang.Long)
     */
    @Override
    public void insertTopCcmtsLoadBalPolicy(Long cmcId, Long policyId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cmcId", cmcId);
        params.put("policyId", policyId);
        getSqlSession().insert(getNameSpace() + "insertTopCcmtsLoadBalPolicy", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#selectAllLoadBalBasicRuleId()
     */
    @Override
    public Long selectMaxLoadBalBasicRuleId() {
        Long ret = (Long) getSqlSession().selectOne(getNameSpace() + "selectMaxLoadBalBasicRuleId");
        return ret == null ? 0 : ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#selectAllLoadBalPolicyId()
     */
    @Override
    public Long selectMaxLoadBalPolicyId() {
        Long ret = (Long) getSqlSession().selectOne(getNameSpace() + "selectMaxLoadBalPolicyId");
        return ret == null ? 0 : ret;
    }

    @Override
    public CmcLoadBalanceGroup selectLoadBalanceGroup(final Long grpId) {

        CmcLoadBalanceGroup group = (CmcLoadBalanceGroup) getSqlSession().selectOne(
                getNameSpace() + "selectLoadBalanceGroup", grpId);
        try {
            List<CmcLoadBalChannel> channels = getSqlSession()
                    .selectList(getNameSpace() + "selectLBChannelList", group);
            group.setChannels(channels);
        } catch (Exception e) {
            logger.error("fetch load balance group's channel list error : {}", e);
        }
        try {
            List<CmcLoadBalRestrictCm> ranges = getSqlSession().selectList(getNameSpace() + "selectLBMacRangeList",
                    group);
            group.setRanges(ranges);
        } catch (Exception e) {
            logger.error("fetch load balance group's mac range list error : {}", e);
        }

        return group;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcLoadBalanceDao#
     * selectLoadBalancePolicyCountByRuleId(java.lang.Long)
     */
    @Override
    public Integer selectLoadBalancePolicyCountByRuleId(Long ruleId) {

        return (Integer) getSqlSession().selectOne(getNameSpace() + "selectLoadBalancePolicyCountByRuleId", ruleId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#queryLoadBalanceRules(java
     * .lang.Long)
     */
    @Override
    public List<CmcLoadBalBasicRule> queryLoadBalanceRulesByEntityId(Long entityId) {
        // TODO Auto-generated method stub
        return getSqlSession().selectList(getNameSpace() + "queryLoadBalanceRulesByEntityId", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#queryAllRestrictCms(java.
     * lang.Long)
     */
    @Override
    public List<CmcLoadBalRestrictCm> queryAllRestrictCms(Long cmcId) {
        // TODO Auto-generated method stub

        return getSqlSession().selectList(getNameSpace() + "queryAllRestrictCms", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcLoadBalanceDao#deleteLoadBalRule(java.lang
     * .Long)
     */
    @Override
    public void deleteLoadBalRule(Long ruleId) {
        // TODO Auto-generated method stub
        getSqlSession().delete(getNameSpace() + "deleteLoadBalRule", ruleId);
    }

    @Override
    public void insertOrUpdateCmcLoadBalCfg(CmcLoadBalCfg cmcLoadBalCfg) {
        if (((Long) getSqlSession().selectOne(getNameSpace() + "getCmcLoadBalCfgCount", cmcLoadBalCfg.getCmcId())) > 0) {
            getSqlSession().update(getNameSpace() + "updateCmcLoadBalCfg", cmcLoadBalCfg);
        } else {
            getSqlSession().insert(getNameSpace() + "insertCmcLoadBalCfg", cmcLoadBalCfg);
        }
    }

    @Override
    public void batchInsertCmc8800BLoadBalGrp(final List<CmcLoadBalGrp> cmcLoadBalGrps, final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            List<CmcLoadBalGrp> oldCmcLoadBalGrps = getSqlSession().selectList(
                    getNameSpace() + "selectAllCmcLoadBalGrp", cmcId);
            getSqlSession().delete(getNameSpace() + "deleteAllCmcLoadBalGrp", cmcId);
            for (CmcLoadBalGrp cmcLoadBalGrp : cmcLoadBalGrps) {
                cmcLoadBalGrp.setCmcId(cmcId);
                for (CmcLoadBalGrp oldCmcLoadBalGrp : oldCmcLoadBalGrps) {
                    if (cmcLoadBalGrp.getDocsLoadBalGrpId().equals(oldCmcLoadBalGrp.getDocsLoadBalGrpId())) {
                        cmcLoadBalGrp.setGroupName(oldCmcLoadBalGrp.getGroupName());
                        break;
                    }
                }
                session.insert(getNameSpace() + "insertCmcLoadBalGrp", cmcLoadBalGrp);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmc8800BLoadBalChannel(final List<CmcLoadBalChannel> cmcLoadBalChannels, final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            // 删除组时已经级联删除掉信道信息，直接插入
            for (CmcLoadBalChannel cmcLoadBalChannel : cmcLoadBalChannels) {
                cmcLoadBalChannel.setCmcId(cmcId);
                Long grpId = (Long) getSqlSession().selectOne(getNameSpace() + "selectLoadBalGrpId", cmcLoadBalChannel);
                cmcLoadBalChannel.setGrpId(grpId);
                session.insert(getNameSpace() + "insertCmcLoadBalChannel", cmcLoadBalChannel);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmc8800bLoadBalRestrictCm(final List<CmcLoadBalRestrictCm> cmcLoadBalRestrictCms,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            // 删除组时已级联删除，直接插入
            for (CmcLoadBalRestrictCm cmcLoadBalRestrictCm : cmcLoadBalRestrictCms) {
                cmcLoadBalRestrictCm.setCmcId(cmcId);
                Long grpId = (Long) getSqlSession().selectOne(getNameSpace() + "selectGrpIdByCmcIdAndIndex",
                        cmcLoadBalRestrictCm);
                cmcLoadBalRestrictCm.setGrpId(grpId);
                session.insert(getNameSpace() + "insertCmcLoadBalRestrictCm", cmcLoadBalRestrictCm);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmcLoadBalExcludeCm(final List<CmcLoadBalExcludeCm> cmcLoadBalExcludeCms, final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            getSqlSession().delete(getNameSpace() + "deleteAllCmcCmcLoadBalExcludeCm", cmcId);
            for (CmcLoadBalExcludeCm cmcLoadBalExcludeCm : cmcLoadBalExcludeCms) {
                cmcLoadBalExcludeCm.setCmcId(cmcId);
                session.insert(getNameSpace() + "insertCmcCmcLoadBalExcludeCm", cmcLoadBalExcludeCm);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmcLoadBalGrp(final List<CmcLoadBalGrp> cmcLoadBalGrps, final Long entityId) {

        SqlSession session = getBatchSession();
        try {
            List<CmcLoadBalGrp> oldCmcLoadBalGrps = getSqlSession().selectList(
                    getNameSpace() + "selectAllCmcLoadBalGrpOnOlt", entityId);
            getSqlSession().delete(getNameSpace() + "deleteAllCmcLoadBalGrpOnOlt", entityId);
            for (CmcLoadBalGrp cmcLoadBalGrp : cmcLoadBalGrps) {
                Map<String, Long> cmcMap = new HashMap<String, Long>();
                cmcMap.put("entityId", entityId);
                cmcMap.put("cmcIndex",
                        CmcIndexUtils.getCmcIndexFromDocsLoadBalGrpId(cmcLoadBalGrp.getDocsLoadBalGrpId()));

                Long cmcId = getCmcIdByCmcIndexAndEntityId(cmcMap);
                if (cmcId == null) {
                    continue;
                }
                cmcLoadBalGrp.setCmcId(cmcId);
                for (CmcLoadBalGrp oldCmcLoadBalGrp : oldCmcLoadBalGrps) {
                    if (cmcLoadBalGrp.getDocsLoadBalGrpId().equals(oldCmcLoadBalGrp.getDocsLoadBalGrpId())
                            && cmcLoadBalGrp.getCmcId().equals(oldCmcLoadBalGrp.getCmcId())) {
                        cmcLoadBalGrp.setGroupName(oldCmcLoadBalGrp.getGroupName());
                        break;
                    }
                }
                session.insert(getNameSpace() + "insertCmcLoadBalGrp", cmcLoadBalGrp);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertCmcLoadBalChannel(final List<CmcLoadBalChannel> cmcLoadBalChannels, final Long entityId) {
        SqlSession session = getBatchSession();
        try {
            // 删除组时已级联删除，直接插入
            for (CmcLoadBalChannel cmcLoadBalChannel : cmcLoadBalChannels) {
                Map<String, Long> cmcMap = new HashMap<String, Long>();
                cmcMap.put("entityId", entityId);
                cmcMap.put("cmcIndex",
                        CmcIndexUtils.getCmcIndexFromDocsLoadBalGrpId(cmcLoadBalChannel.getDocsLoadBalGrpId()));
                Long cmcId = getCmcIdByCmcIndexAndEntityId(cmcMap);
                if (cmcId == null) {
                    continue;
                }
                cmcLoadBalChannel.setCmcId(cmcId);
                Long grpId = (Long) getSqlSession().selectOne(getNameSpace() + "selectLoadBalGrpId", cmcLoadBalChannel);
                cmcLoadBalChannel.setGrpId(grpId);
                session.insert(getNameSpace() + "insertCmcLoadBalChannel", cmcLoadBalChannel);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Long getCmcIdByCmcIndexAndEntityId(Map<String, Long> map) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmcIdByCmcIndexAndEntityId", map);
    }
}