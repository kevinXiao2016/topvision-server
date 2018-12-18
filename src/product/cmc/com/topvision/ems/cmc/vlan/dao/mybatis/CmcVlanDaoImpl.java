package com.topvision.ems.cmc.vlan.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.domain.CmcEntity;
import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanCfgEntry;
import com.topvision.ems.cmc.facade.domain.CmcIpSubVlanScalarObject;
import com.topvision.ems.cmc.vlan.dao.CmcVlanDao;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVifSubIpEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanConfigEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanDhcpAllocEntry;
import com.topvision.ems.cmc.vlan.facade.domain.CmcVlanPrimaryIp;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("cmcVlanDao")
public class CmcVlanDaoImpl extends MyBatisDaoSupport<CmcEntity> implements CmcVlanDao {

    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.vlan.domain.CmcVlan";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#addCmcIpSubVlanScalar(com.topvision
     * .ems.cmc.facade.domain.CmcIpSubVlanScalarObject)
     */
    @Override
    public void addCmcIpSubVlanScalar(CmcIpSubVlanScalarObject obj) {
        getSqlSession().insert(getNameSpace() + "insertCmcIpSubVlanScalar", obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#getCmcIpSubVlanScalarById(java.lang
     * .Long)
     */
    @Override
    public CmcIpSubVlanScalarObject getCmcIpSubVlanScalarById(Long cmcId) {
        return (CmcIpSubVlanScalarObject) getSqlSession().selectOne(getNameSpace() + "getCmcIpSubVlanScalarByCmcId",
                cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#updateCmcIpSubVlanScalar(com.topvision
     * .ems.cmc.facade.domain.CmcIpSubVlanScalarObject)
     */
    @Override
    public void updateCmcIpSubVlanScalar(CmcIpSubVlanScalarObject obj) {
        getSqlSession().update(getNameSpace() + "updateCmcIpSubVlanScalar", obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#deleteCmcIpSubVlanScalar(com.topvision
     * .ems.cmc.facade.domain.CmcIpSubVlanScalarObject)
     */
    @Override
    public void deleteCmcIpSubVlanScalar(CmcIpSubVlanScalarObject obj) {
        getSqlSession().insert(getNameSpace() + "deleteCmcIpSubVlanScalar", obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#getCmcIpSubVlanCfgList(java.lang
     * .Long)
     */
    @Override
    public List<CmcIpSubVlanCfgEntry> getCmcIpSubVlanCfgList(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace() + "getCmcIpSubVlanCfgList", queryMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#getCmcIpSubVlanCfg(java.lang.Long,
     * java.lang.String, java.lang.String)
     */
    @Override
    public CmcIpSubVlanCfgEntry getCmcIpSubVlanCfg(Long cmcId, String cmcVlanIpIndex, String cmcVlanIpMaskIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", cmcId);
        paramMap.put("cmcVlanIpIndex", cmcVlanIpIndex);
        paramMap.put("cmcVlanIpMaskIndex", cmcVlanIpMaskIndex);
        return (CmcIpSubVlanCfgEntry) getSqlSession().selectOne(getNameSpace() + "getCmcIpSubVlanCfg", paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#addCmcIpSubVlanCfg(com.topvision
     * .ems.cmc.facade.domain.CmcIpSubVlanCfgEntry)
     */
    @Override
    public void addCmcIpSubVlanCfg(CmcIpSubVlanCfgEntry cmcIpSubVlanCfgEntry) {
        getSqlSession().insert(getNameSpace() + "insertCmcIpSubVlanCfgEntry", cmcIpSubVlanCfgEntry);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#updateCmcIpSubVlanCfgEntry(com.topvision
     * .ems.cmc.facade.domain.CmcIpSubVlanCfgEntry)
     */
    @Override
    public void updateCmcIpSubVlanCfgEntry(CmcIpSubVlanCfgEntry cmcIpSubVlanCfgEntry) {
        getSqlSession().update(getNameSpace() + "updateCmcIpSubVlanCfgEntry", cmcIpSubVlanCfgEntry);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#deleteCmcIpSubVlanCfg(java.lang.
     * Long, java.lang.String, java.lang.String)
     */
    @Override
    public void deleteCmcIpSubVlanCfg(Long cmcId, String cmcVlanIpIndex, String cmcVlanIpMaskIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", cmcId);
        paramMap.put("cmcVlanIpIndex", cmcVlanIpIndex);
        paramMap.put("cmcVlanIpMaskIndex", cmcVlanIpMaskIndex);
        getSqlSession().delete(getNameSpace() + "deleteCmcIpSubVlanCfg", paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#deleteAllCmcIpSubVlanCfg(java.lang
     * .Long)
     */
    @Override
    public void deleteAllCmcIpSubVlanCfg(Long cmcId) {
        getSqlSession().delete(getNameSpace() + "deleteAllCmcIpSubVlanCfg", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#refreshCmcIpSubVlanCfg(java.lang
     * .Long, java.util.List)
     */
    @Override
    public void refreshCmcIpSubVlanCfg(final Long cmcId, final List<CmcIpSubVlanCfgEntry> list) {

        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllCmcIpSubVlanCfg", cmcId);
            for (CmcIpSubVlanCfgEntry cmcIpSubVlanCfgEntry : list) {
                cmcIpSubVlanCfgEntry.setCmcId(cmcId);
                sqlSession.insert(getNameSpace() + "insertCmcIpSubVlanCfgEntry", cmcIpSubVlanCfgEntry);
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
     * @see com.topvision.ems.cmc.dao.CmcVlanDao#getCmcVlanList(java.lang.Long)
     */
    @Override
    public List<CmcVlanConfigEntry> getCmcVlanList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("getCmcVlanList"), cmcId);
    }

    @Override
    public CmcVlanConfigEntry getCmcVlanCfgById(Long cmcId, Integer topCcmtsVlanIndex) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("cmcId", cmcId);
        map.put("topCcmtsVlanIndex", topCcmtsVlanIndex);
        return (CmcVlanConfigEntry) getSqlSession().selectOne(getNameSpace() + "getCmcVlanCfgById", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#addCmcVlan(com.topvision.ems.cmc
     * .facade.domain.CmcVlanConfigEntry)
     */
    @Override
    public void addCmcVlan(CmcVlanConfigEntry cmcVlan) {
        getSqlSession().insert(getNameSpace() + "insertCmcVlanCfgEntry", cmcVlan);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#deleteCmcVlan(com.topvision.ems.
     * cmc.facade.domain.CmcVlanConfigEntry)
     */
    @Override
    public void deleteCmcVlan(CmcVlanConfigEntry cmcVlan) {
        getSqlSession().delete(getNameSpace() + "deleteCmcVlanCfgEntry", cmcVlan);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#deleteAllCmcVlan(java.lang.Long)
     */
    @Override
    public void deleteAllCmcVlan(Long cmcId) {
        getSqlSession().delete(getNameSpace() + "deleteAllCmcVlanCfgEntry", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#batchInsertCmcVlan(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void batchInsertCmcVlan(final List<CmcVlanConfigEntry> cmcVlanConfigEntries, Long cmcId) {

        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllCmcVlanCfgEntry", cmcId);
            for (CmcVlanConfigEntry cmcVlanConfigEntry : cmcVlanConfigEntries) {
                sqlSession.insert(getNameSpace() + "insertCmcVlanCfgEntry", cmcVlanConfigEntry);
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
     * com.topvision.ems.cmc.dao.CmcVlanDao#getCmcVlanListFromVlan0(java.lang
     * .Long)
     */
    @Override
    public List<CmcVifSubIpEntry> getCmcVlanListFromVlan0(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcVlanListFromVlan0", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#updateCmcVlanPriIp(com.topvision
     * .ems.cmc.facade.domain.CmcVlanPrimaryIp)
     */
    @Override
    public void updateCmcVlanPriIp(CmcVlanPrimaryIp cmcVlanPriIp) {
        getSqlSession().update(getNameSpace() + "updateCmcVlanPriIp", cmcVlanPriIp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#addCmcVlanSubIp(com.topvision.ems
     * .cmc.facade.domain.CmcVifSubIpEntry)
     */
    @Override
    public void addCmcVlanSubIp(CmcVifSubIpEntry cmcVifSubIp) {
        getSqlSession().insert(getNameSpace() + "insertCmcVifSubIp", cmcVifSubIp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#updateCmcVlanSubIp(com.topvision
     * .ems.cmc.facade.domain.CmcVifSubIpEntry)
     */
    @Override
    public void updateCmcVlanSubIp(CmcVifSubIpEntry cmcVifSubIp) {
        getSqlSession().update(getNameSpace() + "updateCmcVlanSubIp", cmcVifSubIp);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#getCmcVlanPriIpList(java.lang.Long)
     */
    @Override
    public List<CmcVlanPrimaryIp> getCmcVlanPriIpList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcVlanPriIpList", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#batchUpdateCmcVlanPriIp(java.util
     * .List, java.lang.Long)
     */
    @Override
    public void batchUpdateCmcVlanPriIp(List<CmcVlanPrimaryIp> cmcVlanPrimaryIps, Long cmcId) {
        List<CmcVlanConfigEntry> cmcVlanList = this.getCmcVlanList(cmcId);
        for (CmcVlanConfigEntry cmcVlanEntry : cmcVlanList) {
            for (CmcVlanPrimaryIp cmcPrimaryIp : cmcVlanPrimaryIps) {
                if (cmcId.equals(cmcVlanEntry.getCmcId())
                        && cmcPrimaryIp.getTopCcmtsVifPriIpVlanId().equals(cmcVlanEntry.getTopCcmtsVlanIndex())) {
                    this.updateCmcVlanPriIp(cmcPrimaryIp);
                }

            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#getCmcVlanVisIpList(java.lang.Long)
     */
    @Override
    public List<CmcVifSubIpEntry> getCmcVlanVisIpList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcVlanVisIpList", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#deleteCmcVlanPriIp(com.topvision
     * .ems.cmc.facade.domain.CmcVlanPrimaryIp)
     */
    @Override
    public void deleteCmcVlanPriIp(CmcVlanPrimaryIp cmcVlanPri) {
        getSqlSession().update(getNameSpace() + "updateCmcVlanPriIp", cmcVlanPri);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#deleteCmcVlanPriIpByPriIp(java.lang
     * .Long, java.lang.Integer)
     */
    @Override
    public void deleteCmcVlanSubIpByPriIp(Long cmcId, Integer vlanId) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("cmcId", cmcId);
        map.put("vlanId", vlanId);
        getSqlSession().update(getNameSpace() + "deleteCmcVlanSecIpBypriIp", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#deleteCmcVlanSubIp(com.topvision
     * .ems.cmc.facade.domain.CmcVifSubIpEntry)
     */
    @Override
    public void deleteCmcVlanSubIp(CmcVifSubIpEntry cmcVlanIpSec) {
        getSqlSession().delete(getNameSpace() + "deleteCmcVlanSubIP", cmcVlanIpSec);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#deleteAllCmcVlanSubIp(java.lang.
     * Long, java.lang.Integer)
     */
    @Override
    public void deleteAllCmcVlanSubIpByVlan(Long cmcId, Integer topCcmtsVifSubIpVlanIdx) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("cmcId", cmcId);
        map.put("topCcmtsVifSubIpVlanIdx", topCcmtsVifSubIpVlanIdx);
        getSqlSession().delete(getNameSpace() + "deleteAllCmcSubIpByVlan", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#deleteAllCmcVlanSubIp(java.lang.
     * Long)
     */
    @Override
    public void deleteAllCmcVlanSubIp(Long cmcId) {
        getSqlSession().delete(getNameSpace() + "deleteAllCmcVlanSubIp", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#batchInsertCmcVlanSubIp(java.lang
     * .Long, java.util.List)
     */
    @Override
    public void batchInsertCmcVlanSubIp(Long cmcId, final List<CmcVifSubIpEntry> cmcVifSubIpEntries) {

        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllCmcVlanSubIP", cmcId);
            for (CmcVifSubIpEntry cmcVifSubIpEntry : cmcVifSubIpEntries) {
                sqlSession.insert(getNameSpace() + "insertCmcVifSubIp", cmcVifSubIpEntry);
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
     * com.topvision.ems.cmc.dao.CmcVlanDao#updateCmcVlanDhcpAlloc(com.topvision
     * .ems.cmc.facade.domain.CmcVlanDhcpAllocEntry)
     */
    @Override
    public void updateCmcVlanDhcpAlloc(CmcVlanDhcpAllocEntry cmcDhcpAlloc) {
        getSqlSession().update(getNameSpace() + "updateCmcVlanPriIpDhcpCfg", cmcDhcpAlloc);
    }

    @Override
    public List<CmcVlanConfigEntry> selectCmcVlanPriIp(Long cmcId, String cmcVlanIp, String cmcVlanIpMask) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("cmcId", cmcId);
        map.put("cmcVlanIp", cmcVlanIp);
        map.put("cmcVlanIpMask", cmcVlanIpMask);
        return getSqlSession().selectList(getNameSpace() + "queryCmcVlanPriIp", map);
    }

    @Override
    public List<CmcVifSubIpEntry> selectCmcVlanSubIp(Long cmcId, String cmcVlanIp, String cmcVlanIpMask) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("cmcId", cmcId);
        map.put("cmcVlanIp", cmcVlanIp);
        map.put("cmcVlanIpMask", cmcVlanIpMask);
        return getSqlSession().selectList(getNameSpace() + "queryCmcVlanSubIp", map);
    }

    @Override
    public void batchUpdateCmcVlanDhcpAlloc(List<CmcVlanDhcpAllocEntry> cmcDhcpAllocs, Long cmcId) {
        List<CmcVlanConfigEntry> cmcVlanList = this.getCmcVlanList(cmcId);
        for (CmcVlanConfigEntry cmcVlanEntry : cmcVlanList) {
            for (CmcVlanDhcpAllocEntry cmcDhcpAlloc : cmcDhcpAllocs) {
                if (cmcId.equals(cmcVlanEntry.getCmcId())
                        && cmcDhcpAlloc.getTopCcmtsVlanIndex().equals(cmcVlanEntry.getTopCcmtsVlanIndex())) {
                    this.updateCmcVlanDhcpAlloc(cmcDhcpAlloc);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcVlanDao#getCmcVlanSubIpIndexList(java.lang
     * .Long)
     */
    @Override
    public List<Integer> getCmcVlanSubIpIndexList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcVlanVisIpIndexList", cmcId);
    }
}
