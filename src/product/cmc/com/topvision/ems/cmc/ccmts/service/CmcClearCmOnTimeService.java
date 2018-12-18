/***********************************************************************
 * $Id: CmcClearCmOnTimeService.java,v1.0 2017年5月20日 下午4:53:53 $
 * 
 * @author: ls
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service;

import com.topvision.framework.service.Service;

/**
 * @author ls
 * @created @2017年5月20日-下午4:53:53
 *
 */
public interface CmcClearCmOnTimeService extends Service {

    /**
     * 获取数据库中保存的上次设置的时间
     * @param
     * @return int
     */
    int getCmcClearTime(Long cmcId);

    /**
     * 设置清除时间
     * @param
     * @return void
     */
    void setClearTimeOnCC(Integer time, Long cmcId);
    /**
     * 保存设置时间
     * @param
     * @return void
     */
    void saveTimeOfCC(Integer time,Long cmcId);
}
