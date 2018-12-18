/***********************************************************************
 * $Id: PnmpCmtsReportDao.java,v1.0 2017年8月8日 下午2:58:17 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmtsReport;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午2:58:17
 *
 */
public interface PnmpCmtsReportDao extends BaseEntityDao<Object> {

    /**
     * 获取分析报告
     * 
     * @return
     */
    List<PnmpCmtsReport> selectReports();

    void insertOrUpdateReport(PnmpCmtsReport cmtsReport);

    /**
     * 获取分析报告总数
     * 
     * @return
     */
    Integer selectCmtsReportsNums();

    /**
     * 在线CM总数
     * 
     * @return
     */
    Integer selectOnlineCmNums();

    /**
     * 健康CM总数
     * 
     * @return
     */
    Integer selectHealthCmNums();

    /**
     * 轻度劣化倾向CM总数
     * 
     * @return
     */
    Integer selectMarginalCmNums();

    /**
     * 严重劣化倾向CM总数
     * 
     * @return
     */
    Integer selectBadCmNums();

    /**
     * 获取单个cmts 分析报告
     * 
     * @param cmcId
     * @return
     */
    PnmpCmtsReport selectCmtsReportByMap(Map<String, Object> queryMap);

    /**
     * 汇总CMTS报告
     */
    void summaryCmtsReport();
}
