/***********************************************************************
 * $Id: SpectrumDiscoveryServiceImpl.java,v1.0 2014-3-5 上午9:58:09 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.service.impl.CmcBaseCommonService;
import com.topvision.ems.cmc.spectrum.dao.SpectrumDiscoveryDao;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumCfg;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumOltSwitch;
import com.topvision.ems.cmc.spectrum.service.SpectrumDiscoveryService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.platform.message.event.CmcSynchronizedEvent;
import com.topvision.platform.message.event.CmcSynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author haojie
 * @created @2014-3-5-上午9:58:09
 *
 */
@Service("spectrumDiscoveryService")
public class SpectrumDiscoveryServiceImpl extends CmcBaseCommonService implements SpectrumDiscoveryService,
        BeanFactoryAware, CmcSynchronizedListener {
    @Autowired
    private MessageService messageService;
    private BeanFactory beanFactory;
    @Autowired
    private SpectrumDiscoveryDao spectrumDiscoveryDao;
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

    @Override
    public void insertEntityStates(CmcSynchronizedEvent event) {
        if (event.getEntityType().equals(entityTypeService.getCcmtswithoutagentType())) {
            //对于类A型CC刷新上联OLT的频谱开关
            try {
                refreshSpectrumOlt(event.getEntityId());
                logger.info("refreshSpectrumOlt finish");
            } catch (Exception e) {
                logger.error("refreshSpectrumOlt wrong ", e);
            }
        }
        if (event.getEntityType().equals(entityTypeService.getCcmtswithoutagentType())
                || event.getEntityType().equals(entityTypeService.getCcmtswithagentType())) {
            //刷新频谱CMTS相关配置
            try {
                refreshSpectrumCmts(event.getEntityId());
                logger.info("refreshSpectrumCmts finish");
            } catch (Exception e) {
                logger.error("refreshSpectrumCmts wrong ", e);
            }
        }
    }

    @Override
    public void refreshSpectrumCmts(Long entityId) {
        snmpParam = this.getSnmpParamByEntityId(entityId);
        List<SpectrumCfg> spectrumCfgs = getSpectrumFacade(snmpParam.getIpAddress()).getSpectrumCfg(snmpParam);
        if (spectrumCfgs != null) {
            spectrumDiscoveryDao.inserSpectrumCfg(entityId, spectrumCfgs);
        }
    }

    @Override
    public void refreshSpectrumOlt(Long entityId) {
        snmpParam = this.getSnmpParamByEntityId(entityId);
        SpectrumOltSwitch spectrumOltSwitch = getSpectrumFacade(snmpParam.getIpAddress()).getSpectrumOltSwitch(
                snmpParam);
        if (spectrumOltSwitch != null) {
            spectrumOltSwitch.setEntityId(entityId);
            spectrumDiscoveryDao.insertSpectrumOltSwitch(spectrumOltSwitch);
        }
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
