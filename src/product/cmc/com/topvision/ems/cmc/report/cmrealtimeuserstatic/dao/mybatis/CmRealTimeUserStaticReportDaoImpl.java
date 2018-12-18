/***********************************************************************
 * $Id: CmRealTimeUserStaticReporDaoImpl.java,v1.0 2013-10-30 上午10:14:22 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmrealtimeuserstatic.dao.mybatis;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.report.cmrealtimeuserstatic.dao.CmRealTimeUserStaticReportDao;
import com.topvision.ems.cmc.report.domain.CmRealTimeUserStaticReport;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-30-上午10:14:22
 * 
 */
@Repository("cmRealTimeUserStaticReportDao")
public class CmRealTimeUserStaticReportDaoImpl extends MyBatisDaoSupport<Entity> implements
        CmRealTimeUserStaticReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.report.domain.CmRealTimeUserStaticReport";
    }

    @Override
    public List<CmRealTimeUserStaticReport> loadCmRealTimeUserStaticData() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("loadCmRealTimeUserStaticData"), map);
    }

}
