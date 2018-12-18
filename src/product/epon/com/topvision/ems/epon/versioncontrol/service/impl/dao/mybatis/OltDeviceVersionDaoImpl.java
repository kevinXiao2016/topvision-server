/***********************************************************************
 * $Id: OltDeviceVersionDaoImpl.java,v1.0 2017年10月11日 下午4:07:40 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.versioncontrol.service.impl.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.versioncontrol.service.impl.dao.OltDeviceVersionDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author vanzand
 * @created @2017年10月11日-下午4:07:40
 *
 */
@Repository("oltDeviceVersionDao")
public class OltDeviceVersionDaoImpl extends MyBatisDaoSupport<Object> implements OltDeviceVersionDao {


    /* (non-Javadoc)
     * @see com.topvision.ems.epon.versioncontrol.service.impl.dao.OltDeviceVersionDao#getDeviceVersion(java.lang.Long)
     */
    @Override
    public String getDeviceVersion(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getDeviceVersion"), entityId);
    }

    @Override
    protected String getDomainName() {
        return this.getClass().getName();
    }

}
