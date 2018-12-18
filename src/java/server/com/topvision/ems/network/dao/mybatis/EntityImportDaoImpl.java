/***********************************************************************
 * $Id: EntityImportDaoImpl.java,v1.0 2013-10-31 上午11:37:23 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.network.dao.EntityImportDao;
import com.topvision.ems.network.domain.EntityImport;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author loyal
 * @created @2013-10-31-上午11:37:23
 * 
 */
@Repository("entityImportDao")
public class EntityImportDaoImpl extends MyBatisDaoSupport<EntityImport> implements EntityImportDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.EntityImport";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.EntityImportDao#selectEntity()
     */
    public List<EntityImport> selectEntity() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectEntity"), map);
    }

}
