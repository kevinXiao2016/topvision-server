/***********************************************************************
 * $Id: EntityTypeDaoImpl.java,v 1.1 Jul 23, 2008 10:04:16 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.domain.EntityTypeRelation;
import com.topvision.ems.template.dao.EntityTypeDao;
import com.topvision.ems.template.domain.EntityCorp;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.exception.dao.DaoException;

/**
 * @Create Date Jul 23, 2008 10:04:16 AM
 * 
 * @author kelers
 * 
 */
@Repository("entityTypeDao")
public class EntityTypeDaoImpl extends MyBatisDaoSupport<EntityType> implements EntityTypeDao {
    @Override
    public String getDomainName() {
        return EntityType.class.getName();
    }

    @Override
    public List<EntityType> getEntityCategories() throws DaoException {
        return getSqlSession().selectList(getNameSpace("getEntityCategories"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.network.dao.EntityTypeDao#getEntityCorps()
     */
    @Override
    public EntityCorp getEntityCorpById(long corpId) throws DaoException {
        return (EntityCorp) getSqlSession().selectOne(getNameSpace("getEntityCorpById"), corpId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.server.network.dao.EntityTypeDao#getEntityCorps()
     */
    @Override
    public List<EntityCorp> getEntityCorps() throws DaoException {
        return getSqlSession().selectList(getNameSpace("getEntityCorps"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.template.dao.EntityTypeDao#getLastModified()
     */
    @Override
    public Long getLastModified() {
        String lastModified = (String) getSqlSession().selectOne(getNameSpace("getLastModified"));
        if (lastModified == null) {
            lastModified = "0";
            getSqlSession().insert(getNameSpace("insertLastModified"), lastModified);
        }
        return Long.parseLong(lastModified);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.template.dao.EntityTypeDao# getTypesBySysObjectID(java.lang.String)
     */
    @Override
    public List<EntityType> getTypesBySysObjectID(String sysObjectID) throws DaoException {
        return getSqlSession().selectList(getNameSpace("getTypesBySysObjectID"), sysObjectID);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.template.dao.EntityTypeDao#
     *      insertEntityCorp(com.topvision.ems.template.domain.EntityCorp)
     */
    @Override
    public void insertEntityCorp(EntityCorp ec) throws DaoException {
        getSqlSession().update(getNameSpace("insertEntityCorp"), ec);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.template.dao.EntityTypeDao#
     *      updateEntityCorp(com.topvision.ems.template.domain.EntityCorp)
     */
    @Override
    public void updateEntityCorp(EntityCorp ec) throws DaoException {
        getSqlSession().update(getNameSpace("updateEntityCorp"), ec);
    }

    /**
     * (non-Javadoc)
     * @see com.topvision.ems.template.dao.EntityTypeDao#updateLastModified(java.lang.Long)
     */
    @Override
    public void updateLastModified(Long lastModified) {
        getSqlSession().update(getNameSpace("updateLastModified"), lastModified.toString());
    }

    /**
     * (non-Javadoc)
     * @see com.topvision.ems.template.dao.EntityTypeDao#getTypeBySysObjectID(java.lang.String)
     */
    @Override
    public EntityType getTypeBySysObjectID(String sysObjectID) throws DaoException {
        return (EntityType) getSqlSession().selectOne(getNameSpace("getTypeBySysObjectID"), sysObjectID);
    }
    
    /*
     * (non-Javadoc)
     * @see com.topvision.ems.template.dao.EntityTypeDao#selectEntityTypeRelation()
     */
    @Override
    public List<EntityTypeRelation> selectEntityTypeRelation() {
        return getSqlSession().selectList(getNameSpace("selectEntityTypeRelation"));
    }

    @Override
    public List<EntityType> selectEntityTypesByTypeGroup(Long type) {
        return getSqlSession().selectList(getNameSpace("selectEntityTypesByTypeGroup"), type);
    }

    @Override
    public List<Long> queryCategoryTypeByTypeId(Long typeId) {
        return this.getSqlSession().selectList(getNameSpace("queryCategoryTypeByTypeId"), typeId);
    }

}
