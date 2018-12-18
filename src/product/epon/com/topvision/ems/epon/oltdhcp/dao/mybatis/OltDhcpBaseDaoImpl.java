/***********************************************************************
 * $Id: OltDhcpBaseDaoImpl.java,v1.0 2017年11月22日 上午8:49:57 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpBaseDao;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpGlobalObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:49:57
 *
 */
@Repository("oltDhcpBaseDao")
public class OltDhcpBaseDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpBaseDao {
    public static final Integer PORT_PROT_DHCP = 1;
    public static final Integer PORT_PROT_PPPOE = 2;

    @Override
    protected String getDomainName() {
        return "OltDhcpBase";
    }

    @Override
    public TopOltDhcpGlobalObjects getOltDhcpBaseCfg(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getOltDhcpBaseCfg"), entityId);
    }

    @Override
    public void updateOltDhcpEnable(Long entityId, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("status", status);
        getSqlSession().update(getNameSpace("updateDhcpEnable"), map);
    }

    @Override
    public void updateOltDhcpGlobalObjects(TopOltDhcpGlobalObjects globalObjects) {
        getSqlSession().update(getNameSpace("updateOltDhcpGlobalObjects"), globalObjects);
    }

    @Override
    public void updateOltDhcpOption82Cfg(TopOltDhcpGlobalObjects globalObjects) {
        getSqlSession().update(getNameSpace("updateOltDhcpOption82Cfg"), globalObjects);
    }

    @Override
    public void updateOltPppoeEnable(Long entityId, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("status", status);
        getSqlSession().update(getNameSpace("updateOltPppoeEnable"), map);
    }

    @Override
    public void updateOltPppoeGlobalObjects(TopOltDhcpGlobalObjects globalObjects) {
        getSqlSession().update(getNameSpace("updateOltPppoeGlobalObjects"), globalObjects);
    }

    @Override
    public void updatePortAttributes(Long entityId, Integer portProtIndex,
            List<TopOltDhcpPortAttribute> portAttributes) {
        // 先删
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("portProtIndex", portProtIndex);
        getSqlSession().delete(getNameSpace("deletePortAttributes"), map);
        // 后加
        SqlSession session = getBatchSession();
        try {
            for (TopOltDhcpPortAttribute port : portAttributes) {
                if (port.getTopOltDhcpPortProtIndex().equals(portProtIndex)) {
                    port.setEntityId(entityId);
                    session.insert(getNameSpace("insertPortAttribute"), port);
                }
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateOltPppoeCfg(TopOltDhcpGlobalObjects globalObjects) {
        getSqlSession().update(getNameSpace("updateOltPppoeCfg"), globalObjects);
    }

    @Override
    public void updateOltDhcpSourceVerifyEnable(Long entityId, Integer status) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("status", status);
        getSqlSession().update(getNameSpace("updateOltDhcpSourceVerifyEnable"), map);
    }

}
