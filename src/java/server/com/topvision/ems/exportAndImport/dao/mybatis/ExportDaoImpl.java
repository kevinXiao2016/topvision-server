/***********************************************************************
 * $Id: ExportDaoImpl.java,v1.0 2015-7-29 上午9:18:58 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.exportAndImport.dao.ExportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author fanzidong
 * @created @2015-7-29-上午9:18:58
 * 
 */
@Repository("exportDao")
public class ExportDaoImpl extends MyBatisDaoSupport<Object> implements ExportDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.exportAndImport.dao.ExportDao#getAllEntityAlias()
     */
    @Override
    public List<Entity> getAllEntityAlias() {
        return getSqlSession().selectList(getNameSpace("getAllEntityAlias"));
    }
    
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.exportAndImport.dao.mybatis.ExportDaoImpl";
    }

}
