/***********************************************************************
 * $Id: CmcFacade.java,v1.0 2011-7-1 下午02:43:48 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcDevSoftware;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcReplaceEntry;
import com.topvision.ems.cmc.ccmts.facade.domain.CmcSysControl;
import com.topvision.ems.cmc.facade.domain.CmcSystemIpInfo;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-7-1-下午02:43:48
 * 
 */
@EngineFacade(serviceName = "CmcFacade", beanName = "ccmtsCmcFacade")
public interface CmcFacade extends Facade {
    /**
     * 重启CCMTS
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcSysControl
     *            CmcSysControl
     * @return CmcSysControl
     */
    public CmcSysControl cmcSysControlSetWithotAgent(SnmpParam snmpParam, CmcSysControl cmcSysControl);

    /**
     * 升级CC
     * 
     * @param snmpParam
     *            SnmpParam
     * @param cmcDevSoftware
     *            CmcDevSoftware
     * @return CmcDevSoftware
     */
    CmcDevSoftware updateCmc(SnmpParam snmpParam, CmcDevSoftware cmcDevSoftware);

    /**
     * 获取设备（拆分型CC）的升级进度
     * 
     * @param snmpParam
     *            SnmpParam
     * @return Integer
     */
    Integer getCmcUpdateProgress(SnmpParam snmpParam);

    /**
     * 获取设备上信道利用率采集间隔
     * 
     * @param snmpParam
     *            SnmpParam
     * @return String
     */
    String getChannelUtilizationInteInterval(SnmpParam snmpParam);

    public void setChannelUtilizationInteInterval(SnmpParam dolSnmpParam, Long period);

    /**
     * 获取CC网络信息
     * @param snmpParam
     * @return
     */
    public CmcSystemIpInfo getCmcSystemIpInfo(SnmpParam snmpParam);

    /**
     * Replace CMC
     * 
     * @param snmpParam
     * @param cmcReplaceEntry
     */
    void replaceCmcEntry(SnmpParam snmpParam, CmcReplaceEntry cmcReplaceEntry);

    public void clearCmcFlap(SnmpParam snmpParam, Long cmcIndex);

    void clearCmcAllCmFlap(Long cmcId);

}
