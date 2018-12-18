/***********************************************************************
 * $Id: OltDhcpPortDaoImpl.java,v1.0 2017年11月22日 上午8:51:38 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpPortDao;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:51:38
 *
 */
@Repository("oltDhcpPortDao")
public class OltDhcpPortDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpPortDao {

    @Override
    protected String getDomainName() {
        return "OltDhcpPort";
    }

    @Override
    public List<TopOltDhcpPortAttribute> getPortAttribute(Long entityId, Integer portProtIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("portProtIndex", portProtIndex);
        return getSqlSession().selectList(getNameSpace("getPortAttributes"), map);
    }

    @Override
    public void updatePortAttribute(TopOltDhcpPortAttribute port) {
        getSqlSession().update(getNameSpace("updatePortAttribute"), port);
    }

    @Override
    public TopOltDhcpPortAttribute getPortAttribute(Long entityId, Integer portProtIndex, Integer portTypeIndex,
            Integer slotIndex, Integer portIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("portProtIndex", portProtIndex);
        map.put("portTypeIndex", portTypeIndex);
        map.put("slotIndex", slotIndex);
        map.put("portIndex", portIndex);
        return getSqlSession().selectOne(getNameSpace("getPortAttribute"), map);
    }

}
