/***********************************************************************
 * $Id: ReportCoreDaoImpl.java,v1.0 2014-6-18 下午4:20:57 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.report.dao.ReportCoreDao;
import com.topvision.ems.report.domain.EntityPair;
import com.topvision.ems.report.domain.Report;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Administrator
 * @created @2014-6-18-下午4:20:57
 * 
 */
@Repository("reportCoreDao")
public class ReportCoreDaoImpl extends MyBatisDaoSupport<Report> implements ReportCoreDao {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.report.dao.ReportCoreDao#fetchCoreData(com.topvision.ems.report.domain.
     * Report, java.util.Map)
     */
    @Override
    public List<Object> fetchCoreData(Report report, Map<String, String> query) {
        return null;
    }

    @Override
    public List<TopoFolder> queryUserFolderList(Map<String, String> queryMap) {
        return getSqlSession().selectList(getNameSpace() + "queryUserFolderList", queryMap);
    }

    @Override
    public List<EntityPair> queryOltByFolderId(Long folderId) {
        if (folderId.equals(-1L)) {
            return getSqlSession().selectList(getNameSpace() + "queryAllOlt");
        }
        return getSqlSession().selectList(getNameSpace() + "queryOltByFolderId", folderId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.report.dao.mybatis.ReportCoreDaoImpl";
    }

}
