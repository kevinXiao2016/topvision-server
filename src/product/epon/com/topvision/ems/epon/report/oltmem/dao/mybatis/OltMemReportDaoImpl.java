/***********************************************************************
 * $Id: OltMemReportDaoImpl.java,v1.0 2013-10-26 下午5:15:13 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltmem.dao.mybatis;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.PerforamanceRank;
import com.topvision.ems.epon.report.oltmem.dao.OltMemReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-26-下午5:15:13
 * 
 */
@Repository("OltMemReportDao")
public class OltMemReportDaoImpl extends MyBatisDaoSupport<Entity> implements OltMemReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.OltMemReport";
    }

    @Override
    public List<PerforamanceRank> getEponMemRank(Long type) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("type", type);
        return getSqlSession().selectList(getNameSpace() + "getEponMemRank", authorityHashMap);
    }

}
