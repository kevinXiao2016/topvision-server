package com.topvision.ems.cmc.cpe.dao.mybatis;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cpe.dao.CpeAnalyseDao;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.TopCpeAttribute;
import com.topvision.ems.cmc.performance.domain.CmAct;
import com.topvision.ems.cmc.performance.domain.CmNum;
import com.topvision.ems.cmc.performance.domain.CpeAct;
import com.topvision.ems.cmc.performance.domain.InitialDataCmAction;
import com.topvision.ems.cmc.performance.domain.InitialDataCpeAction;
import com.topvision.ems.cmc.performance.domain.Monitor;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.common.CollectTimeRange;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponIndex;

/**
 * 
 * @author YangYi
 * @created @2013-10-17-上午8:34:29
 * 
 */
@Repository("cpeAnalyseDao")
public class CpeAnalyseDaoImpl extends MyBatisDaoSupport<Entity>implements CpeAnalyseDao {
    private static Map<Long, Map<Long, CmAct>> lastCmActs = Collections
            .synchronizedMap(new HashMap<Long, Map<Long, CmAct>>());

    private static Map<Long, Map<Long, Map<Long, CpeAct>>> lastCpeActs = Collections
            .synchronizedMap(new HashMap<Long, Map<Long, Map<Long, CpeAct>>>());

    @Override
    public List<InitialDataCmAction> getInitialDataCmActionByTimeRange(CollectTimeRange ctr) {
        return getSqlSession().selectList(getNameSpace("getInitialDataCmActionByTimeRange"), ctr);
    }

    @Override
    public Map<Long, Map<Long, InitialDataCpeAction>> getInitialDataCpeActionByTimeRange(CollectTimeRange ctr) {
        Map<Long, Map<Long, InitialDataCpeAction>> re = new HashMap<Long, Map<Long, InitialDataCpeAction>>();
        List<InitialDataCpeAction> list = getSqlSession().selectList(getNameSpace("getInitialDataCpeActionByTimeRange"),
                ctr);
        for (InitialDataCpeAction initialDataCpeAction : list) {
            Map<Long, InitialDataCpeAction> tmp;
            if (re.containsKey(initialDataCpeAction.getCmmac())) {
                tmp = re.get(initialDataCpeAction.getCmmac());
            } else {
                tmp = new HashMap<Long, InitialDataCpeAction>();
                re.put(initialDataCpeAction.getCmmac(), tmp);
            }
            tmp.put(initialDataCpeAction.getCpemac(), initialDataCpeAction);
        }
        return re;
    }

    @Override
    public void insertAllCmNum(CmNum all) {
        getSqlSession().insert(getNameSpace("insertAllCmNum"), all);
    }

