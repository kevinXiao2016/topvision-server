/***********************************************************************
 * $Id: EngineDaoSupport.java,v1.0 2015年3月7日 下午5:09:11 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.engine.dao;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.topvision.ems.engine.launcher.CollectorContext;
import com.topvision.framework.dao.Dao;

/**
 * @author Victor
 * @created @2015年3月7日-下午5:09:11
 *
 */
public abstract class EngineDaoSupport<T> extends SqlSessionDaoSupport implements Dao {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param sqlSessionTemplate
     *            the sqlSessionTemplate to set
     */
    @Override
    @Autowired
    @Qualifier("engineSqlSessionTemplate")
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        super.setSqlSessionTemplate(sqlSessionTemplate);
    }

    @Override
    public SqlSession getSqlSession() {
        if (super.getSqlSession() == null) {
            super.setSqlSessionTemplate(CollectorContext.getInstance().getSqlSessionTemplate());
        }
        return super.getSqlSession();
    }

    /**
     * 获得MyBatis的批量操作session
     * 
     * 注意此种方式属于程序控制事务 不再托管给Spring进行事务管理
     * 
     * 在使用session时，需要使用try catch finally 使用示例如下 SqlSession session = getBatchSession(); try{
     * doBatch(); session.commit(); }catch(*Exception e){ logger.error("", e); session.rollback();
     * }finally{ session.close(): }
     * 
     * @return
     */
    protected SqlSession getBatchSession() {
        SqlSessionTemplate sessionTemplate = (SqlSessionTemplate) getSqlSession();
        return sessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
    }

    /**
     * 
     * @param name
     * @return
     */
    protected String getNameSpace(String name) {
        return new StringBuilder(getDomainName()).append('.').append(name).toString();
    }

    protected String getNameSpace() {
        return new StringBuilder(getDomainName()).append('.').toString();
    }

    /**
     * @return
     */
    protected abstract String getDomainName();
}
