/***********************************************************************
 * $Id: OltDhcpStaticIpService.java,v1.0 2017年11月21日 下午1:09:31 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStaticIp;

/**
 * @author haojie
 * @created @2017年11月21日-下午1:09:31
 *
 */
public interface OltDhcpStaticIpService {

    /**
     * 获取静态IP列表数据
     * 
     * @param entityId
     * @return
     */
    List<TopOltDhcpStaticIp> getOltDhcpStaticIp(Map<String, Object> queryMap);

    /**
     * 获取静态IP列表数据数量
     * 
     * @param entityId
     * @return
     */
    Long getOltDhcpStaticIpCount(Map<String, Object> queryMap);

    /**
     * 刷新静态IP列表数据和防静态IP开关
     * 
     * @param entityId
     */
    void refreshOltDhcpStaticIp(Long entityId);

    /**
     * 新增静态IP
     * 
     * @param staticIp
     */
    void addOltDhcpStaticIp(TopOltDhcpStaticIp staticIp);

    /**
     * 删除静态IP
     * 
     * @param entityId
     * @param ipIndex
     * @param maskIndex
     */
    void deleteOltDhcpStaticIp(Long entityId, String ipIndex, String maskIndex);

    /**
     * 获取OLT下的槽位号列表
     * 
     * @param entityId
     * @return
     */
    List<Long> getOltSlotIdList(Long entityId);

    /**
     * 获取OLT下的某槽位的端口号列表
     * 
     * @param map
     * @return
     */
    List<Long> getOltSlotPonIndexList(Map<String, Object> map);

    /**
     * 加载OLT下某槽位某端口下的onuIndex
     * 
     * @param map
     * @return
     */
    List<Long> getOltSlotPonOnuIndexList(Map<String, Object> map);

}
