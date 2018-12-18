/***********************************************************************
 * $Id: AlertConfirmConfigDaoImpl.java,v1.0 2012-3-27 下午06:53:06 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.AlertConfirmConfigDao;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.EventType;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author huqiao
 * @created @2012-3-27-下午06:53:06
 * 
 */

@Repository("alertConfirmConfigDao")
public class AlertConfirmConfigDaoImpl extends MyBatisDaoSupport<AlertType> implements AlertConfirmConfigDao {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.dao.AlertConfirmConfigDao#getConfirmAlertByAlertTypeId(java.lang.
     * Long)
     */
    @Override
    public List<EventType> getConfirmEventByEventTypeId(Integer alertTypeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("alertTypeId", alertTypeId);
        return getSqlSession().selectList(getNameSpace() + "getConfirmEventByEventTypeId", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertConfirmConfigDao#insertConfirmAlert(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void insertConfirmAlert(Integer eventTypeId, Integer confirmAlertId) {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("eventTypeId", eventTypeId);
        params.put("alertTypeId", confirmAlertId);
        getSqlSession().insert(getNameSpace() + "insertConfirmAlert", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertConfirmConfigDao#deleteConfirmAlert(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void deleteConfirmAlert(Integer eventTypeId, Integer confirmAlertId) {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("eventTypeId", eventTypeId);
        params.put("alertTypeId", confirmAlertId);
        getSqlSession().insert(getNameSpace() + "deleteConfirmAlert", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.dao.AlertConfirmConfigDao#getAllEventTypeNoAlert(java.lang.Integer)
     */
    @Override
    public List<EventType> getAllEventTypeNoAlert() {
        return getSqlSession().selectList(getNameSpace() + "getAllEventTypeNoAlert");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertConfirmConfigDao#insertOriginAlert(java.lang.Integer,
     * java.lang.Integer)
     */
    @Override
    public void insertOriginAlert(Integer eventTypeId, Integer alertTypeId) {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("eventTypeId", eventTypeId);
        params.put("alertTypeId", alertTypeId);
        getSqlSession().insert(getNameSpace() + "insertOriginAlert", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.dao.AlertConfirmConfigDao#deleteOriginAlert(java.lang.Integer,
     * java.lang.Integer)
     */
    @Override
    public void deleteOriginAlert(Integer eventTypeId, Integer alertTypeId) {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("eventTypeId", eventTypeId);
        params.put("alertTypeId", alertTypeId);
        getSqlSession().delete(getNameSpace() + "deleteOriginAlert", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.dao.AlertConfirmConfigDao#getOriginEventByEventTypeId(java.lang.Integer
     * )
     */
    @Override
    public List<EventType> getOriginEventByEventTypeId(Integer alertTypeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("alertTypeId", alertTypeId);
        return getSqlSession().selectList(getNameSpace() + "getOriginEventByEventTypeId", params);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.AlertConfirmConfig";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.fault.dao.AlertConfirmConfigDao#getAlertConfirmConfig()
     */
    @Override
    public Boolean getAlertConfirmConfig() {
        return this.getSqlSession().selectOne(getNameSpace("getAlertConfirmConfig"));
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.fault.dao.AlertConfirmConfigDao#updateAlertConfirmConfig(java.lang.Boolean)
     */
    @Override
    public void updateAlertConfirmConfig(Boolean alertConfirmConfig) {
        getSqlSession().update(getNameSpace("updateAlertConfirmConfig"), alertConfirmConfig);
    }

}
