/***********************************************************************
 * $Id: CmTopoFolderDaoImpl.java,v1.0 2016年5月10日 下午2:56:22 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.topofolder.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cm.topofolder.dao.CmTopoFolderDao;
import com.topvision.ems.cmc.ccmts.domain.TopoFolderDisplayInfo;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * @author YangYi
 * @created @2016年5月10日-下午2:56:22
 *
 */
@Repository("cmTopoFolderDao")
public class CmTopoFolderDaoImpl extends MyBatisDaoSupport<CmAttribute> implements CmTopoFolderDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.topofolder.dao.CmTopoFolderDao";
    }
    
    @Override
    public TopoFolderDisplayInfo queryTopoDisplayInfo(Long folderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("folderTable", "t_entity_" + folderId);
        return this.getSqlSession().selectOne(getNameSpace("queryTopoDisplayInfo"), map);
    }

    @Override
    public List<CmAttribute> loadCmListByFolder(Map<String, Object> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        List<CmAttribute> list = getSqlSession().selectList(getNameSpace("loadCmListByFolder"), params);
        return list;
    }

    @Override
    public Long loadCmListByFolderNum(Map<String, Object> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectOne(getNameSpace("loadCmListByFolderNum"), params);
    }


}
