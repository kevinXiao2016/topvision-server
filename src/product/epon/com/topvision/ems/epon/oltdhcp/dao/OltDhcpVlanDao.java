/***********************************************************************
 * $Id: OltDhcpVlanDao.java,v1.0 2017年11月22日 上午8:53:28 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao;

import java.util.List;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVLANCfg;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:53:28
 *
 */
public interface OltDhcpVlanDao extends BaseEntityDao<Object> {

    /**
     * 获取模式配置列表信息
     * 
     * @param entityId
     * @return
     */
    List<TopOltDhcpVLANCfg> getOltDhcpVlanCfg(Long entityId);

    /**
     * 修改模式配置信息
     * 
     * @param vlanCfg
     */
    void updateOltDhcpVLANCfg(TopOltDhcpVLANCfg vlanCfg);

    /**
     * 获取单条模式配置信息
     * 
     * @param entityId
     * @param vlanIndex
     * @return
     */
    TopOltDhcpVLANCfg getOltDhcpVlanCfg(Long entityId, Integer vlanIndex);

}
