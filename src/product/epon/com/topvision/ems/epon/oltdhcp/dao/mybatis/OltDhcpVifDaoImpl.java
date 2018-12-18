/***********************************************************************
 * $Id: OltDhcpVifDaoImpl.java,v1.0 2017年11月22日 上午8:53:08 $
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

import com.topvision.ems.epon.logicinterface.domain.LogicInterface;
import com.topvision.ems.epon.oltdhcp.dao.OltDhcpVifDao;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVifCfg;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:53:08
 *
 */
@Repository("oltDhcpVifDao")
public class OltDhcpVifDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpVifDao {

    @Override
    protected String getDomainName() {
        return "OltDhcpVif";
    }

    @Override
    public List<TopOltDhcpVifCfg> getOltDhcpVifCfg(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getOltDhcpVifCfgs"), entityId);
    }

    @Override
    public void insertOltDhcpVifCfg(TopOltDhcpVifCfg vifCfg) {
        //vifCfg.setOpt60StrDisplay(vifCfg.getTopOltDhcpVifOpt60StrIndex());
        getSqlSession().insert(getNameSpace("insertOltDhcpVifCfg"), vifCfg);
    }

    @Override
    public void updateOltDhcpVifCfg(TopOltDhcpVifCfg vifCfg) {
        //vifCfg.setOpt60StrDisplay(vifCfg.getTopOltDhcpVifOpt60StrIndex());
        getSqlSession().update(getNameSpace("updateOltDhcpVifCfg"), vifCfg);
    }

    @Override
    public void deleteOltDhcpVifCfg(Long entityId, Integer vifIndex, String opt60StrIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("vifIndex", vifIndex);
        map.put("opt60StrIndex", opt60StrIndex);
        getSqlSession().delete(getNameSpace("deleteOltDhcpVifCfg"), map);
    }

    @Override
    public TopOltDhcpVifCfg getOltDhcpVifCfg(Long entityId, Integer vifIndex, String opt60StrIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("vifIndex", vifIndex);
        map.put("opt60StrIndex", opt60StrIndex);
        return getSqlSession().selectOne(getNameSpace("getOltDhcpVifCfg"), map);
    }

    @Override
    public List<LogicInterface> getOltLogicInterfaceByType(Map<String, Object> param) {
        return getSqlSession().selectList(getNameSpace("getOltLogicInterfaceByType"), param);
    }

}
