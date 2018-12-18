/***********************************************************************
 * $Id: UnknownEntityPoll.java,v1.0 2014-1-7 下午3:15:32 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityDao;

/**
 * @author Rod John
 * @created @2014-1-7-下午3:15:32
 * 
 */
public class UnknownEntityPoll {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private EntityDao entityDao;
    @SuppressWarnings("rawtypes")
    private DiscoveryService discoveryService;
    @Value("${performance.unknownPollInterval:1440}")
    private Long pollInterval_;

    public void initialize() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        List<Entity> unknownEntities = entityDao.getAllUnknownEntity();
                        for (Entity entity : unknownEntities) {
                            try {
                                discoveryService.refresh(entity.getEntityId());
                            } catch (Exception e) {
                                logger.info("UnknownEntityPoll: ", e);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("UnknownEntityPoll Run : ", e);
                    }
                    try {
                        Thread.sleep(pollInterval_ * 60 * 1000);
                    } catch (InterruptedException e) {
                        logger.error("UnknownEntityPoll Run : ", e);
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * @return the entityDao
     */
    public EntityDao getEntityDao() {
        return entityDao;
    }

    /**
     * @param entityDao
     *            the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * @return the discoveryService
     */
    @SuppressWarnings("rawtypes")
    public DiscoveryService getDiscoveryService() {
        return discoveryService;
    }

    /**
     * @param discoveryService
     *            the discoveryService to set
     */
    @SuppressWarnings("rawtypes")
    public void setDiscoveryService(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

}
