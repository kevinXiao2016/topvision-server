/***********************************************************************
 * $Id: CmtsDeviceListReportDaoImpl.java,v1.0 2013-11-18 下午1:47:15 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.report.cmtsdevicelist.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmts.report.cmtsdevicelist.dao.CmtsDeviceListReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-11-18-下午1:47:15
 * 
 */
@Repository("cmtsDeviceListReportDao")
public class CmtsDeviceListReportDaoImpl extends MyBatisDaoSupport<Entity> implements CmtsDeviceListReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmts.report.cmtsdevicelist.domain.CmtsDeviceListReport";
    }

    @Override
    public List<CmcAttribute> getDeviceListItem(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getDeviceListItem", map);
    }

}
