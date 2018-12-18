/***********************************************************************
 * $Id: CcmtsMaintainDaoImpl.java,v1.0 2015-5-28 上午11:00:26 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.ccmtsmaintain.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cm.ccmtsmaintain.dao.CcmtsMaintainDao;
import com.topvision.ems.cm.ccmtsmaintain.domain.CcmtsMaintain;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author fanzidong
 * @created @2015-5-28-上午11:00:26
 *
 */
@Repository("ccmtsMaintainDao")
public class CcmtsMaintainDaoImpl extends MyBatisDaoSupport<CcmtsMaintain> implements CcmtsMaintainDao {

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.ccmtsmaintain.dao.CcmtsMaintainDao#batchInsertMaintainData(java.util.List)
     */
    @Override
    public void batchInsertMaintainData(List<CcmtsMaintain> ccmtsMaintains) {
        //首先清空该表数据
        getSqlSession().delete("emptyMaintain");
        //批量插入本次采集数据
        SqlSession batchSession = getBatchSession();
        try {
            for (CcmtsMaintain ccmtsMaintain : ccmtsMaintains) {
                batchSession.insert(getNameSpace("insertCcmtsMaintain"), ccmtsMaintain);
            }
            batchSession.commit();
        } catch (Exception e) {
            logger.error("batch insert ccmtsMaintain error:", e);
            batchSession.rollback();
        } finally {
            batchSession.close();
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.ccmtsmaintain.domain.CcmtsMaintain";
    }

}
