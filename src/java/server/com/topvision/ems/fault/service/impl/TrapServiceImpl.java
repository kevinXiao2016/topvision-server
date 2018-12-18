package com.topvision.ems.fault.service.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OctetString;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.fault.TrapFacade;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.domain.NbiConfig;
import com.topvision.ems.fault.domain.NbiTrap;
import com.topvision.ems.fault.parser.TrapConstants;
import com.topvision.ems.fault.parser.TrapParser;
import com.topvision.ems.fault.service.TrapService;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.exception.service.NetworkException;
import com.topvision.framework.exception.service.ServiceException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.Trap;
import com.topvision.framework.snmp.TrapCallback;
import com.topvision.framework.snmp.TrapServerParam;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.Action;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.ApEntityEvent;
import com.topvision.platform.message.event.ApEntityListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.ActionService;
import com.topvision.platform.service.SchedulerService;

public class TrapServiceImpl extends BaseService implements TrapService, TrapCallback, ActionService, BeanFactoryAware {
    public static final String TRAP_MODULE = "trap";
    private static final String NORTH_HEART_BEAT_TRIGGER = "northHeartBeatTrigger";
    private static final Logger logger = LoggerFactory.getLogger(TrapService.class);
    private String listenAddress = "0.0.0.0";
    private List<TrapParser> parsers;
    @Autowired
    private SystemPreferencesDao systemPreferencesDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityAddressDao entityAddressDao;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private MessageService messageService;
    private BeanFactory beanFactory;
    @Autowired
    private SchedulerService schedulerService;
    private JobDetail job = null;

