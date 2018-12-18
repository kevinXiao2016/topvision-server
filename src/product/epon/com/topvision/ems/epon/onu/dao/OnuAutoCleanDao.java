/***********************************************************************
 * $Id: OnuAutoCleanDao.java,v1.0 2016年11月10日 下午4:10:24 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Administrator
 * @created @2016年11月10日-下午4:10:24
 *
 */
public interface OnuAutoCleanDao extends BaseEntityDao<Entity> {
    
    
    Integer selectAutoCleanConfig();
    
     

}
