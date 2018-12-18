/***********************************************************************
 * $Id: OltDhcpVifDao.java,v1.0 2017年11月22日 上午8:52:59 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVifCfg;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:52:59
 *
 */
public interface OltDhcpVifDao extends BaseEntityDao<Object> {

    /**
     * 获取RELAY规则列表信息
     * 
     * @param entityId
     * @return
     */
    List<TopOltDhcpVifCfg> getOltDhcpVifCfg(Long entityId);

    /**
     * 插入RELAY规则
     * 
     * @param vifCfg
     */
    void insertOltDhcpVifCfg(TopOltDhcpVifCfg vifCfg);

    /**
     * 更新RELAY规则
     * 
     * @param vifCfg
     */
    void updateOltDhcpVifCfg(TopOltDhcpVifCfg vifCfg);

    /**
     * 删除RELAY规则
     * 
     * @param entityId
     * @param vifIndex
     * @param opt60StrIndex
     */
    void deleteOltDhcpVifCfg(Long entityId, Integer vifIndex, String opt60StrIndex);

    /**
     * 获取单条RELAY配置信息
     * 
     * @param entityId
     * @param vifIndex
     * @param opt60StrIndex
     * @return
     */
    TopOltDhcpVifCfg getOltDhcpVifCfg(Long entityId, Integer vifIndex, String opt60StrIndex);

    /**
     * 获取DHCP模式为RELAY的VLAN if
     * 
     * @param param
     * @return
     */
    List<LogicInterface> getOltLogicInterfaceByType(Map<String, Object> param);

}
