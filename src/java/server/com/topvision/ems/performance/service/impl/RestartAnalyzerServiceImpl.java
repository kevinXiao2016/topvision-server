/***********************************************************************
 * $Id: RestartAnalyzerServiceImpl.java,v1.0 2013-2-21 下午4:49:37 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.performance.dao.RestartAnalyzerDao;
import com.topvision.ems.performance.domain.RestartCount;
import com.topvision.ems.performance.domain.RestartRecord;
import com.topvision.ems.performance.service.RestartAnalyzerService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2013-2-21-下午4:49:37
 * 
 */
@Service("restartAnalyzerService")
public class RestartAnalyzerServiceImpl extends BaseService implements RestartAnalyzerService {
    @Autowired
    private RestartAnalyzerDao restartAnalyzerDao;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public List<Entity> loadEponDeviceList() throws SQLException {
        Long oltType = entityTypeService.getOltType();
        return restartAnalyzerDao.selectEponDeviceList(oltType);
    }

    @Override
    public List<RestartCount> loadRestartStasticData(Map<String, String> map) throws SQLException {
        return restartAnalyzerDao.selectRestartStasticData(map);
    }

    /**
     * @return the restartAnalyzerDao
     */
    public RestartAnalyzerDao getRestartAnalyzerDao() {
        return restartAnalyzerDao;
    }

    /**
     * @param restartAnalyzerDao
     *            the restartAnalyzerDao to set
     */
    public void setRestartAnalyzerDao(RestartAnalyzerDao restartAnalyzerDao) {
        this.restartAnalyzerDao = restartAnalyzerDao;
    }

    @Override
    public List<RestartRecord> loadRestartRecords(Map<String, String> map) throws SQLException {
        List<RestartRecord> records = restartAnalyzerDao.selectRestartRecords(map);
        if (records == null || records.isEmpty()) {
            return records;
        }
        RestartRecord lastRecord;
        if (records.size() == 1) {
            lastRecord = records.get(0);
            lastRecord.setDeviceNextStartTime(System.currentTimeMillis());
            lastRecord.setRunningTimeString(lastRecord.getRunningTimeString() + "(\u81f3\u4eca)");
            return records;
        }
        for (int i = 0; i < records.size() - 1; i++) {
            records.get(i).setDeviceNextStartTime(records.get(i + 1).getDeviceReStartTime());
        }
        lastRecord = records.get(records.size() - 1);
        lastRecord.setDeviceNextStartTime(System.currentTimeMillis());
        lastRecord.setRunningTimeString(lastRecord.getRunningTimeString() + "(\u81f3\u4eca)");
        return records;
    }

    @Override
    public List<RestartCount> loadRestartStatistic(Map<String, String> map) throws SQLException {
        return restartAnalyzerDao.loadRestartStatistic(map);
    }

}
