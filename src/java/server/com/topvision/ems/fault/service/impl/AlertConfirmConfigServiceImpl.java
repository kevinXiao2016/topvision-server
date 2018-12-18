/***********************************************************************
 * $Id: AlertConfirmConfigServiceImpl.java,v1.0 2012-3-27 下午06:48:41 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.dao.AlertConfirmConfigDao;
import com.topvision.ems.fault.dao.AlertTypeDao;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.Event2Alert;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.ems.fault.service.AlertConfirmConfigService;
import com.topvision.framework.service.BaseService;

/**
 * @author huqiao
 * @created @2012-3-27-下午06:48:41
 * 
 */
@Service("alertConfirmConfigService")
public class AlertConfirmConfigServiceImpl extends BaseService implements AlertConfirmConfigService {
    @Autowired
    private AlertConfirmConfigDao alertConfirmConfigDao;
    @Autowired
    private AlertTypeDao alertTypeDao;
    private Boolean alertConfirmConfig;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        alertConfirmConfig = alertConfirmConfigDao.getAlertConfirmConfig();
    }

    /**
     * @return the alertConfirmConfigDao
     */
    public AlertConfirmConfigDao getAlertConfirmConfigDao() {
        return alertConfirmConfigDao;
    }

    /**
     * @param alertConfirmConfigDao
     *            the alertConfirmConfigDao to set
     */
    public void setAlertConfirmConfigDao(AlertConfirmConfigDao alertConfirmConfigDao) {
        this.alertConfirmConfigDao = alertConfirmConfigDao;
    }

    /**
     * @return the alertTypeDao
     */
    public AlertTypeDao getAlertTypeDao() {
        return alertTypeDao;
    }

    /**
     * @param alertTypeDao
     *            the alertTypeDao to set
     */
    public void setAlertTypeDao(AlertTypeDao alertTypeDao) {
        this.alertTypeDao = alertTypeDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.AlertConfirmConfigService#getConfirmEventByEventTypeId(java
     * .lang.Long)
     */
    @Override
    public List<EventType> getConfirmEventByEventTypeId(Integer eventTypeId) {
        return alertConfirmConfigDao.getConfirmEventByEventTypeId(eventTypeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.AlertConfirmConfigService#getEvent2AlertById(java.lang.Integer
     * , java.lang.Integer)
     */
    @Override
    public Event2Alert getEvent2AlertById(Integer eventTypeId, Integer alertTypeId) {
        // return alertConfirmConfigDao.getEvent2AlertById(eventTypeId, alertTypeId);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.AlertConfirmConfigService#insertConfirmAlert(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void insertConfirmAlert(Integer alertId, Integer confirmAlertId) {
        alertConfirmConfigDao.insertConfirmAlert(confirmAlertId, alertId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.AlertConfirmConfigService#deleteConfirmAlert(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void deleteConfirmAlert(Integer alertId, Integer confirmAlertId) {
        alertConfirmConfigDao.deleteConfirmAlert(confirmAlertId, alertId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertConfirmConfigService#getAllEventTypeNoAlert()
     */
    @Override
    public List<EventType> getAllEventTypeNoAlert() {
        return alertConfirmConfigDao.getAllEventTypeNoAlert();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.AlertConfirmConfigService#insertAlertType(com.topvision.ems
     * .fault.domain.AlertType)
     */
    @Override
    public void insertAlertType(AlertType alertType) {
        alertTypeDao.insertEntity(alertType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.AlertConfirmConfigService#insertOriginAlert(java.lang.Integer
     * , java.lang.Integer)
     */
    @Override
    public void insertOriginAlert(Integer eventTypeId, Integer alertTypeId) {
        alertConfirmConfigDao.insertOriginAlert(eventTypeId, alertTypeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.AlertConfirmConfigService#deleteOriginAlert(java.lang.Integer
     * , java.lang.Integer)
     */
    @Override
    public void deleteOriginAlert(Integer eventTypeId, Integer alertTypeId) {
        alertConfirmConfigDao.deleteOriginAlert(eventTypeId, alertTypeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.AlertConfirmConfigService#getOriginEventByEventTypeId(java
     * .lang.Integer)
     */
    @Override
    public List<EventType> getOriginEventByEventTypeId(Integer alertTypeId) {
        return alertConfirmConfigDao.getOriginEventByEventTypeId(alertTypeId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertConfirmConfigService#getAlertConfirmConfig()
     */
    @Override
    public Boolean getAlertConfirmConfig() {
        return this.alertConfirmConfig;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.service.AlertConfirmConfigService#updateAlertConfirmConfig(java.lang
     * .Boolean)
     */
    @Override
    public void updateAlertConfirmConfig(Boolean alertConfirmConfig) {
        alertConfirmConfigDao.updateAlertConfirmConfig(alertConfirmConfig);
        this.alertConfirmConfig = alertConfirmConfig;
    }

    /**
     * @param alertConfirmConfig
     *            the alertConfirmConfig to set
     */
    public void setAlertConfirmConfig(Boolean alertConfirmConfig) {
        this.alertConfirmConfig = alertConfirmConfig;
    }

}
