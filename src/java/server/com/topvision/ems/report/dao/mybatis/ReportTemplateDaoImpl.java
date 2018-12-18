/***********************************************************************
 * $Id: ReportTemplateDaoImpl.java,v1.0 2013-10-11 下午2:29:11 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.report.dao.ReportTemplateDao;
import com.topvision.ems.report.domain.ReportTemplate;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author fanzidong
 * @created @2013-10-11-下午2:29:11
 * 
 */
@Repository("reportTemplateDao")
public class ReportTemplateDaoImpl extends MyBatisDaoSupport<ReportTemplate> implements ReportTemplateDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.report.domain.ReportTemplate";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.report.dao.ReportTemplateDao#getReportTemplate()
     */
    @Override
    public List<ReportTemplate> getReportTemplate() {
        return getSqlSession().selectList(getNameSpace("getReportTemplate"));
    }

    @Override
    public void updateReportDisplay(List<ReportTemplate> reportTemplates) {
        // 新获取一个模式为BATCH，自动提交为false的session
        SqlSession session = getBatchSession();
        try {
            for (ReportTemplate reportTemplate : reportTemplates) {
                session.update(getNameSpace("updateOneReportDisplay"), reportTemplate);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }
}
