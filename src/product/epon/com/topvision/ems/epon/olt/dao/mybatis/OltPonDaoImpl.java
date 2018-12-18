/***********************************************************************
 * $Id: OltPonDaoImpl.java,v1.0 2013-10-25 上午10:16:09 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.dao.OltPonDao;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPonStormSuppressionEntry;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.TopPonPortSpeedEntry;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author flack
 * @created @2013-10-25-上午10:16:09
 *
 */
@Repository("oltPonDao")
public class OltPonDaoImpl extends MyBatisDaoSupport<Object> implements OltPonDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.olt.domain.OltPon";
    }

    @Override
    public void updatePonAdminStatus(Long ponId, Integer adminStatus) {
        OltPonAttribute ponAttribute = new OltPonAttribute();
        ponAttribute.setPonId(ponId);
        ponAttribute.setPonPortAdminStatus(adminStatus);
        getSqlSession().update(getNameSpace() + "updatePonAdminStatus", ponAttribute);
    }

    @Override
    public void updatePonIsolationStatus(Long ponId, Integer adminStatus) {
        OltPonAttribute ponAttribute = new OltPonAttribute();
        ponAttribute.setPonId(ponId);
        ponAttribute.setPonPortIsolationEnable(adminStatus);
        getSqlSession().update(getNameSpace() + "updatePonIsolationStatus", ponAttribute);
    }

    @Override
    public void updatePonPortEncryptMode(Long ponId, Integer ncryptMode, Integer exchangeTime) {
        OltPonAttribute ponAttribute = new OltPonAttribute();
        ponAttribute.setPonId(ponId);
        ponAttribute.setPonPortEncryptMode(ncryptMode);
        ponAttribute.setPonPortEncryptKeyExchangeTime(exchangeTime);
        getSqlSession().update(getNameSpace() + "updatePonPortEncryptMode", ponAttribute);
    }

    @Override
    public OltPonStormSuppressionEntry getPonStormSuppression(Long ponId) {
        return getSqlSession().selectOne(getNameSpace() + "getPonStormSuppression", ponId);
    }

    @Override
    public void updatePonMaxLearnMacNum(Long ponId, Long maxLearnMacNum) {
        OltPonAttribute ponAttribute = new OltPonAttribute();
        ponAttribute.setPonId(ponId);
        ponAttribute.setPonPortMacAddrLearnMaxNum(maxLearnMacNum);
        getSqlSession().update(getNameSpace() + "updatePonMaxLearnMacNum", ponAttribute);
    }

    @Override
    public void updatePon15MinPerfStatus(Long ponId, Integer adminStatus) {
        OltPonAttribute ponAttribute = new OltPonAttribute();
        ponAttribute.setPonId(ponId);
        ponAttribute.setPerfStats15minuteEnable(adminStatus);
        getSqlSession().update(getNameSpace() + "updatePon15MinPerfStatus", ponAttribute);
    }

    @Override
    public void updatePon24HourPerfStatus(Long ponId, Integer adminStatus) {
        OltPonAttribute ponAttribute = new OltPonAttribute();
        ponAttribute.setPonId(ponId);
        ponAttribute.setPerfStats24hourEnable(adminStatus);
        getSqlSession().update(getNameSpace() + "updatePon24HourPerfStatus", ponAttribute);
    }

    @Override
    public void modifyPonPortSpeedMode(Long entityId, Long ponId, Integer speedMode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("ponId", ponId);
        map.put("speedMode", speedMode);
        getSqlSession().update(getNameSpace() + "updatePonPortSpeedMode", map);
    }

    @Override
    public TopPonPortSpeedEntry getPonPortSpeedMode(Long entityId, Long ponId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("ponId", ponId);
        return (TopPonPortSpeedEntry) getSqlSession().selectOne(getNameSpace() + "getPonPortSpeedMode", map);
    }

    @Override
    public OltPonAttribute getPonAttribute(Long ponId) {
        return getSqlSession().selectOne(getNameSpace() + "getPonAttribute", ponId);
    }

    @Override
    public Long getPonIndex(Long ponId) {
        return getSqlSession().selectOne(getNameSpace() + "getPonIndex", ponId);
    }

    @Override
    public List<Long> getAllPonIndex(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getAllPonIndex", entityId);
    }

    @Override
    public List<Long> getAllEponIndex(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getAllEponIndex", entityId);
    }

    @Override
    public List<OltSlotAttribute> getOltPonList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltPonList", entityId);
    }

    @Override
    public List<OltSlotAttribute> getOltEponList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltEponList", entityId);
    }

    @Override
    public List<OltSlotAttribute> getOltGponList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltGponList", entityId);
    }

    @Override
    public void insertOrUpdateOltPonAttribute(OltPonAttribute oltPonAttribute) {
        if (((Integer) getSqlSession().selectOne(getNameSpace() + "getOltPonCount", oltPonAttribute)) != 0) {
            getSqlSession().update(getNameSpace() + "updateOltPonAttribute", oltPonAttribute);
        } else {
            OltSlotAttribute oAttribute = new OltSlotAttribute();
            oAttribute.setEntityId(oltPonAttribute.getEntityId());
            oAttribute.setSlotIndex(EponIndex.getSlotIndex(oltPonAttribute.getPonIndex()));
            oltPonAttribute.setSlotId((Long) getSqlSession().selectOne("OltSlot.getSlotId", oAttribute));
            getSqlSession().insert(getNameSpace() + "insertOltPonRelation", oltPonAttribute);
            oltPonAttribute.setPonId(oltPonAttribute.getPonId());
            getSqlSession().insert(getNameSpace() + "insertOltPonAttribute", oltPonAttribute);
        }
    }

    @Override
    public void updatePonStormInfo(OltPonStormSuppressionEntry oltPonStormSuppressionEntry) {
        getSqlSession().update(getNameSpace() + "updatePonStormInfo", oltPonStormSuppressionEntry);
    }

    @Override
    public void insertPonStormInfo(OltPonStormSuppressionEntry oltPonStormSuppressionEntry) {
        getSqlSession().insert(getNameSpace() + "insertPonStormInfo", oltPonStormSuppressionEntry);
    }

    @Override
    public void batchInsertOltPonStormInfo(final List<OltPonStormSuppressionEntry> list) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltPonStormSuppressionEntry stormSuppressionEntry : list) {
                // 通过构造一个pon属性对象，获得ONU对应的ponId
                OltPonAttribute oltPonAttribute = new OltPonAttribute();
                oltPonAttribute.setEntityId(stormSuppressionEntry.getEntityId());
                oltPonAttribute.setPonIndex(stormSuppressionEntry.getPonIndex());
                stormSuppressionEntry.setPonId((Long) sqlSession.selectOne(getNameSpace("getPonId"), oltPonAttribute));
                sqlSession.delete(getNameSpace() + "deletePonStormInfo", stormSuppressionEntry.getPonId());
                sqlSession.insert(getNameSpace() + "insertPonStormInfo", stormSuppressionEntry);
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
        return (Long) getSqlSession().selectOne(getNameSpace() + "getPonId", oltPonAttribute);
    }
    
    @Override
    public List<Long> getPonIdsByPonIndexs(Long entityId, List<Long> ponIndexs){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("ponIndexs", ponIndexs);
        return getSqlSession().selectList(getNameSpace() + "getPonIds", map);
    }

    @Override
    public void updatePonBandMax(Long ponId, Integer bandMax) {
        Map<String, Long> m = new HashMap<String, Long>();
        m.put("ponId", ponId);
        m.put("ponBandMax", bandMax.longValue());
        getSqlSession().update(getNameSpace() + "updatePonBandMax", m);
    }

    @Override
    public void updatePonRateLimit(Long ponId, Integer upRatelimit, Integer downRatelimit) {
        Map<String, Long> m = new HashMap<String, Long>();
        m.put("ponId", ponId);
        m.put("upRatelimit", upRatelimit.longValue());
        m.put("downRatelimit", downRatelimit.longValue());
        getSqlSession().update(getNameSpace() + "updatePonRateLimit", m);
    }

    @Override
    public void batchInsertPonAttribute(final List<OltPonAttribute> list, final HashMap<Long, Long> oltMap) {
        for (OltPonAttribute oltPonAttribute : list) {
            if (oltMap.containsKey(oltPonAttribute.getPonIndex())) {
                getSqlSession().update(getNameSpace() + "updateOltPonAttribute", oltPonAttribute);
            } else {
                oltPonAttribute.setSlotId(oltMap.get(EponIndex.getSlotIndex(oltPonAttribute.getPonIndex())));
                getSqlSession().insert(getNameSpace() + "insertOltPonRelation", oltPonAttribute);
                oltPonAttribute.setPonId(oltPonAttribute.getPonId());
                getSqlSession().insert(getNameSpace() + "insertOltPonAttribute", oltPonAttribute);
                oltMap.put(oltPonAttribute.getPonIndex(), oltPonAttribute.getPonId());
            }
        }
    }

    @Override
    public void batchInsertOltPonSpeed(final List<TopPonPortSpeedEntry> list, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deletePonPortSpeed", entityId);
            for (TopPonPortSpeedEntry ponPortSpeed : list) {
                // 通过构造一个ponPortSpeed属性对象
                TopPonPortSpeedEntry topPonPortSpeedEntry = new TopPonPortSpeedEntry();
                topPonPortSpeedEntry.setEntityId(entityId);
                topPonPortSpeedEntry.setPonPortSpeedCardIndex(ponPortSpeed.getPonPortSpeedCardIndex());
                topPonPortSpeedEntry.setPonIndex(ponPortSpeed.getPonIndex());
                topPonPortSpeedEntry.setPonId(ponPortSpeed.getPonId());
                topPonPortSpeedEntry.setPonPortSpeedMod(ponPortSpeed.getPonPortSpeedMod());
                sqlSession.insert(getNameSpace() + "insertPonPortSpeed", topPonPortSpeedEntry);
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
    public List<OltPonAttribute> getPonPortList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getPonPortList", map);
    }

    @Override
    public List<OltOnuAttribute> getOnuAttributeByPonIndexs(Long entityId, List<Long> ponIndexList, Integer onuType) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        if (ponIndexList.size() == 0) {
            ponIndexList.add(-1L);
        }
        paramMap.put("ponIndexList", ponIndexList);
        String onuTypeSql = "";
        if (onuType < 255) {
            // 可识别ONU
            onuTypeSql = "C.onuPreType = " + onuType;
        } else {
            // 不可识别ONU
            onuTypeSql = "C.onuPreType >= 255";
        }
        // paramMap.put("onuType", onuType);
        paramMap.put("onuTypeSql", onuTypeSql);
        return getSqlSession().selectList(getNameSpace() + "getOnuAttributeByPonIndexs", paramMap);
    }

    @Override
    public void batchUpdatePon15MinStatus(List<OltPonAttribute> oltPonAttrs) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltPonAttribute ponAttribute : oltPonAttrs) {
                // 通过构造一个pon属性对象，获得ONU对应的ponId
                ponAttribute.setPonId((Long) sqlSession.selectOne(getNameSpace("getPonId"), ponAttribute));
                sqlSession.update(getNameSpace() + "updatePon15MinPerfStatus", ponAttribute);
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
    public void updatePonOperationStatus(Long ponId, Integer ponOperationStatus) {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("ponId", ponId);
        m.put("ponOperationStatus", ponOperationStatus);
        this.getSqlSession().update(getNameSpace("updatePonOperationStatus"), m);
    }

    @Override
    public List<OltPonAttribute> getPonListByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getPonListByEntityId", entityId);
    }

    @Override
    public Integer getPonPortType(Long entityId, Long ponIndex) {
        Map<String, Long> param = new HashMap<String, Long>();
        param.put("ponIndex", ponIndex);
        param.put("entityId", entityId);
        return getSqlSession().selectOne(getNameSpace() + "getPonPortType", param);
    }
}
