/***********************************************************************
 * $Id: OltPonPortReportCreatorImpl.java,v1.0 2013-10-28 上午9:59:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltponport.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.report.oltponport.dao.OltPonPortReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-28-上午9:59:39
 * 
 */
@Repository("oltPonPortReportDao")
public class OltPonPortReportDaoImpl extends MyBatisDaoSupport<Entity> implements OltPonPortReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.OltPonPortReport";
    }

    @Override
    public List<OltPonAttribute> getPonPortList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getPonPortList", map);
    }

}