    @Override
    public void insertAreaCmNum(Map<Long, CmNum> areaMap) {
        // 重新获取批量模式session
        SqlSessionTemplate sst = (SqlSessionTemplate) getSqlSession();
        SqlSession session = sst.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (Long areaId : areaMap.keySet()) {
                CmNum cmNum = areaMap.get(areaId);
                cmNum.setAreaId(areaId);
                session.insert(getNameSpace("insertAreaCmNum"), cmNum);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertDeviceCmNum(Map<Long, CmNum> deviceMap) {
        SqlSessionTemplate sst = (SqlSessionTemplate) getSqlSession();
        SqlSession session = sst.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (Long entityId : deviceMap.keySet()) {
                CmNum cmNum = deviceMap.get(entityId);
                cmNum.setEntityId(entityId);
                session.insert(getNameSpace("insertDeviceCmNum"), cmNum);
                // 插入汇总表
                session.insert(getNameSpace("insertDeviceCmNumSummary"), cmNum);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertPonCmNum(Map<Long, Map<Long, CmNum>> ponMap) {
        SqlSessionTemplate sst = (SqlSessionTemplate) getSqlSession();
        SqlSession session = sst.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (Long entityId : ponMap.keySet()) {
                Map<Long, CmNum> entityMap = ponMap.get(entityId);
                for (Long ponIndex : entityMap.keySet()) {
                    CmNum cmNum = entityMap.get(ponIndex);
                    cmNum.setEntityId(entityId);
                    cmNum.setPonIndex(ponIndex);
                    session.insert(getNameSpace("insertPonCmNum"), cmNum);
                }
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertCmtsCmNum(Map<Long, Map<Long, CmNum>> cmtsMap) {
        SqlSessionTemplate sst = (SqlSessionTemplate) getSqlSession();
        SqlSession session = sst.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (Long entityId : cmtsMap.keySet()) {
                Map<Long, CmNum> entityMap = cmtsMap.get(entityId);
                for (Long ccIfIndex : entityMap.keySet()) {
                    CmNum cmNum = entityMap.get(ccIfIndex);
                    cmNum.setEntityId(entityId);
                    cmNum.setCcIfIndex(ccIfIndex);
                    session.insert(getNameSpace("insertCmtsCmNum"), cmNum);
                }
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertDownCmNum(Map<Long, Map<Long, Map<Long, CmNum>>> downMap) {
        SqlSessionTemplate sst = (SqlSessionTemplate) getSqlSession();
        SqlSession session = sst.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (Long entityId : downMap.keySet()) {
                Map<Long, Map<Long, CmNum>> entityMap = downMap.get(entityId);
                for (Long ccIfIndex : entityMap.keySet()) {
                    Map<Long, CmNum> cmtsMap = entityMap.get(ccIfIndex);
                    for (Long portIfIndex : cmtsMap.keySet()) {
                        CmNum cmNum = cmtsMap.get(portIfIndex);
                        cmNum.setEntityId(entityId);
                        cmNum.setCcIfIndex(ccIfIndex);
                        cmNum.setPortIfIndex(portIfIndex);
                        cmNum.setPortType(CmNum.DOWNCHANNEL);
                        session.insert(getNameSpace("insertPortCmNum"), cmNum);
                    }
                }
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertUpCmNum(Map<Long, Map<Long, Map<Long, CmNum>>> upMap) {
        SqlSessionTemplate sst = (SqlSessionTemplate) getSqlSession();
        SqlSession session = sst.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (Long entityId : upMap.keySet()) {
                Map<Long, Map<Long, CmNum>> entityMap = upMap.get(entityId);
                for (Long ccIfIndex : entityMap.keySet()) {
                    Map<Long, CmNum> cmtsMap = entityMap.get(ccIfIndex);
                    for (Long portIfIndex : cmtsMap.keySet()) {
                        CmNum cmNum = cmtsMap.get(portIfIndex);
                        cmNum.setEntityId(entityId);
                        cmNum.setCcIfIndex(ccIfIndex);
                        cmNum.setPortIfIndex(portIfIndex);
                        cmNum.setPortType(CmNum.UPCHANNEL);
                        session.insert(getNameSpace("insertPortCmNum"), cmNum);
                    }
                }
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public CmAct getCmLastStatus(Long entityId, CmAttribute cmAttribute) {
        Long macLong = new MacUtils(cmAttribute.getStatusMacAddress()).longValue();
        Map<Long, CmAct> cmMacMap = getCmMacMap(entityId);
        if (cmMacMap.containsKey(macLong)) {
            return cmMacMap.get(macLong);
        } else {
            Map<String, Long> map = new HashMap<>();
            map.put("entityId", entityId);
            map.put("cmmac", macLong);
            CmAct cmAct = getSqlSession().selectOne(getNameSpace("getCmLastStatus"), map);
            cmMacMap.put(macLong, cmAct);
            return cmAct;
        }
    }

    @Override
    public void insertCmAct(CmAct ca) {
        getSqlSession().insert(getNameSpace("insertCmAct"), ca);
        Map<Long, CmAct> cmMacMap = getCmMacMap(ca.getEntityId());
        cmMacMap.put(ca.getCmmac(), ca);
    }

    @Override
    public List<CmAct> getCmLastStatusByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getCmLastStatusByEntityId"), entityId);
    }

    @Override
    public CpeAct getCpeLastStatus(Long entityId, TopCpeAttribute cpeAttribute) {
        Long cmMacLong = new MacUtils(cpeAttribute.getTopCmCpeToCmMacAddrLong()).longValue();
        Long cpeMacLong = new MacUtils(cpeAttribute.getTopCmCpeMacAddressLong()).longValue();
        Map<Long, CpeAct> cpeMacMap = getCpeMacMap(entityId, cmMacLong);
        if (cpeMacMap.containsKey(cpeMacLong)) {
            return cpeMacMap.get(cpeMacLong);
        } else {
            Map<String, Long> map = new HashMap<>();
            map.put("entityId", entityId);
            map.put("cmmac", cmMacLong);
            map.put("cpemac", cpeMacLong);
            CpeAct cpeAct = getSqlSession().selectOne(getNameSpace("getCpeLastStatus"), map);
            cpeMacMap.put(cpeMacLong, cpeAct);
            return cpeAct;
        }
    }

    @Override
    public void insertCpeAct(CpeAct ca) {
        getSqlSession().insert(getNameSpace("insertCpeAct"), ca);
        Map<Long, CpeAct> cpeMacMap = getCpeMacMap(ca.getEntityId(), ca.getCmmac());
        cpeMacMap.put(ca.getCpemac(), ca);
    }

    @Override
    public List<CpeAct> getCpeLastStatusByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getCpeLastStatusByEntityId"), entityId);
    }

    @Override
    public boolean isAllCmMonitorHasCollect(Long endTime) {
        Map<String, Timestamp> map = new HashMap<String, Timestamp>();
        map.put("endTime", new Timestamp(endTime));
        return ((Integer) getSqlSession().selectOne(getNameSpace("isAllCmMonitorHasCollect"), map)) == 0;
    }

    @Override
    public Long getCmMonitorMaxCollectTime() {
        Object obj = getSqlSession().selectOne(getNameSpace("getCmMonitorMaxCollectTime"));
        if (obj != null) {
            return ((Monitor) obj).getLastCollectTimeLong();
        } else {
            return null;
        }
    }

    @Override
    public void deleteCmHistoryData(Long cmHistorySavePolicy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("collecttime", new Timestamp(System.currentTimeMillis() - cmHistorySavePolicy));
        getSqlSession().delete(getNameSpace("deleteCmHistoryData"), map);
    }

    @Override
    public void deleteActionData(Long actionDataSavePolicy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("realtime", new Timestamp(System.currentTimeMillis() - actionDataSavePolicy));
        map.put("table", "initialdatacmaction");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "initialdatacpeaction");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
    }

    @Override
    public void deleteInitialData(Long initialDataSavePolicy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("realtime", new Timestamp(System.currentTimeMillis() - initialDataSavePolicy));
        map.put("table", "initialdatacmaction");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "initialdatacpeaction");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
    }

    @Override
    public void deleteStatisticData(Long statisticDataSavePolicy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("realtime", new Timestamp(System.currentTimeMillis() - statisticDataSavePolicy));
        map.put("table", "usernumhisall");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "usernumhisarea");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "usernumhisdevice");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "usernumhispon");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "usernumhisccmts");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "usernumhisport");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "usernumlastdevice");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "usernumlastpon");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "usernumlastccmts");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
        map.put("table", "usernumlastport");
        getSqlSession().delete(getNameSpace("deleteDate"), map);
    }

