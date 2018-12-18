/***********************************************************************
 * $Id: CmtsInfoSummaryMaintainDaoImpl.java,v1.0 2017年9月12日 下午4:44:34 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmtsInfoSummary.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cm.ccmtsmaintain.domain.CcmtsMaintain;
import com.topvision.ems.cm.cmtsInfoSummary.dao.CmtsInfoSummaryMaintainDao;
import com.topvision.ems.cm.cmtsInfoSummary.domain.CmtsInfoMaintain;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author admin
 * @created @2017年9月12日-下午4:44:34
 *
 */
@Repository("cmtsInfoSummaryMaintainDao")
public class CmtsInfoSummaryMaintainDaoImpl extends MyBatisDaoSupport<CmtsInfoMaintain> implements CmtsInfoSummaryMaintainDao{

    @Override
    public void batchInsertCmtsInfoMaintainLast(List<CmtsInfoMaintain> cmtsInfoMaintain) {
      //首先清空该表数据
        getSqlSession().delete("emptyCmtsMaintain");
        //批量插入本次采集数据
        SqlSession batchSession = getBatchSession();
        try {
            for (CmtsInfoMaintain cm : cmtsInfoMaintain) {
                batchSession.insert(getNameSpace("insertCmtsInfoMaintainlast"), cm);
            }
            batchSession.commit();
        } catch (Exception e) {
            logger.error("batch insert cmtsInfo error:", e);
            batchSession.rollback();
        } finally {
            batchSession.close();
        }
    }

    @Override
    public void batchInsertCmtsInfoMaintain(List<CmtsInfoMaintain> cmtsInfoMaintain) {
        SqlSession batchSession = getBatchSession();
        try {
            for (CmtsInfoMaintain cm : cmtsInfoMaintain) {
                batchSession.insert(getNameSpace("insertCmtsInfoMaintain"), cm);
            }
            batchSession.commit();
        } catch (Exception e) {
            logger.error("batch insert cmtsInfo error:", e);
            batchSession.rollback();
        } finally {
            batchSession.close();
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.cmtsInfoSummary.domain.CmtsInfoMaintain";
    }

}
