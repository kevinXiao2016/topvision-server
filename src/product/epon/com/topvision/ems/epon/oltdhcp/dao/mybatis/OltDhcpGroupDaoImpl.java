/***********************************************************************
 * $Id: OltDhcpGroupDaoImpl.java,v1.0 2017年11月22日 上午8:50:52 $
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

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpGroupDao;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpServerGroup;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:50:52
 *
 */
@Repository("oltDhcpGroupDao")
public class OltDhcpGroupDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpGroupDao {

    @Override
    protected String getDomainName() {
        return "OltDhcpGroup";
    }

    @Override
    public List<TopOltDhcpServerGroup> getOltDhcpServerGroup(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getOltDhcpServerGroups"), entityId);
    }

    @Override
    public void insertOltDhcpServerGroup(TopOltDhcpServerGroup group) {
        getSqlSession().insert(getNameSpace("insertOltDhcpServerGroup"), group);
    }

    @Override
    public void updateOltDhcpServerGroup(TopOltDhcpServerGroup group) {
        getSqlSession().update(getNameSpace("updateOltDhcpServerGroup"), group);
    }

    @Override
    public void deleteOltDhcpServerGroup(Long entityId, Integer groupIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("groupIndex", groupIndex);
        getSqlSession().update(getNameSpace("deleteOltDhcpServerGroup"), map);
    }

    @Override
    public TopOltDhcpServerGroup getOltDhcpServerGroup(Long entityId, Integer groupIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("groupIndex", groupIndex);
        return getSqlSession().selectOne(getNameSpace("getOltDhcpServerGroup"), map);
    }

}
