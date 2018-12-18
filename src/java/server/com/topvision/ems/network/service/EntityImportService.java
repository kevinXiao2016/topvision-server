/***********************************************************************
 * $Id: EntityImportService.java,v1.0 2013-10-30 下午3:11:10 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.EntityImport;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2013-10-30-下午3:11:10
 *
 */
public interface EntityImportService extends Service{
    /**
     * 导入设备名
     * @param importEntityList
     * @param overwrite
     * @return
     */
    Map<String, Object> importEntityName(List<EntityImport> importEntityList, boolean overwrite);
}
