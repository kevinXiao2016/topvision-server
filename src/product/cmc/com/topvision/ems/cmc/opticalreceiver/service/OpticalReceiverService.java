/***********************************************************************
 * $Id: OpticalReceiverService.java,v1.0 2016年9月13日 下午3:14:22 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.service;

import com.topvision.ems.cmc.opticalreceiver.domain.OpticalReceiverData;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwUpg;
import com.topvision.framework.service.Service;

/**
 * @author haojie
 * @created @2016年9月13日-下午3:14:22
 *
 */
public interface OpticalReceiverService extends Service {

    /**
     * 获取光机信息
     * 
     * @param cmcId
     * @return
     */
    OpticalReceiverData getOpticalReceiverInfo(Long cmcId);

    /**
     * 光机配置
     * 
     * @param opticalReceiverData
     */
    void modifyOpticalReceiver(OpticalReceiverData opticalReceiverData);

    /**
     * 光机恢复出厂设置，仅支持CFD/FFA/FFB
     * 
     * @param cmcId
     */
    void restorFactory(Long cmcId);

    /**
     * 刷新光机信息
     * 
     * @param cmcId
     */
    void refreshOpticalReceiver(Long cmcId);

    /**
     * 光机升级
     * 
     * @param topCcmtsDorFirmWare
     */
    void upgradeOpticalReceiver(TopCcmtsDorFwUpg topCcmtsDorFirmWare);

    /**
     * 获取光机升级进度
     * 
     * @param topCcmtsDorFirmWare
     * @return
     */
    Integer getUpdateProgress(Long entityId);

}
