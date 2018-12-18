/***********************************************************************
 * $Id: CcmtsDeviceListReportDaoImpl.java,v1.0 2013-10-29 上午9:25:11 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsdevicelist.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.report.ccmtsdevicelist.dao.CcmtsDeviceListReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-29-上午9:25:11
 * 
 */
@Repository("ccmtsDeviceListReportDao")
public class CcmtsDeviceListReportDaoImpl extends MyBatisDaoSupport<Entity> implements CcmtsDeviceListReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.report.domain.CcmtsDeviceListReport";
    }

    @Override
    public List<CmcAttribute> getDeviceListItem(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getDeviceListItem", map);
    }

}
