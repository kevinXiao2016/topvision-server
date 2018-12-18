/***********************************************************************
 * $Id: CFBOpticalReceiverConfigure.java,v1.0 2016年11月11日 下午4:49:43 $
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
 * @created @2016年11月11日-下午4:49:43
 *
 */
public class CFBOpticalReceiverConfigure extends OpticalReceiverConfigure {

    public CFBOpticalReceiverConfigure(OpticalReceiverData opticalReceiverData, SnmpParam snmpParam,
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
    }

}
