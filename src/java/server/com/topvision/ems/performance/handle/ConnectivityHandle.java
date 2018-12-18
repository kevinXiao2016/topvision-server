/***********************************************************************
 * $Id: ConnectivityHandle.java,v1.0 2014-3-24 下午8:16:43 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.handle;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.connectivity.service.ConnectivityService;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.performance.dao.MonitorDao;
import com.topvision.ems.performance.domain.OnlineResult;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.exception.ResourceNotFoundException;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.message.event.EntityValueEvent;
import com.topvision.platform.message.event.EntityValueListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Rod John
 * @created @2014-3-24-下午8:16:43
 * 
 */
@Service("connectivityHandle")
public class ConnectivityHandle extends PerformanceHandle {
    public static String HANDLE_ID = "CONNECTIVITY";
    public static Integer CONNECTIVITY_HANDLE_ALERT_ID = -201;
    public static Integer CONNECTIVITY_HANDLE_EVENT_ID = -200;
    public static Integer PING_DISCONNECT_TYPE = -301;// 设备不通
    public static Integer PING_CONNECT_TYPE = -302;// 设备连通
    public static Integer SNMP_ERROR = -101;
    public static Integer SNMP_NORMAL = -100;
    public static Integer DEVICE_REPLACE = 100303;// CMTS设备被替换
    // 用来标记各设备的snmp状态
    private Map<Long, Boolean> snmpFlag = new HashMap<Long, Boolean>();
    @Autowired
    public EntityDao entityDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MonitorDao monitorDao;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private ConnectivityService connectivityService;

