/***********************************************************************
 * $Id: OnuPortalService.java,v1.0 2015年4月21日 下午4:10:50 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onu.domain.OnuDeviceInfo;
import com.topvision.framework.service.Service;

/**
 * @author Administrator
 * @created @2015年4月21日-下午4:10:50
 *
 */
public interface OnuDeviceService extends Service {

    /**
     * 查询ONU设备列表
     * 
     * @param queryMap
     * @return
     */
    List<OnuDeviceInfo> queryOnuDeviceList(Map<String, Object> queryMap);

    /**
     * 查询ONU设备数量
     * 
     * @param queryMap
     * @return
     */
    Integer queryOnuDeviceCount(Map<String, Object> queryMap);

}
