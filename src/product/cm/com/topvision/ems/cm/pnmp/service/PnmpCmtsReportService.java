/***********************************************************************
 * $Id: PnmpCmtsReportService.java,v1.0 2017年8月8日 下午4:36:32 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service;

import java.util.List;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmtsReport;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:36:32
 *
 */
public interface PnmpCmtsReportService {

    List<PnmpCmtsReport> selectReports();

    void summaryCmtsReport();

    /**
     * 获取分析报表总数
     * 
     * @return
     */
    Integer selectCmtsReportsNums();

    /**
     * 在线CM总数
     * 
     * @return
     */
    Integer getOnlineCmNums();

    /**
     * 健康CM总数
     * 
     * @return
     */
    Integer getHealthCmNums();

    /**
     * 轻度劣化倾向CM总数
     * 
     * @return
     */
    Integer getMarginalCmNums();

    /**
     * 严重劣化倾向CM总数
     * 
     * @return
     */
    Integer getBadCmNums();

}
