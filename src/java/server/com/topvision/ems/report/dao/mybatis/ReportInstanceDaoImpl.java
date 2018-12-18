/***********************************************************************
 * $Id: ReportInstanceDaoImpl.java,v1.0 2013-10-11 上午10:18:14 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.report.dao.ReportInstanceDao;
import com.topvision.ems.report.domain.ReportInstance;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author fanzidong
 * @created @2013-10-11-上午10:18:14
 * 
 */
@Repository("reportInstanceDao")
public class ReportInstanceDaoImpl extends MyBatisDaoSupport<Entity> implements ReportInstanceDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.report.domain.ReportInstance";
    }

    @Override
    public void insertReportInstance(ReportInstance report) {
        getSqlSession().insert(getNameSpace("insertReportInstance"), report);
    }

    @Override
    public void deleteReport(List<Long> reportIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("reportIds", reportIds);
        getSqlSession().delete(getNameSpace("deleteReport"), map);
    }

    @Override
    public ReportInstance getReportDetailByReportId(Long reportId) {
        return getSqlSession().selectOne(getNameSpace("getReportDetailByReportId"), reportId);
    }

    @Override
    public List<ReportInstance> getReportInstanceList(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("getReportInstanceList"), queryMap);
    }

    @Override
    public Integer getReportInstanceNum(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace("getReportInstanceNum"), queryMap);
    }
}
