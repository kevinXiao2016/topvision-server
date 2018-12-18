/***********************************************************************
 * $Id: CmcOpticalReceiverService.java,v1.0 2013-12-2 下午2:32:09 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.service;

import java.util.List;

import com.topvision.ems.cmc.optical.domain.CmcOpReceiverCfg;
import com.topvision.ems.cmc.optical.domain.CmcOpReceiverStatus;
import com.topvision.framework.service.Service;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author dosion
 * @created @2013-12-2-下午2:32:09
 * 
 */
public interface CmcOpticalReceiverService extends Service {
    /**
     * 获取光机运行状态(数据库信息)
     * 
     * @param cmcId
     * @return
     */
    CmcOpReceiverStatus getOpticalReceiverStatus(Long cmcId);

    /**
     * 获取光机配置参数
     * 
     * @param cmcId
     * @return
     */
    CmcOpReceiverCfg getOpticalReceiverCfg(Long cmcId);

    /**
     * 修改光机配置参数
     * 
     * @param cmcId
     * @param cmcOpReceiverCfg
     */
    void modifyOpReceiverCfg(Long cmcId, CmcOpReceiverCfg cmcOpReceiverCfg);

    /**
     * 刷新光机信息（从设备）
     * 
     * @param cmcId
     */
    void refreshOpReceiverInfo(Long cmcId);

    /**
     * 获取光机接收功率数据
     * 
     * @param cmcId
     * @param startTime
     * @param endTime
     * @return
     */
    List<Point> readOpticalReceivedPowerData(Long cmcId, Long index, String startTime, String endTime);

    /**
     * 获取CC是否支持光机功能
     * 
     * @param cmcId
     * @return
     */
    Boolean isSupportOpticalReceiver(Long cmcId);

}
