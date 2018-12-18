/***********************************************************************
 * $Id: OltPerfService.java,v1.0 2013-10-25 下午3:43:54 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.domain.EponStatsRecord;
import com.topvision.ems.epon.domain.HistoryPerforamnceIndex;
import com.topvision.ems.epon.domain.MonitorBaseInfo;
import com.topvision.ems.epon.domain.MonitorPerforamnceIndex;
import com.topvision.ems.epon.domain.OltPerf;
import com.topvision.ems.epon.domain.OltPerfThreshold;
import com.topvision.ems.epon.domain.OltSimplePort;
import com.topvision.ems.epon.domain.PerfFolderDevice;
import com.topvision.ems.epon.domain.PerfRecord;
import com.topvision.ems.epon.domain.PerfTrendAttribute;
import com.topvision.ems.epon.performance.domain.PerfCurStatsTable;
import com.topvision.ems.epon.performance.domain.PerfStatCycle;
import com.topvision.ems.epon.performance.domain.PerfStats15Table;
import com.topvision.ems.epon.performance.domain.PerfStats24Table;
import com.topvision.ems.epon.performance.domain.PerfStatsGlobalSet;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-10-25-下午3:43:54
 *
 */
public interface OltPerfService extends Service {

    /**
     * 通过指定端口加上指标列表获得实时的指标值
     * 
     * @param simplePort
     *            界面端传递进来的simplePort对象
     * @param oltPerfs
     *            指标列表
     * @return Map<Integer, PerfTrendAttribute> 以指标index作为key的一个map
     */
    // Map<Integer, PerfTrendAttribute> getCurPerfRecord(OltSimplePort simplePort, List<OltPerf>
    // oltPerfs);

    /**
     * 修改阈值
     * 
     * @param perfThreshold
     *            修改值 @throws ModifyPerfThresholdException 修改阈值失败抛出该异常
     */
    void modifyPerfThreshold(OltPerfThreshold perfThreshold);

    /**
     * 从设备上刷新阈值到数据库
     * 
     * @param entityId
     *            所属设备ID
     * @throws com.topvision.ems.epon.exception.RefreshPerfThresholdException
     *             刷新阈值失败抛出该异常
     */
    void refreshPerfThreshold(Long entityId);

    /**
     * 获得轮询周期设置参数
     * 
     * @param entityId
     *            所属设备ID
     * @return PerfStatCycle
     */
    PerfStatCycle getPerfStatCycle(Long entityId);

    /**
     * 保存轮询周期设置
     * 
     * @param perfStatCycle
     *            perfStatCycle
     * @throws com.topvision.ems.epon.exception.SavePerfStatCycleException
     */
    void savePerfStatCycle(PerfStatCycle perfStatCycle);

    /**
     * 从设备上刷新轮询间隔到数据库
     * 
     * @param entityId
     *            所属设备ID
     * @throws com.topvision.ems.epon.exception.RefreshPerfStatCycleException
     *             刷新轮询间隔失败抛出该异常
     */
    void refreshPerfStatCycle(Long entityId);

    /**
     * 获取记录数配置参数
     * 
     * @param entityId
     *            所属设备ID
     * @return PerfStatsGlobalSet
     */
    PerfStatsGlobalSet getPerfStatsGlobalSet(Long entityId);

    /**
     * 保存记录数配置参数
     * 
     * @param perfStatsGlobalSet
     *            perfStatsGlobalSet
     * @throws com.topvision.ems.epon.exception.SavePerfStatsGlobalSetException
     *             保存记录数失败抛出该异常
     */
    void savePerfStatsGlobalSet(PerfStatsGlobalSet perfStatsGlobalSet);

    /**
     * 从设备上刷新历史数据记录数配置到数据库
     * 
     * @param entityId
     *            所属设备ID
     * @throws com.topvision.ems.epon.exception.RefreshPerfStatsGlobalSetException
     *             刷新历史数据记录数配置失败抛出该异常
     */
    void refreshPerfStatsGlobalSet(Long entityId);

    /**
     * 通过指定端口加上指标列表获得实时的指标值
     * 
     * @param simplePort
     *            界面端传递进来的simplePort对象
     * @param oltPerfs
     *            指标列表
     * @return List<PerfTrendAttribute>
     */
    List<PerfCurStatsTable> getCurPerfRecordList(OltSimplePort simplePort, List<OltPerf> oltPerfs);

    /**
     * 通过指定端口加上指标列表获得实时的指标值
     * 
     * @param simplePort
     *            界面端传递进来的simplePort对象
     * @param oltPerfs
     *            指标列表
     * @return Map<Integer, PerfTrendAttribute> 以指标index作为key的一个map
     */
    // Map<Integer, PerfTrendAttribute> getCurPerfRecord(OltSimplePort simplePort, List<OltPerf>
    // oltPerfs);

    /**
     * 获得某个指标15分钟的历史性能
     * 
     * @param params
     *            params
     * @return List<PerfStats15Table>
     */
    List<PerfStats15Table> get15HistoryPerformance(Map<String, Object> params);

