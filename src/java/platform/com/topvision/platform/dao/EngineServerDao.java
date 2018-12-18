/***********************************************************************
 * $Id: EngineServerDao.java,v 1.1 Jul 19, 2009 12:23:41 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao;

import java.util.List;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.EngineServer;

/**
 * @author kelers
 * @Create Date Jul 19, 2009 12:23:41 PM
 */
public interface EngineServerDao extends BaseEntityDao<EngineServer> {

    /**
     * 获取所有的分布式采集器
     * 
     * @return
     */
    List<EngineServer> getAllEngineServerList();

    /**
     * 批量删除分布式采集器
     * 
     * @return
     */
    void deleteEngineServers(List<Integer> engineServerIds);

    /**
     * 批量启用分布式采集器
     * 
     * @return
     */
    void startEngineServers(List<Integer> engineServerIds);

    /**
     * 批量停用分布式采集器
     * 
     * @return
     */
    void stopEngineServers(List<Integer> engineServerIds);

    /**
     * 更新链路状态
     * 
     * @param server
     */
    void updateLinkStatus(EngineServer server);

    /**
     * 更新管理器状态
     * 
     * @param manageIp
     * @param connect
     */
    void updateManageStatus(String manageIp, byte connect);
    
    /**
     * 更新采集器版本
     * 
     * @param id
     * @param version
     */
    void updateEngineVersion(Integer id, String version);
}
