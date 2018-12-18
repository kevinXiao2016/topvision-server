/***********************************************************************
 * $Id: TrapServerConfigDao.java,v1.0 2013-4-23 下午2:43:17 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.trapserver.dao;

import java.util.List;

import com.topvision.ems.cmc.trapserver.facade.domain.CmcTrapServer;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-4-23-下午2:43:17
 *
 */
public interface CmcTrapServerConfigDao extends BaseEntityDao<CmcTrapServer> {

    /**
     * 从数据库获取TrapServer配置
     * @param entityId
     * @return
     */
    public List<CmcTrapServer> getAllTrapServer(Long entityId);

    /**
     * 添加trapServer配置
     * @param trapServer
     */
    public void insertTrapServer(CmcTrapServer trapServer);

    /**
     * 删除TrapServer配置
     * @param trapServer
     */
    public void deleteTrapServer(CmcTrapServer trapServer);

    /**
     * 将从设备获取到的TrapServer配置数据批量插入数据库
     * @param entityId
     * @param trapServerList
     */
    public void batchInsertTrapServer(Long entityId, final List<CmcTrapServer> trapServerList);

}
