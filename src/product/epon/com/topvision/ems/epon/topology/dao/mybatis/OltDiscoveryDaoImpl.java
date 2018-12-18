/***********************************************************************
 * $Id: OltDiscoveryDaoImpl.java,v1.0 2015-8-18 下午1:59:04 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.topology.dao.OltDiscoveryDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Administrator
 * @created @2015-8-18-下午1:59:04
 *
 */
@Repository("oltDiscoveryDao")
public class OltDiscoveryDaoImpl extends MyBatisDaoSupport<OltAttribute> implements OltDiscoveryDao {


    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        // TODO Auto-generated method stub
        return null;
    }

}