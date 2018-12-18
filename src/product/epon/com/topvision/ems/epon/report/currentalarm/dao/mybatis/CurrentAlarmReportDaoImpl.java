/***********************************************************************
 * $Id: CurrentAlarmReportDaoImpl.java,v1.0 2013-10-29 下午2:37:32 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.currentalarm.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.report.currentalarm.dao.CurrentAlarmReportDao;
import com.topvision.ems.epon.report.domain.CurrentAlarmDetail;
import com.topvision.ems.epon.report.domain.CurrentAlarmReport;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.report.domain.FolderEntities;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-下午2:37:32
 * 
 */
@Repository("currentAlarmReportDao")
public class CurrentAlarmReportDaoImpl extends MyBatisDaoSupport<Entity> implements CurrentAlarmReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.CurrentAlarmReport";
    }

    @Override
    public List<CurrentAlarmDetail> statEntityCurrentAlarmDetail(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("statEntityCurrentAlarmDetail"), map);
    }

    @Override
    public Map<Long, FolderEntities> statCurrentAlarmReport(Map<Long, FolderEntities> folderMap, Map<String, Object> map) {
        // 查询出符合条件的设备的告警信息
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        List<CurrentAlarmReport> currentAlarmReports = getSqlSession().selectList(
                getNameSpace("statCurrentAlarmReport"), map);
        // 遍历查询结果，将对应设备插入相应的地域中
        for (CurrentAlarmReport currentAlarmReport : currentAlarmReports) {
            for (Long folderId : folderMap.keySet()) {
                Map<Long, Object> entities = folderMap.get(folderId).getEntities();
                for (Long entityId : entities.keySet()) {
                    if (entityId.equals(currentAlarmReport.getEntityId())) {
                        entities.put(entityId, currentAlarmReport);
                        folderMap.get(folderId).setRowspan(folderMap.get(folderId).getRowspan() + 1);
                    }
                }
            }
        }
        return folderMap;
    }
}
