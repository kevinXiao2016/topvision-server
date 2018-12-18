/***********************************************************************
 * $Id: EponStatsService.java,v1.0 2013-10-25 下午3:46:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.topvision.ems.epon.exception.ModifyPerfThresholdException;
import com.topvision.ems.epon.exception.RefreshPerfException;
import com.topvision.ems.epon.exception.RefreshPerfStatCycleException;
import com.topvision.ems.epon.exception.RefreshPerfStatsGlobalSetException;
import com.topvision.ems.epon.exception.RefreshPerfThresholdException;
import com.topvision.ems.epon.exception.SavePerfStatCycleException;
import com.topvision.ems.epon.exception.SavePerfStatsGlobalSetException;
import com.topvision.ems.epon.performance.dao.OltPerfDao;
import com.topvision.ems.epon.performance.domain.PerfCurStatsTable;
import com.topvision.ems.epon.performance.domain.PerfStatCycle;
import com.topvision.ems.epon.performance.domain.PerfStats15Table;
import com.topvision.ems.epon.performance.domain.PerfStats24Table;
import com.topvision.ems.epon.performance.domain.PerfStatsGlobalSet;
import com.topvision.ems.epon.performance.domain.PerfThresholdPort;
import com.topvision.ems.epon.performance.domain.PerfThresholdTemperature;
import com.topvision.ems.epon.performance.facade.OltPerfFacade;
import com.topvision.ems.epon.performance.service.OltPerfService;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-下午3:46:42
 *
 */
