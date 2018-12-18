/***********************************************************************
 * $Id: CmcUserFlowReportDao.java,v1.0 2013-10-29 下午5:07:23 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmcuserflow.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.report.domain.CmcUserFlowReportDetail;
import com.topvision.ems.report.domain.TopoEntityStastic;

/**
 * @author haojie
 * @created @2013-10-29-下午5:07:23
 * 
 */
public interface CmcUserFlowReportDao {

    /**
     * 获取cmc 用户数报表
     * 
     * @param relates
     * @param map
     * @return
     */
    Map<String, Object> statCmcUserFlowReport(List<TopoEntityStastic> relates, Map<String, Object> map);

    /**
     * 获取cmc 用户数报表详情
     * 
     * @param map
     * @return
     */
    Map<String, List<CmcUserFlowReportDetail>> selectUserFlowDetail(Map<String, Object> map);

    /**
     * 每小时移动CM用户数数据到汇总表
     */
    void migrateChannelCmHourly();

    /**
     * 每天按时，按天汇总CM用户数数据
     */
    void summaryChannelCmDaily();

}
