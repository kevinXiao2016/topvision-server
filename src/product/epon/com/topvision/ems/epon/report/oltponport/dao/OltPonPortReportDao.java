/***********************************************************************
 * $Id: OltPonPortReportDao.java,v1.0 2013-10-28 上午9:59:10 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltponport.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;


/**
 * @author haojie
 * @created @2013-10-28-上午9:59:10
 * 
 */
public interface OltPonPortReportDao {

    /**
     * 获取OLT pon 报表信息
     * 
     * @param map
     * @return
     */
    List<OltPonAttribute> getPonPortList(Map<String, Object> map);

}
