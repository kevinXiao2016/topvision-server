/***********************************************************************
 * $Id: ReportTemplateDao.java,v1.0 2013-6-18 下午5:44:23 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao;

import java.util.List;

import com.topvision.ems.report.domain.ReportTemplate;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013-6-18-下午5:44:23
 * 
 */
public interface ReportTemplateDao extends BaseEntityDao<ReportTemplate> {

    List<ReportTemplate> getReportTemplate();

    /**
     * 更新报表模版的显示
     * 
     * @param reportTemplates
     */
    void updateReportDisplay(List<ReportTemplate> reportTemplates);

}
