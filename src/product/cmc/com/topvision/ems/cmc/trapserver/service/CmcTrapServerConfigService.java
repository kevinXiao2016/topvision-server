/***********************************************************************
 * $Id: TrapServerConfigService.java,v1.0 2013-4-23 下午2:06:32 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.trapserver.service;

import java.util.List;

import com.topvision.ems.cmc.trapserver.facade.domain.CmcTrapServer;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-4-23-下午2:06:32
 *
 */
public interface CmcTrapServerConfigService extends Service {

    /**
     * 从数据库获取当前设备的TrapServer配置信息
     * @return
     */
    public List<CmcTrapServer> getAllTrapServer(Long entityId);

    /**
     * 添加TrapServer配置
     * @param trapServer
     */
    public void addTrapServer(CmcTrapServer trapServer);

    /**
     * 删除TrapServer配置
     * @param trapserver
     */
    public void deleteTrapServer(CmcTrapServer trapServer);

    /**
     * 从设备刷新TrapServer配置
     * @param entityId
     */
    public void refreshTrapServerFromFacility(Long entityId);

}