    @Override
    public void start() {
        List<SystemPreferences> prefs = systemPreferencesDao.selectByModule(TRAP_MODULE);
        if (prefs != null && !prefs.isEmpty()) {
            List<Integer> ports = new ArrayList<Integer>();
            for (SystemPreferences pref : prefs) {
                if (pref.getName().equals("trap.listenPorts") && pref.getValue() != null
                        && !pref.getValue().isEmpty()) {
                    StringTokenizer tokens = new StringTokenizer(pref.getValue(), ",");
                    while (tokens.hasMoreTokens()) {
                        String s = tokens.nextToken();
                        ports.add(Integer.parseInt(s));
                    }
                } else if (pref.getName().equals("trap.listenAddress")) {
                    listenAddress = pref.getValue();
                }
            }
            if (!ports.isEmpty()) {
                List<TrapFacade> facades = facadeFactory.getAllFacade(TrapFacade.class);
                for (TrapFacade facade : facades) {
                    try {
                        TrapServerParam param = new TrapServerParam();
                        param.setListenPorts(ports);
                        param.setListenAddress(listenAddress);
                        facade.setTrapServerParam(param);
                        facade.addTrapListener();
                    } catch (Throwable e) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("TrapServiceImpl initialize.", e);
                        }
                    }
                }
            }
        }
        // START NORTH HEART BEAT
        startNorthHeartBeatJob();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("TrapServiceImpl destroy...");
        }
    }

    @Override
    public Boolean isServerSetting() {
        return false;
    }

    @Override
    public void onTrap(Trap trap) {
        EntityAddress entityAddress = entityAddressDao.selectByAddress(trap.getAddress());
        if (entityAddress == null) {
            if (logger.isInfoEnabled()) {
                logger.info("receive the trap from {},but device not exist.", trap.getAddress());
            }
            if (TrapConstants.AP_DAEMON_TRAP_VALUE.equals(trap.getVarialbleValue(TrapConstants.TRAP_TYPE_OID))) {
                ApEntityEvent apEntityEvent = new ApEntityEvent(trap.getAddress());
                apEntityEvent.setIp(trap.getAddress());
                apEntityEvent.setActionName("apAutoAdded");
                apEntityEvent.setListener(ApEntityListener.class);
                messageService.addMessage(apEntityEvent);
            }
            return;
        }
        Entity entity = entityDao.selectByPrimaryKey(entityAddress.getEntityId());
        // Modify by Rod 当存在设备具有多个IP地址的时候，统一使用Entity表中的IpAddress
        if (!trap.getAddress().equals(entity.getIp())) {
            trap.setAddress(entity.getIp());
        }
        if (!entity.isStatus()) {
            if (logger.isInfoEnabled()) {
                logger.info("receive the trap from {},but device not be managed.", entity.getEntityId());
            }
            return;
        }
        // Modify by Victor@20170601去掉同步，parser不会动态变化
        // synchronized (parsers) {
        for (TrapParser parser : parsers) {
            try {
                // parser支持顺序，前面的优先级高于后面的，高优先级的可以中断后面的解析
                if (parser.match(entityAddress.getEntityId(), trap)) {
                    parser.parse(entityAddress.getEntityId(), trap);
                    break;
                }
            } catch (Exception e) {
                logger.error("", e.getMessage());
            }
        }
        // }
    }

    @Override
    public void reset() {
    }

    @Override
    public void sendAction(Action action, Object object, String msg) throws ServiceException {
        // Modify by Rod Use to Send Nbi Trap
        /*
         * Object o = action.getParamsObject(); if (o != null && o instanceof Trap) {
         * this.sendTrap((Trap) o); }
         */
        sendAlarmToNbi((Alert) object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.TrapService#sendAlarmToNbi(com.topvision.ems.fault.domain.
     * Alert)
     */
    @Override
    public void sendAlarmToNbi(Alert alert) {
        Trap trap = new Trap();
        List<SystemPreferences> northTrap = systemPreferencesDao.selectByModule(NbiConfig.NORTHBOUND);
        for (SystemPreferences pref : northTrap) {
            if (pref.getName().equals(NbiConfig.NORTHBOUND_IPADDRESS)) {
                trap.setAddress(pref.getValue());
            } else if (pref.getName().equals(NbiConfig.NORTHBOUND_PORT)) {
                trap.setPort(Integer.parseInt(pref.getValue()));
            } else if (pref.getName().equals(NbiConfig.NORTHBOUND_COMMUNITY)) {
                trap.setSecurityName(pref.getValue().getBytes());
            }
        }
        if (trap.getAddress() == null || trap.getAddress().equals("0.0.0.0") || trap.getSecurityName() == null) {
            return;
        }
        Map<String, Object> vb = new java.util.HashMap<>();
        vb.put(NbiTrap.TOPVISION_EMS_NORTHBOUND_ALARM_SEQUENCENUMBER, new Integer32(alert.getAlertId().intValue()));
        vb.put(NbiTrap.TOPVISION_EMS_NORTHBOUND_ALARM_HOST, new IpAddress(alert.getIp()));
        vb.put(NbiTrap.TOPVISION_EMS_NORTHBOUND_ALARM_CODE, new Integer32(alert.getTypeId()));
        vb.put(NbiTrap.TOPVISION_EMS_NORTHBOUND_ALARM_INSTANCE, new OctetString(alert.getSource()));
        vb.put(NbiTrap.TOPVISION_EMS_NORTHBOUND_ALARM_SEVERITY, new Integer32(alert.getLevelId()));
        vb.put(NbiTrap.TOPVISION_EMS_NORTHBOUND_ALARM_ADDITIONAL_TEXT, new OctetString(alert.getMessage()));
        if (Level.CLEAR_LEVEL == alert.getLevelId()) {
            vb.put(NbiTrap.TOPVISION_EMS_NORTHBOUND_ALARM_OCCURTIME,
                    new OctetString(getSnmpTimeString(alert.getClearTime().toString())));
        } else {
            vb.put(NbiTrap.TOPVISION_EMS_NORTHBOUND_ALARM_OCCURTIME,
                    new OctetString(getSnmpTimeString(alert.getLastTime().toString())));
        }
        trap.setVariableBindings(vb);
        this.sendTrap(trap);
    }

    private byte[] getSnmpTimeString(String timeString) {
        String year = timeString.split("-")[0];
        String month = timeString.split("-")[1];
        String day = timeString.split("-")[2].split(" ")[0];
        String hour = timeString.split("-")[2].split(" ")[1].split(":")[0];
        String minute = timeString.split(":")[1];
        String second = timeString.split(":")[2].split("\\.")[0];
        byte[] result = new byte[8];
        byte[] yearByte = yearToBytes2(Integer.parseInt(year));
        result[0] = yearByte[2];
        result[1] = yearByte[3];
        result[2] = (byte) Integer.parseInt(month);
        result[3] = (byte) Integer.parseInt(day);
        result[4] = (byte) Integer.parseInt(hour);
        result[5] = (byte) Integer.parseInt(minute);
        result[6] = (byte) Integer.parseInt(second);
        result[7] = 0x00;
        return result;
    }

    private byte[] yearToBytes2(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));
        }
        return b;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.TrapService#sendNorthBoundTestAlarm(com.topvision.ems.fault.
     * domain.NbiConfig)
     */
    @Override
    public void sendNorthBoundTestAlarm(NbiConfig nbiConfig) {
        Trap trap = new Trap();
        trap.setAddress(nbiConfig.getNbiIpAddress());
        trap.setPort(Integer.parseInt(nbiConfig.getNbiPort()));
        trap.setSecurityName(nbiConfig.getNbiCommunity().getBytes());
        Map<String, Object> vb = new java.util.HashMap<>();
        vb.put(NbiTrap.TOPVISION_EMS_NORTHBOUND_HEARTBEAT_LABEL, nbiConfig.getNbiHeartBeatLabel());
        trap.setVariableBindings(vb);
        this.sendTrap(trap);
    }

    @Override
    public void sendTrap(Trap trap) {
        facadeFactory.getDefaultFacade(TrapFacade.class).sendTrap(trap);
    }

    @Override
    public void registeryParser(TrapParser parser) {
        if (parsers == null) {
            parsers = new ArrayList<TrapParser>();
        }
        if (!parsers.contains(parser)) {
            synchronized (parsers) {
                parsers.add(parser);
            }
        }
        Collections.sort(parsers, new Comparator<TrapParser>() {
            @Override
            public int compare(TrapParser o1, TrapParser o2) {
                return o1.getTrapCos().compareTo(o2.getTrapCos());
            }

        });
    }

    @Override
    public void registeryParser(int index, TrapParser parser) {
        if (parsers == null) {
            parsers = new ArrayList<TrapParser>();
            if (parsers.contains(parser)) {
                parsers.remove(parser);
                parsers.add(index, parser);
            } else {
                parsers.add(index, parser);
            }
        }
    }

    @Override
    public void unregisteryParser(TrapParser parser) {
        synchronized (parsers) {
            if (parsers.contains(parser)) {
                parsers.remove(parser);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void startNorthHeartBeatJob() {
        try {
            job = newJob((Class<Job>) Class.forName("com.topvision.ems.fault.service.impl.NorthHeartBeatJob"))
                    .withIdentity("NorthHeartBeatJob").build();
            job.getJobDataMap().put("beanFactory", beanFactory);
            List<SystemPreferences> prefs = systemPreferencesDao.selectByModule(NbiConfig.NORTHBOUND);
            Integer interval = null;
            if (prefs != null && !prefs.isEmpty()) {
                for (SystemPreferences pref : prefs) {
                    if (pref.getName().equals(NbiConfig.NORTHBOUND_HEARTBEAT_SWITCH)
                            && !Boolean.parseBoolean(pref.getValue())) {
                        return;
                    } else if (pref.getName().equals(NbiConfig.NORTHBOUND_HEARTBEAT_INTERVAL)) {
                        interval = Integer.parseInt(pref.getValue());
                    }
                }
            }
            SimpleTrigger trigger = newTrigger().withIdentity(NORTH_HEART_BEAT_TRIGGER)
                    .withSchedule(repeatSecondlyForever(interval)).build();
            schedulerService.scheduleJob(job, trigger);
        } catch (Exception e) {
            logger.error("start north heart beat job error:", e);
        }
    }

    /**
     * Stop Auto Job
     * 
     * @throws NetworkException
     */
    private void stopNorthHeartBeatJob() throws NetworkException {
        logger.info("Stop North Heart Beat Job");
        if (job == null) {
            return;
        }
        try {
            schedulerService.deleteJob(job.getKey());
        } catch (SchedulerException e) {
            logger.error("Stop Auto Discovery Job:", e);
            throw new NetworkException("Stop Auto Discovery Job:", e);
        }
    }

    /**
     * @param parsers
     *            the parsers to set
     */
    public void setParsers(List<TrapParser> parsers) {
        this.parsers = parsers;
    }

    /**
     * 
     * @param systemPreferencesDao
     */
    public void setSystemPreferencesDao(SystemPreferencesDao systemPreferencesDao) {
        this.systemPreferencesDao = systemPreferencesDao;
    }

    /**
     * @param facadeFactory
     *            the facadeFactory to set
     */
    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    /**
     * @return the entityAddressDao
     */
    public EntityAddressDao getEntityAddressDao() {
        return entityAddressDao;
    }

    /**
     * @param entityAddressDao
     *            the entityAddressDao to set
     */
    public void setEntityAddressDao(EntityAddressDao entityAddressDao) {
        this.entityAddressDao = entityAddressDao;
    }

    /**
     * @return the entityDao
     */
    public EntityDao getEntityDao() {
        return entityDao;
    }

    /**
     * @param entityDao
     *            the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans
     * .factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.TrapService#reStartNorthHeartBeatJob()
     */
    @Override
    public void reStartNorthHeartBeatJob() {
        this.stopNorthHeartBeatJob();
        this.startNorthHeartBeatJob();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.TrapService#getNbiConfig()
     */
    @Override
    public NbiConfig getNbiConfig() {
        List<SystemPreferences> prefs = systemPreferencesDao.selectByModule(NbiConfig.NORTHBOUND);
        NbiConfig config = new NbiConfig();
        for (SystemPreferences pref : prefs) {
            if (pref.getName().equals(NbiConfig.NORTHBOUND_HEARTBEAT_SWITCH)) {
                config.setNbiHeartBeatSwitch(Boolean.valueOf(pref.getValue()));
            } else if (pref.getName().equals(NbiConfig.NORTHBOUND_HEARTBEAT_INTERVAL)) {
                config.setNbiHeartBeatInterval(Integer.parseInt(pref.getValue()));
            } else if (pref.getName().equals(NbiConfig.NORTHBOUND_IPADDRESS)) {
                config.setNbiIpAddress(pref.getValue());
            } else if (pref.getName().equals(NbiConfig.NORTHBOUND_PORT)) {
                config.setNbiPort(pref.getValue());
            } else if (pref.getName().equals(NbiConfig.NORTHBOUND_COMMUNITY)) {
                config.setNbiCommunity(pref.getValue());
            } else if (pref.getName().equals(NbiConfig.NORTHBOUND_HEARTBEAT_LABEL)) {
                config.setNbiHeartBeatLabel(pref.getValue());
            }
        }
        return config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.TrapService#modifyNbiConfig(com.topvision.ems.fault.domain.
     * NbiConfig)
     */
    @Override
    public void modifyNbiConfig(NbiConfig nbiConfig) {
        List<SystemPreferences> preferences = new ArrayList<>();
        // switch
        SystemPreferences tmp = new SystemPreferences();
        tmp.setModule(NbiConfig.NORTHBOUND);
        tmp.setName(NbiConfig.NORTHBOUND_HEARTBEAT_SWITCH);
        tmp.setValue(nbiConfig.getNbiHeartBeatSwitch().toString());
        preferences.add(tmp);
        // interval
        tmp = new SystemPreferences();
        tmp.setModule(NbiConfig.NORTHBOUND);
        tmp.setName(NbiConfig.NORTHBOUND_HEARTBEAT_INTERVAL);
        tmp.setValue(nbiConfig.getNbiHeartBeatInterval().toString());
        preferences.add(tmp);
        // ipAddress
        tmp = new SystemPreferences();
        tmp.setModule(NbiConfig.NORTHBOUND);
        tmp.setName(NbiConfig.NORTHBOUND_IPADDRESS);
        tmp.setValue(nbiConfig.getNbiIpAddress().toString());
        preferences.add(tmp);
        // port
        tmp = new SystemPreferences();
        tmp.setModule(NbiConfig.NORTHBOUND);
        tmp.setName(NbiConfig.NORTHBOUND_PORT);
        tmp.setValue(nbiConfig.getNbiPort().toString());
        preferences.add(tmp);
        // community
        tmp = new SystemPreferences();
        tmp.setName(NbiConfig.NORTHBOUND_COMMUNITY);
        tmp.setValue(nbiConfig.getNbiCommunity().toString());
        preferences.add(tmp);
        // labal
        tmp = new SystemPreferences();
        tmp.setName(NbiConfig.NORTHBOUND_HEARTBEAT_LABEL);
        tmp.setValue(nbiConfig.getNbiHeartBeatLabel().toString());
        preferences.add(tmp);
        // update
        systemPreferencesDao.updateEntity(preferences);
        reStartNorthHeartBeatJob();
    }

    @Override
    public String sendActionBak(Action action, Object object, String msg) throws ServiceException {
        return null;
    }

    @Override
    public String checkConnection(String smsServerIp, int smsServicePort) {
        return null;
    }
}
