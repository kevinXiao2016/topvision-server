/***********************************************************************
 * $Id: NorthHeartBeatJob.java,v1.0 2015-11-5 下午2:02:03 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.service.impl;

import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.snmp4j.smi.OctetString;
import org.springframework.beans.factory.BeanFactory;

import com.topvision.ems.facade.fault.TrapFacade;
import com.topvision.ems.fault.domain.NbiConfig;
import com.topvision.ems.fault.domain.NbiTrap;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.snmp.Trap;
import com.topvision.platform.dao.SystemPreferencesDao;
import com.topvision.platform.domain.SystemPreferences;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Rod John
 * @created @2015-11-5-下午2:02:03
 *
 */
public class NorthHeartBeatJob implements Job {

    /*
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        BeanFactory beanFactory = (BeanFactory) jobDataMap.get("beanFactory");
        FacadeFactory facadeFactory = (FacadeFactory) beanFactory.getBean("facadeFactory");
        Trap trap = initHeartBeatTrap(beanFactory);
        facadeFactory.getDefaultFacade(TrapFacade.class).sendTrap(trap);
    }

    private Trap initHeartBeatTrap(BeanFactory beanFactory) {
        SystemPreferencesDao systemPreferencesDao = (SystemPreferencesDao) beanFactory.getBean("systemPreferencesDao");
        List<SystemPreferences> northTrap = systemPreferencesDao.selectByModule(NbiConfig.NORTHBOUND);
        Trap trap = new Trap();
        String label = null;
        for (SystemPreferences pref : northTrap) {
            if (pref.getName().equals(NbiConfig.NORTHBOUND_IPADDRESS)) {
                trap.setAddress(pref.getValue());
            } else if (pref.getName().equals(NbiConfig.NORTHBOUND_PORT)) {
                trap.setPort(Integer.parseInt(pref.getValue()));
            } else if (pref.getName().equals(NbiConfig.NORTHBOUND_COMMUNITY)) {
                trap.setSecurityName(pref.getValue().getBytes());
            } else if (pref.getName().equals(NbiConfig.NORTHBOUND_HEARTBEAT_LABEL)) {
                label = pref.getValue();
            }
        }
        trap.setSource("NM3000 SERVER:" + EnvironmentConstants.getHostAddress());

        Map<String, Object> vb = new java.util.HashMap<>();
        vb.put(NbiTrap.TOPVISION_EMS_NORTHBOUND_HEARTBEAT_LABEL, new OctetString(label));
        trap.setVariableBindings(vb);
        return trap;
    }

}
