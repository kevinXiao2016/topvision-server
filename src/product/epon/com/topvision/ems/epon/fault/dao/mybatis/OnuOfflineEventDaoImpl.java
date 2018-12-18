/***********************************************************************
 * $Id: OnuOfflineEventDaoImpl.java,v1.0 2016年6月20日 上午10:35:10 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.fault.dao.OnuOfflineEventDao;
import com.topvision.ems.epon.fault.domain.OnuOfflineAlarm;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2016年6月20日-上午10:35:10
 *
 */
@Repository
public class OnuOfflineEventDaoImpl extends MyBatisDaoSupport<Object> implements OnuOfflineEventDao {

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.fault.dao.OnuOfflineEventDao#insertOnuOfflineEvent(com.topvision.ems.epon.fault.domain.OnuOfflineAlarm)
     */
    @Override
    public void insertOnuOfflineEvent(OnuOfflineAlarm alarm) {
        getSqlSession().insert("insertOnuOfflineAlarm", alarm);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return OnuOfflineAlarm.class.getName();
    }

}