    /**
     * 通过指定端口加上指标列表获得15分钟的指标值
     * 
     * @param params
     *            指定端口
     * @return List<PerfStats15Table> 获得15分钟性能值
     */
    List<PerfStats15Table> getPerf15ByTarget(Map<String, Object> params);

    /**
     * 通过指定端口加上指标列表获得15分钟的指标值趋势
     * 
     * @param oltSimplePort
     *            指定端口
     * @param oltPerfs
     *            指标列表
     * @return Map<Integer, List<PerfTrendAttribute>> 以指标index作为key的一个map
     */
    Map<String, List<PerfTrendAttribute>> getPerf15FromDB(OltSimplePort oltSimplePort, List<OltPerf> oltPerfs);

    /**
     * 刷新某个接口的15分钟性能数据
     * 
     * @param oltSimplePort
     *            端口INDEX
     * @throws com.topvision.ems.epon.exception.RefreshPerfException
     *             刷新历史性能数据失败抛出该异常
     */
    void refreshPerf15FromFacade(OltSimplePort oltSimplePort);

    /**
     * 通过指定端口加上指标列表获得24小时的指标值
     * 
     * @param params
     *            指定端口
     * @return List<PerfStats15Table> 获得24小时性能值
     */
    List<PerfStats24Table> getPerf24ByTarget(Map<String, Object> params);

    /**
     * 通过指定端口加上指标列表获得24小时的指标值趋势
     * 
     * @param oltSimplePort
     *            指定端口
     * @param oltPerfs
     *            指标列表
     * @return Map<Integer, List<PerfTrendAttribute>> 以指标index作为key的一个map
     */
    Map<Integer, List<PerfTrendAttribute>> getPerf24FromDB(OltSimplePort oltSimplePort, List<OltPerf> oltPerfs);

    /**
     * 获得某个指标24小时的历史性能
     * 
     * @param params
     *            params
     * @return List<PerfStats24Table>
     */
    List<PerfStats24Table> get24HistoryPerformance(Map<String, Object> params);

    /**
     * 刷新某个接口的24小时性能数据
     * 
     * @param oltSimplePort
     *            端口INDEX
     * @throws com.topvision.ems.epon.exception.RefreshPerfException
     *             刷新历史性能数据失败抛出该异常
     */
    void refreshPerf24FromFacade(OltSimplePort oltSimplePort);

    /**
     * 获取流量排名前10的Sni口列表
     * 
     * @return List<PerfRecord>
     */
    List<PerfRecord> getTopSniLoading(Map<String, Object> params);

    /**
     * 获取SNI速率的记录数
     * 
     * @return List<PerfRecord>
     */
    Integer getTopSniSpeedCount(Map<String, Object> params);

    /**
     * 获取流量排名前10的Pon口列表
     * 
     * @return List<PerfRecord>
     */
    List<PerfRecord> getTopPonLoading(Map<String, Object> params);

    /**
     * 获取PON速率的记录数
     * 
     * @return List<PerfRecord>
     */
    Integer getTopPonSpeedCount(Map<String, Object> params);

    /**
     * 获取OLT设备列表
     */
    List<PerfFolderDevice> getAllOltList();

    /**
     * 获取OLT设备列表
     */
    List<PerfFolderDevice> getAllOltDeviceList();

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
     * 获取监视器信息
     * @param monitorName
     * @param ip
     * @param name
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
    List<MonitorPerforamnceIndex> loadMonitorIndexData(String index, List<String> ports);

    /**
     * 查询某一时间段内的端口历史性能数据
     * 
     * @param entityId
     * @param portIndex
     * @param monitorIndex
     * @param head
     * @param startTime
     * @param endTime
     * @return
     */
    List<HistoryPerforamnceIndex> loadHistory(Long entityId, Long portIndex, String monitorIndex, String head,
            String startTime, String endTime);

    /**
     * 加载24小时历史性能统计记录
     * 
     * @param parseLong
     * @param parseLong2
     * @param monitorIndex
     * @param indexHead
     * @param startTime
     * @param endTime
     * @return
     */
    List<HistoryPerforamnceIndex> loadHistory24(long parseLong, long parseLong2, String monitorIndex, String indexHead,
            String startTime, String endTime);

    /**
     * 获取用户的权限地域
     * @return
     */
    List<Long> getUserFolderIdList();

    /**
     * @param entityId
     * @param thresholdType
     * @return
     */
    List<OltPerfThreshold> getPerfThresholdByType(Long entityId, String thresholdType);

    /**
     * 获取OLT设备SNI端口速率
     * 
     * @return List<PerfRecord>
     */
    List<PerfRecord> getDeviceSniLoading(Long entityId);

    /**
     * 获取OLT设备Pon端口速率
     * 
     * @return List<PerfRecord>
     */
    List<PerfRecord> getDevicePonLoading(Long entityId);
}
