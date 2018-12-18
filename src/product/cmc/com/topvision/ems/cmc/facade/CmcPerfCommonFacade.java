/***********************************************************************
 * $Id: CmcPerfCommonFacade.java,v1.0 2013-12-2 上午09:55:30 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade;

import java.util.List;

import com.topvision.ems.cmc.performance.facade.CmcServiceQuality;
import com.topvision.ems.cmc.performance.facade.CmcSignalQuality;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2013-12-2-上午09:55:30
 * 
 */
@EngineFacade(serviceName = "CmcPerfCommonFacade", beanName = "cmcPerfCommonFacade")
public interface CmcPerfCommonFacade extends Facade {

    /**
     * 从设备获得最新服务质量采集记录
     * 
     * @param snmpParam
     * @param cmcIndex
     * @return
     */
    CmcServiceQuality fetchCmcServiceQuality(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 从设备获得最新信号质量采集记录
     * 
     * @param snmpParam
     * @param cmcIndex
     * @param channelIndexs
     * @return
     */
    List<CmcSignalQuality> fetchCmcSignalQuality(SnmpParam snmpParam, Long cmcIndex, List<Long> channelIndexs);

    /**
     * 从设备获得最新信号质量采集记录
     * 
     * @param snmpParam
     * @param cmcIndex
     * @param channelIndex
     * @return
     */
    CmcSignalQuality fetchCmcSignalQuality(SnmpParam snmpParam, Long cmcIndex, Long channelIndex);

}
