/***********************************************************************
 * $Id: OltSniPortReportDaoImpl.java,v1.0 2013-10-28 下午2:02:51 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltsniport.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.domain.OltSniAttribute;
import com.topvision.ems.epon.report.oltsniport.dao.OltSniPortReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-28-下午2:02:51
 * 
 */
@Repository("oltSniPortReportDao")
public class OltSniPortReportDaoImpl extends MyBatisDaoSupport<Entity> implements OltSniPortReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.OltSniPortReport";
    }

    @Override
    public List<OltSniAttribute> getSniPortList(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getSniPortList", map);
    }

}
