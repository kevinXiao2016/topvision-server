/***********************************************************************
 * $Id: OnuPortalServiceImpl.java,v1.0 2015年4月21日 下午4:11:00 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onu.dao.OnuDeviceDao;
import com.topvision.ems.epon.onu.domain.OnuDeviceInfo;
import com.topvision.ems.epon.onu.service.OnuDeviceService;
import com.topvision.framework.service.BaseService;

/**
 * @author Administrator
 * @created @2015年4月21日-下午4:11:00
 *
 */
@Service
public class OnuDeviceServiceImpl extends BaseService implements OnuDeviceService {

	@Autowired
    private OnuDeviceDao onuDeviceDao;

    @Override
    public List<OnuDeviceInfo> queryOnuDeviceList(Map<String, Object> queryMap) {
        return onuDeviceDao.selectOnuDeviceList(queryMap);
    }

    @Override
    public Integer queryOnuDeviceCount(Map<String, Object> queryMap) {
        return onuDeviceDao.selectOnuDeviceCount(queryMap);
    }

}
