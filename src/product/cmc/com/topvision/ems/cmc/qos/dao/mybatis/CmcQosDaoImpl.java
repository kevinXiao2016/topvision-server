/***********************************************************************
 * $Id: CmcQosDaoImpl.java,v1.0 2011-12-8 上午11:05:15 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.domain.CmCmcRelation;
import com.topvision.ems.cmc.domain.CmcServiceFlowRelation;
import com.topvision.ems.cmc.domain.ServiceFlowParamSetRelation;
import com.topvision.ems.cmc.domain.ServiceFlowPkgClassRelation;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.qos.dao.CmcQosDao;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceClassInfo;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowCmRelation;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowInfo;
import com.topvision.ems.cmc.qos.domain.CmcQosServiceFlowStats;
import com.topvision.ems.cmc.qos.facade.domain.CmMacToServiceFlow;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosDynamicServiceStats;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosParamSetInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosPktClassInfo;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceClass;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowAttribute;
import com.topvision.ems.cmc.qos.facade.domain.CmcQosServiceFlowStatus;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2011-12-8-上午11:05:15
 * 
 */
@Repository("cmcQosDao")
public class CmcQosDaoImpl extends MyBatisDaoSupport<Entity> implements CmcQosDao {
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getQosDownDynamicServiceStatsInfo
     * (java.lang.Long)
     */
    @Override
    public CmcQosDynamicServiceStats getQosDownDynamicServiceStatsInfo(Long cmcId) {
        // 获取某个CMC上下行方向的动态服务流统计信息
        return (CmcQosDynamicServiceStats) getSqlSession().selectOne(
                getNameSpace() + "getQosDownDynamicServiceStatsInfo", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getQosUpDynamicServiceStatsInfo(java
     * .lang.Long)
     */
    @Override
    public CmcQosDynamicServiceStats getQosUpDynamicServiceStatsInfo(Long cmcId) {
        // 获取某个CMC上上行方向的动态服务流统计信息
        return (CmcQosDynamicServiceStats) getSqlSession().selectOne(
                getNameSpace() + "getQosUpDynamicServiceStatsInfo", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getQosParamSetInfo(java.lang.Long)
     */

    @Override
    public List<CmcQosParamSetInfo> getQosParamSetInfo(Long sId) {
        return getSqlSession().selectList(getNameSpace() + "getQosParamSetInfo", sId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getQosPktClassInfo(java.lang.Long)
     */

    @Override
    public List<CmcQosPktClassInfo> getQosPktClassInfo(Long sId) {
        return getSqlSession().selectList(getNameSpace() + "getQosPktClassInfo", sId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getServiceFlowConnectedCm(java.lang
     * .Long)
     */
    @Override
    public CmAttribute getServiceFlowConnectedCm(Long sId) {
        // 通过sId查询服务流关联CM信息
        return (CmAttribute) getSqlSession().selectOne(getNameSpace() + "getServiceFlowConnectedCm", sId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getServiceFlowConnectedCm(java.lang
     * .Long, java.lang.String)
     */
    @Override
    public CmAttribute getServiceFlowConnectedCm(Long SId, String cmMac) {
        // TODO 通过Sid与Mac查询服务流关联的CM信息
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getCmcQosServiceFlowListInfoWithCondition
     * (java.util.Map, java.lang.Integer, java.lang.Integer)
     */

    @Override
    public List<CmcQosServiceFlowInfo> getCmcQosServiceFlowListInfoWithCondition(Map<String, String> map,
            Integer start, Integer limit) {
        // 根据过滤条件对服务流服务流列表信息进行过滤
        RowBounds r = new RowBounds(start, limit);
        return getSqlSession().selectList(getNameSpace() + "getCmcQosServiceFlowListInfoWithCondition", map, r);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getUpQosServiceFlowStatsInfo(java
     * .util.Map)
     */
    @Override
    public CmcQosServiceFlowStats getUpQosServiceFlowStatsInfo(Map<String, Object> map) {
        // 获取某个CMC上的上行服务流数量统计
        return (CmcQosServiceFlowStats) getSqlSession().selectOne(getNameSpace() + "getUpQosServiceFlowStatsInfo", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#insertServiceFlowAttribute(com.topvision
     * .ems.cmc.facade .domain.CmcQosServiceFlowAttribute)
     */
    @Override
    public Boolean insertServiceFlowAttribute(CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute) {
        // TODO 插入一条服务流属性
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#updateServiceFlowAttribute(com.topvision
     * .ems.cmc.facade .domain.CmcQosServiceFlowAttribute)
     */
    @Override
    public void updateServiceFlowAttribute(CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute) {
        // TODO 更新一条服务流属性
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#inSertOrUpdateServiceFlowAttribute
     * (java.util.List)
     */
    @Override
    public void inSertOrUpdateServiceFlowAttribute(List<CmcQosServiceFlowAttribute> list) {
        // TODO 插入或更新服务流属性
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getCmcQosServiceFlowListNumWithCondition
     * (java.util.Map)
     */
    @Override
    public Integer getCmcQosServiceFlowListNumWithCondition(Map<String, String> map) {
        // 获取通过过滤条件过滤的服务流列表数量
        return (Integer) getSqlSession().selectOne(this.getNameSpace() + "getCmcQosServiceFlowListNumWithCondition",
                map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getDownQosServiceFlowStatsInfo(java
     * .util.Map)
     */
    public CmcQosServiceFlowStats getDownQosServiceFlowStatsInfo(Map<String, Object> map) {
        // 获取某个CMC上的下行服务流数量统计
        return (CmcQosServiceFlowStats) getSqlSession().selectOne(getNameSpace() + "getDownQosServiceFlowStatsInfo",
                map);
    }

    @Override
    public List<CmcQosServiceClassInfo> getQosServiceClassList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getQosServiceClassList", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getQosServiceClassInfo(java.lang.
     * Long)
     */
    @Override
    public CmcQosServiceClass getQosServiceClassInfo(Long scId) {
        return (CmcQosServiceClass) getSqlSession().selectOne(getNameSpace() + "getQosServiceClassInfo", scId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#insertOrUpdateServiceClass(com.topvision
     * .ems.cmc.facade .domain.CmcQosServiceClass)
     */
    @Override
    public void insertOrUpdateServiceClass(CmcQosServiceClass serviceClass) {
        if ((Integer) getSqlSession().selectOne(getNameSpace() + "getServiceClassNum", serviceClass) != 0) {
            getSqlSession().update(getNameSpace() + "updateServiceClass", serviceClass);
        } else {
            getSqlSession().insert(getNameSpace() + "insertServiceClass", serviceClass);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#deleteServiceClass(java.lang.Long)
     */
    @Override
    public void deleteServiceClass(Long scId) {
        getSqlSession().delete(getNameSpace() + "deleteServiceClass", scId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcQosDao#getEntityIdByCmcId(java.lang.Long)
     */
    @Override
    public Long getEntityIdByCmcId(Long cmcId) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getEntityIdByCmcId", cmcId);
    }

    @Override
    public List<Long> getCmcIdByOlt(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcIdByOlt", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#insertServiceFlowCmRelation(com.topvision
     * .ems.cmc.facade .domain.CmcQosServiceFlowCmRelation)
     */
    @Override
    public Boolean insertServiceFlowCmRelation(CmcQosServiceFlowCmRelation cmcQosServiceFlowCmRelation) {
        // TODO 插入一条服务流与CM的关系记录
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#upDateServiceFlowCmRelation(com.topvision
     * .ems.cmc.facade .domain.CmcQosServiceFlowCmRelation)
     */
    @Override
    public void upDateServiceFlowCmRelation(CmcQosServiceFlowCmRelation cmcQosServiceFlowCmRelation) {
        // TODO 更新一条服务流与CM关系记录
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#insertOrUpdateServiceFlowCmRelation(
     * java.util.List)
     */
    @Override
    public void insertOrUpdateServiceFlowCmRelation(List<CmcQosServiceFlowCmRelation> list) {
        // TODO 插入或更新一批服务流与CM的关系记录
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#insertDynamicServiceStatsInfo(com.topvision
     * .ems.cmc.facade .domain.CmcQosDynamicServiceStats)
     */
    @Override
    public Boolean insertDynamicServiceStatsInfo(CmcQosDynamicServiceStats cmcQosDynamicServiceStats) {
        // TODO 插入一条动态服务流统计信息
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#updateDynamicServiceStatsInfo(com.topvision
     * .ems.cmc.facade .domain.CmcQosDynamicServiceStats)
     */
    @Override
    public void updateDynamicServiceStatsInfo(CmcQosDynamicServiceStats cmcQosDynamicServiceStats) {
        // TODO 更新一条动态服务流统计信息
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#insertOrUpdateDynamicServiceStatsInfo
     * (java.util.List)
     */
    @Override
    public void insertOrUpdateDynamicServiceStatsInfo(List<CmcQosDynamicServiceStats> list) {
        // TODO 插入或更新一批动态服务流统计信息
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#insertQosParamSetInfo(com.topvision.
     * ems.cmc.facade.domain .CmcQosParamSetInfo)
     */
    @Override
    public Boolean insertQosParamSetInfo(CmcQosParamSetInfo cmcQosParamSetInfo) {
        // TODO 插入一条服务流参数集信息
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#updateQosParamSetInfo(com.topvision.
     * ems.cmc.facade.domain .CmcQosParamSetInfo)
     */
    @Override
    public void updateQosParamSetInfo(CmcQosParamSetInfo cmcQosParamSetInfo) {
        // TODO 更新一条服务流参数集信息
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#insertOrUpdateQosParamSetInfo(java.util
     * .List)
     */
    @Override
    public void insertOrUpdateQosParamSetInfo(List<CmcQosParamSetInfo> list) {
        // TODO 插入或更新一批服务流参数集信息
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#insertQosPktClassInfo(com.topvision.
     * ems.cmc.facade.domain .CmcQosPktClassInfo)
     */
    @Override
    public Boolean insertQosPktClassInfo(CmcQosPktClassInfo cmcQosPktClassInfo) {
        // TODO 插入一条包分类器信息
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#updateQosPktClassInfo(com.topvision.
     * ems.cmc.facade.domain .CmcQosPktClassInfo)
     */
    @Override
    public void updateQosPktClassInfo(CmcQosPktClassInfo cmcQosPktClassInfo) {
        // TODO 更新一条包分类器信息
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.dao.CmcDao#insertOrUpdateQosPktClassInfo(java.util
     * .List)
     */
    @Override
    public void insertOrUpdateQosPktClassInfo(List<CmcQosPktClassInfo> list) {
        // TODO 插入或更新一批包分类器信息

    }

    @Override
    public CmcAttribute getCmcAttributeByCmcId(Long cmcId) {
        return (CmcAttribute) getSqlSession().selectOne(getNameSpace() + "getCmcAttributeByCmcId", cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#getNameSpace()
     */
    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.qos.domain.CmcQos";
    }

    @Override
    public List<Long> getServiceFlowIdListByCmcId(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getServiceFlowIdListByCmcId", cmcId);
    }

    @Override
    public List<Long> getDBSIdListByCmcId(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "getDBSIdListByCmcId", cmcId);
    }

    @Override
    public void batchInsertCmcQosPktClassInfo(final List<CmcQosPktClassInfo> cmcQosPktClassInfoList, final Long entityId) {

        List<CmcQosPktClassInfo> insertList = new ArrayList<CmcQosPktClassInfo>();
        for (CmcQosPktClassInfo cmcQosPktClassInfo : cmcQosPktClassInfoList) {
            // add by fanzidong,需要对MAC地址进行格式化
            String formattedDestMac = MacUtils.convertToMaohaoFormat(cmcQosPktClassInfo.getClassDestMacAddr());
            cmcQosPktClassInfo.setClassDestMacAddr(formattedDestMac);
            String formattedSourceMac = MacUtils.convertToMaohaoFormat(cmcQosPktClassInfo.getClassSourceMacAddr());
            cmcQosPktClassInfo.setClassSourceMacAddr(formattedSourceMac);
            Map<String, Long> map = new HashMap<String, Long>();
            map.put("cmcIndex", cmcQosPktClassInfo.getCmcIndex());
            map.put("entityId", entityId);
            Long cmcId = getCmcIdByCmcIndexAndEntityId(map);
            cmcQosPktClassInfo.setCmcId(cmcId);
            map.remove("cmcIndex");
            map.remove("entityId");
            map.put("cmcId", cmcId);
            map.put("serviceFlowId", cmcQosPktClassInfo.getServiceFlowId());
            Long sId = getSIdByCmcIdAndServiceFlowId(map);
            if (sId == null) {
                continue;
            }
            cmcQosPktClassInfo.setsId(sId);
            cmcQosPktClassInfo.setCmcId(cmcId);
            cmcQosPktClassInfo.setsId(sId);
            if (((Integer) getSqlSession().selectOne(getNameSpace() + "getCmcQosPktClassInfo", cmcQosPktClassInfo)) != 0) {
                getSqlSession().update(getNameSpace() + "updateCmcQosPktClassInfo", cmcQosPktClassInfo);
            } else {
                insertList.add(cmcQosPktClassInfo);
            }
        }
        SqlSession session = null;
        try {
            session = getBatchSession();
            for (CmcQosPktClassInfo cmcQosPktClassInfo : insertList) {
                ServiceFlowPkgClassRelation serviceFlowPkgClassRelation = new ServiceFlowPkgClassRelation();
                serviceFlowPkgClassRelation.setCmcId(cmcQosPktClassInfo.getCmcId());
                serviceFlowPkgClassRelation.setServiceFlowId(cmcQosPktClassInfo.getServiceFlowId());
                serviceFlowPkgClassRelation.setDocsQosPktClassId(cmcQosPktClassInfo.getClassId());
                serviceFlowPkgClassRelation.setsId(cmcQosPktClassInfo.getsId());

                getSqlSession().insert(getNameSpace() + "insertServiceFlowPkgClassRelation",
                        serviceFlowPkgClassRelation);
                cmcQosPktClassInfo.setServicePacketId(cmcQosPktClassInfo.getServicePacketId());
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        try {
            session = getBatchSession();
            for (CmcQosPktClassInfo cmcQosPktClassInfo : insertList) {
                getSqlSession().insert(getNameSpace() + "insertCmcQosPktClassInfo", cmcQosPktClassInfo);
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
    public void batchInsertCmcQosPktClassInfo8800B(final List<CmcQosPktClassInfo> cmcQosPktClassInfoList,
            final Long cmcId) {
        List<CmcQosPktClassInfo> insertList = new ArrayList<CmcQosPktClassInfo>();
        for (CmcQosPktClassInfo cmcQosPktClassInfo : cmcQosPktClassInfoList) {
            // add by fanzidong,需要对MAC地址进行格式化
            String formattedDestMac = MacUtils.convertToMaohaoFormat(cmcQosPktClassInfo.getClassDestMacAddr());
            cmcQosPktClassInfo.setClassDestMacAddr(formattedDestMac);
            String formattedSourceMac = MacUtils.convertToMaohaoFormat(cmcQosPktClassInfo.getClassSourceMacAddr());
            cmcQosPktClassInfo.setClassSourceMacAddr(formattedSourceMac);
            Map<String, Long> map = new HashMap<String, Long>();
            cmcQosPktClassInfo.setCmcId(cmcId);
            map.put("cmcId", cmcId);
            map.put("serviceFlowId", cmcQosPktClassInfo.getServiceFlowId());
            Long sId = getSIdByCmcIdAndServiceFlowId(map);
            if (sId == null) {
                continue;
            }
            cmcQosPktClassInfo.setsId(sId);
            if (((Integer) getSqlSession().selectOne(getNameSpace() + "getCmcQosPktClassInfo", cmcQosPktClassInfo)) != 0) {
                getSqlSession().update(getNameSpace() + "updateCmcQosPktClassInfo", cmcQosPktClassInfo);
            } else {
                insertList.add(cmcQosPktClassInfo);
            }
        }
        SqlSession session = null;
        try {
            session = getBatchSession();
            for (CmcQosPktClassInfo cmcQosPktClassInfo : insertList) {
                ServiceFlowPkgClassRelation serviceFlowPkgClassRelation = new ServiceFlowPkgClassRelation();
                serviceFlowPkgClassRelation.setCmcId(cmcId);
                serviceFlowPkgClassRelation.setServiceFlowId(cmcQosPktClassInfo.getServiceFlowId());
                serviceFlowPkgClassRelation.setDocsQosPktClassId(cmcQosPktClassInfo.getClassId());
                serviceFlowPkgClassRelation.setsId(cmcQosPktClassInfo.getCmcId());
                getSqlSession().insert(getNameSpace() + "insertServiceFlowPkgClassRelation",
                        serviceFlowPkgClassRelation);
                cmcQosPktClassInfo.setServicePacketId(serviceFlowPkgClassRelation.getServicePacketId());
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        try {
            session = getBatchSession();
            for (CmcQosPktClassInfo cmcQosPktClassInfo : insertList) {
                getSqlSession().insert(getNameSpace() + "insertCmcQosPktClassInfo", cmcQosPktClassInfo);
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
    public void batchInsertOrUpdateCmcQosParamSetInfoOnCC(final List<CmcQosParamSetInfo> cmcQosParamSetInfoList,
            final Long cmcId, final List<Long> dbSidList) {

        List<CmcQosParamSetInfo> insertList = new ArrayList<CmcQosParamSetInfo>();
        List<Long> tempSidList = dbSidList;
        for (CmcQosParamSetInfo cmcQosParamSetInfo : cmcQosParamSetInfoList) {
            Map<String, Long> map = new HashMap<String, Long>();
            cmcQosParamSetInfo.setCmcId(cmcId);
            map.put("cmcId", cmcId);
            map.put("serviceFlowId", cmcQosParamSetInfo.getServiceFlowId());
            if (cmcQosParamSetInfo.getPriority() > 7) {
                continue;
            }
            Long sId = getSIdByCmcIdAndServiceFlowId(map);
            if (sId != null) { // 有服务流
                tempSidList.remove(sId);
                cmcQosParamSetInfo.setsId(sId);
                if (((Long) getSqlSession().selectOne(getNameSpace() + "getCmcQosParamSetInfo", cmcQosParamSetInfo)) != 0) {
                    getSqlSession().update(getNameSpace() + "updateCmcQosParamSetInfo", cmcQosParamSetInfo);
                } else {
                    insertList.add(cmcQosParamSetInfo);
                }
            }
        }
        SqlSession session = null;
        try {
            session = getBatchSession();
            for (CmcQosParamSetInfo cmcQosParamSetInfo : insertList) {
                ServiceFlowParamSetRelation serviceFlowParamSetRelation = new ServiceFlowParamSetRelation();
                serviceFlowParamSetRelation.setCmcId(cmcId);
                serviceFlowParamSetRelation.setServiceFlowId(cmcQosParamSetInfo.getServiceFlowId());
                serviceFlowParamSetRelation.setDocsQosParamSetType(cmcQosParamSetInfo.getType());
                serviceFlowParamSetRelation.setsId(cmcQosParamSetInfo.getsId());
                getSqlSession().insert(getNameSpace() + "insertServiceFlowParamSetRelation",
                        serviceFlowParamSetRelation);
                cmcQosParamSetInfo.setServiceParamId(serviceFlowParamSetRelation.getServiceParamId());
            }
            if (tempSidList.size() > 0) {
                for (Long tmpSid : tempSidList) {
                    session.delete(getNameSpace() + "deleteServiceFlowParmsetRelationBySid", tmpSid);
                }
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        try {
            session = getBatchSession();
            for (CmcQosParamSetInfo cmcQosParamSetInfo : insertList) {
                session.insert(getNameSpace() + "insertCmcQosParamSetInfo", cmcQosParamSetInfo);
            }
            if (tempSidList.size() > 0) {
                for (Long tmpSid : tempSidList) {
                    session.delete(getNameSpace() + "deleteServiceFlowParmsetRelationBySid", tmpSid);
                }
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
    public void batchInsertOrUpdateQosServiceFlowAttrOnCC(
            final List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributeList, final Long cmcId,
            final List<Long> dbSidList) {
        SqlSession session = null;
        List<Long> templist = dbSidList;
        List<CmcQosServiceFlowAttribute> insertList = new ArrayList<CmcQosServiceFlowAttribute>();
        try {
            session = getBatchSession();
            for (CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute : cmcQosServiceFlowAttributeList) {
                cmcQosServiceFlowAttribute.setCmcId(cmcId);
                Long serviceFlowId = cmcQosServiceFlowAttribute.getDocsQosServiceFlowId();

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cmcId", cmcId);
                map.put("serviceFlowId", serviceFlowId);
                Long sid = (Long) getSqlSession().selectOne(getNameSpace() + "getSIdByCmcIdAndServiceFlowId", map);
                if (sid != null) {
                    cmcQosServiceFlowAttribute.setsId(sid);
                    session.update(getNameSpace() + "updateCmcQosServiceFlowAttribute", cmcQosServiceFlowAttribute);
                    templist.remove(sid);
                } else {
                    insertList.add(cmcQosServiceFlowAttribute);
                }
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        try {
            session = getBatchSession();
            for (CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute : insertList) {
                CmcServiceFlowRelation cmcServiceFlowRelation = new CmcServiceFlowRelation();
                Long serviceFlowId = cmcQosServiceFlowAttribute.getDocsQosServiceFlowId();

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cmcId", cmcId);
                map.put("serviceFlowId", serviceFlowId);
                Long sid = (Long) getSqlSession().selectOne(getNameSpace() + "getSIdByCmcIdAndServiceFlowId", map);
                cmcServiceFlowRelation.setServiceFlowId(serviceFlowId);
                cmcServiceFlowRelation.setCmcId(cmcId);
                getSqlSession().insert(getNameSpace() + "insertCmcServiceFlowRelation", cmcServiceFlowRelation);
                sid = cmcServiceFlowRelation.getsId();
                cmcQosServiceFlowAttribute.setsId(sid);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }

        try {
            session = getBatchSession();
            for (CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute : insertList) {
                session.insert(getNameSpace() + "insertCmcQosServiceFlowAttribute", cmcQosServiceFlowAttribute);
            }
            if (templist.size() > 0) {
                for (Long tmpSid : templist) {
                    session.delete(getNameSpace() + "deleteCmcQosServiceFlowRelationBySid", tmpSid);
                }
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
    public void batchInsertCmcQosServiceFlowAttribute(
            final List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributeList, final Long entityId) {
        SqlSession session = null;
        List<Long> cmcIdList = getCmcIdByOlt(entityId);
        try {
            session = getBatchSession();
            for (Long cmcIdTemp : cmcIdList) {
                deleteCmcQosServiceFlowRelation(cmcIdTemp);
            }
            for (CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute : cmcQosServiceFlowAttributeList) {
                Long cmcId;
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcIndex", cmcQosServiceFlowAttribute.getCmcIndex());
                map.put("entityId", entityId);
                cmcId = getCmcIdByCmcIndexAndEntityId(map);
                cmcQosServiceFlowAttribute.setCmcId(cmcId);
                CmcServiceFlowRelation cmcServiceFlowRelation = new CmcServiceFlowRelation();
                cmcServiceFlowRelation.setServiceFlowId(cmcQosServiceFlowAttribute.getDocsQosServiceFlowId());
                cmcServiceFlowRelation.setCmcId(cmcId);
                getSqlSession().insert(getNameSpace() + "insertCmcServiceFlowRelation", cmcServiceFlowRelation);
                cmcQosServiceFlowAttribute.setsId(cmcServiceFlowRelation.getsId());
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        try {
            session = getBatchSession();
            for (CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute : cmcQosServiceFlowAttributeList) {
                session.insert(getNameSpace() + "insertCmcQosServiceFlowAttribute", cmcQosServiceFlowAttribute);
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
    public void batchInsertCmcQosServiceFlowAttribute8800B(
            final List<CmcQosServiceFlowAttribute> cmcQosServiceFlowAttributeList, final Long cmcId) {
        SqlSession session = null;
        try {
            session = getBatchSession();
            deleteCmcQosServiceFlowRelation(cmcId);
            for (CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute : cmcQosServiceFlowAttributeList) {
                if (cmcQosServiceFlowAttribute.getDocsQosServiceFlowId() == null) {
                    continue;
                }
                cmcQosServiceFlowAttribute.setCmcId(cmcId);
                CmcServiceFlowRelation cmcServiceFlowRelation = new CmcServiceFlowRelation();
                cmcServiceFlowRelation.setServiceFlowId(cmcQosServiceFlowAttribute.getDocsQosServiceFlowId());
                cmcServiceFlowRelation.setCmcId(cmcId);
                getSqlSession().insert(getNameSpace() + "insertCmcServiceFlowRelation", cmcServiceFlowRelation);
                cmcQosServiceFlowAttribute.setsId(cmcServiceFlowRelation.getsId());
                session.insert(getNameSpace() + "insertCmcQosServiceFlowAttribute", cmcQosServiceFlowAttribute);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        try {
            session = getBatchSession();
            for (CmcQosServiceFlowAttribute cmcQosServiceFlowAttribute : cmcQosServiceFlowAttributeList) {
                if (cmcQosServiceFlowAttribute.getDocsQosServiceFlowId() == null) {
                    continue;
                }
                session.insert(getNameSpace() + "insertCmcQosServiceFlowAttribute", cmcQosServiceFlowAttribute);
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
    public void batchInsertCmcQosServiceFlowStatus(final List<CmcQosServiceFlowStatus> cmcQosServiceFlowStatusList,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        try {
            for (CmcQosServiceFlowStatus cmcQosServiceFlowStatus : cmcQosServiceFlowStatusList) {
                Map<String, Long> map = new HashMap<String, Long>();
                cmcQosServiceFlowStatus.setCmcId(cmcId);
                map.put("cmcId", cmcId);
                map.put("serviceFlowId", cmcQosServiceFlowStatus.getFlowId());
                cmcQosServiceFlowStatus.setsId(getSIdByCmcIdAndServiceFlowId(map));
                if (((Integer) getSqlSession().selectOne(getNameSpace() + "getCmcQosServiceFlowStatus",
                        cmcQosServiceFlowStatus)) != 0) {
                    getSqlSession().update(getNameSpace() + "updateCmcQosServiceFlowStatus", cmcQosServiceFlowStatus);
                } else {
                    session.insert(getNameSpace() + "insertCmcQosServiceFlowStatus", cmcQosServiceFlowStatus);
                }
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
    public void batchInsertCmMacToServiceFlow(final List<CmMacToServiceFlow> cmMacToServiceFlows, final Long entityId) {
        SqlSession session = getBatchSession();
        try {
            for (CmMacToServiceFlow cmMacToServiceFlow : cmMacToServiceFlows) {
                Map<String, Long> sIdMap = new HashMap<String, Long>();
                Map<String, Long> cmcIdMap = new HashMap<String, Long>();
                cmcIdMap.put("entityId", entityId);
                cmcIdMap.put("cmcIndex", cmMacToServiceFlow.getDocsQosCmtsIfIndex());
                cmMacToServiceFlow.setCmcId(getCmcIdByCmcIndexAndEntityId(cmcIdMap));
                sIdMap.put("cmcId", cmMacToServiceFlow.getCmcId());
                sIdMap.put("serviceFlowId", cmMacToServiceFlow.getDocsQosCmtsServiceFlowId());
                Long sid = getSIdByCmcIdAndServiceFlowId(sIdMap);
                if (sid != null) {
                    cmMacToServiceFlow.setsId(sid);
                } else {
                    continue;
                }
                Long mac = new MacUtils(cmMacToServiceFlow.getDocsQosCmtsCmMac()).longValue();
                Long cmId = getCmIdByMac(entityId, mac);
                cmMacToServiceFlow.setCmId(cmId);
                // add by fanzidong, 需要在入库之前对MAC地址进行格式化
                String formattedMac = MacUtils.convertToMaohaoFormat(cmMacToServiceFlow.getDocsQosCmtsCmMac());
                cmMacToServiceFlow.setDocsQosCmtsCmMac(formattedMac);
                if (((Long) getSqlSession().selectOne(getNameSpace() + "getCmMacToServiceFlow", cmMacToServiceFlow)) != 0) {
                    getSqlSession().update(getNameSpace() + "updateCmMacToServiceFlow", cmMacToServiceFlow);
                } else {
                    session.insert(getNameSpace() + "insertCmMacToServiceFlow", cmMacToServiceFlow);
                }
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
    public void deleteCmcQosServiceFlowRelation(Long cmcId) {
        getSqlSession().delete(getNameSpace() + "deleteCmcQosServiceFlowRelation", cmcId);

    }

    @Override
    public void batchInsertCmcQosParamSetInfo8800B(final List<CmcQosParamSetInfo> cmcQosParamSetInfoList,
            final Long cmcId) {
        SqlSession session = getBatchSession();
        List<CmcQosParamSetInfo> insertList = new ArrayList<CmcQosParamSetInfo>();
        try {

            for (CmcQosParamSetInfo cmcQosParamSetInfo : cmcQosParamSetInfoList) {
                cmcQosParamSetInfo.setCmcId(cmcId);
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("cmcId", cmcId);
                map.put("serviceFlowId", cmcQosParamSetInfo.getServiceFlowId());
                Long sId = getSIdByCmcIdAndServiceFlowId(map);
                if (sId == null) {
                    continue;
                }
                cmcQosParamSetInfo.setsId(sId);
                if (((Long) getSqlSession().selectOne(getNameSpace() + "getCmcQosParamSetInfo", cmcQosParamSetInfo)) != 0) {
                    getSqlSession().update(getNameSpace() + "updateCmcQosParamSetInfo", cmcQosParamSetInfo);
                } else {
                    ServiceFlowParamSetRelation serviceFlowParamSetRelation = new ServiceFlowParamSetRelation();
                    serviceFlowParamSetRelation.setCmcId(cmcId);
                    serviceFlowParamSetRelation.setServiceFlowId(cmcQosParamSetInfo.getServiceFlowId());
                    serviceFlowParamSetRelation.setDocsQosParamSetType(cmcQosParamSetInfo.getType());
                    serviceFlowParamSetRelation.setsId(sId);

                    getSqlSession().insert(getNameSpace() + "insertServiceFlowParamSetRelation",
                            serviceFlowParamSetRelation);
                    cmcQosParamSetInfo.setServiceParamId(-1L);
                    insertList.add(cmcQosParamSetInfo);
                }
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        try {

            for (CmcQosParamSetInfo cmcQosParamSetInfo : insertList) {
                session.insert(getNameSpace() + "insertCmcQosParamSetInfo", cmcQosParamSetInfo);
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
    public void batchInsertCC8800BCmMacToServiceFlow(final List<CmMacToServiceFlow> cmMacToServiceFlows,
            final Long cmcId, final Long entityId) {
        SqlSession session = getBatchSession();
        try {
            for (CmMacToServiceFlow cmMacToServiceFlow : cmMacToServiceFlows) {
                Map<String, Long> sIdMap = new HashMap<String, Long>();
                cmMacToServiceFlow.setCmcId(cmcId);
                sIdMap.put("cmcId", cmMacToServiceFlow.getCmcId());
                sIdMap.put("serviceFlowId", cmMacToServiceFlow.getDocsQosCmtsServiceFlowId());
                cmMacToServiceFlow.setsId(getSIdByCmcIdAndServiceFlowId(sIdMap));
                Long mac = new MacUtils(cmMacToServiceFlow.getDocsQosCmtsCmMac()).longValue();
                Long cmId = getCmIdByMac(entityId, mac);
                cmMacToServiceFlow.setCmId(cmId);
                // add by fanzidong, 需要在入库之前对MAC地址进行格式化
                String formattedMac = MacUtils.convertToMaohaoFormat(cmMacToServiceFlow.getDocsQosCmtsCmMac());
                cmMacToServiceFlow.setDocsQosCmtsCmMac(formattedMac);
                if (((Long) getSqlSession().selectOne(getNameSpace() + "getCmMacToServiceFlow", cmMacToServiceFlow)) != 0) {
                    getSqlSession().update(getNameSpace() + "updateCmMacToServiceFlow", cmMacToServiceFlow);
                } else {
                    session.insert(getNameSpace() + "insertCmMacToServiceFlow", cmMacToServiceFlow);
                }
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
    public void batchInsertCmcQosParamSetInfo(final List<CmcQosParamSetInfo> cmcQosParamSetInfoList, final Long entityId) {
        List<CmcQosParamSetInfo> insertList = new ArrayList<CmcQosParamSetInfo>();
        for (CmcQosParamSetInfo cmcQosParamSetInfo : cmcQosParamSetInfoList) {
            Map<String, Long> map = new HashMap<String, Long>();
            map.put("cmcIndex", cmcQosParamSetInfo.getCmcIndex());
            map.put("entityId", entityId);
            Long cmcId = getCmcIdByCmcIndexAndEntityId(map);
            cmcQosParamSetInfo.setCmcId(cmcId);
            map.remove("cmcIndex");
            map.remove("entityId");
            cmcQosParamSetInfo.setCmcId(cmcId);
            map.put("cmcId", cmcId);
            map.put("serviceFlowId", cmcQosParamSetInfo.getServiceFlowId());
            Long sId = getSIdByCmcIdAndServiceFlowId(map);
            if (sId == null) {
                continue;
            }
            cmcQosParamSetInfo.setsId(sId);
            if (((Long) getSqlSession().selectOne(getNameSpace() + "getCmcQosParamSetInfo", cmcQosParamSetInfo)) != 0) {
                getSqlSession().update(getNameSpace() + "updateCmcQosParamSetInfo", cmcQosParamSetInfo);
            } else {
                insertList.add(cmcQosParamSetInfo);
            }
        }
        SqlSession session = null;
        try {
            session = getBatchSession();
            for (CmcQosParamSetInfo cmcQosParamSetInfo : insertList) {
                ServiceFlowParamSetRelation serviceFlowParamSetRelation = new ServiceFlowParamSetRelation();
                serviceFlowParamSetRelation.setCmcId(cmcQosParamSetInfo.getCmcId());
                serviceFlowParamSetRelation.setServiceFlowId(cmcQosParamSetInfo.getServiceFlowId());
                serviceFlowParamSetRelation.setDocsQosParamSetType(cmcQosParamSetInfo.getType());
                serviceFlowParamSetRelation.setsId(cmcQosParamSetInfo.getsId());
                getSqlSession().insert(getNameSpace() + "insertServiceFlowParamSetRelation",
                        serviceFlowParamSetRelation);
                cmcQosParamSetInfo.setServiceParamId(serviceFlowParamSetRelation.getServiceParamId());
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
        try {
            session = getBatchSession();
            for (CmcQosParamSetInfo cmcQosParamSetInfo : insertList) {
                session.insert(getNameSpace() + "insertCmcQosParamSetInfo", cmcQosParamSetInfo);
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
    public Long getSIdByCmcIdAndServiceFlowId(Map<String,Long> map) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getSIdByCmcIdAndServiceFlowId", map);
    }
    
    @Override
    public Long getCmcIdByCmcIndexAndEntityId(Map<String, Long> map) {
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmcIdByCmcIndexAndEntityId", map);
    }
    
    @Override
    public Long getCmIdByMac(Long entityId, Long mac) {
        CmCmcRelation cmCmcRelation = new CmCmcRelation();
        cmCmcRelation.setEntityId(entityId);
        cmCmcRelation.setMaclong(mac);
        return (Long) getSqlSession().selectOne(getNameSpace() + "getCmIdByCmMac", cmCmcRelation);
    }
    
    @Override
    public void deleteServiceFlowParmsetRelationByCmcId(Long cmcId) {
        getSqlSession().delete(getNameSpace() + "deleteServiceFlowParmsetRelationByCmcId", cmcId);
    }
}
