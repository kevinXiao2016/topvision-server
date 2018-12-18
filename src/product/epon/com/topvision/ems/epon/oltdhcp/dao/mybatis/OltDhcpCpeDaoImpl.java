/***********************************************************************
 * $Id: OltDhcpCpeDaoImpl.java,v1.0 2017年11月22日 上午8:49:41 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpCpeDao;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpCpeInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:49:41
 *
 */
@Repository("oltDhcpCpeDao")
public class OltDhcpCpeDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpCpeDao {

    @Override
    protected String getDomainName() {
        return "OltDhcpCpe";
    }

    @Override
    public List<TopOltDhcpCpeInfo> getOltDhcpCpeInfo(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("getOltDhcpCpeInfo"), queryMap);
    }

    @Override
    public Long getOltDhcpCpeInfoCount(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace("getOltDhcpCpeInfoCount"), queryMap);
    }

}
