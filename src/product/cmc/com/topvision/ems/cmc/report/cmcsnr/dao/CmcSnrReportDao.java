/***********************************************************************
 * $Id: CmcSnrReportDao.java,v1.0 2013-10-29 下午4:38:26 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmcsnr.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CmcSnrReportDetail;
import com.topvision.ems.cmc.report.domain.CmcSnrReportStatistics;
import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author haojie
 * @created @2013-10-29-下午4:38:26
 * 
 */
public interface CmcSnrReportDao {

    /**
     * 获取cmc snr报表
     * 
     * @param relates
     * @param map
     * @return
     */
    Map<String, List<CmcSnrReportStatistics>> statCmcSnrReport(List<TopoEntityStastic> relates, Map<String, Object> map);

    /**
     * 获取cmc snr报表详细
     * 
     * @param map
     * @return
     */
    List<CmcSnrReportDetail> getSnrReportDetail(Map<String, Object> map);
    
    /**
     * 每小时移动SNR数据到汇总表
     */
    void migrateSnrHourly();

    /**
     * 每天按时，按天汇总snr数据
     */
    void summarySnrDaily();

}
