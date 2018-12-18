/***********************************************************************
 * $Id: CmHistoryDaoImpl.java,v1.0 2015年4月9日 下午8:19:46 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmhistory.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cm.cmhistory.dao.CmHistoryDao;
import com.topvision.ems.cm.cmhistory.domain.CmHistoryShow;
import com.topvision.ems.cm.cmhistory.engine.domain.CmHistory;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author YangYi
 * @created @2015年4月9日-下午8:19:46
 * 
 */
@Repository("cmHistoryDao")
public class CmHistoryDaoImpl extends MyBatisDaoSupport<CmHistoryShow> implements CmHistoryDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.cmhistory.domain.CmHistoryShow";
    }

    @Override
    public List<CmHistoryShow> getCmHistory(Map<String, Object> queryMap) {
        List<CmHistoryShow> list = getSqlSession().selectList(getNameSpace("selectCmHistoryList"), queryMap);
        return list;
    }

    // TODO 刪除 批量插入数据
    @Override
    public void batchInsertCmHistory(List<CmHistory> CmHistoryList) {
        SqlSession batchSession = getBatchSession();
        try {
            for (CmHistory cmHistory : CmHistoryList) {
                batchSession.insert(getNameSpace("insertCmHistory"), cmHistory);
            }
            batchSession.commit();
        } catch (Exception e) {
            logger.error("batch insert cmHistory error:", e);
            batchSession.rollback();
        } finally {
            batchSession.close();
        }
    }
}
