/***********************************************************************
 * $Id: CmtsAlertDaoImpl.java,v1.0 2013-10-22 下午6:46:57 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmts.dao.CmtsAlertDao;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2013-10-22-下午6:46:57
 *
 */
@Repository("cmtsAlertDao")
public class CmtsAlertDaoImpl extends MyBatisDaoSupport<Alert> implements CmtsAlertDao{
    protected String getDomainName() {
        return "com.topvision.ems.cmts.domain.CmtsAlert";
    }
    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmts.dao.CmtsAlertDao#selectCmtsAlertType()
     */
    public List<AlertType> selectCmtsAlertType() {
        return getSqlSession().selectList(getNameSpace("selectCmtsAlertType"));
    }
    
    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmts.dao.CmtsAlertDao#selectCmtsAlertList(java.util.Map, java.lang.Integer, java.lang.Integer)
     */
    public List<Alert> selectCmtsAlertList(Map<String, Object> map, Integer start, Integer limit) {
        map.put("start", start);
        map.put("limit", limit);
        return getSqlSession().selectList(getNameSpace("selectCmtsAlertList"), map);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmts.dao.CmtsAlertDao#selectCmtsAlertListNum(java.util.Map)
     */
    public Integer selectCmtsAlertListNum(Map<String, Object> map) {
        return (Integer) getSqlSession().selectOne(getNameSpace("selectCmtsAlertListNum"), map);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmts.dao.CmtsAlertDao#selectCmtsHistoryAlertList(java.util.Map, java.lang.Integer, java.lang.Integer)
     */
    public List<HistoryAlert> selectCmtsHistoryAlertList(Map<String, Object> map, Integer start, Integer limit) {
        map.put("start", start);
        map.put("limit", limit);
        return getSqlSession().selectList(getNameSpace("selectCmtsHistoryAlertList"), map);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmts.dao.CmtsAlertDao#selectCmtsHistoryAlertListNum(java.util.Map)
     */
    public Integer selectCmtsHistoryAlertListNum(Map<String, Object> map) {
        return (Integer) getSqlSession().selectOne(getNameSpace("selectCmtsHistoryAlertListNum"), map);
    }

}
