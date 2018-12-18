/***********************************************************************
 * $ PerformanceServiceImpl.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.connectivity.service.ConnectivityService;
import com.topvision.ems.facade.callback.PerformanceCallback;
import com.topvision.ems.facade.domain.ConnectivityStrategy;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceConstants;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.facade.performance.PerformanceFacade;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.dao.PerformanceDao;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.ems.performance.domain.PerfTaskUpdateInfo;
import com.topvision.ems.performance.domain.PingPerf;
import com.topvision.ems.performance.domain.ViewerParam;
import com.topvision.ems.performance.service.PerfTargetData;
import com.topvision.ems.performance.service.PerfTargetService;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.ems.performance.service.PerformanceStatistics;
import com.topvision.ems.performance.service.Viewer;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.exception.engine.NoSuchScheduleException;
import com.topvision.exception.service.NoSuchMonitorException;
import com.topvision.exception.service.WrongPerfViewerParamException;
import com.topvision.framework.common.CollectTimeUtil;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.ServerBeanFactory;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EmsEventObject;
import com.topvision.platform.message.event.EngineServerEvent;
import com.topvision.platform.message.event.EngineServerListener;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.PerformanceShiftEvent;
import com.topvision.platform.message.event.PerformanceShiftListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.EngineServerService;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@Service("performanceService")
public class PerformanceServiceImpl extends BaseService implements PerformanceService<OperClass>, PerformanceCallback,
        EntityListener, EngineServerListener, PerformanceShiftListener {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private PerfTargetService perfTargetService;
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;
    @Autowired
    private PerformanceDao performanceDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ServerBeanFactory beanFactory;
    @Autowired
    private PerformanceStatistics performanceStatistics;
    @Autowired
    private DeviceVersionService deviceVersionService;
    @Autowired
    private EntityDao entityDao;
    private final Map<String, Viewer> viewerMap = Collections.synchronizedMap(new HashMap<String, Viewer>());
    @Autowired
    private EngineServerService engineServerService;
    @Autowired
    private ConnectivityService connectivityService;
    @Value("${engine.check.interval:60000}")
    private Long engineCheckInterval;

    private static final String PING = "pingPerf";
    // Key - engineId, Value - messageId
    private Map<Integer, Set<Long>> messageIdCache;

    @Override
    public void initialize() {
        messageService.addListener(EngineServerListener.class, this);
        messageService.addListener(PerformanceShiftListener.class, this);
        messageIdCache = new HashMap<>();
    }

    @Override
    public void destroy() {
        messageIdCache.clear();
        messageIdCache = null;
        messageService.removeListener(EngineServerListener.class, this);
        messageService.removeListener(PerformanceShiftListener.class, this);
    }

    @Override
    public void start() {
        while (true) {
            if (!engineServerService.isAllEngineStart()) {
                try {
                    Thread.sleep(engineCheckInterval);
                } catch (InterruptedException e) {
                }
                continue;
            } else {
                break;
            }
        }
        logger.info("Performance monitor begin to start...");
        List<SystemPreferences> systemPreferencesList = systemPreferencesDao.selectByModule("CollectTimeConfig");
        for (SystemPreferences systemPreference : systemPreferencesList) {
            modifyCollectTimeUtil(systemPreference.getName(), System.currentTimeMillis(),
                    Long.parseLong(systemPreference.getValue()));
        }
        // Clean Redundancy PerfMonitor
        performanceDao.cleanRedundancyPerfMonitor();
        // Start PerfMonitor
        List<ScheduleMessage<OperClass>> scheduleMessageList = performanceDao.selectByMap(null);
        // Modify by Victor@20170613把性能任务按周期进行分组，然后每组在周期中平均（用延时实现）
        // 第一步，进行分组
        Map<Long, List<ScheduleMessage<OperClass>>> groups = new HashMap<>();
        for (ScheduleMessage<OperClass> sm : scheduleMessageList) {
            List<ScheduleMessage<OperClass>> list = groups.get(sm.getPeriod());
            if (list == null) {
                list = new ArrayList<>();
                groups.put(sm.getPeriod(), list);
            }
            list.add(sm);
        }
        // 第二步，根据分组大小进行延时设置并发送engine
        for (Long period : groups.keySet()) {
            List<ScheduleMessage<OperClass>> list = groups.get(period);
            // 平均间隔
            int average = (int) (period / list.size());
            int delay = 0;
            for (ScheduleMessage<OperClass> scheduleMessage : list) {
                // PerfEngineSaver perfService = (PerfEngineSaver)
                // beanFactory.getBean(scheduleMessage.getDomain().getPerfService());
                // perfService.setUp(scheduleMessage);
                try {
                    scheduleMessage.setAction(ScheduleMessage.START);
                    scheduleMessage.setInitialDelay(delay);
                    invoke(scheduleMessage);
                    delay += average;
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
        logger.info("Performance monitor all send to engine started!");
    }

    @Override
    public SnmpParam getSnmpParamByEntity(Long entityId) {
        return entityService.getSnmpParamByEntity(entityId); // To change body of implemented
                                                             // methods use File | Settings | File
                                                             // Templates.
    }

    @Override
    public List<ConnectivityStrategy> getUsingConnectivityStrategy() {
        return connectivityService.getUsingConnectivityStrategy();
    }

    @Override
    public void test() {
    }

    @Override
    public void reStartMonitor(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取对应设备的所有的perfmonitor
        List<ScheduleMessage<OperClass>> scheduleList = performanceDao.queryScheduleByEntityId(entityId);
        // 重启所有开启的perfmonitor
        for(ScheduleMessage<OperClass> scheduleMessage : scheduleList) {
            try{
                reStartMonitor(snmpParam, scheduleMessage.getMonitorId(), scheduleMessage.getPeriod());
            }catch(Exception e) {
                logger.error("", e);
            }
        }
    }

    /**
     * 重启监视器，同时支持重设轮询间隔
     * 
     * @param snmpParam
     *            snmp采集参数
     * @param monitorId
     *            监视器ID
     * @param period
     *            调度轮询周期时间
     */
    @Override
    public void reStartMonitor(SnmpParam snmpParam, Long monitorId, Long period) {
        ScheduleMessage<OperClass> scheduleMessage = performanceDao.selectByPrimaryKey(monitorId);
        if (scheduleMessage != null) {
            scheduleMessage.setSnmpParam(snmpParam);
            scheduleMessage.setPeriod(period);
            scheduleMessage.setAction(ScheduleMessage.RESTART);
            invoke(scheduleMessage);
            performanceDao.updateEntity(scheduleMessage);
        } else {
            throw new NoSuchMonitorException();
        }
    }

    /**
     * 启动一个采集调度
     * 
     * @param snmpParam
     *            snmp采集参数
     * @param domain
     *            调度需要采集的数据描述类
     * @param initialDelay
     *            初始启动间隔时间
     * @param period
     *            调度轮询周期时间
     * @return 该monitor的id
     */
    @Override
    public long startMonitor(SnmpParam snmpParam, OperClass domain, Long initialDelay, Long period,
            Integer scheduleType, Integer isStartUpWithServer) {
        ScheduleMessage<OperClass> scheduleMessage = new ScheduleMessage<OperClass>();
        scheduleMessage.setSnmpParam(snmpParam);
        scheduleMessage.setDomain(domain);
        scheduleMessage.setInitialDelay(initialDelay);
        scheduleMessage.setPeriod(period);
        scheduleMessage.setIdentifyKey(domain.getIdentifyKey());
        scheduleMessage.setCategory(domain.getCategory());
        scheduleMessage.setAction(ScheduleMessage.START);
        scheduleMessage.setScheduleType(scheduleType);
        scheduleMessage.setIsStartUpWithServer(isStartUpWithServer);
        performanceDao.insertEntity(scheduleMessage);
        try {
            invoke(scheduleMessage);
        } catch (Exception e) {
            logger.error("", e);
            performanceDao.deleteByPrimaryKey(scheduleMessage.getMonitorId());
        }
        return scheduleMessage.getMonitorId();
    }

    @Override
    public void updateMonitor(SnmpParam snmpParam, Long monitorId, OperClass domain, Integer scheduleType,
            Integer type) {
        ScheduleMessage<OperClass> scheduleMessage = performanceDao.selectByPrimaryKey(monitorId);
        // 这里的type取值为SchedulerMessage.Insert 或者 是 SchedulerMessage.Delete
        scheduleMessage.setAction(type);
        scheduleMessage.setDomain(domain);
        scheduleMessage.setScheduleType(scheduleType);
        // performanceDao.updateEntity(scheduleMessage);
        scheduleMessage.setSnmpParam(snmpParam);
        invoke(scheduleMessage);
    }

    @Override
    public void registViewer(Viewer viewer) {
        viewerMap.put(viewer.getViewerType(), viewer);
    }

    @Override
    public Viewer getViewerByType(ViewerParam viewerParam) {
        if (viewerMap.containsKey(viewerParam.getPerfType())) {
            return viewerMap.get(viewerParam.getPerfType());
        } else {
            throw new WrongPerfViewerParamException("type error");
        }
    }

    /**
     * 停止一个采集调度
     * 
     * @param monitorId
     *            监视器ID
     */
    @Override
    public void stopMonitor(SnmpParam snmpParam, Long monitorId) {
        ScheduleMessage<OperClass> scheduleMessage = performanceDao.selectByPrimaryKey(monitorId);
        scheduleMessage.setSnmpParam(snmpParam);
        scheduleMessage.setAction(ScheduleMessage.STOP);
        invoke(scheduleMessage);
        performanceDao.deleteByPrimaryKey(monitorId);
    }

    private Integer getEngineIdByCache(Long messageId) {
        for (Entry<Integer, Set<Long>> entry : messageIdCache.entrySet()) {
            Set<Long> tmp = entry.getValue();
            if (tmp.contains(messageId)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void invoke(ScheduleMessage<OperClass> message) {
        if (message.getSnmpParam() == null || message.getSnmpParam().getIpAddress() == null) {
            message.setSnmpParam(getSnmpParamByEntity(message.getIdentifyKey()));
        }
        PerformanceFacade facade = null;
        Long messageId = message.getMonitorId();
        Integer engineId = getEngineIdByCache(messageId);
        if (engineId != null) {
            EngineServer engineServer = engineServerService.getEngineServerById(engineId);
            facade = facadeFactory.getFacade(engineServer, PerformanceFacade.class);
        } else {
            facade = facadeFactory.getPerfFacade(PerformanceFacade.class);
        }
        try {
            engineId = facade.invoke(message);
            syncMessageIdCache(engineId, messageId);
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    @Override
    public void performanceShift(PerformanceShiftEvent event) {
        Integer engineId = event.getEngineId();
        restartMonitor(engineId);
    }

    @Override
    public void restartMonitor(Integer engineId) {
        if (messageIdCache.get(engineId) == null || messageIdCache.get(engineId).isEmpty()) {
            return;
        }

        // 所有采集端重新设置时间
        List<SystemPreferences> systemPreferencesList = systemPreferencesDao.selectByModule("CollectTimeConfig");
        for (SystemPreferences systemPreference : systemPreferencesList) {
            modifyCollectTimeUtil(systemPreference.getName(), System.currentTimeMillis(),
                    Long.parseLong(systemPreference.getValue()));
        }
        EngineServer es = engineServerService.getEngineServerById(engineId);
        PerformanceFacade facade = facadeFactory.getFacade(es, PerformanceFacade.class);
        reAllocatePerfMonitor(facade, engineId);
    }

    /**
     * Sync Cache
     * 
     * @param engineId
     * @param messageId
     */
    private void syncMessageIdCache(Integer engineId, Long messageId) {
        Set<Long> messageIdList = messageIdCache.get(engineId);
        if (messageIdList == null) {
            messageIdList = new HashSet<Long>();
            messageIdCache.put(engineId, messageIdList);
        }
        messageIdList.add(messageId);
    }

    @Override
    public ScheduleMessage<OperClass> getMonitorByIdentifyAndCategory(Long identify, String category) {
        return performanceDao.selectByIdentifyAndCategory(identify, category);
    }

    @Override
    public void recordTaskCollectTime(Long monitorId, Timestamp scheduleRunTime) {
        performanceDao.updateScheduleTaskRunTime(monitorId, scheduleRunTime);
    }

    @Override
    public synchronized void startPingMonitor(SnmpParam snmpParam, PingPerf pingPerf) {
        pingPerf.setIpAddress(snmpParam.getIpAddress());
        startMonitor(snmpParam, pingPerf, 0L, 12000L, PerformanceConstants.PERFORMANCE_DOMAIN,
                PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
    }

    @Override
    public void entityAdded(EntityEvent event) {
        Entity entity = event.getEntity();
        if (entity != null && !(entityTypeService.isUnkownType(entity.getTypeId()))) {
            if (performanceDao.selectByIdentifyAndCategory(entity.getEntityId(), PING) == null) {
                PingPerf pingPerf = (PingPerf) beanFactory.getBean(PING);
                pingPerf.setEntityId(entity.getEntityId());
                pingPerf.setIpAddress(entity.getIp());
                // Add Config Time
                startMonitor(new SnmpParam(), pingPerf, 0L, 15 * 60000L, PerformanceConstants.PERFORMANCE_DOMAIN,
                        PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
            }
        }
    }

    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    @Override
    public void entityChanged(EntityEvent event) {
    }

    @Override
    public void entityRemoved(EntityEvent event) {
        Entity entity = event.getEntity();
        if (entity != null && !(entityTypeService.isUnkownType(entity.getTypeId()))) {
            ScheduleMessage<OperClass> scheduleMessage = performanceDao
                    .selectByIdentifyAndCategory(entity.getEntityId(), PING);
            if (scheduleMessage != null) {
                SnmpParam snmpParam = new SnmpParam();
                snmpParam.setIpAddress(entity.getIp());
                stopMonitor(snmpParam, scheduleMessage.getMonitorId());
            }
        }
    }

    @Override
    public void managerChanged(EntityEvent event) {
    }

    @Override
    public void updatePerformanceTask(List<ScheduleMessage<OperClass>> messages,
            List<PerfTaskUpdateInfo> perfTaskUpdateInfos) {
        if (perfTaskUpdateInfos == null || perfTaskUpdateInfos.size() == 0) {
            return;
        }
        // List<ScheduleMessage<OperClass>> allMessages = performanceDao.selectByMap(null);
        // 如果是全部指标都关闭时重新开启的情况,messages(原来的采集器列表)有可能为空
        List<ScheduleMessage<OperClass>> allMessages = new ArrayList<>();
        if (messages != null) {
            allMessages = messages;
        }
        // 可以通过在Domain中给定默认值实现
        // initScheduleMessage(allMessages);
        List<ScheduleMessage<OperClass>> engineMessages = new ArrayList<>();
        for (PerfTaskUpdateInfo updateInfo : perfTaskUpdateInfos) {
            long identifyKey = updateInfo.getIdentifyKey();
            String category = updateInfo.getCategory();
            long lastInterval = updateInfo.getLastInterval();
            long newInterval = updateInfo.getNewInterval();
            String targetName = updateInfo.getTargetName();
            Object data = updateInfo.getData();
            SnmpParam snmpParam = updateInfo.getSnmpParam();
            if (lastInterval != 0) {
                ScheduleMessage<OperClass> lastScheduleMessage = getScheduleMessage(allMessages, category, identifyKey,
                        lastInterval * 60 * 1000);
                if (lastScheduleMessage != null) {
                    lastScheduleMessage.getDomain().shutdownTarget(targetName, data);
                    lastScheduleMessage.setUpdateFlag(true);
                    lastScheduleMessage.setAction(ScheduleMessage.UPDATE_SHUTDOWN);
                    ScheduleMessage<OperClass> update_Shutdown_Message = new ScheduleMessage<>(category, identifyKey,
                            lastInterval * 60 * 1000);
                    update_Shutdown_Message.setMonitorId(lastScheduleMessage.getMonitorId());
                    update_Shutdown_Message.setTargetName(targetName);
                    update_Shutdown_Message.setData(data);
                    update_Shutdown_Message.setDomain(lastScheduleMessage.getDomain());
                    update_Shutdown_Message.setAction(ScheduleMessage.UPDATE_SHUTDOWN);
                    // 更新使用原先的SNMP参数
                    update_Shutdown_Message.setSnmpParam(lastScheduleMessage.getSnmpParam());
                    engineMessages.add(update_Shutdown_Message);
                }
            }
            if (newInterval != 0) {
                // 新建修改后指标+时间
                // ScheduleMessage newScheduleMessage =
                // performanceDao.selectByIdentifyAndCategoryAndPeriod(identifyKey,
                // category, newInterval * 60 * 1000);
                ScheduleMessage<OperClass> newScheduleMessage = getScheduleMessage(allMessages, category, identifyKey,
                        newInterval * 60 * 1000);

                if (newScheduleMessage != null) {
                    // 存在相同时间周期的任务
                    newScheduleMessage.getDomain().startUpTarget(targetName, data);
                    newScheduleMessage.setUpdateFlag(true);
                    ScheduleMessage<OperClass> update_StartUp_Message = new ScheduleMessage<>(category, identifyKey,
                            newInterval * 60 * 1000);
                    update_StartUp_Message.setMonitorId(newScheduleMessage.getMonitorId());
                    update_StartUp_Message.setTargetName(targetName);
                    update_StartUp_Message.setData(data);
                    update_StartUp_Message.setDomain(newScheduleMessage.getDomain());
                    if (!(newScheduleMessage.getAction() == ScheduleMessage.START)) {
                        newScheduleMessage.setAction(ScheduleMessage.UPDATE_START);
                        update_StartUp_Message.setAction(ScheduleMessage.UPDATE_START);
                        // 更新使用原先的SNMP参数
                        update_StartUp_Message.setSnmpParam(newScheduleMessage.getSnmpParam());
                        engineMessages.add(update_StartUp_Message);
                    }
                } else {
                    // 需要新建任务
                    newScheduleMessage = new ScheduleMessage<OperClass>();
                    OperClass operClass = (OperClass) beanFactory.getBean(category);
                    operClass.setIdentifyKey(identifyKey);
                    operClass.setDeviceTypeId(updateInfo.getTypeId());
                    operClass.startUpTarget(targetName, data);
                    // 新建使用传入的SNMP参数
                    newScheduleMessage.setSnmpParam(snmpParam);
                    newScheduleMessage.setDomain(operClass);
                    newScheduleMessage.setInitialDelay(newInterval * 60000);
                    newScheduleMessage.setPeriod(newInterval * 60000);
                    newScheduleMessage.setIdentifyKey(operClass.getIdentifyKey());
                    newScheduleMessage.setCategory(operClass.getCategory());
                    newScheduleMessage.setAction(ScheduleMessage.START);
                    newScheduleMessage.setScheduleType(PerformanceConstants.PERFORMANCE_DOMAIN);
                    newScheduleMessage.setIsStartUpWithServer(PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
                    allMessages.add(newScheduleMessage);
                    engineMessages.add(newScheduleMessage);
                }
            }
        }
        // Server 修改数据库
        for (ScheduleMessage<OperClass> daoTmp : allMessages) {
            // 需要新开启的任务不在这里进行处理
            if (daoTmp.getAction() == ScheduleMessage.START) {
                continue;
            }
            if (daoTmp.isUpdateFlag()) {
                if (daoTmp.getDomain().isTaskCancle()) {
                    daoTmp.setAction(ScheduleMessage.STOP);
                }
                performanceDao.updateScheduleDomain(daoTmp);
            }
        }
        // Engine 修改采集任务
        List<ScheduleMessage<OperClass>> stopTasks = new ArrayList<>();
        Iterator<ScheduleMessage<OperClass>> iterator = engineMessages.iterator();
        while (iterator.hasNext()) {
            ScheduleMessage<OperClass> engineTask = iterator.next();
            SnmpParam snmpParam = engineTask.getSnmpParam();
            if (engineTask.getAction() == ScheduleMessage.UPDATE_SHUTDOWN) {

                if (!stopTasks.contains(engineTask)) {
                    if (engineTask.getDomain().isTaskCancle()) {
                        engineTask.setAction(ScheduleMessage.STOP);
                        stopTasks.add(engineTask);
                    }
                    invoke(engineTask);
                } else {
                    // DO NOTHING
                }
            } else if (engineTask.getAction() == ScheduleMessage.START) {
                startMonitor(snmpParam, engineTask.getDomain(), engineTask.getInitialDelay(), engineTask.getPeriod(),
                        engineTask.getScheduleType(), engineTask.getIsStartUpWithServer());
            } else {
                invoke(engineTask);
            }

        }
    }

    private ScheduleMessage<OperClass> getScheduleMessage(List<ScheduleMessage<OperClass>> scheduleMessages,
            String category, Long identifyKey, Long period) {
        ScheduleMessage<OperClass> compareMessage = new ScheduleMessage<OperClass>(category, identifyKey, period);
        for (ScheduleMessage<OperClass> tmp : scheduleMessages) {
            if (tmp.equals(compareMessage)) {
                return tmp;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    private void initScheduleMessage(List<ScheduleMessage<OperClass>> scheduleMessages) {
        for (ScheduleMessage<OperClass> tmp : scheduleMessages) {
            tmp.setAction(ScheduleMessage.INIT);
            tmp.setUpdateFlag(false);
        }
    }

    @Override
    public Object getModifyTargetData(Long entityId, String targetName, Long typeId) {
        String beanName = perfTargetService.getPerfTargetCategory(targetName, typeId);
        if (beanName != null) {
            PerfTargetData perfTargetData = (PerfTargetData) beanFactory.getBean(beanName);
            return perfTargetData.perfTargetChangeData(entityId, targetName);
        }
        return null;
    }

    @Override
    public Object getModifyTargetDataByType(Long entityId, String targetName, Long type) {
        String beanName = null;
        if (entityTypeService.isOlt(type)) {
            beanName = PerfTargetConstants.PERF_OLT_SERVICE;
        } else if (entityTypeService.isCcmts(type)) {
            beanName = PerfTargetConstants.PERF_CCMTS_SERVICE;
        } else if (entityTypeService.isCmts(type)) {
            beanName = PerfTargetConstants.PERF_CMTS_SERVICE;
        } else if (entityTypeService.isOnu(type))
            beanName = PerfTargetConstants.PERF_ONU_SERVICE;
        if (beanName != null) {
            PerfTargetData perfTargetData = (PerfTargetData) beanFactory.getBean(beanName);
            return perfTargetData.perfTargetChangeData(entityId, targetName);
        }
        return null;
    }

    @Override
    public void modifyCollectTimeUtil(String name, long startTime, Long period) {
        CollectTimeUtil.createCollectTimeUtil(name, startTime, period);
        // Modify by Victor@20151217需要调用所有采集端
        for (EngineServer es : facadeFactory.getAllEngineServer()) {
            if (es.hasType(EngineServer.TYPE_PERFORMANCE)) {
                try {
                    facadeFactory.getFacade(es, PerformanceFacade.class).createCollectTimeUtil(name, startTime, period);
                } catch (Exception e) {
                    logger.warn("", e);
                }
            }
        }
    }

    public Boolean isCcmts(Long entityId) {
        Entity entity = entityDao.selectByPrimaryKey(entityId);
        Long typeId = -1L;
        if (entity != null) {
            typeId = entity.getTypeId();
        }
        return entityTypeService.isCcmtsWithAgent(typeId);
    }

    @Override
    public Boolean isOlt(Long entityId) {
        Entity entity = entityDao.selectByPrimaryKey(entityId);
        Long typeId = -1L;
        if (entity != null) {
            typeId = entity.getTypeId();
        }
        return entityTypeService.isOlt(typeId);
    }

    @Override
    public Boolean isFunctionSupported(Long entityId, String func) {
        return deviceVersionService.isFunctionSupported(entityId,
                func);
    }

    @Override
    public void sendPerfomaceResult(PerformanceData result) {
        performanceStatistics.sendPerfomaceResult(result);
    }

    @Override
    public void sendPerfomaceResult(List<PerformanceData> result) {
        performanceStatistics.sendPerfomaceResult(result);
    }

    @Override
    public void sendRealtimePerfomaceResult(PerformanceData result) {
        performanceStatistics.sendRealtimePerfomaceResult(result);
    }

    @Override
    public void sendRealtimePerfomaceResult(List<PerformanceData> result) {
        performanceStatistics.sendRealtimePerfomaceResult(result);
    }

    @Override
    public Integer getCmtsSampleCollect(Long typeId) {
        // 内蒙增加CMT速率采样 SampleCollect Config
        List<SystemPreferences> list = systemPreferencesDao.selectByModule("cmtsFlowCollect");
        String flowCollectType = null;
        Integer sampleCollect = null;
        for (SystemPreferences pref : list) {
            if ("flowCollectType".equals(pref.getName())) {
                flowCollectType = pref.getValue();
            } else if ("sampleInterval".equals(pref.getName())) {
                sampleCollect = Integer.parseInt(pref.getValue());
            }
        }
        try {
            if (flowCollectType.indexOf(String.valueOf((typeId))) != -1) {
                return sampleCollect;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public <T extends EventObject> void addServerMessage(T event) {
        messageService.addMessage((EmsEventObject<?>) event);
    }

    @Override
    public void updateScheduleModifyTime(Long identify, String category) {
        performanceDao.updateScheduleModifyTime(identify, category);
    }

    @Override
    public void updateScheduleCollectTime(Long monitorId, Long time) {
        performanceDao.updateScheduleCollectTime(monitorId, time);
    }

    @Override
    public Boolean isCmts(Long cmcId) {
        Long typeId = entityDao.selectByPrimaryKey(cmcId).getTypeId();
        return entityTypeService.isCmts(typeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.platform.message.event.EngineServerListener#statusChanged(com.topvision.
     * platform.message.event.EngineServerEvent)
     */
    @Override
    public void statusChanged(EngineServerEvent event) {
        EngineServer engineServer = event.getEngineServer();
        Integer engineId = Integer.parseInt(engineServer.getEngineId());
        if (event.getStatus() == EngineServerEvent.STATUS_DISABLED
                || event.getStatus() == EngineServerEvent.STATUS_REMOVED) {
            // Re allocate perf task
            List<SystemPreferences> systemPreferencesList = systemPreferencesDao.selectByModule("CollectTimeConfig");
            for (SystemPreferences systemPreference : systemPreferencesList) {
                modifyCollectTimeUtil(systemPreference.getName(), System.currentTimeMillis(),
                        Long.parseLong(systemPreference.getValue()));
            }
            PerformanceFacade facade = facadeFactory.getFacade(PerformanceFacade.class);
            reAllocatePerfMonitor(facade, engineId);
        }
    }

    /**
     * Re Allocate Perf Monitor
     * 
     * 
     * @param facade
     * @param engineId
     */
    private void reAllocatePerfMonitor(PerformanceFacade facade, Integer engineId) {
        Set<Long> messageIdList = messageIdCache.get(engineId);
        Set<Long> reAllocateList = new HashSet<>();
        if (messageIdList == null) {
            return;
        }
        messageIdCache.remove(engineId);
        for (Long messageId : messageIdList) {
            try {
                ScheduleMessage<OperClass> scheduleMessage = performanceDao.selectByPrimaryKey(messageId);
                if (scheduleMessage != null) {
                    scheduleMessage.setAction(ScheduleMessage.START);
                    Integer new_engineId = facade.invoke(scheduleMessage);
                    syncMessageIdCache(new_engineId, messageId);
                }
            } catch (NoSuchScheduleException e) {
            } catch (Exception e) {
                reAllocateList.add(messageId);
                logger.info("reAllocatePerfMonitor error", e);
            }
        }
        if (reAllocateList.size() > 0) {
            // Re Allocate Task (When Delete Facade Type contains Performance)
            PerformanceFacade reFacade = facadeFactory.getFacade(PerformanceFacade.class);
            for (Long messageId : reAllocateList) {
                try {
                    ScheduleMessage<OperClass> scheduleMessage = performanceDao.selectByPrimaryKey(messageId);
                    if (scheduleMessage != null) {
                        scheduleMessage.setAction(ScheduleMessage.START);
                        Integer new_engineId = reFacade.invoke(scheduleMessage);
                        syncMessageIdCache(new_engineId, messageId);
                    }
                } catch (NoSuchScheduleException e) {
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.service.PerformanceService#getMessageIdCache()
     */
    @Override
    public Map<Integer, Set<Long>> getMessageIdCache() {
        return messageIdCache;
    }

    @Override
    public void entityReplaced(EntityEvent event) {
        // 无需重建perfmonitor，perfmonitor执行时会回调取snmpparam，重置缓存即可
        //reStartMonitor(event.getEntity().getEntityId());
    }

}