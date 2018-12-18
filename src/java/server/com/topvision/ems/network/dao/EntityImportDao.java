/***********************************************************************
 * $Id: EntityImportDao.java,v1.0 2013-10-31 上午11:34:34 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao;

import java.util.List;

import com.topvision.ems.network.domain.EntityImport;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author loyal
 * @created @2013-10-31-上午11:34:34
 *
 */
public interface EntityImportDao extends BaseEntityDao<EntityImport> {
    /**
     * 获取所有entity
     * @return
     */
    List<EntityImport> selectEntity();

}
