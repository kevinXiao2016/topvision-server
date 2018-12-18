/***********************************************************************
 * $ com.topvision.ems.epon.dao.OltPerfDao,v1.0 2011-11-20 16:56:54 $
 *
 * @author: Rod John
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.EponStatsRecord;
import com.topvision.ems.epon.domain.HistoryPerforamnceIndex;
import com.topvision.ems.epon.domain.MonitorBaseInfo;
import com.topvision.ems.epon.domain.MonitorPerforamnceIndex;
import com.topvision.ems.epon.domain.OltPerfThreshold;
import com.topvision.ems.epon.domain.OltSimplePort;
import com.topvision.ems.epon.domain.PerfFolderDevice;
import com.topvision.ems.epon.domain.PerfRecord;
import com.topvision.ems.epon.exception.SavePerfStatCycleException;
import com.topvision.ems.epon.exception.SavePerfStatsGlobalSetException;
import com.topvision.ems.epon.performance.domain.EponLinkQualityData;
import com.topvision.ems.epon.performance.domain.EponServiceQualityPertResult;
import com.topvision.ems.epon.performance.domain.PerfCurStatsTable;
import com.topvision.ems.epon.performance.domain.PerfStatCycle;
import com.topvision.ems.epon.performance.domain.PerfStats15Table;
import com.topvision.ems.epon.performance.domain.PerfStats24Table;
import com.topvision.ems.epon.performance.domain.PerfStatsGlobalSet;
import com.topvision.ems.epon.performance.facade.OltFlowQuality;
import com.topvision.ems.epon.performance.facade.OltServiceQualityPerf;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Rod John
 * @created @2011-11-20-16:56:54
 * @modified by Rod @2013-6-21
 * 
 */
public interface OltPerfDao extends BaseEntityDao<OltPerfThreshold> {

    /**
     * 查询设备所有SNI口
     * 
     * @param entityId
     * @return
     */
    List<Long> getOltSniIndexList(Long entityId);

    /**
     * 查询设备所有PON口
     * 
     * @param entityId
     * @return
     */
    List<Long> getOltPonIndexList(Long entityId);

    /**
     * 查询设备所有ONU PON口
     * 
     * @param entityId
     * @return
     */
    List<Long> getOltOnuPonIndexList(Long entityId);

    /**
     * 查询设备所有UNI口
     * 
     * @param entityId
     * @return
     */
    List<Long> getOltUniIndexList(Long entityId);

    /**
     * 通过阈值类型获取阈值设置
     * 
     * @param entityId
     *            所属设备ID
     * @param thresholdType
     *            阈值作用对象类型 @return List<PerfThreshold> 该类型包含的所有阈值
     */
    List<OltPerfThreshold> getPerfThresholdByType(Long entityId, String thresholdType);

    /**
     * 修改阈值
     * 
     * @param perfThreshold
     *            修改值 @throws ModifyPerfThresholdException 修改阈值失败抛出该异常
     */
    void modifyPerfThreshold(OltPerfThreshold perfThreshold);

    /**
     * 获得轮询周期设置参数
     * 
     * @param entityId
     *            所属设备ID
     * @return
     */
    PerfStatCycle getPerfStatCycle(Long entityId);

    /**
     * 保存轮询周期设置
     * 
     * @param perfStatCycle
     * @throws SavePerfStatCycleException
     */
    void savePerfStatCycle(PerfStatCycle perfStatCycle);

    /**
     * 获取记录数配置参数
     * 
     * @param entityId
     *            所属设备ID
     * @return
     */
    PerfStatsGlobalSet getPerfStatsGlobalSet(Long entityId);

    /**
     * 保存记录数配置参数
     * 
     * @param perfStatsGlobalSet
     * @throws SavePerfStatsGlobalSetException
     *             保存记录数失败抛出该异常
     */
    void savePerfStatsGlobalSet(PerfStatsGlobalSet perfStatsGlobalSet);

