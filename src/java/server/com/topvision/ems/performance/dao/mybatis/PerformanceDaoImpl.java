/***********************************************************************
 * $ PerfmormanceDaoImpl.java,v1.0 2012-5-3 9:36:14 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.dao.mybatis;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.performance.dao.PerformanceDao;
import com.topvision.ems.performance.domain.ExecutorThreadSnap;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author jay
 * @created @2012-5-3-9:36:14
 */
@Repository("performanceDao")
public class PerformanceDaoImpl extends MyBatisDaoSupport<ScheduleMessage<OperClass>> implements PerformanceDao {
    @Override
    public void updateScheduleDomain(ScheduleMessage<OperClass> scheduleMessage) {
        if (scheduleMessage.getAction() == ScheduleMessage.STOP) {
            getSqlSession().delete(getNameSpace() + "deleteByPrimaryKey", scheduleMessage.getMonitorId());
        } else {
            getSqlSession().update(getNameSpace() + "updateScheduleDomain", scheduleMessage);
        }
    }

    @Override
    public ScheduleMessage<OperClass> selectByIdentifyAndCategory(Long identify, String category) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("identifyKey", identify);
        map.put("category", category);
        return getSqlSession().selectOne(getNameSpace("selectByIdentifyAndCategory"), map);
    }

    @Override
    public void updateScheduleModifyTime(Long identify, String category) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("identifyKey", identify);
        map.put("category", category);
        getSqlSession().update(getNameSpace() + "updateScheduleModifyTime", map);
    }

    public void updateScheduleCollectTime(long monitorId, long lastCollectTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("monitorId", monitorId);
        map.put("lastCollectTime", new Timestamp(lastCollectTime));
        getSqlSession().update(getNameSpace() + "updateScheduleCollectTime", map);
    }

    public void updateSchedulePeriod(Long period, String category) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("period", period);
        map.put("category", category);
        getSqlSession().update(getNameSpace() + "updateSchedulePeriod", map);
    }

    @Override
    public ScheduleMessage<OperClass> selectByIdentifyAndCategoryAndPeriod(Long identify, String category,
            Integer period) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("identifyKey", identify);
        map.put("category", category);
        map.put("period", period);
        return getSqlSession().selectOne(getNameSpace("selectByIdentifyAndCategoryAndPeriod"), map);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.performance.domain.Performance";
    }

    @Override
    public void updateScheduleTaskRunTime(Long monitorId, Timestamp scheduleRunTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("monitorId", monitorId);
        map.put("scheduleRunTime", scheduleRunTime);
        getSqlSession().update(getNameSpace() + "updateScheduleTaskRunTime", map);
    }

    @Override
    public List<Integer> getRedundancyPerfMonitor() {
        return getSqlSession().selectList(getNameSpace() + "getRedundancyPerfMonitor");
    }

    @Override
    public void cleanRedundancyPerfMonitor() {
        getSqlSession().delete(getNameSpace() + "cleanRedundancyPerfMonitor");
    }

    @Override
    public Map<String, Map<String, String>> getCategoryCounts() {
        return getSqlSession().selectMap(getNameSpace("getCategoryCounts"), "category");
    }

    @Override
    public List<ScheduleMessage<OperClass>> queryScheduleByEntityId(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("queryScheduleByEntityId"), entityId);
    }

    @Override
    public List<ScheduleMessage<OperClass>> queryScheduleByIdList(Map<String, Object> paramsMap) {
        return this.getSqlSession().selectList(getNameSpace("queryScheduleByIdList"), paramsMap);
    }

    @Override
    public List<ScheduleMessage<OperClass>> queryScheduleByEntityType(Map<String, Object> paramsMap) {
        return this.getSqlSession().selectList(getNameSpace("queryScheduleByEntityType"), paramsMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.performance.dao.PerformanceDao#getScheduleMessageForAdminAction(java.lang.
     * Long)
     */
    @Override
    public ScheduleMessage<OperClass> getScheduleMessageForAdminAction(Long monitorId) {
        return this.getSqlSession().selectOne(getNameSpace("getScheduleMessageForAdminAction"), monitorId);
    }

    @Override
    public List<ExecutorThreadSnap> queryEngineThreadStatistic(Map<String, Object> queryMap) {
        return this.getSqlSession().selectList(getNameSpace("queryEngineThreadStatistic"), queryMap);
    }

    @Override
    public List<ScheduleMessage<?>> loadDelayedPerfMonitors(Integer periodCount) {
        return this.getSqlSession().selectList(getNameSpace("loadDelayedPerfMonitors"), periodCount);
    }

}
