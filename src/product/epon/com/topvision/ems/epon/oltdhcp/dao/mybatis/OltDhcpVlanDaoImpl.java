/***********************************************************************
 * $Id: OltDhcpVlanDaoImpl.java,v1.0 2017年11月22日 上午8:53:37 $
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

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpVlanDao;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVLANCfg;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:53:37
 *
 */
@Repository("oltDhcpVlanDao")
public class OltDhcpVlanDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpVlanDao {

    @Override
    protected String getDomainName() {
        return "OltDhcpVlan";
    }

    @Override
    public List<TopOltDhcpVLANCfg> getOltDhcpVlanCfg(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getOltDhcpVlanCfgs"), entityId);
    }

    @Override
    public void updateOltDhcpVLANCfg(TopOltDhcpVLANCfg vlanCfg) {
        getSqlSession().update(getNameSpace("updateOltDhcpVLANCfg"), vlanCfg);
    }

    @Override
    public TopOltDhcpVLANCfg getOltDhcpVlanCfg(Long entityId, Integer vlanIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("vlanIndex", vlanIndex);
        return getSqlSession().selectOne(getNameSpace("getOltDhcpVlanCfg"), map);
    }
}
