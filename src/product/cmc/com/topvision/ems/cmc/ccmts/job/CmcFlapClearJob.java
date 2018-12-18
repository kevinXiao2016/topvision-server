/***********************************************************************
 * $Id: CmcFlapClearJob.java,v1.0 2017年2月6日 下午5:13:04 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.topvision.ems.cmc.ccmts.dao.CmcListDao;
import com.topvision.ems.cmc.ccmts.facade.CmcFacade;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.framework.scheduler.AbstractJob;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author lizongtian
 * @created @2017年2月6日-下午5:13:04
 *
 */
public class CmcFlapClearJob extends AbstractJob {

    @Override
    public void doJob(JobExecutionContext ctx) throws JobExecutionException {
        CmcService cmcService = getService(CmcService.class);
        CmcListDao cmcListDao = getService(CmcListDao.class);
        FacadeFactory facadeFactory = getService(FacadeFactory.class);
        List<CmcAttribute> cmcList = cmcListDao.getAllCmcEntityList();
        for (CmcAttribute cmcAttribute : cmcList) {
            SnmpParam snmpParam = cmcService.getSnmpParamByCmcId(cmcAttribute.getCmcId());
            try {
                facadeFactory.getFacade(snmpParam.getIpAddress(), CmcFacade.class).clearCmcFlap(snmpParam,
                        cmcAttribute.getCmcIndex());
            } catch (Exception e) {
                logger.info("set Clean Cmc Flap:" + cmcAttribute.getCmcId() + " error {0}", e.getMessage());
            }
            try {
                facadeFactory.getFacade(snmpParam.getIpAddress(), CmcFacade.class).clearCmcAllCmFlap(
                        cmcAttribute.getCmcId());
            } catch (Exception e) {
                logger.info("set Clean Cmc FlapHis:" + cmcAttribute.getCmcId() + " error {0}", e.getMessage());
            }
        }
    }

}