    @Override
    public void insertDeviceCmNumLast(Map<Long, CmNum> lastCmNum) {
        SqlSessionTemplate sst = (SqlSessionTemplate) getSqlSession();
        SqlSession session = sst.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (Long entityId : lastCmNum.keySet()) {
                session.delete(getNameSpace("deleteDeviceCmNumLast"), entityId);
                CmNum cmNum = lastCmNum.get(entityId);
                cmNum.setEntityId(entityId);
                cmNum.setAllNum(cmNum.getOnlineNum() + cmNum.getOfflineNum() + cmNum.getOtherNum());
                session.insert(getNameSpace("insertDeviceCmNumLast"), cmNum);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertPonCmNumLast(Map<Long, Map<Long, CmNum>> ponMap) {
        SqlSessionTemplate sst = (SqlSessionTemplate) getSqlSession();
        SqlSession session = sst.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (Long entityId : ponMap.keySet()) {
                Map<Long, CmNum> entityMap = ponMap.get(entityId);
                session.delete(getNameSpace("deletePonCmNumLast"), entityId);
                for (Long ponIndex : entityMap.keySet()) {
                    CmNum cmNum = entityMap.get(ponIndex);
                    cmNum.setEntityId(entityId);
                    cmNum.setPonIndex(ponIndex);
                    cmNum.setAllNum(cmNum.getOnlineNum() + cmNum.getOfflineNum() + cmNum.getOtherNum());
                    session.insert(getNameSpace("insertPonCmNumLast"), cmNum);
                }
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }

    }

    @Override
    public void insertCmtsCmNumLast(Map<Long, Map<Long, CmNum>> cmtsMap) {
        SqlSessionTemplate sst = (SqlSessionTemplate) getSqlSession();
        SqlSession session = sst.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (Long entityId : cmtsMap.keySet()) {
                Map<Long, CmNum> entityMap = cmtsMap.get(entityId);
                session.delete(getNameSpace("deleteCmtsCmNumLast"), entityId);
                for (Long ccIfIndex : entityMap.keySet()) {
                    CmNum cmNum = entityMap.get(ccIfIndex);
                    cmNum.setEntityId(entityId);
                    cmNum.setCcIfIndex(ccIfIndex);
                    cmNum.setAllNum(cmNum.getOnlineNum() + cmNum.getOfflineNum() + cmNum.getOtherNum());
                    session.insert(getNameSpace("insertCmtsCmNumLast"), cmNum);
                }
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }

    }

    @Override
    public void insertPortCmNumLast(Map<Long, Map<Long, Map<Long, CmNum>>> downMap,
            Map<Long, Map<Long, Map<Long, CmNum>>> upMap) {
        SqlSessionTemplate sst = (SqlSessionTemplate) getSqlSession();
        SqlSession session = sst.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (Long entityId : upMap.keySet()) {
                Map<Long, Map<Long, CmNum>> entityUpMap = upMap.get(entityId);
                Map<Long, Map<Long, CmNum>> entityDownMap = downMap.get(entityId);
                session.delete(getNameSpace("deletePortCmNumLast"), entityId);
                for (Long ccIfIndex : entityUpMap.keySet()) {
                    Map<Long, CmNum> cmtsUpMap = entityUpMap.get(ccIfIndex);
                    Map<Long, CmNum> cmtsDownMap = entityDownMap.get(ccIfIndex);
                    for (Long portIfIndex : cmtsUpMap.keySet()) {
                        CmNum cmNum = cmtsUpMap.get(portIfIndex);
                        cmNum.setEntityId(entityId);
                        cmNum.setCcIfIndex(ccIfIndex);
                        cmNum.setPortIfIndex(portIfIndex);
                        cmNum.setPortType(CmNum.UPCHANNEL);
                        cmNum.setAllNum(cmNum.getOnlineNum() + cmNum.getOfflineNum() + cmNum.getOtherNum());
                        session.insert(getNameSpace("insertPortCmNumLast"), cmNum);
                    }
                    for (Long portIfIndex : cmtsDownMap.keySet()) {
                        CmNum cmNum = cmtsDownMap.get(portIfIndex);
                        cmNum.setEntityId(entityId);
                        cmNum.setCcIfIndex(ccIfIndex);
                        cmNum.setPortIfIndex(portIfIndex);
                        cmNum.setPortType(CmNum.DOWNCHANNEL);
                        cmNum.setAllNum(cmNum.getOnlineNum() + cmNum.getOfflineNum() + cmNum.getOtherNum());
                        session.insert(getNameSpace("insertPortCmNumLast"), cmNum);
                    }
                }
            }
            session.commit();
        } catch (Exception e) {
            logger.debug("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertDeviceCmNum(CmNum cmNum) {
        getSqlSession().insert(getNameSpace("insertDeviceCmNumLast"), cmNum);
    }

    @Override
    public void insertPonCmNum(CmNum cmNum) {
        getSqlSession().insert(getNameSpace("insertPonCmNumLast"), cmNum);
    }

    @Override
    public void insertCmtsCmNum(CmNum cmNum) {
        getSqlSession().insert(getNameSpace("insertCmtsCmNumLast"), cmNum);
    }

    @Override
    public void insertPortCmNum(CmNum cmNum) {
        getSqlSession().insert(getNameSpace("insertPortCmNumLast"), cmNum);
    }

    @Override
    public Map<Long, CmNum> selectAllDeviceLastCmNum() {
        List<CmNum> allCmNum = getSqlSession().selectList(getNameSpace("selectAllDeviceLastCmNum"));
        Map<Long, CmNum> re = new HashMap<Long, CmNum>();
        for (CmNum cmNum : allCmNum) {
            re.put(cmNum.getEntityId(), cmNum);
        }
        return re;
    }

    @Override
    public void deleteDeviceCmNumLast(Long entityId) {
        getSqlSession().delete(getNameSpace("deleteDeviceCmNumLast"), entityId);
    }

    @Override
    public void deleteCmtsCmNumLast(Long entityId) {
        getSqlSession().delete(getNameSpace("deleteCmtsCmNumLast"), entityId);
    }

    @Override
    public void deletePonLastCmNum(Long entityId) {
        getSqlSession().delete(getNameSpace("deletePonCmNumLast"), entityId);
    }

    @Override
    public void deletePortCmNumLast(Long entityId) {
        getSqlSession().delete(getNameSpace("deletePortCmNumLast"), entityId);
    }

    @Override
    public void deleteDeviceCmNumLast(CmNum cmNum) {
        getSqlSession().delete(getNameSpace("deleteDeviceCmNumLastByCmNum"), cmNum);
    }

    @Override
    public void deleteCmtsCmNumLast(CmNum cmNum) {
        getSqlSession().delete(getNameSpace("deleteCmtsCmNumLastByCmNum"), cmNum);

    }

    @Override
    public void deletePonLastCmNum(CmNum cmNum) {
        getSqlSession().delete(getNameSpace("deletePonCmNumLastByCmNum"), cmNum);

    }

    @Override
    public void deletePortCmNumLast(CmNum cmNum) {
        getSqlSession().delete(getNameSpace("deletePortCmNumLastByCmNum"), cmNum);

    }

    @Override
    public void updateDeviceCmNumLast(CmNum cmNum) {
        getSqlSession().update(getNameSpace("updateDeviceCmNumLast"), cmNum);
    }

    @Override
    public void updatePonCmNumLast(CmNum cmNum) {
        getSqlSession().update(getNameSpace("updatePonCmNumLast"), cmNum);
    }

    @Override
    public void updateCmtsCmNumLast(CmNum cmNum) {
        getSqlSession().update(getNameSpace("updateCmtsCmNumLast"), cmNum);
    }

    @Override
    public void updatePortCmNumLast(CmNum cmNum) {
        getSqlSession().update(getNameSpace("updatePortCmNumLast"), cmNum);
    }

    @Override
    public void refreshPonLastCmNum(CmNum ponNum) {
        Integer slotNo = EponIndex.getSlotNo(ponNum.getPonIndex()).intValue();
        Integer nextPonNo = EponIndex.getPonNo(ponNum.getPonIndex()).intValue() + 1;
        Long nextPonIndex = EponIndex.getPonIndex(slotNo, nextPonNo);
        Map<String, Long> map = new HashMap<>();
        map.put("entityId", ponNum.getEntityId());
        map.put("ponIndex", ponNum.getPonIndex());
        map.put("nextPonIndex", nextPonIndex);
        getSqlSession().update(getNameSpace("refreshPonLastCmNum"), map);
    }

    @Override
    public List<CmNum> selectCmtsCmNumByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectCmtsCmNumByEntityId"), entityId);
    }

    @Override
    public void insertDeviceCmNumLastByPonNum(Long entityId) {
        getSqlSession().insert(getNameSpace("insertDeviceCmNumLastByPonNum"), entityId);
    }

    @Override
    public void staticCpeNum() {
        SqlSession session = getSqlSession();
        session.delete(getNameSpace("deleteStaticCpeNum"));
        session.insert(getNameSpace("staticCpeNum"));
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.cpe.domain.CmCpeAnalyse";
    }

    public Long getCmcIdByEntityIdAndIndex(Long entityId, Long cmcIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("cmcIndex", cmcIndex);
        return (Long) getSqlSession().selectOne(getNameSpace("getCmcIdByEntityIdAndIndex"), map);
    }

    private Map<Long, CmAct> getCmMacMap(Long entityId) {
        Map<Long, CmAct> cmMacMap;
        if (lastCmActs.containsKey(entityId)) {
            cmMacMap = lastCmActs.get(entityId);
        } else {
            cmMacMap = Collections.synchronizedMap(new HashMap<Long, CmAct>());
            lastCmActs.put(entityId, cmMacMap);
        }
        return cmMacMap;
    }

    private Map<Long, CpeAct> getCpeMacMap(Long entityId, Long cmMacLong) {
        Map<Long, Map<Long, CpeAct>> cmMacMap;
        if (lastCpeActs.containsKey(entityId)) {
            cmMacMap = lastCpeActs.get(entityId);
        } else {
            cmMacMap = Collections.synchronizedMap(new HashMap<Long, Map<Long, CpeAct>>());
            lastCpeActs.put(entityId, cmMacMap);
        }
        Map<Long, CpeAct> cpeMacMap;
        if (cmMacMap.containsKey(cmMacLong)) {
            cpeMacMap = cmMacMap.get(cmMacLong);
        } else {
            cpeMacMap = Collections.synchronizedMap(new HashMap<Long, CpeAct>());
            cmMacMap.put(cmMacLong, cpeMacMap);
        }
        return cpeMacMap;
    }

}
