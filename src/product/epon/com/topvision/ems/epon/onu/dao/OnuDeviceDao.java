/***********************************************************************
 * $Id: OnuPortalDao.java,v1.0 2015年4月21日 下午4:14:48 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OnuDeviceInfo;

/**
 * @author Administrator
 * @created @2015年4月21日-下午4:14:48
 *
 */
public interface OnuDeviceDao {

    /**
     * 获取Onu设备信息列表
     * 
     * @param queryMap
     * @return
     */
    List<OnuDeviceInfo> selectOnuDeviceList(Map<String, Object> queryMap);

    /**
     * 查询ONU设备数量
     * 
     * @param queryMap
     * @return
     */
    Integer selectOnuDeviceCount(Map<String, Object> queryMap);

}
