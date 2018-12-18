/***********************************************************************
 * $Id: PingParamDaoImpl.java,v1.0 2017年3月17日 下午4:17:15 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.engine.mybatis;

import java.util.List;

import com.topvision.ems.engine.dao.EngineDaoSupport;
import com.topvision.ems.performance.domain.PingParam;
import com.topvision.ems.performance.engine.PingParamDao;

/**
 * @author ls
 * @created @2017年3月17日-下午4:17:15
 *
 */
public class PingParamDaoImpl extends EngineDaoSupport<Object> implements PingParamDao{
    @Override
    public List<PingParam> queryPingParamByModule(String module){
        return getSqlSession().selectList(getNameSpace("selectPingParamByModule"), module);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.performance.domain.PingParam";
    }
}
