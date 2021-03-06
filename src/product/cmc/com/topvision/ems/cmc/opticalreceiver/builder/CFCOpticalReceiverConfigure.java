/***********************************************************************
 * $Id: CFCOpticalReceiverConfigure.java,v1.0 2016年11月11日 下午4:50:20 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.builder;

import com.topvision.ems.cmc.opticalreceiver.dao.OpticalReceiverRefreshDao;
import com.topvision.ems.cmc.opticalreceiver.domain.OpticalReceiverData;
import com.topvision.ems.cmc.opticalreceiver.facade.OpticalReceiverFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author vanzand
 * @created @2016年11月11日-下午4:50:20
 *
 */
public class CFCOpticalReceiverConfigure extends OpticalReceiverConfigure {

    public CFCOpticalReceiverConfigure(OpticalReceiverData opticalReceiverData, SnmpParam snmpParam,
            OpticalReceiverFacade facade, OpticalReceiverRefreshDao opticalReceiverRefreshDao) {
        super(opticalReceiverData, snmpParam, facade, opticalReceiverRefreshDao);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.opticalreceiver.builder.OpticalReceiverConfigure#modifyConfig()
     */
    @Override
    public void modifyConfig() {
        // 配置正向光发射频衰减/正向光收模块状态
        modifyOpRxOutput();
        // 配置正向混合信号主路均衡/配置正向混合信号主路衰减/配置DOCSIS US信号衰减/配置反向光路射频衰减/配置反向混合信号主路均衡/配置反向光发激光器工作状态
        modifyDevParams();
    }

}
