/***********************************************************************
 * $Id: EntityTypeDao.java,v 1.1 Jul 23, 2008 10:02:45 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.dao;

import java.util.List;

import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.domain.EntityTypeRelation;
import com.topvision.ems.template.domain.EntityCorp;
import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.framework.exception.dao.DaoException;

/**
 * @Create Date Jul 23, 2008 10:02:45 AM
 * 
 * @author kelers
 * 
 */
public interface EntityTypeDao extends BaseEntityDao<EntityType> {
    List<EntityType> getEntityCategories() throws DaoException;

    EntityCorp getEntityCorpById(long corpId) throws DaoException;

    List<EntityCorp> getEntityCorps() throws DaoException;

    Long getLastModified();

    List<EntityType> getTypesBySysObjectID(String sysObjectID) throws DaoException;
    
    EntityType getTypeBySysObjectID(String sysObjectID) throws DaoException;

    void insertEntityCorp(EntityCorp ec) throws DaoException;

    void updateEntityCorp(EntityCorp ec) throws DaoException;

    void updateLastModified(Long lastModified);
    
    List<EntityTypeRelation> selectEntityTypeRelation();

    /**
     * 通过type获取type内所有EntityType列表
     * @param type
     * @return
     */
    List<EntityType> selectEntityTypesByTypeGroup(Long type);

    /**
     * 根据typeId查询包含该typeId的分类类型
     * @param typeId
     * @return
     */
    List<Long> queryCategoryTypeByTypeId(Long typeId);
}