@Service("oltPerfService")
public class OltPerfServiceImpl extends BaseService implements OltPerfService, SynchronizedListener {
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltPerfDao oltPerfDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    /**
     * 新增设备业务属性
     * 
     * @param event
     */
    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        Long timeTmp = 0L;
        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "PerfStatCycle");
        try {
            refreshPerfStatCycle(event.getEntityId());
            logger.info("refreshPerfStatCycle finished!");
        } catch (Exception e) {
            logger.error("refreshPerfStatCycle error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "PerfStatCycle", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "PerfStatsGlobalSet");
        try {
            refreshPerfStatsGlobalSet(event.getEntityId());
            logger.info("refreshPerfStatsGlobalSet finished!");
        } catch (Exception e) {
            logger.error("refreshPerfStatsGlobalSet error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "PerfStatsGlobalSet", timeTmp);

        timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "PerfThresholdPort");
        try {
            refreshPerfThreshold(event.getEntityId());
            logger.info("refreshPerfThreshold finished!");
        } catch (Exception e) {
            logger.error("refreshPerfThreshold error:", e);
        }
        LoggerUtil.topoEndTimeLog(event.getIpAddress(), "PerfThresholdPort", timeTmp);
    }

    /**
     * 同步设备业务属性
     * 
     * @param event
     */
    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }

    /**
     * 获得轮询周期设置参数
     * 
     * @param entityId
     *            所属设备ID
     * @return
     */
    @Override
    public PerfStatCycle getPerfStatCycle(Long entityId) {
        return oltPerfDao.getPerfStatCycle(entityId);
    }

    /**
     * 通过阈值类型获取阈值设置
     * 
     * @param entityId
     *            所属设备ID
     * @param thresholdType
     *            阈值作用对象类型
     * @return List<OltPerfThreshold> 该类型包含的所有阈值
     */
    @Override
    public List<OltPerfThreshold> getPerfThresholdByType(Long entityId, String thresholdType) {
        List<OltPerfThreshold> perfTempList = oltPerfDao.getPerfThresholdByType(entityId, thresholdType);
        List<OltPerfThreshold> perfList = new ArrayList<OltPerfThreshold>();
        if (thresholdType.equals("TEMPERATURE")) {
            if (perfTempList.size() > 0) {
                for (OltPerfThreshold pe : perfTempList) {
                    perfList.add(pe);
                }
            }
        } else {
            Integer perfTarget[] = OltPerf.getPerfTargetGroup(thresholdType);
            if (perfTempList.size() > 0) {
                for (int i : perfTarget) {
                    perfList.add(perfTempList.get(i - 1));
                }
            }
        }
        return perfList;
    }

    /**
     * 修改阈值
     * 
     * @param entityId
     *            所属设备ID
     * @param thresholdType
     *            阈值作用对象类型
     * @param perfThreshold
     *            修改值
     * @throws ModifyPerfThresholdException
     *             修改阈值失败抛出该异常
     */
    @Override
    public void modifyPerfThreshold(OltPerfThreshold perfThreshold) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(perfThreshold.getEntityId());
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());
        try {
            // 构造传递给facade的对象
            if (!perfThreshold.getThresholdType().equalsIgnoreCase(OltPerfThreshold.TEMPERATURE)) {
                PerfThresholdPort perfThresholdPort = perfThreshold.createPerfThresholdPort();
                // 设置端口性能门限值到设备
                oltPerfFacade.modifyPerfThreshold(snmpParam, perfThresholdPort);
            } else {
                PerfThresholdTemperature perfThresholdTemperature = perfThreshold.createPerfThresholdTemperature();
                // 设置端口性能门限值到设备
                oltPerfFacade.modifyPerfThreshold(snmpParam, perfThresholdTemperature);
            }
        } catch (Exception e) {
            throw new ModifyPerfThresholdException(e);
        }
        // 修改数据门限值设置
        oltPerfDao.modifyPerfThreshold(perfThreshold);
    }

    /**
     * 通过指定端口加上指标列表获得实时的指标值
     * 
     * @param entityId
     *            设备id
     * @param portId
     *            指定端口
     * @param oltPerfs
     *            指标列表 @return Map<Integer, PerfTrendAttribute> 以指标index作为key的一个map
     */
    @Override
    public List<PerfCurStatsTable> getCurPerfRecordList(OltSimplePort oltSimplePort, List<OltPerf> oltPerfs) {
        List<PerfCurStatsTable> re = new ArrayList<PerfCurStatsTable>();
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltSimplePort.getEntityId());
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());

        long portindex = oltSimplePort.getPortIndex();
        boolean isLinkDownPerf = false;

        if (EponIndex.getOnuNo(portindex) > 0) {
            isLinkDownPerf = true;
        }

        // 构造采集index对象
        PerfCurStatsTable perfCurStatsTable = new PerfCurStatsTable();
        perfCurStatsTable.setPortIndex(oltSimplePort.getPortIndex());
        Timestamp time = new Timestamp(System.currentTimeMillis());
        // 在设备上获取端口index对应的接口实时性能
        PerfCurStatsTable curStatsTable = oltPerfFacade.getCurPerfRecord(snmpParam, perfCurStatsTable);
        if (curStatsTable != null) {
            curStatsTable.setCurStatsEndTime(time);
            if (isLinkDownPerf) {
                double curStatsOutOctets = Double.parseDouble(curStatsTable.getCurStatsOutOctets());
                curStatsTable.setCurStatsOutOctets(curStatsOutOctets / 3 + "");
                double curStatsInOctets = Double.parseDouble(curStatsTable.getCurStatsInOctets());
                curStatsTable.setCurStatsInOctets(curStatsInOctets / 3 + "");
            }
            re.add(curStatsTable);
        }
        return re; // To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 通过指定端口加上指标列表获得15分钟的指标值
     * 
     * @param oltSimplePort
     *            指定端口
     * @return List<PerfStats15Table> 获得15分钟性能值
     */
    @Override
    public List<PerfStats15Table> getPerf15ByTarget(Map<String, Object> params) {
        Long st = new Date().getTime() - 50 * 60 * 60 * 1000;
        Long et = new Date().getTime();
        params.put("startTime", new Timestamp(st));
        params.put("endTime", new Timestamp(et));
        return oltPerfDao.getPerf15FromDB(params);
    }

    /**
     * 通过指定端口加上指标列表获得15分钟的指标值
     * 
     * @param 指定端口
     * @return List<PerfStats15Table> 获得15分钟性能值
     */
    @Override
    public List<PerfStats15Table> get15HistoryPerformance(Map<String, Object> params) {
        return oltPerfDao.get15HistoryPerformance(params);
    }

    /**
     * 通过指定端口加上指标列表获得15分钟的指标值趋势
     * 
     * @param oltSimplePort
     *            指定端口
     * @param oltPerfs
     *            指标列表
     * @return Map<Integer, List<PerfTrendAttribute>> 以指标index作为key的一个map
     */
    @Override
    public Map<String, List<PerfTrendAttribute>> getPerf15FromDB(OltSimplePort oltSimplePort, List<OltPerf> oltPerfs) {
        Map<String, List<PerfTrendAttribute>> re = new HashMap<String, List<PerfTrendAttribute>>();
        // List<PerfStats15Table> perfStats15Tables = oltPerfDao.getPerf15FromDB(oltSimplePort);
        List<PerfStats15Table> perfStats15Tables = new ArrayList<PerfStats15Table>();
        for (PerfStats15Table perfStats15Table : perfStats15Tables) {
            try {
                Map<Integer, Long> map = OltPerf.createPerfMap(perfStats15Table);
                for (OltPerf oltPerf : oltPerfs) {
                    List<PerfTrendAttribute> list;
                    if (re.containsKey(oltPerf.getPerfIndex())) {
                        list = re.get(oltPerf.getPerfIndex());
                    } else {
                        list = new ArrayList<PerfTrendAttribute>();
                        re.put(oltPerf.getPerfIndex().toString(), list);
                    }
                    PerfTrendAttribute perfTrendAttribute = new PerfTrendAttribute();
                    perfTrendAttribute.setValue(map.get(oltPerf.getPerfIndex()));
                    perfTrendAttribute.setFacadeTime(perfStats15Table.getStats15EndTimeDb());
                    list.add(perfTrendAttribute);
                }
            } catch (InvocationTargetException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("", e);
                }
            } catch (IllegalAccessException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("", e);
                }
            }
        }
        return re;
    }

    /**
     * 通过指定端口加上指标列表获得24小时的指标值趋势
     * 
     * @param oltSimplePort
     *            指定端口
     * @param oltPerfs
     *            指标列表
     * @return Map<Integer, List<PerfTrendAttribute>> 以指标index作为key的一个map
     */
    @Override
    public Map<Integer, List<PerfTrendAttribute>> getPerf24FromDB(OltSimplePort oltSimplePort, List<OltPerf> oltPerfs) {
        Map<Integer, List<PerfTrendAttribute>> re = new HashMap<Integer, List<PerfTrendAttribute>>();
        // List<PerfStats24Table> perfTrendAttributes = oltPerfDao.getPerf24FromDB(oltSimplePort);
        List<PerfStats24Table> perfTrendAttributes = new ArrayList<PerfStats24Table>();
        for (PerfStats24Table perfStats24Table : perfTrendAttributes) {
            try {
                Map<Integer, Long> map = OltPerf.createPerfMap(perfStats24Table);
                for (OltPerf oltPerf : oltPerfs) {
                    List<PerfTrendAttribute> list;
                    if (re.containsKey(oltPerf.getPerfIndex())) {
                        list = re.get(oltPerf.getPerfIndex());
                    } else {
                        list = new ArrayList<PerfTrendAttribute>();
                        re.put(oltPerf.getPerfIndex(), list);
                    }
                    PerfTrendAttribute perfTrendAttribute = new PerfTrendAttribute();
                    perfTrendAttribute.setValue(map.get(oltPerf.getPerfIndex()));
                    perfTrendAttribute.setFacadeTime(perfStats24Table.getStats24EndTimeDb());
                    list.add(perfTrendAttribute);
                }
            } catch (InvocationTargetException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("", e);
                }
            } catch (IllegalAccessException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("", e);
                }
            }
        }
        return re;
    }

    /**
     * 获取记录数配置参数
     * 
     * @param entityId
     *            所属设备ID
     * @return
     */
    @Override
    public PerfStatsGlobalSet getPerfStatsGlobalSet(Long entityId) {
        return oltPerfDao.getPerfStatsGlobalSet(entityId);
    }

    /**
     * 全设备刷新15分钟历史性能数据
     * 
     * @param entityId
     *            设备ID
     */
    @SuppressWarnings("unused")
    private void refreshPerf15FromFacade(long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());
        // 从设备获取某个端口的15分钟性能
        try {
            List<PerfStats15Table> newPerfStats15Table = oltPerfFacade.getPerfStats15Table(snmpParam);
            if (newPerfStats15Table != null && !newPerfStats15Table.isEmpty()) {
                // 设置entityId
                Map<Long, List<PerfStats15Table>> tmp = new HashMap<Long, List<PerfStats15Table>>();
                for (PerfStats15Table perfStats15Table : newPerfStats15Table) {
                    perfStats15Table.setEntityId(entityId);
                    List<PerfStats15Table> list;
                    if (tmp.containsKey(perfStats15Table.getPortIndex())) {
                        list = tmp.get(perfStats15Table.getPortIndex());
                    } else {
                        list = new ArrayList<PerfStats15Table>();
                        tmp.put(perfStats15Table.getPortIndex(), list);
                    }
                    list.add(perfStats15Table);
                }
                // 将设置保存到数据库
                for (Long portIndex : tmp.keySet()) {
                    OltSimplePort oltSimplePort = new OltSimplePort();
                    oltSimplePort.setEntityId(entityId);
                    oltSimplePort.setPortIndex(portIndex);
                    Long lastTime = oltPerfDao.getLast15RecordTime(oltSimplePort);
                    List<PerfStats15Table> perf15List = new ArrayList<PerfStats15Table>();
                    for (PerfStats15Table perfStats15Table : tmp.get(portIndex)) {
                        if (perfStats15Table.getStats15EndTimeDb().getTime() > lastTime) {
                            perf15List.add(perfStats15Table);
                        }
                    }
                    oltPerfDao.savePerfStats15Table(perf15List);
                }
            }
        } catch (Exception e) {
            throw new RefreshPerfStatCycleException(e);
        }
    }

    /**
     * 全设备刷新24小时历史性能数据
     * 
     * @param entityId
     *            设备ID
     */
    @SuppressWarnings("unused")
    private void refreshPerf24FromFacade(long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());
        // 从设备获取某个端口的15分钟性能
        try {
            List<PerfStats24Table> newPerfStats24Table = oltPerfFacade.getPerfStats24Table(snmpParam);
            if (newPerfStats24Table != null && !newPerfStats24Table.isEmpty()) {
                // 设置entityId
                Map<Long, List<PerfStats24Table>> tmp = new HashMap<Long, List<PerfStats24Table>>();
                for (PerfStats24Table perfStats24Table : newPerfStats24Table) {
                    perfStats24Table.setEntityId(entityId);
                    List<PerfStats24Table> list;
                    if (tmp.containsKey(perfStats24Table.getPortIndex())) {
                        list = tmp.get(perfStats24Table.getPortIndex());
                    } else {
                        list = new ArrayList<PerfStats24Table>();
                        tmp.put(perfStats24Table.getPortIndex(), list);
                    }
                    list.add(perfStats24Table);
                }
                // 将设置保存到数据库
                for (Long portIndex : tmp.keySet()) {
                    OltSimplePort oltSimplePort = new OltSimplePort();
                    oltSimplePort.setEntityId(entityId);
                    oltSimplePort.setPortIndex(portIndex);
                    Long lastTime = oltPerfDao.getLast24RecordTime(oltSimplePort);
                    List<PerfStats24Table> perf24List = new ArrayList<PerfStats24Table>();
                    for (PerfStats24Table perfStats24Table : tmp.get(portIndex)) {
                        if (perfStats24Table.getStats24EndTimeDb().getTime() > lastTime) {
                            perf24List.add(perfStats24Table);
                        }
                    }
                    oltPerfDao.savePerfStats24Table(perf24List);
                }
            }
        } catch (Exception e) {
            throw new RefreshPerfStatCycleException(e);
        }
    }

    /**
     * 刷新某个接口的15分钟性能数据
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @throws RefreshPerfException
     *             刷新历史性能数据失败抛出该异常
     */
    @Override
    public void refreshPerf15FromFacade(OltSimplePort oltSimplePort) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltSimplePort.getEntityId());
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());
        // 从设备获取某个端口的15分钟性能

        PerfStats15Table perfStats15Table = new PerfStats15Table();
        perfStats15Table.setEntityId(oltSimplePort.getEntityId());
        perfStats15Table.setPortIndex(oltSimplePort.getPortIndex());
        Long lastTime = oltPerfDao.getLast15RecordTime(oltSimplePort);
        List<PerfStats15Table> newPerfStats15Table = oltPerfFacade.getPerfStats15Table(snmpParam, perfStats15Table,
                lastTime);
        Map<String, Object> parMap = new HashMap<String, Object>();
        // 端口index
        parMap.put("portIndex", oltSimplePort.getPortIndex());
        parMap.put("entityId", oltSimplePort.getEntityId());
        List<PerfStats15Table> dbPerfStats15Table = oltPerfDao.getPerf15FromDB(parMap);
        if (newPerfStats15Table != null && !newPerfStats15Table.isEmpty()) {
            // 设置entityId
            for (PerfStats15Table perfStats15TableTmp : newPerfStats15Table) {
                perfStats15TableTmp.setEntityId(oltSimplePort.getEntityId());

                // 清除该端口的性能数据
                for (PerfStats15Table p : dbPerfStats15Table) {
                    if (p.getPortIndex().equals(perfStats15TableTmp.getPortIndex())
                            && p.getEntityId().equals(perfStats15TableTmp.getEntityId())
                            && p.getStats15EndTime().equals(perfStats15TableTmp.getStats15EndTime()))
                        oltPerfDao.deletePerf15FromDB(p);
                }

            }
            // 将设置保存到数据库
            oltPerfDao.savePerfStats15Table(newPerfStats15Table);
        }
    }

    /**
     * 通过指定端口加上指标列表获得24小时的指标值
     * 
     * @param 指定端口
     * @return List<PerfStats15Table> 获得24小时性能值
     */
    @Override
    public List<PerfStats24Table> get24HistoryPerformance(Map<String, Object> params) {
        return oltPerfDao.get24HistoryPerformance(params);
    }

    /**
     * 通过指定端口加上指标列表获得24小时的指标值
     * 
     * @param oltSimplePort
     *            指定端口
     * @return List<PerfStats24Table> 获得24小时性能值
     */
    @Override
    public List<PerfStats24Table> getPerf24ByTarget(Map<String, Object> params) {
        Long st = (new Date()).getTime() - 7 * 24 * 60 * 60 * 1000;
        Long et = new Date().getTime();
        params.put("startTime", new Timestamp(st));
        params.put("endTime", new Timestamp(et));
        return oltPerfDao.getPerf24FromDB(params);
    }

    /**
     * 刷新某个接口的24小时性能数据
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @throws RefreshPerfException
     *             刷新历史性能数据失败抛出该异常
     */
    @Override
    public void refreshPerf24FromFacade(OltSimplePort oltSimplePort) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(oltSimplePort.getEntityId());
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());
        // 从设备获取某个端口的15分钟性能
        try {
            PerfStats24Table perfStats24Table = new PerfStats24Table();
            perfStats24Table.setEntityId(oltSimplePort.getEntityId());
            perfStats24Table.setPortIndex(oltSimplePort.getPortIndex());
            Long lastTime = oltPerfDao.getLast24RecordTime(oltSimplePort);
            List<PerfStats24Table> newPerfStats24Table = oltPerfFacade.getPerfStats24Table(snmpParam, perfStats24Table,
                    lastTime);
            Map<String, Object> parMap = new HashMap<String, Object>();
            // 端口index
            parMap.put("portIndex", oltSimplePort.getPortIndex());
            parMap.put("entityId", oltSimplePort.getEntityId());
            List<PerfStats24Table> dbPerfStats24Table = oltPerfDao.getPerf24FromDB(parMap);
            if (newPerfStats24Table != null && !newPerfStats24Table.isEmpty()) {
                // 设置entityId
                for (PerfStats24Table perfStats24TableTmp : newPerfStats24Table) {
                    perfStats24TableTmp.setEntityId(oltSimplePort.getEntityId());
                }
                // 清除端口的24小时数据
                for (PerfStats24Table p : dbPerfStats24Table) {
                    if (p.getPortIndex().equals(perfStats24Table.getPortIndex())
                            && p.getEntityId().equals(perfStats24Table.getEntityId())
                            && p.getStats24EndTime().equals(perfStats24Table.getStats24EndTime()))
                        oltPerfDao.deletePerf24FromDB(perfStats24Table);
                }
                // 将设置保存到数据库
                oltPerfDao.savePerfStats24Table(newPerfStats24Table);
            }
        } catch (Exception e) {
            throw new RefreshPerfException(e);
        }
    }

    /**
     * 保存轮询周期设置
     * 
     * @param entityId
     *            所属设备ID
     * @param perfStatCycle
     * @throws SavePerfStatCycleException
     */
    @Override
    public void savePerfStatCycle(PerfStatCycle perfStatCycle) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(perfStatCycle.getEntityId());
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());
        try {
            // 将设置提交到设备
            oltPerfFacade.savePerfStatCycle(snmpParam, perfStatCycle);
            // 将设置保存到数据库
            oltPerfDao.savePerfStatCycle(perfStatCycle);
        } catch (Exception e) {
            throw new SavePerfStatCycleException(e);
        }
    }

    /**
     * 保存记录数配置参数
     * 
     * @param entityId
     *            所属设备ID
     * @param perfStatsGlobalSet
     * @throws SavePerfStatsGlobalSetException
     *             保存记录数失败抛出该异常
     */
    @Override
    public void savePerfStatsGlobalSet(PerfStatsGlobalSet perfStatsGlobalSet) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(perfStatsGlobalSet.getEntityId());
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());
        try {
            // 将设置提交到设备
            oltPerfFacade.savePerfStatsGlobalSet(snmpParam, perfStatsGlobalSet);
            // 将设置保存到数据库
            oltPerfDao.savePerfStatsGlobalSet(perfStatsGlobalSet);
        } catch (Exception e) {
            throw new SavePerfStatsGlobalSetException(e);
        }
    }

    /**
     * 从设备上刷新轮询间隔到数据库
     * 
     * @param entityId
     *            所属设备ID
     * @throws RefreshPerfStatCycleException
     *             刷新轮询间隔失败抛出该异常
     */
    @Override
    public void refreshPerfStatCycle(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            PerfStatCycle perfStatCycle = oltPerfFacade.getPerfStatCycle(snmpParam);
            perfStatCycle.setEntityId(entityId);
            // 将设置保存到数据库
            oltPerfDao.savePerfStatCycle(perfStatCycle);
        } catch (Exception e) {
            throw new RefreshPerfStatCycleException(e);
        }
    }

    /**
     * 从设备上刷新历史数据记录数配置到数据库
     * 
     * @param entityId
     *            所属设备ID
     * @throws RefreshPerfStatsGlobalSetException
     *             刷新历史数据记录数配置失败抛出该异常
     */
    @Override
    public void refreshPerfStatsGlobalSet(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());
        // 从设备获取历史数据记录数配置
        try {
            PerfStatsGlobalSet perfStatsGlobalSet = oltPerfFacade.getPerfStatsGlobalSet(snmpParam);
            perfStatsGlobalSet.setEntityId(entityId);
            // 将设置保存到数据库
            oltPerfDao.savePerfStatsGlobalSet(perfStatsGlobalSet);
        } catch (Exception e) {
            throw new RefreshPerfStatsGlobalSetException(e);
        }
    }

    /**
     * 从设备上刷新阈值到数据库
     * 
     * @param entityId
     *            所属设备ID
     * @throws RefreshPerfThresholdException
     *             刷新阈值失败抛出该异常
     */
    @Override
    public void refreshPerfThreshold(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltPerfFacade oltPerfFacade = getOltPerfFacade(snmpParam.getIpAddress());
        try {
            List<OltPerfThreshold> oltPerfThresholds = new ArrayList<OltPerfThreshold>();
            // 从设备获取端口性能阈值
            List<PerfThresholdPort> perfThresholdPorts = oltPerfFacade.getPerfThresholdPortList(snmpParam);
            for (PerfThresholdPort perfThresholdPort : perfThresholdPorts) {
                OltPerfThreshold oltPerfThreshold = OltPerfThreshold.createOltPerfThreshold(perfThresholdPort);
                oltPerfThreshold.setEntityId(entityId);
                oltPerfThresholds.add(oltPerfThreshold);
            }
            // 从设备获取温度阈值
            List<PerfThresholdTemperature> perfThresholdTemperatures = oltPerfFacade
                    .getPerfThresholdTemperatureList(snmpParam);
            for (PerfThresholdTemperature perfThresholdTemperature : perfThresholdTemperatures) {
                OltPerfThreshold oltPerfThreshold = OltPerfThreshold.createOltPerfThreshold(perfThresholdTemperature);
                oltPerfThreshold.setEntityId(entityId);
                oltPerfThresholds.add(oltPerfThreshold);
            }
            // 将设置保存到数据库
            oltPerfDao.saveOltPerfThresholds(entityId, oltPerfThresholds);
        } catch (Exception e) {
            throw new RefreshPerfThresholdException(e);
        }
    }

    /**
     * 获取流量排名前10的Pon口列表
     * 
     * @return
     */
    @Override
    public List<PerfRecord> getTopPonLoading(Map<String, Object> params) {
        return oltPerfDao.getTopPonLoading(params);
    }

    /**
     * 获取Pon速率的记录数
     * 
     * @return List<PerfRecord>
     */
    public Integer getTopPonSpeedCount(Map<String, Object> params) {
        return oltPerfDao.getPonSpeedCount(params);
    }

    /**
     * 获取流量排名前10的Sni口列表
     * 
     * @return
     */
    @Override
    public List<PerfRecord> getTopSniLoading(Map<String, Object> params) {
        return oltPerfDao.getTopSniLoading(params);
    }

    /**
     * 获取SNI速率的记录数
     * 
     * @return List<PerfRecord>
     */
    public Integer getTopSniSpeedCount(Map<String, Object> params) {
        return oltPerfDao.getSniSpeedCount(params);
    }

    /**
     * 获取OltPerfFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltPerfFacade
     */
    private OltPerfFacade getOltPerfFacade(String ip) {
        return facadeFactory.getFacade(ip, OltPerfFacade.class);
    }

    /**
     * 获取OLT设备列表
     */
    @Override
    public List<PerfFolderDevice> getAllOltList() {
        Long oltType = entityTypeService.getOltType();
        return oltPerfDao.getAllOltList(oltType);
    }

    /**
     * 获取OLT设备列表
     */
    @Override
    public List<PerfFolderDevice> getAllOltDeviceList() {
        Long oltType = entityTypeService.getOltType();
        return oltPerfDao.getAllOltDeviceList(oltType);
    }

    /**
     * 获取地域列表
     */
    @Override
    public List<TopoFolder> getAllFolderList() {
        return oltPerfDao.getAllFolderList();
    }

    /**
     * 获取阈值列表
     */
    @Override
    public List<OltPerfThreshold> getDeviceThreshold(Long entityId) {
        return oltPerfDao.getDeviceThreshold(entityId);
    }

    /**
     * 获取所有monitor的name列表 新建监视器时使用，用于检测名字的重复性
     */
    @Override
    public List<String> getMonitorNameList() {
        return oltPerfDao.getMonitorNameList();
    }

    /**
     * 获取collect的PortIndexList
     */
    @Override
    public List<EponStatsRecord> getCollectIndex(Long entityId) {
        return oltPerfDao.getCollectIndex(entityId);
    }

    /**
     * 添加一批采集管理的数据
     */
    @Override
    public void addOltCollector(List<EponStatsRecord> statsRecord) {
        /* for (EponStatsRecord record : statsRecord) {
             SnmpParam snmpParam = entityService.getSnmpParamByEntity(record.getEntityId());
             //此处需要判断是SNI端口还是PON口
             OltSniAttribute oltSniAttribute = oltDao.getSniAttribute(record.getEntityId(), record.getPortIndex());
             if (oltSniAttribute != null) {
                 Integer newStatus = getOltControlFacade(snmpParam.getIpAddress()).setSni15MinPerfStatus(snmpParam,
                         record.getPortIndex(), record.getCollector());
                 if (newStatus == null) {
                     throw new SetValueConflictException("Business.connection");
                 } else {
                     Long sniId = oltDao.getSniIdByIndex(record.getPortIndex(), record.getEntityId());
                     oltDao.updateSni15MinPerfStatus(sniId, newStatus);
                     if (!newStatus.equals(record.getCollector())) {
                         throw new SetValueConflictException("Business.setSni15MinPerfStatus");
                     }
                 }
             } else {
                 Integer newStatus = getOltControlFacade(snmpParam.getIpAddress()).setPon15MinPerfStatus(snmpParam,
                         record.getPortIndex(), record.getCollector());
                 if (newStatus == null) {
                     throw new SetValueConflictException("Business.connection");
                 } else {
                     Long ponId = oltDao.getPonIdByPonIndex(record.getPortIndex(), record.getEntityId());
                     oltDao.updatePon15MinPerfStatus(ponId, newStatus);
                     if (!newStatus.equals(record.getCollector())) {
                         throw new SetValueConflictException("Business.setPon15MinPerfStatus");
                     }
                 }
             }
         }*/
        oltPerfDao.addOltCollector(statsRecord);
    }

    /**
     * 修改一批采集管理的数据
     */
    @Override
    public void updateOltCollector(List<EponStatsRecord> statsRecord) {
        oltPerfDao.updateOltCollector(statsRecord);
    }

    /**
     * 删除一批采集管理的数据
     */
    @Override
    public void delOltCollector(Long entityId, List<Long> portIndex) {
        oltPerfDao.delOltCollector(entityId, portIndex);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltPerfService#getMonitorInfo()
     */
    @Override
    public List<MonitorBaseInfo> getMonitorInfo(String monitorName, String ip, String name) {
        return oltPerfDao.getMonitorInfo(monitorName, ip, name);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltPerfService#addPortMonitor(com.topvision.ems.epon.domain.MonitorBaseInfo)
     */
    @Override
    public void addMonitor(MonitorBaseInfo monitorInfo) {
        oltPerfDao.addMonitor(monitorInfo);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.service.OltPerfService#deleteMonitor(java.lang.Integer)
     */
    @Override
    public void deleteMonitor(Long monitorId) {
        oltPerfDao.deleteMonitor(monitorId);
    }

    @Override
    public List<MonitorPerforamnceIndex> loadMonitorIndexData(String index, List<String> ports) {
        StringBuffer sb = new StringBuffer();
        sb.append("select B.ip as entityIp, A.entityId,  A.portIndex ,A.cur").append(index)
                .append(" as value from eponcurrentstats A,entity B where 1=0");
        for (String port : ports) {
            String[] array = port.split("-");
            sb.append(" union all select B.ip as entityIp, A.entityId,  A.portIndex ,A.cur")
                    .append(index)
                    .append(" as value from eponcurrentstats A,entity B where curStatsTime > DATE_SUB(now(), INTERVAL 1 MINUTE) AND A.entityId=");
            // 第一个元素为设备ID
            sb.append(Long.parseLong(array[0]));
            // 关联ENTITY表
            sb.append(" and A.entityId = B.entityId");
            // 第2个元素开始为端口INDEX
            sb.append(" and A.portIndex in (");
            StringBuffer portsString = new StringBuffer();
            for (int i = 2; i < array.length; i++) {
                portsString.append(Long.parseLong(array[i]));
                if (i != array.length - 1) {
                    portsString.append(",");
                }
            }
            sb.append(portsString.toString());
            sb.append(")");
        }
        String sql = sb.toString();
        List<MonitorPerforamnceIndex> result = oltPerfDao.loadMonitorIndexData(sql);
        if (0 == result.size()) {
            for (String port : ports) {
                String[] array = port.split("-");
                for (int i = 2; i < array.length; i++) {
                    MonitorPerforamnceIndex o = new MonitorPerforamnceIndex();
                    o.setEntityIp(array[1]);
                    o.setEntityId(Long.parseLong(array[0]));
                    o.setPortIndex(Long.parseLong(array[i]));
                    o.setValue(0L);
                    result.add(o);
                }
            }
        }
        return result;
    }

    @Override
    public List<HistoryPerforamnceIndex> loadHistory(Long entityId, Long portIndex, String monitorIndex, String head,
            String startTime, String endTime) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select B.ip,B.entityId,A.").append(head).append(monitorIndex).append(" as value ,").append(head)
                .append("EndTime as collectTime");
        buffer.append(" from entity B, perf").append(head).append("table as A");
        buffer.append(" where A.entityId = B.entityId and A.entityId = ").append(entityId).append(" and portIndex = ")
                .append(portIndex);
        buffer.append(" and ").append(head).append("EndTime between '").append(startTime).append("' and '")
                .append(endTime).append("'");
        String sql = buffer.toString();
        return oltPerfDao.loadHistory(sql);
    }

    @Override
    public List<HistoryPerforamnceIndex> loadHistory24(long entityId, long portIndex, String monitorIndex, String head,
            String startTime, String endTime) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select B.ip,B.entityId,A.").append(head).append(monitorIndex).append(" as value ,").append(head)
                .append("EndTime as collectTime");
        buffer.append(" from entity B, perf").append(head).append("table as A");
        buffer.append(" where A.entityId = B.entityId and A.entityId = ").append(entityId).append(" and portIndex = ")
                .append(portIndex);
        buffer.append(" and ").append(head).append("EndTime between '").append(startTime).append("' and '")
                .append(endTime).append("'");
        String sql = buffer.toString();
        return oltPerfDao.loadHistory(sql);
    }

    @Override
    public List<Long> getUserFolderIdList() {
        return oltPerfDao.queryUserFolderIdList();
    }

    @Override
    public List<PerfRecord> getDeviceSniLoading(Long entityId) {
        List<PerfRecord> perfRecords = oltPerfDao.getDeviceSniLoading(entityId);
        for (PerfRecord pr : perfRecords) {
            if (pr.getValue() == null || pr.getValue() < 0) {
                pr.setValue(0d);
            }
            if (pr.getTempValue() == null || pr.getTempValue() < 0) {
                pr.setTempValue(0d);
            }
        }
        return perfRecords;
    }

    @Override
    public List<PerfRecord> getDevicePonLoading(Long entityId) {
        List<PerfRecord> perfRecords = oltPerfDao.getDevicePonLoading(entityId);
        for (PerfRecord pr : perfRecords) {
            if (pr.getValue() == null || pr.getValue() < 0) {
                pr.setValue(0d);
            }
            if (pr.getTempValue() == null || pr.getTempValue() < 0) {
                pr.setTempValue(0d);
            }
        }
        return perfRecords;
    }
}