    /**
     * 通过指定端口加上指标列表获得15分钟的指标值趋势
     * 
     * @param oltSimplePort
     *            指定端口
     * @param perfIndex
     *            指标index
     * @return List<PerfTrendAttribute>
     */
    List<PerfStats15Table> getPerf15FromDB(Map<String, Object> params);

    /**
     * 清除DB中15分钟的性能数据
     * 
     * @param perfStats15Table
     *            指定端口
     */
    void deletePerf15FromDB(PerfStats15Table perfStats15Table);

    /**
     * 通过指定端口加上指标列表获得15分钟的指标值
     * 
     * @param 指定端口
     * @return List<PerfStats15Table> 获得15分钟性能值
     */
    List<PerfStats15Table> get15HistoryPerformance(Map<String, Object> params);

    /**
     * 通过指定端口加上指标列表获得24小时的指标值趋势
     * 
     * @param oltSimplePort
     *            指定端口
     * @param perfIndex
     *            指标index
     * @return Map<Integer, List<PerfTrendAttribute>> 以指标index作为key的一个map
     */
    List<PerfStats24Table> getPerf24FromDB(Map<String, Object> params);

    /**
     * 清除DB中24小时的性能数据
     * 
     * @param perfStats24Table
     *            指定端口
     */
    void deletePerf24FromDB(PerfStats24Table perfStats24Table);

    /**
     * 通过指定端口加上指标列表获得24小时的指标值
     * 
     * @param 指定端口
     * @return List<PerfStats24Table> 获得24小时性能值
     */
    List<PerfStats24Table> get24HistoryPerformance(Map<String, Object> params);

    /**
     * 保存阈值配置
     * 
     * @param entityId
     * @param oltPerfThresholds
     */
    void saveOltPerfThresholds(Long entityId, List<OltPerfThreshold> oltPerfThresholds);

    /**
     * 获取数据库中15分钟历史性能记录最后一次的采集时间
     * 
     * @param oltSimplePort
     * @return
     */
    Long getLast15RecordTime(OltSimplePort oltSimplePort);

    /**
     * 获取数据库中15分钟历史性能记录最后一次的采集时间
     * 
     * @return
     */
    String getLast15RecordTimeString(Long entityId, Long portIndex);

    /**
     * 获取数据库中24小时历史性能记录最后一次的采集时间
     * 
     * @param perfStats15Table
     * @return
     */
    Long getLast24RecordTime(OltSimplePort oltSimplePort);

    /**
     * 获取数据库中15分钟历史性能记录最后一次的采集时间
     * 
     * @return
     */
    String getLast24RecordTimeString(Long entityId, Long portIndex);

    /**
     * 保存刷新到的15分钟历史性能数据
     * 
     * @param newPerfStats15Table
     */
    void savePerfStats15Table(List<PerfStats15Table> newPerfStats15Table);

    /**
     * 保存刷新到的24小时历史性能数据
     * 
     * @param newPerfStats15Table
     */
    void savePerfStats24Table(List<PerfStats24Table> newPerfStats24Table);

    /**
     * 获取流量排名前10的Sni口列表
     * 
     * @return
     */
    List<PerfRecord> getTopSniLoading(Map<String, Object> params);

    /**
     * 获取SNI速率排名记录数
     * 
     * @return
     */
    Integer getSniSpeedCount(Map<String, Object> params);

    /**
     * 获取流量排名前10的Pon口列表
     * 
     * @return
     */
    List<PerfRecord> getTopPonLoading(Map<String, Object> params);

    /**
     * 获取SNI速率排名记录数
     * 
     * @return
     */
    Integer getPonSpeedCount(Map<String, Object> params);

    /**
     * 插入Epon当前实时性能的数据
     * 
     * @param perfCurStatsTables
     */
    void insertEponCurrentStats(List<PerfCurStatsTable> perfCurStatsTables);

    /**
     * 插入Epon15分钟性能的数据
     * 
     * @param perfStats15Tables
     */
    void insertEpon15MinuteStats(List<PerfStats15Table> perfStats15Tables);

