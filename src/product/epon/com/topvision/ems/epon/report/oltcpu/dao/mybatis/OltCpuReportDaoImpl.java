/***********************************************************************
 * $Id: OltCpuReportDaoImpl.java,v1.0 2013-10-26 上午11:38:18 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltcpu.dao.mybatis;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.PerforamanceRank;
import com.topvision.ems.epon.report.oltcpu.dao.OltCpuReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-26-上午11:38:18
 * 
 */
@Repository("oltCpuReportDao")
public class OltCpuReportDaoImpl extends MyBatisDaoSupport<Entity> implements OltCpuReportDao {

    @Override
    public List<PerforamanceRank> getEponCpuRank(Long type) {
        HashMap<String, Object> authorityHashMap = new HashMap<String, Object>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        authorityHashMap.put("type", type);
        return getSqlSession().selectList(getNameSpace() + "getEponCpuRank", authorityHashMap);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.OltCpuReport";
    }

}
