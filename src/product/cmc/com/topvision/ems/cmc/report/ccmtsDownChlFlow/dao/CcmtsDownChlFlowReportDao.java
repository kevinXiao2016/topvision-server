/***********************************************************************
 * $Id: CcmtsDownChlFlowReportDao.java,v1.0 2014-3-24 下午3:31:18 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsDownChlFlow.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CcmtsChlFlowDetail;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowStatic;
import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author haojie
 * @created @2014-3-24-下午3:31:18
 *
 */
public interface CcmtsDownChlFlowReportDao {

    /**
     * 获取下行信道流量信息
     * @param relates
     * @param map
     * @return
     */
    Map<String, List<CcmtsChlFlowStatic>> statDownChlFlowStatic(List<TopoEntityStastic> relates, Map<String, Object> map);

    /**
     * 获取下行信道流量详细
     * @param relates
     * @param map
     * @return
     */
    Map<String, List<CcmtsChlFlowDetail>> statDownChlFlowDetail(List<TopoEntityStastic> relates, Map<String, Object> map);

}
