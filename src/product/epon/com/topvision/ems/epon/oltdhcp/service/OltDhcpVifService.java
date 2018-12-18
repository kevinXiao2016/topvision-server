/***********************************************************************
 * $Id: OltDhcpVifService.java,v1.0 2017年11月21日 下午1:07:56 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVifCfg;

/**
 * @author haojie
 * @created @2017年11月21日-下午1:07:56
 *
 */
public interface OltDhcpVifService {

    /**
     * 获取RELAY配置列表信息
     * 
     * @param entityId
     * @return
     */
    List<TopOltDhcpVifCfg> getOltDhcpVifCfg(Long entityId);

    /**
     * 获取单条RELAY配置规则信息
     * 
     * @param entityId
     * @param vifIndex
     * @param opt60StrIndex
     * @return
     */
    TopOltDhcpVifCfg getOltDhcpVifCfg(Long entityId, Integer vifIndex, String opt60StrIndex);

    /**
     * 新增RELAY规则
     * 
     * @param vifCfg
     */
    void addOltDhcpVifCfg(TopOltDhcpVifCfg vifCfg);

    /**
     * 修改RELAY规则
     * 
     * @param vifCfg
     */
    void modifyOltDhcpVifCfg(TopOltDhcpVifCfg vifCfg);

    /**
     * 删除RELAY规则
     * 
     * @param entityId
     * @param vifIndex
     * @param opt60StrIndex
     */
    void deleteOltDhcpVifCfg(Long entityId, Integer vifIndex, String opt60StrIndex);

    /**
     * 刷新RELAY配置列表数据
     * 
     * @param entityId
     */
    void refreshOltDhcpVifCfg(Long entityId);

    /**
     * 获取DHCP模式为relay的vlanif列表
     * 
     * @param param
     * @return
     */
    List<LogicInterface> getOltLogicInterfaceByType(Map<String, Object> param);

}
