/***********************************************************************
 * $Id: OnuSlaDaoImpl.java,v1.0 2015-8-7 下午4:55:41 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.qos.dao.OnuSlaDao;
import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Administrator
 * @created @2015-8-7-下午4:55:41
 *
 */
@Repository("onuSlaDao")
public class OnuSlaDaoImpl extends MyBatisDaoSupport<BaseEntity> implements OnuSlaDao {

    @Override
    public void saveSlaTable(List<SlaTable> slaTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (SlaTable slaTable : slaTables) {
                sqlSession.delete(getNameSpace() + "deleteSlaTable", slaTable);
                sqlSession.insert(getNameSpace() + "insertSlaTable", slaTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.qos.domain.OnuSla";
    }

}
