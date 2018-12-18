/***********************************************************************
 * $Id: DemoDaoImpl.java,v1.0 2015年3月11日 上午9:32:51 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.demo.dao.mybatis;

import com.topvision.ems.engine.dao.EngineDaoSupport;
import com.topvision.ems.engine.demo.dao.DemoEngineDao;
import com.topvision.platform.domain.EngineServer;

/**
 * @author Victor
 * @created @2015年3月11日-上午9:32:51
 *
 */
public class DemoEngineDaoImpl extends EngineDaoSupport<EngineServer> implements DemoEngineDao {
    @Override
    public String getDomainName() {
        return "com.topvision.platform.domain.EngineServer";
    }

    @Override
    public EngineServer test() {
        return getSqlSession().selectOne(getNameSpace() + "getEntity");
    }
}
