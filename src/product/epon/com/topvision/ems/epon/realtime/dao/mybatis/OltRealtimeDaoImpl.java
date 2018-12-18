/***********************************************************************
 * $Id: OltRealtimeDaoImpl.java,v1.0 2014-7-14 下午4:49:29 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.realtime.dao.OltRealtimeDao;
import com.topvision.ems.performance.domain.PerfThresholdRule;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2014-7-14-下午4:49:29
 *
 */
@Repository("oltRealtimeDao")
public class OltRealtimeDaoImpl extends MyBatisDaoSupport<Object> implements OltRealtimeDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.realtime.domain.OltBaseInfo";
    }

    @Override
    public PerfThresholdRule queryOpticalThreshold(Long entityId, String ruleName) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("targetId", ruleName);
        return this.getSqlSession().selectOne(getNameSpace("queryOpticalThreshold"), paramMap);
    }

    @Override
    public String queryDeviceAlias(Long entityId, String mac) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("mac", new MacUtils(mac).longValue());
        return this.getSqlSession().selectOne(getNameSpace("queryDeviceAlias"), paramMap);
    }

}
