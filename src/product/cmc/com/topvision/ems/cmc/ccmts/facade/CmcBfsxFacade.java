/***********************************************************************
 * $Id: CmcBfsxFacade.java,v1.0 2014年9月23日 上午10:53:53 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade;

import java.util.List;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcBfsxSnapInfo;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2014年9月23日-上午10:53:53
 *
 */
@EngineFacade(serviceName = "cmcBfsxFacade", beanName = "cmcBfsxFacade")
public interface CmcBfsxFacade extends Facade {

    /**
     * 获取CCMTS的CPU,MEM,FLASH,SYSUPTIME等实时信息
     * @param snmpParam
     * @return
     */
    List<CmcBfsxSnapInfo> getCmcBfsxSnapInfo(SnmpParam snmpParam);

    /**
     * 获取CC8800A的基本信息
     * @param snmpParam
     * @param baseInfo
     * @return
     */
    CmcBfsxSnapInfo getCC8800ABaseInfo(SnmpParam snmpParam, CmcBfsxSnapInfo baseInfo);

    /**
     * 获取CMC(类B型)设备系统运行时长
     * @param snmpParam
     * @return
     */
    String getCmcSysUpTime(SnmpParam snmpParam);
}
