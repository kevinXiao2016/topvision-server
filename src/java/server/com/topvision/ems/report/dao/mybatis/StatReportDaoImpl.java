/***********************************************************************
 * $Id: StatReportDaoImpl.java,v1.0 2013-10-11 下午2:47:25 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.report.dao.StatReportDao;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author fanzidong
 * @created @2013-10-11-下午2:47:25
 * 
 */
@Repository("statReportDao")
public class StatReportDaoImpl extends MyBatisDaoSupport<Entity> implements StatReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.report.domain.StatReport";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.report.dao.StatReportDao#selectTopoEntityRelation(java.lang.String,
     * java.lang.Integer)
     */
    @Override
    public List<TopoEntityStastic> selectTopoEntityRelation(String authFolder, Long entityType) {
        List<TopoEntityStastic> authEntityList = new ArrayList<TopoEntityStastic>();
        // 先获取此用户的权限地域及子地域
        List<Long> authFolderIdList = getSqlSession().selectList(getNameSpace("getAuthFolderList"), authFolder);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", entityType);
        // 根据每个用户地域获取地域及其子地域下的设备，只能从权限子表T_entity_XX中取得设备
        for (Long folderId : authFolderIdList) {
            String folderName = getSqlSession().selectOne(getNameSpace("getFolderNameById"), folderId);
            map.put("Authority", "t_entity_" + folderId.toString());
            List<TopoEntityStastic> authEntityStastics = getSqlSession().selectList(
                    getNameSpace("getAuthEntityStastics"), map);
            if (authEntityStastics != null) {
                for (TopoEntityStastic topoEntityStastic : authEntityStastics) {
                    topoEntityStastic.setFolderId(folderId);
                    topoEntityStastic.setFolderName(folderName);
                }
                authEntityList.addAll(authEntityStastics);
            }
        }
        return authEntityList;
    }

    @Override
    public List<Long> selectEntityIdsByFolderId(Long folderId, String types) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Authority", "t_entity_" + folderId.toString());
        map.put("types", types);
        List<Long> entityIds = getSqlSession().selectList(getNameSpace("selectEntityIdsByFolderId"), map);
        return entityIds;
    }

    @Override
    public List<Long> getAuthFolderIds(List<Long> folderIds) {
        return getSqlSession().selectList(getNameSpace("getAuthFolderIds"), folderIds);
    }

}
