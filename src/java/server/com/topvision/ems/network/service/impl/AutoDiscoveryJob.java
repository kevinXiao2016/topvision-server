/***********************************************************************
 * $Id: BatchAutoDiscoveryJob.java,v1.0 2014-5-11 下午2:28:34 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.mp.SnmpConstants;
import org.springframework.beans.factory.BeanFactory;

import com.topvision.ems.network.dao.BatchAutoDiscoveryDao;
import com.topvision.ems.network.domain.BatchAutoDiscoveryIps;
import com.topvision.ems.network.domain.BatchAutoDiscoverySnmpConfig;
import com.topvision.ems.network.service.AutoDiscoveryService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author Rod John
 * @created @2014-5-11-下午2:28:34
 * 
 */
@PersistJobDataAfterExecution
public class AutoDiscoveryJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(AutoDiscoveryJob.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
        JobDataMap jobDataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
        BeanFactory beanFactory = (BeanFactory) jobDataMap.get("beanFactory");
        AutoDiscoveryService autoDiscoveryService = (AutoDiscoveryService) beanFactory.getBean("autoDiscoveryService");
        EntityTypeService entityTypeService = (EntityTypeService) beanFactory.getBean("entityTypeService");
        BatchAutoDiscoveryDao batchAutoDiscoveryDao = (BatchAutoDiscoveryDao) beanFactory
                .getBean("batchAutoDiscoveryDao");
        SystemPreferencesService systemPreferencesService = (SystemPreferencesService) beanFactory
                .getBean("systemPreferencesService");
        List<BatchAutoDiscoveryIps> autoDiscoveryIps = batchAutoDiscoveryDao.getAutoDiscoveryIps();
        // Put autoDiscoveryIps In the jobdetail
        AutoDiscoveryStatus autoDiscoveryStatus = (AutoDiscoveryStatus) beanFactory.getBean("autoDiscoveryStatus");
        autoDiscoveryStatus.setBatchAutoDiscoveryIps(autoDiscoveryIps);
        List<BatchAutoDiscoverySnmpConfig> autoDiscoverySnmpConfigs = batchAutoDiscoveryDao
                .getAutoDiscoverySnmpConfigs();
        List<String> autoDiscoverySysObjectId = new ArrayList<>();
        List<String> autoDiscoveryConfig = batchAutoDiscoveryDao.getAutoDiscoverySysObjectId();
        for(String sysObjectId : autoDiscoveryConfig){
            if(entityTypeService.getEntityTypeBySysObjId(sysObjectId) != null){
                autoDiscoverySysObjectId.add(sysObjectId);
            }
        } 
        List<SnmpParam> snmpParams = new ArrayList<SnmpParam>();
        for (BatchAutoDiscoverySnmpConfig snmpConfig : autoDiscoverySnmpConfigs) {
            SnmpParam snmpParam = new SnmpParam();
            snmpParam.setVersion(snmpConfig.getVersion());
            if (SnmpConstants.version3 == snmpConfig.getVersion()) {
                snmpParam.setUsername(snmpConfig.getUsername());
                snmpParam.setAuthProtocol(snmpConfig.getAuthProtocol());
                snmpParam.setAuthPassword(snmpConfig.getAuthPassword());
                snmpParam.setPrivProtocol(snmpConfig.getPrivProtocol());
                snmpParam.setPrivPassword(snmpConfig.getPrivPassword());
            } else if (SnmpConstants.version2c == snmpConfig.getVersion()) {
                snmpParam.setCommunity(snmpConfig.getReadCommunity());
                snmpParam.setWriteCommunity(snmpConfig.getWriteCommunity());
            }
            snmpParam.setTimeout(Integer.parseInt(systemPreferencesService.getModulePreferences("Snmp").getProperty(
                    "Snmp.timeout", "10000")));
            snmpParam.setRetry(Byte.parseByte(systemPreferencesService.getModulePreferences("Snmp").getProperty(
                    "Snmp.retries", "0")));
            snmpParam.setPort(Integer.parseInt(systemPreferencesService.getModulePreferences("Snmp").getProperty(
                    "Snmp.port", "161")));
            snmpParams.add(snmpParam);
        }
        for (BatchAutoDiscoveryIps tmp : autoDiscoveryIps) {
            try {
                if (tmp.getAutoDiscovery() == 1) {
                    tmp.setAutoDiscoveryStatus(BatchAutoDiscoveryIps.AUTO_DISCOVERY_STATUS_INPROCESS);
                    List<String> ips = IpUtils.parseIp(tmp.getIpInfo());
                    autoDiscoveryService.autoBatchDiscovery(ips, tmp.getFolderId(), snmpParams,
                            autoDiscoverySysObjectId);
                    tmp.setAutoDiscoveryStatus(BatchAutoDiscoveryIps.AUTO_DISCOVERY_STATUS_SUCCESS);
                    tmp.setLastDiscoveryTime(new Timestamp(System.currentTimeMillis()));
                    batchAutoDiscoveryDao.updateLastAutoDiscoveryTime(tmp);
                }
            } catch (Exception e) {
                tmp.setAutoDiscoveryStatus(BatchAutoDiscoveryIps.AUTO_DISCOVERY_STATUS_FAILURE);
                logger.error("", e);
            }
        }
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
        }
        for (BatchAutoDiscoveryIps tmp : autoDiscoveryIps) {
            tmp.setAutoDiscoveryStatus(BatchAutoDiscoveryIps.AUTO_DISCOVERY_STATUS_NO_START);
        }
    }

}
