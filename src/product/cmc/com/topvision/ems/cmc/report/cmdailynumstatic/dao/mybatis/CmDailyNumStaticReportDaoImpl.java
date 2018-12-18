/***********************************************************************
 * $Id: CmDailyNumStaticReportDaoImpl.java,v1.0 2013-10-30 上午8:37:38 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmdailynumstatic.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.report.cmdailynumstatic.dao.CmDailyNumStaticReportDao;
import com.topvision.ems.cmc.report.domain.CmDailyNumStaticReport;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-30-上午8:37:38
 * 
 */
@Repository("cmDailyNumStaticReportDao")
public class CmDailyNumStaticReportDaoImpl extends MyBatisDaoSupport<Entity> implements CmDailyNumStaticReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.report.domain.CmDailyNumStaticReport";
    }

    @Override
    public List<Map<String, String>> loadIdAndNamePairsFromTable(String tableName, Integer deviceType) {
        List<Map<String, String>> resultMapList = null;
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("deviceType", deviceType);
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        resultMapList = getSqlSession().selectList(getNameSpace("loadIdAndNamePairsFromEntity"), queryMap);
        return resultMapList;
    }

    @Override
    public List<CmDailyNumStaticReport> loadCmcDailyMaxCmNum(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("loadCmcDailyMaxCmNum"), map);
    }

}
