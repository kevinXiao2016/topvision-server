/***********************************************************************
 * $Id: EntityAliasBakServiceImpl.java,v1.0 2015-12-10 下午4:51:32 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.network.dao.EntityDao;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2015-12-10-下午4:51:33
 *
 */
@Service("entityAliasBakService")
public class EntityAliasBakServiceImpl extends BaseService {
    @Autowired
    private EntityDao entityDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#start()
     */
    @Override
    public void start() {
        entityAliasBak();
    }

    private void entityAliasBak() {
        try {
            entityDao.entityAliasBak();
        } catch (Exception e) {
            logger.error("entityAliasBak error", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#stop()
     */
    @Override
    public void stop() {
        super.stop();
    }
}
