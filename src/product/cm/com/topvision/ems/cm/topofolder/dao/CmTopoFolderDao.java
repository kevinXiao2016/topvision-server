/***********************************************************************
 * $Id: CmTopoFolderDao.java,v1.0 2016年5月10日 下午2:55:06 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.topofolder.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cmc.ccmts.domain.TopoFolderDisplayInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;

/**
 * @author YangYi
 * @created @2016年5月10日-下午2:55:06
 *
 */
public interface CmTopoFolderDao {

    /**
     * 根据folderId查询展示信息
     * 
     * @param folderId
     * @return
     */
    TopoFolderDisplayInfo queryTopoDisplayInfo(Long folderId);

    /**
     * 获取地域下的CM列表
     * 
     * @param params
     * @return
     */
    List<CmAttribute> loadCmListByFolder(Map<String, Object> params);

    /**
     * 获取地域下的CM总数
     * 
     * @param params
     * @return
     */
    Long loadCmListByFolderNum(Map<String, Object> params);

}
