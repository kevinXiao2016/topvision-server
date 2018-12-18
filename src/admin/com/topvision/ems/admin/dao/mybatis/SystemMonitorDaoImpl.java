/***********************************************************************
 * $Id: SystemMonitorDaoImpl.java,v1.0 2014-6-24 上午9:03:24 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.admin.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.admin.dao.SystemMonitorDao;
import com.topvision.ems.admin.domain.SystemMonitor;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Victor
 * @created @2014-6-24-上午9:03:24
 *
 */
@Repository("systemMonitorDao")
public class SystemMonitorDaoImpl extends MyBatisDaoSupport<SystemMonitor> implements SystemMonitorDao {
    @Override
    protected String getDomainName() {
        return SystemMonitor.class.getName();
    }

    @Override
    public List<SystemMonitor> loadCpuAndMemory(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectCpuAndMemory"), map);
    }
}
