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

import com.topvision.ems.epon.domain.EponBoardStatistics;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.ems.epon.olt.domain.OltFanAttribute;
import com.topvision.ems.epon.olt.domain.OltFanStatus;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerAttribute;
import com.topvision.ems.epon.olt.domain.OltPowerStatus;
import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotMapTable;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.nbi.tl1.api.domain.BoardInfo;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author flack
 * @created @2013-10-25-上午10:16:09
 *
 */
@Repository("oltSlotDao")
public class OltSlotDaoImpl extends MyBatisDaoSupport<Object> implements OltSlotDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.olt.domain.OltSlot";
    }

    @Override
    public OltSlotAttribute getSlotAttribute(Long slotId) {
        return getSqlSession().selectOne(getNameSpace() + "getSlotAttribute", slotId);
    }

    @Override
    public OltPowerAttribute getPowerAttribute(Long powerCardId) {
        return getSqlSession().selectOne(getNameSpace() + "getPowerAttribute", powerCardId);
    }

    @Override
    public OltFanAttribute getFanAttribute(Long fanCardId) {
        return getSqlSession().selectOne(getNameSpace() + "getFanAttribute", fanCardId);
    }

    @Override
    public void updateFanSpeedControlLevel(Long fanCardId, Integer fanSpeedControlLevel) {
        OltFanAttribute oltFanAttribute = new OltFanAttribute();
        oltFanAttribute.setFanCardId(fanCardId);
        oltFanAttribute.setTopSysFanSpeedControl(fanSpeedControlLevel);
        getSqlSession().update(getNameSpace() + "updateFanSpeedControlLevel", oltFanAttribute);
    }

    @Override
    public void updateFanSpeedControl(OltFanStatus fanStatus) {
        getSqlSession().update(getNameSpace() + "updateOltFanStatusByFanCardId", fanStatus);
    }

    @Override
    public void updateSlotStatusBySlotIndex(Long entityId, OltSlotStatus slotStatus) {
        slotStatus.setEntityId(entityId);
        getSqlSession().update(getNameSpace() + "updateOltSlotStatus", slotStatus);
    }

    @Override
    public void updateSlotInstalled(Long entityId, Integer bPresenceStatus, Integer topSysBdActualType,
            Long slotIndex) {
        Map<Object, Object> param = new HashMap<Object, Object>();
        param.put("entityId", entityId);
        param.put("slotIndex", slotIndex);
        param.put("bPresenceStatus", bPresenceStatus);
        param.put("topSysBdActualType", topSysBdActualType);
        getSqlSession().update(getNameSpace() + "updateBoardStatusBySlotIndex", param);
    }

    @Override
    public OltFanStatus getFanRealSpeed(Long fanCardId) {
        return (OltFanStatus) getSqlSession().selectOne(getNameSpace() + "getFanStatus", fanCardId);
    }

    @Override
    public List<EponBoardStatistics> getBoardList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getEponBoardList", map);
    }

    @Override
    public Long getSlotNoById(Long entityId, Long slotId) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("entityId", entityId);
        paramMap.put("slotId", slotId);
        return (Long) getSqlSession().selectOne(getNameSpace() + "getSlotNoById", paramMap);
    }

    @Override
    public List<OltSlotAttribute> getOltSlotList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltSlotList", entityId);
    }

    @Override
    public OltSlotStatus getOltSlotStatus(Long slotId) {
        return getSqlSession().selectOne(getNameSpace() + "getSlotStatus", slotId);
    }

    @Override
    public List<OltPonAttribute> getSlotPonList(Long slotId) {
        return getSqlSession().selectList(getNameSpace() + "getSlotPonList", slotId);
    }

    @Override
    public List<OltSniAttribute> getSlotSniList(Long slotId) {
        return getSqlSession().selectList(getNameSpace() + "getSlotSniList", slotId);
    }

    @Override
    public List<OltUniAttribute> getSlotUniList(Long onuId) {
        return getSqlSession().selectList(getNameSpace() + "getSlotUniList", onuId);
    }

    @Override
    public Long getSlotIndex(Long slotId) {
        return getSqlSession().selectOne(getNameSpace() + "getSlotIndex", slotId);
    }

    @Override
    public Long getFanCardIndex(Long fanCardId) {
        return getSqlSession().selectOne(getNameSpace() + "getFanCardIndex", fanCardId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.olt.dao.OltSlotDao#recordOltSlotCollect(java.lang.Long,
     * java.lang.Long, java.lang.Integer)
     */
    @Override
    public void recordOltSlotCollect(Long slotId, Long entityId, Integer collectPreConfigType) {
        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("slotId", slotId);
        paramMap.put("collectPreConfig", collectPreConfigType);
        OltSlotAttribute slotAttribute = getSlotAttribute(slotId);
        paramMap.put("databasePreConfig", slotAttribute.getTopSysBdPreConfigType());
        getSqlSession().insert(getNameSpace() + "recordOltSlotCollect", paramMap);
    }

    @Override
    public void deleteOltSlot(Long slotId, Long entityId, Integer preConfigType) {
        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("slotId", slotId);
        paramMap.put("preConfigType", preConfigType);
        // 更新板卡预配置
        getSqlSession().update(getNameSpace() + "updateSlotPerConfig", paramMap);
        // 删除板卡对应的PON口
        getSqlSession().delete(getNameSpace() + "deleteSlotPonPort", paramMap);
        // 删除板卡对应的SNI口
        getSqlSession().delete(getNameSpace() + "deleteSlotSniPort", paramMap);
    }

    @Override
    public OltFanStatus getFanStatus(Long fanCardId) {
        return getSqlSession().selectOne(getNameSpace() + "getFanStatus", fanCardId);
    }

    @Override
    public OltPowerStatus getPowerStatus(Long powerCardId) {
        return getSqlSession().selectOne(getNameSpace() + "getPowerStatus", powerCardId);
    }

    @Override
    public void batchInsertOltFanStatus(final List<OltFanStatus> list) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltFanStatus oltFanStatus : list) {
                sqlSession.update(getNameSpace() + "updateOltFanStatus", oltFanStatus);
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
    public void batchInsertOltSlotStatus(final List<OltSlotStatus> list) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltSlotStatus oltSlotStatus : list) {
                Long slotIndex = getSqlSession().selectOne(getNameSpace("getSlotIndexBySlotNo"), oltSlotStatus);
                if (slotIndex != null) {
                    oltSlotStatus.setSlotIndex(slotIndex);
                }
                sqlSession.update(getNameSpace() + "updateOltSlotStatus", oltSlotStatus);
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
    public void batchInsertOltPowerStatus(final List<OltPowerStatus> list) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltPowerStatus oltPowerStatus : list) {
                sqlSession.update(getNameSpace() + "updateOltPowerStatus", oltPowerStatus);
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
    public void batchInsertOltPowerAttribute(Long entityId, List<OltPowerAttribute> list) {
        getSqlSession().delete(getNameSpace() + "batchDeleteOltPowerAttribute", entityId);
        for (OltPowerAttribute oltPowerAttribute : list) {
            getSqlSession().insert(getNameSpace() + "insertOltPowerRelation", oltPowerAttribute);
            oltPowerAttribute.setPowerCardId(oltPowerAttribute.getPowerCardId());
            OltPowerStatus oltPowerStatus = new OltPowerStatus();
            oltPowerStatus.setPowerCardId(oltPowerAttribute.getPowerCardId());
            getSqlSession().insert(getNameSpace() + "insertOltPowerStatus", oltPowerStatus);
            getSqlSession().insert(getNameSpace() + "insertOltPowerAttribute", oltPowerAttribute);
        }
    }

    @Override
    public void batchInsertOltFanAttribute(Long entityId, List<OltFanAttribute> list) {
        getSqlSession().delete(getNameSpace() + "batchDeleteOltFanAttribute", entityId);
        for (OltFanAttribute oltFanAttribute : list) {
            getSqlSession().insert(getNameSpace() + "insertOltFanRelation", oltFanAttribute);
            oltFanAttribute.setFanCardId(oltFanAttribute.getFanCardId());
            OltFanStatus oltFanStatus = new OltFanStatus();
            oltFanStatus.setFanCardId(oltFanAttribute.getFanCardId());
            getSqlSession().insert(getNameSpace() + "insertOltFanStatus", oltFanStatus);
            getSqlSession().insert(getNameSpace() + "insertOltFanAttribute", oltFanAttribute);
        }
    }

    @Override
    public List<Integer> getMpuaBoardList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getMpuaBoardList", entityId);
    }

    @Override
    public void updateSlotBdTempDetectEnable(Long slotId, Integer slotBdTempDetectEnable) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("slotId", slotId);
        paramMap.put("slotBdTempDetectEnable", slotBdTempDetectEnable);
        getSqlSession().update(getNameSpace() + "updateSlotBdTempDetectEnable", paramMap);
    }

    @Override
    public void updateBdTemperature(Long slotId, Integer temperature) {
        HashMap<String, Long> map = new HashMap<String, Long>();
        map.put("slotId", slotId);
        map.put("temperature", temperature.longValue());
        getSqlSession().update(getNameSpace() + "updateBdTemperature", map);
    }

    @Override
    public void updateOltPower(Long entityId, List<OltPowerAttribute> powerAttributes,
            List<OltPowerStatus> powerStatus) {
        getSqlSession().delete(getNameSpace() + "deletePower", entityId);
        for (OltPowerAttribute oltPowerAttribute : powerAttributes) {
            getSqlSession().insert(getNameSpace() + "insertOltPowerRelation", oltPowerAttribute);
            oltPowerAttribute.setPowerCardId(oltPowerAttribute.getPowerCardId());
            // 再插入关系表的时候，必须插入相应状态表的空数据，这样才能保证以后的状态信息都属于更新
            OltPowerStatus oltPowerStatus = new OltPowerStatus();
            oltPowerStatus.setEntityId(entityId);
            oltPowerStatus.setPowerCardId(oltPowerAttribute.getPowerCardId());
            getSqlSession().insert(getNameSpace() + "insertOltPowerStatus", oltPowerStatus);
            getSqlSession().insert(getNameSpace() + "insertOltPowerAttribute", oltPowerAttribute);
        }
        for (OltPowerStatus pStatus : powerStatus) {
            getSqlSession().update(getNameSpace() + "updateOltPowerStatus", pStatus);
        }
    }

    @Override
    public void updateOltFan(Long entityId, List<OltFanAttribute> fanAttributes, List<OltFanStatus> fanStatus) {
        getSqlSession().delete(getNameSpace() + "deleteFan", entityId);
        for (OltFanAttribute oltFanAttribute : fanAttributes) {
            getSqlSession().insert(getNameSpace() + "insertOltFanRelation", oltFanAttribute);
            oltFanAttribute.setFanCardId(oltFanAttribute.getFanCardId());
            // 再插入关系表的时候，必须插入相应状态表的空数据，这样才能保证以后的状态信息都属于更新
            OltFanStatus oltFanStatus = new OltFanStatus();
            oltFanStatus.setFanCardId(oltFanAttribute.getFanCardId());
            oltFanStatus.setEntityId(entityId);
            getSqlSession().insert(getNameSpace() + "insertOltFanStatus", oltFanStatus);
            getSqlSession().insert(getNameSpace() + "insertOltFanAttribute", oltFanAttribute);
        }
        for (OltFanStatus fStatus : fanStatus) {
            getSqlSession().update(getNameSpace() + "updateOltFanStatus", fStatus);
        }
    }

    @Override
    public void updateOltBoardOperationStatus(Long entityId, Long slotIndex, Integer operationStatus) {
        Map<Object, Object> param = new HashMap<Object, Object>();
        param.put("entityId", entityId);
        param.put("slotIndex", slotIndex);
        param.put("bOperationStatus", operationStatus);
        getSqlSession().update(getNameSpace() + "updateBoardOperationStatusBySlotIndex", param);
    }

    @Override
    public void batchInsertSlotAttribute(final List<OltSlotAttribute> list, final HashMap<Long, Long> oltMap) {
        for (OltSlotAttribute oltSlotAttribute : list) {
            if (oltMap.containsKey(oltSlotAttribute.getSlotIndex())) {
                getSqlSession().update(getNameSpace() + "updateOltSlotAttribute", oltSlotAttribute);
            } else {
                getSqlSession().insert(getNameSpace() + "insertOltSlotRelation", oltSlotAttribute);
                oltSlotAttribute.setSlotId(oltSlotAttribute.getSlotId());
                OltSlotStatus oltSlotStatus = new OltSlotStatus();
                oltSlotStatus.setSlotId(oltSlotAttribute.getSlotId());
                getSqlSession().insert(getNameSpace() + "insertOltSlotStatus", oltSlotStatus);
                // 板卡重启时提示信息为"与期望板卡类型不一致"
                if (oltSlotAttribute.getTopSysBdActualType().equals(255)) {
                    oltSlotAttribute.setTopSysBdActualType(0);
                }
                getSqlSession().insert(getNameSpace() + "insertOltSlotAttribute", oltSlotAttribute);
                Map<Object, Object> paramMap = new HashMap<Object, Object>();
                paramMap.put("entityId", oltSlotAttribute.getEntityId());
                paramMap.put("slotId", oltSlotAttribute.getSlotId());
                paramMap.put("collectPreConfig", oltSlotAttribute.getTopSysBdPreConfigType());
                paramMap.put("databasePreConfig", -1);
                getSqlSession().insert(getNameSpace() + "recordOltSlotCollect", paramMap);
                oltMap.put(oltSlotAttribute.getSlotIndex(), oltSlotAttribute.getSlotId());
            }
        }
    }

    @Override
    public void updateSlotAttribute(Long entityId, OltSlotAttribute slotAttribute) {
        slotAttribute.setEntityId(entityId);
        getSqlSession().update(getNameSpace() + "updateOltSlotAttribute", slotAttribute);
    }

    @Override
    public void updateSlotStatus(Long slotId, Integer status) {
        Map<Object, Object> param = new HashMap<Object, Object>();
        param.put("slotId", slotId);
        param.put("status", status);
        getSqlSession().update(getNameSpace() + "updateBoardStatus", param);
    }

    @Override
    public List<Long> getV152MpuSlotIndex(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getV152MpuSlotIndex", entityId);
    }

    @Override
    public void deleteV152MpuSlot(final Long entityId, final List<Long> V152MpuSlotIndex) {
        SqlSession sqlSession = getBatchSession();
        try {
            Map<String, Long> param = new HashMap<String, Long>();
            for (Long slotIndex : V152MpuSlotIndex) {
                param.put("entityId", entityId);
                param.put("slotIndex", slotIndex);
                sqlSession.insert(getNameSpace() + "deleteV152MpuSlot", param);
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
    public void updateMasterOrSlaveStatus(Long slotId, Integer bAttribute, Long slotNo) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("slotId", slotId);
        paramMap.put("bAttribute", bAttribute);
        paramMap.put("slotNo", slotNo);
        getSqlSession().update(getNameSpace() + "updateSlotAttribue", paramMap);
    }

    @Override
    public Long getSlotNoByIndex(Long slotIndex, Long entityId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("slotIndex", slotIndex);
        paramMap.put("entityId", entityId);
        return getSqlSession().selectOne(getNameSpace("getSlotNoByIndex"), paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.olt.dao.OltSlotDao#getMasterSlotIndex(java.lang.Long)
     */
    @Override
    public Long getMasterSlotNo(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getMasterSlotNo"), entityId);
    }

    @Override
    public void batchInsertSlotMap(List<OltSlotMapTable> slotMapList, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        sqlSession.delete(getNameSpace() + "deleteSlotMap", entityId);
        try {
            for (OltSlotMapTable slotMap : slotMapList) {
                sqlSession.insert(getNameSpace("insertSlotMap"), slotMap);
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
    public int querySlotLogNo(Map<String, Object> paramsMap) {
        return this.getSqlSession().selectOne(getNameSpace("getSlotLogNo"), paramsMap);
    }

    @Override
    public void updateBoardLampStatus(Long slotId, Integer lampStatus) {
        Map<Object, Object> param = new HashMap<Object, Object>();
        param.put("slotId", slotId);
        param.put("status", lampStatus);
        getSqlSession().update(getNameSpace() + "updateLampStatus", param);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.olt.dao.OltSlotDao#getSlotIdByPhyNo(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public Long getSlotIdByPhyNo(Long entityId, Long slotNo) {
        Map<Object, Object> param = new HashMap<Object, Object>();
        param.put("entityId", entityId);
        param.put("slotNo", slotNo);
        return getSqlSession().selectOne(getNameSpace("getSlotIdByPhyNo"), param);
    }

    @Override
    public List<Long> getEntityIdBySlotId(Long slotId) {
        return getSqlSession().selectList(getNameSpace("getEntityIdBySlotId"), slotId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.olt.dao.OltSlotDao#selectSlotList(java.lang.Long)
     */
    @Override
    public List<OltSlotAttribute> selectSlotList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectSlotList"), entityId);
    }

    @Override
    public Long getSlotIdByIndex(Long entityId, Long slotIndex) {
        Map<Object, Object> param = new HashMap<Object, Object>();
        param.put("entityId", entityId);
        param.put("slotIndex", slotIndex);
        return getSqlSession().selectOne(getNameSpace("getSlotIdByIndex"), param);
    }

    @Override
    public void updateBoardAlarmStatus(Long slotId, Integer bAlarmStatus) {
        Map<Object, Object> param = new HashMap<Object, Object>();
        param.put("slotId", slotId);
        param.put("bAlarmStatus", bAlarmStatus);
        getSqlSession().update(getNameSpace("updateBoardAlarmStatus"), param);
    }

    @Override
    public Integer querySlotPreType(Map<String, Object> paramsMap) {
        return getSqlSession().selectOne(getNameSpace("querySlotPreType"), paramsMap);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.olt.dao.OltSlotDao#selectSlotListForTl1(long)
     */
    @Override
    public List<BoardInfo> selectSlotListForTl1(long entityId) {
        return getSqlSession().selectList(getNameSpace("selectSlotListForTl1"), entityId);
    }

    @Override
    public Integer querySlotActualType(HashMap<String, Object> paramsMap) {
        return getSqlSession().selectOne(getNameSpace("querySlotActualType"), paramsMap);
    }

}
