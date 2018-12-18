/***********************************************************************
 * $ OltPerfDaoImpl.java,v1.0 2011-11-20 16:57:29 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.engine.mybatis;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.engine.dao.EngineDaoSupport;
import com.topvision.ems.epon.domain.EponStatsRecord;
import com.topvision.ems.epon.domain.HistoryPerforamnceIndex;
import com.topvision.ems.epon.domain.MonitorBaseInfo;
import com.topvision.ems.epon.domain.MonitorIndex;
import com.topvision.ems.epon.domain.MonitorPerforamnceIndex;
import com.topvision.ems.epon.domain.MonitorPort;
import com.topvision.ems.epon.domain.OltPerfThreshold;
import com.topvision.ems.epon.domain.OltSimplePort;
import com.topvision.ems.epon.domain.PerfFolderDevice;
import com.topvision.ems.epon.domain.PerfRecord;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.performance.domain.EponLinkQualityData;
import com.topvision.ems.epon.performance.domain.EponServiceQualityPertResult;
import com.topvision.ems.epon.performance.domain.PerfCurStatsTable;
import com.topvision.ems.epon.performance.domain.PerfStatCycle;
import com.topvision.ems.epon.performance.domain.PerfStats15Table;
import com.topvision.ems.epon.performance.domain.PerfStats24Table;
import com.topvision.ems.epon.performance.domain.PerfStatsGlobalSet;
import com.topvision.ems.epon.performance.engine.OltPerfDao;
import com.topvision.ems.epon.performance.facade.OltFanPerf;
import com.topvision.ems.epon.performance.facade.OltFlowQuality;
import com.topvision.ems.epon.performance.facade.OltServiceQualityPerf;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author jay
 * @created @2011-11-20-16:57:29
 * 
 * @Mybatis Modify by Rod @2013-10-25
 */

public class OltPerfDaoImpl extends EngineDaoSupport<OltPerfThreshold> implements OltPerfDao {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 通过指定端口加上指标列表获得15分钟的指标值趋势
     * 
     * @param params
     *            指定端口
     * @return List<PerfTrendAttribute>
     */
    @Override
    public List<PerfStats15Table> getPerf15FromDB(Map<String, Object> params) {
        return getSqlSession().selectList(getNameSpace() + "getPerf15FromDB", params);
    }

    /**
     * 删除指定端口15分钟历史性能数据
     * 
     * @param perfStats15Table
     *            指定端口
     */
    @Override
    public void deletePerf15FromDB(PerfStats15Table perfStats15Table) {
        getSqlSession().delete(getNameSpace() + "deletePerf15FromDB", perfStats15Table);
    }

    /**
     * 通过指定端口加上指标列表获得24小时的指标值趋势
     * 
     * @param params
     *            指定端口
     * @return List<PerfTrendAttribute>
     */
    @Override
    public List<PerfStats24Table> getPerf24FromDB(Map<String, Object> params) {
        return getSqlSession().selectList(getNameSpace() + "getPerf24FromDB", params);
    }

    /**
     * 删除DB中24小时性能数据
     * 
     * @param perfStats24Table
     *            指定端口
     */
    @Override
    public void deletePerf24FromDB(PerfStats24Table perfStats24Table) {
        getSqlSession().delete(getNameSpace() + "deletePerf24FromDB", perfStats24Table);
    }

