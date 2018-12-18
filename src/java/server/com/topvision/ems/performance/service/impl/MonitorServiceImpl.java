package com.topvision.ems.performance.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.TopologyResult;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.LinkDao;
import com.topvision.ems.network.dao.PortPerfDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.Port;
import com.topvision.ems.network.domain.PortPerf;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.OnlineService;
import com.topvision.ems.performance.dao.MonitorDao;
import com.topvision.ems.performance.domain.Monitor;
import com.topvision.ems.performance.domain.MonitorValue;
import com.topvision.ems.performance.domain.Threshold;
import com.topvision.ems.performance.message.MonitorEvent;
import com.topvision.ems.performance.message.MonitorListener;
import com.topvision.ems.performance.service.MonitorService;
import com.topvision.exception.service.NetworkException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.event.FlowEvent;
import com.topvision.platform.message.event.FlowListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SchedulerService;

@Service("monitorService")
public class MonitorServiceImpl extends BaseService implements MonitorService, EntityListener, MonitorListener,
        BeanFactoryAware {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public static final String PING_MONITOR = "ping";
    public static final String FLOW_MONITOR = "flow";
    @Autowired
    private MonitorDao monitorDao;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private PortPerfDao portPerfDao;
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private OnlineService onlineService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private SchedulerService schedulerService;
    private Map<Long, JobDetail> monitorJobs;
    @Autowired
    private FacadeFactory facadeFactory;
    @Value("${OltMonitorJob.telnetFilter:\"false\"}")
    private String telnetFilter;
    @Value("${performance.monitorStartTotal:300000}")
    private int monitorStartTotal;
    // 为了防止相同linkId在连续发送流量的暂时解决办法
    Map<Long, Long> linkFlow = new HashMap<Long, Long>();
    private BeanFactory beanFactory;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org
     * .springframework.beans .factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#addMonitor(com.topvision.ems.network.domain.Monitor)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Boolean addMonitor(Monitor dm) {
        try {
            // modify by YangYi@20140318
            String identity = dm.getIp(); // 原有的方式保持不变
            if (dm.getIp() == null || dm.getIp().length() == 0) { // 当无法取得IP时，使用entityId作为identity
                identity = String.valueOf(dm.getEntityId());
            }
            JobDetail job = newJob((Class<Job>) Class.forName(dm.getJobClass())).withIdentity(identity,
                    "Monitor." + dm.getCategory()).build();
            job.getJobDataMap().put("facadeFactory", facadeFactory);
            job.getJobDataMap().put("monitor", dm);
            job.getJobDataMap().put("onlineService", onlineService);
            job.getJobDataMap().put("monitorService", this);
            job.getJobDataMap().put("beanFactory", beanFactory);
            job.getJobDataMap().put("messageService", messageService);
            job.getJobDataMap().put("entityService", entityService);
            // add by victor@20131225增加通过参数控制是否开启telnet采集
            job.getJobDataMap().put("telnetFilter", telnetFilter);
            monitorJobs.put(dm.getMonitorId(), job);
            Long intervalTime = 0L;
            if (dm.isHealthy()) {
                intervalTime = dm.getIntervalOfNormal();
            } else {
                intervalTime = dm.getIntervalAfterError();
            }

            TriggerBuilder<SimpleTrigger> builder = newTrigger().withIdentity(job.getKey().getName(),
                    job.getKey().getGroup()).withSchedule(repeatSecondlyForever((int) (intervalTime / 1000)));
            // Modify by Rod Monitor Start Delayed
            if (dm.getIntervalStart() > 0L) {
                builder.startAt(new Date(System.currentTimeMillis() + dm.getIntervalStart()));
            }
            // builder.startAt(new Date(System.currentTimeMillis() + dm.getIntervalOfNormal()));
            schedulerService.scheduleJob(job, builder.build());
            return true;
        } catch (ClassNotFoundException e) {
            logger.error("device monitor not found error:", e);
            throw new NetworkException("device monitor not found error:", e);
        } catch (SchedulerException e) {
            logger.error("add device monitor:", e);
            throw new NetworkException("add device monitor error:", e);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#attributeChanged(long,
     *      java.lang.String[], java.lang.String[])
     */
    @Override
    // TODO long --> Long
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.MonitorListener#monitorAdded(com.topvision.ems.message.event.MonitorEvent)
     */
    @Override
    public void monitorAdded(MonitorEvent event) {
        addMonitor(event.getMonitor());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.MonitorListener#monitorChanged(com.topvision.ems.message.event.MonitorEvent)
     */
    @Override
    public void monitorChanged(MonitorEvent event) {
        logger.info("MonitorId:" + event.getMonitor().getMonitorId().toString() + " Category:"
                + event.getMonitor().getCategory() + " Stop(monitorChanged)");
        Map<String, String> map = new HashMap<String, String>();
        map.put("category", event.getMonitor().getCategory());
        map.put("intervalOfNormal", event.getMonitor().getIntervalOfNormal().toString());
        map.put("monitorId", event.getMonitor().getMonitorId().toString());
        monitorDao.updatePollingInterval(map);
        stopMonitor(event.getMonitor());
        addMonitor(event.getMonitor());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.MonitorListener#monitorDisabled(com.topvision.ems.message.event.MonitorEvent)
     */
    @Override
    public void monitorDisabled(MonitorEvent event) {
        logger.info("MonitorId:" + event.getMonitor().getMonitorId().toString() + " Category:"
                + event.getMonitor().getCategory() + " Stop(monitorDisabled)");
        monitorDao.updateStatus(event.getMonitor().getMonitorId(), false);
        stopMonitor(event.getMonitor());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.MonitorListener#monitorEnabled(com.topvision.ems.message.event.MonitorEvent)
     */
    @Override
    public void monitorEnabled(MonitorEvent event) {
        monitorDao.updateStatus(event.getMonitor().getMonitorId(), true);
        startMonitor(event.getMonitor());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.MonitorListener#monitorRemoved(com.topvision.ems.message.event.MonitorEvent)
     */
    @Override
    public void monitorRemoved(MonitorEvent event) {
        logger.info("MonitorId:" + event.getMonitor().getMonitorId().toString() + " Category:"
                + event.getMonitor().getCategory() + " Stop(monitorRemoved)");
        stopMonitor(event.getMonitor());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityAdded(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) throws NetworkException {
        /*
        Entity entity = event.getEntity();
        if (entity.getIp() == null || entity.getIp().equals("0.0.0.0")) {
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("[" + entity.getIp() + "]entityAdded!!!");
        }
        Monitor dm = null; // 创建ICMP监视器
        if (!monitorDao.existMonitor(entity.getEntityId(), PING_MONITOR)) {
            dm = new Monitor();
            dm.setCategory(PING_MONITOR);
            dm.setIp(entity.getIp());
            dm.setName(entity.getIp());
            dm.setEntityId(entity.getEntityId());
            dm.setEnabled(true);
            Ping ping = new Ping();
            dm.setContent(ping);
            dm.setJobClass(PingJob.class.getName());
            dm.setIntervalStart(0L);
            monitorDao.insertEntity(dm);
            addMonitor(dm);
            if (logger.isDebugEnabled()) {
                logger.debug("[" + entity.getIp() + "]created ping device monitor successful!!!");
            }
        }
        */
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityDiscovered(com.topvision
     * .ems.message .event.EntityEvent)
     */
    @Override
    public void entityDiscovered(EntityEvent event) {
        Entity entity = event.getEntity();
        if (entity.getIp() == null || entity.getIp().equals("0.0.0.0")) {
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("[" + entity.getIp() + "]entityDiscovered!!!");
        }
        // TODO 创建端口流量
        // Monitor dm = null;
        /*
         * if (!monitorDao.existMonitor(entity.getEntityId(), FLOW_MONITOR) &&
         * entity.isSnmpSupport()) { dm = new Monitor(); dm.setCategory(FLOW_MONITOR);
         * dm.setIp(entity.getIp()); dm.setName(entity.getIp());
         * dm.setEntityId(entity.getEntityId()); dm.setContent(entity.getIp().getBytes());
         * dm.setJobClass(FlowJob.class.getName()); monitorDao.insertEntity(dm); addMonitor(dm); if
         * (logger.isDebugEnabled()) { logger.debug("[" + entity.getIp() +
         * "]created flow device monitor successful!!!"); } }
         */
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityChanged(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityChanged(EntityEvent event) {
        // Modify by Rod 去掉entityChanged方法，设备变化不需要删除设备再新增设备
        // entityRemoved(event);
        // entityAdded(event);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityRemoved(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
        logger.debug("entityRemoved {}", event);
        List<Monitor> monitors = monitorDao.getMonitorByEntity(event.getEntity().getEntityId());
        List<Long> ids = new ArrayList<Long>(monitors.size());
        for (Monitor dm : monitors) {
            ids.add(dm.getMonitorId());
            logger.info("MonitorId:" + dm.getMonitorId().toString() + " Category:" + dm.getCategory()
                    + " Stop(entityRemoved)");
            stopMonitor(dm);
        }
        monitorDao.deleteByPrimaryKey(ids);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#fireEntityPerformance(Long, Double,
     *      Double, Double, Double, java.lang.String)
     */
    @Override
    public void fireEntityPerformance(Long entityId, Double cpu, Double mem, Double vmem, Double disk, String sysUpTime) {
        EntityValueEvent event = new EntityValueEvent(entityId);
        event.setEntityId(entityId);
        event.setCpu(cpu);
        event.setMem(mem);
        event.setVmem(vmem);
        event.setDisk(disk);
        event.setSysUpTime(sysUpTime);
        event.setActionName("performanceChanged");
        event.setListener(EntityValueListener.class);

        messageService.addMessage(event);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#fireEntityState(Long, Integer)
     */
    @Override
    public void fireEntityState(Long entityId, Integer delay) {
        EntityValueEvent event = new EntityValueEvent(entityId);
        event.setEntityId(entityId);
        event.setState(delay >= 0);
        event.setDelay(delay);
        event.setActionName("stateChanged");
        event.setListener(EntityValueListener.class);

        messageService.addMessage(event);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#fireLinkFlow(Long, Double, Double,
     *      Double)
     */
    @Override
    public void fireLinkFlow(Long linkId, Double flow, Double rate, Double usage) {
        if (logger.isDebugEnabled()) {
            logger.debug(">>>>>>>>>>>>>>>>linkId[" + linkId + "]" + flow + "," + rate + "," + usage);
        }
        if (linkFlow.containsKey(linkId)) {
            if (System.currentTimeMillis() - linkFlow.get(linkId) < 60000) {
                return;
            }
        }
        linkFlow.put(linkId, System.currentTimeMillis());
        FlowEvent event = new FlowEvent(linkId);
        event.setLinkId(linkId);
        event.setFlow(flow);
        event.setRate(rate);
        event.setUsage(usage);
        event.setActionName("flowChanged");
        event.setListener(FlowListener.class);

        messageService.addMessage(event);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#getMonitor(java.util.Map)
     */
    @Override
    public Monitor getMonitor(Map<String, String> map) {
        List<Monitor> dms = monitorDao.selectByMap(map);
        if (CollectionUtils.isEmpty(dms)) {
            return null;
        } else if (dms.size() == 1) {
            return dms.get(0);
        } else {
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#getLastValuesByMonitor(Long)
     */
    @Override
    public List<MonitorValue> getLastValuesByMonitor(Long monitorId) {
        return monitorDao.getLastValuesByMonitor(monitorId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#getLinkByPort(Long, Integer)
     */
    @Override
    public List<Link> getLinkByPort(Long entityId, Long ifIndex) {
        return linkDao.getLinkByPort(entityId, ifIndex);
    }

    /**
     * @return the linkDao
     */
    public final LinkDao getLinkDao() {
        return linkDao;
    }

    /**
     * @return the onlineService
     */
    public final OnlineService getOnlineService() {
        return onlineService;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#getPortStatusByEntityId(java.lang.Long)
     */
    @Override
    public List<Port> getPortStatusByEntityId(Long entityId) {
        return portPerfDao.getPortStatusByEntityId(entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#getSnmpParamByEntity(java.lang.Long)
     */
    @Override
    public SnmpParam getSnmpParamByEntity(Long entityId) {
        return entityDao.getSnmpParamByEntityId(entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#getThreshold(Long, java.lang.String)
     */
    @Override
    public List<Threshold> getThreshold(Long monitorId, String itemName) {
        List<Threshold> thresholds = new ArrayList<Threshold>();
        // Threshold t = null;
        // List<EntityThresholdRela> etrs =
        // thresholdService.getEntityThresholdByMonitorItem(itemName);
        // if (!CollectionUtils.isEmpty(etrs)) {
        // for (EntityThresholdRela etr : etrs) {
        // t = thresholdService.loadThresholdById(etr.getThresholdId());
        // if (t != null) {
        // thresholds.add(t);
        // }
        // }
        // }
        // if (!thresholds.isEmpty()) {
        // return thresholds;
        // }
        // t = thresholdService.getDefaultThresholdByMonitorItem(itemName);
        // if (t != null) {
        // thresholds.add(t);
        // }

        return thresholds;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        logger.debug("MonitorServiceImpl initialize");
        messageService.addListener(EntityListener.class, this);
        messageService.addListener(MonitorListener.class, this);

        monitorJobs = new HashMap<Long, JobDetail>();
    }

    public void start() {
        try {
            // Start PerfMonitor
            final List<Monitor> dms = monitorDao.selectByMap(null);
            if (dms == null || dms.isEmpty()) {
                return;
            }
            // Calculate Start Interval
            int interval_ = 0;
            if (dms.size() > 0) {
                interval_ = monitorStartTotal / (dms.size());
            }
            for (Monitor dm : dms) {
                if (dm.isEnabled()) {
                    addMonitor(dm);
                }
                Thread.sleep(interval_);
            }
        } catch (DataAccessException dae) {
            getLogger().error("MonitorServiceImpl initialize error:", dae);
        } catch (InterruptedException e) {
            getLogger().error("MonitorServiceImpl initialize error:", e);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#insertConnectivity(com.topvision.platform.domain.SystemLog,
     *      com.topvision.ems.network.domain.MonitorValue)
     */
    @Override
    public void insertConnectivity(SystemLog systemLog, MonitorValue value) {
        monitorDao.insertConnectivity(value);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#insertConnectivity(com.topvision.platform.domain.SystemLog,
     *      java.util.List)
     */
    @Override
    public void insertConnectivity(SystemLog systemLog, List<MonitorValue> values) {
        monitorDao.insertConnectivity(values);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#insertPortPerf(com.topvision.platform.domain.SystemLog,
     *      java.util.List)
     */
    @Override
    public void insertPortPerf(SystemLog systemLog, List<PortPerf> perfs) {
        if (CollectionUtils.isEmpty(perfs)) {
            return;
        }

        portPerfDao.insertEntity(perfs);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#insertValue(com.topvision.platform.domain.SystemLog,
     *      com.topvision.ems.network.domain.MonitorValue)
     */
    @Override
    public void insertValue(SystemLog systemLog, MonitorValue value) {
        monitorDao.insertValue(value);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#insertValue(com.topvision.platform.domain.SystemLog,
     *      java.util.List)
     */
    @Override
    public void insertValue(SystemLog systemLog, List<MonitorValue> values) {
        monitorDao.insertValue(values);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#managerChanged(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void managerChanged(EntityEvent event) {
        List<Monitor> monitors = monitorDao.getMonitorByEntity(event.getEntity().getEntityId());
        List<Long> ids = new ArrayList<Long>(monitors.size());
        for (Monitor dm : monitors) {
            ids.add(dm.getMonitorId());
            if (event.getEnableManager()) {
                startMonitor(dm);
            } else {
                logger.info("MonitorId:" + dm.getMonitorId().toString() + " Category:" + dm.getCategory()
                        + " Stop(managerChanged)");
                stopMonitor(dm);
            }
        }

        monitorDao.updateStatus(ids, event.getEnableManager());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#pauseMonitor(com.topvision.ems.network.domain.Monitor)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Boolean pauseMonitor(Monitor dm) throws NetworkException {
        logger.info("MonitorId:" + dm.getMonitorId().toString() + " Category:" + dm.getCategory()
                + " Stop(pauseMonitor)");
        JobDetail job = monitorJobs.get(dm.getMonitorId());
        try {
            schedulerService.pauseJob(job.getKey());
            ConcurrentHashMap<Long, TopologyResult> exchangers = (ConcurrentHashMap<Long, TopologyResult>) schedulerService
                    .getContext().get("exchangers");
            if (exchangers != null && dm.getEntityId() > 0) {
                exchangers.remove(dm.getEntityId());
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("pause device monitor:", e);
            throw new NetworkException("pause device monitor:", e);
        }
    }

    /**
     * @param monitorDao
     *            the monitorDao to set
     */
    public void setMonitorDao(MonitorDao monitorDao) {
        this.monitorDao = monitorDao;
    }

    /**
     * @param entityDao
     *            the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * @param linkDao
     *            the linkDao to set
     */
    public final void setLinkDao(LinkDao linkDao) {
        this.linkDao = linkDao;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public final void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * @param onlineService
     *            the onlineService to set
     */
    public final void setOnlineService(OnlineService onlineService) {
        this.onlineService = onlineService;
    }

    /**
     * @param portPerfDao
     *            the portPerfDao to set
     */
    public void setPortPerfDao(PortPerfDao portPerfDao) {
        this.portPerfDao = portPerfDao;
    }

    /**
     * 
     */
    @Override
    public Boolean startMonitor(Monitor dm) throws NetworkException {
        JobDetail job = monitorJobs.get(dm.getMonitorId());
        try {
            if (job == null) {
                return addMonitor(dm);
            } else if (schedulerService.getJobDetail(job.getKey()) != null) {
                schedulerService.resumeJob(job.getKey());
            } else {
                TriggerBuilder<SimpleTrigger> builder = newTrigger()
                        .withIdentity(job.getKey().getName(), job.getKey().getGroup())
                        .usingJobData(job.getJobDataMap())
                        .withSchedule(repeatSecondlyForever((int) (dm.getIntervalOfNormal() / 1000)));
                Calendar now = Calendar.getInstance();
                now.add(Calendar.MILLISECOND, (int) (dm.getIntervalOfNormal() * Math.random()));
                builder.startAt(now.getTime());
                schedulerService.scheduleJob(job, builder.build());
                schedulerService.resumeJob(job.getKey());
                if (!monitorJobs.containsKey(dm.getMonitorId())) {
                    monitorJobs.put(dm.getMonitorId(), job);
                }
            }
            return true;
        } catch (SchedulerException e) {
            logger.error("start device monitor:", e);
            throw new NetworkException("start device monitor:", e);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#stopMonitor(com.topvision.ems.network.domain.Monitor)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Boolean stopMonitor(Monitor dm) throws NetworkException {
        logger.info("MonitorId:" + dm.getMonitorId().toString() + " Category:" + dm.getCategory()
                + " Stop(stopMonitor)");
        logger.debug("stop device monitor {}", dm);
        JobDetail job = monitorJobs.get(dm.getMonitorId());
        if (job == null) {
            return true;
        }
        try {
            schedulerService.deleteJob(job.getKey());
            ConcurrentHashMap<Long, TopologyResult> exchangers = (ConcurrentHashMap<Long, TopologyResult>) schedulerService
                    .getContext().get("exchangers");
            if (exchangers != null && dm.getEntityId() > 0) {
                exchangers.remove(dm.getEntityId());
            }
            monitorJobs.remove(dm.getMonitorId());
            return true;
        } catch (SchedulerException e) {
            logger.error("stop device monitor:", e);
            throw new NetworkException("stop device monitor:", e);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#updateHealthy(com.topvision.ems.network.domain.Monitor)
     */
    @Override
    public void updateHealthy(Monitor monitor) {
        monitorDao.updateHealthy(monitor);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#updatePortStatus(java.util.List)
     */
    @Override
    public void updatePortStatus(List<Port> ports) {
        portPerfDao.updatePortStatus(ports);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#updateSnapPerf(java.lang.String,
     *      com.topvision.ems.network.domain.EntitySnap)
     */
    @Override
    public void updateSnapPerf(String ip, EntitySnap snap) {
        Entity entity = entityDao.getEntityByIp(ip);
        if (entity != null) {
            snap.setEntityId(entity.getEntityId());
            entityDao.updateEntityPerf(snap);
            fireEntityPerformance(snap.getEntityId(), snap.getCpu(), snap.getMem(), snap.getVmem(), snap.getDisk(),
                    snap.getSysUpTime());
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.MonitorService#updateSnapState(com.topvision.ems.network.domain.EntitySnap)
     */
    @Override
    public void updateSnapState(EntitySnap snap) {
        entityDao.updateEntityState(snap);
    }

    /**
     * @param facadeFactory
     *            the facadeFactory to set
     */
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    public void setSchedulerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public EntityService getEntityService() {
        return entityService;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }
}
