/***********************************************************************
 * $Id: DemoEngine.java,v1.0 2015年3月16日 下午4:47:25 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.demo;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.BaseEngine;
import com.topvision.ems.engine.dao.EngineDaoFactory;
import com.topvision.ems.engine.demo.dao.DemoEngineDao;
import com.topvision.ems.engine.demo.dao.DemoHsqlDao;
import com.topvision.framework.annotation.Engine;

/**
 * @author Victor
 * @created @2015年3月16日-下午4:47:25
 *
 */
@Engine("demoEngine")
public class DemoEngine extends BaseEngine {
    @Autowired
    private EngineDaoFactory engineDaoFactory;

    @Override
    public void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} initialize invoked.", getClass());
        }
    }

    @Override
    public void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("{} destroy invoked.", getClass());
        }
    }

    @Override
    public void connected() {
        super.connected();
        DemoEngineDao demoDao = engineDaoFactory.getEngineDao(DemoEngineDao.class);
        if (demoDao != null) {
            logger.debug("DemoEngineDao:{}", demoDao.test());
        }
        DemoHsqlDao hsqlDao = engineDaoFactory.getEngineDao(DemoHsqlDao.class);
        if (hsqlDao != null) {
            logger.debug("DemoHsqlDao:{}", hsqlDao.getVersion());
        }
        logger.debug("DemoEngine.Server={}:{}", serviceIp, servicePort);
    }

    @Override
    public void disconnected() {
        super.disconnected();
    }
}
