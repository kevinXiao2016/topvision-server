/***********************************************************************
 * $Id: OltDhcpStaticIpDaoImpl.java,v1.0 2017年11月22日 上午8:52:10 $
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

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpStaticIpDao;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStaticIp;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:52:10
 *
 */
@Repository("oltDhcpStaticIpDao")
public class OltDhcpStaticIpDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpStaticIpDao {

    @Override
    protected String getDomainName() {
        return "OltDhcpStaticIp";
    }

    @Override
    public List<TopOltDhcpStaticIp> getOltDhcpStaticIp(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("getOltDhcpStaticIp"), queryMap);
    }

    @Override
    public Long getOltDhcpStaticIpCount(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace("getOltDhcpStaticIpCount"), queryMap);
    }

    @Override
    public void insertOltDhcpStaticIp(TopOltDhcpStaticIp staticIp) {
        getSqlSession().insert(getNameSpace("insertOltDhcpStaticIp"), staticIp);
    }

    @Override
    public void deleteOltDhcpStaticIp(Long entityId, String ipIndex, String maskIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("ipIndex", ipIndex);
        map.put("maskIndex", maskIndex);
        getSqlSession().delete(getNameSpace("deleteOltDhcpStaticIp"), map);
    }

    @Override
    public void updateOltDhcpSourceVerifyEnable(Long entityId, Integer sourceVerifyEnable) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("topOltDhcpSourceVerifyEnable", sourceVerifyEnable);
        getSqlSession().update(getNameSpace("updateOltDhcpSourceVerifyEnable"), map);
    }

    @Override
    public List<Long> selectOltSlotIdList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectOltSlotIdList"), entityId);
    }

    @Override
    public List<Long> selectOltSlotPonIndexList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectOltSlotPonIndexList"), map);
    }

    @Override
    public List<Long> selectOltSlotPonOnuIndexList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectOltSlotPonOnuIndexList"), map);
    }

    @Override
    public Long getOnuStaticIpCount(TopOltDhcpStaticIp staticIp) {
        return getSqlSession().selectOne(getNameSpace("getOnuStaticIpCount"), staticIp);
    }

    @Override
    public TopOltDhcpStaticIp selectUniqueStaticIp(Long entityId, String ipIndex, String maskIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("ipIndex", ipIndex);
        map.put("maskIndex", maskIndex);
        return getSqlSession().selectOne(getNameSpace("selectUniqueStaticIp"), map);
    }

}
