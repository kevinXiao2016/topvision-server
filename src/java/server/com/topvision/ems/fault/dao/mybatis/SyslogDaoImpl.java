/***********************************************************************
 * $Id: SyslogDaoImpl.java,v1.0 2013-4-21 下午02:38:35 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.fault.dao.SyslogDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.syslog.Syslog;

/**
 * @author Rod John
 * @created @2013-4-21-下午02:38:35
 * 
 */

@Repository("syslogDao")
public class SyslogDaoImpl extends MyBatisDaoSupport<Syslog> implements SyslogDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.fault.domain.Syslog";
    }

}
