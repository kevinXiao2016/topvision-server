/***********************************************************************
 * $Id: AutoRefreshLastDeregisterTimeJob.java,v1.0 2017年1月9日 上午10:00:23 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OnuDeregisterTable;
import com.topvision.ems.epon.onu.facade.OnuFacade;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Rod John
 * @created @2017年1月9日-上午10:00:23
 *
 */
public class AutoRefreshLastDeregisterTimeJob  extends AbstractJob {

    /* (non-Javadoc)
     * @see com.topvision.framework.scheduler.AbstractJob#doJob(org.quartz.JobExecutionContext)
     */
    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        EntityService entityService = getService(EntityService.class);
        OnuDao onuDao = getService(OnuDao.class);
        FacadeFactory facadeFactory = getService(FacadeFactory.class);
        

        try {
            List<OltOnuAttribute> onuAttributes = onuDao.getAllOnuIndex();
            List<OnuDeregisterTable> deregisterTables = new ArrayList<>();
            for (OltOnuAttribute onuAttribute : onuAttributes) {
                SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuAttribute.getEntityId());
                deregisterTables.add(new OnuDeregisterTable(snmpParam, onuAttribute));
            }
            // Snmp Fetch
            deregisterTables = facadeFactory.getFacade(OnuFacade.class).getOnuDeregisterInfo(deregisterTables);
            // Database Sync
            onuDao.updateOnuDeregisterInfo(deregisterTables);
        } catch (Exception e) {
            logger.error("autoRefreshOnuLastDeregisterTimeThread error:", e);
        }
    }

}