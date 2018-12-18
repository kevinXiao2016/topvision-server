/***********************************************************************
 * $Id: CmTopoFolderServiceImpl.java,v1.0 2016年5月10日 下午2:45:46 $ * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.topofolder.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.topofolder.dao.CmTopoFolderDao;
import com.topvision.ems.cm.topofolder.service.CmTopoFolderService;
import com.topvision.ems.cmc.ccmts.domain.TopoFolderDisplayInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.network.dao.TopoFolderDao;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.framework.service.BaseService;

/**
 * @author YangYi
 * @created @2016年5月10日-下午2:45:46
 *
 */
@Service("cmTopoFolderService")
public class CmTopoFolderServiceImpl extends BaseService implements CmTopoFolderService {
    @Autowired
    private TopoFolderDao topoFolderDao;
    @Autowired
    private CmTopoFolderDao cmTopoFolderDao;

    @Override
    public TopoFolderDisplayInfo getRootFolderInfo(Long rootFolderId) {
        TopoFolder folder = topoFolderDao.selectByPrimaryKey(rootFolderId);
        TopoFolderDisplayInfo displayInfo = cmTopoFolderDao.queryTopoDisplayInfo(rootFolderId);
        displayInfo.setFolderId(folder.getFolderId());
        displayInfo.setSuperiorId(folder.getSuperiorId());
        displayInfo.setName(folder.getName());
        return displayInfo;
    }

    @Override
    public List<TopoFolderDisplayInfo> getTopoDisplayInfo(Long folderId) {
        List<TopoFolder> folderList = topoFolderDao.getChildTopoFolder(folderId);
        List<TopoFolderDisplayInfo> displayInfoList = new ArrayList<TopoFolderDisplayInfo>();
        TopoFolderDisplayInfo displayInfo = null;
        for (TopoFolder folder : folderList) {
            displayInfo = cmTopoFolderDao.queryTopoDisplayInfo(folder.getFolderId());
            displayInfo.setFolderId(folder.getFolderId());
            displayInfo.setSuperiorId(folder.getSuperiorId());
            displayInfo.setName(folder.getName());
            displayInfoList.add(displayInfo);
        }
        return displayInfoList;
    }


    @Override
    public Long loadCmListByFolderNum(Long folderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("folderId", folderId);
        params.put("folderTable", "t_entity_" + folderId);
        return cmTopoFolderDao.loadCmListByFolderNum(params);
    }

    @Override
    public List<CmAttribute> loadCmListByFolder(Long folderId, int start, int limit, String sort, String dir) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("folderId", folderId);
        params.put("folderTable", "t_entity_" + folderId);
        params.put("sort", sort);
        params.put("dir", dir);
        params.put("start", start);
        params.put("limit", limit);
        return cmTopoFolderDao.loadCmListByFolder(params);
    }

}
