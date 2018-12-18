/***********************************************************************
 * $Id: LoopBackConfigDao.java,v1.0 2013-11-16 上午11:45:59 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.loopback.dao;

import java.util.List;

import com.topvision.ems.epon.loopback.domain.LoopbackConfigTable;
import com.topvision.ems.epon.loopback.domain.LoopbackSubIpTable;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2013-11-16-上午11:45:59
 * 
 */
public interface LoopBackConfigDao extends BaseEntityDao<LoopbackConfigTable> {

    /**
     * 刷新LoopBackConfig的信息
     * 
     * @param entityId
     * @param loopbackConfigTables
     */
    void refreshLoopBackConfigTable(Long entityId, List<LoopbackConfigTable> loopbackConfigTables);

    /**
     * 刷新LoopBackSub的信息
     * 
     * @param entityId
     * @param loopbackSubIpTables
     */
    void refreshLoopBackSubTable(Long entityId, List<LoopbackSubIpTable> loopbackSubIpTables);

    /**
     * 添加LoopBackConfig信息
     * @param loopBack
     */
    void insertLoopBackInterface(LoopbackConfigTable loopBack);

    /**
     * 删除LoopBackConfig信息
     * @param loopBack
     */
    void deleteLoopBackInterface(LoopbackConfigTable loopBack);

    /**
     * 获取LoopBackConfig信息列表
     * @return
     */
    List<LoopbackConfigTable> queryLoopbackList(Long entityId);

    /**
     * 修改LoopBackConfig信息
     * @param loopBack
     */
    void updateLoopBackInterface(LoopbackConfigTable loopBack);

    /**
     * 添加LoopBackSub的信息
     * @param subIpTable
     */
    void insertLBSubIp(LoopbackSubIpTable subIpTable);

    /**
     * 删除LoopBackSub的信息
     * @param subIpTable
     */
    void deleteLBSubIp(LoopbackSubIpTable subIpTable);

    /**
     * 获取指定的环回接口下的子IP列表
     * @param entityId
     * @param subIpInex
     * @return
     */
    List<LoopbackSubIpTable> querySubIpList(Long entityId, Integer interfaceIndex);

    /**
     * 修改环回接口子IP
     * @param subIpTable
     */
    void updateLBSubIp(LoopbackSubIpTable subIpTable);


}
