/***********************************************************************
 * $Id: PingJob.java,v 1.1 Apr 6, 2009 2:42:37 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.job;

import java.sql.Timestamp;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.EntitySnap;
import com.topvision.ems.performance.domain.MonitorValue;
import com.topvision.platform.domain.SystemLog;

/**
 * @Create Date Apr 6, 2009 2:42:37 PM
 * 
 * @author kelers
 * 
 */
public class PingJob extends MonitorJob {
    public final static int PING_DISCONNECT_TYPE = -301;// 设备不通
    public final static int PING_CONNECT_TYPE = -302;// 设备连通
    public final static int DELAY_TYPE = 20001;// 延时阈值告警

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.monitor.MonitorJob#doJob(org.quartz.JobExecutionContext)
     */
    @Override
    protected void doJob(JobExecutionContext context) throws JobExecutionException {
        EntitySnap snap = null;
        snap = new EntitySnap();
        snap.setEntityId(monitor.getEntityId());
        snap.setState(false);
        Integer delay = -1;
        Entity entity = entityService.getEntity(monitor.getEntityId());
        monitor.setIp(entity.getIp());
        MonitorValue value = null;
        try {
            delay = onlineService.ping(entity.getIp());
            if (delay == 0) {
                delay = 1;
            }
            monitor.setLastCollectTime(new Timestamp(System.currentTimeMillis()));
            value = new MonitorValue();
            value.setCollectTime(monitor.getLastCollectTime());
            value.setItemIndex(0);
            value.setItemValue(Double.valueOf(delay));
            value.setEntityId(monitor.getEntityId());
            value.setItemName("ping");
            snap.setState(delay >= 0);
            snap.setSnapTime(value.getCollectTime());
            if (delay >= 0) {
                Boolean lastResult = (Boolean) jobDataMap.get("previousResult");
                if (lastResult != null && lastResult) {
                    return;
                }
                clearAlert(PING_CONNECT_TYPE, entity.getIp(), getString("Ping.Monitor.up", entity.getIp()));
                jobDataMap.put("previousResult", true);
                monitor.setHealthy(true);
            } else {
                monitor.setAvailable(false);
                monitor.setReason(getString("Ping.Monitor.down", entity.getIp()));
                jobDataMap.put("previousResult", false);
                sendEvent(PING_DISCONNECT_TYPE, entity.getIp(), monitor.getReason());
                monitor.setHealthy(false);
            }
        } catch (Throwable ex) {
            getLogger().debug("Ping doJob", ex);
            monitor.setAvailable(false);
            monitor.setHealthy(false);
            // Modify by Victor@20170223解决Ping异常后误报ICMP不可达
            // EMS-13911【河南新乡】ICMP不可达告警误报
            // EMS-8312
            // monitor.setReason(getString("Ping.Monitor.down", entity.getIp()));
            monitor.setReason(ex.getMessage());
            // jobDataMap.put("previousResult", false);//出现错误不改变状态
            // sendEvent(PING_DISCONNECT_TYPE, entity.getIp(), monitor.getReason());//不发告警
        } finally {
            try {
                if (value != null) {
                    monitorService.insertConnectivity(new SystemLog(), value);
                }
                monitorService.fireEntityState(monitor.getEntityId(), delay);
                monitorService.updateSnapState(snap);
            } catch (Exception e) {
                getLogger().debug(e.getMessage(), e);
            }
        }
    }
}
