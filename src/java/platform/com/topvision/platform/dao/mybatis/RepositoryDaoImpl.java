/***********************************************************************
 * $Id: RepositoryDaoImpl.java,v 1.1 Jun 28, 2008 11:56:34 AM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao.mybatis;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.RepositoryDao;
import com.topvision.platform.domain.Repository;

/**
 * @author kelers
 * @Create Date Jun 28, 2008 11:56:34 AM
 */
@org.springframework.stereotype.Repository("repositoryDao")
public class RepositoryDaoImpl extends MyBatisDaoSupport<Repository> implements RepositoryDao {
    @Override
    public String getDomainName() {
        return Repository.class.getName();
    }
}
