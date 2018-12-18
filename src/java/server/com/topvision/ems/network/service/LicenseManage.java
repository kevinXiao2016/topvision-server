/***********************************************************************
 * $Id: LicenseManage.java,v1.0 2014-1-4 下午4:52:10 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.domain.Level;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.license.common.domain.Module;
import com.topvision.platform.service.LicenseService;
import com.topvision.platform.service.impl.LicenseServiceImpl;

/**
 * @author Administrator
 * @created @2014-1-4-下午4:52:10
 * 
 */
public class LicenseManage {
    private static Integer LICENSE_EVENT = -400;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private LicenseService licenseService;
    private EntityDao entityDao;

    public void initialize() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        for (Entry<String, EntityType> entry : LicenseServiceImpl.entityTypeMap.entrySet()) {
                            Module licenseModule = licenseService.getLicenseModule(entry.getValue().getTypeId());
                            if (licenseModule != null) {
                                int count = entityDao.selectEntityIdsByType(entry.getValue().getTypeId()).size();
                                int limit = (int) (Integer.valueOf(licenseModule.getNumberOfEntities()) * 0.8);
                                if (count >= limit) {
                                    Event event = EventSender.getInstance().createEvent(LICENSE_EVENT, "", "NM3000");
                                    event.setLevelId(Level.CRITICAL_LEVEL);
                                    event.setMessage("License Wrong");
                                    EventSender.getInstance().send(event);
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("LicenseManage Run : ", e);
                    }
                    try {
                        Thread.sleep(60 * 2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * @return the licenseService
     */
    public LicenseService getLicenseService() {
        return licenseService;
    }

    /**
     * @param licenseService
     *            the licenseService to set
     */
    public void setLicenseService(LicenseService licenseService) {
        this.licenseService = licenseService;
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

}
