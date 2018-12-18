/***********************************************************************
 * $Id: OnuCpeLocationService.java,v1.0 2016-5-6 上午11:23:25 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.cpelocation.service;

import com.topvision.ems.epon.cpelocation.domain.OnuCpeLocation;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2016-5-6-上午11:23:25
 *
 */
public interface OnuCpeLocationService extends Service {

    /**
     * 获取ONU CPE定位信息
     * @param cpeMac
     * @return
     */
    OnuCpeLocation getOnuCpeLocation(String cpeMac);

}
