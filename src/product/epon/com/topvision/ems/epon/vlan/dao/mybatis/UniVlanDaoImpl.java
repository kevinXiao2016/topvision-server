/***********************************************************************
 * $Id: SniVlanDaoImpl.java,v1.0 2013-10-25 上午11:45:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.vlan.dao.UniVlanDao;
import com.topvision.ems.epon.vlan.domain.PortVlanAttribute;
import com.topvision.ems.epon.vlan.domain.VlanAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanTrunkRule;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author jay
 * @created @2011-10-25-11:36:51
 * @Mybatis Modify by Rod @2013-10-19
 */
@Repository("uniVlanDao")
public class UniVlanDaoImpl extends MyBatisDaoSupport<Object> implements UniVlanDao {

    @Override
    public PortVlanAttribute getPortVlanAttribute(Long portId) {
        return getSqlSession().selectOne(getNameSpace() + "getPortVlanAttribute", portId);
    }

    @Override
    public void updatePortVlanAttribute(PortVlanAttribute portVlanAttribute) {
        getSqlSession().update(getNameSpace() + "updateOnuPortVlan", portVlanAttribute);
    }

    @Override
    public List<VlanTranslationRule> getVlanTranslationRuleList(Long portId) {
        return getSqlSession().selectList(getNameSpace() + "getVlanTranslationRuleList", portId);
    }

    @Override
    public VlanTranslationRule getVlanTranslationRule(Long uniId, Integer vlanIndex) {
        VlanTranslationRule vlanTranslationRule = new VlanTranslationRule();
        vlanTranslationRule.setPortId(uniId);
        vlanTranslationRule.setVlanIndex(vlanIndex);
        return getSqlSession().selectOne(getNameSpace() + "getVlanTranslationRule", vlanTranslationRule);
    }

    @Override
    public VlanTranslationRule getVlanTranslationRuleByAfter(Long uniId, Integer vlanIndex, Integer newVlan) {
        VlanTranslationRule vlanTranslationRule = new VlanTranslationRule();
        vlanTranslationRule.setPortId(uniId);
        vlanTranslationRule.setVlanIndex(vlanIndex);
        vlanTranslationRule.setTranslationNewVid(newVlan);
        return getSqlSession().selectOne(getNameSpace() + "getVlanTranslationRuleByAfter", vlanTranslationRule);
    }

    @Override
    public void addVlanTranslationRule(VlanTranslationRule vlanTranslationRule) {
        getSqlSession().update(getNameSpace() + "insertOnuVlanTranslation", vlanTranslationRule);
    }

    @Override
    public void deleteVlanTranslationRule(VlanTranslationRule vlanTranslationRule) {
        getSqlSession().delete(getNameSpace() + "deleteOnuVlanTranslation", vlanTranslationRule);
    }

    @Override
    public List<VlanAggregationRule> getVlanAggregationRuleList(Long portId) {
        return getSqlSession().selectList(getNameSpace() + "getVlanAggregationRuleList", portId);
    }

    @Override
    public VlanAggregationRule getVlanAggregationRule(Long uniId, Integer vlanIndex) {
        VlanAggregationRule vlanAggregationRule = new VlanAggregationRule();
        vlanAggregationRule.setPortId(uniId);
        vlanAggregationRule.setPortAggregationVidIndex(vlanIndex);
        return getSqlSession().selectOne(getNameSpace() + "getVlanAggregationRule", vlanAggregationRule);
    }

    @Override
    public void addVlanAggregationRule(VlanAggregationRule vlanAggregationRule) {
        getSqlSession().update(getNameSpace() + "insertOnuVlanAgg", vlanAggregationRule);
    }

    @Override
    public void modifyCVlanAggregationRule(VlanAggregationRule vlanAggregationRule) {
        getSqlSession().update(getNameSpace() + "updateOnuVlanAgg", vlanAggregationRule);
    }

    @Override
    public void deleteVlanAggregationRule(VlanAggregationRule vlanAggregationRule) {
        getSqlSession().delete(getNameSpace() + "deleteOnuVlanAgg", vlanAggregationRule);
    }

    @Override
    public VlanTrunkRule getVlanTrunkRules(Long uniId) {
        return getSqlSession().selectOne(getNameSpace() + "getVlanTrunkRules", uniId);
    }

