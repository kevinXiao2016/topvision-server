/***********************************************************************
 * $Id: CmReportDaoImpl.java,v1.0 2013-10-29 下午4:06:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cm.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.report.cm.dao.CmReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-下午4:06:39
 * 
 */
@Repository("cmReportDao")
public class CmReportDaoImpl extends MyBatisDaoSupport<Entity> implements CmReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.report.domain.CmReport";
    }

    @Override
    public List<CmAttribute> getCmBasicInfoList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("getCmBasicInfoList"), map);
    }

    @Override
    public List<Map<String, String>> loadIdAndNamePairsFromTable(String tableName, Long deviceType) {
        List<Map<String, String>> resultMapList = null;
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("deviceType", deviceType);
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        resultMapList = getSqlSession().selectList(getNameSpace("loadIdAndNamePairsFromEntity"), queryMap);
        return resultMapList;
    }

}
