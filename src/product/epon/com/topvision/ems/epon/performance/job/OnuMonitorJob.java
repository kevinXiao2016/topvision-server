/***********************************************************************
 * $Id: OnuMonitorJob.java,v1.0 2013-3-21 上午11:00:13 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.job;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;

import com.topvision.ems.epon.onu.service.OnuService;
import com.topvision.ems.epon.performance.facade.OltPerfFacade;
import com.topvision.ems.network.service.DiscoveryService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.job.MonitorJob;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 该类提供对OLT设备下属的所有ONU进行定时刷新同步功能
 * 
 * 
 * @author Rod Johnson
 * @created @2013-3-21-上午11:00:13
 *
 */
public class OnuMonitorJob extends MonitorJob {
    @SuppressWarnings("unused")
    private static int flag = 0;

    /* (non-Javadoc)
     * @see com.topvision.ems.performance.job.MonitorJob#doJob(org.quartz.JobExecutionContext)
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected void doJob(JobExecutionContext context) throws JobExecutionException {
        /*if (logger.isDebugEnabled()) {
            logger.debug("OnuMonitorJob start...");
        }
        BeanFactory beanFactory = (BeanFactory) jobDataMap.get("beanFactory");
        OnuService onuService = (OnuService) beanFactory.getBean("onuService");
        DiscoveryService discoveryService = (DiscoveryService) beanFactory.getBean("discoveryService");
        EntityService entityService = (EntityService) beanFactory.getBean("entityService");
        Long entityId = entityService.getEntityIdByIp(monitor.getIp());
        SnmpParam param = entityService.getSnmpParamByEntity(entityId);
        monitor.setLastCollectTime(new Timestamp(System.currentTimeMillis()));
        OltPerfFacade facade = facadeFactory.getFacade(monitor.getIp(), OltPerfFacade.class);
        Map<String, String> onuMacAddressOld = onuService.getAllOnuMacAndIndex(entityId);
        Map<String, String> onuMacAddressNew = new HashMap<String, String>();
        try {
            onuMacAddressNew = facade.getOnuMacAddress(param);
        } catch (Exception e) {
            logger.error("", e);
            return;
        }
        // compare onuMacAddress
        if (onuMacAddressNew.size() != onuMacAddressOld.size()) {
            //oltService.refreshOnuInfo(entityId);
            discoveryService.refresh(entityId);
            return;
        }
        }else if(onuMacAddressNew.size() < onuMacAddressOld.size() && flag == 0){
            flag = 1;
            return;
        }else if(onuMacAddressNew.size() < onuMacAddressOld.size() && flag == 1){
            discoveryService.refresh(entityId);
            flag = 0;
            return;
        }
        for (Map.Entry<String, String> entry : onuMacAddressNew.entrySet()) {
            String oldValue = onuMacAddressOld.get(entry.getKey());
            if (oldValue == null) {
                discoveryService.refresh(entityId);
                return;
            }
            if (!oldValue.equalsIgnoreCase(entry.getValue())) {
                discoveryService.refresh(entityId);
                return;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("OnuMonitorJob end...");
        }*/
    }

}
