/***********************************************************************
 * $Id: OnuDaoImpl.java,v1.0 2013-10-25 上午11:07:48 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.UniDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OltUniStormSuppressionEntry;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2013-10-25-上午11:07:48
 *
 */
@Repository("uniDao")
public class UniDaoImpl extends MyBatisDaoSupport<Object> implements UniDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.Uni";
    }

    @Override
    public OltUniAttribute getOnuUniAttribute(Long uniId) {
        return getSqlSession().selectOne(getNameSpace() + "getOnuUniAttribute", uniId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onu.dao.UniDao#getUniIndexAttributes(java.lang.Long)
     */
    @Override
    public List<OltUniAttribute> getUniIndexAttributes(Long onuId) {
        return getSqlSession().selectList(getNameSpace() + "getUniIndexAttributes", onuId);
    }

    @Override
    public void updateUniAdminStatus(OltUniAttribute uniAttribute) {
        getSqlSession().update(getNameSpace() + "updateUniAdminStatus", uniAttribute);
    }

    @Override
    public void updateUniAdminStatus(Long uniId, Integer uniAdminStatus) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("uniId", uniId);
        paramMap.put("uniAdminStatus", uniAdminStatus);
        getSqlSession().update(getNameSpace() + "updateUniAdminStatusById", paramMap);
    }

    @Override

    public void updateUniOperaStatus(Long uniId, Integer status) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("uniId", uniId);
        paramMap.put("uniOperationStatus", status);
        getSqlSession().update(getNameSpace() + "updateUniOperaStatus", paramMap);
    }

    @Override
    public void updateUniAutoNegotiationStatus(Long uniId, Integer adminStatus) {
        OltUniAttribute uniAttribute = new OltUniAttribute();
        uniAttribute.setUniId(uniId);
        uniAttribute.setUniAutoNegotiationEnable(adminStatus);
        getSqlSession().update(getNameSpace() + "updateUniAutoNegotiationStatus", uniAttribute);
    }

    @Override
    public void updateOltUniAutoNegoMode(OltUniExtAttribute oltUniExtAttribute) {
        getSqlSession().update(getNameSpace() + "updateUniAutoNegoMode", oltUniExtAttribute);
    }

    @Override
    public void updateOnuUniAttribute(OltUniAttribute oltUniAttribute) {
        OltUniAttribute uniAttribute = new OltUniAttribute();
        uniAttribute.setUniOperationStatus(oltUniAttribute.getUniOperationStatus());
        uniAttribute.setUniAdminStatus(oltUniAttribute.getUniAdminStatus());
        uniAttribute.setUniAutoNegotiationEnable(oltUniAttribute.getUniAutoNegotiationEnable());
        // uniAttribute.setUniAttrFlowCtrl(oltUniAttribute.getUniAttrFlowCtrl());
        getSqlSession().update(getNameSpace() + "updateOnuUniAttribute", uniAttribute);
    }

    @Override
    public void insertOrUpdateOltUniAttribute(OltUniAttribute oltUniAttribute) {
        if (((Integer) getSqlSession().selectOne(getNameSpace() + "getOltUniCount", oltUniAttribute)) != 0) {
            getSqlSession().update(getNameSpace() + "updateOnuUniAttribute", oltUniAttribute);
        } else {
            OltOnuAttribute oAttribute = new OltOnuAttribute();
            oAttribute.setEntityId(oltUniAttribute.getEntityId());
            oAttribute.setOnuIndex(EponIndex.getOnuIndex(oltUniAttribute.getUniIndex()));
            oltUniAttribute.setOnuId((Long) getSqlSession().selectOne("Onu.getOnuId", oAttribute));
            getSqlSession().insert(getNameSpace() + "insertOltUniRelation", oltUniAttribute);
            oltUniAttribute.setUniId(oltUniAttribute.getUniId());
            getSqlSession().insert(getNameSpace() + "insertOltUniAttribute", oltUniAttribute);
        }
    }

    @Override
    public void batchInsertOltUniExtAttribute(List<OltUniExtAttribute> uniExtAttributes) {
        SqlSession sqlSession = getBatchSession();
        try {
            if (uniExtAttributes != null && uniExtAttributes.size() > 0) {
                OltUniAttribute uniAttribute = new OltUniAttribute();
                for (OltUniExtAttribute uAttribute : uniExtAttributes) {
                    uniAttribute.setEntityId(uAttribute.getEntityId());
                    uniAttribute.setUniIndex(uAttribute.getUniIndex());
                    uAttribute.setUniId((Long) sqlSession.selectOne(getNameSpace() + "getUniId", uniAttribute));
                    sqlSession.update(getNameSpace() + "updateUniExtAttribute", uAttribute);
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
    public void updateUniAutoNegoStatus(Long uniId, Integer autoNegoStatus) {
        OltUniAttribute uniAttribute = new OltUniAttribute();
        uniAttribute.setUniId(uniId);
        getSqlSession().update(getNameSpace() + "updateUniAutoNegoStatus", uniAttribute);
    }

    @Override
    public Long getUniIndex(Long uniId) {
        return getSqlSession().selectOne(getNameSpace() + "getUniIndex", uniId);
    }

    @Override
    public Long getUniIdByIndex(Long entityId, Long uniIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("uniIndex", uniIndex);
        return getSqlSession().selectOne(getNameSpace() + "getUniIdByIndex", paramMap);
    }

    @Override
    public void updateUniFlowCtrlEnable(Long uniId, Integer uniFlowCtrlEnable) {
        OltUniAttribute uniAttribute = new OltUniAttribute();
        uniAttribute.setUniId(uniId);
        uniAttribute.setFlowCtrl(uniFlowCtrlEnable);
        getSqlSession().update(getNameSpace() + "updateUniFlowCtrlEnable", uniAttribute);
    }

    @Override
    public void updateUniAutoNegoEnable(Long uniId, Integer autoNegoEnable) {
        OltUniAttribute uniAttribute = new OltUniAttribute();
        uniAttribute.setUniId(uniId);
        uniAttribute.setUniAutoNegotiationEnable(autoNegoEnable);
        getSqlSession().update(getNameSpace() + "updateUniAutoNegoEnable", uniAttribute);
    }

    @Override
    public OltUniPortRateLimit getUniPortRateLimit(Long uniId) {
        return getSqlSession().selectOne(getNameSpace() + "getOltUniPortRateLimit", uniId);
    }

    @Override
    public void updateUniIsolationEnable(Long uniId, Integer uniIsolationEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("uniId", uniId);
        paramMap.put("uniIsolationEnable", uniIsolationEnable);
        getSqlSession().update(getNameSpace() + "updateUniIsolationEnable", paramMap);
    }

    @Override
    public void updateUni15minEnable(Long uniId, Integer uni15minEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("uniId", uniId);
        paramMap.put("uni15minEnable", uni15minEnable);
        getSqlSession().update(getNameSpace() + "updateUni15minEnable", paramMap);
    }

    @Override
    public void updateUni24hEnable(Long uniId, Integer uni24hEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("uniId", uniId);
        paramMap.put("uni24hEnable", uni24hEnable);
        getSqlSession().update(getNameSpace() + "updateUni24hEnable", paramMap);
    }

    @Override
    public void updateUniPortRateLimit(OltUniPortRateLimit oltUniPortRateLimit) {
        getSqlSession().update(getNameSpace() + "updateOltUniPortRateLimit", oltUniPortRateLimit);
    }

    @Override
    public OltUniStormSuppressionEntry getUniStormSuppression(Long uniId) {
        return getSqlSession().selectOne(getNameSpace() + "getUniStormSuppression", uniId);
    }

    @Override
    public void updateUniStormSuppression(OltUniStormSuppressionEntry oltUniStormSuppressionEntry) {
        getSqlSession().update(getNameSpace() + "updateUniStormSuppression", oltUniStormSuppressionEntry);
    }

    @Override
    public OltUniExtAttribute getOltUniExtAttribute(Long uniId) {
        return getSqlSession().selectOne(getNameSpace() + "getOltUniExtAttribute", uniId);
    }

    @Override
    public void batchInsertOltUniRateLimit(final List<OltUniPortRateLimit> list) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltUniPortRateLimit rateLimit : list) {
                // 通过构造一个pon属性对象，获得ONU对应的ponId
                OltUniAttribute oltUniAttribute = new OltUniAttribute();
                oltUniAttribute.setEntityId(rateLimit.getEntityId());
                oltUniAttribute.setUniIndex(rateLimit.getUniIndex());
                rateLimit.setUniId((Long) sqlSession.selectOne(getNameSpace() + "getUniId", oltUniAttribute));
                sqlSession.delete(getNameSpace() + "deleteUniRateLimit", rateLimit.getUniId());
                sqlSession.insert(getNameSpace() + "insertUniRateLimit", rateLimit);
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
    public void batchInsertOltUniStormInfo(final List<OltUniStormSuppressionEntry> list) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltUniStormSuppressionEntry uniStormSuppressionEntry : list) {
                // 通过构造一个uni属性对象，获得UNI对应的uniId
                OltUniAttribute oltUniAttribute = new OltUniAttribute();
                oltUniAttribute.setEntityId(uniStormSuppressionEntry.getEntityId());
                oltUniAttribute.setUniIndex(uniStormSuppressionEntry.getUniIndex());
                uniStormSuppressionEntry.setUniId((Long) sqlSession.selectOne(getNameSpace() + "getUniId",
                        oltUniAttribute));
                sqlSession.delete(getNameSpace() + "deleteUniStormInfo", uniStormSuppressionEntry.getUniId());
                sqlSession.insert(getNameSpace() + "insertUniStormInfo", uniStormSuppressionEntry);
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
    public void syncUniAttribute(List<OltUniAttribute> list, Long entityId, HashMap<Long, Long> onuMap,
            boolean delUniFlag) {
        //@Add by Rod uniMap 只用来记录   拓扑逻辑使用onuMap
        Map<Long, Long> uniMap = selectMapByKeyAndValue(getNameSpace() + "getUniMap", entityId, "INDEX", "ID");
        SqlSession sqlSession = getBatchSession();
        for (OltUniAttribute oltUniAttribute : list) {
            if (onuMap.containsKey(oltUniAttribute.getUniIndex())) {
                oltUniAttribute.setUniId(onuMap.get(oltUniAttribute.getUniIndex()));
                if (EponConstants.EPON_ONU.equals(oltUniAttribute.getUniEorG())) {
                    getSqlSession().update(getNameSpace() + "updateOnuUniAttribute", oltUniAttribute);
                } else if (GponConstant.GPON_ONU.equals(oltUniAttribute.getUniEorG())) {
                    getSqlSession().update(getNameSpace() + "updateGponUniAttribute", oltUniAttribute);
                }
                uniMap.remove(oltUniAttribute.getUniIndex());
            } else {
                oltUniAttribute.setOnuId(onuMap.get(EponIndex.getOnuIndex(oltUniAttribute.getUniIndex())));
                getSqlSession().insert(getNameSpace() + "insertOltUniRelation", oltUniAttribute);
                oltUniAttribute.setUniId(oltUniAttribute.getUniId());
                if (EponConstants.EPON_ONU.equals(oltUniAttribute.getUniEorG())) {
                    getSqlSession().insert(getNameSpace() + "insertOltUniAttribute", oltUniAttribute);
                } else if (GponConstant.GPON_ONU.equals(oltUniAttribute.getUniEorG())) {
                    getSqlSession().insert(getNameSpace() + "insertGponUniAttribute", oltUniAttribute);
                }
                onuMap.put(oltUniAttribute.getUniIndex(), oltUniAttribute.getUniId());
            }
        }
        try {
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
        if (delUniFlag) {
            for (Entry<Long, Long> entry : uniMap.entrySet()) {
                getSqlSession().delete(getNameSpace() + "deleteOnuUniRelation", entry.getValue());
                onuMap.remove(entry.getKey());
            }
        }
    }

    @Override
    public void syncUniExtAttribute(List<OltUniExtAttribute> uniExtAttributes, HashMap<Long, Long> onuMap) {
        SqlSession sqlSession = getBatchSession();
        try {
            if (uniExtAttributes != null && uniExtAttributes.size() > 0) {
                for (OltUniExtAttribute uAttribute : uniExtAttributes) {
                    uAttribute.setUniId(onuMap.get(uAttribute.getUniIndex()));
                    sqlSession.update(getNameSpace() + "updateUniExtAttribute", uAttribute);
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
    public Long getUniAttrMacAddrLearnMaxNum(Long uniId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getUniAttrMacAddrLearnMaxNum", uniId);
    }

    @Override
    public void modifyUniMacAddrLearnMaxNum(Long uniId, Long topUniAttrMacAddrLearnMaxNum) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uniId", uniId);
        map.put("macAddrLearnMaxNum", topUniAttrMacAddrLearnMaxNum);
        getSqlSession().update(getNameSpace() + "modifyUniMacAddrLearnMaxNum", map);
    }

    @Override
    public void modifyUniMacAgeTime(Long uniId, Integer uniMacAgeTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("uniId", uniId);
        paramMap.put("uniMacAgeTime", uniMacAgeTime);
        getSqlSession().update(getNameSpace() + "updateUniMacAgeTime", paramMap);
    }

    @Override
    public void updateOltUniExtAttribute(OltUniExtAttribute v) {
        getSqlSession().update(getNameSpace() + "updateOltUniExtAttribute", v);
    }

    @Override
    public void updateUniUniDSLoopBackEnable(OltUniExtAttribute v) {
        getSqlSession().update(getNameSpace() + "updateUniDSLoopBackEnable", v);
    }

    @Override
    public void updateUniUSUtgPri(OltUniExtAttribute v) {
        getSqlSession().update(getNameSpace() + "updateUniUSUtgPri", v);
    }

    @Override
    public void updateUniLoopDetectEnable(OltUniExtAttribute v) {
        getSqlSession().update(getNameSpace() + "updateUniLoopDetectEnable", v);
    }

    @Override
    public void batchUpdateUni15MinStatus(List<OltUniExtAttribute> uniAttrs) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltUniExtAttribute uniAttr : uniAttrs) {
                Long uniId = (Long) sqlSession.selectOne(getNameSpace() + "getUniId", uniAttr);
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("uniId", uniId);
                paramMap.put("uni15minEnable", uniAttr.getPerfStats15minuteEnable());
                sqlSession.update(getNameSpace() + "updateUni15minEnable", paramMap);
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
    public List<Long> getUniIndexListByEntityIdAndOnuIndex(Long entityId, Long onuIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("onuIndex", onuIndex);
        return getSqlSession().selectList(getNameSpace("getUniIndexListByEntityIdAndOnuIndex"), map);
    }

    @Override
    public String getOnuEorGByUniId(Long uniId) {
        return getSqlSession().selectOne(getNameSpace("getOnuEorGByUniId"), uniId);
    }

}
