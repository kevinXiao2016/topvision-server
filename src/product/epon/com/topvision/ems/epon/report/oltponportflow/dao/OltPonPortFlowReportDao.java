/***********************************************************************
 * $Id: OltPonPortFlowReportDao.java,v1.0 2013-10-28 上午10:48:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltponportflow.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.report.domain.OltPonPortFlowDetail;
import com.topvision.ems.epon.report.domain.OltPonPortFlowStastic;
import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author haojie
 * @created @2013-10-28-上午10:48:43
 * 
 */
public interface OltPonPortFlowReportDao {

    /**
     * 获取PON口流量报表
     * 
     * @param relates
     * @param map
     * @return
     */
    Map<String, List<OltPonPortFlowStastic>> statPonFlowReport(List<TopoEntityStastic> relates, Map<String, Object> map);

    /**
     * 获取PON口流量报表详细
     * 
     * @param map
     * @return
     */
    List<OltPonPortFlowDetail> selectPonFlowDetail(Map<String, Object> map);

}
