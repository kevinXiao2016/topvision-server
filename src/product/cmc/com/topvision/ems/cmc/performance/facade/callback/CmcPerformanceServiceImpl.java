/***********************************************************************
 * $ CmcPerformanceService.java,v1.0 2012-5-2 10:39:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade.callback;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.epon.olt.service.OltService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;

/**
 * @author jay
 * @created @2012-5-2-10:39:52
 */
@Service("cmcPerformanceService")
public class CmcPerformanceServiceImpl extends BaseService
        implements CmcPerformanceCallback {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    CmcService cmcService;
    @Autowired
    OltService oltService;
    @Autowired
    EntityService entityService;
    @Resource(name = "deviceVersionService")
    private DeviceVersionService deviceVersionService;

    @Override
    public void initialize() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void start() {
    }


    @Override
    public void test() {
    }

    public Boolean isSupportCmPreStatus(Long cmcId) {
        return deviceVersionService.isFunctionSupported(cmcId, "cmPreStatus");
    }

    public Boolean isSupportCmDocsisMode(Long entityId) {
        return deviceVersionService.isFunctionSupported(entityId, "cmDocsisMode");
    }
}