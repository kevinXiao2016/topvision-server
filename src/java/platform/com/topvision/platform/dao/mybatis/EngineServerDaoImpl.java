/***********************************************************************
 * $Id: EngineServerDaoImpl.java,v 1.1 Jul 19, 2009 12:24:39 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.EngineServerDao;
import com.topvision.platform.domain.EngineServer;

@Repository("engineServerDao")
public class EngineServerDaoImpl extends MyBatisDaoSupport<EngineServer> implements EngineServerDao {
    @Override
    protected String getDomainName() {
        return EngineServer.class.getName();
    }

    @Override
    public List<EngineServer> getAllEngineServerList() {
        return getSqlSession().selectList(getNameSpace("getEngineServerList"));
    }

    @Override
    public void updateLinkStatus(EngineServer server) {
        getSqlSession().update(getNameSpace("updateLinkStatus"), server);
    }

    @Override
    public void deleteEngineServers(List<Integer> engineServerIds) {
        getSqlSession().delete(getNameSpace("deleteEngineServers"), engineServerIds);
    }

    @Override
    public void startEngineServers(List<Integer> engineServerIds) {
        getSqlSession().update(getNameSpace("startEngineServers"), engineServerIds);
    }

    @Override
    public void stopEngineServers(List<Integer> engineServerIds) {
        getSqlSession().update(getNameSpace("stopEngineServers"), engineServerIds);
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dao.EngineServerDao#updateManageStatus(java.lang.String, byte)
     */
    @Override
    public void updateManageStatus(String manageIp, byte connect) {
        EngineServer engineServer = new EngineServer();
        engineServer.setIp(manageIp);
        engineServer.setManageStatus(connect);
        getSqlSession().update(getNameSpace("updateManageStatus"), engineServer);
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.dao.EngineServerDao#updateEngineVersion(java.lang.Integer, java.lang.String)
     */
    @Override
    public void updateEngineVersion(Integer id, String version) {
        EngineServer engineServer = new EngineServer();
        engineServer.setId(id);
        engineServer.setVersion(version);
        getSqlSession().update(getNameSpace("updateEngineVersion"), engineServer);
    }

    
}
