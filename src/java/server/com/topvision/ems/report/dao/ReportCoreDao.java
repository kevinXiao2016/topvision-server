/***********************************************************************
 * $Id: ReportCoreDao.java,v1.0 2014-6-18 下午4:18:35 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.report.domain.EntityPair;
import com.topvision.ems.report.domain.Report;

/**
 * @author Administrator
 * @created @2014-6-18-下午4:18:35
 * 
 */
public interface ReportCoreDao {

    List<Object> fetchCoreData(Report report, Map<String, String> query);

    /**
     * 获取指定用户能够访问的地域
     * 
     * @param queryMap
     * @return
     */
    List<TopoFolder> queryUserFolderList(Map<String, String> queryMap);

    /**
     * 获取指定地域下的OLT
     * 
     * @param folderId
     * @return
     */
    List<EntityPair> queryOltByFolderId(Long folderId);

}
