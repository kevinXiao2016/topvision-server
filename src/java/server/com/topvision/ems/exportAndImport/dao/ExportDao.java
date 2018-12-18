/***********************************************************************
 * $Id: ExportDao.java,v1.0 2015-7-29 上午9:17:57 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.dao;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;

/**
 * @author fanzidong
 * @created @2015-7-29-上午9:17:57
 * 
 */
public interface ExportDao {

    /**
     * 加载所有设备的别名
     * 
     * @return
     */
    List<Entity> getAllEntityAlias();

}