    @Override
    public void updateVlanTrunkRule(VlanTrunkRule vlanTrunkRule) {
        getSqlSession().insert(getNameSpace() + "updateOnuVlanTrunk", vlanTrunkRule);
    }

    @Override
    public void addVlanTrunkRule(VlanTrunkRule vlanTrunkRule) {
        // 数据库中新增一条规则
        getSqlSession().insert(getNameSpace() + "insertOnuVlanTrunk", vlanTrunkRule);
    }

    @Override
    public void deleteVlanTrunkRule(Long uniId) {
        // 调用dao删除数据库中记录
        getSqlSession().delete(getNameSpace() + "deleteOnuVlanTrunk", uniId);
    }

    @Override
    public List<PortVlanAttribute> getOnuPortVlanList(Long onuId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuPortVlanList", onuId);
    }

    @Override
    public void batchInsertOnuPortVlan(final List<PortVlanAttribute> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            //sqlSession.delete(getNameSpace() + "deleteAllOnuPortVlan", entityId);
            for (PortVlanAttribute portVlanAttribute : list) {
                OltUniAttribute oltUniAttribute = new OltUniAttribute();
                oltUniAttribute.setEntityId(portVlanAttribute.getEntityId());
                oltUniAttribute.setUniIndex(portVlanAttribute.getPortIndex());
                portVlanAttribute.setPortId((Long) sqlSession.selectOne(getNameSpace() + "getUniId", oltUniAttribute));
                sqlSession.insert(getNameSpace() + "insertOnuPortVlan", portVlanAttribute);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertOnuVlanTranslation(final List<VlanTranslationRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOnuVlanTranslation", entityId);
            for (VlanTranslationRule vlanTranslationRule : list) {
                OltUniAttribute oltUniAttribute = new OltUniAttribute();
                oltUniAttribute.setEntityId(vlanTranslationRule.getEntityId());
                oltUniAttribute.setUniIndex(vlanTranslationRule.getPortIndex());
                vlanTranslationRule
                        .setPortId((Long) sqlSession.selectOne(getNameSpace() + "getUniId", oltUniAttribute));
                sqlSession.insert(getNameSpace() + "insertOnuVlanTranslation", vlanTranslationRule);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertUniVlanTranslation(final List<VlanTranslationRule> list, Long uniId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteUniVlanTranslation", uniId);
            for (VlanTranslationRule vlanTranslationRule : list) {
                vlanTranslationRule.setPortId(uniId);
                sqlSession.insert(getNameSpace() + "insertOnuVlanTranslation", vlanTranslationRule);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertOnuVlanTrunk(final List<VlanTrunkRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOnuVlanTrunk", entityId);
            for (VlanTrunkRule vlanTrunkRule : list) {
                OltUniAttribute oltUniAttribute = new OltUniAttribute();
                oltUniAttribute.setEntityId(vlanTrunkRule.getEntityId());
                oltUniAttribute.setUniIndex(vlanTrunkRule.getPortIndex());
                vlanTrunkRule.setPortId((Long) sqlSession.selectOne(getNameSpace() + "getUniId", oltUniAttribute));
                sqlSession.insert(getNameSpace() + "insertOnuVlanTrunk", vlanTrunkRule);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertOnuVlanAgg(final List<VlanAggregationRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOnuVlanAgg", entityId);
            for (VlanAggregationRule vlanAggregationRule : list) {
                OltUniAttribute oltUniAttribute = new OltUniAttribute();
                oltUniAttribute.setEntityId(vlanAggregationRule.getEntityId());
                oltUniAttribute.setUniIndex(vlanAggregationRule.getPortIndex());
                vlanAggregationRule
                        .setPortId((Long) sqlSession.selectOne(getNameSpace() + "getUniId", oltUniAttribute));
                sqlSession.insert(getNameSpace() + "insertOnuVlanAgg", vlanAggregationRule);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void batchInsertUniVlanAgg(final List<VlanAggregationRule> list, Long uniId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteUniPortVlanAgg", uniId);
            for (VlanAggregationRule vlanAggregationRule : list) {
                vlanAggregationRule.setPortId(uniId);
                sqlSession.insert(getNameSpace() + "insertOnuVlanAgg", vlanAggregationRule);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.vlan.domain.UniVlan";
    }

}
