/***********************************************************************
 * $Id: OltResponseReportDaoImpl.java,v1.0 2013-10-28 上午11:40:59 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltresponse.dao.mybatis;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.PerforamanceRank;
import com.topvision.ems.epon.report.oltresponse.dao.OltResponseReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-28-上午11:40:59
 * 
 */
@Repository("oltResponseReportDao")
public class OltResponseReportDaoImpl extends MyBatisDaoSupport<Entity> implements OltResponseReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.OltResponseReport";
    }

    @Override
    public List<PerforamanceRank> getEponDelayRank(Long type) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("type", type);
        return getSqlSession().selectList(getNameSpace() + "getEponDelayRank", authorityHashMap);
    }

}
