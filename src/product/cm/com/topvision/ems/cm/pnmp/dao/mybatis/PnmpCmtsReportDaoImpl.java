/***********************************************************************
 * $Id: PnmpCmtsReportDaoImpl.java,v1.0 2017年8月8日 下午4:12:28 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cm.pnmp.dao.PnmpCmtsReportDao;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmtsReport;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:12:28
 *
 */
@Repository("pnmpCmtsReportDao")
public class PnmpCmtsReportDaoImpl extends MyBatisDaoSupport<Object> implements PnmpCmtsReportDao {

    @Override
    public List<PnmpCmtsReport> selectReports() {
        return getSqlSession().selectList(getNameSpace() + "selectCmtsReports");
    }

    @Override
    public void insertOrUpdateReport(PnmpCmtsReport cmtsReport) {
        getSqlSession().update(getNameSpace() + "insertOrUpdateCmtsReport", cmtsReport);
    }

    @Override
    public Integer selectCmtsReportsNums() {
        return getSqlSession().selectOne(getNameSpace() + "selectCmtsReportsNums");
    }

    @Override
    public Integer selectOnlineCmNums() {
        return getSqlSession().selectOne(getNameSpace() + "selectOnlineCmNums");
    }

    @Override
    public Integer selectHealthCmNums() {
        return getSqlSession().selectOne(getNameSpace() + "selectHealthCmNums");
    }

    @Override
    public Integer selectMarginalCmNums() {
        return getSqlSession().selectOne(getNameSpace() + "selectMarginalCmNums");
    }

    @Override
    public Integer selectBadCmNums() {
        return getSqlSession().selectOne(getNameSpace() + "selectBadCmNums");
    }

    @Override
    public void summaryCmtsReport() {
        getSqlSession().update(getNameSpace() + "summaryCmtsReport");
    }

    @Override
    public PnmpCmtsReport selectCmtsReportByMap(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace() + "selectCmtsReportByMap", queryMap);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.pnmp.facade.domain.PnmpCmtsReport";
    }

}