    /**
     * 获得轮询周期设置参数
     * 
     * @param entityId
     *            所属设备ID
     * @return PerfStatCycle
     */
    @Override
    public PerfStatCycle getPerfStatCycle(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "getPerfStatCycle", entityId);
    }

    /**
     * 获取记录数配置参数
     * 
     * @param entityId
     *            所属设备ID
     * @return PerfStatsGlobalSet
     */
    @Override
    public PerfStatsGlobalSet getPerfStatsGlobalSet(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "getPerfStatsGlobalSet", entityId);
    }

    /**
     * 通过阈值类型获取阈值设置
     * 
     * @param entityId
     *            所属设备ID
     * @param thresholdType
     *            阈值作用对象类型 @return List<PerfThreshold> 该类型包含的所有阈值
     */
    @Override
    public List<OltPerfThreshold> getPerfThresholdByType(Long entityId, String thresholdType) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", entityId.toString());
        map.put("thresholdType", thresholdType);
        return getSqlSession().selectList(getNameSpace() + "getPerfThresholdByType", map);
    }

    /**
     * 修改阈值
     * 
     * @param perfThreshold
     *            修改值 @throws ModifyPerfThresholdException 修改阈值失败抛出该异常
     */
    @Override
    public void modifyPerfThreshold(OltPerfThreshold perfThreshold) {
        getSqlSession().update(getNameSpace() + "updatePerfThreshold", perfThreshold);
    }

    /**
     * 保存轮询周期设置
     * 
     * @param perfStatCycle
     *            perfStatCycle
     * @throws com.topvision.ems.epon.exception.SavePerfStatCycleException
     */
    @Override
    public synchronized void savePerfStatCycle(PerfStatCycle perfStatCycle) {
        getSqlSession().delete(getNameSpace() + "deletePerfStatCycle", perfStatCycle.getEntityId());
        getSqlSession().insert(getNameSpace() + "insertPerfStatCycle", perfStatCycle);
    }

    /**
     * 保存记录数配置参数
     * 
     * @param perfStatsGlobalSet
     *            perfStatsGlobalSet
     * @throws com.topvision.ems.epon.exception.SavePerfStatsGlobalSetException
     *             保存记录数失败抛出该异常
     */
    @Override
    public synchronized void savePerfStatsGlobalSet(PerfStatsGlobalSet perfStatsGlobalSet) {
        getSqlSession().delete(getNameSpace() + "deletePerfStatsGlobalSet", perfStatsGlobalSet.getEntityId());
        getSqlSession().insert(getNameSpace() + "insertPerfStatsGlobalSet", perfStatsGlobalSet);
    }

    /**
     * 通过指定端口加上指标列表获得15分钟的指标值
     * 
     * @param params
     *            params
     * @return List<PerfStats15Table> 获得15分钟性能值
     */
    @Override
    public List<PerfStats15Table> get15HistoryPerformance(Map<String, Object> params) {
        return getSqlSession().selectList(getNameSpace() + "getHistoryPerformance", params);
    }

    /**
     * 获取数据库中15分钟历史性能记录最后一次的采集时间
     * 
     * @param oltSimplePort
     *            oltSimplePort
     * @return Long
     */
    @Override
    public Long getLast15RecordTime(OltSimplePort oltSimplePort) {
        Object o = getSqlSession().selectOne(getNameSpace() + "getLast15RecordTime", oltSimplePort);
        if (o != null) {
            return ((Timestamp) o).getTime();
        } else {
            return System.currentTimeMillis() - 24 * 60 * 60 * 1000L;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.OltPerfDao#getLast15RecordTimeString(com.topvision.ems.epon.domain
     * .OltSimplePort)
     */
    @Override
    public String getLast15RecordTimeString(Long entityId, Long portIndex) {
        OltSimplePort oltSimplePort = new OltSimplePort();
        oltSimplePort.setEntityId(entityId);
        oltSimplePort.setPortIndex(portIndex);
        Timestamp timestamp = getSqlSession().selectOne(getNameSpace() + "getLast15RecordTime", oltSimplePort);
        if (timestamp != null) {
            return String.valueOf(timestamp.getTime());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.OltPerfDao#getLast24RecordTimeString(com.topvision.ems.epon.domain
     * .OltSimplePort)
     */
    @Override
    public String getLast24RecordTimeString(Long entityId, Long portIndex) {
        OltSimplePort oltSimplePort = new OltSimplePort();
        oltSimplePort.setEntityId(entityId);
        oltSimplePort.setPortIndex(portIndex);
        Timestamp timestamp = getSqlSession().selectOne(getNameSpace() + "getLast15RecordTime", oltSimplePort);
        if (timestamp != null) {
            return String.valueOf(timestamp.getTime());
        } else {
            return null;
        }
    }

    /**
     * 通过指定端口加上指标列表获得24小时的指标值
     * 
     * @param params
     *            params
     * @return List<PerfStats24Table> 获得24小时性能值
     */
    @Override
    public List<PerfStats24Table> get24HistoryPerformance(Map<String, Object> params) {
        return getSqlSession().selectList(getNameSpace() + "get24HistoryPerformance", params);
    }

    /**
     * 获取数据库中24小时历史性能记录最后一次的采集时间
     * 
     * @param oltSimplePort
     *            oltSimplePort
     * @return Long
     */
    @Override
    public Long getLast24RecordTime(OltSimplePort oltSimplePort) {
        Object o = getSqlSession().selectOne(getNameSpace() + "getLast24RecordTime", oltSimplePort);
        if (o != null) {
            return ((Timestamp) o).getTime();
        } else {
            return System.currentTimeMillis() - 24 * 60 * 60 * 1000L;
        }
    }

    /**
     * 保存阈值配置
     * 
     * @param oltPerfThresholds
     *            oltPerfThresholds
     */
    @Override
    public void saveOltPerfThresholds(Long entityId, final List<OltPerfThreshold> oltPerfThresholds) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteOltPerfThresholds", entityId);
            for (OltPerfThreshold oltPerfThreshold : oltPerfThresholds) {
                sqlSession.insert(getNameSpace() + "insertOltPerfThresholds", oltPerfThreshold);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存刷新到的15分钟历史性能数据
     * 
     * @param newPerfStats15Table
     *            newPerfStats15Table
     */
    @Override
    public void savePerfStats15Table(final List<PerfStats15Table> newPerfStats15Table) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (PerfStats15Table perfStats15Table : newPerfStats15Table) {
                sqlSession.insert(getNameSpace() + "insertPerfStats15Table", perfStats15Table);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存刷新到的24小时历史性能数据
     * 
     * @param newPerfStats24Table
     *            newPerfStats24Table
     */
    @Override
    public void savePerfStats24Table(final List<PerfStats24Table> newPerfStats24Table) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (PerfStats24Table perfStats24Table : newPerfStats24Table) {
                sqlSession.insert(getNameSpace() + "insertPerfStats24Table", perfStats24Table);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 获取流量排名前10的Pon口列表
     * 
     * @return List<PerfRecord>
     */
    @Override
    public List<PerfRecord> getTopPonLoading(Map<String, Object> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getTopPonLoading", params);
    }

    /**
     * 获取Pon速率排名记录数
     * 
     * @return
     */
    @Override
    public Integer getPonSpeedCount(Map<String, Object> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace() + "getPonSpeedCount", params);
    }

    /**
     * 获取流量排名前10的Sni口列表
     * 
     * @return List<PerfRecord>
     */
    @Override
    public List<PerfRecord> getTopSniLoading(Map<String, Object> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getTopSniLoading", params);
    }

    /**
     * 获取SNI速率排名记录数
     * 
     * @return
     */
    @Override
    public Integer getSniSpeedCount(Map<String, Object> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace() + "getSniSpeedCount", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.OltPerfDao#insertEponCurrentStats(com.topvision.ems.epon.facade
     * .domain.PerfCurStatsTable)
     */
    @Override
    public void insertEponCurrentStats(final List<PerfCurStatsTable> perfCurStatsTables) {
        Map<String, Long> map = new HashMap<String, Long>();
        if (perfCurStatsTables.size() > 0) {
            map.put("entityId", perfCurStatsTables.get(0).getEntityId());
            map.put("portIndex", perfCurStatsTables.get(0).getPortIndex());
            Long entityId = perfCurStatsTables.get(0).getEntityId();
            // 此处暂时做成只保存有数据的，如果需要包括上个时间点的数据，可加入portIndex匹配删除
            getSqlSession().delete(getNameSpace() + "deleteEponCurrentStats", entityId);
            // Modify by Rod
            for (PerfCurStatsTable perfCurStatsTable : perfCurStatsTables) {
                try {
                    getSqlSession().insert(getNameSpace() + "insertEponCurrentStats", perfCurStatsTable);
                } catch (Exception e) {
                    logger.debug("", e);
                }
            }
            /*
             * getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
             * 
             * @Override public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException
             * { executor.startBatch(); for (PerfCurStatsTable perfCurStatsTable :
             * perfCurStatsTables) { executor.insert(getNameSpace() + "insertEponCurrentStats",
             * perfCurStatsTable); } executor.executeBatch(); return null; } });
             */
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltPerfDao#getEponPerformanceMonitorId(java.lang.Long)
     */
    @Override
    public List<Integer> getEponPerformanceMonitorId(Long entityId, String category) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("category", category);
        return getSqlSession().selectList(getNameSpace() + "getEponPerformanceMonitorId", map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltPerfDao#insertEpon15MinuteStats(java.util.List)
     */
    @Override
    public void insertEpon15MinuteStats(final List<PerfStats15Table> perfStats15Tables) {
        /*
         * getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
         * 
         * @Override public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
         * executor.startBatch(); for (PerfStats15Table perfStats15Table : perfStats15Tables) {
         * executor.insert(getNameSpace() + "insertPerfStats15Table", perfStats15Table); }
         * executor.executeBatch(); return null; } });
         */
        // Modify by Rod
        for (PerfStats15Table perfStats15Table : perfStats15Tables) {
            try {
                getSqlSession().insert(getNameSpace() + "insertPerfStats15Table", perfStats15Table);
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltPerfDao#insertEpon15MinuteStats(java.util.List)
     */
    @Override
    public void insertEpon15MinuteStats(PerfStats15Table perfStats15Table) {
        getSqlSession().insert(getNameSpace() + "insertPerfStats15Table", perfStats15Table);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltPerfDao#insertEpon24HourStats(java.util.List)
     */
    @Override
    public void insertEpon24HourStats(final List<PerfStats24Table> perfStats24Tables) {
        /*
         * getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
         * 
         * @Override public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
         * executor.startBatch(); for (PerfStats24Table perfStats24Table : perfStats24Tables) {
         * executor.insert(getNameSpace() + "insertPerfStats24Table", perfStats24Table); }
         * executor.executeBatch(); return null; } });
         */
        // Modify by Rod
        for (PerfStats24Table perfStats24Table : perfStats24Tables) {
            try {
                getSqlSession().insert(getNameSpace() + "insertPerfStats24Table", perfStats24Table);
            } catch (Exception e) {
                logger.debug("", e);
            }
        }
    }

    /**
     * 获取OLT设备列表
     */
    @Override
    public List<PerfFolderDevice> getAllOltList(Long type) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("type", type);
        return getSqlSession().selectList(getNameSpace() + "getAllOltList", authorityHashMap);
    }

    /**
     * 获取OLT设备列表
     */
    @Override
    public List<PerfFolderDevice> getAllOltDeviceList(Long type) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("type", type);
        return getSqlSession().selectList(getNameSpace() + "getAllOltDeviceList", authorityHashMap);
    }

    /**
     * 获取地域列表
     */
    @Override
    public List<TopoFolder> getAllFolderList() {
        return getSqlSession().selectList(getNameSpace() + "getAllFolderList");
    }

    /**
     * 获取阈值列表
     */
    @Override
    public List<OltPerfThreshold> getDeviceThreshold(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getDeviceThreshold", entityId);
    }

    /**
     * 获取所有monitor的name列表 新建监视器时使用，用于检测名字的重复性
     */
    @Override
    public List<String> getMonitorNameList() {
        return getSqlSession().selectList(getNameSpace() + "getMonitorNameList");
    }

    /**
     * 获取collect的PortIndexList
     */
    @Override
    public List<EponStatsRecord> getCollectIndex(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCollectIndex", entityId);
    }

    /**
     * 添加一批采集管理的数据
     */
    @Override
    public void addOltCollector(final List<EponStatsRecord> statsRecord) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (EponStatsRecord p : statsRecord) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("entityId", p.getEntityId());
                map.put("portIndex", p.getPortIndex());
                map.put("collector", p.getCollector().longValue());
                sqlSession.insert(getNameSpace() + "insertOltCollector", map);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 添加一批采集管理的数据
     */
    @Override
    public void updateOltCollector(final List<EponStatsRecord> statsRecord) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (EponStatsRecord p : statsRecord) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("entityId", p.getEntityId());
                map.put("portIndex", p.getPortIndex());
                map.put("collector", p.getCollector().longValue());
                sqlSession.insert(getNameSpace() + "updateOltCollector", map);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 删除一批采集管理的数据
     */
    @Override
    public void delOltCollector(final Long entityId, final List<Long> portIndex) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (Long p : portIndex) {
                Map<String, Long> map = new HashMap<String, Long>();
                map.put("entityId", entityId);
                map.put("portIndex", p);
                sqlSession.insert(getNameSpace() + "deleteOltCollector", map);
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
    public List<MonitorPerforamnceIndex> loadMonitorIndexData(String sql) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sql", sql);
        return getSqlSession().selectList(getNameSpace() + "selectPerformanceBySql", map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltPerfDao#getMonitorInfo()
     */
    @Override
    public List<MonitorBaseInfo> getMonitorInfo(String monitorName, String ip, String name) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (monitorName != null && monitorName.equals("")) {
            paramMap.put("monitorName", null);
        } else {
            paramMap.put("monitorName", monitorName);
        }
        if (ip != null && ip.equals("")) {
            paramMap.put("ip", null);
        } else {
            paramMap.put("ip", ip);
        }
        if (name != null && name.equals("")) {
            paramMap.put("name", null);
        } else {
            paramMap.put("name", name);
        }
        List<MonitorBaseInfo> monitorInfo = new ArrayList<MonitorBaseInfo>();
        monitorInfo = getSqlSession().selectList(getNameSpace() + "getMonitorInfoList", paramMap);
        for (int i = 0; i < monitorInfo.size(); i++) {
            if (monitorInfo.get(i).getMonitorType() < 100) {
                List<MonitorPort> monitorPorts = new ArrayList<MonitorPort>();
                monitorPorts = getSqlSession().selectList(getNameSpace() + "getMonitorPorts",
                        monitorInfo.get(i).getMonitorId());
                monitorInfo.get(i).setMonitorPorts(monitorPorts);
            }
            List<MonitorIndex> monitorIndexes = new ArrayList<MonitorIndex>();
            monitorIndexes = getSqlSession().selectList(getNameSpace() + "getMonitorIndexes",
                    monitorInfo.get(i).getMonitorId());
            monitorInfo.get(i).setMonitorIndexes(monitorIndexes);
        }
        return monitorInfo;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltPerfDao#addPortMonitor(com.topvision.ems.epon.domain.MonitorBaseInfo)
     */
    @Override
    public void addMonitor(MonitorBaseInfo monitorInfo) {
        getSqlSession().insert(getNameSpace() + "insertMonitorBaseInfo", monitorInfo);
        Long id = monitorInfo.getMonitorId();
        if (monitorInfo.getMonitorType() < 100) {
            for (MonitorPort monitorPort : monitorInfo.getMonitorPorts()) {
                monitorPort.setMonitorId(id);
                getSqlSession().insert(getNameSpace() + "insertPortMonitorPortInfo", monitorPort);
            }
        }
        for (MonitorIndex monitorIndex : monitorInfo.getMonitorIndexes()) {
            monitorIndex.setMonitorId(id);
            getSqlSession().insert(getNameSpace() + "insertMonitorIndexInfo", monitorIndex);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltPerfDao#deleteMonitor(java.lang.Integer)
     */
    @Override
    public void deleteMonitor(Long monitorId) {
        getSqlSession().delete(getNameSpace() + "deleteMonitor", monitorId);
    }

    @Override
    public List<HistoryPerforamnceIndex> loadHistory(String sql) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sql", sql);
        return getSqlSession().selectList(getNameSpace() + "loadHistoryPerformance", map);
    }

    @Override
    public List<Long> queryUserFolderIdList() {
        HashMap<String, String> authorityHashMap = new HashMap<String, String>();
        String folderView = CurrentRequest.getUserAuthorityFolderName();
        authorityHashMap.put("folderView", folderView);
        return getSqlSession().selectList(getNameSpace() + "queryUserFolderIdList", authorityHashMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltPerfDao#getPortTypeByIndex(java.lang.Long, java.lang.Long)
     */
    @Override
    public Integer getPortTypeByIndex(Long entityId, Long portIndex) {
        OltSniAttribute sniAttribute = new OltSniAttribute();
        sniAttribute.setEntityId(entityId);
        sniAttribute.setSniIndex(portIndex);
        Long sniId = (Long) getSqlSession().selectOne("OltSni.getSniId", sniAttribute);
        if (sniId != null) {
            return EponConstants.SNI_PORT;
        }
        OltPonAttribute ponAttribute = new OltPonAttribute();
        ponAttribute.setEntityId(entityId);
        ponAttribute.setPonIndex(portIndex);
        Long ponId = (Long) getSqlSession()
                .selectOne("com.topvision.ems.epon.olt.domain.OltPon.getPonId", ponAttribute);
        if (ponId != null) {
            return EponConstants.PON_PORT;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltPerfDao#insertEponServiceQuality(java.lang.Long,
     * java.lang.String, java.util.List)
     */
    @Override
    public void insertEponServiceQuality(final Long entityId, final String targetName,
            final EponServiceQualityPertResult perfs) {
        SqlSession sqlSession = getBatchSession();
        try {
            Map<String, Object> perfMap = new HashMap<String, Object>();
            if (PerfTargetConstants.OLT_CPUUSED.equals(targetName)) {
                for (OltServiceQualityPerf perf : perfs.getPerfs()) {
                    perfMap.clear();
                    perfMap.put("entityId", entityId);
                    perfMap.put("slotIndex", perf.getSlotIndex());
                    perfMap.put("collectValue", perf.getTopSysBdCPUUseRatio());
                    perfMap.put("collectTime", perf.getCollectTime());
                    sqlSession.insert(getNameSpace() + "insertEponCpuQuality", perfMap);
                }
            } else if (PerfTargetConstants.OLT_MEMUSED.equals(targetName)) {
                for (OltServiceQualityPerf perf : perfs.getPerfs()) {
                    perfMap.clear();
                    perfMap.put("entityId", entityId);
                    perfMap.put("slotIndex", perf.getSlotIndex());
                    perfMap.put("collectValue", perf.getMemUsed());
                    perfMap.put("collectTime", perf.getCollectTime());
                    sqlSession.insert(getNameSpace() + "insertEponMemQuality", perfMap);
                }
            } else if (PerfTargetConstants.OLT_FLASHUSED.equals(targetName)) {
                for (OltServiceQualityPerf perf : perfs.getPerfs()) {
                    perfMap.clear();
                    perfMap.put("entityId", entityId);
                    perfMap.put("slotIndex", perf.getSlotIndex());
                    perfMap.put("collectValue", perf.getFlashUsed());
                    perfMap.put("collectTime", perf.getCollectTime());
                    sqlSession.insert(getNameSpace() + "insertEponFlashQuality", perfMap);
                }
            } else if (PerfTargetConstants.OLT_BOARDTEMP.equals(targetName)) {
                for (OltServiceQualityPerf perf : perfs.getPerfs()) {
                    if (EponUtil.isValidBoardTemp(perf.getTopSysBdCurrentTemperature())) {
                        perfMap.clear();
                        perfMap.put("entityId", entityId);
                        perfMap.put("slotIndex", perf.getSlotIndex());
                        perfMap.put("targetName", targetName);
                        perfMap.put("collectValue", perf.getTopSysBdCurrentTemperature());
                        perfMap.put("collectTime", perf.getCollectTime());
                        sqlSession.insert(getNameSpace() + "insertEponBoardTempQuality", perfMap);
                    }
                }
            } else if (PerfTargetConstants.OLT_FANSPEED.equals(targetName)) {
                for (OltFanPerf perf : perfs.getFanSpeedPerfs()) {
                    perfMap.clear();
                    perfMap.put("entityId", entityId);
                    perfMap.put("slotIndex", perf.getFanIndex());
                    perfMap.put("collectValue", perf.getFanSpeed());
                    perfMap.put("collectTime", perf.getCollectTime());
                    sqlSession.insert(getNameSpace() + "insertEponFanSpeedQuality", perfMap);
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
    public void insertEponLinkQuality(final Long entityId, final String targetName,
            final List<EponLinkQualityData> perfs) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (EponLinkQualityData linkQuality : perfs) {
                sqlSession.insert(getNameSpace() + "insertEponLinkQuality", linkQuality);
                if (linkQuality.getPortType().equals("SNI")) {
                    sqlSession.update(getNameSpace() + "syncSniOptical", linkQuality);
                } else if (linkQuality.getPortType().equals("PON")) {
                    sqlSession.update(getNameSpace() + "syncPonOptical", linkQuality);
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
    public void insertEponFlowQuality(final Long entityId, final List<OltFlowQuality> perfs) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltFlowQuality perf : perfs) {
                if (perf != null && perf.getCollectStatus()) {
                    sqlSession.insert(getNameSpace() + "insertEponFlowQuality", perf);
                    //插入汇总表
                    sqlSession.insert(getNameSpace() + "insertEponFlowQualitySummary", perf);
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
    public List<Long> getOltSniIndexList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltSniIndexList", entityId);
    }

    @Override
    public List<Long> getOltPonIndexList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltPonIndexList", entityId);
    }

    @Override
    public List<Long> getOltOnuPonIndexList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltOnuPonIndexList", entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.performance.dao.OltPerfDao#getOltUniIndexList(java.lang.Long)
     */
    @Override
    public List<Long> getOltUniIndexList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOltUniIndexList", entityId);
    }

    @Override
    public List<PerfRecord> getDeviceSniLoading(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getDeviceSniLoading", entityId);
    }

    @Override
    public List<PerfRecord> getDevicePonLoading(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getDevicePonLoading", entityId);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.performance.domain.OltPerf";
    }

    @Override
    public void updateSlotStatusWithServiceQuality(List<OltServiceQualityPerf> oltServiceQualityPerfs) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltServiceQualityPerf perf : oltServiceQualityPerfs) {
                sqlSession.update(getNameSpace() + "syncSlotStatus", perf);
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
    public Long getSlotNoByIndex(Long slotIndex, Long entityId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("slotIndex", slotIndex);
        paramMap.put("entityId", entityId);
        return getSqlSession().selectOne(getNameSpace("getSlotNoByIndex"), paramMap);
    }
}
