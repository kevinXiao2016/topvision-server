/***********************************************************************
 * $Id: UpgradeParamService.java,v1.0 2014年9月23日 下午2:44:53 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.service;

import com.topvision.ems.upgrade.domain.UpgradeGlobalParam;
import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2014年9月23日-下午2:44:53
 * 
 */
public interface UpgradeParamService extends Service {

    /**
     * 获取升级全局配置参数
     * 
     * @return
     */
    UpgradeGlobalParam getUpgradeGlobalParam();

    /**
     * 修改升级全局配置参数
     * 
     * @param param
     */
    void modifyUpgradeGlobalParam(UpgradeGlobalParam param);
}
