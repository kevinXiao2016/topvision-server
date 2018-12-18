/***********************************************************************
 * $Id: LicenseDao.java,v1.0 2014年10月18日 下午2:03:56 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;

/**
 * @author Victor
 * @created @2014年10月18日-下午2:03:56
 *
 */
public interface LicenseDao {
    /**
     * 通过License获取type为3的所有EntityType列表
     * @return
     */
    List<EntityType> selectEntityTypes();

    /**
     * 根据module名称获取所有子类型
     * @param parentName module名称
     * @return
     */
    List<EntityType> selectEntityTypesByModule(String module);

    /**
     * 获取所有未授权的Entity
     * @return
     */
    List<Entity> selectUnauthorizedEntities();
}
