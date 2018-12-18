/***********************************************************************
 * $Id: OltSniPortFlowReportDao.java,v1.0 2013-10-28 下午3:20:56 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltsniportflow.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.report.domain.OltSniPortFlowDetail;
import com.topvision.ems.epon.report.domain.OltSniPortFlowStastic;
import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author haojie
 * @created @2013-10-28-下午3:20:56
 * 
 */
public interface OltSniPortFlowReportDao {

    /**
     * 获取SNI流量报表
     * 
     * @param relates
     * @param map
     * @return
     */
    Map<String, List<OltSniPortFlowStastic>> statSniFlowReport(List<TopoEntityStastic> relates, Map<String, Object> map);

    /**
     * 获取SNI流量报表详细
     * 
     * @param map
     * @return
     */
    List<OltSniPortFlowDetail> selectSniFlowDetail(Map<String, Object> map);

    /**
     * 每小时移动端口流量数据到汇总表
     */
    void migrateFlowHourly();

    /**
     * 每天按时，按天汇总端口流量数据
     */
    void summaryFlowDaily();

}
