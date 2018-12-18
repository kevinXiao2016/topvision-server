/***********************************************************************
 * $Id: CcmtsUpChlFlowReportDao.java,v1.0 2014-3-24 下午3:26:02 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsUpChlFlow.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CcmtsChlFlowDetail;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowStatic;
import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author haojie
 * @created @2014-3-24-下午3:26:02
 *
 */
public interface CcmtsUpChlFlowReportDao {

    /**
     * 获取上行流量统计
     * @param relates
     * @param map
     * @return
     */
    Map<String, List<CcmtsChlFlowStatic>> statUpChlFlowStatic(List<TopoEntityStastic> relates, Map<String, Object> map);

    /**
     * 获取上行流量详细
     * @param relates
     * @param map
     * @return
     */
    Map<String, List<CcmtsChlFlowDetail>> statUpChlFlowDetail(List<TopoEntityStastic> relates, Map<String, Object> map);

}
