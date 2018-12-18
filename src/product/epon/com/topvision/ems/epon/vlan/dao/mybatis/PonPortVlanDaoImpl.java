/***********************************************************************
 * $Id: SniVlanDaoImpl.java,v1.0 2013-10-25 上午11:45:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.PonCvidModeRela;
import com.topvision.ems.epon.domain.PonSvidModeRela;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.vlan.dao.PonPortVlanDao;
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
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-10-25-上午11:45:47
 *
 */
@Repository("ponPortVlanDao")
public class PonPortVlanDaoImpl extends MyBatisDaoSupport<Object> implements PonPortVlanDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getTransList(java.lang.Long)
     */
    @Override
    public List<VlanTranslationRule> getTransList(Long ponId) {
        return getSqlSession().selectList(getNameSpace("getTransList"), ponId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#addTransRule(com.topvision.ems.epon.facade.domain.
     * VlanTranslationRule)
     */
    @Override
    public void addTransRule(VlanTranslationRule vlanTranslationRule) {
        getSqlSession().insert(getNameSpace("insertOltVlanTranslation"), vlanTranslationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#deleteTransRule(java.lang.Long, java.lang.Integer)
     */
    @Override
    public void deleteTransRule(Long ponId, Integer vlanId) {
        VlanTranslationRule vlanTranslationRule = new VlanTranslationRule();
        vlanTranslationRule.setPortId(ponId);
        vlanTranslationRule.setVlanIndex(vlanId);
        getSqlSession().delete(getNameSpace("deleteOltVlanTranslation"), vlanTranslationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getAggrList(java.lang.Long)
     */
    @Override
    public List<VlanAggregationRule> getAggrList(Long ponId) {
        return getSqlSession().selectList(getNameSpace("getAggrList"), ponId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#addAggrRule(com.topvision.ems.epon.facade.domain.
     * VlanAggregationRule)
     */
    @Override
    public void addSvlanAggrRule(VlanAggregationRule vlanAggregationRule) {
        getSqlSession().insert(getNameSpace("insertOltVlanAgg"), vlanAggregationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#deleteAggrRule(java.lang.Long, java.lang.Integer)
     */
    @Override
    public void deleteSvlanAggrRule(Long ponId, Integer vlanId) {
        VlanAggregationRule vlanAggregationRule = new VlanAggregationRule();
        vlanAggregationRule.setPortId(ponId);
        vlanAggregationRule.setPortAggregationVidIndex(vlanId);
        getSqlSession().delete(getNameSpace("deleteOltVlanAgg"), vlanAggregationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.PonVlanDao#modifyAggrRule(com.topvision.ems.epon.facade.domain
     * .VlanAggregationRule)
     */
    @Override
    public void modifyCvlanAggrRule(VlanAggregationRule vlanAggregationRule) {
        getSqlSession().update(getNameSpace("modifyCvlanAggrRule"), vlanAggregationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getTrunkList(java.lang.Long)
     */
    @Override
    public VlanTrunkRule getTrunkList(Long ponId) {
        return getSqlSession().selectOne(getNameSpace("getTrunkList"), ponId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#addTrunkRule(com.topvision.ems.epon.facade.domain.
     * VlanTrunkRule)
     */
    @Override
    public void addTrunkRule(VlanTrunkRule vlanTrunkRule) {
        getSqlSession().insert(getNameSpace("insertOltVlanTrunk"), vlanTrunkRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#deleteTrunkRule(java.lang.Long)
     */
    @Override
    public void deleteTrunkRule(Long ponId) {
        getSqlSession().delete(getNameSpace("deleteOltVlanTrunk"), ponId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.PonVlanDao#modifyTrunkRule(com.topvision.ems.epon.facade.domain
     * .VlanTrunkRule)
     */
    @Override
    public void modifyTrunkRule(VlanTrunkRule vlanTrunkRule) {
        getSqlSession().update(getNameSpace("modifyTrunkRule"), vlanTrunkRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getQinQList(java.lang.Long)
     */
    @Override
    public List<VlanQinQRule> getQinQList(Long ponId) {
        return getSqlSession().selectList(getNameSpace("getQinQList"), ponId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#addQinQRule(com.topvision.ems.epon.facade.domain.
     * VlanQinQRule)
     */
    @Override
    public void addQinQRule(VlanQinQRule vlanQinQRule) {
        getSqlSession().insert(getNameSpace("insertOltPonQinQ"), vlanQinQRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#deleteQinQRule(java.lang.Long, java.lang.Integer,
     * java.lang.Integer)
     */
    @Override
    public void deleteQinQRule(Long ponId, Integer startVlanId, Integer endVlanId) {
        VlanQinQRule vlanQinQRule = new VlanQinQRule();
        vlanQinQRule.setPortId(ponId);
        vlanQinQRule.setPqStartVlanId(startVlanId);
        vlanQinQRule.setPqEndVlanId(endVlanId);
        getSqlSession().delete(getNameSpace("deleteOltPonQinQ"), vlanQinQRule);
    }

    @Override
    public VlanTransparentRule loadTransparentData(Long entityId, Long ponId) {
        Map<String, Long> v = new HashMap<String, Long>();
        v.put("ponId", ponId);
        v.put("entityId", entityId);
        return getSqlSession().selectOne(getNameSpace("getTransparent"), v);
    }

    @Override
    public void addTransparentRule(VlanTransparentRule vlanTransparentRule) {
        getSqlSession().insert(getNameSpace("insertOltPonTransparent"), vlanTransparentRule);
    }

    @Override
    public void delTransparentRule(VlanTransparentRule vlanTransparentRule) {
        getSqlSession().delete(getNameSpace("deleteOltPonTransparent"), vlanTransparentRule);
    }

    @Override
    public void modifyTransparentRule(VlanTransparentRule vlanTransparentRule) {
        getSqlSession().update(getNameSpace("updateTransparentRule"), vlanTransparentRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getPonCvidModeRela(java.lang.Long)
     */
    @Override
    public List<PonCvidModeRela> getPonCvidModeRela(Long ponId) {
        return getSqlSession().selectList(getNameSpace("getPonCvidModeRela"), ponId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getPonSvidModeRela(java.lang.Long)
     */
    @Override
    public List<PonSvidModeRela> getPonSvidModeRela(Long entityId, Long ponId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("ponId", ponId);
        return getSqlSession().selectList(getNameSpace("getPonSvidModeRela"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#batchInsertOltVlanTranslation(java.util.List)
     */
    @Override
    public void batchInsertOltVlanTranslation(final List<VlanTranslationRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltVlanTranslation"), entityId);
            for (VlanTranslationRule translationRule : list) {
                OltPonAttribute ponAttribute = new OltPonAttribute();
                ponAttribute.setEntityId(translationRule.getEntityId());
                ponAttribute.setPonIndex(translationRule.getPortIndex());
                translationRule.setPortId((Long) sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute));
                sqlSession.insert(getNameSpace("insertOltVlanTranslation"), translationRule);
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
     * @see com.topvision.ems.epon.dao.PonVlanDao#batchInsertOltVlanAgg(java.util.List)
     */
    @Override
    public void batchInsertOltVlanAgg(final List<VlanAggregationRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltVlanAgg"), entityId);
            for (VlanAggregationRule aggregationRule : list) {
                OltPonAttribute ponAttribute = new OltPonAttribute();
                ponAttribute.setEntityId(aggregationRule.getEntityId());
                ponAttribute.setPonIndex(aggregationRule.getPortIndex());
                aggregationRule.setPortId((Long) sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute));
                sqlSession.insert(getNameSpace("insertOltVlanAgg"), aggregationRule);
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
     * @see com.topvision.ems.epon.dao.PonVlanDao#batchInsertOltVlanTrunk(java.util.List)
     */
    @Override
    public void batchInsertOltVlanTrunk(final List<VlanTrunkRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltVlanTrunk"), entityId);
            for (VlanTrunkRule trunkRule : list) {
                OltPonAttribute ponAttribute = new OltPonAttribute();
                ponAttribute.setEntityId(trunkRule.getEntityId());
                ponAttribute.setPonIndex(trunkRule.getPortIndex());
                Long ponId = sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute);
                if (ponId != null) {
                    trunkRule.setPortId(ponId);
                    sqlSession.insert(getNameSpace("insertOltVlanTrunk"), trunkRule);
                }
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
    public void batchInsertOltVlanTransparent(final List<VlanTransparentRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltVlanTransparent"), entityId);
            for (VlanTransparentRule rule : list) {
                OltPonAttribute ponAttribute = new OltPonAttribute();
                ponAttribute.setEntityId(rule.getEntityId());
                ponAttribute.setPonIndex(rule.getPortIndex());
                rule.setPortId((Long) sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute));
                sqlSession.insert(getNameSpace("insertOltVlanTransparent"), rule);
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
     * @see com.topvision.ems.epon.dao.PonVlanDao#batchInsertOltPonQinQ(java.util.List)
     */
    @Override
    public void batchInsertOltPonQinQ(final List<VlanQinQRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltPonQinQ"), entityId);
            for (VlanQinQRule qinQRule : list) {
                OltPonAttribute ponAttribute = new OltPonAttribute();
                ponAttribute.setEntityId(qinQRule.getEntityId());
                ponAttribute.setPonIndex(qinQRule.getPortIndex());
                qinQRule.setPortId((Long) sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute));
                sqlSession.insert(getNameSpace("insertOltPonQinQ"), qinQRule);
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
     * @see com.topvision.ems.epon.dao.PonVlanDao#batchInsertTopVlanTrans(java.util.List)
     */
    @Override
    public void batchInsertTopVlanTrans(final List<VlanLlidTranslationRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltTopVlanTrans"), entityId);
            for (VlanLlidTranslationRule llidTranslationRule : list) {
                OltPonAttribute ponAttribute = new OltPonAttribute();
                ponAttribute.setEntityId(llidTranslationRule.getEntityId());
                ponAttribute.setPonIndex(llidTranslationRule.getPortIndex());
                Long ponId = sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute);
                if (ponId != null) {
                    llidTranslationRule.setPortId(ponId);
                    sqlSession.insert(getNameSpace("insertOltTopVlanTrans"), llidTranslationRule);
                }
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
     * @see com.topvision.ems.epon.dao.PonVlanDao#batchInsertTopVlanTrunk(java.util.List)
     */
    @Override
    public void batchInsertTopVlanTrunk(final List<VlanLlidTrunkRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltTopVlanTrunk"), entityId);
            for (VlanLlidTrunkRule llidTrunkRule : list) {
                OltPonAttribute ponAttribute = new OltPonAttribute();
                ponAttribute.setEntityId(llidTrunkRule.getEntityId());
                ponAttribute.setPonIndex(llidTrunkRule.getPortIndex());
                llidTrunkRule.setPortId((Long) sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute));
                sqlSession.insert(getNameSpace("insertOltTopVlanTrunk"), llidTrunkRule);
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
     * @see com.topvision.ems.epon.dao.PonVlanDao#batchInsertTopVlanAgg(java.util.List)
     */
    @Override
    public void batchInsertTopVlanAgg(final List<VlanLlidAggregationRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltTopVlanAgg"), entityId);
            for (VlanLlidAggregationRule llidAggregationRule : list) {
                OltPonAttribute ponAttribute = new OltPonAttribute();
                ponAttribute.setEntityId(llidAggregationRule.getEntityId());
                ponAttribute.setPonIndex(llidAggregationRule.getPortIndex());
                llidAggregationRule.setPortId((Long) sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute));
                // add by fanzidong,需要在入库前格式化MAC地址
                String formattedMac = MacUtils.convertToMaohaoFormat(llidAggregationRule.getOnuMacString());
                llidAggregationRule.setOnuMacString(formattedMac);
                sqlSession.insert(getNameSpace("insertOltTopVlanAgg"), llidAggregationRule);
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
     * @see com.topvision.ems.epon.dao.PonVlanDao#batchInsertTopVlanQinQ(java.util.List)
     */
    @Override
    public void batchInsertTopVlanQinQ(final List<VlanLlidQinQRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltTopVlanQinQ"), entityId);
            for (VlanLlidQinQRule llidQinQRule : list) {
                OltPonAttribute ponAttribute = new OltPonAttribute();
                ponAttribute.setEntityId(llidQinQRule.getEntityId());
                ponAttribute.setPonIndex(llidQinQRule.getPortIndex());
                llidQinQRule.setPortId((Long) sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute));
                // add by fanzidong,需要在入库前格式化MAC地址
                String formattedMac = MacUtils.convertToMaohaoFormat(llidQinQRule.getOnuMacString());
                llidQinQRule.setOnuMacString(formattedMac);
                sqlSession.insert(getNameSpace("insertOltTopVlanQinQ"), llidQinQRule);
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
    public void batchInsertTopVlanOnuQinQ(List<VlanLlidOnuQinQRule> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltTopVlanQinQ"), entityId);
            for (VlanLlidOnuQinQRule llidOnuQinQRule : list) {
                OltPonAttribute ponAttribute = new OltPonAttribute();
                ponAttribute.setEntityId(llidOnuQinQRule.getEntityId());
                ponAttribute.setPonIndex(llidOnuQinQRule.getPonIndex());
                llidOnuQinQRule.setPonId((Long) sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute));
                // add by fanzidong,需要在入库前格式化MAC地址
                String formattedMac = MacUtils.convertToMaohaoFormat(llidOnuQinQRule.getMac());
                llidOnuQinQRule.setMac(formattedMac);
                sqlSession.insert(getNameSpace("insertOltTopVlanOnuQinQ"), llidOnuQinQRule);
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
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.vlan.domain.PonVlan";
    }

    @Override
    public void batchInsertPonVlan(List<PortVlanAttribute> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllPonVlan"), entityId);
            for (PortVlanAttribute portVlanAttribute : list) {
                Long ponId = this.getPonIdByPonIndex(entityId, portVlanAttribute.getPortIndex());
                if (ponId == null) {
                    continue;
                } else {
                    portVlanAttribute.setPortId(ponId);
                    sqlSession.insert(getNameSpace("insertPonVlan"), portVlanAttribute);
                }
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
    public Long getPonIdByPonIndex(Long entityId, Long ponIndex) {
        OltPonAttribute oltPonAttribute = new OltPonAttribute();
        oltPonAttribute.setEntityId(entityId);
        oltPonAttribute.setPonIndex(ponIndex);
        return (Long) getSqlSession().selectOne(getNameSpace("getPonId"), oltPonAttribute);
    }

    @Override
    public PortVlanAttribute queryPonVlan(Long ponId) {
        return getSqlSession().selectOne(getNameSpace("queryPonVlan"), ponId);
    }

    @Override
    public void updatePonPvid(Long ponId, Integer pvid) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("ponId", ponId);
        paramsMap.put("pvid", pvid);
        getSqlSession().update(getNameSpace("updatePonPvid"), paramsMap);
        getSqlSession().update(getNameSpace("syncUpdatePortPvid"), paramsMap);
    }

    @Override
    public void insertOrUpdatePonVlan(PortVlanAttribute ponVlan) {
        getSqlSession().insert(getNameSpace("insertOrUpdatePonVlan"), ponVlan);
    }

}
