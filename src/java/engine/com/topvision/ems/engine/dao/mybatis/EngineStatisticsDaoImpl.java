/***********************************************************************
 * $Id: EngineStatisticsDaoImpl.java,v1.0 2017年6月15日 下午7:06:02 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import com.topvision.ems.engine.dao.EngineDaoSupport;
import com.topvision.ems.engine.dao.EngineStatisticsDao;
import com.topvision.ems.engine.performance.ExecutorThreadSnap;

/**
 * @author vanzand
 * @created @2017年6月15日-下午7:06:02
 *
 */
public class EngineStatisticsDaoImpl extends EngineDaoSupport<Object> implements EngineStatisticsDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.performance.engine.EngineStatisticsDao#insertExecutorSnap(java.lang.
     * Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Long)
     */
    @Override
    public void insertExecutorSnap(ExecutorThreadSnap snap) {
        getSqlSession().insert(getNameSpace("insertExecutorSnap"), snap);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.performance.engine.mybatis.EngineStatisticsDaoImpl";
    }
}
