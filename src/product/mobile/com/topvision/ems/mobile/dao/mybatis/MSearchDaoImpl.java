/***********************************************************************
 * $Id: MSearchDaoImpl.java,v1.0 2014-6-21 下午2:06:32 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cm.domain.CcmtsLocation;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.mobile.dao.MSearchDao;
import com.topvision.ems.mobile.domain.Location;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author jay
 * @created @2014-6-21-下午2:06:32
 * 
 */
@Repository("mSearchDao")
public class MSearchDaoImpl extends MyBatisDaoSupport<Location> implements MSearchDao {

    @Override
    public String getDomainName() {
        return "com.topvision.ems.mobile.domain.Location";
    }

    @Override
    public List<Entity> getEntityListByMac(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("getEntityListByMac"), map);
    }

    @Override
    public Long getEntityCountByMac(Map<String, String> map) {
        return getSqlSession().selectOne(getNameSpace("getEntityCountByMac"), map);
    }

    @Override
    public List<Entity> getEntityListByName(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("getEntityListByName"), map);
    }

    @Override
    public Long getEntityCountByName(Map<String, String> map) {
        return getSqlSession().selectOne(getNameSpace("getEntityCountByName"), map);
    }

    @Override
    public List<Entity> getEntityListByCmMac(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("getEntityListByCmMac"), map);
    }

    @Override
    public Long getEntityCountByCmMac(Map<String, String> map) {
        return getSqlSession().selectOne(getNameSpace("getEntityCountByCmMac"), map);
    }

    @Override
    public CcmtsLocation getCmtsLocation(long cmcId) {
        return getSqlSession().selectOne(getNameSpace("getCmtsLocation"), cmcId);
    }

}
