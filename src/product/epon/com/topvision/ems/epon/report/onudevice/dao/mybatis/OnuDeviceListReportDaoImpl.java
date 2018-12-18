/***********************************************************************
 * $Id: OnuDeviceListReportDaoImpl.java,v1.0 2013-10-28 下午4:45:59 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.onudevice.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.report.onudevice.dao.OnuDeviceListReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-28-下午4:45:59
 * 
 */
@Repository("onuDeviceListReportDao")
public class OnuDeviceListReportDaoImpl extends MyBatisDaoSupport<Entity> implements OnuDeviceListReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.OnuDeviceListReport";
    }

    @Override
    public List<OltOnuAttribute> getOnuDeviceListItem(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace() + "getOnuDeviceListItem", map);
    }

}
