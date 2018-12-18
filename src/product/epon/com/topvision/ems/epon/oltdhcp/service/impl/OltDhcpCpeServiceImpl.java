/***********************************************************************
 * $Id: OltDhcpCpeServiceImpl.java,v1.0 2017年11月22日 下午2:18:17 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpCpeDao;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpCpeInfo;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpCpeService;
import com.topvision.ems.epon.oltdhcp.service.OltDhcpRefreshService;
import com.topvision.framework.service.BaseService;

/**
 * @author haojie
 * @created @2017年11月22日-下午2:18:17
 *
 */
@Service("oltDhcpCpeService")
public class OltDhcpCpeServiceImpl extends BaseService implements OltDhcpCpeService {
    @Autowired
    private OltDhcpCpeDao oltDhcpCpeDao;
    @Autowired
    private OltDhcpRefreshService oltDhcpRefreshService;

    @Override
    public List<TopOltDhcpCpeInfo> getOltDhcpCpeInfo(Map<String, Object> queryMap) {
        return oltDhcpCpeDao.getOltDhcpCpeInfo(queryMap);
    }

    @Override
    public Long getOltDhcpCpeInfoCount(Map<String, Object> queryMap) {
        return oltDhcpCpeDao.getOltDhcpCpeInfoCount(queryMap);
    }

    @Override
    public void refreshOltDhcpCpeInfo(Long entityId) {
        oltDhcpRefreshService.refreshTopOltDhcpCpeInfo(entityId);
    }

}
