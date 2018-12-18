/***********************************************************************
 * $Id: CmcOpticalReceiverFacade.java,v1.0 2013-12-2 下午2:15:56 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.optical.facade;

import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverAcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverChannelNum;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverDcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverInputInfo;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfPort;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverSwitchCfg;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverType;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * 获取、设置光机信息
 * 
 * @author dosion
 * @created @2013-12-2-下午2:15:56
 * 
 */
@EngineFacade(serviceName = "CmcOpticalReceiverFacade", beanName = "cmcOpticalReceiverFacade")
public interface CmcOpticalReceiverFacade extends Facade {
    /**
     * 获取光机RF配置
     * 
     * @param snmpParam
     * @param index
     * @return
     */
    CmcOpReceiverRfCfg getOpReceiverRfCfg(SnmpParam snmpParam, Integer index);

    /**
     * 获取光机开关配置
     * 
     * @param snmpParam
     * @param index
     * @return
     */
    CmcOpReceiverSwitchCfg getOpReceiverSwitchCfg(SnmpParam snmpParam, Integer index);

    /**
     * 获取光机接收光功率信息
     * 
     * @param snmpParam
     * @param index
     * @return
     */
    CmcOpReceiverInputInfo getOpReceiverInputInfo(SnmpParam snmpParam, Integer index);

    /**
     * 获取光机交流电源信息
     * 
     * @param snmpParam
     * @param index
     * @return
     */
    CmcOpReceiverAcPower getOpReceiverAcPower(SnmpParam snmpParam, Integer index);

    /**
     * 获取光机直流电源信息
     * 
     * @param snmpParam
     * @param index
     * @return
     */
    CmcOpReceiverDcPower getOpReceiverDcPower(SnmpParam snmpParam, Integer index);

    /**
     * 修改光机RF配置
     * 
     * @param snmpParam
     * @param cmcOpReceiverRfCfg
     * @return
     */
    CmcOpReceiverRfCfg setOpReceiverRfCfg(SnmpParam snmpParam, CmcOpReceiverRfCfg cmcOpReceiverRfCfg);

    /**
     * 修改光机开关配置
     * 
     * @param snmpParam
     * @param cmcOpReceiverSwitchCfg
     * @return
     */
    CmcOpReceiverSwitchCfg setOpReceiverSwitchCfg(SnmpParam snmpParam, CmcOpReceiverSwitchCfg cmcOpReceiverSwitchCfg);

    /**
     * 获取光机射频输出功率
     * 
     * @param snmpParam
     * @param index
     * @return
     */
    CmcOpReceiverRfPort getOpReceiverRfPort(SnmpParam snmpParam, Integer index);

    /**
     * 获取光机的类型信息
     * 
     * @param snmpParam
     * @param index
     * @return
     */
    CmcOpReceiverType getOpReceiverType(SnmpParam snmpParam, Integer index);

    CmcOpReceiverChannelNum getOpReceiverChannelNum(SnmpParam snmpParam, Integer index);
}
