/***********************************************************************
 * $Id: CmcDocsisFacade.java,v1.0 2013-4-26 下午9:59:46 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.docsis.facade;

import java.util.List;

import com.topvision.ems.cmc.docsis.facade.domain.CmcDocsisConfig;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-4-26-下午9:59:46
 *
 */
@EngineFacade(serviceName = "CmcDocsisFacade", beanName = "cmcDocsisFacade")
public interface CmcDocsisFacade extends Facade {

    /**
     * 将配置的Docsis MDD/MDF更新到设备上
     * 
     * @param snmpParam
     * @param cmcDocsis
     * @return
     */
    public CmcDocsisConfig updateDocsisToFacility(SnmpParam snmpParam, CmcDocsisConfig cmcDocsis);

    /**
     * 从设备上获取Docsis MDD/MDF配置
     * 
     * @param snmpParam
     * @param cmcIndex
     * @return
     */
    public CmcDocsisConfig getDocsisFromFacility(SnmpParam snmpParam, Long cmcIndex);

    public List<CmcDocsisConfig> getDocsisconfigList(SnmpParam snmpParam);

}
