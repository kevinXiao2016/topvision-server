/***********************************************************************
 * $Id: OnuOfflineEventDao.java,v1.0 2016年6月20日 上午10:34:04 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.dao;

import com.topvision.ems.epon.fault.domain.OnuOfflineAlarm;
import com.topvision.framework.dao.Dao;

/**
 * @author Bravin
 * @created @2016年6月20日-上午10:34:04
 *
 */
public interface OnuOfflineEventDao extends Dao {

    /**
     * @param alarm
     */
    void insertOnuOfflineEvent(OnuOfflineAlarm alarm);

}
