/***********************************************************************
 * $Id: HistoryAlarmReportDaoImpl.java,v1.0 2013-10-29 下午3:20:47 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.historyalarm.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.report.domain.HistoryAlarmDetail;
import com.topvision.ems.epon.report.domain.HistoryAlarmReport;
import com.topvision.ems.epon.report.historyalarm.dao.HistoryAlarmReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.report.domain.FolderEntities;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-下午3:20:47
 * 
 */
@Repository("historyAlarmReportDao")
public class HistoryAlarmReportDaoImpl extends MyBatisDaoSupport<Entity> implements HistoryAlarmReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.HistoryAlarmReport";
    }

    @Override
    public List<HistoryAlarmDetail> statEntityHistoryAlarmDetail(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("statEntityHistoryAlarmDetail"), map);
    }

    @Override
    public Map<Long, FolderEntities> statHistoryAlarmReport(Map<Long, FolderEntities> folderMap, Map<String, Object> map) {
        // 查询出符合条件的设备的告警信息
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        List<HistoryAlarmReport> historyAlarmReports = getSqlSession().selectList(
                getNameSpace("statHistoryAlarmReport"), map);
        // 遍历查询结果，将对应设备插入相应的地域中
        for (HistoryAlarmReport historyAlarmReport : historyAlarmReports) {
            for (Long folderId : folderMap.keySet()) {
                Map<Long, Object> entities = folderMap.get(folderId).getEntities();
                for (Long entityId : entities.keySet()) {
                    if (entityId.equals(historyAlarmReport.getEntityId())) {
                        entities.put(entityId, historyAlarmReport);
                        folderMap.get(folderId).setRowspan(folderMap.get(folderId).getRowspan() + 1);
                    }
                }
            }
        }
        return folderMap;
    }

}
