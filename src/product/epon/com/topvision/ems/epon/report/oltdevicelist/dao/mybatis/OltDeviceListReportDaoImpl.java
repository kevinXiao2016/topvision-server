/***********************************************************************
 * $Id: OltDeviceListReportDaoImpl.java,v1.0 2013-10-26 下午2:58:15 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltdevicelist.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.domain.DeviceListItem;
import com.topvision.ems.epon.report.oltdevicelist.dao.OltDeviceListReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author haojie
 * @created @2013-10-26-下午2:58:15
 * 
 */
@Repository("oltDeviceListReportDao")
public class OltDeviceListReportDaoImpl extends MyBatisDaoSupport<Entity> implements OltDeviceListReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.OltDeviceListReport";
    }

    @Override
    public List<DeviceListItem> getDeviceListItem(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        if (map.get("sortName").equals("ip")) {
            return getSqlSession().selectList(getNameSpace() + "getDeviceListItemSortIp", map);
        } else {
            return getSqlSession().selectList(getNameSpace() + "getDeviceListItem", map);
        }
    }

}