    @Override
    @PostConstruct
    public void initialize() {
        performanceStatisticsCenter.registerPerformanceHandler(HANDLE_ID, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        performanceStatisticsCenter.unregisterPerformanceHandler(HANDLE_ID);
    }

    @Override
    public void handle(PerformanceData data) {
        OnlineResult performanceResult = (OnlineResult) data.getPerfData();
        Long entityId = performanceResult.getEntityId();
        Entity entity = entityDao.selectByPrimaryKey(entityId);
        Integer delay = performanceResult.getDelay();
        String mac = performanceResult.getMac();
        String sysUpTime = performanceResult.getSysUpTime();
        Timestamp collectTime = performanceResult.getCollectTime();
        // 初始将状态置为false
        snmpFlag.put(entityId, false);

        try {
            delay = connectivityService.checkConnectivityUsingEntityUnique(entity, delay, mac);
        } catch (Exception e) {
            logger.error("check connectivity error: " + e);
            delay = -1;
        }

        try {
            EntitySnap snap = new EntitySnap();
            snap.setEntityId(entityId);
            snap.setDelay(delay);
            if (StringUtils.isNumeric(sysUpTime)) {
                snap.setSysUpTime(sysUpTime);
            } else {
                snap.setSysUpTime("-1");
            }
            snap.setState(delay >= 0);
            snap.setSnapTime(collectTime);
            entityDao.updateEntityState(snap);
            // TODO 设备不在线时更新cpu, mem, flash等为-1,但是在设备上线后导致在线状态更新了,而其它值无法进行更新,所以不在这里进行修改
            /*
             * if (!snap.isState()) { snap.setCpu(-1D); snap.setMem(-1D); snap.setVmem(-1D);
             * snap.setUsedMem(-1D); snap.setDisk(-1D); snap.setUsedDisk(-1D); snap.setDiskio(-1D);
             * entityDao.updateEntitySnap(snap); }
             */
        } catch (Exception e) {
            logger.error(entityId + " updateEntityState error ", e);
        }
        // StateChange
        EntityValueEvent event = new EntityValueEvent(entityId);
        event.setEntityId(entityId);
        event.setDelay(delay);
        if (sysUpTime.equals("snmpTimeout")) {
            event.setSysUpTime("-1");
        } else {
            event.setSysUpTime(sysUpTime);
        }
        event.setState(delay >= 0);
        event.setActionName("stateChanged");
        event.setListener(EntityValueListener.class);
        messageService.addMessage(event);
        // Connectivity
        try {
            monitorDao.insertPerfConnectivity(entityId, delay, collectTime);
        } catch (Exception e) {
            logger.error(entityId + " insertConnectivity error ", e);
        }
        // Snmp Alert
        if (delay > 0 && sysUpTime.equals("snmpTimeout")) {
            snmpFlag.put(entityId, false);
            Event snmpEvent = EventSender.getInstance().createEvent(SNMP_ERROR, entity.getIp(), entity.getIp());
            snmpEvent.setEntityId(entityId);
            snmpEvent.setMessage(getString("SNMP.Timeout"));
            EventSender.getInstance().send(snmpEvent);
        } else if (delay > 0 && !sysUpTime.equals("snmpTimeout") && !snmpFlag.get(entityId)) {
            snmpFlag.put(entityId, true);
            Event snmpEvent = EventSender.getInstance().createEvent(SNMP_NORMAL, entity.getIp(), entity.getIp());
            snmpEvent.setEntityId(entityId);
            snmpEvent.setMessage(getString("SNMP.normal"));
            EventSender.getInstance().send(snmpEvent);
        }
        super.handle(data);
    }

    @Override
    public PerfThresholdEventParams getEventParams(PerformanceData data) {
        try {
            OnlineResult onlineResult = (OnlineResult) data.getPerfData();
            Entity entity = entityDao.selectByPrimaryKey(onlineResult.getEntityId());
            PerfThresholdEventParams param = new PerfThresholdEventParams();
            param.setAlertEventId(CONNECTIVITY_HANDLE_ALERT_ID);
            param.setClearEventId(CONNECTIVITY_HANDLE_EVENT_ID);
            param.setSource(entity.getIp());
            Integer delay = onlineResult.getDelay();
            if (delay == -1) {
                Event icmpEvent = EventSender.getInstance().createEvent(PING_DISCONNECT_TYPE, entity.getIp(),
                        entity.getIp());
                icmpEvent.setEntityId(entity.getEntityId());
                // modify by fanzidong，message修改为(X.X.X.X 无法连通（ICMP不可达、SNMP不可达、TCP不可达）)
                String strategys = convertMessage(onlineResult.getStrategys());
                icmpEvent.setMessage(entity.getIp() + " " + getString("Device.Unreachable") + strategys);
                EventSender.getInstance().send(icmpEvent);
            } else {
                Event icmpEvent = EventSender.getInstance().createEvent(PING_CONNECT_TYPE, entity.getIp(),
                        entity.getIp());
                icmpEvent.setEntityId(entity.getEntityId());
                icmpEvent.setMessage(entity.getIp() + " " + getString("Device.Reachable"));
                EventSender.getInstance().send(icmpEvent);
                // Delay Handle
                param.setPerfValue(delay.floatValue());
                param.setTargetId(HANDLE_ID);
                param.setTargetIndex(0L);
                param.setMessage(entity.getIp() + " " + getString("Device.Delay") + " " + delay + "ms");
                return param;
            }
        } catch (Exception e) {
            logger.info("ConnectivityHandle error ", e);
            return null;
        }
        return null;
    }

    private String convertMessage(List<String> strategys) {
        StringBuilder sb = new StringBuilder("(");
        for (String name : strategys) {
            sb.append(name.toUpperCase()).append(getString("Unreachable")).append(",");
        }
        String message = sb.toString();
        return message.substring(0, message.length() - 1) + ")";
    }

    @Override
    public String getTypeCode(PerformanceData data) {
        OnlineResult onlineResult = (OnlineResult) data.getPerfData();
        Entity entity = entityDao.selectByPrimaryKey(onlineResult.getEntityId());
        long typeId = entity.getTypeId();
        if (entityTypeService.isOlt(typeId)) {
            return "OLT_CONNECTIVITY";
        } else if (entityTypeService.isCcmts(typeId)) {
            return "CC_CONNECTIVITY";
        } else if (entityTypeService.isCmts(typeId)) {
            return "CC_CONNECTIVITY";
        }
        return HANDLE_ID;

    }

    /**
     * 国际化
     * 
     * @param key
     * @param strings
     * @return
     */
    protected String getString(String key, String... strings) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.network.resources").getString(key, strings);
        } catch (ResourceNotFoundException e) {
            for (int i = 0; i < strings.length; i++) {
                key = key.replaceAll("\\{" + i + "\\}", strings[i]);
            }
            return key;
        }
    }
}
