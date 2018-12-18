/***********************************************************************
 * $Id: CmcSystemTimeServiceImpl.java,v1.0 2013-7-18 下午4:25:08 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.systemtime.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.systemtime.dao.CmcSystemTimeDao;
import com.topvision.ems.cmc.systemtime.facade.domain.CmcSystemTimeConfig;
import com.topvision.ems.cmc.systemtime.service.CmcSystemTimeService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author dosion
 * @created @2013-7-18-下午4:25:08
 * 
 */
@Service("cmcSystemTimeService")
public class CmcSystemTimeServiceImpl extends CmcBaseCommonService implements CmcSystemTimeService,
        CmcSynchronizedListener {
    @Resource(name = "cmcSystemTimeDao")
    private CmcSystemTimeDao cmcSystemTimeDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;

    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(CmcSynchronizedListener.class, this);
    }

    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(CmcSynchronizedListener.class, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcSystemTimeService#getCmcSystemTimeConfig(java.lang.Long)
     */
    @Override
    public CmcSystemTimeConfig getCmcSystemTimeConfig(Long entityId) {
        return cmcSystemTimeDao.selectCmcSystemTimeConfig(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcSystemTimeService#modifyCmcSystemTimeConfig(com.topvision
     * .ems.cmc.facade.domain.CmcSystemTimeConfig)
     */
    @Override
    public CmcSystemTimeConfig modifyCmcSystemTimeConfig(CmcSystemTimeConfig cmcSystemTimeConfig) {
        Long entityId = cmcSystemTimeConfig.getEntityId();
        snmpParam = getSnmpParamByEntityId(entityId);
        CmcSystemTimeConfig systemTimeConfig = new CmcSystemTimeConfig();
        if (cmcSystemTimeConfig.getTopCcmtsSysTimeSynInterval() == null
                || cmcSystemTimeConfig.getTopCcmtsSysTimeSynInterval() == 0) {
            systemTimeConfig.setTopCcmtsNtpserverAddress(cmcSystemTimeConfig.getTopCcmtsNtpserverAddress());
            systemTimeConfig.setTopCcmtsSysTime(cmcSystemTimeConfig.getTopCcmtsSysTime());
            systemTimeConfig.setTopCcmtsSysTimeZone(cmcSystemTimeConfig.getTopCcmtsSysTimeZone());
            systemTimeConfig.setTopCcmtsSysTimeSynInterval(cmcSystemTimeConfig.getTopCcmtsSysTimeSynInterval());
        } else {
            CmcSystemTimeConfig timeZone = new CmcSystemTimeConfig();
            timeZone.setTopCcmtsSysTimeZone(cmcSystemTimeConfig.getTopCcmtsSysTimeZone());
            getCmcFacade(snmpParam.getIpAddress()).updateCmcSystemTime(snmpParam, timeZone);
            systemTimeConfig.setTopCcmtsNtpserverAddress(cmcSystemTimeConfig.getTopCcmtsNtpserverAddress());
            systemTimeConfig.setTopCcmtsSysTimeSynInterval(cmcSystemTimeConfig.getTopCcmtsSysTimeSynInterval());
        }
        getCmcFacade(snmpParam.getIpAddress()).updateCmcSystemTime(snmpParam, systemTimeConfig);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        systemTimeConfig = getCmcFacade(snmpParam.getIpAddress()).getCmcSystemTime(snmpParam);
        systemTimeConfig.setCollectTime(System.currentTimeMillis());
        systemTimeConfig.setEntityId(entityId);
        cmcSystemTimeDao.updateCmcSystemTimeConfig(systemTimeConfig);
        return systemTimeConfig;
    }

    public CmcSystemTimeDao getCmcSystemTimeDao() {
        return cmcSystemTimeDao;
    }

    public void setCmcSystemTimeDao(CmcSystemTimeDao cmcSystemTimeDao) {
        this.cmcSystemTimeDao = cmcSystemTimeDao;
    }

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        if (event.getEntityType().equals(entityTypeService.getCcmtswithagentType())) {
            try {
                Long entityId = event.getEntityId();
                snmpParam = getSnmpParamByEntityId(entityId);
                CmcSystemTimeConfig systemTimeConfig = getCmcFacade(snmpParam.getIpAddress()).getCmcSystemTime(
                        snmpParam);
                systemTimeConfig.setEntityId(entityId);
                if (cmcSystemTimeDao.selectCmcSystemTimeConfig(entityId) == null) {
                    cmcSystemTimeDao.insertCmcSystemTimeConfig(entityId, systemTimeConfig);
                } else {
                    cmcSystemTimeDao.updateCmcSystemTimeConfig(systemTimeConfig);
                }
                logger.info("refreshCmcSystemTimeConfig finish");
            } catch (Exception e) {
                logger.error("refreshCmcSystemTimeConfig wrong", e);
            }
        }
    }

}
