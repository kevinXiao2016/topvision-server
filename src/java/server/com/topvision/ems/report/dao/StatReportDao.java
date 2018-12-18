/***********************************************************************
 * $Id: ReportDao.java,v1.0 2013-5-28 上午10:44:51 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013-5-28-上午10:44:51
 * 
 */
public interface StatReportDao extends BaseEntityDao<Entity> {

    /**
     * 查询所有地域的与设备的关系列表，与用户相关的处理在service中处理
     * 
     * @param authFolder
     *            地域权限表名
     * @param entityType
     *            设备的大类型
     * @return
     */
    List<TopoEntityStastic> selectTopoEntityRelation(String authFolder, Long entityType);

    /**
     * 根据地域ID获取该地域及其子地域中的所有设备
     * 
     * @param folderId
     * @param types
     * @return
     */
    List<Long> selectEntityIdsByFolderId(Long folderId, String types);

    /**
     * 通过查询条件中的folderIds获取权限folderIds（包含子地域）
     * @param folderIds
     * @return
     */
    List<Long> getAuthFolderIds(List<Long> folderIds);

}
