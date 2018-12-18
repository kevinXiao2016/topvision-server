/***********************************************************************
 * $Id: ReportInstanceDao.java,v1.0 2013-6-19 下午4:19:11 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.report.domain.ReportInstance;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013-6-19-下午4:19:11
 * 
 */
public interface ReportInstanceDao extends BaseEntityDao<Entity> {

    /**
     * 添加一个报表结果
     * 
     * @param report
     */
    void insertReportInstance(ReportInstance report);

    /**
     * 批量删除报表
     * 
     * @param reportIds
     */
    void deleteReport(List<Long> reportIds);

    /**
     * 根据报表ID获取报表信息
     * 
     * @param reportId
     * @return
     */
    ReportInstance getReportDetailByReportId(Long reportId);

    /**
     * 加载实例列表
     * 
     * @param queryMap
     * @return
     */
    List<ReportInstance> getReportInstanceList(Map<String, Object> queryMap);

    /**
     * 获取实例列表数目
     * 
     * @param queryMap
     * @return
     */
    Integer getReportInstanceNum(Map<String, Object> queryMap);

}
