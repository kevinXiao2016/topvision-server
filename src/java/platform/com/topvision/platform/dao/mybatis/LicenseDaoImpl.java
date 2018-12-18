/***********************************************************************
 * $Id: LicenseDaoImpl.java,v1.0 2014年10月18日 下午2:05:09 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.LicenseDao;

/**
 * @author Victor
 * @created @2014年10月18日-下午2:05:09
 *
 */
@Repository("licenseDao")
public class LicenseDaoImpl extends MyBatisDaoSupport<EntityType> implements LicenseDao {
    @Override
    protected String getDomainName() {
        return "license";
    }

    @Override
    public List<EntityType> selectEntityTypes() {
        return getSqlSession().selectList(getNameSpace("selectEntityTypes"));
    }

    @Override
    public List<EntityType> selectEntityTypesByModule(String module) {
        return getSqlSession().selectList(getNameSpace("selectEntityTypesByModule"), module);
    }

    @Override
    public List<Entity> selectUnauthorizedEntities() {
        return getSqlSession().selectList(getNameSpace("selectUnauthorizedEntities"));
    }
}
