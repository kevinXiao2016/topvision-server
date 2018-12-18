/***********************************************************************
 * $Id: OnuCpeLocatonDao.java,v1.0 2016-5-6 上午9:43:43 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.cpelocation.dao;

import com.topvision.ems.epon.cpelocation.domain.OnuCpeLocation;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2016-5-6-上午9:43:43
 *
 */
public interface OnuCpeLocatonDao extends BaseEntityDao<Object> {

    /**
     * 插入或者更新ONU CPE定位信息
     * @param cpeLoc
     */
    void insertOrUpdateCpeLoc(OnuCpeLocation cpeLoc);

    /**
     * 根据CPE MAC获取定位信息
     * @param cpeMac
     * @return
     */
    OnuCpeLocation queryOnuCpeLoc(String cpeMac);

    /**
     * 获取ONU CPE关联设备信息
     * 包括OLT信息和ONU信息
     * @param cpeLoc
     * @return
     */
    OnuCpeLocation getOnuCpeRelaInfo(OnuCpeLocation cpeLoc);

}
