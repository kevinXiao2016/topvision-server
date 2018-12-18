/***********************************************************************
 * $Id: CmtsInfoSummaryMaintainDao.java,v1.0 2017年9月12日 下午4:41:34 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmtsInfoSummary.dao;

import java.util.List;

import com.topvision.ems.cm.cmtsInfoSummary.domain.CmtsInfoMaintain;

/**
 * @author admin
 * @created @2017年9月12日-下午4:41:34
 *
 */
public interface CmtsInfoSummaryMaintainDao {
    /**
     * 插入cmts概况到数据库到last
     * @param
     * @return void
     */
    void batchInsertCmtsInfoMaintainLast(List<CmtsInfoMaintain> cmtsInfoMaintain);
    /**
     * 插入cmts概况到数据库到汇总
     * @param
     * @return void
     */
    void batchInsertCmtsInfoMaintain(List<CmtsInfoMaintain> cmtsInfoMaintain);
}
