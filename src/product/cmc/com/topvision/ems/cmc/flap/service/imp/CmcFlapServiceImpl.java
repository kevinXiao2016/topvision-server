/***********************************************************************
 * $Id: CmcPerfServiceImpl.java,v1.0 2012-5-8 上午10:53:54 $
 *
 * @author: loyal
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.flap.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.flap.dao.CmFlapDao;
import com.topvision.ems.cmc.flap.facade.CmcFlapFacade;
import com.topvision.ems.cmc.flap.service.CmcFlapService;
import com.topvision.ems.cmc.perf.dao.CmcPerfDao;
import com.topvision.ems.cmc.performance.domain.CMFlapHis;
import com.topvision.ems.cmc.performance.domain.CmFlapMonitor;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.facade.domain.PerformanceConstants;
import com.topvision.ems.facade.domain.ScheduleMessage;
import com.topvision.ems.performance.service.PerformanceService;
import com.topvision.platform.facade.FacadeFactory;

@Service("cmcFlapService")
public class CmcFlapServiceImpl extends CmcBaseCommonService implements CmcFlapService, BeanFactoryAware {

    private BeanFactory beanFactory;
    @Resource(name = "facadeFactory")
    private FacadeFactory facadeFactory;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Resource(name = "performanceService")
    private PerformanceService<CmFlapMonitor> performanceService;
    @Resource(name = "cmcPerfDao")
    private CmcPerfDao cmcPerfDao;
    @Resource(name = "cmFlapDao")
    private CmFlapDao flapDao;

    @Override
    public Integer getTopCmFlapInterval(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFlapFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), CmcFlapFacade.class);
        try {
            return new Integer(facade.getTopCmFlapInterval(snmpParam));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void cmFlapConfig(Long cmcId, Integer topCmFlapInterval) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFlapFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), CmcFlapFacade.class);
        facade.setTopCmFlapInterval(snmpParam, topCmFlapInterval);
    }

    @Override
    public void resetFlapCounters(Long cmcId) {
        snmpParam = getSnmpParamByCmcId(cmcId);
        CmcFlapFacade facade = facadeFactory.getFacade(snmpParam.getIpAddress(), CmcFlapFacade.class);
        Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
        facade.resetFlapCounters(snmpParam, cmcIndex);

    }

    /**
     * interval 采集周期，单位：s modified by bryan 单位改为秒
     */
    @Override
    public void modifyFlapMonitor(Long cmcId, Long interval) {

        Long entityId = getEntityIdByCmcId(cmcId);
        Long monitorId = cmcPerfDao.getMonitorId(entityId, "cmFlapMonitor");
        if (monitorId != null && monitorId > 0) {
            snmpParam = getSnmpParamByEntityId(entityId);
            performanceService.stopMonitor(snmpParam, monitorId);
        }

        if (interval != null && interval > 0) {
            CmFlapMonitor cmFlapMonitor = (CmFlapMonitor) beanFactory.getBean("cmFlapMonitor");
            snmpParam = getSnmpParamByEntityId(entityId);
            cmFlapMonitor.setEntityId(entityId);
            performanceService.startMonitor(snmpParam, cmFlapMonitor, 0L, interval * 1000L,
                    PerformanceConstants.PERFORMANCE_DOMAIN, PerformanceConstants.PERFORMANCE_IS_STARTWITHSERVER);
        }

    }

    @Override
    public List<CMFlapHis> queryOneCMFlapHis(String cmMac, Long startTime, Long endTime) {
        List<CMFlapHis> oneCMFlapHis = flapDao.getCmFlapHis(cmMac, startTime, endTime);
        return oneCMFlapHis;
    }

    @Override
    public void stopFlapMonitor(Long entityId) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean hasFlapMonitor(Long entityId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.flap.service.CmcFlapService#queryCmcFlapMonitorInterval(java.lang.Long)
     */
    @Override
    public Long queryCmcFlapMonitorInterval(Long cmcId) {
        ScheduleMessage<?> monitor = performanceService.getMonitorByIdentifyAndCategory(cmcId, "cmFlapMonitor");
        Long period = 0l;
        if (monitor != null) {
            period = monitor.getPeriod();
        }
        return period;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.flap.service.CmcFlapService#queryCmFlapInfo(java.lang.Long)
     */
    @Override
    public CmFlap queryCmFlapInfo(Long cmId) {
        return flapDao.queryForLastCmFlapByCmId(cmId);
    }

}
