/***********************************************************************
 * $Id: LoopBackConfigService.java,v1.0 2013-11-16 上午11:50:32 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.loopback.service;

import java.util.List;

import com.topvision.ems.epon.loopback.domain.LoopbackConfigTable;
import com.topvision.ems.epon.loopback.domain.LoopbackSubIpTable;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2013-11-16-上午11:50:32
 */
public interface LoopBackConfigService extends Service {

    /**
     * 刷新LoopbackConfigTable信息
     * 
     * @param entityId
     */
    void refreshLoopBackConfig(Long entityId);

    /**
     * 刷新LoopbackSubIpTable信息
     * 
     * @param entityId
     */
    void refreshLoopBackSub(Long entityId);

    /**
     * 获取环回接口列表
     * @return
     */
    List<LoopbackConfigTable> getLBInterfaceList(Long entityId);

    /**
     * 添加环回接口
     */
    void addLoopBackInterface(LoopbackConfigTable loopBack);

    /**
     * 删除环回接口
     */
    void deleteLoopBackInterface(LoopbackConfigTable loopBack);

    /**
     * 修改环回接口配置
     * @param loopBack
     */
    void modifyLoopBackInterface(LoopbackConfigTable loopBack);

    /**
     * 获取环回接口子Ip列表
     * @return
     */
    List<LoopbackSubIpTable> getLBSubIpList(Long entityId, Integer interfaceIndex);

    /**
     * 添加环回接口子IP
     */
    void addLBSubIp(LoopbackSubIpTable subIpTable);

    /**
     * 删除环回接口子IP
     */
    void deleteLBSubIp(LoopbackSubIpTable subIpTable);

    /**
     * 修改环回接口子Ip
     * @param subIpTable
     */
    void modifyLBSubIp(LoopbackSubIpTable subIpTable);

}
