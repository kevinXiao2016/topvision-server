/***********************************************************************
 * $Id: OltSniPortReportDao.java,v1.0 2013-10-28 下午2:02:22 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltsniport.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.olt.domain.OltSniAttribute;


/**
 * @author haojie
 * @created @2013-10-28-下午2:02:22
 * 
 */
public interface OltSniPortReportDao {

    /**
     * 获取SNI端口报表
     * 
     * @param map
     * @return
     */
    List<OltSniAttribute> getSniPortList(Map<String, Object> map);

}