    /**
     * 插入Epon15分钟性能的数据
     * 
     * @param perfStats15Tables
     */
    void insertEpon15MinuteStats(PerfStats15Table perfStats15Table);

    /**
     * 插入Epon24小时性能的数据
     * 
     * @param perfStats24Tables
     */
    void insertEpon24HourStats(List<PerfStats24Table> perfStats24Tables);

    /**
     * 获得Epon当前模块的性能处理集合
     * 
     * @param entityId
     * @param category
     * @return
     */
    List<Integer> getEponPerformanceMonitorId(Long entityId, String category);

    /**
     * 获取OLT设备列表
     * @param type
     * @return
     */
    List<PerfFolderDevice> getAllOltList(Long type);

    /**
     * 获取OLT设备列表
     * @param type
     */
    List<PerfFolderDevice> getAllOltDeviceList(Long type);

    /**
     * 获取地域列表
     */
    List<TopoFolder> getAllFolderList();

    /**
     * 获取阈值列表
     */
    List<OltPerfThreshold> getDeviceThreshold(Long entityId);

    /**
     * 获取所有monitor的name列表 新建监视器时使用，用于检测名字的重复性
     */
    List<String> getMonitorNameList();

    /**
     * 获取collect的PortIndexList
     */
    List<EponStatsRecord> getCollectIndex(Long entityId);

    /**
     * 添加一批采集管理的数据
     */
    void addOltCollector(List<EponStatsRecord> statsRecord);

    /**
     * 修改一批采集管理的数据
     */
    void updateOltCollector(List<EponStatsRecord> statsRecord);

    /**
     * 删除一批采集管理的数据
     */
    void delOltCollector(Long entityId, List<Long> portIndex);

    /**
     * 获取性能监视器信息
     * 
     * @return
     */
    List<MonitorBaseInfo> getMonitorInfo(String monitorName, String ip, String name);

    /**
     * 新增性能监视器
     * 
     * @param monitorIndo
     */
    void addMonitor(MonitorBaseInfo monitorInfo);

    /**
     * 删除监视器
     * 
     * @param monitorId
     */
    void deleteMonitor(Long monitorId);

    /**
     * 获取实时性能数据
     * 
     * @param index
     * @param ports
     * @return
     */
    List<MonitorPerforamnceIndex> loadMonitorIndexData(String sql);

    /**
     * 查询某一时间段内的端口历史性能数据
     * 
     * @param sql
     * @return
     */
    List<HistoryPerforamnceIndex> loadHistory(String sql);

    /**
     * 获取用户权限地域
     * 
     * @return
     */
    List<Long> queryUserFolderIdList();

    /**
     * Add by Rod
     * 
     * 根据PORT的INDEX获得PORT的类型
     */
    Integer getPortTypeByIndex(Long entityId, Long portIndex);

    /**
     * 更新EPON服务质量性能数据
     * 
     * @param entityId
     * @param targetName
     * @param perfs
     */
    void insertEponServiceQuality(Long entityId, String targetName, EponServiceQualityPertResult perfs);

    /**
     * 更新EPON光链路质量性能数据
     * 
     * @param entityId
     * @param targetName
     * @param perfs
     */
    void insertEponLinkQuality(Long entityId, String targetName, List<EponLinkQualityData> perfs);

    /**
     * 更新EPON端口流量性能数据
     * 
     * @param entityId
     * @param perfs
     */
    void insertEponFlowQuality(Long entityId, List<OltFlowQuality> perfs);

    /**
     * 获取SNI端口速率
     * 
     * @return
     */
    List<PerfRecord> getDeviceSniLoading(Long entityId);

    /**
     * 获取PON端口速率
     * 
     * @return
     */
    List<PerfRecord> getDevicePonLoading(Long entityId);

    /**
     * 性能更新SLOT状态
     * 
     * @param oltServiceQualityPerfs
     */
    void updateSlotStatusWithServiceQuality(List<OltServiceQualityPerf> oltServiceQualityPerfs);

}
