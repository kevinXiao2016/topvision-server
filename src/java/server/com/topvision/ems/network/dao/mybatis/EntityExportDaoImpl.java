/***********************************************************************
 * $Id: EntityExportDaoImpl.java,v1.0 2013-11-1 上午8:36:23 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityExportDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author loyal
 * @created @2013-11-1-上午8:36:23
 * 
 */
@Repository("entityExportDao")
public class EntityExportDaoImpl extends MyBatisDaoSupport<Entity> implements EntityExportDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityExportDao#selectEntity(java.util.Map, int, int)
     */
    public List<Entity> selectEntity(Map<String, Object> map, int start, int limit) {
        map.put("start", start);
        map.put("limit", limit);
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectEntity"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityExportDao#selectEntityNum(java.util.Map)
     */
    public Long selectEntityNum(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectOne(getNameSpace("selectEntityNum"), map);
    }
    
    @Override
    public List<String> selectEntityFolder(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectEntityFolder"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.EntityExport";
    }

}
