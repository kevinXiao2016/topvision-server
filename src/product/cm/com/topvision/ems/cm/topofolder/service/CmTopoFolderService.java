/***********************************************************************
 * $Id: CmTopoFolderService.java,v1.0 2016年5月10日 下午2:35:44 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.topofolder.service;

import java.util.List;

import com.topvision.ems.cmc.ccmts.domain.TopoFolderDisplayInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;

/**
 * @author YangYi
 * @created @2016年5月10日-下午2:35:44
 *
 */
public interface CmTopoFolderService {

    /**
     * 获取根节点的信息
     * 
     * @param rootFolderId
     * @return
     */
    TopoFolderDisplayInfo getRootFolderInfo(Long rootFolderId);

    /**
     * 根据folderId获取节点下的列表
     * 
     * @param folderId
     * @return
     */
    List<TopoFolderDisplayInfo> getTopoDisplayInfo(Long folderId);

    /**
     * 根据folderId，获取CM列表
     * 
     * @param folderId
     * @param start
     * @param limit
     * @param sort
     * @param dir
     * @return
     */
    List<CmAttribute> loadCmListByFolder(Long folderId, int start, int limit, String sort, String dir);

    /**
     * 根据folderId，获取CM总数
     * 
     * @param folderId
     * @return
     */
    Long loadCmListByFolderNum(Long folderId);

}
