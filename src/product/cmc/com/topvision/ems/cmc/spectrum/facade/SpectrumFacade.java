/***********************************************************************
 * $Id: SpectrumFacade.java,v1.0 2014-1-13 上午9:21:45 $
 *
 * @author: haojie
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.facade;

import java.util.List;

import com.topvision.ems.facade.Facade;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumCfg;
import com.topvision.ems.cmc.spectrum.facade.domain.SpectrumOltSwitch;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2014-1-13-上午9:21:45
 *
 */
@EngineFacade(serviceName = "SpectrumFacade", beanName = "spectrumFacade")
public interface SpectrumFacade extends Facade {

    /**
     * CMTS频谱参数配置
     * @param spectrumCfg
     */
    void spectrumCfg(SnmpParam snmpParam, SpectrumCfg spectrumCfg);

    /**
     * 获取OLT频谱开关
     * @param snmpParam
     * @return
     */
    boolean getOltSwitchStatus(SnmpParam snmpParam);

    /**
     * 获取CMTS频谱开关
     * @param snmpParam
     * @return
     */
    boolean getCmcSwitchStatus(SnmpParam snmpParam, String cmcMac);

    /**
     * 刷新CMTS频谱参数
     * @param snmpParam
     * @return
     */
    List<SpectrumCfg> getSpectrumCfg(SnmpParam snmpParam);

    /**
     * 开启OLT频谱开关
     * @param snmpParam
     */
    void startSpectrumSwitchOlt(SnmpParam snmpParam);

    /**
     * 关闭OLT频谱开关
     * @param snmpParam
     */
    void stopSpectrumSwitchOlt(SnmpParam snmpParam);

    /**
     * 获取olt的频谱开关
     * @param snmpParam
     * @return
     */
    SpectrumOltSwitch getSpectrumOltSwitch(SnmpParam snmpParam);
}
