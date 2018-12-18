/***********************************************************************
 * $Id: VersionDaoImpl.java,v1.0 2012-11-28 下午2:39:39 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.version.domain.Version;
import com.topvision.platform.dao.VersionControlDao;

/**
 * @author dengl
 * @created @2012-11-28-下午2:39:39
 *
 */
@Repository("versionControlDao")
public class VersionControlDaoImpl extends MyBatisDaoSupport<Version> implements VersionControlDao {
    @Override
    public List<Version> queryVersion() {
        return getSqlSession().selectList(getNameSpace("queryVersion"));
    }

    @Override
    public String getDomainName() {
        return Version.class.getName();
    }
}
